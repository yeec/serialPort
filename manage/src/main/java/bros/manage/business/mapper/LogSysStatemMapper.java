package bros.manage.business.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

// 记录操作日志Mapper类
/**
 * 
 * @author liwei
 *
 */
@Mapper
public interface LogSysStatemMapper {
	
	// 记录操作日志
	public void insertLogSysStatemInfo(Map<String, Object> contextMap);
}
