package com.jsg.employee.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jsg.base.model.BasePage;
import com.jsg.employee.dao.ICustomerDao;
import com.jsg.employee.model.Customer;
import com.jsg.employee.service.ICustomerService;
/**
 * 
* @ClassName: CustomerServiceImpl 
* @Description: TODO(客户信息处理) 
* @author duanws
* @date 2016-6-29 下午5:10:26 
*
 */
@Service("customerService")
public class CustomerServiceImpl implements ICustomerService {

	@Autowired
	private ICustomerDao customerDao;
	/**
	 * 分页查询客户信息
	 */
	@Override
	public BasePage queryCustomer(int pageNo, int pageSize, Customer customer) {
		
		return this.customerDao.queryCustomer(pageNo, pageSize, customer);
	}
	
}
