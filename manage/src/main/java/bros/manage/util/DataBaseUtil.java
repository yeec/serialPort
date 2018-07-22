package bros.manage.util;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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
			defaultDataSource.close();
			throw new ServiceException("EBNT0000", "数据库连接超时,请检查数据库配置");
		}catch (SQLException e) {
			logger.error("检查数据库状态失败", e);
			throw new ServiceException("EBNT0000", "检查数据库状态失败");
		}
		
		return flag;
	}
}
