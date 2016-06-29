package com.jsg.employee.service;

import com.jsg.base.model.BasePage;
import com.jsg.base.service.IBaseService;
import com.jsg.employee.model.Customer;
/**
 * 
* @ClassName: ICustomerService 
* @Description: TODO(客户信息处理) 
* @author duanws
* @date 2016-6-29 下午5:20:58 
*
 */
public interface ICustomerService extends IBaseService {
	/**
	 * 
	* @Title: queryCustomer 
	* @Description: TODO(分页查询客户信息) 
	* @param @param pageNo
	* @param @param pageSize
	* @param @param customer
	* @param @return
	* @return BasePage
	* @throws 
	* @author duanws
	* @date 2016-6-29 下午5:21:10
	 */
	BasePage queryCustomer(int pageNo,int pageSize,Customer customer);
}
