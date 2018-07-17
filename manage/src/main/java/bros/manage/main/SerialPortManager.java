package bros.manage.main;

import gnu.io.CommPortIdentifier;

import java.util.ArrayList;
import java.util.Enumeration;

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
	@SuppressWarnings("unchecked")
	public static final ArrayList<String> findPort() {
		// 获得当前所有可用串口
		Enumeration<CommPortIdentifier> portList = CommPortIdentifier
				.getPortIdentifiers();
		ArrayList<String> portNameList = new ArrayList<String>();
		// 将可用串口名添加到List并返回该List
		while (portList.hasMoreElements()) {
			String portName = portList.nextElement().getName();
			portNameList.add(portName);
		}
		return portNameList;
	}

	
}
