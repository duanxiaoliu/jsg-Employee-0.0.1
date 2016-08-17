package com.jsg.employee.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.jsg.base.dao.impl.BaseDaoImpl;
import com.jsg.employee.dao.ISalaryThinkDao;
import com.jsg.employee.model.SalaryResult;
/**
 * 
* @ClassName: SalaryThinkDaoImpl 
* @Description: TODO(员工薪资分析) 
* @author duanws
* @date 2016-8-17 上午11:14:18 
*
 */
@Repository("salaryThinkDao")
public class SalaryThinkDaoImpl extends BaseDaoImpl implements ISalaryThinkDao {

	@Override
	public List getSalarySum(String salaryDate) {
		String hql = "select sum(sr.sum) as sum,sr.salaryDate from SalaryResult sr group by sr.salaryDate having sr.salaryDate like '"+salaryDate+"%'";
		
		return this.queryList(hql, new Object[0]);
	}

}
