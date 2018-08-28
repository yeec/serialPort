package bros.manage.telegraph;

import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import bros.manage.business.view.LocalBoard;
import bros.manage.main.MainWindow;
import bros.manage.util.DataBaseUtil;
import bros.manage.util.DateUtil;
import bros.manage.util.PropertiesUtil;

/**
 * 接收电报监听器
 * 
 * @author wyc
 *
 */
public class SerialListener extends Thread implements SerialPortEventListener {

	private static final Log logger = LogFactory.getLog(SerialListener.class);
	InputStream inputStream; // 从串口来的输入流
	SerialPort port;
	//缓存NNNN字符串
	private StringBuilder linkWgt = new StringBuilder();
	//缓存ETX字符串
	private StringBuilder linkEtx = new StringBuilder();

	public SerialListener(SerialPort port) throws IOException {
		this.port = port;
		this.inputStream = new BufferedInputStream(port.getInputStream());
	}

	private BlockingQueue<String> msgQueue = new LinkedBlockingQueue<String>();

	// 报文处理线程停止位
	private boolean stop = false;
	
	private byte[] resultTmp;
	
	private AtomicInteger receiveNum = new AtomicInteger(0);
	private AtomicInteger count = new AtomicInteger(0);
	/**
	 * 
	 * @param data1
	 * @param data2
	 * @return data1 与 data2拼接的结果
	 */
	public byte[] addBytes(byte[] data1, byte[] data2) {
		if(data1==null){
			return data2;
		}
		byte[] data3 = new byte[data1.length + data2.length];
		System.arraycopy(data1, 0, data3, 0, data1.length);
		System.arraycopy(data2, 0, data3, data1.length, data2.length);
		return data3;
	}

	// 停止线程
	public void stopHandle() {
		stop = true;
	}

	/**
	 * 处理监控到的串口事件
	 */
	public static String bytesToHexString(byte[] bArr) {
	    StringBuffer sb = new StringBuffer(bArr.length);
	    String sTmp;

	    for (int i = 0; i < bArr.length; i++) {
	        sTmp = Integer.toHexString(0xFF & bArr[i]);
	        if (sTmp.length() < 2)
	            sb.append(0);
	        sb.append(sTmp.toUpperCase());
	    }

	    return sb.toString();
	}
	public void serialEvent(SerialPortEvent serialPortEvent) {
		
		switch (serialPortEvent.getEventType()) {
			case SerialPortEvent.BI: // 10 通讯中断
				MainWindow.mainBoard.addMsg("与串口设备通讯中断", LocalBoard.INFO_SYSTEM);
				break;
			case SerialPortEvent.OE: // 7 溢位（溢出）错误
				MainWindow.mainBoard.addMsg("溢位（溢出）错误", LocalBoard.INFO_SYSTEM);
				break;
			case SerialPortEvent.FE: // 9 帧错误
				MainWindow.mainBoard.addMsg("帧错误", LocalBoard.INFO_SYSTEM);
				break;
			case SerialPortEvent.PE: // 8 奇偶校验错误
				MainWindow.mainBoard.addMsg("帧错误奇偶校验错误", LocalBoard.INFO_SYSTEM);
				break;
			case SerialPortEvent.CD: // 6 载波检测
				MainWindow.mainBoard.addMsg("载波检测", LocalBoard.INFO_SYSTEM);
				break;
			case SerialPortEvent.CTS: // 3 清除待发送数据
				MainWindow.mainBoard.addMsg("载波检测", LocalBoard.INFO_SYSTEM);
				break;
			case SerialPortEvent.DSR: // 4 待发送数据准备好了
				MainWindow.mainBoard.addMsg("待发送数据准备好了", LocalBoard.INFO_SYSTEM);
				break;
			case SerialPortEvent.RI: // 5 振铃指示
				MainWindow.mainBoard.addMsg("振铃指示", LocalBoard.INFO_SYSTEM);
				break;
			case SerialPortEvent.OUTPUT_BUFFER_EMPTY: // 2 输出缓冲区已清空
				MainWindow.mainBoard.addMsg("输出缓冲区已清空", LocalBoard.INFO_SYSTEM);
				break;
			case SerialPortEvent.DATA_AVAILABLE: // 1 串口存在可用数据
				try {
					String sSubStr = "";
					
					byte[] readBuffer = new byte[1];
					int newData = inputStream.read(readBuffer);
					int i=0;
					while (newData !=-1) {
	                	try {
							
								resultTmp = addBytes(resultTmp, readBuffer);
								
								// 把0~255的int转换成两位的16进制字符串
	//							sSubStr = Integer.toHexString((newData & 0x000000FF) | 0xFFFFFF00);
								sSubStr = bytesToHexString(readBuffer);
								if(bytesToHexString("N".getBytes()).equalsIgnoreCase(sSubStr)){
									linkWgt.append(sSubStr);
								}else if(bytesToHexString("E".getBytes()).equalsIgnoreCase(sSubStr)){
									linkEtx.setLength(0);
									linkEtx.append(sSubStr);
								}else if(bytesToHexString("T".getBytes()).equalsIgnoreCase(sSubStr)){
									if(linkEtx.toString().equals(bytesToHexString("E".getBytes()))){
										linkEtx.append(sSubStr);
									}else{
										linkEtx.setLength(0);
									}
								}else if(bytesToHexString("X".getBytes()).equalsIgnoreCase(sSubStr)){
									if(linkEtx.toString().equals(bytesToHexString("ET".getBytes()))){
										linkEtx.append(sSubStr);
									}else{
										linkEtx.setLength(0);
									}
								}else{
									linkWgt.setLength(0);
									linkEtx.setLength(0);
								}
								if(linkWgt.toString().equals(bytesToHexString("NNNN".getBytes()))){
									linkWgt.setLength(0);
									logger.debug("第"+receiveNum.getAndIncrement()+"次接收到电报："+new String(resultTmp,"UTF-8"));
									try{
										msgQueue.add(new String(resultTmp));
									}catch(Throwable t){
										logger.error("msgQueue插入队列失败",t);
										// 断网、连接不上数据库时存入文件
										Map<String, Object> propertiesMap = PropertiesUtil.getDBPropertiesInfo();
										String teleRestorFilePath = (String) propertiesMap.get("teleRestorFilePath");
										String date = DateUtil.getServerTime(DateUtil.DEFAULT_DATE_FORMAT);
										DataBaseUtil.writeFileByLine(teleRestorFilePath+"msgQueue"+date+".txt",date+"时间开始接收异常:"+new String(resultTmp));
									}
									resultTmp=null;
									try{
										//记录最后一次电报时间
										DataBaseUtil.updateJaiJailtime("TEL_SENDREC_REC_LASTTIME");
									}catch(Exception e){
										logger.error("更新最后一次接收电报时间失败");
									}
								}
								if(linkEtx.toString().equals(bytesToHexString("ETX".getBytes()))){
									linkEtx.setLength(0);
									logger.debug("第"+receiveNum.getAndIncrement()+"次接收到电报："+new String(resultTmp,"UTF-8"));
									try{
										msgQueue.add(new String(resultTmp));
									}catch(Throwable t){
										logger.error("msgQueue插入队列失败",t);
										// 断网、连接不上数据库时存入文件
										Map<String, Object> propertiesMap = PropertiesUtil.getDBPropertiesInfo();
										String teleRestorFilePath = (String) propertiesMap.get("teleRestorFilePath");
										String date = DateUtil.getServerTime(DateUtil.DEFAULT_DATE_FORMAT);
										DataBaseUtil.writeFileByLine(teleRestorFilePath+"msgQueue"+date+".txt",date+"时间开始接收异常:"+new String(resultTmp));
									}

									resultTmp=null;
									try{
										//记录最后一次电报时间
										DataBaseUtil.updateJaiJailtime("TEL_SENDREC_REC_LASTTIME");
									}catch(Exception e){
										logger.error("更新最后一次接收电报时间失败");
									}
								}
								readBuffer = new byte[1];
								newData = inputStream.read(readBuffer);
							
	                	} catch (Exception e) {
	                		logger.error("接收电报数据异常", e);
	                		continue;
	    	            }
	                }
				} catch (IOException e) {
					MainWindow.mainBoard.addMsg("接收电报数据异常,请检查COM口连接是否正常", LocalBoard.INFO_LOG);
	            	logger.error("接收电报数据异常,请检查COM口连接是否正常", e);
				}
			break;
		}
	
	}

	@Override
	public void run() {
		try {
			logger.info("--------------任务处理线程运行了--------------");
			while (!stop) {
				try {
					// 如果堵塞队列中存在数据就将其输出
					if (msgQueue.size() > 0) {
						String sb = msgQueue.take();
						int num = count.incrementAndGet();
						logger.debug("第"+num+"次读取电报："+sb);
						String result = null;
						if(sb.lastIndexOf("ZCZC")!=-1 && sb.indexOf("NNNN")!=-1 && sb.lastIndexOf("ZCZC")<sb.indexOf("NNNN")){
							result = sb.substring(sb.lastIndexOf("ZCZC"), sb.indexOf("NNNN")+4);
						}
						if(null==result || "".equals(result)){
							if(sb.lastIndexOf("SOH")!=-1 && sb.indexOf("ETX")!=-1 && sb.lastIndexOf("SOH")<sb.indexOf("ETX")){
								result = sb.substring(sb.lastIndexOf("SOH"), sb.indexOf("ETX")+3);
							}
						}
						if(null!=result && !"".equals(result)){
							logger.debug("第"+num+"次处理后电报："+result);
	//						MainWindow.recieveBoard.setText("第" + num + "次接收电报:" + result + "\r\n");
							MainWindow.recieveBoard.setText(result + "\r\n");
	
							DataBaseUtil.addTelReceiveQueueInfo(result, "0", sb.indexOf("NNNN") != -1 ? "NNNN" : "SOH");
							DataBaseUtil.updateJaiJailtime("TEL_SENDREC_DATABASE_TIME");
							MainWindow.mainBoard.addMsg("电报写入数据库", LocalBoard.INFO_LOG);
							if (MainWindow.serialPortStatus.getBackground().getRed() == 0) {
								MainWindow.serialPortStatus.setBackground(new java.awt.Color(255, 0, 0));
							} else {
								MainWindow.serialPortStatus.setBackground(new java.awt.Color(0, 255, 0));
								MainWindow.serialPortStatus.repaint();
							}
						}
					}
				} catch (Exception e) {
					logger.error("处理接收电报数据线程异常", e);
					continue;
				}
			}
		} catch (Exception e) {
			logger.error("处理接收电报队列线程异常", e);
		}
	}

}
