package bros.manage.main;


import java.util.List;

import bros.manage.entity.SerialParameters;

public class ContextTemp {

	public static SerialParameters serialParameters;
	
	public static MainWindow window;
	
	public static String dabaseIp;
	/**
	 * 点击开始收发报，参数设置弹框中的内容不可改标志（true:可改；false:不可改）
	 */
	public static boolean configSetFlag=true;
	
	public static List<String>  SerialPortList = null;
}
