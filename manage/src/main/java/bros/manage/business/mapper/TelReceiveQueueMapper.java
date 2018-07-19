package bros.manage.business.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * 
 * @author wyc
 * @记录电报接收队列表Mapper实体类
 */
@Mapper
public interface TelReceiveQueueMapper {
	public void insertTelReceiveInfo(Map<String, Object> contextMap);
}
