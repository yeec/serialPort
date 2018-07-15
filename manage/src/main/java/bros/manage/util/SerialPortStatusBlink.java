package bros.manage.util;

import bros.manage.main.MainWindow;

public class SerialPortStatusBlink extends Thread {

	public boolean change;

	SerialPortStatusBlink(){
		change=false;
	}
	
	public void run(){
		while(true){	
			if (change){
				turn();
				try {
					sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				turn();
				try {
					sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			change=false;
			}
			else{
				try {
					sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void turn(){
		if (MainWindow.serialPortStatus.getBackground().getRed()==0)
			MainWindow.serialPortStatus.setBackground(new java.awt.Color(255,0,0));
		else
			MainWindow.serialPortStatus.setBackground(new java.awt.Color(0,255,0));
		MainWindow.serialPortStatus.repaint();
	}
}
