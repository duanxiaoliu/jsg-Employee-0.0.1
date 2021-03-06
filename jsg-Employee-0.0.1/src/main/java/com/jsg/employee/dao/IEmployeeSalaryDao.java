package com.jsg.employee.dao;

import java.util.List;

import com.jsg.base.dao.IBaseDao;
import com.jsg.base.model.BasePage;
import com.jsg.employee.model.EmployeeSalary;
import com.jsg.employee.model.SalaryResult;
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
	/**
	 * 
	* @Title: querySalaryResultList 
	* @Description: TODO(获得员工薪资导出信息列表) 
	* @param @param employeeSalary
	* @param @return
	* @return List<SalaryResult>
	* @throws 
	* @author duanws
	* @date 2016-8-1 下午1:31:55
	 */
	List<SalaryResult> querySalaryResultList(EmployeeSalary employeeSalary);
	/**
	 * 
	* @Title: delEmployeeSalaryById 
	* @Description: TODO(通过id删除员工薪资信息) 
	* @param @param id
	* @return void
	* @throws 
	* @author duanws
	* @date 2016-7-13 上午11:49:44
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
	* @date 2016-7-13 下午2:17:12
	 */
	BasePage querySalaryResult(int pageNo,int pageSize,SalaryResult salaryResult);
	
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
	* @Title: getSumSickLeave 
	* @Description: TODO(合算本年已休病假数) 
	* @param @param employeeId
	* @param @param year
	* @param @return
	* @return List<Object[]>
	* @throws 
	* @author duanws
	* @date 2016-7-18 下午4:23:09
	 */
	List<Object[]> getSumSickLeave(String employeeId,String year);
	/**
	 * 
	* @Title: getSumAnnualLeave 
	* @Description: TODO(合算本年已休年假数) 
	* @param @param employeeId
	* @param @param year
	* @param @return
	* @return Object
	* @throws 
	* @author duanws
	* @date 2016-7-19 下午1:43:05
	 */
	List<Object[]> getSumAnnualLeave(String employeeId,String year);
	/**
	 * 
	* @Title: getRwardByDate 
	* @Description: TODO(通过薪资年月查询奖励金额) 
	* @param @param employeeId
	* @param @param salaryDate
	* @param @return
	* @return EmployeeSalary
	* @throws 
	* @author duanws
	* @date 2016-7-19 下午1:22:15
	 */
	EmployeeSalary getRwardByDate(String employeeId,String salaryDate);
	/**
	 * 
	* @Title: delEmployeeSalaryByIdDate 
	* @Description: TODO(通过员工id和薪资日期删除员工薪资情况) 
	* @param @param employeeId
	* @param @param salaryDate
	* @return void
	* @throws 
	* @author duanws
	* @date 2016-7-19 下午1:23:25
	 */
	void delEmployeeSalaryByIdDate(String employeeId,String salaryDate);
	/**
	 * 
	* @Title: delSalaryResultByIdDate 
	* @Description: TODO(通过员工id和薪资日期删除员工薪资结果) 
	* @param @param employeeId
	* @param @param salaryDate
	* @return void
	* @throws 
	* @author duanws
	* @date 2016-7-19 下午1:24:19
	 */
	void delSalaryResultByIdDate(String employeeId,String salaryDate);
	/**
	 * 
	* @Title: getSalaryMouth 
	* @Description: TODO(按月份获得所有员工工资) 
	* @param @param salaryResult
	* @param @return
	* @return List<SalaryResult>
	* @throws 
	* @author duanws
	* @date 2016-8-4 下午3:46:20
	 */
	List<SalaryResult> getSalaryMouth(SalaryResult salaryResult);
}
