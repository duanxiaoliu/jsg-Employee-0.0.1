package com.jsg.employee.dao.impl;

import org.springframework.stereotype.Repository;

import com.jsg.base.dao.impl.BaseDaoImpl;
import com.jsg.base.model.BasePage;
import com.jsg.base.util.DataUtil;
import com.jsg.employee.dao.IEmployeeSalaryDao;
import com.jsg.employee.model.EmployeeSalary;
/**
 * 
* @ClassName: EmployeeSalaryDaoImpl 
* @Description: TODO(员工薪资管理) 
* @author duanws
* @date 2016-7-12 下午3:17:34 
*
 */
@Repository("employeeSalaryDao")
public class EmployeeSalaryDaoImpl extends BaseDaoImpl implements
		IEmployeeSalaryDao {

	@Override
	public BasePage queryEmployeeSalary(int pageNo, int pageSize,
			EmployeeSalary employeeSalary) {
		StringBuffer hql = new StringBuffer(" from EmployeeSalary e where 1=1");
		//员工id
		if(DataUtil.strIsNotNull(employeeSalary.getEmployee().getId())){
			hql.append(" and e.employee.id='"+employeeSalary.getEmployee().getId()+"'");
		}
		//薪资年月
		if(DataUtil.strIsNotNull(employeeSalary.getSalaryDate())){
			hql.append(" and e.salaryDate='"+employeeSalary.getSalaryDate()+"'");
		}
		return this.queryPage(hql.toString(), pageNo, pageSize, new Object[0]);
	}
	
	

}
