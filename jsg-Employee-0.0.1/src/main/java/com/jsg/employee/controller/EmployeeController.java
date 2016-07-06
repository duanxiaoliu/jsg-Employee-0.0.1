package com.jsg.employee.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jsg.base.controller.BaseController;
import com.jsg.base.model.BaseDic;
import com.jsg.base.model.BasePage;
import com.jsg.base.service.IDicInfoService;
import com.jsg.base.util.DataUtil;
import com.jsg.base.util.PageUtil;
import com.jsg.employee.model.Customer;
import com.jsg.employee.model.Employee;
import com.jsg.employee.service.ICustomerService;
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
	@Autowired
	private IDicInfoService dicService;
	@Autowired
	private ICustomerService customerService;
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
	* @Title: editEmployee 
	* @Description: TODO(跳转新增，修改页面) 
	* @param @param request
	* @param @param model
	* @param @param employee
	* @param @return
	* @return String
	* @throws 
	* @author duanws
	* @date 2016-7-6 下午3:01:49
	 */
	@RequestMapping({"employeeManage/employeeManage/ope-add/addEmployee","employeeManage/employeeManage/ope-update/editEmployee"})
	public String editEmployee(HttpServletRequest request,ModelMap model,Employee employee){
		String id = request.getParameter("id");
		if(DataUtil.strIsNotNull(id)){
			employee = this.employeeService.getEmployeeById(id);
		}else{
			employee = new Employee();
		}
		this.setData(model, employee);
		return "employee/employee/editEmployee";
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
		List<BaseDic> departmentDicList = this.dicService.getDicListByCode("DEPARTMENT");
		List<Customer> customerList = this.customerService.getCustomerList();
		List<BaseDic> registerList = this.dicService.getDicListByCode("REGISTER");
		List<BaseDic> YNDicList = this.dicService.getDicListByCode("Y_N");
		//部门
		model.addAttribute("departmentDicList", departmentDicList);
		//客户
		model.addAttribute("customerList", customerList);
		//客户性质
		model.addAttribute("registerList", registerList);
		//是否
		model.addAttribute("YNDicList", YNDicList);
		model.addAttribute("employee", employee);
	}
}
