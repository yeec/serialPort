package bros.manage.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.dao.DataAccessException;

import bros.manage.business.service.IJaiJAiltimeService;
import bros.manage.business.service.ILogSysStatemService;
import bros.manage.business.service.ILogTellogService;
import bros.manage.business.service.ITelReceiveQueueService;
import bros.manage.business.view.LocalBoard;
import bros.manage.dynamic.datasource.DynamicDataSource;
import bros.manage.entity.MyBatisSql;
import bros.manage.exception.ServiceException;
import bros.manage.main.MainWindow;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidPooledConnection;
import com.alibaba.druid.pool.GetConnectionTimeoutException;
import com.alibaba.druid.stat.JdbcDataSourceStat;

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
		
		// 获取执行SQL
		String sqlId = "bros.manage.business.mapper.TelReceiveQueueMapper.insertTelReceiveInfo";
		String sql = getSql(sqlId,contextMap);
		
		// 获取ben
		ITelReceiveQueueService itelReceiveQueueService = (ITelReceiveQueueService) SpringUtil.getBean("telReceiveQueueService");
		
		try {
			itelReceiveQueueService.addTelReceiveQueueInfo(contextMap);
			// 存入电报接收队列时记录电报处理日志
			saveReceiveQueueDealLog("电报接收", "新建", sql ,"成功","无", mainKey, "1" ,"电报接收","接收结果");
		}catch(DataAccessException da){
			Map<String, Object> propertiesMap;
			try {
				// 断网、连接不上数据库时存入文件
				propertiesMap = PropertiesUtil.getDBPropertiesInfo();
				String teleRestorFilePath = (String) propertiesMap.get("teleRestorFilePath");
				String ip = (String) propertiesMap.get("ip");
				String date = DateUtil.getServerTime(DateUtil.DEFAULT_DATE_FORMAT);
				writeFileByLine(teleRestorFilePath+date+".txt",date+"时间开始接收异常:"+teletext);
				MainWindow.mainBoard.addMsg("写库数据库状态异常:"+ip, LocalBoard.INFO_ERROR);
				MainWindow.databaseStatus.setBackground(new java.awt.Color(255, 0, 0));
				logger.error("写库库状态异常:"+ip);
				// 存入电报接收队列时记录电报处理日志
				saveReceiveQueueDealLog("电报接收", "新建", sql ,"失败",da.getMessage().toString(), mainKey, "1" ,"电报接收","接收结果");
			} catch (ConfigurationException e1) {
				logger.error("配置文件读取失败",e1);
			}catch(Exception e2){
				logger.error("记录数据库失败",e2);
			}
			logger.error("记录数据库失败",da);
		} catch (Exception e) {
			Map<String, Object> propertiesMap;
			try {
				// 断网、连接不上数据库时存入文件
				propertiesMap = PropertiesUtil.getDBPropertiesInfo();
				String teleRestorFilePath = (String) propertiesMap.get("teleRestorFilePath");
				String date = DateUtil.getServerTime(DateUtil.DEFAULT_DATE_FORMAT);
				writeFileByLine(teleRestorFilePath+date+".txt",date+"时间开始接收异常:"+teletext);
				// 存入电报接收队列时记录电报处理日志
				saveReceiveQueueDealLog("电报接收", "新建", sql ,"失败",e.getMessage().toString(), mainKey, "1" ,"电报接收","接收结果");
			} catch (ConfigurationException e1) {
				logger.error("配置文件读取失败",e1);
			}catch(Exception e2){
				logger.error("记录数据库失败",e2);
			}
			logger.error("记录数据库失败",e);
		}
	}
	
	// 文件读写文件
	public static void writeFileByLine(String file, String conent) {
		String path=file.substring(0,file.lastIndexOf("/"));
		File pathF = new File(path);
		if(!pathF.exists()){
			pathF.mkdirs();
		}
		File f = new File(file);
		if(!f.exists()){
			try {
				f.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		BufferedWriter out = null;
		try {
			out = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(file, true)));
			out.write(conent + "\r\n");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	// 检测数据库状态
	public static boolean checkDBState(String dataSourceName) throws Exception{
		boolean flag = true;
		try {
			DruidDataSource ds = (DruidDataSource) DynamicDataSource.getInstance().getDataSourceMap().get("default");
			DruidPooledConnection conn = ds.getConnection(6000);
			if(conn.isClosed()){
				MainWindow.databaseStatus.setBackground(new java.awt.Color(255, 0, 0));
				flag=false;
			}else{
				MainWindow.databaseStatus.setBackground(new java.awt.Color(0, 255, 0));
				MainWindow.databaseStatus.repaint();
				flag = true;
			}
		} catch(GetConnectionTimeoutException gte){
			flag=false;
			MainWindow.mainBoard.addMsg("数据库连接超时,请检查数据库配置!", LocalBoard.INFO_SYSTEM);
			//defaultDataSource.close();
			logger.error("数据库连接超时,请检查数据库配置",gte);
		}catch (SQLException e) {
			flag=false;
			MainWindow.mainBoard.addMsg("检查数据库状态失败!", LocalBoard.INFO_SYSTEM);
			logger.error("检查数据库状态失败", e);
		}catch(Throwable t){
			flag=false;
			MainWindow.mainBoard.addMsg("检查数据库状态失败!", LocalBoard.INFO_SYSTEM);
			logger.error("检查数据库状态失败", t);
		}
//		boolean flag = true;
//		DruidDataSource defaultDataSource = (DruidDataSource) new DynamicDataSource().getInstance().getDataSourceMap().get(dataSourceName);
//		try {
//			DruidPooledConnection dpc  = defaultDataSource.getConnection(2000);
//			Connection connection  = dpc.getConnection();
//			
//			if(null == connection || connection.isClosed()){ // 关闭状态
//				MainWindow.databaseStatus.setBackground(new java.awt.Color(255, 0, 0));
//				flag = false;
//			}else{ // 非关闭状态
//				MainWindow.databaseStatus.setBackground(new java.awt.Color(0, 255, 0));
//				MainWindow.databaseStatus.repaint();
//				flag = true;
//			}
//		} catch(GetConnectionTimeoutException gte){
//			flag=false;
//			MainWindow.mainBoard.addMsg("数据库连接超时,请检查数据库配置!", LocalBoard.INFO_SYSTEM);
//			//defaultDataSource.close();
//			logger.error("数据库连接超时,请检查数据库配置",gte);
//		}catch (SQLException e) {
//			flag=false;
//			MainWindow.mainBoard.addMsg("检查数据库状态失败!", LocalBoard.INFO_SYSTEM);
//			logger.error("检查数据库状态失败", e);
//			
//		}
		
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
			logger.error("记录数据库失败",e);
		}
	}
	
	/**
	 * 存入电报接收队列时记录电报处理日志
	 */
	public static void saveReceiveQueueDealLog(String logFun, String logType, String logSQL ,String logResult,String logMemo, String mainKey, String deaNum ,String dealType,String dealMemo) {
		try {
			ILogTellogService logTellogService = (ILogTellogService) SpringUtil.getBean("logTellogService");
			Map<String,Object> contextMap = new HashMap<String, Object>();
			 contextMap.put("logFun", logFun);
			 contextMap.put("logType", logType); 
			 contextMap.put("logSQL", logSQL);
			 contextMap.put("logResult", logResult);
			 contextMap.put("logMemo", logMemo);
			 contextMap.put("mainKey", mainKey);
			 contextMap.put("deaNum", deaNum);
			 contextMap.put("dealType", dealType);
			 contextMap.put("dealMemo", dealMemo);
			logTellogService.addLogTellogInfo(contextMap);
		} catch (ServiceException e) {
			logger.error("记录数据库失败",e);
		}
	}
	
	/**
	 * 电报发送模块异常捕捉：记录系统运行日志
	 */
	public static void saveSendExceptionLog(String sysDealType,String sysState,String logMemo, String logGrade) {
		
		try {
			ILogSysStatemService logSysStatemService = (ILogSysStatemService) SpringUtil.getBean("logSysStatemService");
			
			Map<String,Object> contextMap = new HashMap<String, Object>();
			contextMap.put("sysDealType", sysDealType); // 系统操作类型
			contextMap.put("sysState", sysState); // 系统状态
			contextMap.put("logMemo", logMemo); // 日志描述
			contextMap.put("logGrade", logGrade); // 日志等级
			contextMap.put("delaIp", DeviceInfo.getDeviceIp()); // 操作机器IP
			contextMap.put("delaMac", DeviceInfo.getDeviceMAC()); // 操作机器MAC
			
			logSysStatemService.addLogSysStatemInfo(contextMap);
		} catch (ServiceException e) {
			logger.error("记录数据库失败",e);
		}
	}
	
	public static String getSql(String sqlId,Map<String,Object> contextMap ){
		
		SqlSessionFactory sqlSessionFactory = (SqlSessionFactory) SpringUtil.getBean("sqlSessionFactory");
        MyBatisSql sql = MyBatisSqlUtils.getMyBatisSql(sqlId, contextMap, sqlSessionFactory); 
        return sql.toString();
	}
}
