package bros.manage.business.view;

import java.util.HashMap;
import java.util.Map;

import bros.manage.business.service.ILogSysStatemService;
import bros.manage.exception.ServiceException;
import bros.manage.main.MainWindow;
import bros.manage.util.DeviceInfo;

// 程序入口main
public class TelegraphMain {
	
	// 记录操作日志接口
	private static ILogSysStatemService logSysStatemService;
	
	public static void main(String[] args) throws ServiceException {
		// 组装正常记录日志入参（数据库参数设置）
		Map<String, Object> saveLaunchLogMap = new HashMap<String, Object>();
		// 系统操作类型
		saveLaunchLogMap.put("sysDealType", "启动");
		// 操作机器IP
		saveLaunchLogMap.put("delaIp", DeviceInfo.getDeviceIp());
		// 操作机器MAC
		saveLaunchLogMap.put("delaMac", DeviceInfo.getDeviceMAC());
		
		try {
			new MainWindow("串口调试助手");
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
	}
}
