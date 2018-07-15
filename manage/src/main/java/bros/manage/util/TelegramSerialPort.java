/**
 * 
 */
package bros.manage.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StreamTokenizer;
import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;








import bros.manage.business.service.SPTelegramListener;
import bros.manage.business.view.LocalBoard;
import bros.manage.entity.ReceiveDataBean;
import bros.manage.event.SPTelegramEvent;
import bros.manage.main.MainWindow;
import bros.mange.options.ClientTableModel;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;

/**
 * @author yangj
 *
 */
public class TelegramSerialPort implements SerialPortEventListener {

	SerialParameters serialParameters;
    CommPortIdentifier portId;
    SerialPort serialPort;
    OutputStream out;
    InputStream  in;
    StreamTokenizer st;
	StringBuffer inbuf;
	ClientTableModel clientTableModel;
	ReceiveDataBean receiveData;
	ReceiveTelegraph receiveTelegraph; 

	/* create a listener list */
	private Set listeners;

	
	public TelegramSerialPort(){
			listeners=new HashSet();
//			Configuration XMLconfig=new Configuration();
//			serialParameters=XMLconfig.readSPConfig();
//			clientTableModel=XMLconfig.readClientsTableModel();
			initSerialPort();
 			receiveTelegraph= new ReceiveTelegraph(this);
	}

	public boolean initSerialPort(){
        try 
        {
            portId = CommPortIdentifier.getPortIdentifier(serialParameters.getPortName());
            try 
            {
                serialPort = (SerialPort)portId.open("TELEGRAM_App", 1000);
            } catch (PortInUseException e) 
            {
                return false;
            }
            
            //Use InputStream in to read from the serial port, and OutputStream
            //out to write to the serial port.
            
            try 
            {
                in  = serialPort.getInputStream();
                out = serialPort.getOutputStream();
            } 
            catch (IOException e) 
            {	
            	closePort();
    			e.printStackTrace();
                return false;
            }
            //Initialize the communication
            try 
            {
                 serialPort.setSerialPortParams(serialParameters.getBaudRate(),
                		 serialParameters.getDatabits(),
                		 serialParameters.getStopbits(),
                		 serialParameters.getParity());
            } catch (UnsupportedCommOperationException e) 
            {
            	closePort();
    			e.printStackTrace();
                return false;
            }
            try
            {
            	serialPort.setFlowControlMode(serialParameters.getFlowControlIn()|serialParameters.getFlowControlOut());
            }catch(UnsupportedCommOperationException e)
            {
            	closePort();
    			e.printStackTrace();
                return false;
            }
        } catch (NoSuchPortException e) 
        {        		
				e.printStackTrace();
                return false;
        }
		return true;
	}

	// TODO 修改此处的返回值，在初始化成功或失败的地方作记录MainWindow Line 595
	public boolean sentData(String data) {
		try {
			out.write(data.getBytes("US-ASCII"));
			return true;
		}  catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	public ReceiveDataBean readData() {
		return receiveData;
	}

	public void closePort() {
		serialPort.close();
	}

	
	public void startReceiveTelegraph() {
		receiveTelegraph.start();
	}

	public void stopReceiveTelegraph() {
		receiveTelegraph.stopSafely();
	}

	public void serialEvent(SerialPortEvent evt) {
		switch (evt.getEventType()){
			case SerialPortEvent.DATA_AVAILABLE:
					TelegramReady();
					break; 
		}
	}

	public void finalize() {
		serialParameters = null;
		portId = null;
		serialPort = null;
		out = null;
		in = null;
		st = null;
		inbuf = null;
		clientTableModel = null;
		receiveData = null;
		receiveTelegraph = null;
		listeners = null;
	}

	/*add a listener to listener list*/
	public void addListener(SPTelegramListener telegramListener){
		synchronized (listeners) {
			listeners.add(telegramListener);
		}
	}
	
	/* when event happens... */
	public void TelegramReady() {
		MainWindow.mainBoard.addMsg("收到一份电报", LocalBoard.INFO_LOG);
		// 先Log到本地
		MainWindow.mainBoard.addMsg("写入日志文件", LocalBoard.INFO_LOG);
//		TelegraphLogger.getInstance().log(this.receiveData.teletext);
//		TelegraphLogService.saveLastReceiveLog(); // TODO 保存最后接收的电报日志						

		Iterator iterator;
		// synchronized(listeners){
		iterator = new HashSet(listeners).iterator();
		// }
		SPTelegramEvent telegramEvent = new SPTelegramEvent(this);
		/* let listeners work */
		while (iterator.hasNext()) {
			SPTelegramListener telegramListener = (SPTelegramListener) iterator.next();
			telegramListener.DealTelegramEvent(telegramEvent);
		}
	}

}
