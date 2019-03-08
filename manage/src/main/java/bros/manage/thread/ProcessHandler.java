package bros.manage.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;

public class ProcessHandler implements Runnable{
	private String taskId;
	private int index;
	
	private int insertSize;
	
	public ProcessHandler(String taskId,int index,int insertSize) {
		this.taskId = taskId;
		this.index = index;
		this.insertSize = insertSize;
	}
	
	@Override
	public void run() {
		ArrayBlockingQueue<ProcessEntity> queue = Queue.getInstance().getQueue(taskId,index);
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		int count = 0;
		while(true){
			try{
				ProcessEntity entity = queue.take();
				list.add(entity.getData());
				count++;
				if(queue.size()>0){
					if(count >= insertSize){
						//执行批量插入数据操作
						entity.getDetalHandler().execute(list);
						count = 0;
						list.clear();
					}
				}else{
					entity.getDetalHandler().execute(list);
					count = 0;
					list.clear();
				}
			}catch(Throwable e){
				
			}
		}
		
	}

}
