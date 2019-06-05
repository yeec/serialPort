package bros.manage.business.mapper;

import java.util.Map;

import org.springframework.dao.DataAccessException;

public interface TelSendQueueBakMapper {
	// 新增发送电报备份
	public int insertTelSendInfoBak(Map<String, Object> contextMap)throws DataAccessException;
	// 新增发送电报备份
	public int insertTelTAllSend(Map<String, Object> contextMap)throws DataAccessException;
}
