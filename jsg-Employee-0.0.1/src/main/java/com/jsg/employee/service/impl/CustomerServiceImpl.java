package com.jsg.employee.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jsg.base.model.BasePage;
import com.jsg.employee.dao.ICustomerDao;
import com.jsg.employee.model.Allowance;
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
	/**
	 * 通过id查询客户信息
	 */
	@Override
	public Customer getCustomerById(String id) {
		
		return (Customer) this.customerDao.get(Customer.class, id);
	}
	/**
	 * 删除客户信息
	 */
	@Override
	public void delCustomerById(String id) {
		this.customerDao.delCustomer(id);
		
	}
	@Override
	public void saveCustomer(Customer customer) {
		this.customerDao.save(customer);
		
	}
	@Override
	public void updateCustomer(Customer customer) {
		this.customerDao.update(customer);
		
	}
	@Override
	public boolean isExistCustomerCode(String id, String name, String code) {
		String hql = " from Customer c where c.code='"+code+"'";
		List<Customer> list = this.customerDao.queryList(hql, new Object[0]);
		if((list != null) && (list.size()>0)){
			if(list.size()==1){
				if(!list.get(0).getId().equals(id)){
					return false;
				}
			}else if(list.size() > 1){
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean isExistCustomerName(String id, String name, String code) {
		String hql = " from Customer c where c.name='"+name+"'";
		List<Customer> list = this.customerDao.queryList(hql, new Object[0]);
		if((list != null) && (list.size()>0)){
			if(list.size()==1){
				if(!list.get(0).getId().equals(id)){
					return false;
				}
			}else if(list.size() > 1){
				return false;
			}
		}
		return true;
	}
	@Override
	public List<Customer> getCustomerList() {
		return this.customerDao.getCustomerList();
	}
	@Override
	public Allowance getAllowanceByCustomerId(String id) {
		
		return this.customerDao.getAllowanceByCustomerId(id);
	}
	@Override
	public void saveAllowance(Allowance allowance) {
		this.customerDao.save(allowance);
		
	}
	@Override
	public void updateAllowance(Allowance allowance) {
		this.customerDao.update(allowance);
		
	}
	
}
