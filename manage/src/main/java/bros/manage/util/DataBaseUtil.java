package bros.manage.util;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import bros.manage.business.service.IJaiJAiltimeService;
import bros.manage.business.service.ILogSysStatemService;
import bros.manage.business.service.ITelReceiveQueueService;
import bros.manage.business.view.LocalBoard;
import bros.manage.dynamic.datasource.DynamicDataSource;
import bros.manage.exception.ServiceException;
import bros.manage.main.MainWindow;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidPooledConnection;
import com.alibaba.druid.pool.GetConnectionTimeoutException;

public class DataBaseUtil {
	private static final Log logger = LogFactory.getLog(DataBaseUtil.class);
	/**
	 * 
	 * @param teletext 电报内容
	 * @param vFlag    处理标志 0：成功 1：失败
	 * @param teleFlag 电报标志 ZCZC SOH
	 */
	public static void addTelReceiveQueueInfo(String teletext,String vFlag,String teleFlag){
		
		Map<String,Object> contextMap = new HashMap<String,Object>();
		// 主键
		final String mainKey = UUID.randomUUID().toString();
		contextMap.put("telId", mainKey);
		// 电报报文
		contextMap.put("teletext", teletext);
		// 报文类别
		contextMap.put("teleFlag", teleFlag);
		// 处理标志
		contextMap.put("vFlag", vFlag);
		// 接收机器IP
		contextMap.put("recIp", DeviceInfo.getDeviceIp());
		// 接收机器MA
		contextMap.put("recMac", DeviceInfo.getDeviceMAC());
		// 用户ID
		contextMap.put("userid", "SYSTEM_TEL");
		// 获取ben
		ITelReceiveQueueService itelReceiveQueueService = (ITelReceiveQueueService) SpringUtil.getBean("telReceiveQueueService");

		try {
			itelReceiveQueueService.addTelReceiveQueueInfo(contextMap);
		} catch (ServiceException e) {
			logger.error("记录数据库失败",e);
		}
	}
	
	public static boolean checkDBState(String dataSourceName) throws Exception{
		boolean flag = true;
		DruidDataSource defaultDataSource = (DruidDataSource) new DynamicDataSource().getInstance().getDataSourceMap().get(dataSourceName);
		try {
			DruidPooledConnection dpc  = defaultDataSource.getConnection(2000);
			Connection connection  = dpc.getConnection();
			
			if(null == connection || connection.isClosed()){ // 关闭状态
				MainWindow.databaseStatus.setBackground(new java.awt.Color(255, 0, 0));
				flag = false;
			}else{ // 非关闭状态
				MainWindow.databaseStatus.setBackground(new java.awt.Color(0, 255, 0));
				MainWindow.databaseStatus.repaint();
				flag = true;
			}
		} catch(GetConnectionTimeoutException gte){
			MainWindow.mainBoard.addMsg("数据库连接超时,请检查数据库配置!", LocalBoard.INFO_SYSTEM);
			defaultDataSource.close();
			throw new ServiceException("EBNT0000", "数据库连接超时,请检查数据库配置");
		}catch (SQLException e) {
			MainWindow.mainBoard.addMsg("检查数据库状态失败!", LocalBoard.INFO_SYSTEM);
			logger.error("检查数据库状态失败", e);
			throw new ServiceException("EBNT0000", "检查数据库状态失败");
		}
		
		return flag;
	}
	
	
	/**
	 * 记录最后一次接收电报时间
	 */
	public static void saveLastReceiveLog(){
		try {
			// 获取ben
			IJaiJAiltimeService jaiJAiltimeService = (IJaiJAiltimeService) SpringUtil.getBean("jaiJAiltimeService");
			jaiJAiltimeService.addJaiJailtimeinfo();
		} catch (Exception e) {
			logger.error("记录数据库失败",e);
		}
		
	}
	
	
	/**
	 * 记录最后一次接收电报时间
	 */
	public static int updateJaiJailtime(String flag){
		try {
			Map<String, Object> columMap = new HashMap<String, Object>();
			columMap.put("colum", flag);
			// 获取ben
			IJaiJAiltimeService jaiJAiltimeService = (IJaiJAiltimeService) SpringUtil.getBean("jaiJAiltimeService");
			int con = jaiJAiltimeService.updateJaiJailtimeinfo(columMap);
			if(con == 0){ // 没有记录，更新0条，说明数据库没数据，则插入一条数据
				saveLastReceiveLog();
			}else if(con < 0){
				if("TEL_SENDREC_REC_LASTTIME".equals(flag)){ // 更新接收电报时间失败
					saveLastReceiveStoreErrorLog(true);
				}else if("TEL_SENDREC_DATABASE_TIME".equals(flag) ){//更新存储电报时间失败
					saveLastReceiveStoreErrorLog(false);
				}else if("TEL_SENDREC_DATABASE_TIME".equals(flag)){//更新发送电报时间失败
					saveLastReceiveStoreErrorLog(false);
				}
			}
		} catch (Exception e) {
			logger.error("记录数据库失败",e);
		}
		return 0;
	}
	
	
	// 记录最后一次接收电报时间和电报存储时间
	public static void saveLastReceiveStoreErrorLog(boolean flag){
		try {
			ILogSysStatemService logSysStatemService = (ILogSysStatemService) SpringUtil.getBean("logSysStatemService");
			
			Map<String,Object> contextMap = new HashMap<String, Object>();
			
			contextMap.put("sysDealType", "运行");
			if(flag){ // 接收
				contextMap.put("sysState", "接收异常");
				contextMap.put("logMemo", "记录最后一次电报接收时间异常");
			}else{ // 存储
				contextMap.put("sysState", "存储运行");
				contextMap.put("logMemo", "记录最后一次电报存储时间异常");
			}
			contextMap.put("logGrade", "3");
			contextMap.put("delaIp", DeviceInfo.getDeviceIp());
			contextMap.put("delaMac", DeviceInfo.getDeviceMAC());
			logSysStatemService.addLogSysStatemInfo(contextMap);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
