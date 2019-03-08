package bros.manage.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import bros.manage.business.service.ITelReceiveQueueService;
import bros.manage.business.service.ITelSendQueueService;

import com.csvreader.CsvReader;

public class CvsDataUtil {
	
	public static void addData(){
		try {    
            BufferedReader reader = new BufferedReader(new FileReader("D:/a.csv"));//换成你的文件名   
            reader.readLine();//第一行信息，为标题信息，不用,如果需要，注释掉   
            String line = null;    
            while((line=reader.readLine())!=null){    
                String item[] = line.split(",");//CSV格式文件为逗号分隔符文件，这里根据逗号切分   
                Map<String,Object> contextMap = new HashMap<String,Object>();
        		// 主键
                contextMap.put("telId", item[0]);
        		// 电报报文
        		contextMap.put("teletext", item[2]);
        		// 报文类别
        		contextMap.put("teleFlag", item[3]);
        		// 处理标志
        		contextMap.put("vFlag", item[4]);
        		// 接收机器IP
        		contextMap.put("recIp", item[6]);
        		// 接收机器MA
        		contextMap.put("recMac", item[7]);
        		// 用户ID
        		contextMap.put("userid", "SYSTEM_TEL");
        		ITelReceiveQueueService itelReceiveQueueService = (ITelReceiveQueueService) SpringUtil.getBean("telReceiveQueueService");
        		itelReceiveQueueService.addTelReceiveQueueInfo(contextMap);
            }    
        } catch (Exception e) {    
            e.printStackTrace();    
        }    
	}
	
	public static void read(){

        try {
            // 创建CSV读对象
            CsvReader csvReader = new CsvReader("D:/a.csv");

            // 读表头
            csvReader.readHeaders();
            while (csvReader.readRecord()){
                // 读一整行
//                System.out.println(csvReader.getRawRecord());
                // 读这行的某一列
//                System.out.println(csvReader.get("Link"));
                String line = csvReader.getRawRecord().replaceAll("\"", "");
                String item[] = line.split(",");//CSV格式文件为逗号分隔符文件，这里根据逗号切分   
                Map<String,Object> contextMap = new HashMap<String,Object>();
        		// 主键
                contextMap.put("telId", item[0]);
        		// 电报报文
        		contextMap.put("teletext", new String(item[2].getBytes(),"GBK"));
        		
        		ITelSendQueueService itelSendQueueService = (ITelSendQueueService) SpringUtil.getBean("telSendQueueService");
        		itelSendQueueService.addTelSendInfo(contextMap);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	public static void main(String[] args) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader("D:/c.txt"));//换成你的文件名   
        reader.readLine();//第一行信息，为标题信息，不用,如果需要，注释掉   
        String line = null;    
        while((line=reader.readLine())!=null){ 
//        	if(line.indexOf("") != -1){
        		System.out.println(line);
//        	}
        }
	}


}
