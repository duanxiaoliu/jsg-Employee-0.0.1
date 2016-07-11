package com.jsg.employee.model;

import java.io.Serializable;

import com.jsg.base.model.BaseModel;
/**
 * 
* @ClassName: Allowance 
* @Description: TODO(客户补助信息) 
* @author duanws
* @date 2016-7-11 下午2:36:32 
*
 */
public class Allowance extends BaseModel implements Serializable{

	private static final long serialVersionUID = 1L;
	//饭补补助
	private String food;
	//电脑补助
	private String computer;
	//交通补助
	private String traffic;
	//住房
	private String housing;
	//其它补助`天
	private String otherDay;
	//其它补助`月
	private String otherMouth;
	//客户
	private Customer customer;
	
	public Customer getCustomer() {
		return customer;
	}
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	public String getFood() {
		return food;
	}
	public void setFood(String food) {
		this.food = food;
	}
	public String getComputer() {
		return computer;
	}
	public void setComputer(String computer) {
		this.computer = computer;
	}
	public String getTraffic() {
		return traffic;
	}
	public void setTraffic(String traffic) {
		this.traffic = traffic;
	}
	public String getHousing() {
		return housing;
	}
	public void setHousing(String housing) {
		this.housing = housing;
	}
	public String getOtherDay() {
		return otherDay;
	}
	public void setOtherDay(String otherDay) {
		this.otherDay = otherDay;
	}
	public String getOtherMouth() {
		return otherMouth;
	}
	public void setOtherMouth(String otherMouth) {
		this.otherMouth = otherMouth;
	}
	
	

}
