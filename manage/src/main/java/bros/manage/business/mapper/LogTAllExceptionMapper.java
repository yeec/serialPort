package bros.manage.business.mapper;

import java.util.Map;

/**
 * 
 * @author wyc
 * @记录收发电报失败日志表
 */
public interface LogTAllExceptionMapper {
	// 记录收发电报失败日志表
	public void insertLogTAllException(Map<String,Object> contextMap);
}
