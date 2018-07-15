package bros.manage.business.view;

import java.io.File;

import bros.manage.main.MainWindow;

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
public class TelegraphMain {

   public static final int DATABASE = 0;
   public static final int SOCKET = 1;

   /*
    * flag for database <--> serial port for socket <--> serial port
    */
   public static int route = DATABASE;

   /*
    * flag for restore
    */
   public static boolean needRestore = true;

   public static void main(String[] args) {
      File currentPath = new File("logs");
      System.setProperty("telegraph.log.base", currentPath.getAbsolutePath());

      // init values
      needRestore = true;

      try {
         new MainWindow("Telegragh Repeater");
         //TelegraphLogService.saveLaunchLog(true, null);
      } catch (Exception e) {
    	  //         TelegraphLogService.saveLaunchLog(false, e);
      }
   }

}
