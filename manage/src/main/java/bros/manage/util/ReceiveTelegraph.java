package bros.manage.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.StringTokenizer;

import bros.manage.business.view.LocalBoard;
import bros.manage.entity.ReceiveDataBean;
import bros.manage.main.MainWindow;



public class ReceiveTelegraph extends Thread {
	TelegramSerialPort sp;
	Date date;
	SimpleDateFormat dateFormat;
	SimpleDateFormat timeFormat;
	long ID;
	String tempDate;
	TelegraphAnalyser analyser;
	SerialPortStatusBlink blink;

	int clientCount;
	ArrayList clientList;
	ArrayList flagList;
	String tempFlagString;
	int tempClientID;
	int flagListCount;
	StringTokenizer st;
	
	private int count;
	
	public ReceiveTelegraph(TelegramSerialPort serialPort){
		//init client flag list
		sp=serialPort;
		clientCount=sp.clientTableModel.getRowCount();
		clientList=sp.clientTableModel.getClientList();
		flagList= new ArrayList();
		blink=new SerialPortStatusBlink();
		blink.start();
		for(int i=0;i<clientCount;i++){
//			tempFlagString=((ClientConfigs)(clientList.get(i))).flag;
//			tempClientID=((ClientConfigs)(clientList.get(i))).id;
			st=new StringTokenizer(tempFlagString);
			while(st.hasMoreTokens()){
				flagList.add(new ID_Flag(tempClientID,st.nextToken()));
			}
		}
		flagListCount=flagList.size();
		
		sp.receiveData=new ReceiveDataBean();
        timeFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        date = new Date();
        tempDate=dateFormat.format(date);
		ID=1;
		analyser= new TelegraphAnalyser(sp.in);

	}
	
	@Override
	public void run(){
		while(analyser.getStopbit()==TelegraphAnalyser.RUN
				||analyser.getStopbit()==TelegraphAnalyser.SAFELYSTOP){
			if(analyser.analysisNextChar()){
				count += 1;
				System.out.println("��" + count + "�ν��յ��籨 ~~��"); // TODO ���������
				sp.receiveData.teleflag=analyser.getTeleflag();
				sp.receiveData.teletext=analyser.getTeleContent();
				sp.receiveData.clientIDList=dispatcher(sp.receiveData.teletext);
		        date = new Date();
				sp.receiveData.rec_date=timeFormat.format(date);
				if (!(dateFormat.format(date).equalsIgnoreCase(tempDate))){
					tempDate=dateFormat.format(date);
					ID=1;
				}
				sp.receiveData.rec_id=ID;
				ID=ID+1;
				sp.receiveData.v_flag="0";
				sp.TelegramReady();
			}
			blink.change=true;
		}

		sp.closePort();
	
		MainWindow.mainBoard.addMsg("Serial port closed.", LocalBoard.INFO_SYSTEM);

	}

	/**
	 * dispatch telegraph to its corresponde TCP/IP client
	 * 
	 * @param flag
	 *            :String to be analyse
	 * @return: a ArrayList With element type of int
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ArrayList dispatcher(String flag) {
		@SuppressWarnings("rawtypes")
		ArrayList idList =new ArrayList();
		for (int i=0;i<flagListCount;i++){
			if (flag.indexOf(((ID_Flag)(flagList.get(i))).flag)>=0)
				if (idList.indexOf(new Integer(((ID_Flag)(flagList.get(i))).client_id))<0)
					idList.add(new Integer(((ID_Flag)(flagList.get(i))).client_id));
		}
		if (idList.size()==0)
			idList.add(new Integer(1));//if no client recieve,send telegraph to client No.1
		return idList;
	}

	public void stopSafely() {
		analyser.StopSafely();
		blink.stop();
		if (!analyser.getWorkingStatus()){
			this.stop();
			MainWindow.mainBoard.addMsg("Serial port closed.", LocalBoard.INFO_SYSTEM);
			MainWindow.serialPortStatus.setBackground(new java.awt.Color(255,0,0));
			sp.closePort();
		}
	}

}

class ID_Flag{
	int client_id;
	String flag;

	ID_Flag(int id,String Flag){
		client_id=id;
		flag=Flag;
	}
}