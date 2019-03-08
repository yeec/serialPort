package bros.manage.thread;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import bros.manage.business.view.LocalBoard;
import bros.manage.main.MainWindow;
import bros.manage.util.DataBaseUtil;
import bros.manage.util.DateUtil;
import bros.manage.util.JavaPing;
import bros.manage.util.PropertiesUtil;

public class IndertDataHandler implements DealHandler {
	
	private static final Log logger = LogFactory.getLog(IndertDataHandler.class);
	
	@Override
	public void execute(List<Map<String, Object>> list) {
		for(int i = 0; i < list.size(); i++){
			try{
				//记录最后一次电报时间
				DataBaseUtil.updateJaiJailtime("TEL_SENDREC_REC_LASTTIME");
			}catch(Exception e){
				logger.error("更新最后一次接收电报时间失败");
			}
			Map<String, Object> map = list.get(i);
			String sb = (String)map.get("message");
			String num = (String)map.get("num");
		
			String result = null;
			if(sb.lastIndexOf("ZCZC")!=-1 && sb.indexOf("NNNN")!=-1 && sb.lastIndexOf("ZCZC")<sb.indexOf("NNNN")){
				result = sb.substring(sb.lastIndexOf("ZCZC"), sb.indexOf("NNNN")+4);
			}
			if(null==result || "".equals(result)){
				if(sb.lastIndexOf("")!=-1 && sb.indexOf("")!=-1 && sb.lastIndexOf("")<sb.indexOf("")){
					result = sb.substring(sb.lastIndexOf(""), sb.indexOf(""))+"";
				}
			}
			if(null!=result && !"".equals(result)){
				logger.debug("第"+num+"次处理后电报："+result);
	//						MainWindow.recieveBoard.setText("第" + num + "次接收电报:" + result + "\r\n");
				MainWindow.recieveBoard.setText(result + "\r\n");
				if(!checkServerStatus(result)){
					continue;
				}
				DataBaseUtil.addTelReceiveQueueInfo(result, "0", sb.indexOf("NNNN") != -1 ? "NNNN" : "SOH");
				DataBaseUtil.updateJaiJailtime("TEL_SENDREC_DATABASE_TIME");
				MainWindow.mainBoard.addMsg("电报写入数据库", LocalBoard.INFO_LOG);
				if (MainWindow.serialPortStatus.getBackground().getRed() == 0) {
					MainWindow.serialPortStatus.setBackground(new java.awt.Color(255, 0, 0));
				} else {
					MainWindow.serialPortStatus.setBackground(new java.awt.Color(0, 255, 0));
					MainWindow.serialPortStatus.repaint();
				}
			}
		}
	}
	
	// 检测ping网络是否同
	public static boolean checkServerStatus(String teletext){
		boolean flag = true;
		try {
			Map<String, Object> propertiesMap = PropertiesUtil.getDBPropertiesInfo();
			String ip = (String)propertiesMap.get("ip");
			if(!JavaPing.pingDbHost()){
				String teleRestorFilePath = (String) propertiesMap.get("teleRestorFilePath");
				String date = DateUtil.getServerTime(DateUtil.DEFAULT_DATE_FORMAT_EN);
				String dateTemp = DateUtil.getServerTime(DateUtil.DEFAULT_TIME_FORMAT_EN);
				DataBaseUtil.writeFileByLine(teleRestorFilePath+date+".txt",dateTemp+"时间开始接收异常:"+teletext);
				MainWindow.mainBoard.addMsg("收报数据库网络不通:"+ip, LocalBoard.INFO_ERROR);
				logger.error("收报数据库网络不通:"+ip);
				flag = false;
			}
			if(!DataBaseUtil.checkDBState("default")){
				String teleRestorFilePath = (String) propertiesMap.get("teleRestorFilePath");
				String date = DateUtil.getServerTime(DateUtil.DEFAULT_DATE_FORMAT_EN);
				String dateTemp = DateUtil.getServerTime(DateUtil.DEFAULT_TIME_FORMAT_EN);
				DataBaseUtil.writeFileByLine(teleRestorFilePath+date+".txt",dateTemp+"时间开始接收异常:"+teletext);
				MainWindow.mainBoard.addMsg("收报数据库状态异常:"+ip, LocalBoard.INFO_ERROR);
				logger.error("收报数据库状态异常:"+ip);
				flag = false;
			}
		} catch (Exception e3) {
			flag = false;
			logger.error("检查数据库状态失败",e3);
		}
		
		return flag;
	}

}
