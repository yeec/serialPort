package bros.mange.options;

import java.util.ArrayList;

import javax.swing.table.*;

import bros.manage.entity.ClientConfigs;


public class ClientTableModel extends AbstractTableModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 972312064630245442L;
	private String[][] data ={ };
	final String[] titles = {"ID","Address","Port","Flag"};
	public int getColumnCount() { return titles.length; }
	public int getRowCount() { return data.length;}
	public String getStringAt(int row, int col) {return data[row][col];}
	public String getColumnName(int column) {return titles[column];}
	public boolean isCellEditable(int row, int col) {return (col==4);}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ArrayList getClientList(){
		ArrayList clientList = new ArrayList(); 
		int size=getRowCount();
		for (int i=0;i<size;i++){
			ClientConfigs client = new ClientConfigs();
			client.id=Integer.valueOf(getStringAt(i,0)).intValue();
			client.address=getStringAt(i,1);
			client.port=getStringAt(i,2);
			client.flag=getStringAt(i,3);
			clientList.add(client);
		}
		return clientList;
	}



	public void setValueAt(String aValue, int row, int column) {
		data[row][column] = aValue; }
	public void addRow(String address,String port,String flag) {
		String[][] dataTemp = new String[getRowCount() + 1][getColumnCount()];
		System.arraycopy(data, 0, dataTemp, 0, getRowCount());
		dataTemp[getRowCount()][0]=Integer.toString(getRowCount()+1);
		dataTemp [getRowCount()][1] =address;
		dataTemp [getRowCount()][2] =port;
		dataTemp [getRowCount()][3] =flag;
		data = dataTemp;
		fireTableDataChanged();

	}

	public void deleteRow(int index) throws NoClientException{
		if (data.length>0){
			String[][] dataTemp = new String[data.length][titles.length];
			System.arraycopy(data, 0, dataTemp, 0, data.length);
			data=new String[dataTemp.length-1][titles.length];
			System.arraycopy(dataTemp,0,data,0,index);
			System.arraycopy(dataTemp,index+1,data,index,dataTemp.length-1-index);
			//redefine ID;
			for (int i=index;i<data.length;i++){
				data[i][0]=Integer.toString(i+1);
			}
			fireTableDataChanged();
		}
		else{
			throw new NoClientException();
		}
	}

	public void modifyRow(int index, String address,String port,String flag){
		data [index][1] = address;
		data [index][2] = port;
		data [index][3] = flag;
		fireTableDataChanged();
	}
	public Object getValueAt(int row, int col) {
		return data[row][col];
	}
}

class NoClientException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7938505240490776173L;
}