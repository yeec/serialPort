package bros.manage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.Banner.Mode;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import bros.manage.business.service.ILogSysStatemService;
import bros.manage.dynamic.datasource.DataSourceContextHolder;
import bros.manage.exception.ServiceException;
import bros.manage.main.MainWindow;
import bros.manage.util.DataBaseUtil;
import bros.manage.util.DeviceInfo;
import bros.manage.util.SpringUtil;

@SpringBootApplication
public class NetUnionManageApplication {

	public static void main(String[] args) {
//		SpringApplication.run(NetUnionManageApplication.class, args);
		SpringApplication newRun= new SpringApplication(NetUnionManageApplication.class); 
		newRun.setBannerMode(Mode.OFF);
		newRun.run(args);
		
//		new MainWindow();
//		try{
//			log();
//		}catch(ServiceException se){
//			
//		}
	}
	
//	@Bean
//	public HttpMessageConverters fastJsonHttpMessageConverters() {  
//		FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();  
//		FastJsonConfig fastJsonConfig = new FastJsonConfig();
//		fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat);
//		//附加：处理中文乱码（后期添加）
//        List<MediaType> fastMedisTypes=new ArrayList<MediaType>();
//        fastMedisTypes.add(MediaType.APPLICATION_JSON_UTF8);
//        fastConverter.setSupportedMediaTypes(fastMedisTypes);
//        
//		fastConverter.setFastJsonConfig(fastJsonConfig);
//		HttpMessageConverter<?> converter = fastConverter;
//		return new HttpMessageConverters(converter);
//	}
	
	public static void log() throws ServiceException{
		DataSourceContextHolder.setDBType("default");
		
		// 组装正常记录日志入参（数据库参数设置）
		Map<String, Object> saveLaunchLogMap = new HashMap<String, Object>();
		// 系统操作类型
		saveLaunchLogMap.put("sysDealType", "启动");
		// 操作机器IP
		saveLaunchLogMap.put("delaIp", DeviceInfo.getDeviceIp());
		// 操作机器MAC
		saveLaunchLogMap.put("delaMac", DeviceInfo.getDeviceMAC());
		ILogSysStatemService logSysStatemService = (ILogSysStatemService) SpringUtil.getBean("logSysStatemService");
		try {
			
			// 日志描述
			saveLaunchLogMap.put("logMemo", "无");
			// 系统状态
			saveLaunchLogMap.put("sysState", "成功");
			// 日志等级
			saveLaunchLogMap.put("logGrade", "1");
			// 数据库参数设置界面, 记录系统状态日志
			logSysStatemService.addLogSysStatemInfo(saveLaunchLogMap);
		} catch (Exception e) {
			// 日志描述
			String logMemo = "数据库参数组装失败, 错误信息:" + (e.getMessage());
			saveLaunchLogMap.put("logMemo", logMemo);
			// 系统状态
			saveLaunchLogMap.put("sysState", "失败");
			// 日志等级
			saveLaunchLogMap.put("logGrade", "1");
			// 数据库参数设置界面, 记录系统状态日志
			
			logSysStatemService.addLogSysStatemInfo(saveLaunchLogMap);
			e.printStackTrace();
		}
		try {
			System.in.read();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
