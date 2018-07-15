package bros.manage.main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileNotFoundException;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;

import bros.manage.business.service.SPTelegramListener;
import bros.manage.business.view.LocalBoard;
import bros.manage.business.view.TelegraphMain;
import bros.manage.entity.ReceiveDataBean;
import bros.manage.event.SPTelegramEvent;
import bros.manage.util.TelegramSerialPort;
import bros.mange.options.JDialogOptions;

/**
 * This code was generated using CloudGarden's Jigloo SWT/Swing GUI Builder,
 * which is free for non-commercial use. If Jigloo is being used commercially
 * (ie, by a corporation, company or business for any purpose whatever) then you
 * should purchase a license for each developer using Jigloo. Please visit
 * www.cloudgarden.com for details. Use of Jigloo implies acceptance of these
 * licensing terms. ************************************* A COMMERCIAL LICENSE
 * HAS NOT BEEN PURCHASED for this machine, so Jigloo or this code cannot be
 * used legally for any corporate or commercial purpose.
 * *************************************
 */
public class MainWindow extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1499933012703850522L;
	private JButton jButtonStop;
	private JButton jButtonStart;
	private JScrollPane jScrollPane1;
	private JMenuItem configMenuItem;
	private JMenu jMenu4;
	private JMenuItem exitMenuItem;
	private JSeparator jSeparator2;
	private JMenuItem stopMenuItem;
	private JMenuItem startMenuItem;
	private JMenu jMenu3;
	private JMenuBar jMenuBar1;

	static public LocalBoard mainBoard;
	static public JTextArea recieveBoard,sendBoard;

	public static  TelegramSerialPort sp;

	private JLabel jLabel4;
	private JLabel jLabel5;
	private JLabel jLabel3;
	private JScrollPane jScrollPane3;
	private JScrollPane jScrollPane2;
	static public JLabel serialPortStatus;
	private JLabel jLabel1;
	private JLabel dbLabel;
	private JButton jButtonConfig;

	public static ReceiveDataBean tempdata;

	public static JLabel databaseStatus;

	public MainWindow() {
		super();
		initGUI();
		this.setVisible(true);
	}

	public MainWindow(String title) {
		super(title);
		initGUI();
		this.setVisible(true);
		this.setSize(888, 700);
	}

	private void initGUI() {
		try {
			setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
			{
				this.getContentPane().setLayout(null);
				this.setResizable(false);
			}
			this.setSize(885, 700);
			{
				jMenuBar1 = new JMenuBar();
				setJMenuBar(jMenuBar1);
				{
					jMenu3 = new JMenu();
					jMenuBar1.add(jMenu3);
					jMenu3.setText("ϵͳ");
					jMenu3.setFont(new java.awt.Font("Dialog",1,18));
					{
						startMenuItem = new JMenuItem();
						jMenu3.add(startMenuItem);
						startMenuItem.setText("��ʼ�շ���");
						startMenuItem.setFont(new java.awt.Font("Dialog",1,18));
						startMenuItem.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								startActionPerformed(evt);
							}
						});
					}
					{
						stopMenuItem = new JMenuItem();
						jMenu3.add(stopMenuItem);
						stopMenuItem.setText("ֹͣ�շ���");
						stopMenuItem.setEnabled(false);
						stopMenuItem.setFont(new java.awt.Font("Dialog",1,18));
						stopMenuItem.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								stopActionPerformed(evt);
							}
						});

					}
					{
						jSeparator2 = new JSeparator();
						jMenu3.add(jSeparator2);
					}
					{
						exitMenuItem = new JMenuItem();
						jMenu3.add(exitMenuItem);
						exitMenuItem.setText("�˳�����");
						exitMenuItem.setFont(new java.awt.Font("Dialog",1,18));
						exitMenuItem.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								exitMenuItemActionPerformed(evt);
							}
						});
					}
				}
				{
					jMenu4 = new JMenu();
					jMenuBar1.add(jMenu4);
					jMenu4.setText("����");
					jMenu4.setFont(new java.awt.Font("Dialog",1,18));
					{
						configMenuItem = new JMenuItem();
						jMenu4.add(configMenuItem);
						configMenuItem.setText("��������");
						configMenuItem.setFont(new java.awt.Font("Dialog",1,18));
						configMenuItem.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								configMenuItemActionPerformed(evt);
							}
						});
					}
				}
				{
					jScrollPane1 = new JScrollPane();
					this.getContentPane().add(jScrollPane1);
					jScrollPane1.setBounds(20, 430, 848, 120);
					jScrollPane1.setAutoscrolls(true);
					//jScrollPane1.
				}
				{
					jButtonStart = new JButton();
					this.getContentPane().add(jButtonStart);
					jButtonStart.setText("��ʼ�շ���");
					jButtonStart.setBounds(550, 570, 130, 30);
					jButtonStart.setFont(new java.awt.Font("Dialog",1,18));
					jButtonStart.addMouseListener(new MouseAdapter() {
						public void mouseClicked(MouseEvent evt) {
							jButtonStartMouseClicked(evt);
						}
					});
				}
				{
					jButtonStop = new JButton();
					this.getContentPane().add(jButtonStop);
					jButtonStop.setText("ֹͣ�շ���");
					jButtonStop.setEnabled(false);
					jButtonStop.setBounds(700, 570, 130, 30);
					jButtonStop.setFont(new java.awt.Font("Dialog",1,18));
					jButtonStop.addMouseListener(new MouseAdapter() {
						public void mouseClicked(MouseEvent evt) {
							jButtonStopMouseClicked(evt);
						}
					});
				}
				{
					jButtonConfig = new JButton();
					this.getContentPane().add(jButtonConfig);
					jButtonConfig.setText("��������");
					jButtonConfig.setBounds(400, 570, 130, 30);
					jButtonConfig.setFont(new java.awt.Font("Dialog",1,18));
					jButtonConfig.addMouseListener(new MouseAdapter() {
						public void mouseClicked(MouseEvent evt) {
							jButtonConfigMouseClicked(evt);
						}
					});
				}
				{
					databaseStatus = new JLabel();
					this.getContentPane().add(databaseStatus);
					databaseStatus.setBounds(149, 577, 15, 18);
					databaseStatus.setBackground(new java.awt.Color(255,0,0));
					databaseStatus.setBorder(BorderFactory.createTitledBorder(""));
					databaseStatus.setOpaque(true);
				}
				{
					dbLabel = new JLabel();
					this.getContentPane().add(dbLabel);
					dbLabel.setText("Database");
					dbLabel.setBounds(58, 571, 92, 30);
					dbLabel.setFont(new java.awt.Font("Dialog",1,18));
				}
				{
					jLabel1 = new JLabel();
					this.getContentPane().add(jLabel1);
					jLabel1.setText("Serial Port");
					jLabel1.setBounds(223, 572, 109, 30);
					jLabel1.setFont(new java.awt.Font("Dialog",1,18));
				}
				{
					serialPortStatus = new JLabel();
					this.getContentPane().add(serialPortStatus);
					serialPortStatus.setBackground(new java.awt.Color(255, 0, 0));
					serialPortStatus.setOpaque(true);
					serialPortStatus.setBorder(BorderFactory.createTitledBorder(""));
					serialPortStatus.setBounds(326, 578, 15, 18);
				}
				{
					jScrollPane2 = new JScrollPane();
					this.getContentPane().add(jScrollPane2);
					jScrollPane2.setBounds(20, 50, 410, 340);
				}
				{
					jScrollPane3 = new JScrollPane();
					this.getContentPane().add(jScrollPane3);
					jScrollPane3.setBounds(458, 50, 410, 340);
				}
				{
					jLabel3 = new JLabel();
					this.getContentPane().add(jLabel3);
					jLabel3.setText("\u63a5\u6536\u7535\u62a5");
					jLabel3.setBounds(29, 11, 84, 30);
					jLabel3.setFont(new java.awt.Font("Dialog",1,18));
				}
				{
					jLabel4 = new JLabel();
					this.getContentPane().add(jLabel4);
					jLabel4.setText("\u53d1\u9001\u7535\u62a5");
					jLabel4.setBounds(473, 13, 84, 30);
					jLabel4.setFont(new java.awt.Font("Dialog",1,18));
				}
				{
					jLabel5 = new JLabel();
					this.getContentPane().add(jLabel5);
					jLabel5.setText("\u7cfb\u7edf\u6d88\u606f");
					jLabel5.setBounds(21, 400, 87, 30);
					jLabel5.setFont(new java.awt.Font("Dialog",1,18));
				}
				{
					recieveBoard = new JTextArea();
					jScrollPane2.setViewportView(recieveBoard);
					recieveBoard.setFont(new java.awt.Font("Dialog",1,18));
					recieveBoard.setEditable(false);
				}
				{
					mainBoard = new LocalBoard();
					jScrollPane1.setViewportView(mainBoard);
					mainBoard.setFont(new java.awt.Font("Dialog",1,18));
					//mainBoard.setBounds(0, 0, 160, 65);
					//mainBoard.setPreferredSize(new java.awt.Dimension(611, 351));
				}
				{
					sendBoard = new JTextArea();
					jScrollPane3.setViewportView(sendBoard);
					sendBoard.setFont(new java.awt.Font("Dialog",1,18));
					sendBoard.setEditable(false);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * ֹͣ���У��˳�����
	 * @param evt
	 */
	private void exitMenuItemActionPerformed(ActionEvent evt) {
		this.dispose();
		stopTelegram();
		System.exit(0);
	}

	/**
	 * ��������
	 * @param evt
	 */
	private void configMenuItemActionPerformed(ActionEvent evt) {	
		new JDialogOptions(this);	
	}

	/**
	 * ��ʼ�������
	 * @param evt
	 */
	private void jButtonStartMouseClicked(MouseEvent evt) {

		startTelegram();
		jButtonStart.setEnabled(false);
		jButtonStop.setEnabled(true);
		jButtonConfig.setEnabled(false);
		configMenuItem.setEnabled(false);
		startMenuItem.setEnabled(false);
		stopMenuItem.setEnabled(true);
	}

	/**
	 * ��������
	 * @param evt
	 */
	private void jButtonConfigMouseClicked(MouseEvent evt) {
		new JDialogOptions(this);	
	}

	/**
	 * ֹͣ��ݽ��պͷ���
	 * @param evt
	 */
	private void jButtonStopMouseClicked(MouseEvent evt) {
		stopTelegram();
		jButtonStart.setEnabled(true);
		jButtonStop.setEnabled(false);
		jButtonConfig.setEnabled(true);
		configMenuItem.setEnabled(true);
		startMenuItem.setEnabled(true);
		stopMenuItem.setEnabled(false);
	}

	private void startActionPerformed(ActionEvent evt) {

		startTelegram();
		jButtonStart.setEnabled(false);
		jButtonStop.setEnabled(true);
		jButtonConfig.setEnabled(false);
		configMenuItem.setEnabled(false);
		startMenuItem.setEnabled(false);
		stopMenuItem.setEnabled(true);

	}

	private void stopActionPerformed(ActionEvent evt) {
		stopTelegram();
		jButtonStart.setEnabled(true);
		jButtonStop.setEnabled(false);
		jButtonConfig.setEnabled(true);
		configMenuItem.setEnabled(true);
		startMenuItem.setEnabled(true);
		stopMenuItem.setEnabled(false);

	}

	private void stopTelegram() {

		MainWindow.mainBoard.addMsg("System stopping ...", LocalBoard.INFO_SYSTEM);
		try {
			// stop serialport reading thread.
			if (sp != null) {
				sp.stopReceiveTelegraph();
				sp = null;
			}
			
			// TODO ����־
			//			TelegraphLogService.saveReceiveSendStatusChangeLog(false, true, null);
		} catch (Exception e) {
			//			TelegraphLogService.saveReceiveSendStatusChangeLog(false, false, e);
		}

	}

	private void startTelegram() {
		sp.startReceiveTelegraph();
		MainWindow.mainBoard.addMsg("System started.", LocalBoard.INFO_SYSTEM);
	}
}

class ReceiveSPData implements SPTelegramListener {
	private int count = 0,countError;

	public void DealTelegramEvent(SPTelegramEvent telegramEvent) {
		//		count += 1;
		//		System.out.println("��" + count + "�ν��յ��籨 ��"); // TODO ���������
		//write serialport data to database;
		ReceiveDataBean data_readin = MainWindow.sp.readData();
		MainWindow.recieveBoard.setText(data_readin.teletext);

		MainWindow.mainBoard.addMsg("�籨д����ݿ�", LocalBoard.INFO_LOG);
		// TODO ��ʼ��ֵ
		final String mainKey = UUID.randomUUID().toString();
		//			String logSQL = "INSERT INTO TEL_T_RECEIVE_QUEUE(LOG_NUM, LOG_TIME, SYS_DEAL_TYPE, SYS_STATE, LOG_SYS, LOG_MEMO, LOG_GRADE, LOG_CLASS, USERID, DELA_IP, DELA_MAC) VALUES(ATMLOG.LOG_SEQ.NEXTVAL, sysdate, '����', '�ɹ�', '�籨����ϵͳ', '', '1', 'ֵ��', 'SYSTEM_TEL', '192.168.1.251', '48-0F-CF-4A-A4-AD')";
		if (!MainWindow.db.writeData(mainKey,data_readin,DeviceInfo.getDeviceIp(),DeviceInfo.getDeviceMAC())) {

			//				TelegraphLogService.saveReceiveQueueDealLog(mainKey, MainWindow.db.getmSql(), false, null);						
			if(TelegraphMain.route == TelegraphMain.DATABASE)
				MainWindow.mainBoard.addMsg("Database offline!", LocalBoard.INFO_ERROR);

			// turn the route to socket.
			TelegraphMain.route = TelegraphMain.SOCKET;
			// write data to socket and file;				

			//	write serialport data to socket;
			MainWindow.mainBoard.addMsg("�籨д��Socket 1", LocalBoard.INFO_LOG);
			MainWindow.ts.writeData(data_readin);//removed by chairmand
			// meanwhile, write data to file
			MainWindow.mainBoard.addMsg("д����־�ļ� 1", LocalBoard.INFO_LOG);
			MainWindow.tf.writeData(data_readin);

		} else { // TODO ��־
			//				TelegraphLogService.saveLastStoreLog();
			//				TelegraphLogService.saveReceiveQueueDealLog(mainKey, MainWindow.db.getmSql(), true, null);
		}
	}

}

/**
 * ����ݿ��ȡ���͵ı��ģ�ͨ��ڷ���
 * 
 * @author jackflit
 *
 */
class ReceiveDBData implements DBTelegramListener {

	public void dealDBTelegramEvent(DBTelegramEvent evt) {

		/*
		 * if Database read thread sent out an event, that means the database is
		 * already online. so, turn the route to the database first, then send
		 * data to serial port.
		 */
		try {
			do {
				/**
				 * 1. ������ݿ������TeleDatabase���� 2. db.readData()����һ��ResultSet
				 */
				// ������ݵ�����

				// ����ݿ��ȡҪ���͵ı���
				String dataToSend = MainWindow.db.readData().getString(1);
				// ��ʾ�������ڷ����ı���
				MainWindow.sendBoard.setText(dataToSend);
				//				TelegraphLogService.saveLastSendLog();
				// ͨ��ڷ��ͳ�ȥ sp���Ǵ���
				// TODO �˴��жϵ籨�Ƿ��ͳɹ��������д��ɹ�����Ϊ���ͳɹ�������Ϊʧ�ܣ��޸�ԭд��������boolean����
				if (!MainWindow.sp.sentData(dataToSend)) {
					//					TelegraphLogService.saveSendTelegraphErrorLog();
				}
				// change the v_sendflag field to '1';  �޸ĸ�����¼״̬Ϊ�ѷ���
				MainWindow.db.readData().updateString("SEND_FLAG", "1");
				//				String mainKey = MainWindow.db.readData().getNString("TEL_ID");
				/** TODO ������Ҫ��ȡmainkey
				ResultSetMetaData rsmd = MainWindow.db.readData().getMetaData();// ��ȡ�м�
				int col = rsmd.getColumnCount(); // ��ȡ�еĸ���
				 **/
				String mainKey = MainWindow.db.readData().getMetaData().getColumnName(0);
				// TODO ��ʼ��ֵ
				String logSQL = "UPDATE TEL_T_SEND_QUEUE SET SEND_FLAG='1' WHERE TEL_ID = '"+mainKey+"'";
				// ������ݿ��¼
				try {
					MainWindow.db.readData().updateRow();
					//					TelegraphLogService.saveUpdateFlagLog(mainKey, logSQL, true);
				} catch (Exception e) {
					//					TelegraphLogService.saveUpdateFlagLog(mainKey, logSQL, false);
				}
				//				TelegraphLogService.saveSendTelegraphSuccessLog(mainKey);
				// �α��ƶ�����һ������
			} while (MainWindow.db.readData().next());

			// close resultset.
			MainWindow.db.readData().close();
		} catch (SQLException e) {
			//			TelegraphLogService.saveSendExceptionLog(e);
			if (TelegraphMain.route == TelegraphMain.DATABASE)
				MainWindow.mainBoard.addMsg("Database offline!", LocalBoard.INFO_ERROR);

			TelegraphMain.route = TelegraphMain.SOCKET;
			e.printStackTrace();
		}

	}
}

class ReceiveSocketData implements SocketTelegramListener {

	public void dealSocketTelegramEvent(cn.edu.tsinghua.event.SocketTelegramEvent evt) {
		if (TelegraphMain.route == TelegraphMain.SOCKET) {
			//			String data=MainWindow.ts.readData();
			//			
			//			MainWindow.sendBoard.setText(data);
			//			MainWindow.sp.sentData(data);//removed by chairmand
		}else{
			//			String data=MainWindow.ts.readData();
			//			
			//			MainWindow.mainBoard.addMsg("From clients, not sending: "+data, LocalBoard.INFO_DATA);
			//removed by chairmand
		}
	}
}
