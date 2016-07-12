package com.jsg.employee.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jsg.base.controller.BaseController;
import com.jsg.base.model.BaseDic;
import com.jsg.base.model.BasePage;
import com.jsg.base.service.IDicInfoService;
import com.jsg.base.util.DataUtil;
import com.jsg.base.util.PageUtil;
import com.jsg.employee.model.Allowance;
import com.jsg.employee.model.Customer;
import com.jsg.employee.service.ICustomerService;
/**
 * 
* @ClassName: CustomerController 
* @Description: TODO(处理客户信息) 
* @author duanws
* @date 2016-6-29 下午5:09:24 
*
 */
@Controller
public class CustomerController extends BaseController {
	
	@Autowired
	private ICustomerService customerService;
	@Autowired
	private IDicInfoService dicService;
	/**
	 * 
	* @Title: queryCustomer 
	* @Description: TODO(分页查询客户信息) 
	* @param @param request
	* @param @param customer
	* @param @return
	* @return String
	* @throws 
	* @author duanws
	* @date 2016-6-30 下午2:48:08
	 */
	@RequestMapping({"employeeManage/customerManage/queryCustomer","employeeManage/customerManage/ope-query/queryCustomer"})
	public String queryCustomer(HttpServletRequest request,Customer customer,ModelMap model){
		String flag = request.getParameter("flag");
		String pageNo = (request.getParameter("pageNo")!=null)?request.getParameter("pageNo"):"1";
		HttpSession session = request.getSession();
		BasePage page = this.customerService.queryCustomer(Integer.parseInt(pageNo), BasePage.DEFAULT_PAGE_SIZE, customer);
		String pageTag = PageUtil.getPageInfo((int)page.getTotalPageCount(),(int)page.getTotalCount());
		if(DataUtil.strIsNotNull(flag) && flag.equals("1")){
			Customer customerB = (Customer) session.getAttribute("customerB");
			if(DataUtil.objIsNotNull(customerB)){
				customer = customerB;
			}
		}
		session.setAttribute("customerB", customer);
		model.addAttribute("pageTag", pageTag);
		model.addAttribute("page", page);
		this.setData(customer, model);
		return "employee/customer/queryCustomer";
	}
	/**
	 * 
	* @Title: editCustomer 
	* @Description: TODO(跳转到编辑页面) 
	* @param @param request
	* @param @param model
	* @param @return
	* @return String
	* @throws 
	* @author duanws
	* @date 2016-7-1 下午3:07:42
	 */
	@RequestMapping({"employeeManage/customerManage/ope-add/addCustomer","employeeManage/customerManage/ope-update/editCustomer"})
	public String editCustomer(HttpServletRequest request,ModelMap model,Customer customer){
		String id = request.getParameter("id");
		if(DataUtil.strIsNotNull(id)){
			customer = this.customerService.getCustomerById(id);
		}else{
			customer = new Customer();
		}
		
		this.setData(customer, model);
		return "employee/customer/editCustomer";
	}
	/**
	 * 
	* @Title: saveCustomer 
	* @Description: TODO(保存，修改客户信息) 
	* @param @param request
	* @param @param model
	* @param @param customer
	* @param @return
	* @return String
	* @throws 
	* @author duanws
	* @date 2016-7-4 下午1:55:44
	 */
	@RequestMapping({"employeeManage/customerManage/ope-add/saveCustomer"})
	public String saveCustomer(HttpServletRequest request,ModelMap model,Customer customer){
		String id = customer.getId();
		//修改
		if(DataUtil.strIsNotNull(id)){
			this.customerService.updateCustomer(customer);
		}else{
			this.customerService.saveCustomer(customer);
		}
		return "redirect:/employeeManage/customerManage/ope-query/queryCustomer.do";
	}
	/**
	 * 
	* @Title: delCustomer 
	* @Description: TODO(删除用户信息) 
	* @param @param request
	* @param @param id
	* @param @return
	* @return String
	* @throws 
	* @author duanws
	* @date 2016-7-4 下午2:01:42
	 */
	@RequestMapping(value={"employeeManage/customerManage/ope-del/delCustomer/{id}"},produces={"text/plain;;charset=UTF-8"},method=RequestMethod.DELETE)
	public @ResponseBody String delCustomer(HttpServletRequest request,@PathVariable("id") String id){
		try{
			this.customerService.delCustomerById(id);
		}catch(Exception e){
			return "error";
		}
		return "success";
	}
	/**
	 * 
	* @Title: viewCustomer 
	* @Description: TODO(查看用户信息) 
	* @param @param request
	* @param @return
	* @return String
	* @throws 
	* @author duanws
	* @date 2016-7-4 下午2:04:05
	 */
	@RequestMapping({"employeeManage/customerManage/ope-view/viewCustomer"})
	public String viewCustomer(HttpServletRequest request,ModelMap model){
		String id = request.getParameter("id");
		
		Customer customer = this.customerService.getCustomerById(id);
		//补助信息
		Allowance allowance = this.customerService.getAllowanceByCustomerId(id);
		if(DataUtil.objIsNotNull(allowance)){
			model.addAttribute("allowance", allowance);
		}
			
		this.setData(customer, model);
		return "employee/customer/viewCustomer";
	}
	/**
	 * 
	* @Title: isExistCustomerName 
	* @Description: TODO(通过名称验证客户信息是否存在) 
	* @param @param request
	* @param @param model
	* @param @return
	* @return String
	* @throws 
	* @author duanws
	* @date 2016-7-4 下午1:47:57
	 */
	@RequestMapping(value={"employeeManage/customerManage/ope-query/isExistCustomerName"},produces={"text/plain;charset=UTF-8"})
	public @ResponseBody String isExistCustomerName(HttpServletRequest request,ModelMap model){
		String id = request.getParameter("id");
		String name = request.getParameter("name");
		boolean check = this.customerService.isExistCustomerName(id, name, null);
		if(check){
			return "true";
		}
		return "false";
	}
	/**
	 * 
	* @Title: isExistCustomerCode 
	* @Description: TODO(通过代码验证客户信息是否存在) 
	* @param @param request
	* @param @param model
	* @param @return
	* @return String
	* @throws 
	* @author duanws
	* @date 2016-7-4 下午1:51:18
	 */
	@RequestMapping(value={"employeeManage/customerManage/ope-query/isExistCustomerCode"},produces={"text/plain;charset=UTF-8"})
	public @ResponseBody String isExistCustomerCode(HttpServletRequest request,ModelMap model){
		String id = request.getParameter("id");
		String code = request.getParameter("code");
		boolean check = this.customerService.isExistCustomerCode(id, null, code);
		if(check){
			return "true";
		}
		return "false";
	}
	/**
	 * 
	* @Title: editAllowance 
	* @Description: TODO(设置客户的补助标准) 
	* @param @param request
	* @param @param model
	* @param @return
	* @return String
	* @throws 
	* @author duanws
	* @date 2016-7-11 下午3:38:54
	 */
	@RequestMapping({"employeeManage/customerManage/ope-query/editAllowance"})
	public String editAllowance(HttpServletRequest request,ModelMap model,Allowance allowance){
		String id = request.getParameter("id");
		Customer customer = this.customerService.getCustomerById(id);
		//补助信息
		allowance = this.customerService.getAllowanceByCustomerId(id);
		if(DataUtil.objIsNotNull(allowance)){
			model.addAttribute("allowance", allowance);
		}else{
			model.addAttribute("allowance", new Allowance());
		}
		
		this.setData(customer, model);
		
		return "employee/customer/editAllowance";
	}
	/**
	 * 
	* @Title: saveAllowance 
	* @Description: TODO(保存客户补助信息) 
	* @param @param request
	* @param @param model
	* @param @return
	* @return String
	* @throws 
	* @author duanws
	* @date 2016-7-11 下午3:49:39
	 */
	@RequestMapping({"employeeManage/customerManage/ope-save/saveAllowance"})
	public String saveAllowance(HttpServletRequest request,ModelMap model,Allowance allowance){
		String id = allowance.getId();
		if(DataUtil.strIsNotNull(id)){
			this.customerService.updateAllowance(allowance);
		}else{
			this.customerService.saveAllowance(allowance);
		}
		
		return "redirect:/employeeManage/customerManage/ope-query/queryCustomer.do?flag=1";
	}
	
	/**
	 * 
	* @Title: setData 
	* @Description: TODO(封闭页面显示元素) 
	* @param @param customer
	* @param @param model
	* @return void
	* @throws 
	* @author duanws
	* @date 2016-6-30 下午2:51:45
	 */
	private void setData(Customer customer,ModelMap model){
		//是否有餐补
		List<BaseDic> dicListIsMeal = this.dicService.getDicListByCode("Y_N");
		
		model.addAttribute("dicListIsMeal", dicListIsMeal);
		model.addAttribute("customer", customer);
	}

}
