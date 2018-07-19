package bros.manage.telegraph;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;

import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import bros.manage.business.view.LocalBoard;
import bros.manage.main.MainWindow;
import bros.manage.main.SerialPortManager;

/**
 * 接收电报监听器
 * 
 * @author wyc
 *
 */
public class SerialListener implements SerialPortEventListener {

	private static final Log logger = LogFactory.getLog(SerialListener.class);
	private SerialPort serialport;

	public SerialListener(SerialPort serialport) {
		this.serialport = serialport;
	}
	
	// 电报格式报文处理
	public String getTele(String teleContent){
		boolean flag = true;
		while(flag){
			int INZCZCTELEGRAPH = teleContent.lastIndexOf("ZCZC");
			int INSOHTELEGRAPH = teleContent.lastIndexOf("SOH");
			if(INZCZCTELEGRAPH!=-1){
				int INNNNNTELEGRAPH = teleContent.lastIndexOf("NNNN");
				teleContent = teleContent.substring(INZCZCTELEGRAPH, INNNNNTELEGRAPH);
				if(teleContent.indexOf("NNNN")==-1){
					teleContent = teleContent + "NNNN";
					flag = false;
					continue;
				}
			}else if(INSOHTELEGRAPH!=-1){
				int INETXTELEGRAPH = teleContent.lastIndexOf("ETX");
				teleContent = teleContent.substring(INSOHTELEGRAPH, INETXTELEGRAPH);
				if(teleContent.indexOf("ETX")==-1){
					teleContent = teleContent + "ETX";
					flag = false;
					continue;
				}
			}else{
				teleContent = "输入字符串的格式不正确";
				flag = false;
				continue;
			}
		}
		return teleContent;
	}

	/**
	 * 处理监控到的串口事件
	 */
	public void serialEvent(SerialPortEvent serialPortEvent) {

		switch (serialPortEvent.getEventType()) {

		case SerialPortEvent.BI: // 10 通讯中断
			// ShowUtils.errorMessage("与串口设备通讯中断");
			MainWindow.mainBoard.addMsg("与串口设备通讯中断", LocalBoard.INFO_SYSTEM);
			break;

		case SerialPortEvent.OE: // 7 溢位（溢出）错误

		case SerialPortEvent.FE: // 9 帧错误

		case SerialPortEvent.PE: // 8 奇偶校验错误

		case SerialPortEvent.CD: // 6 载波检测

		case SerialPortEvent.CTS: // 3 清除待发送数据

		case SerialPortEvent.DSR: // 4 待发送数据准备好了

		case SerialPortEvent.RI: // 5 振铃指示

		case SerialPortEvent.OUTPUT_BUFFER_EMPTY: // 2 输出缓冲区已清空
			break;

		case SerialPortEvent.DATA_AVAILABLE: // 1 串口存在可用数据
			byte[] data = null;
			try {
				if (serialport == null) {
					MainWindow.mainBoard.addMsg("串口对象为空！监听失败！",
							LocalBoard.INFO_SYSTEM);
				} else {
					// 读取串口数据
					data = SerialPortManager.readFromPort(serialport);
					// MainWindow.recieveBoard.setText(new String(data)+
					// "\r\n");
					String contextData = getTele(new String(data));
					MainWindow.recieveBoard.append(contextData + "\r\n");
					MainWindow.mainBoard.addMsg("电报写入数据库", LocalBoard.INFO_LOG);
					if (MainWindow.serialPortStatus.getBackground().getRed() == 0) {
						MainWindow.serialPortStatus.setBackground(new java.awt.Color(255, 0, 0));
					} else {
						MainWindow.serialPortStatus.setBackground(new java.awt.Color(0, 255, 0));
						MainWindow.serialPortStatus.repaint();
					}
					// MainWindow.mainBoard.addMsg("接收到电报:"+new String(data),
					// LocalBoard.INFO_SYSTEM);
				}
			} catch (Exception e) {
				MainWindow.mainBoard.addMsg("系统异常：" + e.toString(),LocalBoard.INFO_SYSTEM);
				logger.error("接收电报监听异常", e);
			}
			break;
		}
	}
}