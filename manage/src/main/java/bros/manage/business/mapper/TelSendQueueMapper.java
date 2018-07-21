package bros.manage.business.mapper;

import java.util.List;
import java.util.Map;

/**
 * 
 * @author liwei
 * 电报发送列表Mapper
 */
public interface TelSendQueueMapper {
	
	// 查询发送电报列表中发送标志为0的数据
	public List<Map<String, Object>> getTelSendInfo();
	// 更新发送电报列表中发送标志为0，更新为1
	public void updateTelSendInfo(Map <String,Object> contextMap);
	
}
