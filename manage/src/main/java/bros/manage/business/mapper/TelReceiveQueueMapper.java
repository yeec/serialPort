package bros.manage.business.mapper;

import java.util.Map;

/**
 * 
 * @author wyc
 * @记录电报接收队列表Mapper实体类
 */
public interface TelReceiveQueueMapper {
	public void insertTelReceiveInfo(Map<String, Object> contextMap);
}
