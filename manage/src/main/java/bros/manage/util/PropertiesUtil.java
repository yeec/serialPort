package bros.manage.util;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;



public class PropertiesUtil {
	
	private static final Log logger = LogFactory.getLog(PropertiesUtil.class);
	
	/**
	 * 修改数据库配置文件信息
	 * @param dbMap
	 * @throws ConfigurationException
	 */
	public static void setDBPropertiesInfo(Map<String, Object> dbMap) throws ConfigurationException{
		try {
			// 获取配置文件中的路径
			PropertiesConfiguration pconf = new PropertiesConfiguration("application.properties");
			String filePath = pconf.getString("dbPropertiesFilePath");
			File file = new File(filePath);
			//获取父目录
			File fileParent = file.getParentFile();
			//判断是否存在
			if (!fileParent.exists()) {
				// 创建父目录文件
				fileParent.mkdirs();
			}
			if(!file.exists()){
				file.createNewFile();
			}
			PropertiesConfiguration conf = new PropertiesConfiguration(filePath);
			conf.setProperty("spring.datasource.default.ip", dbMap.get("ip"));
			conf.setProperty("spring.datasource.default.port", dbMap.get("port"));
			conf.setProperty("spring.datasource.default.svrName", dbMap.get("svrName"));
			conf.setProperty("spring.datasource.default.username", dbMap.get("userName"));
			conf.setProperty("spring.datasource.default.password", dbMap.get("password"));
			// 串口参数
			conf.setProperty("spring.datasource.default.portName", dbMap.get("portName"));
			conf.setProperty("spring.datasource.default.baudRate", dbMap.get("baudRate"));
			conf.setProperty("spring.datasource.default.databits", dbMap.get("databits"));
			conf.setProperty("spring.datasource.default.stopbits", dbMap.get("stopbits"));
			conf.setProperty("spring.datasource.default.parity", dbMap.get("parity"));
			conf.setProperty("spring.datasource.default.flowControlIn", dbMap.get("flowControlIn"));
			conf.setProperty("spring.datasource.default.flowControlOut", dbMap.get("flowControlOut"));
			
			conf.save();
		} catch (ConfigurationException e) {
			logger.error("修改数据库配置文件信息失败",e);
			throw e;
		} catch (IOException ex) {
			logger.error("创建文件夹失败",ex);
		}
	}
	
	/**
	 * 读取数据库配置文件信息
	 * @return Map
	 * @throws ConfigurationException
	 */
	public static Map<String,Object> getDBPropertiesInfo() throws ConfigurationException{
		PropertiesConfiguration conf = null;
		try {
			
			PropertiesConfiguration pconf = new PropertiesConfiguration("application.properties");
			String filePath = pconf.getString("dbPropertiesFilePath");
			// 获取配置文件中的路径
			
			File file = new File(filePath);
			if(!file.exists()){
				conf = new PropertiesConfiguration("application.properties");
			}else{
				conf = new PropertiesConfiguration(filePath);
			}
			
			Map<String,Object> dbMap = new HashMap<String, Object>();
			dbMap.put("ip", conf.getString("spring.datasource.default.ip"));
			dbMap.put("port", conf.getString("spring.datasource.default.port"));
			dbMap.put("svrName", conf.getString("spring.datasource.default.svrName"));
			dbMap.put("username", conf.getString("spring.datasource.default.username"));
			dbMap.put("password", conf.getString("spring.datasource.default.password"));
			dbMap.put("dbPropertiesFilePath", pconf.getString("dbPropertiesFilePath"));
			dbMap.put("teleRestorFilePath", pconf.getString("teleRestorFilePath"));
			// 串口参数配置
			dbMap.put("portName", conf.getString("spring.datasource.default.portName"));
			dbMap.put("baudRate", conf.getString("spring.datasource.default.baudRate"));
			dbMap.put("databits", conf.getString("spring.datasource.default.databits"));
			dbMap.put("stopbits", conf.getString("spring.datasource.default.stopbits"));
			dbMap.put("parity", conf.getString("spring.datasource.default.parity"));
			dbMap.put("flowControlIn", conf.getString("spring.datasource.default.flowControlIn"));
			dbMap.put("flowControlOut", conf.getString("spring.datasource.default.flowControlOut"));
			return dbMap;
		} catch (ConfigurationException e) {
			logger.error("读取数据库配置文件信息失败",e);
			throw e;
		}
		
	}
	
	/**
	 * 
	 * @return Map
	 * @throws ConfigurationException
	 */
	public static Map<String,Object> getDBPropertiesDiskInfo() throws ConfigurationException{
		try {
			
			Map<String,Object> propertiesMap =  getDBPropertiesInfo();
			return propertiesMap;
		} catch (Exception e) {
			logger.error("读取数据库配置文件信息失败",e);
			throw e;
		}
	}
	
	
	
}
