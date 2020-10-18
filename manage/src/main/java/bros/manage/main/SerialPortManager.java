package bros.manage.main;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.TooManyListenersException;

import bros.manage.business.view.LocalBoard;
import bros.manage.entity.SerialParameters;
import bros.manage.telegraph.exception.NoSuchPort;
import bros.manage.telegraph.exception.NotASerialPort;
import bros.manage.telegraph.exception.PortInUse;
import bros.manage.telegraph.exception.ReadDataFromSerialPortFailure;
import bros.manage.telegraph.exception.SendDataToSerialPortFailure;
import bros.manage.telegraph.exception.SerialPortInputStreamCloseFailure;
import bros.manage.telegraph.exception.SerialPortOutputStreamCloseFailure;
import bros.manage.telegraph.exception.SerialPortParameterFailure;
import bros.manage.telegraph.exception.TooManyListeners;
import bros.manage.util.ArrayUtils;
import bros.manage.util.DataBaseUtil;


/**
 * 串口管理
 * 
 */
public class SerialPortManager {

	/**
	 * 查找所有可用端口
	 * 
	 * @return 可用端口名称列表
	 */
	@SuppressWarnings({ "unchecked", "restriction" })
	public static final ArrayList<String> findPort() {
		ArrayList<String> portNameList = new ArrayList<String>();
		
		if(ContextTemp.SerialPortList != null && ContextTemp.SerialPortList.size() > 0){
			portNameList =  (ArrayList<String>) ContextTemp.SerialPortList;
		}else{
			CommPortIdentifier portId;
			// 将可用串口名添加到List并返回该List
		    Enumeration	en = CommPortIdentifier.getPortIdentifiers();
		    // iterate through the ports.
		    while (en.hasMoreElements()) {
		    	portId = (CommPortIdentifier) en.nextElement();
				if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
					portNameList.add(portId.getName());
				} 
		    } 
		}
		
		
		// 获得当前所有可用串口
//		Enumeration<CommPortIdentifier> portList = CommPortIdentifier.getPortIdentifiers();
//		ArrayList<String> portNameList = new ArrayList<String>();
//		// 将可用串口名添加到List并返回该List
//		while (portList.hasMoreElements()) {
//			String portName = portList.nextElement().getName();
//			portNameList.add(portName);
//		}
		return portNameList;
	}

	/**
	 * 打开串口
	 * 
	 * @param portName 端口名称
	 * @param baudrate 波特率
	 * @return 串口对象
	 * @throws SerialPortParameterFailure 设置串口参数失败
	 * @throws NotASerialPort             端口指向设备不是串口类型
	 * @throws NoSuchPort                 没有该端口对应的串口设备
	 * @throws PortInUse                  端口已被占用
	 */
	public static final SerialPort openPort(SerialParameters sp)
			throws SerialPortParameterFailure, NotASerialPort, NoSuchPort, PortInUse {
		try {
			// 通过端口名识别端口
			CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(sp.getPortName());
			// 打开端口，并给端口名字和一个timeout（打开操作的超时时间）
			CommPort commPort = portIdentifier.open(sp.getPortName(), 2000);
			// 判断是不是串口
			if (commPort instanceof SerialPort) {
				SerialPort serialPort = (SerialPort) commPort;
				try {
					// 设置一下串口的波特率等参数
//					serialPort.setSerialPortParams(baudrate,
//							SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
//							SerialPort.PARITY_NONE);
					serialPort.setSerialPortParams(sp.getBaudRate(), sp.getDatabits(), sp.getStopbits(),
							sp.getParity());
				} catch (UnsupportedCommOperationException e) {
					throw new SerialPortParameterFailure();
				}
				return serialPort;
			} else {
				// 不是串口
				throw new NotASerialPort();
			}
		} catch (NoSuchPortException e1) {
			throw new NoSuchPort();
		} catch (PortInUseException e2) {
			throw new PortInUse();
		}
	}

	/**
	 * 关闭串口
	 * 
	 * @param serialport 待关闭的串口对象
	 */
	public static SerialPort closePort(SerialPort serialPort) {
		if (serialPort != null) {
			serialPort.close();
			MainWindow.mainBoard.addMsg("串口已关闭.", LocalBoard.INFO_SYSTEM);
			MainWindow.serialPortStatus.setBackground(new java.awt.Color(255,0,0));
		}
		return null;
	}

	/**
	 * 往串口发送数据
	 * 
	 * @param serialPort 串口对象
	 * @param order      待发送数据
	 * @throws SendDataToSerialPortFailure        向串口发送数据失败
	 * @throws SerialPortOutputStreamCloseFailure 关闭串口对象的输出流出错
	 */
	public static void sendToPort(SerialPort serialPort, byte[] order) throws SendDataToSerialPortFailure, SerialPortOutputStreamCloseFailure {
		OutputStream out = null;
		try {
			out = serialPort.getOutputStream();
			out.write(order);
			out.flush();
		} catch (IOException e) {
			DataBaseUtil.saveSendExceptionLog("运行", "异常", e.getMessage().toString(), "3");
			throw new SendDataToSerialPortFailure();
		} finally {
			try {
				if (out != null) {
					out.close();
					out = null;
				}
			} catch (IOException e) {
				throw new SerialPortOutputStreamCloseFailure();
			}
		}
	}

	/**
	 * 从串口读取数据
	 * 
	 * @param serialPort 当前已建立连接的SerialPort对象
	 * @return 读取到的数据
	 */
	public static byte[] readFromPort(SerialPort serialPort) {
		InputStream in = null;
		byte[] bytes = {};
		try {
			in = serialPort.getInputStream();
//			int bufflength = in.available();
//			while(bufflength!=0){
//				bytes = new byte[bufflength];
//				in.read(bytes);
//				bufflength = in.available();
//			}
			BufferedReader reader=new BufferedReader(new InputStreamReader(in));
			int a = reader.read();
			while(a!=-1){
				char b = (char)a;
				System.out.println(b);
				a = reader.read();
			}
			// 缓冲区大小为一个字节
			/*byte[] readBuffer = new byte[1];
			
//			int bytesNum = in.read(readBuffer);
			int bytesNum=in.read(readBuffer);
			while (bytesNum != -1) {
				bytes = ArrayUtils.concat(bytes, readBuffer);
				String dataTmp = new String(bytes);
				if(dataTmp.indexOf("NNNN")!=-1||dataTmp.indexOf("ETX")!=-1){
					break;
				}
				bytesNum = in.read(readBuffer);
			}*/
		} catch (IOException e) {
			new ReadDataFromSerialPortFailure().printStackTrace();
		} finally {
			try {
				if (in != null) {
					in.close();
					in = null;
				}
			} catch (IOException e) {
				new SerialPortInputStreamCloseFailure().printStackTrace();
			}
		}
//		System.out.println(new String(bytes));
		return bytes;
	}

	/**
	 * 添加监听器
	 * 
	 * @param port     串口对象
	 * @param listener 串口监听器
	 * @throws TooManyListeners 监听类对象过多
	 */
	public static void addListener(SerialPort port, SerialPortEventListener listener) throws TooManyListeners {
		try {
			// 给串口添加监听器
			port.addEventListener(listener);
			// 设置当有数据到达时唤醒监听接收线程
			port.notifyOnDataAvailable(true);
			// 设置当通信中断时唤醒中断线程
			port.notifyOnBreakInterrupt(true);
		} catch (TooManyListenersException e) {
			throw new TooManyListeners();
		}
	}
	
	/**
	 * @param port
	 */
	public static void removeListener(SerialPort port){
			port.removeEventListener();
	}

}
