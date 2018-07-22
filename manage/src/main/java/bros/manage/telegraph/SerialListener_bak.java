package bros.manage.telegraph;

import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import bros.manage.business.view.LocalBoard;
import bros.manage.main.MainWindow;
import bros.manage.util.DataBaseUtil;

/**
 * 接收电报监听器
 * 
 * @author wyc
 *
 */
public class SerialListener_bak extends Thread implements SerialPortEventListener {

	private static final Log logger = LogFactory.getLog(SerialListener_bak.class);
	InputStream inputStream; // 从串口来的输入流
	public SerialListener_bak(InputStream inputStream) {
		this.inputStream = inputStream;
	}
//	private BlockingQueue<String> msgQueue = new LinkedBlockingQueue<String>();
	private BlockingQueue<String> msgQueue = new LinkedBlockingQueue<String>();
	//读到的电报缓存
	private StringBuffer sb = new StringBuffer();
	//报文处理线程停止位
	private boolean stop = false;
	// 电报格式报文处理
	public boolean getTele(String teleContent){
		boolean flag = true;
		boolean result = false;
		while(flag){
			int INZCZCTELEGRAPH = teleContent.lastIndexOf("ZCZC");
			int INSOHTELEGRAPH = teleContent.lastIndexOf("SOH");
			if(INZCZCTELEGRAPH!=-1){
				int INNNNNTELEGRAPH = teleContent.lastIndexOf("NNNN");
				if(INNNNNTELEGRAPH!=-1 && INNNNNTELEGRAPH>INZCZCTELEGRAPH){
					teleContent = teleContent.substring(INZCZCTELEGRAPH, INNNNNTELEGRAPH);
					if(teleContent.indexOf("NNNN")==-1){
						teleContent = teleContent + "NNNN";
						flag = false;
						continue;
					}
				}
			}else if(INSOHTELEGRAPH!=-1){
				int INETXTELEGRAPH = teleContent.lastIndexOf("ETX");
				if(INETXTELEGRAPH!=-1 && INETXTELEGRAPH>INSOHTELEGRAPH){
					teleContent = teleContent.substring(INSOHTELEGRAPH, INETXTELEGRAPH);
					if(teleContent.indexOf("ETX")==-1){
						teleContent = teleContent + "ETX";
						flag = false;
						continue;
					}
				}
			}else{
				teleContent = "输入字符串的格式不正确:"+teleContent;
				flag = false;
				result = false;
				continue;
			}
		}
		result = true;
		return result;
//		return teleContent;
	}
	
	//清空报文缓存
	public void clearBuffer(){
		sb.setLength(0);;
	}
	//停止线程
	public void stopHandle(){
		stop = true;
	}
	

	/**
	 * 处理监控到的串口事件
	 */
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
				byte[] readBuffer = new byte[1];
	            try {
	                int numBytes = -1;
	                while (inputStream.available() > 0) {
	                    numBytes = inputStream.read(readBuffer);

	                    if (numBytes > 0) {
	                        msgQueue.add(new String(readBuffer));
	                        readBuffer = new byte[1];// 重新构造缓冲对象，否则有可能会影响接下来接收的数据
	                    } else {
	                        msgQueue.add("额------没有读到数据");
	                    }
	                }
	            } catch (IOException e) {
	            	logger.error("接收出口数据异常", e);
	            }
				break;
		}
	}
	
	public String process(InputStream in, String charset) {
	    byte[] buf = new byte[1024];
	    StringBuffer sb = new StringBuffer();
	    int len = 0;
	    try {
	        while ((len=in.read(buf)) != -1) {
	            sb.append(new String(buf, 0, len, charset));
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    return sb.toString();
	}
	
	@Override
    public void run() {
        // TODO Auto-generated method stub
		
		int count = 0;
        try {
            System.out.println("--------------任务处理线程运行了--------------");
            while (!stop) {
                // 如果堵塞队列中存在数据就将其输出
                if (msgQueue.size() > 0) {
//                	System.out.println("数据队列里还有数据");
                	String tmp = msgQueue.take();
                    sb.append(tmp);
                    if((sb.indexOf("NNNN")!=-1 && sb.indexOf("ZCZC")!=-1 && sb.indexOf("ZCZC")<sb.lastIndexOf("NNNN"))
                    		|| (sb.indexOf("SOH")!=-1 && sb.indexOf("ETX")!=-1 && sb.indexOf("SOH")<sb.lastIndexOf("ETX"))){
                    	String result = sb.toString();
                    	
                    	if(getTele(result)){
                    		sb = new StringBuffer();
                    		count++;
                    		MainWindow.recieveBoard.append("第"+count+"次电报:"+result + "\r\n");
                    		DataBaseUtil.addTelReceiveQueueInfo(result, "0", sb.indexOf("NNNN")!=-1 ? "NNNN":"SOH");
                    		MainWindow.mainBoard.addMsg("电报写入数据库", LocalBoard.INFO_LOG);
                    		if (MainWindow.serialPortStatus.getBackground().getRed() == 0) {
    							MainWindow.serialPortStatus.setBackground(new java.awt.Color(255, 0, 0));
    						} else {
    							MainWindow.serialPortStatus.setBackground(new java.awt.Color(0, 255, 0));
    							MainWindow.serialPortStatus.repaint();
    						}
                    	}
                    }
                }
            }
        } catch (InterruptedException e) {
        	logger.error("处理接收电报队列线程异常",e);
        }
    }
}