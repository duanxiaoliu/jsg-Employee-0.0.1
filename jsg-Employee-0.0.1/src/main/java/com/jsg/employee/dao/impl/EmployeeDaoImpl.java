package com.jsg.employee.dao.impl;

import org.springframework.stereotype.Repository;

import com.jsg.base.dao.impl.BaseDaoImpl;
import com.jsg.base.model.BasePage;
import com.jsg.base.util.DataUtil;
import com.jsg.employee.dao.IEmployeeDao;
import com.jsg.employee.model.Employee;
/**
 * 
* @ClassName: EmployeeDaoImpl 
* @Description: TODO(员工管理) 
* @author duanws
* @date 2016-7-4 下午4:42:35 
*
 */
@Repository("employeeDao")
public class EmployeeDaoImpl extends BaseDaoImpl implements IEmployeeDao {

	@Override
	public BasePage queryEmployee(int pageNo, int pageSize, Employee employee) {
		StringBuffer hql = new StringBuffer(" from Employee e where 1=1");
		//姓名
		if(DataUtil.strIsNotNull(employee.getEmployeeName())){
			hql.append(" and e.employeeName like '%"+employee.getEmployeeName()+"%'");
		}
		//编码
		if(DataUtil.strIsNotNull(employee.getEmployeeCode())){
			hql.append(" and e.employeeName like '%"+employee.getEmployeeCode()+"%'");
		}
		//所在部门
		if(DataUtil.objIsNotNull(employee.getDepartment()) && DataUtil.strIsNotNull(employee.getDepartment().getId())){
			hql.append(" and e.department.id ='"+employee.getDepartment().getId()+"'");
		}
		//所在客户
		if(DataUtil.objIsNotNull(employee.getCustomer()) && DataUtil.strIsNotNull(employee.getCustomer().getId())){
			hql.append(" and e.customer.id ='"+employee.getCustomer().getId()+"'");
		}
		return this.queryPage(hql.toString(), pageNo, pageSize, new Object[0]);
	}

	@Override
	public void delEmployeeById(String id) {
		String hql = " delete from Employee e where e.id='"+id+"'";
		this.executeHql(hql, new Object[0]);
	}

}
