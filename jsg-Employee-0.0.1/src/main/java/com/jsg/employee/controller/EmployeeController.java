package com.jsg.employee.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
	* @Title: isExistEmployeeCode 
	* @Description: TODO(通过员工编号进行唯一验证) 
	* @param @param request
	* @param @return
	* @return String
	* @throws 
	* @author duanws
	* @date 2016-7-7 下午2:34:21
	 */
	@RequestMapping(value={"employeeManage/employeeManage/ope-query/isExistEmployeeCode"},produces={"text/plain;charset=UTF-8"})
	public @ResponseBody String isExistEmployeeCode(HttpServletRequest request){
		String id = request.getParameter("id");
		String code = request.getParameter("code");
		boolean check = this.employeeService.isExistEmployeeCode(id, code);
		if(check){
			return "true";
		}else{
			return "false";
		}
	}
	/**
	 * 
	* @Title: saveEmployee 
	* @Description: TODO(保存员工信息) 
	* @param @param request
	* @param @param model
	* @param @param employee
	* @param @return
	* @return String
	* @throws 
	* @author duanws
	* @date 2016-7-7 下午2:37:05
	 */
	@RequestMapping({"employeeManage/employeeManage/ope-save/saveEmployee"})
	public String saveEmployee(HttpServletRequest request,ModelMap model,Employee employee){
		String id = employee.getId();
		if(DataUtil.strIsNotNull(id)){
			this.employeeService.updateEmployee(employee);
		}else{
			this.employeeService.saveEmployee(employee);
		}
		return "redirect:/employeeManage/EmployeeManage/ope-query/queryEmployee.do";
	}
	/**
	 * 
	* @Title: delEmployee 
	* @Description: TODO() 
	* @param @param request
	* @return void
	* @throws 
	* @author duanws
	* @date 2016-7-7 下午3:48:04
	 */
	@RequestMapping(value={"employeeManage/employeeManage/ope-del/delEmployee"},produces={"text/pain;charset=UTF-8"})
	public @ResponseBody String delEmployee(HttpServletRequest request){
		String id = request.getParameter("id");
		if(DataUtil.objIsNotNull(id)){
			try{
				this.employeeService.delEmployeeById(id);
			}catch(Exception e){
				return "error";
			}
		}else{
			return "error";
		}
		return "success";
	}
	/**
	 * 
	* @Title: viewEmployee 
	* @Description: TODO(查看员工信息) 
	* @param @param request
	* @param @param model
	* @param @return
	* @return String
	* @throws 
	* @author duanws
	* @date 2016-7-7 下午3:55:48
	 */
	@RequestMapping({"employeeManage/employeeManage/ope-view/viewEmployee"})
	public String viewEmployee(HttpServletRequest request,ModelMap model){
		String id = request.getParameter("id");
		Employee employee = this.employeeService.getEmployeeById(id);
		this.setData(model, employee);
		return "employee/employee/viewEmployee";
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
		//户口性质
		model.addAttribute("registerList", registerList);
		//是否
		model.addAttribute("YNDicList", YNDicList);
		model.addAttribute("employee", employee);
	}
}
