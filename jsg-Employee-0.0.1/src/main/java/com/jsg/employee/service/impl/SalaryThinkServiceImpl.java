package com.jsg.employee.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jsg.employee.dao.ISalaryThinkDao;
import com.jsg.employee.model.SalaryResult;
import com.jsg.employee.service.ISalaryThinkService;
/**
 * 
* @ClassName: SalaryThinkServiceImpl 
* @Description: TODO(员工薪资分析) 
* @author duanws
* @date 2016-8-17 上午11:15:47 
*
 */

@Service("salaryThinkService")
public class SalaryThinkServiceImpl implements ISalaryThinkService {

	@Autowired
	private ISalaryThinkDao salaryThinkDao;

	@Override
	public List getSalarySum(String salaryDate) {

		return this.salaryThinkDao.getSalarySum(salaryDate);
	}
}
