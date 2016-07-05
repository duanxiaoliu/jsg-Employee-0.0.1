package com.jsg.employee.dao;

import com.jsg.base.dao.IBaseDao;
import com.jsg.base.model.BasePage;
import com.jsg.employee.model.Employee;
/**
 * 
* @ClassName: IEmployeeDao 
* @Description: TODO(员工管理) 
* @author duanws
* @date 2016-7-4 下午4:42:51 
*
 */
public interface IEmployeeDao extends IBaseDao {
	/**
	 * 
	* @Title: queryEmployee 
	* @Description: TODO(分页查询员工信息) 
	* @param @param pageNo
	* @param @param pageSize
	* @param @param employee
	* @param @return
	* @return BasePage
	* @throws 
	* @author duanws
	* @date 2016-7-5 下午2:40:25
	 */
	BasePage queryEmployee(int pageNo,int pageSize,Employee employee);
}
