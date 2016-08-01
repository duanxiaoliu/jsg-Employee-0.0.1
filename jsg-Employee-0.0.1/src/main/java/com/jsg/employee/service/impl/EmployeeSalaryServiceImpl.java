package com.jsg.employee.service.impl;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jsg.base.model.BasePage;
import com.jsg.base.util.DataUtil;
import com.jsg.employee.dao.ICustomerDao;
import com.jsg.employee.dao.IEmployeeDao;
import com.jsg.employee.dao.IEmployeeSalaryDao;
import com.jsg.employee.model.Allowance;
import com.jsg.employee.model.Customer;
import com.jsg.employee.model.Employee;
import com.jsg.employee.model.EmployeeSalary;
import com.jsg.employee.model.SalaryResult;
import com.jsg.employee.service.IEmployeeSalaryService;
import com.jsg.employee.util.SalaryUtils;
import com.mysql.fabric.xmlrpc.base.Array;
/**
 * 
* @ClassName: EmployeeSalaryServiceImpl 
* @Description: TODO(员工薪资管理) 
* @author duanws
* @date 2016-7-12 下午3:18:54 
*
 */
@Service("employeeSalaryService")
public class EmployeeSalaryServiceImpl implements IEmployeeSalaryService {

	@Autowired
	private IEmployeeSalaryDao employeeSalaryDao;
	@Autowired
	private IEmployeeDao employeeDao;
	@Autowired
	private ICustomerDao customerDao;
	//所在客户月平均工作天数
	private double workDays = 21.75;
	private SalaryUtils salaryUtils = new SalaryUtils();
	//转正工资调整额
	double promotion = 0.00;
	SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");

	@Override
	public BasePage queryEmployeeSalary(int pageNo, int pageSize,
			EmployeeSalary employeeSalary) {
		return this.employeeSalaryDao.queryEmployeeSalary(pageNo, pageSize, employeeSalary);
	}

	@Override
	public void delEmployeeSalaryById(String id,String salaryDate) {
		this.employeeSalaryDao.delEmployeeSalaryById(id,salaryDate);
		
	}

	@Override
	public BasePage querySalaryResult(int pageNo, int pageSize,
			SalaryResult salaryResult) {
		
		return this.employeeSalaryDao.querySalaryResult(pageNo, pageSize, salaryResult);
	}

	@Override
	public EmployeeSalary getEmployeeSalaryBySalaryDate(String employeeId,
			String salaryDate) {
		return this.employeeSalaryDao.getEmployeeSalaryBySalaryDate(employeeId, salaryDate);
	}

	@Override
	public SalaryResult getSalaryResultBySalaryDate(String employeeId,
			String salaryDate) {
		return this.employeeSalaryDao.getSalaryResultBySalaryDate(employeeId, salaryDate);
	}

	@Override
	public SalaryResult computeSalary(EmployeeSalary employeeSalary){

		Employee employee = (Employee) this.employeeDao.get(Employee.class, employeeSalary.getEmployee().getId());
		//根据客户管理调整月平均工作天数
		workDays = Double.parseDouble(employee.getCustomer().getWorkDays());
		String employeeSalaryId = employeeSalary.getId();
		EmployeeSalary employeeSalaryE = this.employeeSalaryDao.getEmployeeSalaryBySalaryDate(employee.getId(), employeeSalary.getSalaryDate());
		if(DataUtil.objIsNotNull(employeeSalaryE)){
			employeeSalary.setId(employeeSalaryE.getId());
			this.employeeSalaryDao.evit(employeeSalaryE);
			this.employeeSalaryDao.update(employeeSalary);
		}else{
			//新增
			//创建时间
			employeeSalary.setCreateTime(new Date());
			this.employeeSalaryDao.save(employeeSalary);
			employeeSalaryId = employeeSalary.getId();
		}
		SalaryResult salaryResult = this.employeeSalaryDao.getSalaryResultBySalaryDate(employee.getId(), employeeSalary.getSalaryDate());
		if(DataUtil.objIsNotNull(salaryResult)){
			try {
				salaryResult = getSalaryResult(employeeSalary,employee,salaryResult);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.employeeSalaryDao.update(salaryResult);
		}else{
			try {
				salaryResult = getSalaryResult(employeeSalary,employee,new SalaryResult());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			salaryResult.setCreateTime(new Date());
			this.employeeSalaryDao.save(salaryResult);
		}		
		return salaryResult;
	}
	//----------------------工资合算------------START----------------------------------------------------------------
		//合算工资
		private SalaryResult getSalaryResult(EmployeeSalary employeeSalary,Employee employee,SalaryResult salaryResult) throws Exception{
			//员工所在客户
			Customer customer = employee.getCustomer();
			//获得客户的补助标准
			Allowance Callowance = this.customerDao.getAllowanceByCustomerId(customer.getId());
			//是否补助
			String isAllowance = customer.getIsMeal().getCode();
			//工资及补偿总额
			double sumMoney = 0.00;
			//入职日期天
			int eDays = Integer.parseInt(employee.getEntryTime().toString().substring(8,10));
			//是否转正
			String isFullMemberStr = SalaryUtils.getFullMember(employee, employeeSalary.getSalaryDate());
			//是否在职
			String isJobCode = employee.getIsJob().getCode();
			//基本工资
			double baseSalary = this.getBaseSalary(employeeSalary,employee);
			//员工
			salaryResult.setEmployee(employee);
			//薪资年月
			salaryResult.setSalaryDate(employeeSalary.getSalaryDate());
			//缺勤天数
			salaryResult.setAbsenceDay(String.valueOf(this.getAbsenceDay(employeeSalary)));
			//缺勤扣款
			salaryResult.setAbsenceMoney(String.valueOf(this.getAbsenceMoney(employeeSalary, employee)));
			//出勤工资--基本工资减去缺勤扣款
			salaryResult.setAttendanceMoney(String.valueOf(baseSalary- this.getAbsenceMoney(employeeSalary, employee)));
			//转正工资调整
			salaryResult.setPromotion(String.valueOf(promotion));
			//奖金
			double reward = (this.employeeSalaryDao.getRwardByDate(employee.getId(), employeeSalary.getSalaryDate())!=null)?Double.parseDouble(this.employeeSalaryDao.getRwardByDate(employee.getId(), employeeSalary.getSalaryDate()).getRewardAmount()):0.00;
			salaryResult.setReward(String.valueOf(reward));
			//岗位津贴
			String allowance = employeeSalary.getAllowance()!=null&&employeeSalary.getAllowance()!=""?employeeSalary.getAllowance():"0";
			salaryResult.setAllowance(allowance);
			//上月调整
			salaryResult.setAdjustment(employeeSalary.getAdjustment()!=null?employeeSalary.getAdjustment():"0");
			//离职补偿金
			salaryResult.setCompensate(employeeSalary.getResignMoney()!=null?employeeSalary.getResignMoney():"0");
			//判断是否有补助
			if("NO".equals(isAllowance)){
				//加班费及补助
				salaryResult.setOverTimeMoney(String.valueOf(this.getOverTimeMoney(employeeSalary, employee)));
				//餐补
				salaryResult.setMealSupplement("0.00");
				//电脑补助
				salaryResult.setComputerSupplement("0.00");
				//工资及补偿总额
				sumMoney =  baseSalary - this.getAbsenceMoney(employeeSalary, employee) + reward + Double.parseDouble(allowance) + employeeSalary.getAdjustment()!=null?Double.parseDouble(employeeSalary.getAdjustment()):0.00 + employeeSalary.getResignMoney()!=null?Double.parseDouble(employeeSalary.getResignMoney()):0.00 + promotion + this.getOverTimeMoney(employeeSalary, employee);
				
			}else{
				//加班费及补助
				salaryResult.setOverTimeMoney(String.valueOf(this.getOverTimeMoney(employeeSalary, employee) + this.getMealSupplement(employeeSalary, employee,Callowance) + this.getComputerSupplement(employeeSalary, employee,Callowance) ));
				//电脑补助
				salaryResult.setComputerSupplement(String.valueOf(this.getComputerSupplement(employeeSalary, employee,Callowance)));
				//餐补
				salaryResult.setMealSupplement(String.valueOf(this.getMealSupplement(employeeSalary, employee,Callowance)));
				//上月调整
				double adjustment = employeeSalary.getAdjustment()!=""?Double.parseDouble(employeeSalary.getAdjustment()):0.00;
				//离职补贴金
				double resignmoney = employeeSalary.getResignMoney()!=""?Double.parseDouble(employeeSalary.getResignMoney()):0.00;
				//补助、月
				double housing = Callowance.getHousing()!=""?Double.parseDouble(Callowance.getHousing()):0.00;
				double otherMouth = Callowance.getOtherMouth()!=""?Double.parseDouble(Callowance.getOtherMouth()):0.00;
				//工资及补偿总额
				sumMoney =  baseSalary + 
						reward + 
						Double.parseDouble(allowance) + 
						this.getMealSupplement(employeeSalary, employee,Callowance) + 
						this.getComputerSupplement(employeeSalary, employee,Callowance) + 
						this.getTraffic(employeeSalary, employee, Callowance) + 
						this.getOtherDay(employeeSalary, employee, Callowance) + 
						housing + otherMouth +
						
						promotion + adjustment + resignmoney+
						this.getOverTimeMoney(employeeSalary, employee)- 
						this.getAbsenceMoney(employeeSalary, employee);
			}
			salaryResult.setSum(String.valueOf(sumMoney));
			//社保
			
			double socialSecurity = 0.00;
			if("未转正".equals(isFullMemberStr)){
				socialSecurity = 0.00;
			}else if("当月转正".equals(isFullMemberStr)){
				if(eDays<15){
					socialSecurity = this.getSocialSecurity(employeeSalary, employee);
				}else{
					socialSecurity = 0.00;
				}
			}else if("N_JOB".equals(isJobCode)){
				String quitDay = employee.getQuitTime().toString().substring(8, 10);
				if(Integer.parseInt(quitDay)<15){
					socialSecurity = 0.00;
				}else{
					socialSecurity = this.getSocialSecurity(employeeSalary, employee);
				}
			}else{
				socialSecurity = this.getSocialSecurity(employeeSalary, employee);
			}
			salaryResult.setSocialSecurity(String.valueOf(socialSecurity));
			//公积金
			double fundMoney = 0.00;
			if("未转正".equals(isFullMemberStr)){
				if("Y".equals(employeeSalary.getIsSelfDic().getCode())){
					fundMoney = (employeeSalary.getFundMoney()!=null)?Double.parseDouble(employeeSalary.getFundMoney()):0.00;
				}else{
					fundMoney = 0.00;
				}
			}else if("当月转正".equals(isFullMemberStr)){
				if(eDays<10){
					fundMoney = Double.parseDouble(employeeSalary.getFundMoney());
				}else{
					if("Y".equals(employeeSalary.getIsSelfDic().getCode())){
						fundMoney = (employeeSalary.getFundMoney()!=null)?Double.parseDouble(employeeSalary.getFundMoney()):0.00;
					}else{
						fundMoney = 0.00;
					}
				}
			}else if("N_JOB".equals(isJobCode)){
				String quitDay = employee.getQuitTime().toString().substring(8, 10);
				if(Integer.parseInt(quitDay)<10){
					fundMoney = 0.00;
				}else{
					fundMoney = (employeeSalary.getFundMoney()!=null)?Double.parseDouble(employeeSalary.getFundMoney()):0.00;
				}
			}else{
				fundMoney = (employeeSalary.getFundMoney()!=null)?Double.parseDouble(employeeSalary.getFundMoney()):0.00;
			}
			salaryResult.setFundMoney(String.valueOf(fundMoney));
			//罚款
			salaryResult.setPunish((employeeSalary.getPunish()!=null)?employeeSalary.getPunish():"0");
			//代扣款
			salaryResult.setOther((employeeSalary.getOther()!=null)?employeeSalary.getOther():"0");
			double debitMoney = socialSecurity + fundMoney + Double.parseDouble(salaryResult.getPunish()!=""?salaryResult.getPunish():"0.00") + Double.parseDouble(salaryResult.getOther()!=""?salaryResult.getOther():"0.00");
			double totalSalary = sumMoney - debitMoney;
			//个税
			double tax = this.getTax(employeeSalary, employee, totalSalary);
			salaryResult.setTax(String.valueOf(tax));
			//实发工资
			double finnalMoney = totalSalary - tax;
			salaryResult.setFinnalMoney(String.valueOf(finnalMoney));
			
			return salaryResult;
		}
		/**
		 * 
		* @Title: getBaseSalary 
		* @Description: TODO(获取基本工资，处理试用期工资等) 
		* @param @param employeeSalary
		* @param @param employee
		* @param @return
		* @param @throws Exception
		* @return double
		* @throws 
		* @author duanws
		* @date 2016-7-19 上午11:03:27
		 */
		private double getBaseSalary(EmployeeSalary employeeSalary,Employee employee) throws Exception{
			
			//是否转正
			String isFullMemberStr = this.salaryUtils.getFullMember(employee, employeeSalary.getSalaryDate());
			//试用期是否全薪
			String probationStateStr = employee.getProbationState().getCode();
			//获得基本工资
			double baseSalary= Double.parseDouble(employee.getSalary());
			
			if("未转正".equals(isFullMemberStr)){
				if("NO".equals(probationStateStr)){
					//试用期不是全额工资，乘80%
					baseSalary = baseSalary * 0.8 ;
				}
			}else if("当月转正".equals(isFullMemberStr)){
				//当月转正
				if("NO".equals(probationStateStr)){
					//试用期不是全额工资，乘80%
					baseSalary = baseSalary * 0.8;
					String salaryDate = employeeSalary.getSalaryDate();
					
					//获得当月天数
					Calendar days = this.getDays(Integer.parseInt(salaryDate.substring(0, 4)), Integer.parseInt(salaryDate.substring(5, 7)));
					//获得当月第一天
					Calendar dayOne = Calendar.getInstance();
					dayOne.set(Calendar.YEAR, Integer.parseInt(salaryDate.substring(0, 4)));
					dayOne.set(Calendar.MONTH, Integer.parseInt(salaryDate.substring(5, 7)));
					dayOne.set(Calendar.DATE, 1);
					
					//转正日期
					String entryTimeStr = employee.getEntryTime();
					Date EDay = sdf.parse(entryTimeStr);
					Calendar ca = Calendar.getInstance();
					ca.setTime(EDay);
					ca.add(Calendar.MONTH, 3);
					//当月工作日
					int wDay = salaryUtils.getWorkingDay(dayOne, days);
					//转正天数校正
					double zDay = salaryUtils.getWorkingDay(ca, days) * workDays / wDay;
					
					//转正工资调整额
					promotion =  baseSalary * 0.8 * (workDays-zDay) /workDays + (baseSalary * zDay / workDays - baseSalary * 0.8);
						
				}
			}
			//其它情况为全额工资
			return baseSalary;
		}
		
		//根据年和月得到天数
		private Calendar getDays(int year,int month){
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.YEAR,year);
			cal.set(Calendar.MONTH, month);//Java月份才0开始算
			int dateOfMonth = cal.getActualMaximum(Calendar.DATE); 
			cal.set(Calendar.DATE,dateOfMonth);
			return cal;
		}
		/**
		 * 
		* @Title: getMealSupplement 
		* @Description: TODO(获取餐补) 
		* @param @param employeeSalary
		* @param @param employee
		* @param @return
		* @param @throws Exception
		* @return double
		* @throws 
		* @author duanws
		* @date 2016-7-19 上午11:03:00
		 */
		private double getMealSupplement(EmployeeSalary employeeSalary,Employee employee,Allowance allowance) throws Exception{
			double mealSupplement = 0.00;
			double mealDay = 0.00;
			if(allowance.getFood()!=null || allowance.getFood()=="0" || "0".equals(allowance.getFood())){
				return 0.00;
			}else{
				mealDay = Double.parseDouble(allowance.getFood());
			}
			//是否转正
			String isFullMemberStr = this.salaryUtils.getFullMember(employee, employeeSalary.getSalaryDate());
			//是否在职
			String isJobCode = employee.getIsJob().getCode();
			String salaryDate = employeeSalary.getSalaryDate();
			//获得当月天数
			Calendar days = this.getDays(Integer.parseInt(salaryDate.substring(0, 4)), Integer.parseInt(salaryDate.substring(5, 7)));
			//获得当月第一天
			Calendar dayOne = Calendar.getInstance();
			dayOne.set(Calendar.YEAR, Integer.parseInt(salaryDate.substring(0, 4)));
			dayOne.set(Calendar.MONTH, Integer.parseInt(salaryDate.substring(5, 7)));
			dayOne.set(Calendar.DATE, 1);
			//当月工作日
			int wDay = salaryUtils.getWorkingDay(dayOne, days);
			//入职日期天
			int eDays = Integer.parseInt(employee.getEntryTime().toString().substring(8,10));
			//入职日期
			Calendar eCDays = Calendar.getInstance();
			String entryTimeStr = employee.getEntryTime();
			eCDays.setTime(sdf.parse(entryTimeStr));
			//员工入职当月实际出勤天数
			int realDay = salaryUtils.getWorkingDay(eCDays, days);
			//是否全勤
			String isFullTime = employeeSalary.getIsFullTimeDic().getCode();
			//应出勤天数
			int fullTime = employeeSalary.getFullTime();
			//出勤天数减去各种假
			double attendanceTime = getAttendanceTime(employeeSalary);
			//员工所在客户
			String isMealCode = employee.getCustomer().getIsMeal().getCode();
			if("Y_MEAL".equals(isMealCode)){
				if("Y".equals(isFullTime)){
					mealSupplement = attendanceTime * mealDay ;
					
				}else{
					//当月转正状态
					 if("当月转正".equals(isFullMemberStr)){
						//入职
						if(eDays<=15){
							mealSupplement = mealDay * ((double)realDay-(Math.floor(((double)employeeSalary.getPersonalLeave()/8)+((double)employeeSalary.getTryPersonalLeave()/8))+employeeSalary.getAnnualLeave()+Math.floor((employeeSalary.getSickLeave()+employeeSalary.getTrySickLeave())/8)+employeeSalary.getMarriageLeave() + employeeSalary.getMaternityLeave()+employeeSalary.getFuneralLeave()));
						}else{
							//有问题，应是入职月实际出勤天数
							mealSupplement = mealDay * attendanceTime;
						}
					 }else{
						 //转正状态
						 mealSupplement = attendanceTime * mealDay;
					 }
					 if("N_JOB".equals(isJobCode)){
						 String quitDateMouth = employee.getQuitTime().toString().substring(0, 7);
						 if(salaryDate.equals(quitDateMouth)){
							 //离职日期天
							int qDays = Integer.parseInt(employee.getQuitTime().toString().substring(8,10));
							//离职
							if(qDays<=15){
								mealSupplement =  mealDay * attendanceTime;
							}else{
								mealSupplement = mealDay * ((double)realDay-(Math.floor(((double)employeeSalary.getPersonalLeave()/8)+((double)employeeSalary.getTryPersonalLeave()/8))+employeeSalary.getAnnualLeave()+Math.floor((employeeSalary.getSickLeave()+employeeSalary.getTrySickLeave())/8)+employeeSalary.getMarriageLeave() + employeeSalary.getMaternityLeave()+employeeSalary.getFuneralLeave()));
								
							}
						 }
					 }else{
						 //转正状态
						 mealSupplement = attendanceTime * 10;
					 }
				}
			}
			return mealSupplement;
		}
		/**
		 * 
		* @Title: getComputerSupplement 
		* @Description: TODO(获得电脑补助) 
		* @param @param employeeSalary
		* @param @param employee
		* @param @return
		* @param @throws Exception
		* @return double
		* @throws 
		* @author duanws
		* @date 2016-7-19 上午11:02:46
		 */
		private double getComputerSupplement(EmployeeSalary employeeSalary,Employee employee,Allowance allowance) throws Exception{
			double computerSupplement = 0.00;
			String isComputer = employeeSalary.getIsComputerDic().getCode();
			if("NO".equals(isComputer)){
				return 0.00;
			}
			double computerMoney = (allowance.getComputer()!=null)?Double.parseDouble(allowance.getComputer()):0.00;
			//是否转正
			String isFullMemberStr = this.salaryUtils.getFullMember(employee, employeeSalary.getSalaryDate());
			//是否在职
			String isJobCode = employee.getIsJob().getCode();
			String salaryDate = employeeSalary.getSalaryDate();
			//获得当月天数
			Calendar days = this.getDays(Integer.parseInt(salaryDate.substring(0, 4)), Integer.parseInt(salaryDate.substring(5, 7)));
			//获得当月第一天
			Calendar dayOne = Calendar.getInstance();
			dayOne.set(Calendar.YEAR, Integer.parseInt(salaryDate.substring(0, 4)));
			dayOne.set(Calendar.MONTH, Integer.parseInt(salaryDate.substring(5, 7)));
			dayOne.set(Calendar.DATE, 1);
			//当月工作日
			int wDay = salaryUtils.getWorkingDay(dayOne, days);
			//入职日期天
			int eDays = Integer.parseInt(employee.getEntryTime().toString().substring(8,10));
			//入职日期
			Calendar eCDays = Calendar.getInstance();
			String entryTimeStr = employee.getEntryTime();
			
			eCDays.setTime(sdf.parse(entryTimeStr));
			//员工入职当月实际出勤天数
			int realDay = salaryUtils.getWorkingDay(eCDays, days);
			//是否全勤
			String isFullTime = employeeSalary.getIsFullTimeDic().getCode();
			//应出勤天数
			int fullTime = employeeSalary.getFullTime();
			//出勤天数减去各种假
			double attendanceTime = getAttendanceTime(employeeSalary);
			//员工所在客户
			String customer = employee.getCustomer().getName();
			if("Y".equals(isFullTime)){
				computerSupplement = attendanceTime * computerMoney;
				
			}else{
				//当月转正状态
				 if("当月转正".equals(isFullMemberStr)){
					//入职
					if(eDays<=15){
						computerSupplement = computerMoney *((double)realDay-(Math.floor(((double)employeeSalary.getPersonalLeave()/8)+((double)employeeSalary.getTryPersonalLeave()/8))+employeeSalary.getAnnualLeave()+Math.floor((employeeSalary.getSickLeave()+employeeSalary.getTrySickLeave())/8)+employeeSalary.getMarriageLeave() + employeeSalary.getMaternityLeave()+employeeSalary.getFuneralLeave()));
					}else{
						//有问题，应是入职月实际出勤天数
						computerSupplement =computerMoney * attendanceTime;
					}
				 }else{
					 //转正状态
					 computerSupplement = attendanceTime * computerMoney;
				 }
				 if("N_JOB".equals(isJobCode)){
					//离职日期
					Calendar qCDays = Calendar.getInstance();
					String quitTimeStr = employee.getQuitTime();
					qCDays.setTime(sdf.parse(quitTimeStr));
					//员工离职当月实际出勤天数
					int qRealDay = salaryUtils.getWorkingDay(qCDays, days);
					//离职
					 String quitDateMouth = employee.getQuitTime().toString().substring(0, 7);
					 if(salaryDate.equals(quitDateMouth)){
						//离职日期天
							int qDays = Integer.parseInt(employee.getQuitTime().toString().substring(8,10));
							if(qDays<=15){
								computerSupplement =  computerMoney * attendanceTime;
							}else{
								computerSupplement = computerMoney * ((double)realDay-(Math.floor(((double)employeeSalary.getPersonalLeave()/8)+((double)employeeSalary.getTryPersonalLeave()/8))+employeeSalary.getAnnualLeave()+Math.floor((employeeSalary.getSickLeave()+employeeSalary.getTrySickLeave())/8)+employeeSalary.getMarriageLeave() + employeeSalary.getMaternityLeave()+employeeSalary.getFuneralLeave()));
							}
					 }
				 }else{
					 //转正状态
					 computerSupplement = attendanceTime * computerMoney ;
				 }
			}
			return computerSupplement;
		}
		/**
		 * 
		* @Title: getTraffic 
		* @Description: TODO(交通补助) 
		* @param @param employeeSalary
		* @param @param employee
		* @param @param allowance
		* @param @return
		* @param @throws Exception
		* @return double
		* @throws 
		* @author duanws
		* @date 2016-7-20 下午2:34:20
		 */
		private double getTraffic(EmployeeSalary employeeSalary,Employee employee,Allowance allowance) throws Exception{
			double trafficSupplement = 0.00;
			double trafficDay = 0.00;
			if(allowance.getTraffic()!=null || allowance.getTraffic()=="0" || "0".equals(allowance.getTraffic())){
				return 0.00;
			}else{
				trafficDay = Double.parseDouble(allowance.getTraffic());
			}
			//是否转正
			String isFullMemberStr = this.salaryUtils.getFullMember(employee, employeeSalary.getSalaryDate());
			//是否在职
			String isJobCode = employee.getIsJob().getCode();
			String salaryDate = employeeSalary.getSalaryDate();
			//获得当月天数
			Calendar days = this.getDays(Integer.parseInt(salaryDate.substring(0, 4)), Integer.parseInt(salaryDate.substring(5, 7)));
			//获得当月第一天
			Calendar dayOne = Calendar.getInstance();
			dayOne.set(Calendar.YEAR, Integer.parseInt(salaryDate.substring(0, 4)));
			dayOne.set(Calendar.MONTH, Integer.parseInt(salaryDate.substring(5, 7)));
			dayOne.set(Calendar.DATE, 1);
			//当月工作日
			int wDay = salaryUtils.getWorkingDay(dayOne, days);
			//入职日期天
			int eDays = Integer.parseInt(employee.getEntryTime().toString().substring(8,10));
			//入职日期
			Calendar eCDays = Calendar.getInstance();
			String entryTimeStr = employee.getEntryTime();
			
			eCDays.setTime(sdf.parse(entryTimeStr));
			//员工入职当月实际出勤天数
			int realDay = salaryUtils.getWorkingDay(eCDays, days);
			//是否全勤
			String isFullTime = employeeSalary.getIsFullTimeDic().getCode();
			//应出勤天数
			int fullTime = employeeSalary.getFullTime();
			//出勤天数减去各种假
			double attendanceTime = getAttendanceTime(employeeSalary);
			//员工所在客户
			String customer = employee.getCustomer().getName();
			if("Y".equals(isFullTime)){
				trafficSupplement = attendanceTime * trafficDay;
				
			}else{
				//当月转正状态
				 if("当月转正".equals(isFullMemberStr)){
					//入职
					if(eDays<=15){
						trafficSupplement = trafficDay *((double)realDay-(Math.floor(((double)employeeSalary.getPersonalLeave()/8)+((double)employeeSalary.getTryPersonalLeave()/8))+employeeSalary.getAnnualLeave()+Math.floor((employeeSalary.getSickLeave()+employeeSalary.getTrySickLeave())/8)+employeeSalary.getMarriageLeave() + employeeSalary.getMaternityLeave()+employeeSalary.getFuneralLeave()));
					}else{
						//有问题，应是入职月实际出勤天数
						trafficSupplement =trafficDay * attendanceTime;
					}
				 }else{
					 //转正状态
					 trafficSupplement = attendanceTime * trafficDay;
				 }
				 if("N_JOB".equals(isJobCode)){
					//离职日期
					Calendar qCDays = Calendar.getInstance();
					String quitTimeStr = employee.getQuitTime();
					qCDays.setTime(sdf.parse(quitTimeStr));
					//员工离职当月实际出勤天数
					int qRealDay = salaryUtils.getWorkingDay(qCDays, days);
					//离职
					 String quitDateMouth = employee.getQuitTime().toString().substring(0, 7);
					 if(salaryDate.equals(quitDateMouth)){
						//离职日期天
							int qDays = Integer.parseInt(employee.getQuitTime().toString().substring(8,10));
							if(qDays<=15){
								trafficSupplement =  trafficDay * attendanceTime;
							}else{
								trafficSupplement = trafficDay * ((double)realDay-(Math.floor(((double)employeeSalary.getPersonalLeave()/8)+((double)employeeSalary.getTryPersonalLeave()/8))+employeeSalary.getAnnualLeave()+Math.floor((employeeSalary.getSickLeave()+employeeSalary.getTrySickLeave())/8)+employeeSalary.getMarriageLeave() + employeeSalary.getMaternityLeave()+employeeSalary.getFuneralLeave()));
							}
					 }
				 }else{
					 //转正状态
					 trafficSupplement = attendanceTime * trafficDay ;
				 }
			}
			return trafficSupplement;
		}
		/**
		 * 
		* @Title: getOtherDay 
		* @Description: TODO(其它补助每天) 
		* @param @param employeeSalary
		* @param @param employee
		* @param @param allowance
		* @param @return
		* @param @throws Exception
		* @return double
		* @throws 
		* @author duanws
		* @date 2016-7-20 下午2:35:02
		 */
		private double getOtherDay(EmployeeSalary employeeSalary,Employee employee,Allowance allowance) throws Exception{
			double otherDaySupplement = 0.00;
			double otherDay = 0.00;
			if(allowance.getOtherDay()!=null || allowance.getOtherDay()=="0" || "0".equals(allowance.getOtherDay())){
				return 0.00;
			}else{
				otherDay = Double.parseDouble(allowance.getOtherDay());
			}
			//是否转正
			String isFullMemberStr = this.salaryUtils.getFullMember(employee, employeeSalary.getSalaryDate());
			//是否在职
			String isJobCode = employee.getIsJob().getCode();
			String salaryDate = employeeSalary.getSalaryDate();
			//获得当月天数
			Calendar days = this.getDays(Integer.parseInt(salaryDate.substring(0, 4)), Integer.parseInt(salaryDate.substring(5, 7)));
			//获得当月第一天
			Calendar dayOne = Calendar.getInstance();
			dayOne.set(Calendar.YEAR, Integer.parseInt(salaryDate.substring(0, 4)));
			dayOne.set(Calendar.MONTH, Integer.parseInt(salaryDate.substring(5, 7)));
			dayOne.set(Calendar.DATE, 1);
			//当月工作日
			int wDay = salaryUtils.getWorkingDay(dayOne, days);
			//入职日期天
			int eDays = Integer.parseInt(employee.getEntryTime().toString().substring(8,10));
			//入职日期
			Calendar eCDays = Calendar.getInstance();
			String entryTimeStr = employee.getEntryTime();
			
			eCDays.setTime(sdf.parse(entryTimeStr));
			//员工入职当月实际出勤天数
			int realDay = salaryUtils.getWorkingDay(eCDays, days);
			//是否全勤
			String isFullTime = employeeSalary.getIsFullTimeDic().getCode();
			//应出勤天数
			int fullTime = employeeSalary.getFullTime();
			//出勤天数减去各种假
			double attendanceTime = getAttendanceTime(employeeSalary);
			//员工所在客户
			String customer = employee.getCustomer().getName();
			if("Y".equals(isFullTime)){
				otherDaySupplement = attendanceTime * otherDay;
				
			}else{
				//当月转正状态
				 if("当月转正".equals(isFullMemberStr)){
					//入职
					if(eDays<=15){
						otherDaySupplement = otherDay *((double)realDay-(Math.floor(((double)employeeSalary.getPersonalLeave()/8)+((double)employeeSalary.getTryPersonalLeave()/8))+employeeSalary.getAnnualLeave()+Math.floor((employeeSalary.getSickLeave()+employeeSalary.getTrySickLeave())/8)+employeeSalary.getMarriageLeave() + employeeSalary.getMaternityLeave()+employeeSalary.getFuneralLeave()));
					}else{
						//有问题，应是入职月实际出勤天数
						otherDaySupplement =otherDay * attendanceTime;
					}
				 }else{
					 //转正状态
					 otherDaySupplement = attendanceTime * otherDay;
				 }
				 if("N_JOB".equals(isJobCode)){
					//离职日期
					Calendar qCDays = Calendar.getInstance();
					String quitTimeStr = employee.getQuitTime();
					qCDays.setTime(sdf.parse(quitTimeStr));
					//员工离职当月实际出勤天数
					int qRealDay = salaryUtils.getWorkingDay(qCDays, days);
					//离职
					 String quitDateMouth = employee.getQuitTime().toString().substring(0, 7);
					 if(salaryDate.equals(quitDateMouth)){
						//离职日期天
							int qDays = Integer.parseInt(employee.getQuitTime().toString().substring(8,10));
							if(qDays<=15){
								otherDaySupplement =  otherDay * attendanceTime;
							}else{
								otherDaySupplement = otherDay * ((double)realDay-(Math.floor(((double)employeeSalary.getPersonalLeave()/8)+((double)employeeSalary.getTryPersonalLeave()/8))+employeeSalary.getAnnualLeave()+Math.floor((employeeSalary.getSickLeave()+employeeSalary.getTrySickLeave())/8)+employeeSalary.getMarriageLeave() + employeeSalary.getMaternityLeave()+employeeSalary.getFuneralLeave()));
							}
					 }
				 }else{
					 //转正状态
					 otherDaySupplement = attendanceTime * otherDay ;
				 }
			}
			return otherDaySupplement;
		}
		
		
		/**
		 * 
		* @Title: getAnnualLeave 
		* @Description: TODO(获得剩余年假数) 
		* @param @param employeeSalary
		* @param @return
		* @return int
		* @throws 
		* @author duanws
		* @date 2016-7-19 上午11:02:35
		 */
		private int getAnnualLeave(EmployeeSalary employeeSalary){
			String year = employeeSalary.getSalaryDate().toString().substring(0, 4);
			String employeeId = employeeSalary.getEmployee().getId();
			List<Object[]> objList= this.employeeSalaryDao.getSumAnnualLeave(employeeId, year);
			int annualLeave = (Integer) objList.get(0)[0];
			return (5-annualLeave>0)?5-annualLeave:0;
		}
		/**
		 * 
		* @Title: getSocialSecurity 
		* @Description: TODO(计算社保) 
		* @param @param employeeSalary
		* @param @param employee
		* @param @return
		* @return double
		* @throws 
		* @author duanws
		* @date 2016-7-19 上午11:00:51
		 */
		private double getSocialSecurity(EmployeeSalary employeeSalary,Employee employee){
			String registerStr = employee.getRegister().getCode();
			double socialSecurity = 0.00;
			if("CITY".equals(registerStr)){
				//城镇
				socialSecurity = 292.53;
			}else if("COUNTRY".equals(registerStr)){
				//农村
				socialSecurity = 287.36;
			}
			
			return socialSecurity;
		}
		/**
		 * 
		* @Title: getTax 
		* @Description: TODO(计算个人所得税) 
		* @param @param employeeSalary
		* @param @param employee
		* @param @param totalSalary
		* @param @return
		* @return double
		* @throws 
		* @author duanws
		* @date 2016-7-19 上午11:01:05
		 */
		private double getTax(EmployeeSalary employeeSalary,Employee employee,double totalSalary){
//	      扣税公式是：  
//	      （扣除社保医保公积金后薪水-个税起征点）*税率-速算扣除数  
	        double taxbase=totalSalary - 3500;  
	        double Taxrate=0;//这里税率没有除以百分比；  
	        
	        double Quickdeduction=0;  //速算扣除数
	        if(taxbase <=0)//低于个税起征点  
	        {  
	            return 0.00;  
	        }else if(taxbase <=1500)  
	        {  
	            Taxrate=3;  
	            Quickdeduction=0;  
	        }else if(taxbase <=4500)  
	        {  
	            Taxrate=10;  
	            Quickdeduction=105;  
	        }else if(taxbase <=9000)  
	        {  
	            Taxrate=20;  
	            Quickdeduction=555;  
	        }else if(taxbase <=35000)  
	        {  
	            Taxrate=25;  
	            Quickdeduction=1005;  
	        }else if(taxbase <=55000)  
	        {  
	            Taxrate=30;  
	            Quickdeduction=2755;  
	        }else if(taxbase <=80000)  
	        {  
	            Taxrate=35;  
	            Quickdeduction=5505;  
	        }else  
	        {  
	            Taxrate=45;  
	            Quickdeduction=13505;  
	        }
	        DecimalFormat df = new DecimalFormat("#.00");
	        return Double.parseDouble(df.format(taxbase * Taxrate/100-Quickdeduction));  

		}
		/**
		 * 
		* @Title: getOverTimeMoney 
		* @Description: TODO(合算加班费) 
		* @param @param employeeSalary
		* @param @param employee
		* @param @return
		* @return double
		* @throws 
		* @author duanws
		* @date 2016-7-19 上午11:01:27
		 */
		private double getOverTimeMoney(EmployeeSalary employeeSalary,Employee employee){
			//加班费
			double overTimeMoney = 0.00;
			//员工所在客户
			String customerCode = employee.getCustomer().getCode();
			//每小时加班费
			double overTimeMoneyHour = Double.parseDouble(employee.getCustomer().getOverTimeMoney());
			//加班小时数
			double overTime = (employeeSalary.getOverTime()==null||employeeSalary.getOverTime()=="")?0.00:Double.parseDouble(employeeSalary.getOverTime());
			if(overTime==0.00 || overTime==0){
				return 0.00;
			}
			//员工基本工资
			double salary = Double.parseDouble(employee.getSalary());
			//加班小时数换算成天数
			//BigDecimal overTimeDay = (overTime!=null && overTime !=BigDecimal.valueOf(0))?overTime.divide(BigDecimal.valueOf(8)):BigDecimal.valueOf(0);
			overTimeMoney = overTime * overTimeMoneyHour;
			
			return overTimeMoney;
		}
		/**
		 * 
		* @Title: getAttendanceTime 
		* @Description: TODO(获得员工减去各种假后的出勤天数) 
		* @param @param employeeSalary
		* @param @return
		* @return double
		* @throws 
		* @author duanws
		* @date 2016-7-19 上午11:01:46
		 */
		private double getAttendanceTime(EmployeeSalary employeeSalary){
			//应出勤天数
			int fullTime = employeeSalary.getFullTime();
			//出勤天数
			double attendanceTime = fullTime-(Math.floor(((double)employeeSalary.getPersonalLeave()/8)+((double)employeeSalary.getTryPersonalLeave()/8))+employeeSalary.getAnnualLeave()+Math.floor((employeeSalary.getSickLeave()+employeeSalary.getTrySickLeave())/8)+ employeeSalary.getMarriageLeave() + employeeSalary.getMaternityLeave()+employeeSalary.getFuneralLeave());
			
			return attendanceTime;
		}
		/**
		 * 
		* @Title: getAbsenceDay 
		* @Description: TODO(取缺勤总天数) 
		* @param @param employeeSalary
		* @param @return
		* @return double
		* @throws 
		* @author duanws
		* @date 2016-7-19 上午11:01:57
		 */
		private double getAbsenceDay(EmployeeSalary employeeSalary){
			return ((double)employeeSalary.getPersonalLeave()/8)+((double)employeeSalary.getTryPersonalLeave()/8)+employeeSalary.getAnnualLeave()+((double)employeeSalary.getSickLeave()+(double)employeeSalary.getTrySickLeave())/8+ employeeSalary.getMarriageLeave() + employeeSalary.getMaternityLeave()+employeeSalary.getFuneralLeave();
		}
		/**
		 * 
		* @Title: getAbsenceMoney 
		* @Description: TODO(获得员工缺勤扣款) 
		* @param @param employeeSalary
		* @param @param employee
		* @param @return
		* @return double
		* @throws 
		* @author duanws
		* @date 2016-7-19 上午11:02:15
		 */
		private double getAbsenceMoney(EmployeeSalary employeeSalary,Employee employee){		
			
			String salaryDate = employeeSalary.getSalaryDate();
			String year = employeeSalary.getSalaryDate().toString().substring(0, 4);
			String employeeId = employeeSalary.getEmployee().getId();
			//获得当月天数
			Calendar days = this.getDays(Integer.parseInt(salaryDate.substring(0, 4)), Integer.parseInt(salaryDate.substring(5, 7)));
			//获得当月第一天
			Calendar dayOne = Calendar.getInstance();
			dayOne.set(Calendar.YEAR, Integer.parseInt(salaryDate.substring(0, 4)));
			dayOne.set(Calendar.MONTH, Integer.parseInt(salaryDate.substring(5, 7)));
			dayOne.set(Calendar.DATE, 1);
			//当月实际工作日
			int wDay = salaryUtils.getWorkingDay(dayOne, days);
			//试用期事假天数
			double tryPersonalDays = (double)employeeSalary.getTryPersonalLeave()/8;
			//转正事假天数
			double personalDays = (double)employeeSalary.getPersonalLeave()/8;
			//事假扣款总额
			double personalMoney = Double.parseDouble(employee.getSalary())/(double)wDay*personalDays+Double.parseDouble(employee.getSalary())*0.8/(double)wDay*tryPersonalDays;
			//合算病假--------------------------------------------------------
			//是否转正
			String isFullMemberStr = this.salaryUtils.getFullMember(employee, employeeSalary.getSalaryDate());
			List<Object[]> objList = this.employeeSalaryDao.getSumSickLeave(employeeId, year);
			//试用期病假天数
			double trySickLeave = (Long) objList.get(0)[1] / 8;
			//转正期病假天数
			double sickLeave = (Long) objList.get(0)[0] / 8;
			//已休病假天数
			double sumSickEd = trySickLeave + sickLeave;
			//申请休病假天数
			double sumSickLeave = (employeeSalary.getSickLeave() + employeeSalary.getTrySickLeave())/8;
			double sickMoney = 0.00;
			if((sumSickLeave+sumSickEd)<=3){
				
			}else if(4 <= (sumSickLeave+sumSickEd) && (sumSickLeave+sumSickEd) <= 5){
				//当月转正状态
				 if("当月转正".equals(isFullMemberStr)){
					//试用
					if(employeeSalary.getTrySickLeave()+sumSickEd>3){
						sickMoney = (double)(employeeSalary.getTrySickLeave()+sumSickEd - 3) * Double.parseDouble(employee.getSalary()) * 0.8 / workDays * 0.5;
					}
					//转正
					if(sumSickLeave+sumSickEd>3){
						sickMoney = (double)(sumSickLeave+sumSickEd - 3) * Double.parseDouble(employee.getSalary()) / workDays *0.5;
					}
				 }else{
						sickMoney = (double)(sumSickLeave+sumSickEd - 3) * Double.parseDouble(employee.getSalary()) / workDays *0.5;
					}
				
			}else if((sumSickLeave+sumSickEd) >= 6){
				
				//当月转正状态
				 if("当月转正".equals(isFullMemberStr)){
					//试用
					if(employeeSalary.getTrySickLeave()+sumSickEd>=6){
						sickMoney = (Double.parseDouble(employee.getSalary()) * 0.8 / wDay - 1720 /wDay * employeeSalary.getTrySickLeave()+sumSickEd-5);
					}
					//转正
					if(sumSickLeave+sumSickEd>=6){
						sickMoney = (Double.parseDouble(employee.getSalary()) / wDay - 1720 /wDay * employeeSalary.getTrySickLeave()+sumSickEd-5);
					}
				 }else{
					 sickMoney = (Double.parseDouble(employee.getSalary()) / wDay - 1720 /wDay * employeeSalary.getTrySickLeave()+sumSickEd-5);
				 }
			}
			
			
			return (sickMoney + personalMoney);
		}

		
	//----------------------工资合算-------END---------------------------------------------------------------------
		@Override
		public void delEmployeeSalaryByIdDate(String employeeId,
				String salaryDate) {
			this.employeeSalaryDao.delEmployeeSalaryByIdDate(employeeId, salaryDate);
			
		}

		@Override
		public void delSalaryResultByIdDate(String employeeId, String salaryDate) {
			this.employeeSalaryDao.delSalaryResultByIdDate(employeeId, salaryDate);
			
		}


		@Override
		public boolean checkEmployeeSalaryIsExist(String id, String salaryDate) {
			String hql = " from EmployeeSalary e where e.salaryDate='"+salaryDate+"'";
			List<EmployeeSalary> list = this.employeeSalaryDao.queryList(hql, new Object[0]);
			if((list != null) && (list.size()>0)){
				if(list.size()==1){
					if(!list.get(0).getId().equals(id)){
						return false;
					}
				}else if(list.size() > 1){
					return false;
				}
			}
			return true;
		}

		@Override
		public void exportEmployeeSalary(EmployeeSalary employeeSalary,HttpServletResponse response) {
			String salaryDate = employeeSalary.getSalaryDate();
			String sheetName = salaryDate+"员工薪资信息";
			
			//声明一个工作薄
			HSSFWorkbook workbook = new HSSFWorkbook();
			//生成一个表格
			HSSFSheet sheet = workbook.createSheet(sheetName);
			sheet.setColumnWidth(0, 8*256);
			sheet.setColumnWidth(1, 15*256);
			sheet.setColumnWidth(2, 8*256);
			sheet.setColumnWidth(3, 15*256);
			sheet.setColumnWidth(4, 15*256);
			sheet.setColumnWidth(5, 15*256);
			sheet.setColumnWidth(6, 20*256);
			//生成一个样式
			HSSFCellStyle styleHeader = workbook.createCellStyle();
			styleHeader.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			styleHeader.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			styleHeader.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			styleHeader.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			styleHeader.setBorderRight(HSSFCellStyle.BORDER_THIN);
			styleHeader.setBorderTop(HSSFCellStyle.BORDER_THIN);
			//生成一个字体
			HSSFFont fontHeader = workbook.createFont();
			fontHeader.setFontName("宋体");
			fontHeader.setFontHeightInPoints((short)10);
			fontHeader.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			styleHeader.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
			styleHeader.setFillPattern(CellStyle.SOLID_FOREGROUND);
			styleHeader.setFont(fontHeader);
			//生成并设置另一个样式
			HSSFCellStyle style1 = workbook.createCellStyle();
			style1.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			style1.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			style1.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			style1.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			style1.setBorderRight(HSSFCellStyle.BORDER_THIN);
			style1.setBorderTop(HSSFCellStyle.BORDER_THIN);
			HSSFFont font1 = workbook.createFont();
			font1.setFontName("宋体");
			font1.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			font1.setFontHeightInPoints((short)10);
			style1.setFont(font1);
			//生成并设置另一个样式
			HSSFCellStyle style2 = workbook.createCellStyle();
			style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			style2.setBorderRight(HSSFCellStyle.BORDER_THIN);
			style2.setBorderTop(HSSFCellStyle.BORDER_THIN);
			HSSFFont font2 = workbook.createFont();
			font2.setFontName("宋体");
			font2.setFontHeightInPoints((short)10);
			style2.setFont(font2);
			
			HSSFRow rowsClass = sheet.createRow(0);
			HSSFCell cell = rowsClass.createCell(0);
			cell.setCellValue(sheetName);
			cell.setCellStyle(style1);
			rowsClass.setHeight((short)500);//行高
			//合并单元格
			sheet.addMergedRegion(new CellRangeAddress(0,(short)0,0,(short)(10)));
			//产生表格标题行
			HSSFRow row = sheet.createRow(1);
			row.setHeightInPoints(20);
			//表头名称
			String [] headers = {"日期","姓名","员工编号","缺勤扣款","出勤工资","工资及补贴总额","代扣款","实发工资"};
			List<String> headerList = new ArrayList<String>();
			Collections.addAll(headerList, headers);//将数组转换成列表
			//List<String> headerList1 = new ArrayList<String>(Arrays.asList(headers));   数据转列表方法2
			//添加标题
			for(short i = 0;i < headerList.size();i++){
				HSSFCell cell1 = row.createCell(i);
				cell1.setCellStyle(styleHeader);
				HSSFRichTextString text = new HSSFRichTextString(headerList.get(i));
				cell1.setCellValue(text);
			}
			//行序号
			int index1 = 0;
			List<SalaryResult> salaryResultList = this.employeeSalaryDao.querySalaryResultList(employeeSalary);
			for(SalaryResult sr:salaryResultList){
				index1++;
				row = sheet.createRow(index1);
				Map<String,String> salaryMap = this.getValueMap(sr, headerList);
				//列序号
				short j = 0;
				for(String key:headerList){
					//赋值
					HSSFCell cellNew = row.createCell(j);
					cellNew.setCellStyle(style2);
					cellNew.setCellValue(salaryMap.get(key));
					j++;
				}
				
				
			}
			try{
				String fileName = "员工薪资表.xls";
				response.setContentType("application/x-excel");
				response.setHeader("Content-disposition", "attachment;filename="+new String(fileName.getBytes("GBK"),"iso-8859-1"));
				response.setCharacterEncoding("UTF-8");
				OutputStream os = response.getOutputStream();
				workbook.write(os);
				os.flush();
				os.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		/**
		 * 
		* @Title: getValueMap 
		* @Description: TODO(拼接导出信息) 
		* @param @param sr
		* @param @param headerList
		* @param @return
		* @return Map<String,String>
		* @throws 
		* @author duanws
		* @date 2016-8-1 下午1:39:07
		 */
		private Map<String,String> getValueMap(SalaryResult sr,List headerList){
			Map salaryMap = new HashMap<String,String>();
			//日期
			salaryMap.put(headerList.get(0), sr.getSalaryDate());
			//员工姓名
			salaryMap.put(headerList.get(1), sr.getEmployee().getEmployeeName());
			//员工编号
			salaryMap.put(headerList.get(2), sr.getEmployee().getEmployeeCode());
			//缺勤扣款
			salaryMap.put(headerList.get(3),sr.getAbsenceMoney());
			//出勤工资
			salaryMap.put(headerList.get(4), sr.getAttendanceMoney());
			//工资及补贴总额
			salaryMap.put(headerList.get(5), sr.getSum());
			//代扣款
			salaryMap.put(headerList.get(6), sr.getOther());
			//实发工资
			salaryMap.put(headerList.get(7), sr.getFinnalMoney());
			return salaryMap;
		}
	
}
