package bros.manage.business.mapper;

import bros.manage.entity.LogSysStatemBen;

// 记录操作日志Mapper类
/**
 * 
 * @author liwei
 *
 */
public interface LogSysStatemMapper {
	
	// 记录操作日志
	public void insertLogSysStatemInfo(LogSysStatemBen logSysStatemBen);
}
