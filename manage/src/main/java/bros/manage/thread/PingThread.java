package bros.manage.thread;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import bros.manage.main.ContextTemp;
import bros.manage.util.PropertiesUtil;

public class PingThread extends Thread{
	
	private static final Log logger = LogFactory.getLog(PingThread.class);
	//ping是否通：true:通；false：不通
	private volatile boolean pingFlag = true;
	
	//类初始化时，不初始化这个对象(延时加载，真正用的时候再创建)
    private static PingThread instance;
    //ping线程运行停止标志
    private volatile boolean run = false;
	
	//方法同步，调用效率低
    public static  PingThread getInstance(){
        if(instance==null){
        	synchronized (ProcessThread.class) {
				if(instance == null){
					instance=new PingThread();
					instance.start();
				}
			}
        }
        return instance;
    }

	public boolean isPingFlag() {
		return pingFlag;
	}
	public void setRun(boolean run) {
		this.run = run;
	}
	
	public void startT(){
		setRun(true);
	}
	public void stopT(){
		setRun(false);
	}

	@Override
	public void run() {
		while(true){
			while(run){
				try{
					try {
						String ip = ContextTemp.dabaseIp;
						if(null==ip || ip==""){
							Map<String, Object> propertiesMap = PropertiesUtil.getDBPropertiesInfo();
							ip = (String)propertiesMap.get("ip");
							ContextTemp.dabaseIp = ip;
						}
						InetAddress ad = InetAddress.getByName(ip);
						boolean stat = ad.isReachable(5000);
						if(stat){
							pingFlag = true;
						}else{
							pingFlag = false;
						}
					} catch (UnknownHostException e) {
						pingFlag = false;
						logger.error("数据库网络ping线程异常：UnknownHostException",e);
					} catch (IOException e) {
						pingFlag = false;
						logger.error("数据库网络ping线程异常：IOException",e);
					}
					Thread.sleep(5000);
				}catch(Exception e){
					logger.error("数据库网络ping线程异常",e);
					continue;
				}
			}
		}
		
	}
	

}
