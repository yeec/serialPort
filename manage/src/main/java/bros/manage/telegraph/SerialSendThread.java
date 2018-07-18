package bros.manage.telegraph;

import java.io.UnsupportedEncodingException;

import gnu.io.SerialPort;
import bros.manage.main.MainWindow;
import bros.manage.main.SerialPortManager;
import bros.manage.telegraph.exception.SendDataToSerialPortFailure;
import bros.manage.telegraph.exception.SerialPortOutputStreamCloseFailure;

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
			//从数据库查询记录
			String tele = "ZCZC BZY0120 080311FF ZBAAZPZX080311 ZGGGZPZX (DEP-CCA1501/A1234-ZBAA0100-ZGGG-0)NNNN";
			try {
				SerialPortManager.sendToPort(sp, tele.getBytes("US-ASCII"));
				MainWindow.sendBoard.append(tele + "\r\n");
				Thread.sleep(2000);
			} catch (SendDataToSerialPortFailure e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SerialPortOutputStreamCloseFailure e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
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
