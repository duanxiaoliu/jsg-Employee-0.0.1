package com.jsg.employee.service.impl;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jsg.base.model.BasePage;
import com.jsg.base.util.DataUtil;
import com.jsg.employee.dao.IEmployeeDao;
import com.jsg.employee.dao.IEmployeeSalaryDao;
import com.jsg.employee.model.Employee;
import com.jsg.employee.model.EmployeeSalary;
import com.jsg.employee.model.SalaryResult;
import com.jsg.employee.service.IEmployeeSalaryService;
import com.jsg.employee.util.SalaryUtils;
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
	//所在客户月平均工作天数
	private double workDays = 21.75;
	private SalaryUtils salaryUtils = new SalaryUtils();
	//转正工资调整额
	double promotion = 0.00;

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
	public void saveEmployeeSalary(EmployeeSalary employeeSalary) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void saveSalaryResult(SalaryResult salaryResult) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public EmployeeSalary getEmployeeSalaryBySalaryDate(String employeeId,
			String salaryDate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SalaryResult getSalaryResultBySalaryDate(String employeeId,
			String salaryDate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SalaryResult computeSalary(EmployeeSalary employeeSalary) {

		Employee employee = (Employee) this.employeeDao.get(Employee.class, employeeSalary.getEmployee().getId());
		//根据客户管理调整月平均工作天数
		workDays = employee.getCustomer().getWorkDays().doubleValue();
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
			salaryResult = getSalaryResult(employeeSalary,employee,salaryResult);
			this.employeeSalaryDao.update(salaryResult);
		}else{
			salaryResult = getSalaryResult(employeeSalary,employee,new SalaryResult());
			salaryResult.setCreateTime(new Date());
			this.employeeSalaryDao.save(salaryResult);
		}		
		return salaryResult;
	}
	//----------------------工资合算------------START----------------------------------------------------------------
		//合算工资
		private SalaryResult getSalaryResult(EmployeeSalary employeeSalary,Employee employee,SalaryResult salaryResult){
			//入职日期天
			int eDays = Integer.parseInt(employee.getEntryTime().toString().substring(8,10));
			//是否转正
			String isFullMemberStr = SalaryUtils.getFullMember(employee, employeeSalary.getSalaryDate());
			//是否在职
			String isJobCode = employee.getIsJob().getCode();
			//基本工资
			BigDecimal baseSalary = this.getBaseSalary(employeeSalary,employee);
			//员工
			salaryResult.setEmployee(employee);
			//薪资年月
			salaryResult.setSalaryDate(employeeSalary.getSalaryDate());
			//缺勤天数
			salaryResult.setAbsenceDay(String.valueOf(this.getAbsenceDay(employeeSalary)));
			//缺勤扣款
			salaryResult.setAbsenceMoney(this.getAbsenceMoney(employeeSalary, employee));
			//加班费及补助
			salaryResult.setOverTimeMoney(this.getOverTimeMoney(employeeSalary, employee).add(this.getMealSupplement(employeeSalary, employee)).add(this.getComputerSupplement(employeeSalary, employee)));
			//出勤工资--基本工资减去缺勤扣款
			salaryResult.setAttendanceMoney(baseSalary.subtract(this.getAbsenceMoney(employeeSalary, employee)));
			//转正工资调整
			salaryResult.setPromotion(promotion);
			//奖金
			BigDecimal reward = (this.employeeSalaryDao.getRwardByDate(employee.getId(), employeeSalary.getSalaryDate())!=null)?this.employeeSalaryDao.getRwardByDate(employee.getId(), employeeSalary.getSalaryDate()).getRewardAmount():BigDecimal.valueOf(0);
			salaryResult.setReward(reward);
			//岗位津贴
			BigDecimal allowance = employeeSalary.getAllowance()!=null?employeeSalary.getAllowance():BigDecimal.valueOf(0);
			salaryResult.setAllowance(allowance);
			//餐补
			salaryResult.setMealSupplement(this.getMealSupplement(employeeSalary, employee));
			//电脑补助
			salaryResult.setComputerSupplement(this.getComputerSupplement(employeeSalary, employee));
			//上月调整
			salaryResult.setAdjustment(employeeSalary.getAdjustment()!=null?employeeSalary.getAdjustment():BigDecimal.valueOf(0));
			//离职补偿金
			salaryResult.setCompensate(employeeSalary.getResignMoney()!=null?employeeSalary.getResignMoney():BigDecimal.valueOf(0));
			//工资及补偿总额
			BigDecimal sumMoney = baseSalary.subtract(this.getAbsenceMoney(employeeSalary, employee)).add(reward).add(allowance).add(this.getMealSupplement(employeeSalary, employee)).add(this.getComputerSupplement(employeeSalary, employee)).add(employeeSalary.getAdjustment()!=null?employeeSalary.getAdjustment():BigDecimal.valueOf(0)).add(employeeSalary.getResignMoney()!=null?employeeSalary.getResignMoney():BigDecimal.valueOf(0)).add(promotion).add(this.getOverTimeMoney(employeeSalary, employee));
			salaryResult.setSum(sumMoney);
			//社保
			
			BigDecimal socialSecurity = BigDecimal.valueOf(0);
			if("未转正".equals(isFullMemberStr)){
				socialSecurity = BigDecimal.valueOf(0);
			}else if("当月转正".equals(isFullMemberStr)){
				if(eDays<15){
					socialSecurity = this.getSocialSecurity(employeeSalary, employee);
				}else{
					socialSecurity = BigDecimal.valueOf(0);
				}
			}else if("N_JOB".equals(isJobCode)){
				String quitDay = employee.getQuitTime().toString().substring(8, 10);
				if(Integer.parseInt(quitDay)<15){
					socialSecurity = BigDecimal.valueOf(0);
				}else{
					socialSecurity = this.getSocialSecurity(employeeSalary, employee);
				}
			}else{
				socialSecurity = this.getSocialSecurity(employeeSalary, employee);
			}
			salaryResult.setSocialSecurity(socialSecurity);
			//公积金
			BigDecimal fundMoney = BigDecimal.valueOf(0);
			if("未转正".equals(isFullMemberStr)){
				if("Y".equals(employeeSalary.getIsSelfDic().getCode())){
					fundMoney = (employeeSalary.getFundMoney()!=null)?employeeSalary.getFundMoney():BigDecimal.valueOf(0);
				}else{
					fundMoney = BigDecimal.valueOf(0);
				}
			}else if("当月转正".equals(isFullMemberStr)){
				if(eDays<10){
					fundMoney = employeeSalary.getFundMoney();
				}else{
					if("Y".equals(employeeSalary.getIsSelfDic().getCode())){
						fundMoney = (employeeSalary.getFundMoney()!=null)?employeeSalary.getFundMoney():BigDecimal.valueOf(0);
					}else{
						fundMoney = BigDecimal.valueOf(0);
					}
				}
			}else if("N_JOB".equals(isJobCode)){
				String quitDay = employee.getQuitTime().toString().substring(8, 10);
				if(Integer.parseInt(quitDay)<10){
					fundMoney = BigDecimal.valueOf(0);
				}else{
					fundMoney = (employeeSalary.getFundMoney()!=null)?employeeSalary.getFundMoney():BigDecimal.valueOf(0);
				}
			}else{
				fundMoney = (employeeSalary.getFundMoney()!=null)?employeeSalary.getFundMoney():BigDecimal.valueOf(0);
			}
			salaryResult.setFundMoney(fundMoney);
			//罚款
			salaryResult.setPunish((employeeSalary.getPunish()!=null)?employeeSalary.getPunish():BigDecimal.valueOf(0));
			//代扣款
			salaryResult.setOther((employeeSalary.getOther()!=null)?employeeSalary.getOther():BigDecimal.valueOf(0));
			BigDecimal debitMoney = socialSecurity.add(fundMoney).add(salaryResult.getPunish()).add(salaryResult.getOther());
			BigDecimal totalSalary = sumMoney.subtract(debitMoney);
			//个税
			BigDecimal tax = this.getTax(employeeSalary, employee, totalSalary);
			salaryResult.setTax(tax);
			//实发工资
			BigDecimal finnalMoney = totalSalary.subtract(tax);
			salaryResult.setFinnalMoney(finnalMoney);
			
			return salaryResult;
		}
		/**
		 * 
		* @Title: getBaseSalary 
		* @Description: TODO(获取基本工资，处理试用期工资等) 
		* @param @param employee
		* @param @return
		* @return BigDecimal
		* @throws 
		* @author duanws
		* @date 2016-5-17 下午2:40:12
		 */
		private BigDecimal getBaseSalary(EmployeeSalary employeeSalary,Employee employee){
			
			//是否转正
			String isFullMemberStr = this.salaryUtils.getFullMember(employee, employeeSalary.getSalaryDate());
			//试用期是否全薪
			String probationStateStr = employee.getProbationState().getCode();
			//获得基本工资
			BigDecimal baseSalary= employee.getSalary();
			
			if("未转正".equals(isFullMemberStr)){
				if("NO".equals(probationStateStr)){
					//试用期不是全额工资，乘80%
					baseSalary = baseSalary.multiply(BigDecimal.valueOf(0.8)) ;
				}
			}else if("当月转正".equals(isFullMemberStr)){
				//当月转正
				if("NO".equals(probationStateStr)){
					//试用期不是全额工资，乘80%
					baseSalary = baseSalary.multiply(BigDecimal.valueOf(0.8)) ;
					String salaryDate = employeeSalary.getSalaryDate();
					
					//获得当月天数
					Calendar days = this.getDays(Integer.parseInt(salaryDate.substring(0, 4)), Integer.parseInt(salaryDate.substring(5, 7)));
					//获得当月第一天
					Calendar dayOne = Calendar.getInstance();
					dayOne.set(Calendar.YEAR, Integer.parseInt(salaryDate.substring(0, 4)));
					dayOne.set(Calendar.MONTH, Integer.parseInt(salaryDate.substring(5, 7)));
					dayOne.set(Calendar.DATE, 1);
					
					//转正日期
					Date EDay = employee.getEntryTime();
					Calendar ca = Calendar.getInstance();
					ca.setTime(EDay);
					ca.add(Calendar.MONTH, 3);
					//当月工作日
					int wDay = salaryUtils.getWorkingDay(dayOne, days);
					//转正天数校正
					double zDay = salaryUtils.getWorkingDay(ca, days) * workDays / wDay;
					
					//转正工资调整额
					promotion =  (baseSalary.multiply(BigDecimal.valueOf(0.8))).multiply(BigDecimal.valueOf((workDays-zDay))).divide(BigDecimal.valueOf(workDays),2).add(baseSalary.multiply(BigDecimal.valueOf(zDay)).divide(BigDecimal.valueOf(workDays),2)).subtract(baseSalary.multiply(BigDecimal.valueOf(0.8)));
						
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
		* @return BigDecimal
		* @throws 
		* @author duanws
		* @date 2016-5-17 下午3:57:25
		 */
		private BigDecimal getMealSupplement(EmployeeSalary employeeSalary,Employee employee){
			BigDecimal mealSupplement = BigDecimal.valueOf(0);
			
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
			eCDays.setTime(employee.getEntryTime());
			//员工入职当月实际出勤天数
			int realDay = salaryUtils.getWorkingDay(eCDays, days);

//			//离职日期
//			Calendar qCDays = Calendar.getInstance();
//			qCDays.setTime(employee.getQuitTime());
//			//员工离职当月实际出勤天数
//			int qRealDay = salaryUtils.getWorkingDay(qCDays, days);
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
					mealSupplement = BigDecimal.valueOf((attendanceTime * 10<=200)?attendanceTime * 10:200);
					
				}else{
					//当月转正状态
					 if("当月转正".equals(isFullMemberStr)){
						//入职
						if(eDays<=15){
							mealSupplement = BigDecimal.valueOf(200).subtract(BigDecimal.valueOf(10).multiply(BigDecimal.valueOf((double)wDay - ((double)realDay-(Math.floor(((double)employeeSalary.getPersonalLeave()/8)+((double)employeeSalary.getTryPersonalLeave()/8))+employeeSalary.getAnnualLeave()+Math.floor((employeeSalary.getSickLeave()+employeeSalary.getTrySickLeave())/8)+employeeSalary.getMarriageLeave() + employeeSalary.getMaternityLeave()+employeeSalary.getFuneralLeave())))));
						}else{
							//有问题，应是入职月实际出勤天数
							mealSupplement = BigDecimal.valueOf(10 * attendanceTime);
						}
					 }else{
						 //转正状态
						 mealSupplement = BigDecimal.valueOf((attendanceTime * 10<=200)?attendanceTime * 10:200);
					 }
					 if("N_JOB".equals(isJobCode)){
						 String quitDateMouth = employee.getQuitTime().toString().substring(0, 7);
						 if(salaryDate.equals(quitDateMouth)){
							 //离职日期天
							int qDays = Integer.parseInt(employee.getQuitTime().toString().substring(8,10));
							//离职
							if(qDays<=15){
								mealSupplement =  BigDecimal.valueOf(10 * attendanceTime);
							}else{
								mealSupplement = BigDecimal.valueOf(200).subtract(BigDecimal.valueOf(10).multiply(BigDecimal.valueOf((double)wDay - ((double)realDay-(Math.floor(((double)employeeSalary.getPersonalLeave()/8)+((double)employeeSalary.getTryPersonalLeave()/8))+employeeSalary.getAnnualLeave()+Math.floor((employeeSalary.getSickLeave()+employeeSalary.getTrySickLeave())/8)+employeeSalary.getMarriageLeave() + employeeSalary.getMaternityLeave()+employeeSalary.getFuneralLeave())))));
								
							}
						 }
					 }else{
						 //转正状态
						 mealSupplement = BigDecimal.valueOf((attendanceTime * 10<=200)?attendanceTime * 10:200);
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
		* @return BigDecimal
		* @throws 
		* @author duanws
		* @date 2016-5-17 下午4:56:18
		 */
		private BigDecimal getComputerSupplement(EmployeeSalary employeeSalary,Employee employee){
			BigDecimal computerSupplement = BigDecimal.valueOf(0);
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
			eCDays.setTime(employee.getEntryTime());
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
				computerSupplement = BigDecimal.valueOf((attendanceTime * 5<=100)?attendanceTime * 5:100);
				
			}else{
				//当月转正状态
				 if("当月转正".equals(isFullMemberStr)){
					//入职
					if(eDays<=15){
						computerSupplement = BigDecimal.valueOf(100).subtract(BigDecimal.valueOf(5).multiply(BigDecimal.valueOf((double)wDay - ((double)realDay-(Math.floor(((double)employeeSalary.getPersonalLeave()/8)+((double)employeeSalary.getTryPersonalLeave()/8))+employeeSalary.getAnnualLeave()+Math.floor((employeeSalary.getSickLeave()+employeeSalary.getTrySickLeave())/8)+employeeSalary.getMarriageLeave() + employeeSalary.getMaternityLeave()+employeeSalary.getFuneralLeave())))));
					}else{
						//有问题，应是入职月实际出勤天数
						computerSupplement = BigDecimal.valueOf(5 * attendanceTime);
					}
				 }else{
					 //转正状态
					 computerSupplement = BigDecimal.valueOf((attendanceTime * 5<=100)?attendanceTime * 5:100);
				 }
				 if("N_JOB".equals(isJobCode)){
					//离职日期
					Calendar qCDays = Calendar.getInstance();
					qCDays.setTime(employee.getQuitTime());
					//员工离职当月实际出勤天数
					int qRealDay = salaryUtils.getWorkingDay(qCDays, days);
					//离职
					 String quitDateMouth = employee.getQuitTime().toString().substring(0, 7);
					 if(salaryDate.equals(quitDateMouth)){
						//离职日期天
							int qDays = Integer.parseInt(employee.getQuitTime().toString().substring(8,10));
							if(qDays<=15){
								computerSupplement =  BigDecimal.valueOf(5 * attendanceTime);
							}else{
								computerSupplement = BigDecimal.valueOf(100).subtract(BigDecimal.valueOf(5).multiply(BigDecimal.valueOf((double)wDay - ((double)qRealDay-(Math.floor(((double)employeeSalary.getPersonalLeave()/8)+((double)employeeSalary.getTryPersonalLeave()/8))+employeeSalary.getAnnualLeave()+Math.floor((employeeSalary.getSickLeave()+employeeSalary.getTrySickLeave())/8)+employeeSalary.getMarriageLeave() + employeeSalary.getMaternityLeave()+employeeSalary.getFuneralLeave())))));
							}
					 }
				 }else{
					 //转正状态
					 computerSupplement = BigDecimal.valueOf((attendanceTime *5<=100)?attendanceTime * 5:100);
				 }
			}
			return computerSupplement;
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
		* @date 2016-5-17 下午5:06:45
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
		* @Description: TODO(计算社保缴纳金额) 
		* @param @param employeeSalary
		* @param @param employee
		* @param @return
		* @return BigDecimal
		* @throws 
		* @author duanws
		* @date 2016-5-17 下午5:24:32
		 */
		private BigDecimal getSocialSecurity(EmployeeSalary employeeSalary,Employee employee){
			String registerStr = employee.getRegister().getCode();
			BigDecimal socialSecurity = BigDecimal.valueOf(0);
			if("CITY".equals(registerStr)){
				//城镇
				socialSecurity = BigDecimal.valueOf(292.53);
			}else if("COUNTRY".equals(registerStr)){
				//农村
				socialSecurity = BigDecimal.valueOf(287.36);
			}
			
			return socialSecurity;
		}
		/**
		 * 
		* @Title: getTax 
		* @Description: TODO(获取个人所得税) 
		* @param @param employeeSalary
		* @param @param employee
		* @param @return
		* @return BigDecimal
		* @throws 
		* @author duanws
		* @date 2016-5-17 下午6:01:29
		 */
		private BigDecimal getTax(EmployeeSalary employeeSalary,Employee employee,BigDecimal totalSalary){
//	      扣税公式是：  
//	      （扣除社保医保公积金后薪水-个税起征点）*税率-速算扣除数  
	        double taxbase=totalSalary.subtract(BigDecimal.valueOf(3500)).doubleValue();  
	        double Taxrate=0;//这里税率没有除以百分比；  
	        
	        double Quickdeduction=0;  //速算扣除数
	        if(taxbase <=0)//低于个税起征点  
	        {  
	            return BigDecimal.valueOf(0);  
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
	        return BigDecimal.valueOf(taxbase).multiply(BigDecimal.valueOf(Taxrate)).divide(BigDecimal.valueOf(100)).subtract(BigDecimal.valueOf(Quickdeduction));  

		}
		/**
		 * 
		* @Title: getOverTimeMoney 
		* @Description: TODO(合算加班费) 
		* @param @param employeeSalary
		* @param @param employee
		* @param @return
		* @return BigDecimal
		* @throws 
		* @author duanws
		* @date 2016-5-19 上午10:30:30
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
		* @date 2016-5-19 下午2:59:17
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
		* @date 2016-5-23 上午11:00:49
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
		* @return BigDecimal
		* @throws 
		* @author duanws
		* @date 2016-5-19 下午2:59:27
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
	
}
