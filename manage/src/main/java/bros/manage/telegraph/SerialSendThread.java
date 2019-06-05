package bros.manage.telegraph;

import gnu.io.SerialPort;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import bros.manage.business.service.ITelSendQueueService;
import bros.manage.business.view.LocalBoard;
import bros.manage.main.MainWindow;
import bros.manage.main.SerialPortManager;
import bros.manage.telegraph.exception.SendDataToSerialPortFailure;
import bros.manage.telegraph.exception.SerialPortOutputStreamCloseFailure;
import bros.manage.util.DataBaseUtil;
import bros.manage.util.DateUtil;
import bros.manage.util.JavaPing;
import bros.manage.util.PropertiesUtil;
import bros.manage.util.SpringUtil;

public class SerialSendThread extends Thread {
	
	private static final Log logger = LogFactory.getLog(SerialSendThread.class);
	
	private SerialPort sp;
	volatile boolean runbit;

	public SerialSendThread(SerialPort sp) {
		super();
		this.sp = sp;
		//TODO：发报开关：true：开；false:关
		this.runbit = false;
	}

	// 检测ping网络是否同
		public static boolean checkServerStatus(){
			boolean flag = true;
			try {
				if(!JavaPing.pingDbHost()){
					flag = false;
				}
				if(!DataBaseUtil.checkDBState("default")){
					flag = false;
				}else{
					flag=true;
				}
			} catch (Exception e3) {
				flag = false;
				logger.error("检查数据库状态失败",e3);
			}
			
			return flag;
		}


	@Override
	public void run() {
		while(true){
			while(runbit){
				try{
					AtomicInteger count = new AtomicInteger(0);
					// 组装入参
					Map <String,Object> contextMap = null;
					String tel_id = "";
					// 获取执行SQL
					String sqlId = "bros.manage.business.mapper.TelSendQueueMapper.updateTelSendInfo";
					try {
						Map<String, Object> propertiesMap = new HashMap<String, Object>();
						try {
							propertiesMap = PropertiesUtil.getDBPropertiesInfo();
						} catch (ConfigurationException e) {
							MainWindow.mainBoard.addMsg("读取数据库地址配置失败", LocalBoard.INFO_ERROR);
							logger.error("读取数据库地址配置失败");
							Thread.sleep(5000);
							continue;
						}
						String ip = (String)propertiesMap.get("ip");
		//				if(!JavaPing.pingDbHost()){
						/*if(!checkServerStatus()){
							MainWindow.mainBoard.addMsg("发报数据库网络不通:"+ip, LocalBoard.INFO_ERROR);
							logger.error("发报数据库网络不通:"+ip);
							Thread.sleep(5000);
							continue;
						}*/
						DataBaseUtil.updateJaiJailtime("TEL_SENDREC_DATABASE_TIME");
						// 获取ben
						ITelSendQueueService itelSendQueueService = (ITelSendQueueService) SpringUtil.getBean("telSendQueueService");
						// 待发送列表List
						List<Map<String, Object>> queryTeleInfoList = itelSendQueueService.queryTelSendInfo();
						
						if(null != queryTeleInfoList && queryTeleInfoList.size() > 0){
							
							for(int i = 0; i < queryTeleInfoList.size(); i++){
								// 主键
								tel_id = (String) queryTeleInfoList.get(i).get("tel_id");
								// 电报原文
								String originaltxt = (String) queryTeleInfoList.get(i).get("tel_text");
								SerialPortManager.sendToPort(sp, originaltxt.getBytes("US-ASCII"));
								//更新最后一次发送电报时间
								DataBaseUtil.updateJaiJailtime("TEL_SENDREC_SEND_LASTTIME");
								try{
									int num = count.incrementAndGet();
									logger.debug("第"+num+"次发送电报："+tel_id);
								}catch(Exception e){
									logger.error("发送电报计数失败",e);
								}
								
								MainWindow.sendBoard.setText(originaltxt + "\r\n");
								contextMap = new HashMap<String,Object>();
								contextMap.put("tel_id", tel_id);
								
								// 获取执行sql
								String sql = DataBaseUtil.getSql(sqlId,contextMap);
								// 执行更新操作
								itelSendQueueService.updateTelSendInfo(contextMap);
								// 更新发送电报标志位：记录电报处理日志
								DataBaseUtil.saveReceiveQueueDealLog("电报发送（更新标志位）", "更新", sql ,"成功","无", tel_id, "4" ,"更新电报标志位(SEND_FLAG)为已发送","发送电报");
								// 电报发送成功,记录电报处理日志
								DataBaseUtil.saveReceiveQueueDealLog("电报发送", "新建", "" ,"成功","无", tel_id, "4" ,"发送电报","发送电报");
								// 发送电报备份与删除队列操作
								DataBaseUtil.saveSendQueueBak(queryTeleInfoList.get(i));
							}
						}else {
							MainWindow.sendBoard.setText("数据库中暂时没有待发送电报" + "\r\n");
						}
						Thread.sleep(2000);
					} catch (SendDataToSerialPortFailure e) {
						contextMap = new HashMap<String,Object>();
						contextMap.put("tel_id", tel_id);
						// 获取执行sql
						String sql = DataBaseUtil.getSql(sqlId,contextMap);
						// 电报发送模块异常捕捉：记录系统运行日志
						DataBaseUtil.saveSendExceptionLog("运行","异常",e.getMessage().toString(), "3");
						// 更新发送电报标志位：记录电报处理日志
						DataBaseUtil.saveReceiveQueueDealLog("电报发送（更新标志位）", "发送", sql ,"失败",e.getMessage().toString(), tel_id, "4" ,"对已经发送的电报标志位更新为已发送","发送电报");
						MainWindow.mainBoard.addMsg("串口写数据失败[SendDataToSerialPortFailure]", LocalBoard.INFO_ERROR);
						logger.error("串口写数据失败",e);
						continue;
					} catch (SerialPortOutputStreamCloseFailure e) {
						contextMap = new HashMap<String,Object>();
						contextMap.put("tel_id", tel_id);
						// 获取执行sql
						String sql = DataBaseUtil.getSql(sqlId,contextMap);
						// 电报发送模块异常捕捉：记录系统运行日志
						DataBaseUtil.saveSendExceptionLog("运行","异常","串口写数据失败[SerialPortOutputStreamCloseFailure]", "3");
						// 更新发送电报标志位：记录电报处理日志
						DataBaseUtil.saveReceiveQueueDealLog("电报发送（更新标志位）", "发送", sql ,"失败","串口写数据失败[SerialPortOutputStreamCloseFailure]", tel_id, "4" ,"对已经发送的电报标志位更新为已发送","发送电报");
						MainWindow.mainBoard.addMsg("串口写数据失败[SerialPortOutputStreamCloseFailure]", LocalBoard.INFO_ERROR);
						logger.error("串口写数据失败",e);
						continue;
					} catch (UnsupportedEncodingException e) {
						contextMap = new HashMap<String,Object>();
						contextMap.put("tel_id", tel_id);
						// 获取执行sql
						String sql = DataBaseUtil.getSql(sqlId,contextMap);
						// 电报发送模块异常捕捉：记录系统运行日志
						DataBaseUtil.saveSendExceptionLog("运行","异常","发送电报失败[UnsupportedEncodingException]", "3");
						// 更新发送电报标志位：记录电报处理日志
						DataBaseUtil.saveReceiveQueueDealLog("电报发送（更新标志位）", "发送", sql ,"失败","发送电报失败[UnsupportedEncodingException]", tel_id, "4" ,"对已经发送的电报标志位更新为已发送","发送电报");
						MainWindow.mainBoard.addMsg("发送电报失败[UnsupportedEncodingException]", LocalBoard.INFO_ERROR);
						logger.error("发送电报失败",e);
						continue;
					} catch (InterruptedException e) {
						contextMap = new HashMap<String,Object>();
						contextMap.put("tel_id", tel_id);
						// 获取执行sql
						String sql = DataBaseUtil.getSql(sqlId,contextMap);
						// 电报发送模块异常捕捉：记录系统运行日志
						DataBaseUtil.saveSendExceptionLog("运行","异常","发送电报失败[InterruptedException]", "3");
						// 更新发送电报标志位：记录电报处理日志
						DataBaseUtil.saveReceiveQueueDealLog("电报发送（更新标志位）", "发送", sql ,"失败","发送电报失败[InterruptedException]", tel_id, "4" ,"对已经发送的电报标志位更新为已发送","发送电报");
						MainWindow.mainBoard.addMsg("发送电报失败[InterruptedException]", LocalBoard.INFO_ERROR);
						logger.error("发送电报失败",e);
						continue;
					}catch(Throwable t){
						contextMap = new HashMap<String,Object>();
						contextMap.put("tel_id", tel_id);
						// 获取执行sql
						String sql = DataBaseUtil.getSql(sqlId,contextMap);
						// 电报发送模块异常捕捉：记录系统运行日志
						DataBaseUtil.saveSendExceptionLog("运行","异常","发送电报失败[Throwable]", "3");
						// 更新发送电报标志位：记录电报处理日志
						DataBaseUtil.saveReceiveQueueDealLog("电报发送（更新标志位）", "发送", sql ,"失败","发送电报失败[Throwable]", tel_id, "4" ,"标志位更新为已发送","发送电报");
						MainWindow.mainBoard.addMsg("发送电报失败[Throwable]", LocalBoard.INFO_ERROR);
						logger.error("发送电报失败",t);
						continue;
					}
				}catch(Throwable t){
					logger.error("发送电报失败",t);
					continue;
				}
			}
		}
	}
	/**
	 * 停止
	 */
	public void stopSafely(){
		runbit=false;
	}
	public void startSafely(){
		runbit=true;
	}
	
	public void updateSp(SerialPort sp){
		this.sp=sp;
	}


}
