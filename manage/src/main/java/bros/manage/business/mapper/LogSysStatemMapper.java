package bros.manage.business.mapper;

import java.util.Map;

// 记录操作日志Mapper类
/**
 * 
 * @author liwei
 *
 */
public interface LogSysStatemMapper {
	
	// 记录操作日志
	public void insertLogSysStatemInfo(Map<String, Object> contextMap);
}
