package bros.manage.thread;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProcessThread {
	private Map<String,ExecutorService> executorServiceMap = new HashMap<String, ExecutorService>();
	
	//类初始化时，不初始化这个对象(延时加载，真正用的时候再创建)
    private static ProcessThread instance;
     
    //构造器私有化
    private ProcessThread(String taskId,int poolSize,int queueSize,int limit){
    	ExecutorService executorService = Executors.newFixedThreadPool(poolSize);
    	Queue requestQueue = Queue.getInstance();
    	for(int i=0;i<poolSize;i++){
    		ArrayBlockingQueue<ProcessEntity> queue = new ArrayBlockingQueue<ProcessEntity>(queueSize);
    		requestQueue.addQueue(taskId, queue);
    		executorService.submit(new ProcessHandler(taskId,i,limit));
    	}
    	executorServiceMap.put(taskId, executorService);
    };
    
    //方法同步，调用效率低
    public static  ProcessThread getInstance(String taskId,int poolSize,int queueSize,int limit){
        if(instance==null){
        	synchronized (ProcessThread.class) {
				if(instance == null){
					instance=new ProcessThread(taskId,poolSize,queueSize,limit);
				}
			}
        }
        return instance;
    }
    
    public static void init(String taskId,int poolSize,int queueSize,int limit){
    	getInstance(taskId, poolSize, queueSize, limit);
    }
}
