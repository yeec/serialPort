package bros.manage.business.mapper;

import java.util.Map;

/**
 * 
 * @author wyc
 * @记录当前系统的状态表Mapper实体类
 */
public interface TelSysStatusMapper {
	// 记录当前系统的状态表
	public void insertTelSysStatus(Map<String,Object> contextMap);
	// 更新当前系统的状态表
	public int updateTelSysStatus(Map<String, Object> contextMap);
	// 查询当前系统的状态表
    public int getTelSysStatus(Map<String, Object> contextMap);
}
