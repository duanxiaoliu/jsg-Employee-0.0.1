package com.jsg.employee.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jsg.base.controller.BaseController;
import com.jsg.base.model.BaseDic;
import com.jsg.base.model.BasePage;
import com.jsg.base.service.IDicInfoService;
import com.jsg.base.util.DataUtil;
import com.jsg.base.util.PageUtil;
import com.jsg.employee.model.Employee;
import com.jsg.employee.model.EmployeeSalary;
import com.jsg.employee.model.SalaryResult;
import com.jsg.employee.service.IEmployeeSalaryService;
import com.jsg.employee.service.IEmployeeService;
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
	@Autowired
	private IEmployeeService employeeService;
	@Autowired
	private IDicInfoService dicService;
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
	* @Title: editEmployeeSalary 
	* @Description: TODO(新增，修改员工薪资跳转) 
	* @param @param request
	* @param @param model
	* @param @return
	* @return String
	* @throws 
	* @author duanws
	* @date 2016-7-15 上午11:30:43
	 */
	@RequestMapping({"employeeManage/employeeSalary/ope-add/addEmployeeSalary","employeeManage/employeeSalary/ope-update/editEmployeeSalary"})
	public String editEmployeeSalary(HttpServletRequest request,ModelMap model){
		//员工id
		String employeeId = request.getParameter("employeeId");
		//薪资日期
		String salaryDate = request.getParameter("salaryDate");
		//员工
		Employee employee = this.employeeService.getEmployeeById(employeeId);
		//员工薪资情况
		EmployeeSalary employeeSalary = this.employeeSalaryService.getEmployeeSalaryBySalaryDate(employeeId, salaryDate);
		//员工薪资结果
		SalaryResult salaryResult = this.employeeSalaryService.getSalaryResultBySalaryDate(employeeId, salaryDate);
		if(!DataUtil.objIsNotNull(employeeSalary) || !DataUtil.strIsNotNull(employeeSalary.getId())){
			employeeSalary = new EmployeeSalary();
			employeeSalary.setEmployee(employee);
		}
		if(!DataUtil.objIsNotNull(salaryResult) || !DataUtil.strIsNotNull(salaryResult.getId())){
			salaryResult = new SalaryResult();
			salaryResult.setEmployee(employee);
		}
		this.setData(model, employeeSalary,salaryResult);
		
		return "employee/employee/salary/editEmployeeSalary";
	}
	/**
	 * 
	* @Title: checkEmployeeSalaryIsExist 
	* @Description: TODO(通过员工薪资日期判断是否唯一) 
	* @param @return
	* @return String
	* @throws 
	* @author duanws
	* @date 2016-7-27 下午3:20:02
	 */
	@RequestMapping(value={"salary/employeeSalary/check/checkEmployeeSalaryIsExist"},produces={"text/plain"})
	public @ResponseBody String checkEmployeeSalaryIsExist(HttpServletRequest request){
		String id = request.getParameter("id");
		String salaryDate = request.getParameter("salaryDate");
		if(this.employeeSalaryService.checkEmployeeSalaryIsExist(id, salaryDate)){
			return "true";
		}
		return "false";
	}
	/**
	 * 
	* @Title: computeSalary 
	* @Description: TODO(计算员工薪资) 
	* @param @param request
	* @param @param employeeSalary
	* @param @param model
	* @param @return
	* @return String
	* @throws 
	* @author duanws
	* @date 2016-7-18 下午3:35:06
	 */
	@RequestMapping(value={"employeeManage/employeeSalary/ope-add/saveEmployeeSalary","employeeManage/employeeSalary/ope-update/saveEmployeeSalary"},produces={"text/plain;charset=UTF-8"})
	public @ResponseBody String computeSalary(HttpServletRequest request,EmployeeSalary employeeSalary,ModelMap model){
		SalaryResult salaryResult = this.employeeSalaryService.computeSalary(employeeSalary);
		salaryResult.setEmployee(null);
		//将员工薪资计算结果转成json格式用于页面显示
		JSONObject obj = JSONObject.fromObject(salaryResult);
		return obj.toString();
	}
	/**
	 * 
	* @Title: viewEmployeeSalaryResult 
	* @Description: TODO(查看员工薪资信息) 
	* @param @param request
	* @param @param model
	* @param @return
	* @return String
	* @throws 
	* @author duanws
	* @date 2016-7-19 上午11:47:42
	 */
	@RequestMapping({"employeeManage/employeeSalary/ope-view/viewEmployeeSalary"})
	public String viewEmployeeSalaryResult(HttpServletRequest request,ModelMap model){
		//员工id
		String employeeId = request.getParameter("employeeId");
		//薪资日期
		String salaryDate = request.getParameter("salaryDate");
		//员工
		Employee employee = this.employeeService.getEmployeeById(employeeId);
		//员工薪资情况
		EmployeeSalary employeeSalary = this.employeeSalaryService.getEmployeeSalaryBySalaryDate(employeeId, salaryDate);
		//员工薪资结果
		SalaryResult salaryResult = this.employeeSalaryService.getSalaryResultBySalaryDate(employeeId, salaryDate);
		this.setData(model, employeeSalary, salaryResult);
		return "employee/employee/salary/viewEmployeeSalary";
	}
	/**
	 * 
	* @Title: delEmployeeSalaryByIdDate 
	* @Description: TODO(删除员工薪资信息) 
	* @param @param employeeId
	* @param @param salaryDate
	* @param @param request
	* @param @return
	* @return String
	* @throws 
	* @author duanws
	* @date 2016-7-19 下午2:03:13
	 */
	@RequestMapping(value={"employeeManage/employeeSalary/ope-del/delEmployeeSalary/{employeeId}/{salaryDate}"},produces={"text/palin;charset=UTF-8"})
	public @ResponseBody String delEmployeeSalaryByIdDate(@PathVariable("employeeId") String employeeId,@PathVariable("salaryDate") String salaryDate,HttpServletRequest request){
		try{
			this.employeeSalaryService.delEmployeeSalaryByIdDate(employeeId, salaryDate);
			this.employeeSalaryService.delSalaryResultByIdDate(employeeId, salaryDate);
		}catch(Exception e){
			return "error";
		}
		return "success";
	}
	/**
	 * 
	* @Title: exportEmployeeSalary 
	* @Description: TODO(导出员工薪资情况) 
	* @param @param request
	* @param @param response
	* @param @param employeeSalary
	* @return void
	* @throws 
	* @author duanws
	* @date 2016-7-21 下午5:02:30
	 */
	@RequestMapping({"employeeManage/employeeSalary/ope-query/exportEmployeeSalary"})
	public void exportEmployeeSalary(HttpServletRequest request,HttpServletResponse response,EmployeeSalary employeeSalary){
		
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
		List<BaseDic> YNListDic = this.dicService.getDicListByCode("Y_N");
		
		//是否
		model.addAttribute("YNListDic", YNListDic);
		model.addAttribute("employeeSalary", employeeSalary);
		model.addAttribute("salaryResult", salaryResult);
	}
}
