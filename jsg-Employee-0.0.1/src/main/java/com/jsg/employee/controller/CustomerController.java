package com.jsg.employee.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.jsg.base.controller.BaseController;
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

}
