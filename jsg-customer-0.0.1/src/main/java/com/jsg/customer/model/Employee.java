package com.jsg.customer.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.jsg.base.model.BaseDic;
import com.jsg.base.model.BaseModel;
import com.jsg.base.model.UserInfo;
/**
 * 
* @ClassName: Employee 
* @Description: TODO(员工信息) 
* @author duanws
* @date 2016-6-28 下午2:02:35 
*
 */
public class Employee extends BaseModel implements Serializable{

	/** 
	* @Fields serialVersionUID : TODO() 
	*/ 
	private static final long serialVersionUID = 1L;
	//员工姓名
	private String employeeName;
	//员工编号
	private String employeeCode;
	//员工所属部门
	private BaseDic department;
	//员工所在客户现场
	private Customer customer;
	//邮箱
	private String email;
	//试用期工资
	private BigDecimal probation;
	//基本工资
	private BigDecimal salary;
	//试用期是否全薪
	private BaseDic probationState;
	//是否在职
	private BaseDic isJob;
	//入职时间
	private Date entryTime;
	//离职时间
	private Date quitTime;
	//户口性质
	private BaseDic register;
	//创建人
	private UserInfo creator;
	//转正状态
	private BaseDic isFullMember;
	
	
	public String getEmployeeName() {
		return employeeName;
	}
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}
	public String getEmployeeCode() {
		return employeeCode;
	}
	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
	}
	public BaseDic getDepartment() {
		return department;
	}
	public void setDepartment(BaseDic department) {
		this.department = department;
	}
	public Customer getCustomer() {
		return customer;
	}
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public BigDecimal getProbation() {
		return probation;
	}
	public void setProbation(BigDecimal probation) {
		this.probation = probation;
	}
	public BigDecimal getSalary() {
		return salary;
	}
	public void setSalary(BigDecimal salary) {
		this.salary = salary;
	}
	public BaseDic getProbationState() {
		return probationState;
	}
	public void setProbationState(BaseDic probationState) {
		this.probationState = probationState;
	}
	public BaseDic getIsJob() {
		return isJob;
	}
	public void setIsJob(BaseDic isJob) {
		this.isJob = isJob;
	}
	public Date getEntryTime() {
		return entryTime;
	}
	public void setEntryTime(Date entryTime) {
		this.entryTime = entryTime;
	}
	public Date getQuitTime() {
		return quitTime;
	}
	public void setQuitTime(Date quitTime) {
		this.quitTime = quitTime;
	}
	public BaseDic getRegister() {
		return register;
	}
	public void setRegister(BaseDic register) {
		this.register = register;
	}
	public UserInfo getCreator() {
		return creator;
	}
	public void setCreator(UserInfo creator) {
		this.creator = creator;
	}
	public BaseDic getIsFullMember() {
		return isFullMember;
	}
	public void setIsFullMember(BaseDic isFullMember) {
		this.isFullMember = isFullMember;
	}
	
}
