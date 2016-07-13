package com.jsg.employee.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jsg.base.model.BasePage;
import com.jsg.employee.dao.IEmployeeSalaryDao;
import com.jsg.employee.model.EmployeeSalary;
import com.jsg.employee.model.SalaryResult;
import com.jsg.employee.service.IEmployeeSalaryService;
/**
 * 
* @ClassName: EmployeeSalaryServiceImpl 
* @Description: TODO(员工薪资管理) 
* @author duanws
* @date 2016-7-12 下午3:18:54 
*
 */
@Service("employeeSalaryService")
public class EmployeeSalaryServiceImpl implements IEmployeeSalaryService {

	@Autowired
	private IEmployeeSalaryDao employeeSalaryDao;

	@Override
	public BasePage queryEmployeeSalary(int pageNo, int pageSize,
			EmployeeSalary employeeSalary) {
		return this.employeeSalaryDao.queryEmployeeSalary(pageNo, pageSize, employeeSalary);
	}

	@Override
	public void delEmployeeSalaryById(String id,String salaryDate) {
		this.employeeSalaryDao.delEmployeeSalaryById(id,salaryDate);
		
	}

	@Override
	public BasePage querySalaryResult(int pageNo, int pageSize,
			SalaryResult salaryResult) {
		
		return this.employeeSalaryDao.querySalaryResult(pageNo, pageSize, salaryResult);
	}
}
