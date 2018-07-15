package bros.manage.business.service;

import java.util.Map;

import bros.manage.exception.ServiceException;

public interface ITellerService {

	public Map<String, Object> getTellerById(String id) throws ServiceException;
}
