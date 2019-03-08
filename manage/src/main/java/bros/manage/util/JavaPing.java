package bros.manage.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import bros.manage.business.view.LocalBoard;
import bros.manage.main.MainWindow;
import bros.manage.thread.PingThread;

public class JavaPing {
	private static final Log logger = LogFactory.getLog(JavaPing.class);
	public static boolean isWindows(){
		String osname = System.getProperty("os.name");
		if(osname.indexOf("indows") > 0){
			return true;
		} 
		else{
			return false;
		}
	}
	
	public static boolean ping(String ip){
		if(isWindows()){
			return pingWindows(ip, 10);
		}
		else{
			return /*pingLinux(ip, 10);*/ ping(ip, 2, 500);
		}
	}

	public static boolean ping(String ip, int timeout){
		boolean flag = false;
		if(isWindows()){
			flag = pingWindows(ip, timeout);
		}else{
			flag = ping(ip, 5, 1000);
		}
		
		if(!flag){
			MainWindow.databaseStatus.setBackground(new java.awt.Color(255, 0, 0));
		}else{
			MainWindow.databaseStatus.setBackground(new java.awt.Color(0, 255, 0));
			MainWindow.databaseStatus.repaint();
		}
		return flag;
	}
	
	// ��ping��ʽ
	public static boolean pingDbHost(){
		PingThread pt1 = PingThread.getInstance();
		return pt1.isPingFlag();
	}

	/*
	 *
PING 10.0.0.1 (10.0.0.1) 56(84) bytes of data.
--- 10.0.0.1 ping statistics ---
2 packets transmitted, 0 received, 100% packet loss, time 1000ms

PING 10.2.6.57 (10.2.6.57) 56(84) bytes of data.
--- 10.2.6.57 ping statistics ---
2 packets transmitted, 0 received, 100% packet loss, time 1000ms

PING DX-180 (127.0.0.1) 56(84) bytes of data.
64 bytes from DX-180 (127.0.0.1): icmp_seq=0 ttl=220 time=0.025 ms
--- DX-180 ping statistics ---
1 packets transmitted, 1 received, 0% packet loss, time 0ms
rtt min/avg/max/mdev = 0.025/0.025/0.025/0.000 ms, pipe 2

	 */
	
	
	private static boolean pingWindows(String ip, int timeout){
		String cmd = "cmd.exe /c ping -n 1 -w "+timeout*1000+" "+ip;
		List lines = exec(cmd);
		if(lines.size() != 6){
			// ping��ʱ���������޷�����
			if(MainWindow.mainBoard != null){
				MainWindow.mainBoard.addMsg(ip+"���粻ͨ", LocalBoard.INFO_ERROR);
			}
			return false;
		}
		String str = (String)lines.get(1);
		logger.info("ping result:"+str);
		if(str.indexOf("TTL=") > 0){
			return true;
		}
		else{
			// TTL����
			if(MainWindow.mainBoard != null){
				MainWindow.mainBoard.addMsg(ip+"���粻ͨ", LocalBoard.INFO_ERROR);
			}
			return false;
		}
	}
	
	public static List exec(String cmd){
		List lines = new ArrayList();
		try {
			Process proc = Runtime.getRuntime().exec(cmd);
			BufferedReader stdin = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			BufferedReader stderr = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
			String line = stdin.readLine();
			while(line != null){
				line = line.trim();
				if(line.length() > 0){
					lines.add(line);
				}
				line = stdin.readLine();
			}
			
			line = stderr.readLine();
			while(line != null){
				line = line.trim();
				if(line.length() > 0){
					lines.add(line);
				}
				line = stderr.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return lines;
	}

	 /**
     * @param ipAddress  ip��ַ
     * @param pingTimes  ����(һ��ping,�Է����ص�ping�Ľ���Ĵ���)
     * @param timeOut    ��ʱʱ�� ��λms(ping��ͨ,���õĴ˴�ping����ʱ��)
     * @return
     */
    public static boolean ping(String ipAddress, int pingTimes, int timeOut) {
        BufferedReader in = null;
        String pingCommand = null;
        Runtime r = Runtime.getRuntime();
        String osName = System.getProperty("os.name");
        if(osName.contains("Windows")){
            //��Ҫִ�е�ping����,��������windows��ʽ������
            pingCommand = "ping " + ipAddress + " -n " + pingTimes    + " -w " + timeOut;
        }else{
            //��Ҫִ�е�ping����,��������Linux��ʽ������
            //-c:����,-w:��ʱʱ��(��λ/ms)  ping -c 10 -w 0.5 192.168.120.206
            pingCommand = "ping " + " -c " + "4" + " -w " + "2 " + ipAddress;
        }
        try {
            //ִ�������ȡ���
            Process p = r.exec(pingCommand);
            if (p == null) {
                return false;
            }
            in = new BufferedReader(new InputStreamReader(p.getInputStream()));
            int connectedCount = 0;
            String line = null;
            while ((line = in.readLine()) != null) {
                connectedCount += getCheckResult(line,osName);
            }
            //�����������=23 ms ttl=64(TTL=64 Windows)����������,���ֵĴ���=���Դ����򷵻���
            //return connectedCount == pingTimes;
            return connectedCount >= 2 ? true : false;
        } catch (Exception ex) {
            ex.printStackTrace(); //�����쳣�򷵻ؼ�
            return false;
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    //��line����=18 ms ttl=64����,˵���Ѿ�pingͨ,����1,��t����0.
    private static int getCheckResult(String line,String osName) {
        if(osName.contains("Windows")){
            if(line.contains("TTL=")){
                return 1;
            }
        }else{
            if(line.contains("ttl=")){
                return 1;
            }
        }
        return 0;
    }
    
    
	/**
	 * @param args
	 */
	public static void main(String[] args) {
	}

}
