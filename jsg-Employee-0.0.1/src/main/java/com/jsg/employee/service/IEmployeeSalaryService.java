package com.jsg.employee.service;

import com.jsg.base.model.BasePage;
import com.jsg.base.service.IBaseService;
import com.jsg.employee.model.EmployeeSalary;
/**
 * 
* @ClassName: IEmployeeSalaryService 
* @Description: TODO(员工薪资管理) 
* @author duanws
* @date 2016-7-12 下午3:19:30 
*
 */
public interface IEmployeeSalaryService extends IBaseService {
	/**
	 * 
	* @Title: queryEmployeeSalary 
	* @Description: TODO(分页查询员工薪资信息) 
	* @param @param pageNo
	* @param @param pageSize
	* @param @param employeeSalary
	* @param @return
	* @return BasePage
	* @throws 
	* @author duanws
	* @date 2016-7-12 下午4:08:55
	 */
	BasePage queryEmployeeSalary(int pageNo,int pageSize,EmployeeSalary employeeSalary);
}
