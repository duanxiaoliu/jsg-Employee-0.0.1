package com.jsg.employee.service;

import java.util.List;

import com.jsg.base.service.IBaseService;
import com.jsg.employee.model.SalaryResult;
/**
 * 
* @ClassName: ISalaryThinkService 
* @Description: TODO(员工薪资分析) 
* @author duanws
* @date 2016-8-17 上午11:16:45 
*
 */
public interface ISalaryThinkService extends IBaseService {
	
	List getSalarySum(String salaryDate);
}
