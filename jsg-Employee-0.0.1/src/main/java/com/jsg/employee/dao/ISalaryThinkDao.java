package com.jsg.employee.dao;

import java.util.List;

import com.jsg.base.dao.IBaseDao;
import com.jsg.employee.model.SalaryResult;
/**
 * 
* @ClassName: ISalaryThinkDao 
* @Description: TODO(员工薪资分析) 
* @author duanws
* @date 2016-8-17 上午11:17:00 
*
 */
public interface ISalaryThinkDao extends IBaseDao {

	List getSalarySum(String salaryDate);
}
