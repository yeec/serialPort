package bros.mange.options;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.ListSelectionModel;

import bros.manage.util.SerialParameters;

import java.awt.Point;
import java.io.FileNotFoundException;
import java.util.Enumeration;

import gnu.io.CommPortIdentifier;

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
public class JDialogOptions extends javax.swing.JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = -162439852265569721L;
	private JFrame parentframe;
	private JTabbedPane jTabbedPaneOptions;
	private JPanel jPanelSPOptions;
	private JPanel jPanelClientOptions;
	private JPanel jPanelDBOptions;
	private JComboBox jComboBoxPortName;
	private JComboBox jComboBoxBaudRate;
	private JLabel jLabel8;
	private JTextField jTextFieldServiceName;
	private JTextField jTextFieldPassword;
	private JTextField jTextFieldUsername;
	private JTextField jTextFieldServerPort;
	private JTextField jTextFieldServerAddress;
	private JLabel jLabel12;
	private JLabel jLabel11;
	private JLabel jLabel10;
	private JTable jTableClients;
	private JButton jButtonModify;
	private JButton jButtonDelete;
	private JButton jButtonAdd;
	private JButton jButtonCancel;
	private JButton jButtonOK;
	private JButton jButtonReset;
	private JComboBox jComboBoxFlowCtlOut;
	private JComboBox jComboBoxFlowCtlIn;
	private JComboBox jComboBoxParity;
	private JLabel jLabel7;
	private JLabel jLabel6;
	private JLabel jLabel4;
	private JLabel jLabel9;
	private JScrollPane jScrollPaneClientOption;
	private JLabel jLabel5;
	private JComboBox jComboBoxStopbits;
	private JLabel jLabel3;
	private JLabel jLabel2;
	private JLabel jLabel1;
	private JComboBox jComboBoxDatabits;

	private SerialParameters serialParameters;
	public ClientTableModel clientTableModel;


	public JDialogOptions(JFrame parentFrame) {
		super(parentFrame, "参数设置");
		this.parentframe = parentFrame;
//		LoadXMLFile();
		initGUI();

		parentFrame.setEnabled(false);
		this.setVisible(true);
	}

	public JDialogOptions(JFrame parentFrame, String title) {
		super(parentFrame, title);
		this.parentframe = parentFrame;
//		LoadXMLFile();
		initGUI();

		this.setVisible(true);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void initGUI() {
		try {
			this.getContentPane().setLayout(null);
			this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			this.setBounds(0, 0, 385, 360);
			this.setFocusable(true);
			this.setFocusableWindowState(true);
			this.setResizable(false);
			this.addWindowListener(new WindowAdapter() {
				public void windowClosed(WindowEvent evt) {
					rootWindowClosed(evt);
				}
			});
			{
				jTabbedPaneOptions = new JTabbedPane();
				this.getContentPane().add(jTabbedPaneOptions);
				jTabbedPaneOptions.setBounds(1, 1, 376, 290);
				{
					jPanelSPOptions = new JPanel();
					jTabbedPaneOptions.addTab("串口参数设置", null, jPanelSPOptions,
							null);
					jPanelSPOptions.setPreferredSize(new java.awt.Dimension(
							331, 239));
					jPanelSPOptions.setLayout(null);
					jPanelSPOptions.setFont(new java.awt.Font("Dialog", 0, 18));
					{
						ComboBoxModel jComboBoxPortNameModel = new DefaultComboBoxModel();
						jComboBoxPortName = new JComboBox();
						jPanelSPOptions.add(jComboBoxPortName);
						jComboBoxPortName.setModel(jComboBoxPortNameModel);
						jComboBoxPortName.setBounds(145, 20, 200, 20);
						jComboBoxPortName.setFont(new java.awt.Font("Dialog",
								1, 18));
						getPortNames();
					}
					{
						ComboBoxModel jComboBoxBaudRateModel = new DefaultComboBoxModel(
								new String[] { "300", "2400", "4800", "9600",
										"19200", "38400", "57600", "115200",
										"230400", "460800" });
						jComboBoxBaudRate = new JComboBox();
						jPanelSPOptions.add(jComboBoxBaudRate);
						jComboBoxBaudRate.setModel(jComboBoxBaudRateModel);
						jComboBoxBaudRate.setBounds(145, 50, 200, 20);
						jComboBoxBaudRate.setFont(new java.awt.Font("Dialog",
								1, 18));
					}
					{
						ComboBoxModel jComboBoxDatabitsModel = new DefaultComboBoxModel(
								new String[] { "5", "6", "7", "8" });
						jComboBoxDatabits = new JComboBox();
						jPanelSPOptions.add(jComboBoxDatabits);
						jComboBoxDatabits.setModel(jComboBoxDatabitsModel);
						jComboBoxDatabits.setBounds(145, 80, 200, 20);
						jComboBoxDatabits.setFont(new java.awt.Font("Dialog",
								1, 18));
					}
					{
						jLabel1 = new JLabel();
						jPanelSPOptions.add(jLabel1);
						jLabel1.setText("端口:");
						jLabel1.setBounds(36, 20, 63, 20);
						jLabel1.setFont(new java.awt.Font("Dialog", 1, 18));
					}
					{
						jLabel2 = new JLabel();
						jPanelSPOptions.add(jLabel2);
						jLabel2.setText("波特率:");
						jLabel2.setBounds(36, 50, 93, 20);
						jLabel2.setFont(new java.awt.Font("Dialog", 1, 18));
					}
					{
						jLabel3 = new JLabel();
						jPanelSPOptions.add(jLabel3);
						jLabel3.setText("数据位:");
						jLabel3.setBounds(36, 80, 94, 20);
						jLabel3.setFont(new java.awt.Font("Dialog", 1, 18));
					}
					{
						ComboBoxModel jComboBoxStopbitsModel = new DefaultComboBoxModel(
								new String[] { "1", "1.5", "2" });
						jComboBoxStopbits = new JComboBox();
						jPanelSPOptions.add(jComboBoxStopbits);
						jComboBoxStopbits.setModel(jComboBoxStopbitsModel);
						jComboBoxStopbits.setBounds(145, 140, 200, 20);
						jComboBoxStopbits.setFont(new java.awt.Font("Dialog",
								1, 18));
					}
					{
						jLabel4 = new JLabel();
						jPanelSPOptions.add(jLabel4);
						jLabel4.setText("停止位:");
						jLabel4.setBounds(36, 140, 88, 20);
						jLabel4.setFont(new java.awt.Font("Dialog", 1, 18));
					}
					{
						jLabel5 = new JLabel();
						jPanelSPOptions.add(jLabel5);
						jLabel5.setText("输入流控制:");
						jLabel5.setBounds(36, 170, 103, 20);
						jLabel5.setFont(new java.awt.Font("Dialog", 1, 18));
					}
					{
						jLabel6 = new JLabel();
						jPanelSPOptions.add(jLabel6);
						jLabel6.setText("输出流控制:");
						jLabel6.setBounds(36, 200, 106, 20);
						jLabel6.setAutoscrolls(true);
						jLabel6.setFont(new java.awt.Font("Dialog", 1, 18));
					}
					{
						jLabel7 = new JLabel();
						jPanelSPOptions.add(jLabel7);
						jLabel7.setText("奇偶校验:");
						jLabel7.setBounds(36, 110, 92, 20);
						jLabel7.setFont(new java.awt.Font("Dialog", 1, 18));
					}
					{
						ComboBoxModel jComboBoxParityModel = new DefaultComboBoxModel(
								new String[] { "None", "Even", "Odd" });
						jComboBoxParity = new JComboBox();
						jPanelSPOptions.add(jComboBoxParity);
						jComboBoxParity.setModel(jComboBoxParityModel);
						jComboBoxParity.setBounds(145, 110, 200, 20);
						jComboBoxParity.setFont(new java.awt.Font("Dialog", 1,
								18));
					}
					{
						ComboBoxModel jComboBoxFlowCtlInModel = new DefaultComboBoxModel(
								new String[] { "None", "Xon/Xoff In",
										"RTS/CTS In" });
						jComboBoxFlowCtlIn = new JComboBox();
						jPanelSPOptions.add(jComboBoxFlowCtlIn);
						jComboBoxFlowCtlIn.setModel(jComboBoxFlowCtlInModel);
						jComboBoxFlowCtlIn.setBounds(145, 170, 200, 20);
						jComboBoxFlowCtlIn.setFont(new java.awt.Font("Dialog",
								1, 18));
					}
					{
						ComboBoxModel jComboBoxFlowCtlOutModel = new DefaultComboBoxModel(
								new String[] { "None", "Xon/Xoff Out",
										"RTS/CTS Out" });
						jComboBoxFlowCtlOut = new JComboBox();
						jPanelSPOptions.add(jComboBoxFlowCtlOut);
						jComboBoxFlowCtlOut.setModel(jComboBoxFlowCtlOutModel);
						jComboBoxFlowCtlOut.setBounds(145, 200, 200, 20);
						jComboBoxFlowCtlOut.setFont(new java.awt.Font("Dialog",
								1, 18));
					}
					LoadSPParameters();
					{
						jButtonReset = new JButton();
						jPanelSPOptions.add(jButtonReset);
						jButtonReset.setText("恢复默认值");
						jButtonReset.setBounds(211, 231, 133, 20);
						jButtonReset
								.setFont(new java.awt.Font("Dialog", 1, 18));
						jButtonReset.addMouseListener(new MouseAdapter() {
							public void mouseClicked(MouseEvent evt) {
								jButtonResetMouseClicked(evt);
							}
						});
					}
				}
				{
					jPanelClientOptions = new JPanel();
					jTabbedPaneOptions.addTab("客户端参数设置", null,
							jPanelClientOptions, null);
					jPanelClientOptions
							.setPreferredSize(new java.awt.Dimension(437, 240));
					jPanelClientOptions.setLayout(null);
					jPanelClientOptions.setFocusCycleRoot(true);
					jPanelClientOptions.setFont(new java.awt.Font("Dialog", 0,
							18));
					{
						jButtonAdd = new JButton();
						jPanelClientOptions.add(jButtonAdd);
						jButtonAdd.setText("添加");
						jButtonAdd.setBounds(32, 230, 74, 20);
						jButtonAdd.setFont(new java.awt.Font("Dialog", 1, 18));
						jButtonAdd.addMouseListener(new MouseAdapter() {
							public void mouseClicked(MouseEvent evt) {
								jButtonAddMouseClicked(evt);
							}
						});
					}
					{
						jButtonDelete = new JButton();
						jPanelClientOptions.add(jButtonDelete);
						jButtonDelete.setText("删除");
						jButtonDelete.setBounds(142, 230, 73, 20);
						jButtonDelete
								.setFont(new java.awt.Font("Dialog", 1, 18));
						jButtonDelete.addMouseListener(new MouseAdapter() {
							public void mouseClicked(MouseEvent evt) {
								jButtonDeleteMouseClicked(evt);
							}
						});
					}
					{
						jButtonModify = new JButton();
						jPanelClientOptions.add(jButtonModify);
						jButtonModify.setText("修改");
						jButtonModify.setBounds(248, 230, 74, 20);
						jButtonModify
								.setFont(new java.awt.Font("Dialog", 1, 18));
						jButtonModify.addMouseListener(new MouseAdapter() {
							public void mouseClicked(MouseEvent evt) {
								jButtonModifyMouseClicked(evt);
							}
						});
					}
					{
						jScrollPaneClientOption = new JScrollPane();
						jPanelClientOptions.add(jScrollPaneClientOption);
						jScrollPaneClientOption.setBounds(6, 15, 357, 201);
						{

							jTableClients = new JTable();
							jScrollPaneClientOption
									.setViewportView(jTableClients);
							jTableClients.setModel(clientTableModel);
							// jTableClients.getTableHeader().setBounds(0, 0,
							// 263, 23);
							jTableClients
									.setPreferredSize(new java.awt.Dimension(
											357, 177));
							jTableClients
									.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
							jTableClients.setColumnSelectionAllowed(false);
							jTableClients.setCellSelectionEnabled(false);
							jTableClients.setRowSelectionAllowed(true);
							jTableClients
									.setIntercellSpacing(new java.awt.Dimension(
											1, 1));
							jTableClients
									.setMaximumSize(new java.awt.Dimension(200,
											32));
							jTableClients.getTableHeader().setResizingAllowed(
									false);
							jTableClients.getTableHeader().addMouseListener(
									new MouseAdapter() {
										public void mousePressed(MouseEvent evt) {
											tableHeaderMousePressed(evt);
										}
									});
						}
					}
				}
				{
					jPanelDBOptions = new JPanel();
					jTabbedPaneOptions.addTab("数据库参数设置", null, jPanelDBOptions,
							null);
					jPanelDBOptions.setLayout(null);
					jPanelDBOptions.setPreferredSize(new java.awt.Dimension(
							278, 252));
					jPanelDBOptions.setFont(new java.awt.Font("Dialog", 0, 18));
					{
						jLabel8 = new JLabel();
						jPanelDBOptions.add(jLabel8);
						jLabel8.setText("数据库IP地址/端口:");
						jLabel8.setBounds(36, 20, 174, 20);
						jLabel8.setFont(new java.awt.Font("Dialog", 1, 18));
					}
					{
						jLabel10 = new JLabel();
						jPanelDBOptions.add(jLabel10);
						jLabel10.setText("用户名:");
						jLabel10.setBounds(36, 70, 100, 20);
						jLabel10.setFont(new java.awt.Font("Dialog", 1, 18));
					}
					{
						jLabel11 = new JLabel();
						jPanelDBOptions.add(jLabel11);
						jLabel11.setText("密码:");
						jLabel11.setBounds(36, 120, 100, 20);
						jLabel11.setFont(new java.awt.Font("Dialog", 1, 18));
					}
					{
						jLabel12 = new JLabel();
						jPanelDBOptions.add(jLabel12);
						jLabel12.setText("服务名:");
						jLabel12.setBounds(36, 170, 100, 20);
						jLabel12.setFont(new java.awt.Font("Dialog", 1, 18));
					}
					{
						jTextFieldServerAddress = new JTextField();
						jPanelDBOptions.add(jTextFieldServerAddress);
						jTextFieldServerAddress.setBounds(36, 45, 160, 22);
						jTextFieldServerAddress.setFont(new java.awt.Font(
								"Dialog", 0, 18));
					}
					{
						jTextFieldServerPort = new JTextField();
						jPanelDBOptions.add(jTextFieldServerPort);
						jTextFieldServerPort.setBounds(206, 45, 54, 23);
						jTextFieldServerPort.setFont(new java.awt.Font(
								"Dialog", 0, 18));
					}
					{
						jTextFieldUsername = new JTextField();
						jPanelDBOptions.add(jTextFieldUsername);
						jTextFieldUsername.setBounds(36, 95, 130, 22);
					}
					{
						jTextFieldPassword = new JTextField();
						jPanelDBOptions.add(jTextFieldPassword);
						jTextFieldPassword.setBounds(36, 145, 130, 20);
					}
					{
						jTextFieldServiceName = new JTextField();
						jPanelDBOptions.add(jTextFieldServiceName);
						jTextFieldServiceName.setBounds(36, 195, 130, 20);
					}
					
					// TODO 动态设置数据库地址，后续修改
					/*jTextFieldServerAddress.setText(DBconfig.serverAddress);
					jTextFieldServerPort.setText(DBconfig.serverPort);
					jTextFieldUsername.setText(DBconfig.username);
					jTextFieldPassword.setText(DBconfig.password);
					jTextFieldServiceName.setText(DBconfig.serviceName);*/
					{
						jLabel9 = new JLabel();
						jPanelDBOptions.add(jLabel9);
						jLabel9.setText(":");
						jLabel9.setBounds(199, 40, 6, 30);
					}
				}
			}
			{
				jButtonOK = new JButton();
				this.getContentPane().add(jButtonOK);
				jButtonOK.setText("确定");
				jButtonOK.setBounds(193, 301, 80, 20);
				jButtonOK.setFont(new java.awt.Font("Dialog", 1, 18));
				jButtonOK.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent evt) {
						jButtonOKMouseClicked(evt);
					}
				});
			}
			{
				jButtonCancel = new JButton();
				this.getContentPane().add(jButtonCancel);
				jButtonCancel.setText("取消");
				jButtonCancel.setBounds(282, 301, 80, 20);
				jButtonCancel.setFont(new java.awt.Font("Dialog", 1, 18));
				jButtonCancel.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent evt) {
						jButtonCancelMouseClicked(evt);
					}
				});
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void LoadSPParameters() {
		jComboBoxPortName.setSelectedItem(serialParameters.getPortName());
		jComboBoxDatabits.setSelectedItem(serialParameters.getDatabitsString());
		jComboBoxBaudRate.setSelectedItem(serialParameters.getBaudRateString());
		jComboBoxParity.setSelectedItem(serialParameters.getParityString());
		jComboBoxStopbits.setSelectedItem(serialParameters.getStopbitsString());
		jComboBoxFlowCtlIn.setSelectedItem(serialParameters
				.getFlowControlInString());
		jComboBoxFlowCtlOut.setSelectedItem(serialParameters
				.getFlowControlOutString());
	};

	private void SetDefaultParameters() {
		serialParameters.setPortName("");
		serialParameters.setBaudRate("300");
		serialParameters.setDatabits("8");
		serialParameters.setStopbits("1");
		serialParameters.setParity("None");
		serialParameters.setFlowControlIn("None");
		serialParameters.setFlowControlOut("None");
	}

	private void jButtonResetMouseClicked(MouseEvent evt) {
		jComboBoxPortName.setSelectedIndex(0);
		jComboBoxDatabits.setSelectedItem("8");
		jComboBoxBaudRate.setSelectedItem("300");
		jComboBoxParity.setSelectedItem("None");
		jComboBoxStopbits.setSelectedItem("1");
		jComboBoxFlowCtlIn.setSelectedItem("None");
		jComboBoxFlowCtlOut.setSelectedItem("None");
	}

	private void jButtonOKMouseClicked(MouseEvent evt) {
		serialParameters.setPortName(jComboBoxPortName.getSelectedItem()
				.toString());
		serialParameters.setBaudRate(jComboBoxBaudRate.getSelectedItem()
				.toString());
		serialParameters.setDatabits(jComboBoxDatabits.getSelectedItem()
				.toString());
		serialParameters.setStopbits(jComboBoxStopbits.getSelectedItem()
				.toString());
		serialParameters
				.setParity(jComboBoxParity.getSelectedItem().toString());
		serialParameters.setFlowControlIn(jComboBoxFlowCtlIn.getSelectedItem()
				.toString());
		serialParameters.setFlowControlOut(jComboBoxFlowCtlOut
				.getSelectedItem().toString());
		//TODO 设置数据库地址
//		DBconfig.serverAddress = jTextFieldServerAddress.getText();
//		DBconfig.serverPort = jTextFieldServerPort.getText();
//		DBconfig.username = jTextFieldUsername.getText();
//		DBconfig.password = jTextFieldPassword.getText();
//		DBconfig.serviceName = jTextFieldServiceName.getText();
		// TODO 保存配置 2018.05.08 修改 加入日志
		//TODO 修改成动态加载数据源
		/*try {
			XMLFile.wirteConfig(serialParameters, DBconfig, clientTableModel);
			// TelegraphLogService.saveSerialParametersLog(serialParameters,
			// true, null);
			// TelegraphLogService.saveDBConfigLog(DBconfig, true, null);
		} catch (Exception e) {
			// TelegraphLogService.saveSerialParametersLog(serialParameters,
			// false, e);
			// TelegraphLogService.saveDBConfigLog(DBconfig, false, e);
		}*/

		this.dispose();
	}

	private void jButtonCancelMouseClicked(MouseEvent evt) {
		this.dispose();
	}

	private void rootWindowClosed(WindowEvent evt) {
		parentframe.setEnabled(true);
		parentframe.requestFocus();
	}

	private void jButtonAddMouseClicked(MouseEvent evt) {
		JDialogClientOption clientOption = new JDialogClientOption(this,
				"添加客户端");
		clientOption.setVisible(true);
		this.setEnabled(false);
	}

	private void jButtonDeleteMouseClicked(MouseEvent evt) {
		ListSelectionModel rowSM = jTableClients.getSelectionModel();
		int row = rowSM.getMinSelectionIndex();
		if (row >= 0) {
			try {
				clientTableModel.deleteRow(row);
			} catch (NoClientException e) {
				e.printStackTrace();
			}
		}
	}

	private void jButtonModifyMouseClicked(MouseEvent evt) {
		ListSelectionModel rowSM = jTableClients.getSelectionModel();
		int row = rowSM.getMinSelectionIndex();
		if (row >= 0) {
			JDialogClientOption clientOption = new JDialogClientOption(this,
					"修改客户端", row, clientTableModel.getStringAt(row, 1),
					clientTableModel.getStringAt(row, 2),
					clientTableModel.getStringAt(row, 3));
			clientOption.setVisible(true);
			this.setEnabled(false);
		}
	}

	private void tableHeaderMousePressed(MouseEvent evt) {
		int row = jTableClients.rowAtPoint(new Point(evt.getX(), evt.getY()));
		ListSelectionModel rowSM = jTableClients.getSelectionModel();
		rowSM.setSelectionInterval(row, row);
	}

	/*private void LoadXMLFile() {
		try {
			XMLFile = new Configuration();
			serialParameters = XMLFile.readSPConfig();
			DBconfig = XMLFile.readDBConfig();
			clientTableModel = XMLFile.readClientsTableModel();
		} catch (FileNotFoundException e) {
			XMLFile = new Configuration("Config.xml");
			serialParameters = new SerialParameters();
			SetDefaultParameters();
			clientTableModel = new ClientTableModel();
			DBconfig = new DBConfiguration();
		}

	}*/

	@SuppressWarnings({ "unchecked", "rawtypes" })
	void getPortNames() {
		CommPortIdentifier portId;
		Enumeration en = CommPortIdentifier.getPortIdentifiers();

		// iterate through the ports.
		while (en.hasMoreElements()) {
			portId = (CommPortIdentifier) en.nextElement();

			if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
				jComboBoxPortName.addItem(portId.getName());
			}
		}
	}

}
