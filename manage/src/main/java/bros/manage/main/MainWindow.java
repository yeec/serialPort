package bros.manage.main;

import gnu.io.SerialPort;

import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.SpringApplication;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidPooledConnection;

import bros.manage.NetUnionManageApplication;
import bros.manage.business.service.ILogSysStatemService;
import bros.manage.business.view.LocalBoard;
import bros.manage.dynamic.datasource.DataSourceContextHolder;
import bros.manage.dynamic.datasource.DynamicDataSource;
import bros.manage.entity.SerialParameters;
import bros.manage.exception.ServiceException;
import bros.manage.telegraph.SerialListener;
import bros.manage.telegraph.SerialListener_bak;
import bros.manage.telegraph.SerialSendThread;
import bros.manage.telegraph.exception.NoSuchPort;
import bros.manage.telegraph.exception.NotASerialPort;
import bros.manage.telegraph.exception.PortInUse;
import bros.manage.telegraph.exception.SerialPortParameterFailure;
import bros.manage.telegraph.exception.TooManyListeners;
import bros.manage.util.DataBaseUtil;
import bros.manage.util.DeviceInfo;
import bros.manage.util.SpringUtil;

// 程序主窗口界面初始化
//@SpringBootApplication
public class MainWindow extends JFrame {
	
	private static final Log logger = LogFactory.getLog(MainWindow.class);

	// 创建一个菜单栏
	private JMenuBar jMenuBar1;
	// 创建一个菜单名称(系统)
	private JMenu jMenu3;
	// 创建一个菜单名称(设置)
	private JMenu jMenu4;
	// 创建一个菜单项(开始发报)
	private JMenuItem startMenuItem;
	// 创建一个菜单项（停止发报）
	private JMenuItem stopMenuItem;
	// 创建一个菜单项（退出程序）
	private JMenuItem exitMenuItem;
	// 创建一个菜单项（参数设置）
	private JMenuItem configMenuItem;
	// 创建一个菜单项（清除左屏）
	private JMenuItem clearLeftScreen;
	// 创建一个菜单项（清除左屏）
	private JMenuItem clearRightScreen;
	// JSeparator类是一种特殊的组件，他在JMenu上提供分隔符
	private JSeparator jSeparator2;
	// JScrollPane是带有滚动条的面板
	private JScrollPane jScrollPane1;
	private JScrollPane jScrollPane3;
	private JScrollPane jScrollPane2;
	// 按钮（开始收发报）
	private JButton jButtonStop;
	// 按钮（停止收发报）
	private JButton jButtonStart;
	// 按钮（参数设置）
	private JButton jButtonConfig;
	// JLabel，标签。标签主要用于展示 文本 或 图片，也可以 同时显示文本和图片。
	public static JLabel databaseStatus;
	public static JLabel serialPortStatus;
	private JLabel dbLabel;
	private JLabel jLabel1;
	private JLabel jLabel4;
	private JLabel jLabel5;
	private JLabel jLabel3;
	// 串口对象
	private SerialPort sp;
	// 串口参数对象
	private SerialParameters serialParameters;
	//发送电报线程
	private SerialSendThread thread;
	//串口监听
	private SerialListener sl;

	// 文本区域对象
	public static JTextArea recieveBoard, sendBoard;

	public static LocalBoard mainBoard;
	
	
	
	public MainWindow() {
		super();
		initGUI();
		initComponents();
		this.setVisible(true);
	}

	public MainWindow(String title) {
		super(title);
		initGUI();
		initComponents();
		this.setVisible(true);
		this.setSize(888, 700);
	}

	private void initGUI() {
		try {
			// 关闭程序
			setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
			// 禁止窗口最大化
			setResizable(false);

			// 设置程序窗口居中显示
			int DIALOG_WHITE = 900;//宽度
			int DIALOG_HEIGHT = 700;//高度
			Point point = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint();
			this.setBounds(point.x - DIALOG_WHITE / 2, point.y - DIALOG_HEIGHT / 2, DIALOG_WHITE, DIALOG_HEIGHT);

//			Point p = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint();
//			setBounds(p.x - WIDTH / 2, p.y - HEIGHT / 2, WIDTH, HEIGHT);
			this.getContentPane().setLayout(null);

			// 设置Title
			setTitle("串口通信");
			
			
	        Image icon = Toolkit.getDefaultToolkit().getImage("src/main/resources/img/logo.png"); 
	        System.out.println(Toolkit.getDefaultToolkit());
	        this.setIconImage(icon);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 初始化控件
	 */
	private void initComponents() {
		try {

			// 菜单对象
			jMenuBar1 = new JMenuBar();
			setJMenuBar(jMenuBar1);

			// 菜单名称
			jMenu3 = new JMenu();
			jMenuBar1.add(jMenu3);
			jMenu3.setText("系统");
			jMenu3.setFont(new java.awt.Font("Dialog", 1, 18));

			// 菜单项
			startMenuItem = new JMenuItem();
			jMenu3.add(startMenuItem);
			startMenuItem.setText("开始收发报");
			startMenuItem.setFont(new java.awt.Font("Dialog", 1, 18));
			startMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					// 开始收发报
					startActionPerformed(evt);
				}
			});

			stopMenuItem = new JMenuItem();
			jMenu3.add(stopMenuItem);
			stopMenuItem.setText("停止收发报");
			stopMenuItem.setEnabled(false);
			stopMenuItem.setFont(new java.awt.Font("Dialog", 1, 18));
			stopMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					// 停止收发报
					stopActionPerformed(evt);
				}
			});

			// JSeparator类是一种特殊的组件，他在JMenu上提供分隔符
			jSeparator2 = new JSeparator();
			jMenu3.add(jSeparator2);

			exitMenuItem = new JMenuItem();
			jMenu3.add(exitMenuItem);
			exitMenuItem.setText("退出程序");
			exitMenuItem.setFont(new java.awt.Font("Dialog", 1, 18));
			exitMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					exitMenuItemActionPerformed(evt);
				}
			});

			jMenu4 = new JMenu();
			jMenuBar1.add(jMenu4);
			jMenu4.setText("设置");
			jMenu4.setFont(new java.awt.Font("Dialog", 1, 18));

			configMenuItem = new JMenuItem();
			jMenu4.add(configMenuItem);
			configMenuItem.setText("参数设置");
			configMenuItem.setFont(new java.awt.Font("Dialog", 1, 18));
			configMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					// 参数设置
					configMenuItemActionPerformed(evt);
				}
			});
			
			
			// 创建一个菜单项（清除左屏）
			
			clearLeftScreen = new JMenuItem();
			jMenu4.add(clearLeftScreen);
			clearLeftScreen.setText("清空左屏");
			clearLeftScreen.setFont(new java.awt.Font("Dialog", 1, 18));
			clearLeftScreen.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					clearLeftScreenActionPerformed(evt);
				}
			});
			
			// 创建一个菜单项（清除左屏）
			clearRightScreen = new JMenuItem();
			jMenu4.add(clearRightScreen);
			clearRightScreen.setText("清空右屏");
			clearRightScreen.setFont(new java.awt.Font("Dialog", 1, 18));
			clearRightScreen.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					clearRightScreenActionPerformed(evt);
				}
			});

			// 控制台打印框
			jScrollPane1 = new JScrollPane();
			this.getContentPane().add(jScrollPane1);
			jScrollPane1.setBounds(20, 430, 848, 120);
			jScrollPane1.setAutoscrolls(true);

			jButtonStart = new JButton();
			this.getContentPane().add(jButtonStart);
			jButtonStart.setText("开始收发报");
			jButtonStart.setBounds(550, 570, 130, 30);
			jButtonStart.setFont(new java.awt.Font("Dialog", 1, 18));
			jButtonStart.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					// 开始收发报
					startActionPerformed(evt);
				}
			});
			
			jButtonStop = new JButton();
			this.getContentPane().add(jButtonStop);
			jButtonStop.setText("停止收发报");
			jButtonStop.setEnabled(false);
			jButtonStop.setBounds(700, 570, 130, 30);
			jButtonStop.setFont(new java.awt.Font("Dialog", 1, 18));
			jButtonStop.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					// 停止收发报
					stopActionPerformed(evt);
				}
			});

			jButtonConfig = new JButton();
			this.getContentPane().add(jButtonConfig);
			jButtonConfig.setText("参数设置");
			jButtonConfig.setBounds(400, 570, 130, 30);
			jButtonConfig.setFont(new java.awt.Font("Dialog", 1, 18));
			jButtonConfig.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					// 参数设置
					configMenuItemActionPerformed(evt);
				}
			});

			databaseStatus = new JLabel();
			this.getContentPane().add(databaseStatus);
			databaseStatus.setBounds(149, 577, 15, 18);
			databaseStatus.setBackground(new java.awt.Color(255, 0, 0));
			databaseStatus.setBorder(BorderFactory.createTitledBorder(""));
			databaseStatus.setOpaque(true);

			dbLabel = new JLabel();
			this.getContentPane().add(dbLabel);
			dbLabel.setText("Database");
			dbLabel.setBounds(58, 571, 92, 30);
			dbLabel.setFont(new java.awt.Font("Dialog", 1, 18));

			jLabel1 = new JLabel();
			this.getContentPane().add(jLabel1);
			jLabel1.setText("Serial Port");
			jLabel1.setBounds(223, 572, 109, 30);
			jLabel1.setFont(new java.awt.Font("Dialog", 1, 18));

			serialPortStatus = new JLabel();
			this.getContentPane().add(serialPortStatus);
			serialPortStatus.setBackground(new java.awt.Color(255, 0, 0));
			serialPortStatus.setOpaque(true);
			serialPortStatus.setBorder(BorderFactory.createTitledBorder(""));
			serialPortStatus.setBounds(326, 578, 15, 18);

			// 接受电报显示框
			jScrollPane2 = new JScrollPane();
			this.getContentPane().add(jScrollPane2);
			jScrollPane2.setBounds(20, 50, 410, 340);

			// 发送报文显示框
			jScrollPane3 = new JScrollPane();
			this.getContentPane().add(jScrollPane3);
			jScrollPane3.setBounds(458, 50, 410, 340);

			jLabel3 = new JLabel();
			this.getContentPane().add(jLabel3);
			jLabel3.setText("接收电报");
			jLabel3.setBounds(29, 11, 84, 30);
			jLabel3.setFont(new java.awt.Font("Dialog", 1, 18));

			jLabel4 = new JLabel();
			this.getContentPane().add(jLabel4);
			jLabel4.setText("发送电报");
			jLabel4.setBounds(473, 13, 84, 30);
			jLabel4.setFont(new java.awt.Font("Dialog", 1, 18));

			jLabel5 = new JLabel();
			this.getContentPane().add(jLabel5);
			jLabel5.setText("系统消息");
			jLabel5.setBounds(21, 400, 87, 30);
			jLabel5.setFont(new java.awt.Font("Dialog", 1, 18));

			recieveBoard = new JTextArea();
			jScrollPane2.setViewportView(recieveBoard);
			recieveBoard.setFont(new java.awt.Font("Dialog", 1, 18));
			recieveBoard.setEditable(false);

			mainBoard = new LocalBoard();
			jScrollPane1.setViewportView(mainBoard);
			mainBoard.setFont(new java.awt.Font("Dialog", 1, 18));

			sendBoard = new JTextArea();
			jScrollPane3.setViewportView(sendBoard);
			sendBoard.setFont(new java.awt.Font("Dialog", 1, 18));
			sendBoard.setEditable(false);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 清空左屏
	 * @param evt
	 */
	private void clearLeftScreenActionPerformed(ActionEvent evt){
		MainWindow.recieveBoard.setText(new String(""));
	}
	/**
	 * 清空右屏
	 * @param evt
	 */
	private void clearRightScreenActionPerformed(ActionEvent evt){
		MainWindow.sendBoard.setText(new String(""));
	}

	/**
	 * 参数设置
	 * 
	 * @param evt
	 */
	private void configMenuItemActionPerformed(ActionEvent evt) {
		serialParameters = new JDialogOptions(this).getSerialParameters();

	}

	/**
	 * 参数设置
	 * 
	 * @param evt
	 */
	private void jButtonConfigMouseClicked(MouseEvent evt) {
		serialParameters = new JDialogOptions(this).getSerialParameters();
	}

	/**
	 * 停止运行，退出程序
	 * 
	 * @param evt
	 */
	private void exitMenuItemActionPerformed(ActionEvent evt) {
		this.dispose();
		try{
			stopTelegram();
		}catch(Exception e){
			logger.error("系统退出异常",e);
		}finally{
			System.exit(0);
		}
	}

	/**
	 * 开始接收数据
	 * 
	 * @param evt
	 */	private void jButtonStartMouseClicked(MouseEvent evt) {

		startTelegram();
//		jButtonStart.setEnabled(false);
//		jButtonStop.setEnabled(true);
//		jButtonConfig.setEnabled(false);
//		configMenuItem.setEnabled(false);
//		startMenuItem.setEnabled(false);
//		stopMenuItem.setEnabled(true);
	}

	private void startActionPerformed(ActionEvent evt) {

		startTelegram();
//		jButtonStart.setEnabled(false);
//		jButtonStop.setEnabled(true);
//		jButtonConfig.setEnabled(false);
//		configMenuItem.setEnabled(false);
//		startMenuItem.setEnabled(false);
//		stopMenuItem.setEnabled(true);
	}
	
	/**
	 * 停止数据接收和发送
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
		if(sl!=null){
			//停止报文处理线程
			sl.stopHandle();
			//清空报文缓存
			sl.clearBuffer();
		}
		// 组装正常记录日志入参（电报收发系统接收发送状态改变日志）
		Map<String, Object> stopTelegramMap = new HashMap<String, Object>();
		// 系统操作类型
		stopTelegramMap.put("sysDealType", "停止");
		// 操作机器IP
		stopTelegramMap.put("delaIp", DeviceInfo.getDeviceIp());
		// 操作机器MAC
		stopTelegramMap.put("delaMac", DeviceInfo.getDeviceMAC());

		MainWindow.mainBoard.addMsg("系统停止中 ...", LocalBoard.INFO_SYSTEM);
		ILogSysStatemService logSysStatemService = (ILogSysStatemService) SpringUtil.getBean("logSysStatemService");
		try {
			//停止发送电报线程
			stopSendThread();
			// 删除监听器
			SerialPortManager.removeListener(sp);
			// 关闭监听端口
			sp = SerialPortManager.closePort(sp);
			
			MainWindow.mainBoard.addMsg("系统已停止.", LocalBoard.INFO_SYSTEM);
			
			// 日志描述
			stopTelegramMap.put("logMemo", "无");
			// 系统状态
			stopTelegramMap.put("sysState", "成功");
			// 日志等级
			stopTelegramMap.put("logGrade", "1");
			// 电报收发系统接收发送状态改变日志
			logSysStatemService.addLogSysStatemInfo(stopTelegramMap);
		} catch (Exception e) {
			
			// 日志描述
			String logMemo = "电报收发系统停止失败, 错误信息:" + (e.getMessage());
			stopTelegramMap.put("logMemo", logMemo);
			// 系统状态
			stopTelegramMap.put("sysState", "失败");
			// 日志等级
			stopTelegramMap.put("logGrade", "1");
			// 电报收发系统接收发送状态改变日志
			try {
				logSysStatemService.addLogSysStatemInfo(stopTelegramMap);
			} catch (ServiceException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

	private void startTelegram(){
		try {
			
			if(!DataBaseUtil.checkDBState("default")){
				JOptionPane.showMessageDialog(this, "数据库参数配置错误,请检查配置!");
				return;
			}
			
			// 开始发报清屏
			MainWindow.recieveBoard.setText(new String(""));
			
			// 组装正常记录日志入参（电报收发系统接收发送状态改变日志）
			Map<String, Object> startTelegramMap = new HashMap<String, Object>();
			// 系统操作类型
			startTelegramMap.put("sysDealType", "运行");
			// 操作机器IP
			startTelegramMap.put("delaIp", DeviceInfo.getDeviceIp());
			// 操作机器MAC
			startTelegramMap.put("delaMac", DeviceInfo.getDeviceMAC());
			ILogSysStatemService logSysStatemService = (ILogSysStatemService) SpringUtil.getBean("logSysStatemService");
			if (!initSP()) {
				// 日志描述
				String logMemo = "电报收发系统接收初始化失败, 错误信息:" + (new IllegalStateException("初始化失败"));
				startTelegramMap.put("logMemo", logMemo);
				// 系统状态
				startTelegramMap.put("sysState", "失败");
				// 日志等级
				startTelegramMap.put("logGrade", "1");
				// 电报收发系统接收发送状态改变日志
				
				logSysStatemService.addLogSysStatemInfo(startTelegramMap);
			} else {
				jButtonStart.setEnabled(false);
				jButtonStop.setEnabled(true);
				jButtonConfig.setEnabled(false);
				configMenuItem.setEnabled(false);
				startMenuItem.setEnabled(false);
				stopMenuItem.setEnabled(true);
				MainWindow.mainBoard.addMsg("系统已就绪.", LocalBoard.INFO_SYSTEM);
				
				// 日志描述
				startTelegramMap.put("logMemo", "无");
				// 系统状态
				startTelegramMap.put("sysState", "成功");
				// 日志等级
				startTelegramMap.put("logGrade", "1");
				// 电报收发系统接收发送状态改变日志
				logSysStatemService.addLogSysStatemInfo(startTelegramMap);
			}
		} catch (ServiceException e) {
			JOptionPane.showMessageDialog(this, e.getErrorMsg());
			logger.error("收发报初始化异常", e);
		} catch (HeadlessException e) {
			logger.error("收发报初始化异常", e);
		} catch (Exception e) {
			logger.error("收发报初始化异常", e);
		}
	}
	
	private void startSendThread(){
		thread = new SerialSendThread(sp);
		thread.start();
		MainWindow.mainBoard.addMsg("发送电报线程启动.", LocalBoard.INFO_SYSTEM);
	}
	
	private void stopSendThread(){
		if(null!=thread){
			thread.stopSafely();
			thread.stop();
			MainWindow.mainBoard.addMsg("发送电报线程停止.", LocalBoard.INFO_SYSTEM);
		}
	}

	private boolean initSP() {
		try {
			if(null == serialParameters){
				JOptionPane.showMessageDialog(this, "配置未找到!\n请先编辑配置!");
				serialParameters = new JDialogOptions(this).getSerialParameters();
				MainWindow.mainBoard.addMsg("请先设置收发报参数。", LocalBoard.INFO_SYSTEM);
				return false;
			}
			if(null==sp){
				sp = SerialPortManager.openPort(serialParameters);
			}
			InputStream inputStream = sp.getInputStream();
			sl = new SerialListener(sp);
			SerialPortManager.addListener(sp, sl);
			sl.start();
			//启动发送电报线程
			startSendThread();
		} catch (SerialPortParameterFailure e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotASerialPort e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPort e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PortInUse e) {
			MainWindow.mainBoard.addMsg(e.toString(), LocalBoard.INFO_SYSTEM);
			e.printStackTrace();
			return false;
		} catch (TooManyListeners e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return true;

	}
	
	
	public static void main(String[] args) throws ServiceException {
		
		// 加载sping容器
		try{
			SpringApplication.run(NetUnionManageApplication.class, args);
			new MainWindow();
			
			if(!DataBaseUtil.checkDBState("default")){
				MainWindow.mainBoard.addMsg("数据库参数配置错误,请检查配置!", LocalBoard.INFO_SYSTEM);
				return;
			}
		}catch(Exception e){
			throw new ServiceException();
		}
		
		
		DataSourceContextHolder.setDBType("default");
		
		
		
		// 组装正常记录日志入参（数据库参数设置）
		Map<String, Object> saveLaunchLogMap = new HashMap<String, Object>();
		// 系统操作类型
		saveLaunchLogMap.put("sysDealType", "启动");
		// 操作机器IP
		saveLaunchLogMap.put("delaIp", DeviceInfo.getDeviceIp());
		// 操作机器MAC
		saveLaunchLogMap.put("delaMac", DeviceInfo.getDeviceMAC());
		ILogSysStatemService logSysStatemService = (ILogSysStatemService) SpringUtil.getBean("logSysStatemService");
		try {
			
			// 日志描述
			saveLaunchLogMap.put("logMemo", "无");
			// 系统状态
			saveLaunchLogMap.put("sysState", "成功");
			// 日志等级
			saveLaunchLogMap.put("logGrade", "1");
			// 数据库参数设置界面, 记录系统状态日志
			logSysStatemService.addLogSysStatemInfo(saveLaunchLogMap);
		} catch (Exception e) {
			// 日志描述
			String logMemo = "数据库参数组装失败, 错误信息:" + (e.getMessage());
			saveLaunchLogMap.put("logMemo", logMemo);
			// 系统状态
			saveLaunchLogMap.put("sysState", "失败");
			// 日志等级
			saveLaunchLogMap.put("logGrade", "1");
			// 数据库参数设置界面, 记录系统状态日志
			
			logSysStatemService.addLogSysStatemInfo(saveLaunchLogMap);
			e.printStackTrace();
		}
		try {
			System.in.read();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
