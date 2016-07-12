package com.jsg.employee.model;

import java.io.Serializable;

import com.jsg.base.model.BaseModel;


/**
 * 
* @ClassName: SalaryResult 
* @Description: TODO(薪资合算结果) 
* @author duanws
* @date 2016-7-16 上午9:56:37 
*
 */
public class SalaryResult extends BaseModel implements Serializable{

	/** 
	* @Fields serialVersionUID : TODO() 
	*/ 
	private static final long serialVersionUID = 8000041063115933942L;
	//员工
	private Employee employee;
	//薪资年月
	private String salaryDate;
	//缺勤天数
	private String absenceDay;
	//缺勤扣款
	private String absenceMoney;
	//加班费及补助
	private String overTimeMoney;
	//出勤工资
	private String attendanceMoney;
	//转正工资调整
	private String promotion;
	//奖金
	private String reward;
	//岗位津贴
	private String allowance;
	//餐补
	private String mealSupplement;
	//电脑补助
	private String computerSupplement;
	//上月调整
	private String adjustment;
	//离职补偿金
	private String compensate;
	//工资及补偿总额
	private String sum;
	//社保
	private String socialSecurity;
	//公积金
	private String fundMoney;
	//罚款
	private String punish;
	//代扣款
	private String other;
	//个税
	private String tax;
	//实发工资
	private String finnalMoney;
	//剩余年假数
	private int haveAnnualLeave;

	public Employee getEmployee() {
		return employee;
	}
	public void setEmployee(Employee employee) {
		this.employee = employee;
	}
	public String getSalaryDate() {
		return salaryDate;
	}
	public void setSalaryDate(String salaryDate) {
		this.salaryDate = salaryDate;
	}
	public String getAbsenceDay() {
		return absenceDay;
	}
	public void setAbsenceDay(String absenceDay) {
		this.absenceDay = absenceDay;
	}
	public String getAbsenceMoney() {
		return absenceMoney;
	}
	public void setAbsenceMoney(String absenceMoney) {
		this.absenceMoney = absenceMoney;
	}
	public String getOverTimeMoney() {
		return overTimeMoney;
	}
	public void setOverTimeMoney(String overTimeMoney) {
		this.overTimeMoney = overTimeMoney;
	}
	public String getAttendanceMoney() {
		return attendanceMoney;
	}
	public void setAttendanceMoney(String attendanceMoney) {
		this.attendanceMoney = attendanceMoney;
	}
	public String getPromotion() {
		return promotion;
	}
	public void setPromotion(String promotion) {
		this.promotion = promotion;
	}
	public String getReward() {
		return reward;
	}
	public void setReward(String reward) {
		this.reward = reward;
	}
	public String getAllowance() {
		return allowance;
	}
	public void setAllowance(String allowance) {
		this.allowance = allowance;
	}
	public String getMealSupplement() {
		return mealSupplement;
	}
	public void setMealSupplement(String mealSupplement) {
		this.mealSupplement = mealSupplement;
	}
	public String getComputerSupplement() {
		return computerSupplement;
	}
	public void setComputerSupplement(String computerSupplement) {
		this.computerSupplement = computerSupplement;
	}
	public String getAdjustment() {
		return adjustment;
	}
	public void setAdjustment(String adjustment) {
		this.adjustment = adjustment;
	}
	public String getCompensate() {
		return compensate;
	}
	public void setCompensate(String compensate) {
		this.compensate = compensate;
	}
	public String getSum() {
		return sum;
	}
	public void setSum(String sum) {
		this.sum = sum;
	}
	public String getSocialSecurity() {
		return socialSecurity;
	}
	public void setSocialSecurity(String socialSecurity) {
		this.socialSecurity = socialSecurity;
	}
	public String getFundMoney() {
		return fundMoney;
	}
	public void setFundMoney(String fundMoney) {
		this.fundMoney = fundMoney;
	}
	public String getPunish() {
		return punish;
	}
	public void setPunish(String punish) {
		this.punish = punish;
	}
	public String getOther() {
		return other;
	}
	public void setOther(String other) {
		this.other = other;
	}
	public String getTax() {
		return tax;
	}
	public void setTax(String tax) {
		this.tax = tax;
	}
	public String getFinnalMoney() {
		return finnalMoney;
	}
	public void setFinnalMoney(String finnalMoney) {
		this.finnalMoney = finnalMoney;
	}

	

}
