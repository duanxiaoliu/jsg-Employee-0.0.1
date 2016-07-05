package com.jsg.employee.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jsg.base.model.BasePage;
import com.jsg.employee.dao.IEmployeeDao;
import com.jsg.employee.model.Employee;
import com.jsg.employee.service.IEmployeeService;
/**
 * 
* @ClassName: EmployeeServiceImpl 
* @Description: TODO(员工管理) 
* @author duanws
* @date 2016-7-4 下午4:44:06 
*
 */
@Service("employeeService")
public class EmployeeServiceImpl implements IEmployeeService {
	
	@Autowired
	private IEmployeeDao employeeDao;
	
	@Override
	public BasePage queryEmpolyee(int pageNo, int pageSize, Employee employee) {
		
		return this.employeeDao.queryEmployee(pageNo, pageSize, employee);
	}

}
