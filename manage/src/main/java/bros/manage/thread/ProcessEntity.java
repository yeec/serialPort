package bros.manage.thread;

import java.util.Map;

public class ProcessEntity {
	/**
	 * 插入数据
	 */
	private Map<String,Object> data;
	
	private DealHandler detalHandler;
	
	public ProcessEntity(Map<String,Object> data,DealHandler detalHandler){
		this.data = data;
		this.detalHandler = detalHandler;
	}

	public Map<String, Object> getData() {
		return data;
	}

	public void setData(Map<String, Object> data) {
		this.data = data;
	}

	public DealHandler getDetalHandler() {
		return detalHandler;
	}

	public void setDetalHandler(DealHandler detalHandler) {
		this.detalHandler = detalHandler;
	}
	
	
}
