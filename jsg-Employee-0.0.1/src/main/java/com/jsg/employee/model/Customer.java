package com.jsg.employee.model;

import java.io.Serializable;
import java.math.BigDecimal;

import com.jsg.base.model.BaseDic;
import com.jsg.base.model.BaseModel;

public class Customer extends BaseModel implements Serializable {

	/** 
	* @Fields serialVersionUID : TODO() 
	*/ 
	private static final long serialVersionUID = 1L;
	//客户名称
	private String name;
	//客户编号
	private String code;
	//加班费
	private BigDecimal overTimeMoney;
	//是否有餐补
	private BaseDic isMeal;
	//月平均工作日天数
	private BigDecimal workDays;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public BigDecimal getOverTimeMoney() {
		return overTimeMoney;
	}
	public void setOverTimeMoney(BigDecimal overTimeMoney) {
		this.overTimeMoney = overTimeMoney;
	}
	public BaseDic getIsMeal() {
		return isMeal;
	}
	public void setIsMeal(BaseDic isMeal) {
		this.isMeal = isMeal;
	}
	public BigDecimal getWorkDays() {
		return workDays;
	}
	public void setWorkDays(BigDecimal workDays) {
		this.workDays = workDays;
	}

}
