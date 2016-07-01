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
	@RequestMapping({"employeeManage/customerManage/ope-add/editCustomer","employeeManage/customerManage/ope-update/editCustomer"})
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
