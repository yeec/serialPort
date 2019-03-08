package bros.manage.thread;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;

public class Queue {
	
	private Map<String,List<ArrayBlockingQueue<ProcessEntity>>> queueMap = new HashMap<String, List<ArrayBlockingQueue<ProcessEntity>>>();
	//类初始化时，不初始化这个对象(延时加载，真正用的时候再创建)
    private static Queue instance;
    
    //构造器私有化
    private Queue(){};
    
    //方法同步，调用效率低
    public static Queue getInstance(){
    	if(instance==null){
        	synchronized (ProcessThread.class) {
				if(instance == null){
					instance=new Queue();
				}
			}
        }
    	return instance;
    }
    
    
    public ArrayBlockingQueue<ProcessEntity> getQueue(String taskId,int index){
    	return queueMap.get(taskId).get(index);
    }
    
    public void addQueue(String taskId,ArrayBlockingQueue<ProcessEntity> queue){
    	if(queueMap.get(taskId) == null){
    		List<ArrayBlockingQueue<ProcessEntity>> list = new ArrayList<ArrayBlockingQueue<ProcessEntity>>();
    		list.add(queue);
    		queueMap.put(taskId, list);
    	}else{
    		queueMap.get(taskId).add(queue);
    	}
    }
}
