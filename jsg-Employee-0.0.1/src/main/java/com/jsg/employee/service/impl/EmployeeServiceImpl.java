package com.jsg.employee.service.impl;

import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jsg.base.model.BasePage;
import com.jsg.base.model.UserInfo;
import com.jsg.employee.dao.IEmployeeDao;
import com.jsg.employee.model.Employee;
import com.jsg.employee.service.IEmployeeService;
/**
 * 
* @ClassName: EmployeeServiceImpl 
* @Description: TODO(员工管理) 
* @author duanws
* @date 2016-7-4 下午4:44:06 
*
 */
@Service("employeeService")
public class EmployeeServiceImpl implements IEmployeeService {
	
	@Autowired
	private IEmployeeDao employeeDao;
	
	@Override
	public BasePage queryEmpolyee(int pageNo, int pageSize, Employee employee) {
		
		return this.employeeDao.queryEmployee(pageNo, pageSize, employee);
	}

	@Override
	public Employee getEmployeeById(String id) {
		
		return (Employee) this.employeeDao.get(Employee.class, id);
	}

	@Override
	public void saveEmployee(Employee employee) {
		this.employeeDao.save(employee);
		
	}

	@Override
	public void updateEmployee(Employee employee) {
		this.employeeDao.update(employee);
		
	}

	@Override
	public void delEmployeeById(String id) {
		this.employeeDao.delEmployeeById(id);
		
	}

	@Override
	public boolean isExistEmployeeCode(String id, String employeeCode) {
		String hql = " from Employee e where e.employeeCode='"+employeeCode+"'";
		List<Employee> list = this.employeeDao.queryList(hql, new Object[0]);
		if((list != null) && (list.size()>0)){
			if(list.size()==1){
				if(!list.get(0).getId().equals(id)){
					return false;
				}
			}else if(list.size() > 1){
				return false;
			}
		}
		return true;
	}

	@Override
	public HSSFWorkbook getWorkBook(Employee employee) {
		// TODO Auto-generated method stub
		return null;
	}

}
