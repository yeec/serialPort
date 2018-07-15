package bros.manage.business.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bros.manage.business.mapper.TellerMapper;
import bros.manage.business.service.ITellerService;
import bros.manage.constants.ServiceErrorCodeContants;
import bros.manage.exception.ServiceException;
@Service("tellerService")
public class TellerServiceImpl implements ITellerService {

	@Autowired
	private TellerMapper tellerMapper;
	@Override
	public Map<String, Object> getTellerById(String id) throws ServiceException {
		try{
			return tellerMapper.getTellerById(id);
		}catch(Exception e){
			throw new ServiceException(ServiceErrorCodeContants.EBMT0000,"查询柜员失败");
		}
	}

}
