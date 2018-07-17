package bros.manage.main;

import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;

import bros.manage.business.view.LocalBoard;

// 程序主窗口界面初始化
public class MainWindow extends JFrame {

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
	
	// 文本区域对象
	public static JTextArea recieveBoard,sendBoard;
	
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
			Point p = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint();
			setBounds(p.x - WIDTH / 2, p.y - HEIGHT / 2, WIDTH, HEIGHT);
			this.getContentPane().setLayout(null);

			// 设置Title
			setTitle("串口通信");

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
					System.out.println("开始收发报");
				}
			});

			stopMenuItem = new JMenuItem();
			jMenu3.add(stopMenuItem);
			stopMenuItem.setText("停止收发报");
			stopMenuItem.setEnabled(false);
			stopMenuItem.setFont(new java.awt.Font("Dialog", 1, 18));
			stopMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					// 开始收发报
					System.out.println("停止收发报");
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
			jButtonStart.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent evt) {
					// 开始收发报
					System.out.println("开始收发报");
				}
			});

			jButtonStop = new JButton();
			this.getContentPane().add(jButtonStop);
			jButtonStop.setText("停止收发报");
			jButtonStop.setEnabled(false);
			jButtonStop.setBounds(700, 570, 130, 30);
			jButtonStop.setFont(new java.awt.Font("Dialog", 1, 18));
			jButtonStop.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent evt) {
					// 开始收发报
					System.out.println("停止收发报");
				}
			});

			jButtonConfig = new JButton();
			this.getContentPane().add(jButtonConfig);
			jButtonConfig.setText("参数设置");
			jButtonConfig.setBounds(400, 570, 130, 30);
			jButtonConfig.setFont(new java.awt.Font("Dialog", 1, 18));
			jButtonConfig.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent evt) {
					// 参数设置
					jButtonConfigMouseClicked(evt);
				}
			});
			
			
			
			databaseStatus = new JLabel();
			this.getContentPane().add(databaseStatus);
			databaseStatus.setBounds(149, 577, 15, 18);
			databaseStatus.setBackground(new java.awt.Color(255,0,0));
			databaseStatus.setBorder(BorderFactory.createTitledBorder(""));
			databaseStatus.setOpaque(true);
			
			
			dbLabel = new JLabel();
			this.getContentPane().add(dbLabel);
			dbLabel.setText("Database");
			dbLabel.setBounds(58, 571, 92, 30);
			dbLabel.setFont(new java.awt.Font("Dialog",1,18));
			
			
			jLabel1 = new JLabel();
			this.getContentPane().add(jLabel1);
			jLabel1.setText("Serial Port");
			jLabel1.setBounds(223, 572, 109, 30);
			jLabel1.setFont(new java.awt.Font("Dialog",1,18));
			
			
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
			jLabel3.setFont(new java.awt.Font("Dialog",1,18));
			
			
			jLabel4 = new JLabel();
			this.getContentPane().add(jLabel4);
			jLabel4.setText("发送电报");
			jLabel4.setBounds(473, 13, 84, 30);
			jLabel4.setFont(new java.awt.Font("Dialog",1,18));
			
			
			jLabel5 = new JLabel();
			this.getContentPane().add(jLabel5);
			jLabel5.setText("系统消息");
			jLabel5.setBounds(21, 400, 87, 30);
			jLabel5.setFont(new java.awt.Font("Dialog",1,18));
			
			
			recieveBoard = new JTextArea();
			jScrollPane2.setViewportView(recieveBoard);
			recieveBoard.setFont(new java.awt.Font("Dialog",1,18));
			recieveBoard.setEditable(false);
			
			
			mainBoard = new LocalBoard();
			jScrollPane1.setViewportView(mainBoard);
			mainBoard.setFont(new java.awt.Font("Dialog",1,18));
			
			
			sendBoard = new JTextArea();
			jScrollPane3.setViewportView(sendBoard);
			sendBoard.setFont(new java.awt.Font("Dialog",1,18));
			sendBoard.setEditable(false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 参数设置
	 * @param evt
	 */
	private void configMenuItemActionPerformed(ActionEvent evt) {	
		new JDialogOptions(this);	
	}
	
	/**
	 * 参数设置
	 * @param evt
	 */
	private void jButtonConfigMouseClicked(MouseEvent evt) {
		new JDialogOptions(this);	
	}

	/**
	 * 停止运行，退出程序
	 * 
	 * @param evt
	 */
	private void exitMenuItemActionPerformed(ActionEvent evt) {
		this.dispose();
//		stopTelegram();
		System.exit(0);
	}
}
