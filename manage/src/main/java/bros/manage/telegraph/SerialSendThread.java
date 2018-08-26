package bros.manage.telegraph;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

import bros.manage.business.service.ITelSendQueueService;
import bros.manage.main.MainWindow;
import bros.manage.main.SerialPortManager;
import bros.manage.telegraph.exception.SendDataToSerialPortFailure;
import bros.manage.telegraph.exception.SerialPortOutputStreamCloseFailure;
import bros.manage.util.DataBaseUtil;
import bros.manage.util.SpringUtil;
import gnu.io.SerialPort;

public class SerialSendThread extends Thread {
	
	private SerialPort sp;
	boolean runbit;

	public SerialSendThread(SerialPort sp) {
		super();
		this.sp = sp;
		this.runbit = false;
	}




	@Override
	public void run() {
		AtomicInteger count = new AtomicInteger(0);
		while(runbit){
			DataBaseUtil.updateJaiJailtime("TEL_SENDREC_DATABASE_TIME");
			// 获取ben
			ITelSendQueueService itelSendQueueService = (ITelSendQueueService) SpringUtil.getBean("telSendQueueService");
			// 待发送列表List
			List<Map<String, Object>> queryTeleInfoList = itelSendQueueService.queryTelSendInfo();
			String tel_id = "";
			//从数据库查询记录
//					String tele = "ZCZC BZY0120 080311FF ZBAAZPZX080311 ZGGGZPZX (DEP-CCA1501/A1234-ZBAA0100-ZGGG-0)NNNN";
			// 获取执行SQL
			String sqlId = "bros.manage.business.mapper.TelReceiveQueueMapper.insertTelReceiveInfo";
			
			// 组装入参
			Map <String,Object> contextMap = null;
			try {
				
				if(null != queryTeleInfoList && queryTeleInfoList.size() > 0){
					
					for(int i = 0; i < queryTeleInfoList.size(); i++){
						// 主键
						tel_id = (String) queryTeleInfoList.get(i).get("tel_id");
						// 电报原文
//						String originaltxt = (String) queryTeleInfoList.get(i).get("originaltxt");
						String originaltxt = (String) queryTeleInfoList.get(i).get("tel_text");
						SerialPortManager.sendToPort(sp, originaltxt.getBytes("US-ASCII"));
						int num = count.incrementAndGet();
						MainWindow.sendBoard.setText(originaltxt + "\r\n");
						contextMap = new HashMap<String,Object>();
						contextMap.put("tel_id", tel_id);
						
						// 获取执行sql
						String sql = DataBaseUtil.getSql(sqlId,contextMap);
						// 执行更新操作
						itelSendQueueService.updateTelSendInfo(contextMap);
						// 更新发送电报标志位：记录电报处理日志
						DataBaseUtil.saveReceiveQueueDealLog("电报发送（更新标志位）", "发送", sql ,"成功","无", tel_id, "4" ,"更新电报标志位(SEND_FLAG)为已发送","发送电报");
						// 电报发送成功,记录电报处理日志
						DataBaseUtil.saveReceiveQueueDealLog("电报发送", "发送", sql ,"成功","无", tel_id, "4" ,"发送电报","发送电报");
					}
				}else {
					int num = count.incrementAndGet();
					MainWindow.sendBoard.setText("数据库中暂时没有待发送电报" + "\r\n");
//					if(num % 200  == 0){
//						MainWindow.sendBoard.setText("");
//					}
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
				DataBaseUtil.saveReceiveQueueDealLog("电报发送（更新标志位）", "发送", sql ,"失败",e.getMessage().toString(), tel_id, "4" ,"对已经发送的电报标志位(SEND_FLAG)更新为已发送","发送电报");
				e.printStackTrace();
			} catch (SerialPortOutputStreamCloseFailure e) {
				contextMap = new HashMap<String,Object>();
				contextMap.put("tel_id", tel_id);
				// 获取执行sql
				String sql = DataBaseUtil.getSql(sqlId,contextMap);
				// 电报发送模块异常捕捉：记录系统运行日志
				DataBaseUtil.saveSendExceptionLog("运行","异常",e.getMessage().toString(), "3");
				// 更新发送电报标志位：记录电报处理日志
				DataBaseUtil.saveReceiveQueueDealLog("电报发送（更新标志位）", "发送", sql ,"失败",e.getMessage().toString(), tel_id, "4" ,"对已经发送的电报标志位(SEND_FLAG)更新为已发送","发送电报");
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				contextMap = new HashMap<String,Object>();
				contextMap.put("tel_id", tel_id);
				// 获取执行sql
				String sql = DataBaseUtil.getSql(sqlId,contextMap);
				// 电报发送模块异常捕捉：记录系统运行日志
				DataBaseUtil.saveSendExceptionLog("运行","异常",e.getMessage().toString(), "3");
				// 更新发送电报标志位：记录电报处理日志
				DataBaseUtil.saveReceiveQueueDealLog("电报发送（更新标志位）", "发送", sql ,"失败",e.getMessage().toString(), tel_id, "4" ,"对已经发送的电报标志位(SEND_FLAG)更新为已发送","发送电报");
				e.printStackTrace();
			} catch (InterruptedException e) {
				contextMap = new HashMap<String,Object>();
				contextMap.put("tel_id", tel_id);
				// 获取执行sql
				String sql = DataBaseUtil.getSql(sqlId,contextMap);
				// 电报发送模块异常捕捉：记录系统运行日志
				DataBaseUtil.saveSendExceptionLog("运行","异常",e.getMessage().toString(), "3");
				// 更新发送电报标志位：记录电报处理日志
				DataBaseUtil.saveReceiveQueueDealLog("电报发送（更新标志位）", "发送", sql ,"失败",e.getMessage().toString(), tel_id, "4" ,"对已经发送的电报标志位(SEND_FLAG)更新为已发送","发送电报");
				e.printStackTrace();
			}
					
		}
	}
	/**
	 * 停止
	 */
	public void stopSafely(){
		runbit=false;
	}


}
