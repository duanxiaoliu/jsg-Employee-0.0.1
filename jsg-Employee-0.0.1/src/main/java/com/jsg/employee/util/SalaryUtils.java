package com.jsg.employee.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.jsg.employee.model.Employee;

/**
 * 
* @ClassName: SalaryUtils 
* @Description: TODO(计算员工薪资工具类) 
* @author duanws
* @date 2016-7-18 下午4:01:11 
*
 */
public class SalaryUtils {
	/**
     * <p>Description:    [计算两个日期之间的天数，支持跨年]</p>  
     * @param d1  
     * @param d2  
     * @return 
     */  
    public int getDaysBetween(Calendar d1,Calendar d2){
        if (d1.after(d2)){ // swap dates so that d1 is start and d2 is end
            Calendar swap = d1;
            d1 = d2;
            d2 = swap;
        }
        int days  = d2.get(Calendar.DAY_OF_YEAR) - d1.get(Calendar.DAY_OF_YEAR);
        int y2 = d2.get(Calendar.YEAR);
        if (d1.get(Calendar.YEAR) != y2){
            d1 = (Calendar) d1.clone();
            do{
                days += d1.getActualMaximum(Calendar.DAY_OF_YEAR);
                d1.add(Calendar.YEAR, 1);
            }
            while (d1.get(Calendar.YEAR) != y2);
        }
        return days;
    }
  
    /**
     * <p>Description:    [计算 两个日期之间的工作日天数，小日期的当天也计算在内]</p>
     * @param d1  
     * @param d2  
     * @return 
     */  
    public int getWorkingDay(Calendar d1,Calendar d2){
    	d1.add(Calendar.DATE, -1);//转正当天也算作转正日期，所以需要提前一天
        int result = -1;
        if (d1.after(d2)){
            java.util.Calendar swap = d1;
            d1 = d2;
            d2 = swap;
        }
        int charge_start_date = 0;  //开始日期的日期偏移量  
        int charge_end_date = 0;   //结束日期的日期偏移量  
        //日期不在同一个日期内
        int stmp;
        int etmp;
        stmp = 7 - d1.get(Calendar.DAY_OF_WEEK);
        etmp = 7 - d2.get(Calendar.DAY_OF_WEEK);
        if (stmp != 0 && stmp != 6){
            charge_start_date = stmp - 1; // 开始日期为星期六和星期日时偏移量为 0
        }
        if (etmp != 0 && etmp != 6){
            charge_end_date = etmp - 1; // 结束日期为星期六和星期日时偏移量为 0
        }
        result = (getDaysBetween(this.getNextMonday(d1),this.getNextMonday(d2))/7)*5+charge_start_date-charge_end_date;
        return result;
    }
    /**
     * <p>Description:    [获得日期的下一个星期一的日期]</p>  
     * @param date
     * @return
     */
    public Calendar getNextMonday(Calendar date){
        Calendar result = null;
        result = date;
        do{
            result = (Calendar) result.clone();
            result.add(Calendar.DATE, 1);
        }
        while (result.get(Calendar.DAY_OF_WEEK) != 2);
        return result;
    }  
      
    /**
     * <p>Description:    [方法功能中文描述]</p>  
     * @param d1
     * @param d2
     * @return
     */  
    public int getHolidays(Calendar d1, Calendar d2){
        return this.getDaysBetween(d1, d2) - this.getWorkingDay(d1, d2);
    }
  
   
    public static void main(String[] args){
    	try{
	    	String strDateStart = "2016-5-1"; //起始日期  
	        String strDateEnd = "2016-5-31"; //截至日期
	        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
	        Date date_start = sdf.parse(strDateStart);  
	        Date date_end = sdf.parse(strDateEnd);
	        
	        Calendar d1 = Calendar.getInstance();
	    	Calendar d2 = Calendar.getInstance();
	    	d1.setTime(date_start);
	    	d2.setTime(date_end);
	    	
	    	SalaryUtils su = new SalaryUtils();
	    	
	    	System.out.println(su.getWorkingDay(d1, d2));
    	}catch(Exception ex){
    		ex.printStackTrace();
    	}
    }
    //合算工资时，查看员工转正状态
	public static String getFullMember(Employee employee,String salaryDateStr){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
		SimpleDateFormat sdf2=new SimpleDateFormat("yyyy-MM");
		SimpleDateFormat sdf3=new SimpleDateFormat("yyyy-MM-dd");
		Date salaryDate = new Date();
		try {
			salaryDate = sdf2.parse(salaryDateStr);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String entryDateStr1 = employee.getEntryTime();
		Date entryDate = null;
		try {
			entryDate = sdf3.parse(entryDateStr1);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		Calendar ca = Calendar.getInstance();
		ca.setTime(entryDate);
		ca.add(Calendar.MONTH, 3);
		
		Date cEntryDate = ca.getTime();
		
		String entryDateStr = sdf.format(cEntryDate);
		int salaryDateInt = Integer.parseInt(sdf.format(salaryDate));
		int fullDateInt = Integer.parseInt(sdf.format(cEntryDate));
		if(fullDateInt > salaryDateInt){
			return "未转正";
		}else if(fullDateInt == salaryDateInt){
			return "当月转正";
		}else{
			return "已转正";
		}
	}
}  


