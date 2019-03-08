package bros.manage.entity;

import java.io.Serializable;
import java.util.ArrayList;

public class ReceiveDataBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5245524750419416284L;

	public String rec_date; //yyyy-mm-dd hh24-mm-ss

	public long rec_id;

	public String teletext;

	public String teleflag;

	public String v_flag;
	
	public ArrayList clientIDList;

	public ReceiveDataBean() {
		
		rec_date = null;
		rec_id = -1;
		teletext = null;
		teleflag = null;
		v_flag = null;
		clientIDList=null;
	}
	

	ReceiveDataBean(String rec_date, long rec_id, String teletext, String teleflag,
			String v_flag, ArrayList idList) {
		
		this.rec_date = rec_date;
		this.rec_id = rec_id;
		this.teletext = teletext;
		this.teleflag = teleflag;
		this.v_flag = v_flag;
		this.clientIDList = idList;
	}
	
//	public String toString(){
//		
//		return rec_date + "\t" + rec_id + "\t" + teletext + "\t" + teleflag 
//					+ "\t" + v_flag + "\t" + client_id;
//	}

}