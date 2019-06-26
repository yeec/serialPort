package bros.manage.util;
 
import java.sql.Connection;
import java.sql.DriverManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
 
public class JDBCUtil {
 
	private static final Log logger = LogFactory.getLog(JDBCUtil.class);
    
 
    public static boolean isValidateConnection(String ip,String port,String svrName,String user,String pass) throws Exception {
    	try{
//    		String URL="jdbc:oracle:thin:@"+ip+":"+port+":"+svrName;
    		String URL = "jdbc:oracle:thin:@(description=(address_list=(address=(protocol=tcp)(port="+port+")(host="+ip+")))(connect_data=(service_name="+svrName+")))";
    		//1.加载驱动程序
    		Class.forName("oracle.jdbc.OracleDriver");
    		//2.获得数据库链接
    		DriverManager.setLoginTimeout(3);
    		Connection conn=DriverManager.getConnection(URL, user, pass);
    		if (conn != null) {
    			conn.close();
    			return true;
    		}
    		return false;
    	}catch(Exception e){
    		logger.error("数据库连接失败",e);
    		return false;
    	}
        
    }
 
}