package com.jsg.employee.service;

import java.util.List;

import com.jsg.base.model.BasePage;
import com.jsg.base.service.IBaseService;
import com.jsg.employee.model.Allowance;
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
	/**
	 * 
	* @Title: getCustomerById 
	* @Description: TODO(通过id查询客户信息) 
	* @param @param id
	* @param @return
	* @return Customer
	* @throws 
	* @author duanws
	* @date 2016-7-1 下午3:04:53
	 */
	Customer getCustomerById(String id);
	/**
	 * 
	* @Title: delCustomerById 
	* @Description: TODO(删除客户信息) 
	* @param @param id
	* @return void
	* @throws 
	* @author duanws
	* @date 2016-7-1 下午4:01:24
	 */
	void delCustomerById(String id);
	/**
	 * 
	* @Title: saveCustomer 
	* @Description: TODO(保存客户信息) 
	* @param @param customer
	* @return void
	* @throws 
	* @author duanws
	* @date 2016-7-1 下午4:04:00
	 */
	void saveCustomer(Customer customer);
	/**
	 * 
	* @Title: updateCustomer 
	* @Description: TODO(修改客户信息) 
	* @param @param customer
	* @return void
	* @throws 
	* @author duanws
	* @date 2016-7-1 下午4:04:55
	 */
	void updateCustomer(Customer customer);
	/**
	 * 
	* @Title: isExistCustomerName 
	* @Description: TODO(根据客户名称验证唯一) 
	* @param @param id
	* @param @param name
	* @param @param code
	* @param @return
	* @return boolean
	* @throws 
	* @author duanws
	* @date 2016-7-1 下午4:35:46
	 */
	boolean isExistCustomerName(String id, String name, String code);
	/**
	 * 
	* @Title: isExistCustomerCode 
	* @Description: TODO(根据客户代码验证唯一) 
	* @param @param id
	* @param @param name
	* @param @param code
	* @param @return
	* @return boolean
	* @throws 
	* @author duanws
	* @date 2016-7-1 下午4:36:03
	 */
	boolean isExistCustomerCode(String id, String name, String code);
	/**
	 * 
	* @Title: getCustomerList 
	* @Description: TODO(查询客户列表) 
	* @param @return
	* @return List<Customer>
	* @throws 
	* @author duanws
	* @date 2016-7-6 下午2:06:56
	 */
	List<Customer> getCustomerList();
	/**
	 * 
	* @Title: getAllowanceByCustomerId 
	* @Description: TODO(通过客户id查询补助信息) 
	* @param @param id
	* @param @return
	* @return Allowance
	* @throws 
	* @author duanws
	* @date 2016-7-11 下午3:45:01
	 */
	Allowance getAllowanceByCustomerId(String id);
	/**
	 * 
	* @Title: saveAllowance 
	* @Description: TODO(保存客户补助信息) 
	* @param @param allowance
	* @return void
	* @throws 
	* @author duanws
	* @date 2016-7-11 下午3:52:55
	 */
	void saveAllowance(Allowance allowance);
	/**
	 * 
	* @Title: updateAllowance 
	* @Description: TODO(保存客户补助信息) 
	* @param @param allowance
	* @return void
	* @throws 
	* @author duanws
	* @date 2016-7-11 下午3:53:10
	 */
	void updateAllowance(Allowance allowance);
}
