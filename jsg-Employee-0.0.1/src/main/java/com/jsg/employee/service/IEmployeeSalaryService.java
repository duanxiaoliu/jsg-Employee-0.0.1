package com.jsg.employee.service;

import com.jsg.base.model.BasePage;
import com.jsg.base.service.IBaseService;
import com.jsg.employee.model.EmployeeSalary;
import com.jsg.employee.model.SalaryResult;
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
	/**\
	 * 
	* @Title: delEmployeeSalaryById 
	* @Description: TODO(通过id删除员工薪资信息) 
	* @param @param id
	* @return void
	* @throws 
	* @author duanws
	* @date 2016-7-13 上午11:51:58
	 */
	void delEmployeeSalaryById(String id,String salaryDate);
	/**
	 * 
	* @Title: querySalaryResult 
	* @Description: TODO(分页查询员工薪资结果) 
	* @param @param pageNo
	* @param @param pageSize
	* @param @param salaryResult
	* @param @return
	* @return BasePage
	* @throws 
	* @author duanws
	* @date 2016-7-13 下午2:16:26
	 */
	BasePage querySalaryResult(int pageNo,int pageSize,SalaryResult salaryResult);
	/**
	 * 
	* @Title: saveEmployeeSalary 
	* @Description: TODO(保存员工薪资情况) 
	* @param @param employeeSalary
	* @return void
	* @throws 
	* @author duanws
	* @date 2016-7-13 下午4:21:44
	 */
	void saveEmployeeSalary(EmployeeSalary employeeSalary);
	/**
	 * 
	* @Title: saveSalaryResult 
	* @Description: TODO(保存员工薪资结果) 
	* @param @param salaryResult
	* @return void
	* @throws 
	* @author duanws
	* @date 2016-7-13 下午4:22:24
	 */
	void saveSalaryResult(SalaryResult salaryResult);
	/**
	 * 
	* @Title: getEmployeeSalaryBySalaryDate 
	* @Description: TODO(通过薪资日期查询员工薪资情况) 
	* @param @param employeeId
	* @param @param salaryDate
	* @param @return
	* @return EmployeeSalary
	* @throws 
	* @author duanws
	* @date 2016-7-15 下午2:22:16
	 */
	EmployeeSalary getEmployeeSalaryBySalaryDate(String employeeId,String salaryDate);
	/**
	 * 
	* @Title: getSalaryResultBySalaryDate 
	* @Description: TODO(通过薪资日期查询员工薪资结果) 
	* @param @param employeeId
	* @param @param salaryDate
	* @param @return
	* @return SalaryResult
	* @throws 
	* @author duanws
	* @date 2016-7-15 下午2:23:10
	 */
	SalaryResult getSalaryResultBySalaryDate(String employeeId,String salaryDate);
}
