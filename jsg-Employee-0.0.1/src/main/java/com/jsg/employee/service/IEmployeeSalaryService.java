package com.jsg.employee.service;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

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
	* @Title: computeSalary 
	* @Description: TODO(计算员工薪资) 
	* @param @param employeeSalary
	* @param @return
	* @return SalaryResult
	* @throws 
	* @author duanws
	* @date 2016-7-18 下午3:54:06
	 */
	SalaryResult computeSalary(EmployeeSalary employeeSalary);
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
	/**
	 * 
	* @Title: delEmployeeSalaryByIdDate 
	* @Description: TODO(通过员工id和薪资月份删除员工薪资情况) 
	* @param @param employeeId
	* @param @param salaryDate
	* @return void
	* @throws 
	* @author duanws
	* @date 2016-7-19 下午1:30:16
	 */
	void delEmployeeSalaryByIdDate(String employeeId,String salaryDate);
	/**
	 * 
	* @Title: delSalaryResultByIdDate 
	* @Description: TODO(通过员工id和薪资月份删除员工薪资结果) 
	* @param @param employeeId
	* @param @param salaryDate
	* @return void
	* @throws 
	* @author duanws
	* @date 2016-7-19 下午1:30:48
	 */
	void delSalaryResultByIdDate(String employeeId,String salaryDate);
	/**
	 * 
	* @Title: getWorkBook 
	* @Description: TODO(获得导出信息) 
	* @param @param employeeSalary
	* @param @return
	* @return HSSFWorkbook
	* @throws 
	* @author duanws
	* @date 2016-7-21 下午5:12:10
	 */
	HSSFWorkbook getWorkBook(EmployeeSalary employeeSalary);
}
