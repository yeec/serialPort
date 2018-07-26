package bros.manage.telegraph;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

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
		this.runbit = true;
	}




	@Override
	public void run() {
		while(runbit){
			DataBaseUtil.updateJaiJailtime("TEL_SENDREC_DATABASE_TIME");
			
			// 获取ben
			ITelSendQueueService itelSendQueueService = (ITelSendQueueService) SpringUtil.getBean("telSendQueueService");
			// 待发送列表List
			List<Map<String, Object>> queryTeleInfoList = itelSendQueueService.queryTelSendInfo();
			String tel_id = "";
			//从数据库查询记录
//					String tele = "ZCZC BZY0120 080311FF ZBAAZPZX080311 ZGGGZPZX (DEP-CCA1501/A1234-ZBAA0100-ZGGG-0)NNNN";
			try {
				
				if(null != queryTeleInfoList && queryTeleInfoList.size() > 0){
					// 主键
					tel_id = (String) queryTeleInfoList.get(0).get("tel_id");
					// 电报原文
					String originaltxt = (String) queryTeleInfoList.get(0).get("originaltxt");
					SerialPortManager.sendToPort(sp, originaltxt.getBytes("US-ASCII"));
					MainWindow.sendBoard.append(originaltxt + "\r\n");
					
					// 组装入参
					Map <String,Object> contextMap = new HashMap<String,Object>();
					contextMap.put("tel_id", tel_id);
					// 执行更新操作
					itelSendQueueService.updateTelSendInfo(contextMap);
					// 更新发送电报标志位：记录电报处理日志
					DataBaseUtil.saveReceiveQueueDealLog("电报发送（更新标志位）", "发送", "" ,"成功","无", tel_id, "4" ,"对已经发送的电报标志位(SEND_FLAG)更新为已发送","发送电报");
					// 电报发送成功,记录电报处理日志
					DataBaseUtil.saveReceiveQueueDealLog("电报发送", "发送", "" ,"成功","无", tel_id, "4" ,"发送电报","发送电报");
				}else {
					MainWindow.sendBoard.append("数据库中暂时没有待发送电报" + "\r\n");
				}
				Thread.sleep(2000);
			} catch (SendDataToSerialPortFailure e) {
				// 电报发送模块异常捕捉：记录系统运行日志
				DataBaseUtil.saveSendExceptionLog("运行","异常",e.getMessage().toString(), "3");
				// 更新发送电报标志位：记录电报处理日志
				DataBaseUtil.saveReceiveQueueDealLog("电报发送（更新标志位）", "发送", "" ,"失败",e.getMessage().toString(), tel_id, "4" ,"对已经发送的电报标志位(SEND_FLAG)更新为已发送","发送电报");
				e.printStackTrace();
			} catch (SerialPortOutputStreamCloseFailure e) {
				// 电报发送模块异常捕捉：记录系统运行日志
				DataBaseUtil.saveSendExceptionLog("运行","异常",e.getMessage().toString(), "3");
				// 更新发送电报标志位：记录电报处理日志
				DataBaseUtil.saveReceiveQueueDealLog("电报发送（更新标志位）", "发送", "" ,"失败",e.getMessage().toString(), tel_id, "4" ,"对已经发送的电报标志位(SEND_FLAG)更新为已发送","发送电报");
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				// 电报发送模块异常捕捉：记录系统运行日志
				DataBaseUtil.saveSendExceptionLog("运行","异常",e.getMessage().toString(), "3");
				// 更新发送电报标志位：记录电报处理日志
				DataBaseUtil.saveReceiveQueueDealLog("电报发送（更新标志位）", "发送", "" ,"失败",e.getMessage().toString(), tel_id, "4" ,"对已经发送的电报标志位(SEND_FLAG)更新为已发送","发送电报");
				e.printStackTrace();
			} catch (InterruptedException e) {
				// 电报发送模块异常捕捉：记录系统运行日志
				DataBaseUtil.saveSendExceptionLog("运行","异常",e.getMessage().toString(), "3");
				// 更新发送电报标志位：记录电报处理日志
				DataBaseUtil.saveReceiveQueueDealLog("电报发送（更新标志位）", "发送", "" ,"失败",e.getMessage().toString(), tel_id, "4" ,"对已经发送的电报标志位(SEND_FLAG)更新为已发送","发送电报");
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
