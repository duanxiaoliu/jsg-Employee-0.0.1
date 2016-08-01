package com.jsg.employee.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.jsg.base.dao.impl.BaseDaoImpl;
import com.jsg.base.model.BasePage;
import com.jsg.base.util.DataUtil;
import com.jsg.employee.dao.IEmployeeSalaryDao;
import com.jsg.employee.model.EmployeeSalary;
import com.jsg.employee.model.SalaryResult;
/**
 * 
* @ClassName: EmployeeSalaryDaoImpl 
* @Description: TODO(员工薪资管理) 
* @author duanws
* @date 2016-7-12 下午3:17:34 
*
 */
@Repository("employeeSalaryDao")
public class EmployeeSalaryDaoImpl extends BaseDaoImpl implements
		IEmployeeSalaryDao {

	@Override
	public BasePage queryEmployeeSalary(int pageNo, int pageSize,
			EmployeeSalary employeeSalary) {
		StringBuffer hql = new StringBuffer(" from EmployeeSalary e where 1=1");
		//员工id
		if(DataUtil.strIsNotNull(employeeSalary.getEmployee().getId())){
			hql.append(" and e.employee.id='"+employeeSalary.getEmployee().getId()+"'");
		}
		//薪资年月
		if(DataUtil.strIsNotNull(employeeSalary.getSalaryDate())){
			hql.append(" and e.salaryDate='"+employeeSalary.getSalaryDate()+"'");
		}
		return this.queryPage(hql.toString(), pageNo, pageSize, new Object[0]);
	}

	@Override
	public void delEmployeeSalaryById(String id,String salaryDate) {
		String hql = " delete from EmployeeSalary e where e.employee.id='"+id+"' and e.salaryDate='"+salaryDate+"'";
		this.executeHql(hql, new Object[0]);
		
	}

	@Override
	public BasePage querySalaryResult(int pageNo, int pageSize,
			SalaryResult salaryResult) {
		StringBuffer hql = new StringBuffer(" from SalaryResult e where 1=1");
		//员工id
		if(DataUtil.strIsNotNull(salaryResult.getEmployee().getId())){
			hql.append(" and e.employee.id='"+salaryResult.getEmployee().getId()+"'");
		}
		//薪资年月
		if(DataUtil.strIsNotNull(salaryResult.getSalaryDate())){
			hql.append(" and e.salaryDate='"+salaryResult.getSalaryDate()+"'");
		}
		return this.queryPage(hql.toString(), pageNo, pageSize, new Object[0]);
	}

	@Override
	public EmployeeSalary getEmployeeSalaryBySalaryDate(String employeeId,
			String salaryDate) {
		String hql = " from EmployeeSalary e where e.employee.id='"+employeeId+"' and e.salaryDate='"+salaryDate+"'";
		return (EmployeeSalary) this.queryUnique(hql, new Object[0]);
	}

	@Override
	public SalaryResult getSalaryResultBySalaryDate(String employeeId,
			String salaryDate) {
		String hql = " from SalaryResult e where e.employee.id='"+employeeId+"' and e.salaryDate='"+salaryDate+"'";
		return (SalaryResult) this.queryUnique(hql, new Object[0]);
	}

	@Override
	public List<Object[]> getSumSickLeave(String employeeId, String year) {
		StringBuffer hql = new StringBuffer(" select sum(es.sickLeave) as sickLeave,sum(es.trySickLeave) as trySickLeave from EmployeeSalary es where es.employee.id='"+employeeId+"' and es.salaryDate like'%"+year+"%'");
		return this.queryList(hql.toString(), new Object[0]);
	}

	@Override
	public List<Object[]> getSumAnnualLeave(String employeeId, String year) {
		StringBuffer hql = new StringBuffer(" select sum(es.annualLeave) as annualLeave from EmployeeSalary es where es.employee.id='"+employeeId+"' and es.salaryDate.like'%"+year+"%'");
		return this.queryList(hql.toString(), new Object[0]);
	}

	@Override
	public EmployeeSalary getRwardByDate(String employeeId, String salaryDate) {
		StringBuffer hql = new StringBuffer(" from EmployeeSalary es where es.rewardTime='"+salaryDate+"' and es.employee.id='"+employeeId+"'");
		return (EmployeeSalary) this.queryUnique(hql.toString(), new Object[0]);
	}

	@Override
	public void delEmployeeSalaryByIdDate(String employeeId, String salaryDate) {
		String hql = " delete from EmployeeSalary es where es.employee.id='"+employeeId+"' and es.salaryDate='"+salaryDate+"'";
		this.executeHql(hql, new Object[0]);
	}

	@Override
	public void delSalaryResultByIdDate(String employeeId, String salaryDate) {
		String hql = " delete from SalaryResult es where es.employee.id='"+employeeId+"' and es.salaryDate='"+salaryDate+"'";
		this.executeHql(hql, new Object[0]);
		
	}

	@Override
	public List<SalaryResult> querySalaryResultList(
			EmployeeSalary employeeSalary) {
		StringBuffer hql = new StringBuffer(" from EmployeeSalary e where 1=1");
		//员工id
		if(DataUtil.strIsNotNull(employeeSalary.getEmployee().getId())){
			hql.append(" and e.employee.id='"+employeeSalary.getEmployee().getId()+"'");
		}
		//薪资年月
		if(DataUtil.strIsNotNull(employeeSalary.getSalaryDate())){
			hql.append(" and e.salaryDate='"+employeeSalary.getSalaryDate()+"'");
		}
		return this.queryList(hql.toString(), new Object[0]);
	}
	
	
	
	

}
