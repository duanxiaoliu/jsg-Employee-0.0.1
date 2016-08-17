package com.jsg.employee.service;

import javax.servlet.http.HttpServletResponse;

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
	* @Title: checkEmployeeSalaryIsExist 
	* @Description: TODO(通过薪资日期验证是否唯一) 
	* @param @param id
	* @param @param salaryDate
	* @param @return
	* @return boolean
	* @throws 
	* @author duanws
	* @date 2016-7-27 下午3:36:31
	 */
	boolean checkEmployeeSalaryIsExist(String id,String salaryDate);
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
	* @Title: exportEmployeeSalary 
	* @Description: TODO(导出员工薪资信息) 
	* @param @param employeeSalary
	* @return void
	* @throws 
	* @author duanws
	* @date 2016-7-29 下午4:11:10
	 */
	void exportEmployeeSalary(EmployeeSalary employeeSalary,HttpServletResponse response);
	/**
	 * 
	* @Title: exportEmployeeSalaryMouth 
	* @Description: TODO(按月导出所有员工工资) 
	* @param @param salaryResult
	* @param @param response
	* @return void
	* @throws 
	* @author duanws
	* @date 2016-8-4 下午3:39:43
	 */
	void exportEmployeeSalaryMouth(SalaryResult salaryResult,HttpServletResponse response);
}
