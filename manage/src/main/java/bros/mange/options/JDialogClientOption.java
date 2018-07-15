package bros.mange.options;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JButton;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

/**
* This code was generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* *************************************
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED
* for this machine, so Jigloo or this code cannot be used legally
* for any corporate or commercial purpose.
* *************************************
*/
public class JDialogClientOption extends javax.swing.JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1403934977114255280L;
	private JDialogOptions parentDlg;
	private JTextField jTextFieldAddress;
	private JTextField jTextFieldPort;
	private JLabel jLabel2;
	private JButton jButtonCancel;
	private JButton jButtonOK;
	private JLabel jLabel3;
	private JLabel jLabel1;
	private JTextField jTextFieldFlag;
	private String dlgTitle;
	private int clientIndex;
	private JLabel jLabel4;
	/**
	* Auto-generated main method to display this JDialog
	*/

	public JDialogClientOption(JDialogOptions parentdlg,String title) {
		super(parentdlg,title);
		parentDlg=parentdlg;
		dlgTitle=title;
		initGUI();
	}
	
	public JDialogClientOption(JDialogOptions parentdlg,String title,
			int index,String address,String port,String flag) {
		super(parentdlg,title);
		parentDlg=parentdlg;
		dlgTitle=title;
		clientIndex=index;
		initGUI();
	}
	
	private void initGUI() {
		try {
			{
				this.getContentPane().setLayout(null);
				this.setBounds(100,100,360,260);
				this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
				this.addWindowListener(new WindowAdapter() {
					public void windowClosed(WindowEvent evt) {
						rootWindowClosed(evt);
					}
				});
				{
					jTextFieldAddress = new JTextField();
					this.getContentPane().add(jTextFieldAddress);
					jTextFieldAddress.setBounds(136, 45, 160, 20);
					jTextFieldAddress.setAutoscrolls(false);
				}
				{
					jTextFieldPort = new JTextField();
					this.getContentPane().add(jTextFieldPort);

					jTextFieldPort.setBounds(136, 85, 160, 20);
				}
				{
					jTextFieldFlag = new JTextField();
					this.getContentPane().add(jTextFieldFlag);

					jTextFieldFlag.setBounds(136, 125, 160, 20);
				}
				if (dlgTitle=="��ӿͻ���"){
					jTextFieldAddress.setText("0.0.0.0");
					jTextFieldAddress.setFont(new java.awt.Font("Dialog",0,18));
					jTextFieldAddress.setPreferredSize(new java.awt.Dimension(4, 22));
					jTextFieldPort.setText("8000");
					jTextFieldPort.setFont(new java.awt.Font("Dialog",0,18));
					jTextFieldFlag.setText("flag1 flag2");
					jTextFieldFlag.setFont(new java.awt.Font("Dialog",0,18));
				}
				else if (dlgTitle=="�޸Ŀͻ���"){
					jTextFieldAddress.setText(parentDlg.clientTableModel.getStringAt(clientIndex,1));
					jTextFieldPort.setText(parentDlg.clientTableModel.getStringAt(clientIndex,2));
					jTextFieldFlag.setText(parentDlg.clientTableModel.getStringAt(clientIndex,3));
				}
				{
					jLabel1 = new JLabel();
					this.getContentPane().add(jLabel1);
					jLabel1.setText("IP��ַ:");
					jLabel1.setBounds(36, 45, 87, 20);
					jLabel1.setFont(new java.awt.Font("Dialog",1,18));
				}
				{
					jLabel2 = new JLabel();
					this.getContentPane().add(jLabel2);
					jLabel2.setText("�˿�:");
					jLabel2.setBounds(36, 85, 87, 20);
					jLabel2.setFont(new java.awt.Font("Dialog",1,18));
				}
				{
					jLabel3 = new JLabel();
					this.getContentPane().add(jLabel3);
					jLabel3.setText("��־:");
					jLabel3.setBounds(36, 125, 87, 20);
					jLabel3.setFont(new java.awt.Font("Dialog",1,18));
				}
				{
					jButtonOK = new JButton();
					this.getContentPane().add(jButtonOK);
					jButtonOK.setText("ȷ��");
					jButtonOK.setBounds(80, 195, 80, 20);
					jButtonOK.addMouseListener(new MouseAdapter() {
						public void mouseClicked(MouseEvent evt) {
							jButtonOKMouseClicked(evt);
						}
					});
				}
				{
					jButtonCancel = new JButton();
					this.getContentPane().add(jButtonCancel);
					jButtonCancel.setText("ȡ��");
					jButtonCancel.setBounds(180, 195, 80, 20);
					jButtonCancel.addMouseListener(new MouseAdapter() {
						public void mouseClicked(MouseEvent evt) {
							jButtonCancelMouseClicked(evt);
						}
					});
				}
				{
					jLabel4 = new JLabel();
					this.getContentPane().add(jLabel4);
					jLabel4.setText("���ÿո�ָ������־ ��AAA  BBB  CCC");
					jLabel4.setBounds(7, 157, 333, 30);
					jLabel4.setFont(new java.awt.Font("Dialog",1,16));
				}
			}
			this.setSize(343, 247);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void jButtonOKMouseClicked(MouseEvent evt) {
		if (dlgTitle=="��ӿͻ���")
			parentDlg.clientTableModel.addRow(jTextFieldAddress.getText(),
					jTextFieldPort.getText(),jTextFieldFlag.getText());
		else if (dlgTitle=="�޸Ŀͻ���")
			parentDlg.clientTableModel.modifyRow(clientIndex,jTextFieldAddress.getText(),
					jTextFieldPort.getText(),jTextFieldFlag.getText());
		this.dispose();		
	}
	
	private void jButtonCancelMouseClicked(MouseEvent evt) {
		this.dispose();
	}
	
	private void rootWindowClosed(WindowEvent evt) {
		parentDlg.setEnabled(true);	
		parentDlg.requestFocus();	
	}
}
