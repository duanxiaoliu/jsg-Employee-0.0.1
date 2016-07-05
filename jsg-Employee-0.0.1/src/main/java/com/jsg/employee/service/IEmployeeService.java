package com.jsg.employee.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.jsg.base.model.BasePage;
import com.jsg.base.service.IBaseService;
import com.jsg.employee.dao.IEmployeeDao;
import com.jsg.employee.model.Employee;
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

}
