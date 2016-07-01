package com.jsg.employee.dao.impl;

import org.springframework.stereotype.Repository;

import com.jsg.base.dao.impl.BaseDaoImpl;
import com.jsg.base.model.BasePage;
import com.jsg.base.util.DataUtil;
import com.jsg.employee.dao.ICustomerDao;
import com.jsg.employee.model.Customer;
/**
 * 
* @ClassName: CustomerDaoImpl 
* @Description: TODO(处理客户信息) 
* @author duanws
* @date 2016-6-29 下午5:09:57 
*
 */
@Repository("customerDao")
public class CustomerDaoImpl extends BaseDaoImpl implements ICustomerDao {

	@Override
	public BasePage queryCustomer(int pageNo, int pageSize, Customer customer) {
		StringBuffer hql = new StringBuffer(" from Customer c where 1=1");
		//客户姓名
		if(DataUtil.strIsNotNull(customer.getName())){
			hql.append(" and c.name like '%"+customer.getName()+"%'");
		}
		//客户代码
		if(DataUtil.strIsNotNull(customer.getCode())){
			hql.append(" and c.code='"+customer.getCode()+"'");
		}
		return this.queryPage(hql.toString(), pageNo, pageSize, new Object[0]);
	}

	@Override
	public void delCustomer(String id) {
		String hql = " delete from Customer c where c.id='"+id+"'";
		this.executeHql(hql, new Object[0]);
	}



}
