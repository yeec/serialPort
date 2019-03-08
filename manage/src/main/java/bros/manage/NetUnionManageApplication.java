package bros.manage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.Banner.Mode;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import bros.manage.business.service.ILogSysStatemService;
import bros.manage.dynamic.datasource.DataSourceContextHolder;
import bros.manage.exception.ServiceException;
import bros.manage.util.DeviceInfo;
import bros.manage.util.SpringUtil;

@ComponentScan("bros")
@SpringBootApplication
public class NetUnionManageApplication {
	
	private static final  Logger logger = LoggerFactory.getLogger(NetUnionManageApplication.class);
    
	public static void main(String[] args) {
		SpringApplication newRun= new SpringApplication(NetUnionManageApplication.class); 
		newRun.setBannerMode(Mode.OFF);
	}
	
	
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
			logger.error("记录启动操作日志失败", e);
			// 日志描述
			String logMemo = "记录启动操作日志失败";
			if(e instanceof ServiceException){
				logMemo = logMemo+":"+((ServiceException)e).getErrorMsg();
			}
			saveLaunchLogMap.put("logMemo", logMemo);
			// 系统状态
			saveLaunchLogMap.put("sysState", "失败");
			// 日志等级
			saveLaunchLogMap.put("logGrade", "1");
			// 数据库参数设置界面, 记录系统状态日志
			
			logSysStatemService.addLogSysStatemInfo(saveLaunchLogMap);
		}
		try {
			System.in.read();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
