package bros.manage.business.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
/**
 * 柜员
 * @author wyc
 *
 */
@Mapper
public interface TellerMapper {

	Map<String, Object> getTellerById(String id);
}
