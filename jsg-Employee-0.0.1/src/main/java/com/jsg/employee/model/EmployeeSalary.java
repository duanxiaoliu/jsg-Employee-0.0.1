package com.jsg.employee.model;

import java.io.Serializable;
import java.util.Date;

import javax.xml.crypto.Data;

import com.jsg.base.model.BaseDic;
import com.jsg.base.model.BaseModel;

/**
 * 
* @ClassName: EmployeeSalary 
* @Description: TODO(员工薪资表) 
* @author duanws
* @date 2016-7-16 上午9:34:35 
*
 */
public class EmployeeSalary extends BaseModel implements Serializable{

	/** 
	* @Fields serialVersionUID : TODO() 
	*/ 
	private static final long serialVersionUID = 7391267775740916255L;
	//员工
	private Employee employee;
	//薪资年月
	private String salaryDate;
	//薪资年月查询使用
	private String QsalaryDate;
	//上月调整
	private String adjustment;
	//岗位津贴
	private String allowance;
	//其它代扣款
	private String other;
	//奖金内容
	private String rewardItems;
	//奖金金额
	private String rewardAmount;
	//奖金时间
	private String rewardTime;
	//离职补贴金
	private String resignMoney;
	//罚款
	private String punish;
	//是否全勤
	private BaseDic isFullTimeDic;
	//是否全勤处理数据字典
	private String isFullTime;
	//应出勤天数
	private int fullTime;
	//加班小时数
	private String overTime;
	//是否有加班费
	private BaseDic isFixedDic;
	//是否固定加班费处理数据字典
	private String isFixed;
	//是否公积金自缴
	private BaseDic isSelfDic;
	//是否公积金自缴处理数据字典
	private String isSelf;
	//公积金缴纳金额
	private String fundMoney;
	//是否有电脑补助
	private BaseDic isComputerDic;
	//是否电脑补助处理数据字典
	private String isComputer;
	//试用期事假小时数
	private int tryPersonalLeave;
	//事假小时数
	private int personalLeave;
	//事假说明
	private String personalRemark;
	//年假天数
	private int annualLeave;
	//年假说明
	private String annualRemark;
	//转正病假天数
	private int sickLeave;
	//试用期病假天数
	private int trySickLeave;
	//病假说明
	private String sickRemark;
	//婚假天数
	private int marriageLeave;
	//婚假说明
	private String marriageRemark;
	//产假天数
	private int maternityLeave;
	//产假说明
	private String maternityRemark;
	//丧假天数
	private int funeralLeave;
	//丧假说明
	private String funeralRemark;

	
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
		this.salaryDate = salaryDate.replace("[object HTMLInputElement],", "");
	}
	public String getQsalaryDate() {
		return QsalaryDate;
	}
	public void setQsalaryDate(String qsalaryDate) {
		QsalaryDate = qsalaryDate;
	}
	public String getAdjustment() {
		return adjustment;
	}
	public void setAdjustment(String adjustment) {
		this.adjustment = adjustment;
	}
	public String getAllowance() {
		return allowance;
	}
	public void setAllowance(String allowance) {
		this.allowance = allowance;
	}
	public String getOther() {
		return other;
	}
	public void setOther(String other) {
		this.other = other;
	}
	public String getRewardItems() {
		return rewardItems;
	}
	public void setRewardItems(String rewardItems) {
		this.rewardItems = rewardItems;
	}
	public String getRewardAmount() {
		return rewardAmount;
	}
	public void setRewardAmount(String rewardAmount) {
		this.rewardAmount = rewardAmount;
	}
	public String getRewardTime() {
		return rewardTime;
	}
	public void setRewardTime(String rewardTime) {
		this.rewardTime = rewardTime;
	}
	public String getResignMoney() {
		return resignMoney;
	}
	public void setResignMoney(String resignMoney) {
		this.resignMoney = resignMoney;
	}
	public String getPunish() {
		return punish;
	}
	public void setPunish(String punish) {
		this.punish = punish;
	}



	public int getFullTime() {
		return fullTime;
	}
	public void setFullTime(int fullTime) {
		this.fullTime = fullTime;
	}
	public String getOverTime() {
		return overTime;
	}
	public void setOverTime(String overTime) {
		this.overTime = overTime;
	}

	public String getFundMoney() {
		return fundMoney;
	}
	public void setFundMoney(String fundMoney) {
		this.fundMoney = fundMoney;
	}

	public BaseDic getIsFullTimeDic() {
		return isFullTimeDic;
	}
	public void setIsFullTimeDic(BaseDic isFullTimeDic) {
		this.isFullTimeDic = isFullTimeDic;
	}
	public BaseDic getIsFixedDic() {
		return isFixedDic;
	}
	public void setIsFixedDic(BaseDic isFixedDic) {
		this.isFixedDic = isFixedDic;
	}
	public BaseDic getIsSelfDic() {
		return isSelfDic;
	}
	public void setIsSelfDic(BaseDic isSelfDic) {
		this.isSelfDic = isSelfDic;
	}
	public BaseDic getIsComputerDic() {
		return isComputerDic;
	}
	public void setIsComputerDic(BaseDic isComputerDic) {
		this.isComputerDic = isComputerDic;
	}
	public int getTryPersonalLeave() {
		return tryPersonalLeave;
	}
	public void setTryPersonalLeave(int tryPersonalLeave) {
		this.tryPersonalLeave = tryPersonalLeave;
	}
	public int getPersonalLeave() {
		return personalLeave;
	}
	public void setPersonalLeave(int personalLeave) {
		this.personalLeave = personalLeave;
	}
	public String getPersonalRemark() {
		return personalRemark;
	}
	public void setPersonalRemark(String personalRemark) {
		this.personalRemark = personalRemark;
	}
	public int getAnnualLeave() {
		return annualLeave;
	}
	public void setAnnualLeave(int annualLeave) {
		this.annualLeave = annualLeave;
	}
	public String getAnnualRemark() {
		return annualRemark;
	}
	public void setAnnualRemark(String annualRemark) {
		this.annualRemark = annualRemark;
	}
	public int getSickLeave() {
		return sickLeave;
	}
	public void setSickLeave(int sickLeave) {
		this.sickLeave = sickLeave;
	}
	public int getTrySickLeave() {
		return trySickLeave;
	}
	public void setTrySickLeave(int trySickLeave) {
		this.trySickLeave = trySickLeave;
	}
	public String getSickRemark() {
		return sickRemark;
	}
	public void setSickRemark(String sickRemark) {
		this.sickRemark = sickRemark;
	}
	public int getMarriageLeave() {
		return marriageLeave;
	}
	public void setMarriageLeave(int marriageLeave) {
		this.marriageLeave = marriageLeave;
	}
	public String getMarriageRemark() {
		return marriageRemark;
	}
	public void setMarriageRemark(String marriageRemark) {
		this.marriageRemark = marriageRemark;
	}
	public int getMaternityLeave() {
		return maternityLeave;
	}
	public void setMaternityLeave(int maternityLeave) {
		this.maternityLeave = maternityLeave;
	}
	public String getMaternityRemark() {
		return maternityRemark;
	}
	public void setMaternityRemark(String maternityRemark) {
		this.maternityRemark = maternityRemark;
	}
	public int getFuneralLeave() {
		return funeralLeave;
	}
	public void setFuneralLeave(int funeralLeave) {
		this.funeralLeave = funeralLeave;
	}
	public String getFuneralRemark() {
		return funeralRemark;
	}
	public void setFuneralRemark(String funeralRemark) {
		this.funeralRemark = funeralRemark;
	}

	public String getIsFullTime() {
		return isFullTime;
	}
	public void setIsFullTime(String isFullTime) {
		this.isFullTime = isFullTime;
	}
	public String getIsFixed() {
		return isFixed;
	}
	public void setIsFixed(String isFixed) {
		this.isFixed = isFixed;
	}
	public String getIsSelf() {
		return isSelf;
	}
	public void setIsSelf(String isSelf) {
		this.isSelf = isSelf;
	}
	public String getIsComputer() {
		return isComputer;
	}
	public void setIsComputer(String isComputer) {
		this.isComputer = isComputer;
	}
}
