package com.jsg.employee.dao;

import com.jsg.base.dao.IBaseDao;
import com.jsg.base.model.BasePage;
import com.jsg.employee.model.Customer;
/**
 * 
* @ClassName: ICustomerDao 
* @Description: TODO(处理客户信息接口) 
* @author duanws
* @date 2016-6-29 下午5:16:49 
*
 */
public interface ICustomerDao extends IBaseDao {
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
	* @date 2016-6-29 下午5:16:34
	 */
	BasePage queryCustomer(int pageNo,int pageSize,Customer customer);

}
