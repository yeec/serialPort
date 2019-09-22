package bros.manage.telegraph;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import bros.manage.business.view.LocalBoard;
import bros.manage.main.MainWindow;
import bros.manage.thread.IndertDataHandler;
import bros.manage.thread.ProcessEntity;
import bros.manage.thread.ProcessThread;
import bros.manage.thread.Queue;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

/**
 * 接收电报监听器
 * 
 * @author wyc
 *
 */
public class SerialListener extends Thread implements SerialPortEventListener {

	private static final Log logger = LogFactory.getLog(SerialListener.class);
	SerialPort port;
	public SerialListener(SerialPort port) throws IOException {
		this.port = port;
	}
	
	public void updateSerialPort(SerialPort port) {
		this.port = port;
	}

	private BlockingQueue<String> msgQueue = new LinkedBlockingQueue<String>();

	// 报文处理线程停止位
	private volatile boolean stop = false;
	private int count1 = 0;
	
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
	// 开始线程
	public void startHandle() {
		stop = false;
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

	public static String hexToStringGBK(String s) {
		byte[] baKeyword = new byte[s.length() / 2];
		for (int i = 0; i < baKeyword.length; i++) {
			try {
				baKeyword[i] = (byte) (0xff & Integer.parseInt(s.substring(i * 2, i * 2 + 2), 16));
			} catch (Exception e) {
				e.printStackTrace();
				return "";
			}
		}
		try {
			s = new String(baKeyword, "GBK");// UTF-16le:Not
		} catch (Exception e1) {
			e1.printStackTrace();
			return "";
		}
		return s;
	}
	public synchronized void serialEvent(SerialPortEvent serialPortEvent) {
		
		switch (serialPortEvent.getEventType()) {
			case SerialPortEvent.BI: // 10 通讯中断
				MainWindow.mainBoard.addMsg("与串口设备通讯中断", LocalBoard.INFO_ERROR);
				break;
			case SerialPortEvent.OE: // 7 溢位（溢出）错误
				MainWindow.mainBoard.addMsg("溢位（溢出）错误", LocalBoard.INFO_ERROR);
				break;
			case SerialPortEvent.FE: // 9 帧错误
				MainWindow.mainBoard.addMsg("帧错误", LocalBoard.INFO_ERROR);
				break;
			case SerialPortEvent.PE: // 8 奇偶校验错误
				MainWindow.mainBoard.addMsg("帧错误奇偶校验错误", LocalBoard.INFO_ERROR);
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
					InputStream inputStream = new BufferedInputStream(port.getInputStream());
					byte[] readBuffer = new byte[1];
					ByteArrayOutputStream bytes = new ByteArrayOutputStream();
					StringBuffer dataHex = new StringBuffer();
					while (inputStream.read(readBuffer) !=-1) {
	                	try {
								bytes.write(readBuffer);
								dataHex.append(bytesToHexString(readBuffer));
								String message = bytes.toString();
								if(message.endsWith("NNNN") || message.endsWith("")){
									String result = hexToStringGBK(dataHex.toString());
									logger.debug("第"+receiveNum.getAndIncrement()+"次读取电报："+result);
									msgQueue.put(result);
									bytes.reset();
								}
	                	} catch (Exception e) {
	                		logger.error("接收电报数据异常", e);
	                		continue;
	    	            }
	                }
				} catch (IOException e) {
					MainWindow.mainBoard.addMsg("接收电报数据异常,请检查COM口连接是否正常", LocalBoard.INFO_ERROR);
	            	logger.error("接收电报数据异常,请检查COM口连接是否正常", e);
				}
			break;
		}
	
	}

	@Override
	public void run() {
		String taskId = "insertId";
		int poolSize = 10;
		int queueSize = 20;
		int limit = 1;
		ProcessThread.init(taskId, poolSize, queueSize, limit);
		try {
			logger.info("--------------任务处理线程运行了--------------");
			while(true){
				while (!stop) {
					try {
						// 如果堵塞队列中存在数据就将其输出
						if (msgQueue.size() > 0) {
							String sb = msgQueue.take();
							int num = count.incrementAndGet();
							logger.debug("第"+num+"次处理电报："+sb);
							
							Map<String,Object> data = new HashMap<String, Object>();
							data.put("message", sb);
							data.put("num", num+"");
							IndertDataHandler indertDataHandler = new IndertDataHandler();
							ProcessEntity entity = new ProcessEntity(data,indertDataHandler);
							Queue.getInstance().getQueue(taskId, 1).put(entity);
						}
					} catch (Exception e) {
						logger.error("处理接收电报数据线程异常", e);
						continue;
					}
				}
			}
		} catch (Exception e) {
			logger.error("处理接收电报队列线程异常", e);
		}
	}
}