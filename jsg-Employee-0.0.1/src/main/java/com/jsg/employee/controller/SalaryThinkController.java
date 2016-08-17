package com.jsg.employee.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jsg.base.controller.BaseController;
import com.jsg.employee.model.SalaryResult;
import com.jsg.employee.service.ISalaryThinkService;
/**
 * 
* @ClassName: SalaryThinkController 
* @Description: TODO(员工薪资分析) 
* @author duanws
* @date 2016-8-17 上午11:12:56 
*
 */

@Controller
public class SalaryThinkController extends BaseController {

	@Autowired
	private ISalaryThinkService salaryThinkService;
	/**
	 * 
	* @Title: querySalaryThink 
	* @Description: TODO(跳转到图表显示页面) 
	* @param @param request
	* @param @param model
	* @param @return
	* @return String
	* @throws 
	* @author duanws
	* @date 2016-8-17 下午1:53:37
	 */
	@RequestMapping({"employeeManage/salaryThinkManage/querySalaryThinkManage"})
	public String querySalaryThink(HttpServletRequest request,ModelMap model){
		
		return "employee/salaryThink/querySalaryThink";
	}
	/**
	 * 
	* @Title: getJsonDate 
	* @Description: TODO(获得json数据) 
	* @param @param request
	* @param @return
	* @return String
	* @throws 
	* @author duanws
	* @date 2016-8-17 下午2:46:20
	 */
	@RequestMapping(value={"employeeManage/salaryThinkManage/getSalaryJsonData"},produces={"text/plain"})
	public @ResponseBody String getJsonDate(HttpServletRequest request){
		String salaryDate = request.getParameter("salaryDate");
		List<Object[]> list =	this.salaryThinkService.getSalarySum(salaryDate);
		Map map = new HashMap();
		//拼接用于图表显示的数据
		StringBuilder total = new StringBuilder("");
		//列标题
		StringBuilder title = new StringBuilder("");
		//值
		StringBuilder val = new StringBuilder("");
		for(Object[] obj:list){
			title.append(obj[1]+",");
			val.append(obj[0]+",");
		}

		total.append(title+"."+val);
		return total.toString();
	}
	
	
	
}
