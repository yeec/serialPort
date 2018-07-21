package bros.manage.util;

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
			PropertiesConfiguration conf = new PropertiesConfiguration("application.properties");
//			conf.setProperty("spring.datasource.default.url", "jdbc:oracle:thin:@"+dbMap.get("ip")+":"+dbMap.get("port")+":"+dbMap.get("svrName"));
			conf.setProperty("spring.datasource.default.ip", dbMap.get("ip"));
			conf.setProperty("spring.datasource.default.port", dbMap.get("port"));
			conf.setProperty("spring.datasource.default.svrName", dbMap.get("svrName"));
			conf.setProperty("spring.datasource.default.username", dbMap.get("userName"));
			conf.setProperty("spring.datasource.default.password", dbMap.get("password"));
			conf.save();
		} catch (ConfigurationException e) {
			logger.error("修改数据库配置文件信息失败",e);
			throw e;
		}
	}
	
	/**
	 * 读取数据库配置文件信息
	 * @return Map
	 * @throws ConfigurationException
	 */
	public static Map<String,Object> getDBPropertiesInfo() throws ConfigurationException{
		
		try {
			
			Map<String,Object> dbMap = new HashMap<String, Object>();
			PropertiesConfiguration conf = new PropertiesConfiguration("application.properties");
			dbMap.put("ip", conf.getString("spring.datasource.default.ip"));
			dbMap.put("port", conf.getString("spring.datasource.default.port"));
			dbMap.put("svrName", conf.getString("spring.datasource.default.svrName"));
			dbMap.put("username", conf.getString("spring.datasource.default.username"));
			dbMap.put("password", conf.getString("spring.datasource.default.password"));
			return dbMap;
		} catch (ConfigurationException e) {
			logger.error("读取数据库配置文件信息失败",e);
			throw e;
		}
		
	}
	
	
	
}
