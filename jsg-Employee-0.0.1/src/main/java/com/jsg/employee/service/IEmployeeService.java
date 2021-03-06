package com.jsg.employee.service;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;

import com.jsg.base.model.BasePage;
import com.jsg.base.service.IBaseService;
import com.jsg.employee.dao.IEmployeeDao;
import com.jsg.employee.model.Employee;
import com.jsg.employee.model.EmployeeSalary;
/**
 * 
* @ClassName: IEmployeeService 
* @Description: TODO(员工管理) 
* @author duanws
* @date 2016-7-4 下午4:44:33 
*
 */
public interface IEmployeeService extends IBaseService {
	/**
	 * 
	* @Title: queryEmpolyee 
	* @Description: TODO(分页查询员工信息) 
	* @param @param pageNo
	* @param @param pageSize
	* @param @param employee
	* @param @return
	* @return BasePage
	* @throws 
	* @author duanws
	* @date 2016-7-5 下午4:43:15
	 */
	BasePage queryEmpolyee(int pageNo,int pageSize,Employee employee);
	/**
	 * 
	* @Title: getEmployeeById 
	* @Description: TODO(通过id查询员工信息) 
	* @param @param id
	* @param @return
	* @return Employee
	* @throws 
	* @author duanws
	* @date 2016-7-6 下午3:06:12
	 */
	Employee getEmployeeById(String id);
	/**
	 * 
	* @Title: saveEmployee 
	* @Description: TODO(保存员工信息) 
	* @param @param employee
	* @return void
	* @throws 
	* @author duanws
	* @date 2016-7-6 下午3:23:06
	 */
	void saveEmployee(Employee employee);
	/**
	 * 
	* @Title: updateEmployee 
	* @Description: TODO(修改员工信息) 
	* @param @param employee
	* @return void
	* @throws 
	* @author duanws
	* @date 2016-7-6 下午3:23:15
	 */
	void updateEmployee(Employee employee);
	/**
	 * 
	* @Title: delEmployeeById 
	* @Description: TODO(通过id删除员工信息) 
	* @param @param id
	* @return void
	* @throws 
	* @author duanws
	* @date 2016-7-6 下午3:31:04
	 */
	void delEmployeeById(String id);
	/**
	 * 
	* @Title: isExistEmployeeCode 
	* @Description: TODO(通过员工编码验证是否唯一) 
	* @param @param id
	* @param @param employeeCode
	* @param @return
	* @return boolean
	* @throws 
	* @author duanws
	* @date 2016-7-7 下午2:26:44
	 */
	boolean isExistEmployeeCode(String id,String employeeCode);
	/**
	 * 
	* @Title: getWorkBook 
	* @Description: TODO(获得导出信息) 
	* @param @param employee
	* @param @return
	* @return HSSFWorkbook
	* @throws 
	* @author duanws
	* @date 2016-7-21 下午5:12:35
	 */
	HSSFWorkbook getWorkBook(Employee employee);
}
