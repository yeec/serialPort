package bros.manage.business.view;

import java.awt.Point;
import java.awt.Rectangle;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JViewport;

import bros.manage.constants.StaticSettings;


public class LocalBoard extends JList {

   /**
    * 
    */
private String logfileName;
	
	private static final long serialVersionUID = -8688882408943316999L;
	public static final int INFO_ERROR = 11111;
	public static final int INFO_SYSTEM = 22222;
	public static final int INFO_DATA = 33333;
	public static final int INFO_LOG = 44444;
	
	Date date;
	SimpleDateFormat timeFormat;
	String tempDate;
	File logfile;
	
//	private Vector data;
	@SuppressWarnings("rawtypes")
	private DefaultListModel data;
	private LocaleCellRenderer cellRenderer;

	private SimpleDateFormat dateFormat;
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public LocalBoard(){
		super();
		data = new DefaultListModel();
		this.setModel(data);
//		data = new Vector();
		cellRenderer = new LocaleCellRenderer();
		this.setCellRenderer(cellRenderer);
		
        timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        date = new Date();
        tempDate=timeFormat.format(date);
	}

	@SuppressWarnings("unchecked")
	public void addMsg(String text, int type){
        date = new Date();//chairmand add
        tempDate=timeFormat.format(date);//chairmand add
		String msg = "";
		JLabel jl = new JLabel();
		switch(type){
		case INFO_ERROR : 
			msg ="[" + tempDate+ "]" + "[ERROR]:  " + text;
			jl.setBackground(StaticSettings.errColor);
			break;
		case INFO_DATA:
			msg ="[" + tempDate+ "]" + "[DATA]:  " + text;
			jl.setBackground(StaticSettings.dataInfoColor);
			break;
		case INFO_SYSTEM:
			msg ="[" + tempDate+ "]" + "[SYSTEM]:  " + text;
			jl.setBackground(StaticSettings.sysInfoColor);
			break;
		case INFO_LOG:
			msg ="[" + tempDate+ "]" + "[LOG]:  " + text;
			break;
		}
		
		jl.setText(msg);
		if(type != INFO_LOG){
			writeLog(msg);
		
			while(data.size() > StaticSettings.displayBufferSize) {
				data.remove(0);
			}
			data.addElement(jl);
			if(data.size() > 4){
				makeLastVisible();
				this.repaint();
			}
		}
			
	}
	
	public void makeLastVisible(){
		if(! (this.getParent() instanceof JViewport) ){
			return;
		}
		JViewport viewport = (JViewport)this.getParent();
		Rectangle rect = this.getCellBounds(data.size()-2, data.size()-1);
		Point pt = viewport.getViewPosition();
		rect.setLocation(rect.x-pt.x, rect.y-pt.y);
		viewport.scrollRectToVisible(rect);
		viewport.repaint();
		
	}
	
	private void writeLog(String log){
		
		//File testfile= new File(logfileName);
		FileWriter fout;
		File logfile;
		
		fout =null;
		logfile = null;
		
		logfileName = dateFormat.format(date) + ".txt";
		
		try {
			logfile = new File(logfileName);
			//fw = new FileWriter(logfileName, true);
			fout = new FileWriter(logfile, true);
		} catch (Exception e) {}
		
		try {
			if(fout != null)
				fout.write(log + "\r\n");
		} catch (IOException e) {}
		
		try {
			if(fout != null)
				fout.close();
		} catch (Exception e) {}
		
	}

}
