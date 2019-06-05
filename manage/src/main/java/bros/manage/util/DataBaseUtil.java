package bros.manage.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
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
import bros.manage.business.service.ILogTAllExceptionService;
import bros.manage.business.service.ILogTellogService;
import bros.manage.business.service.ITelReceiveQueueService;
import bros.manage.business.service.ITelSendQueueBakService;
import bros.manage.business.service.ITelSendQueueService;
import bros.manage.business.service.ITelSysStatusService;
import bros.manage.business.view.LocalBoard;
import bros.manage.dynamic.datasource.DynamicDataSource;
import bros.manage.entity.MyBatisSql;
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
		
		// 获取执行SQL
		String sqlId = "bros.manage.business.mapper.TelReceiveQueueMapper.insertTelReceiveInfo";
		String sql = "";
		try{
		  sql = getSql(sqlId,contextMap);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
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
				String date = DateUtil.getServerTime(DateUtil.DEFAULT_DATE_FORMAT_EN);
				String dateTemp = DateUtil.getServerTime(DateUtil.DEFAULT_TIME_FORMAT_EN);
				writeFileByLine(teleRestorFilePath+date+".txt",dateTemp+"时间开始接收异常:"+teletext);
				
				MainWindow.mainBoard.addMsg("写库数据库状态异常:"+ip, LocalBoard.INFO_ERROR);
				MainWindow.databaseStatus.setBackground(new java.awt.Color(255, 0, 0));
				logger.error("写库库状态异常:"+ip,da);
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
		DruidPooledConnection conn = null;
		try {
			DruidDataSource ds = (DruidDataSource) DynamicDataSource.getInstance().getDataSourceMap().get("default");
			conn = ds.getConnection(6000);
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
			MainWindow.mainBoard.addMsg("数据库连接超时,请检查数据库配置!", LocalBoard.INFO_ERROR);
			//defaultDataSource.close();
			logger.error("数据库连接超时,请检查数据库配置",gte);
		}catch (SQLException e) {
			flag=false;
			MainWindow.mainBoard.addMsg("检查数据库状态失败!", LocalBoard.INFO_ERROR);
			logger.error("检查数据库状态失败", e);
		}catch(Throwable t){
			flag=false;
			MainWindow.mainBoard.addMsg("检查数据库状态失败!", LocalBoard.INFO_ERROR);
			logger.error("检查数据库状态失败", t);
		}finally{
			if(conn!=null){
				conn.close();
			}
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
			
			if("失败".equals(logResult)){
				Map<String,Object> LogTAllExceptionMap = new HashMap<String, Object>();
				LogTAllExceptionMap.put("logFun", logFun);
				LogTAllExceptionMap.put("logType", logType);
				LogTAllExceptionMap.put("logSql", logSQL);
				LogTAllExceptionMap.put("logMemo", logMemo);
				LogTAllExceptionMap.put("mainkey", mainKey);
				LogTAllExceptionMap.put("dealType", dealType);
				LogTAllExceptionMap.put("dealMemo", dealMemo);
				LogTAllExceptionMap.put("delaIp", DeviceInfo.getDeviceIp());
				LogTAllExceptionMap.put("delaMac", DeviceInfo.getDeviceMAC());
				saveLogTAllException(LogTAllExceptionMap);
			}
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
	
	
	
	// 记录当前系统运行状态
	public static void saveSysStatusInfo(String sysStatusText) throws ServiceException{
		try {
			ITelSysStatusService telSysStatusService = (ITelSysStatusService) SpringUtil.getBean("telSysStatusService");
			// 组装记录当前系统的状态入参
			Map<String, Object> telSysStatusMap = new HashMap<String, Object>();
			
			// 主键
			final String telId = UUID.randomUUID().toString();
			telSysStatusMap.put("telId", telId);
			// 程序运行所在服务器IP
			telSysStatusMap.put("sysIp", DeviceInfo.getDeviceIp());
			// 当前状态
			telSysStatusMap.put("sysStatus", sysStatusText);
			
			Map<String, Object> propertiesMap = PropertiesUtil.getDBPropertiesInfo();
			// 数据库服务器IP
			telSysStatusMap.put("sysDatabase", (String) propertiesMap.get("ip"));
			
			// 查询当前运行系统运行状态表记录
			int count = telSysStatusService.queryTelSysStatus(telSysStatusMap);
			if(count > 0){
				// 更新
				telSysStatusService.updateTelSysStatus(telSysStatusMap);
			}else{
				// 添加纪录
				telSysStatusService.addTelSysStatus(telSysStatusMap);
			}
		} catch (ConfigurationException e1) {
			e1.printStackTrace();
		}
	}
	
	/**
	 * 系统本身现在有做一个电报处理日志的功能，用到的表(LOG_T_TELLOG),这张表对某个操作不管成功还是失败都要记录下在，
	 * 现在要做的是，如果某个操作失败了，同时也要把失败的记录到这张表（LOG_T_ALL_EXCEPTION ）
	 * @param contextMap
	 */
	public static void saveLogTAllException(Map<String,Object> contextMap) throws ServiceException{
		ILogTAllExceptionService logTAllExceptionService = (ILogTAllExceptionService) SpringUtil.getBean("logTAllExceptionService");
		logTAllExceptionService.addLogTAllException(contextMap);
	}
	
	/**
	 * 新增发送电报备份
	 * @param contextMap
	 * @throws ServiceException
	 */
	public static void saveSendQueueBak(Map<String,Object> contextMap) throws ServiceException{
		ITelSendQueueBakService telSendQueueBakService = (ITelSendQueueBakService) SpringUtil.getBean("telSendQueueBakService");
		ITelSendQueueService telSendQueueService = (ITelSendQueueService) SpringUtil.getBean("telSendQueueService");
		// 获取执行SQL
		String sqlIdBak = "bros.manage.business.mapper.TelSendQueueBakMapper.insertTelSendInfoBak";
		String sqlIdAll = "bros.manage.business.mapper.TelSendQueueBakMapper.insertTelTAllSend";
		String sqlDelete = "bros.manage.business.mapper.TelSendQueueMapper.deleteTelSendInfo";
		
		// 获取执行sql
		String sqlBak ="";
		String sqlAll ="";
		String sqlDeleteTel ="";
		// 主键
		String tel_id = (String) contextMap.get("tel_id");
		try{
			int a = telSendQueueBakService.addTelSendInfoBak(contextMap);
			logger.debug("记录TEL_T_SEND_QUEUE_BAK表:"+tel_id+"[成功条数："+a+"]");
			// 获取执行sql
			sqlBak = DataBaseUtil.getSql(sqlIdBak,contextMap);
			// 电报发送成功,记录电报处理日志
			DataBaseUtil.saveReceiveQueueDealLog("电报发送备份TEL_T_SEND_QUEUE_BAK", "新建", sqlBak ,"成功","无", tel_id, "4" ,"发送电报备份","发送电报备份");
		}catch(Exception e){
			DataBaseUtil.saveReceiveQueueDealLog("电报发送备份TEL_T_SEND_QUEUE_BAK", "新建", sqlBak ,"失败","无", tel_id, "4" ,"发送电报备份","发送电报备份");
			logger.error("电报发送备份TEL_T_SEND_QUEUE_BAK失败", e);
		}
		
		try{
			int a = telSendQueueBakService.addTelTAllSend(contextMap);
			logger.debug("记录TEL_T_ALLSEND表:"+tel_id+"[成功条数："+a+"]");
			// 获取执行sql
			sqlAll = DataBaseUtil.getSql(sqlIdAll,contextMap);
			// 电报发送成功,记录电报处理日志
			DataBaseUtil.saveReceiveQueueDealLog("电报发送备份TEL_T_ALLSEND", "新建", sqlAll ,"成功","无", tel_id, "4" ,"发送电报备份","发送电报备份");
		}catch(Exception e){
			DataBaseUtil.saveReceiveQueueDealLog("电报发送备份TEL_T_ALLSEND", "新建", sqlAll ,"失败","无", tel_id, "4" ,"发送电报备份","发送电报备份");
			logger.error("电报发送备份TEL_T_ALLSEND失败", e);
		}
		
		

		try{
			int a = telSendQueueService.deleteTelSendInfo(contextMap);
			logger.debug("删除TEL_T_SEND_QUEUE表记录:"+tel_id+"[成功条数："+a+"]");
			// 获取执行sql
			sqlDeleteTel = DataBaseUtil.getSql(sqlDelete,contextMap);
			// 电报发送成功,记录电报处理日志
			DataBaseUtil.saveReceiveQueueDealLog("电报发送", "删除", sqlDeleteTel ,"成功","无", tel_id, "4" ,"删除发送队列","删除发送队列");
		}catch(Exception e){
			DataBaseUtil.saveReceiveQueueDealLog("电报发送", "删除", sqlDeleteTel ,"失败","无", tel_id, "4" ,"删除发送队列","删除发送队列");
			logger.error("电报发送记录操作日志失败", e);
		}
	}
}
