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
import com.jsg.employee.model.Customer;
import com.jsg.employee.model.Employee;
import com.jsg.employee.service.IEmployeeService;
/**
 * 
* @ClassName: EmployeeController 
* @Description: TODO(员工管理) 
* @author duanws
* @date 2016-7-4 下午4:41:00 
*
 */
@Controller
public class EmployeeController extends BaseController {

	@Autowired
	private IEmployeeService employeeService;
	/**
	 * 
	* @Title: queryEmpoyee 
	* @Description: TODO(分页查询员工信息) 
	* @param @param request
	* @param @param employee
	* @param @param model
	* @param @return
	* @return String
	* @throws 
	* @author duanws
	* @date 2016-7-5 下午4:50:36
	 */
	@RequestMapping({"employeeManage/EmployeeManage/queryEmployee","employeeManage/EmployeeManage/ope-query/queryEmployee"})
	public String queryEmpoyee(HttpServletRequest request,Employee employee,ModelMap model){
		String flag = request.getParameter("flag");
		String pageNo = (request.getParameter("pageNo")!=null)?request.getParameter("pageNo"):"1";
		HttpSession session = request.getSession();
		BasePage page = this.employeeService.queryEmpolyee(Integer.parseInt(pageNo), BasePage.DEFAULT_PAGE_SIZE, employee);
		String pageTag = PageUtil.getPageInfo((int)page.getTotalPageCount(),(int)page.getTotalCount());
		
		if(DataUtil.strIsNotNull(flag) && flag.equals("1")){
			Employee employeeB = (Employee) session.getAttribute("employeeB");
			if(DataUtil.objIsNotNull(employeeB)){
				employee = employeeB;
			}
		}
		session.setAttribute("employeeB", employee);
		model.addAttribute("pageTag", pageTag);
		model.addAttribute("page", page);
		this.setData(model,employee);
		return "employee/employee/queryEmployee";		
	}
	/**
	 * 
	* @Title: setData 
	* @Description: TODO(封闭页面显示元素) 
	* @param @param model
	* @param @param employee
	* @return void
	* @throws 
	* @author duanws
	* @date 2016-7-5 下午4:51:15
	 */
	private void setData(ModelMap model,Employee employee){
		
		
		model.addAttribute("employee", employee);
	}
}
