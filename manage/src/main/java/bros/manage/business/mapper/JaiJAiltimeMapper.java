package bros.manage.business.mapper;

import java.util.Map;

/**
 * 
 * @author wyc
 * 监控时间表接口
 */
public interface JaiJAiltimeMapper {
	
	// 添加监控时间表
	public void insertJaiJailtimeinfo();
	// 更新监控时间表
	public int updateJaiJailtimeinfo(Map<String, Object> contextMap);
}
