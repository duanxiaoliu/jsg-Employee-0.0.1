package com.jsg.employee.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jsg.base.controller.BaseController;
import com.jsg.base.model.BasePage;
import com.jsg.base.util.DataUtil;
import com.jsg.base.util.PageUtil;
import com.jsg.employee.model.Employee;
import com.jsg.employee.model.EmployeeSalary;
import com.jsg.employee.model.SalaryResult;
import com.jsg.employee.service.IEmployeeSalaryService;
/**
 * 
* @ClassName: EmployeeSalaryController 
* @Description: TODO(员工薪资) 
* @author duanws
* @date 2016-7-12 下午3:15:52 
*
 */
@Controller
public class EmployeeSalaryController extends BaseController {

	@Autowired
	private IEmployeeSalaryService employeeSalaryService;
	/**
	 * 
	* @Title: queryEmployeeSalary 
	* @Description: TODO(查询员工薪资信息) 
	* @param @param request
	* @param @param employeeSalary
	* @param @param model
	* @param @return
	* @return String
	* @throws 
	* @author duanws
	* @date 2016-7-12 下午3:37:41
	 */
	@RequestMapping({"employeeManage/employeeSalary/ope-query/queryEmployeeSalary"})
	public String queryEmployeeSalary(HttpServletRequest request,SalaryResult salaryResult,ModelMap model){
		String flag = request.getParameter("flag");
		String pageNo = (request.getParameter("pageNo")!=null)?request.getParameter("pageNo"):"1";
		HttpSession session = request.getSession();
		BasePage page = this.employeeSalaryService.querySalaryResult(Integer.parseInt(pageNo), BasePage.DEFAULT_PAGE_SIZE, salaryResult);
		String pageTag = PageUtil.getPageInfo((int)page.getTotalPageCount(),(int)page.getTotalCount());
		
		if(DataUtil.strIsNotNull(flag) && flag.equals("1")){
			SalaryResult salaryResultB = (SalaryResult) session.getAttribute("salaryResultB");
			if(DataUtil.objIsNotNull(salaryResultB)){
				salaryResult = salaryResultB;
			}
		}
		session.setAttribute("salaryResultB", salaryResult);
		model.addAttribute("pageTag", pageTag);
		model.addAttribute("page", page);
		this.setData(model,null,salaryResult);
		
		return "employee/employee/salary/queryEmployeeSalary";
	}
	/**
	 * 
	* @Title: setData 
	* @Description: TODO(封闭页面元素) 
	* @param @param model
	* @param @param employeeSalary
	* @param @param salaryResult
	* @return void
	* @throws 
	* @author duanws
	* @date 2016-7-12 下午3:43:30
	 */
	private void setData(ModelMap model,EmployeeSalary employeeSalary,SalaryResult salaryResult){
		
		
		
		model.addAttribute("employeeSalary", employeeSalary);
		model.addAttribute("salaryResult", salaryResult);
	}
}
