package bros.manage.thread;

import java.util.HashMap;
import java.util.Map;

public class Test {

	public static void main(String[] args) {
		String taskId = "insertId";
		int poolSize = 10;
		int queueSize = 20;
		int limit = 1;
		ProcessThread.init(taskId, poolSize, queueSize, limit);
		
		for(int i=0;i<50;i++){
			new Thread(new Runnable() {
				@Override
				public void run() {
					
					for(int j = 0;j<=100;j++){
						try{
							Map<String,Object> data = new HashMap<String, Object>();
							data.put("message", j+"=========");
							IndertDataHandler aa = new IndertDataHandler();
							ProcessEntity entity = new ProcessEntity(data,aa);
							Queue.getInstance().getQueue(taskId, j%10).put(entity);
						}catch(Throwable e){
							e.printStackTrace();
						}
					}
				}
			}).start();
		}
		while(true){
//			try {
//				Thread.sleep(2000);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
		}
	}
	
	

}
