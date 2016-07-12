package com.jsg.employee.dao;

import com.jsg.base.dao.IBaseDao;
import com.jsg.base.model.BasePage;
import com.jsg.employee.model.EmployeeSalary;
/**
 * 
* @ClassName: IEmployeeSalaryDao 
* @Description: TODO(员工薪资管理) 
* @author duanws
* @date 2016-7-12 下午3:16:39 
*
 */
public interface IEmployeeSalaryDao extends IBaseDao {
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
	* @date 2016-7-12 下午4:08:15
	 */
	BasePage queryEmployeeSalary(int pageNo,int pageSize,EmployeeSalary employeeSalary);
}
