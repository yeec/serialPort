package bros.manage.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * 日期辅助类工具类，集中处理所有日期操作
 * @author WYC
 *
 */
public class DateUtil {
	
	/**
	 * 数据库存储的默认时间格式
	 */
	public static final String DEFAULT_TIME_FORMAT_DB = "yyyyMMddHHmmss";
	/**
	 * 默认时间格式(EN)
	 */
	public static final String DEFAULT_TIME_FORMAT_EN = "yyyy-MM-dd HH:mm:ss";
	/**
	 * 默认时间格式(中文)
	 */
	public static final String DEFAULT_TIME_FORMAT_CN = "yyyy年MM月dd日 HH:mm:ss";
	/**
	 * 默认日期格式(EN)
	 */
	public static final String DEFAULT_DATE_FORMAT_EN = "yyyy-MM-dd";
	/**
	 * 默认日期格式(中文)
	 */
	public static final String DEFAULT_DATE_FORMAT_CN = "yyyy年MM月dd日";
	/**
	 * 默认日期格式
	 */
	public static final String DEFAULT_DATE_FORMAT = "yyyyMMdd";
	
	/**
	 * 根据参数格式显示格式化日期
	 * 
	 * @param date
	 * @param format
	 *            String
	 * @return String
	 */
	public static String formatDate( String date, String format )
	{
		Date dt = null;
		SimpleDateFormat inFmt = null, outFmt = null;
		ParsePosition pos = new ParsePosition( 0 );
		if ( (date == null) || ("".equals( date.trim() )) )
			return "";
		try
		{
			if ( Long.parseLong( date ) == 0 )
				return "";
		}
		catch ( Exception nume )
		{
			return date;
		}
		try
		{
			switch ( date.trim().length() ) {
				case 14 :
					inFmt = new SimpleDateFormat( "yyyyMMddHHmmss" );
					break;
				case 12 :
					inFmt = new SimpleDateFormat( "yyyyMMddHHmm" );
					break;
				case 10 :
					inFmt = new SimpleDateFormat( "yyyyMMddHH" );
					break;
				case 8 :
					inFmt = new SimpleDateFormat( "yyyyMMdd" );
					break;
				case 6 :
					inFmt = new SimpleDateFormat( "yyyyMM" );
					break;
				default :
					return date;
			}
			if ( (dt = inFmt.parse( date, pos )) == null )
				return date;
			if ( (format == null) || ("".equals( format.trim() )) )
			{
				outFmt = new SimpleDateFormat( "yyyy年MM月dd日" );
			}
			else
			{
				outFmt = new SimpleDateFormat( format );
			}
			return outFmt.format( dt );
		}
		catch ( Exception ex )
		{
			return date;
		}
	}

	/**
	 * 重新格式化时间
	 * 
	 * @param datetime
	 *            原有时间字符串，如20050101
	 * @param oldFormat
	 *            原有时间字符串的格式，如20050101160145为"yyyyMMddHHmmss"
	 * @param newFormat
	 *            新的时间字符串的格式，如2005-01-01 16:01:45为"yyyy-MM-dd HH:mm:ss"
	 * @return String
	 */
	public static String convertTimeFormat( String datetime, String oldFormat, String newFormat )
	{
		SimpleDateFormat oldFmt = null, newFmt = null;
		oldFmt = new SimpleDateFormat( oldFormat );
		newFmt = new SimpleDateFormat( newFormat );
		Date date = null;
		try
		{
			date = oldFmt.parse( datetime );
			return newFmt.format( date );
		}
		catch ( ParseException ex )
		{
			
		}
		return datetime;
	}

	/**
	 * 取得指定时间间隔后的系统时间<br>
	 * 示例：<br>
	 * getDifferentTime( 1, 2, 3,"yyyyMMddHHmmss") <br>
	 * 使用yyyyMMddHHmmss格式输出1小时2分3秒后的系统时间<br>
	 * getDifferentTime( -24, 0, 0,"yyyyMMdd") <br>
	 * 使用yyyyMMdd格式输出1天前的系统日期<br>
	 * 
	 * @param hour
	 *            小时
	 * @param minute
	 *            分钟
	 * @param second
	 *            秒
	 * @param timeFormat
	 *            格式化
	 * @return String
	 */
	public static String getDifferentTime( int hour, int minute, int second, String timeFormat )
	{
		String format = (timeFormat == null) ? DEFAULT_TIME_FORMAT_DB : timeFormat;
		GregorianCalendar calendar = (GregorianCalendar) Calendar.getInstance();
		calendar.add( Calendar.HOUR, hour );
		calendar.add( Calendar.MINUTE, minute );
		calendar.add( Calendar.SECOND, second );
		SimpleDateFormat formatter = new SimpleDateFormat( format );
		return formatter.format( calendar.getTime() );
	}

	/**
	 * 取得若干天前/后的系统日期
	 * 
	 * @param days
	 *            与现在相隔的日数，正数为当前日期之后，负数为当前日期之前
	 * @param timeFormat
	 *            格式化
	 * @return String
	 */
	public static String getDifferentDate( int days, String timeFormat )
	{
		return getDifferentTime( 24 * days, 0, 0, timeFormat );
	}
	
	/**
	 * 取得格式化的指定时间
	 * 
	 * @param timeFormat
	 *            时间：yyyyMMddHHmmss；日期：yyyyMMdd
	 * @param date           
	 * @return String
	 */
	public static String formatDate( Date date, String timeFormat)
	{
		String format = (timeFormat == null) ? DEFAULT_DATE_FORMAT_EN : timeFormat;
		SimpleDateFormat formatter = new SimpleDateFormat( format );
		if(null==date){
			Calendar calendar = Calendar.getInstance();
			date = calendar.getTime();
		}
		return formatter.format( date );
	}

	/**
	 * 取得格式化的服务器时间
	 * 
	 * @param timeFormat
	 *            时间：yyyyMMddHHmmss；日期：yyyyMMdd
	 * @return String
	 */
	public static String getServerTime(String timeFormat) {
		String format = (timeFormat == null) ? DEFAULT_TIME_FORMAT_DB : timeFormat;
		Calendar calendar = Calendar.getInstance();
		Date date = calendar.getTime();
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		return formatter.format(date);
	}

	/**
	 * 取得本月第一天
	 * 
	 * @return String
	 */
	public static String getThisMonthFirstDay(String timeFormat) {
		String format = (timeFormat == null) ? DEFAULT_TIME_FORMAT_DB : timeFormat;
		Calendar cal = Calendar.getInstance();
		Calendar f = (Calendar) cal.clone();
		f.clear();
		f.set(Calendar.YEAR, cal.get(Calendar.YEAR));
		f.set(Calendar.MONTH, cal.get(Calendar.MONTH));
		String firstday = new SimpleDateFormat(format).format(f.getTime());
		return firstday;
	}

	/**
	 * 取得本月最后一天
	 * 
	 * @return String
	 */
	public static String getThisMonthLastDay(String timeFormat) {
		String format = (timeFormat == null) ? DEFAULT_TIME_FORMAT_DB : timeFormat;
		Calendar cal = Calendar.getInstance();
		Calendar l = (Calendar) cal.clone();
		l.clear();
		l.set(Calendar.YEAR, cal.get(Calendar.YEAR));
		l.set(Calendar.MONTH, cal.get(Calendar.MONTH) + 1);
		l.set(Calendar.MILLISECOND, -1);
		String lastday = new SimpleDateFormat(format).format(l.getTime());
		return lastday;
	}

	/**
	 * 获得本季度 第一天
	 * 
	 * @return String
	 */
	public static String getThisSeasonFirstDay(String timeFormat) {
		String format = (timeFormat == null) ? DEFAULT_TIME_FORMAT_DB : timeFormat;
		int month = Integer.parseInt(getServerTime("MM"));
		return getSeasonFirstDay(month, format);
	}

	/**
	 * 获得本季度 最后一天
	 * 
	 * @return String
	 */
	public static String getThisSeasonLastDay(String timeFormat) {
		String format = (timeFormat == null) ? DEFAULT_TIME_FORMAT_DB : timeFormat;
		int month = Integer.parseInt(getServerTime("MM"));
		return getSeasonLastDay(month, format);
	}

	/**
	 * 获得季度 第一天
	 * 
	 * @param month
	 *            MM
	 * @return String
	 */
	public static String getSeasonFirstDay(int month, String timeFormat) {

		String format = (timeFormat == null) ? DEFAULT_TIME_FORMAT_DB : timeFormat;

		int array[][] = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 }, { 10, 11, 12 } };
		int season = 1;
		if (month >= 1 && month <= 3) {
			season = 1;
		}
		if (month >= 4 && month <= 6) {
			season = 2;
		}
		if (month >= 7 && month <= 9) {
			season = 3;
		}
		if (month >= 10 && month <= 12) {
			season = 4;
		}
		int start_month = array[season - 1][0];

		Calendar cal = Calendar.getInstance();
		Calendar f = (Calendar) cal.clone();
		f.clear();
		f.set(Calendar.YEAR, cal.get(Calendar.YEAR));
		f.set(Calendar.MONTH, start_month - 1);
		String firstday = new SimpleDateFormat(format).format(f.getTime());
		return firstday;
	}

	/**
	 * 获得季度 最后一天
	 * 
	 * @param month
	 *            MM
	 * @return String
	 */
	public static String getSeasonLastDay(int month, String timeFormat) {

		String format = (timeFormat == null) ? DEFAULT_TIME_FORMAT_DB : timeFormat;

		int array[][] = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 }, { 10, 11, 12 } };
		int season = 1;
		if (month >= 1 && month <= 3) {
			season = 1;
		}
		if (month >= 4 && month <= 6) {
			season = 2;
		}
		if (month >= 7 && month <= 9) {
			season = 3;
		}
		if (month >= 10 && month <= 12) {
			season = 4;
		}
		int end_month = array[season - 1][2];

		Calendar cal = Calendar.getInstance();
		Calendar f = (Calendar) cal.clone();
		f.clear();

		int end_days = getLastDayOfMonth(cal.get(Calendar.YEAR), end_month);

		f.set(Calendar.YEAR, cal.get(Calendar.YEAR));
		f.set(Calendar.MONTH, end_month - 1);
		f.set(Calendar.DATE, end_days);

		f.add(Calendar.DATE, 1);
		f.add(Calendar.SECOND, -1);

		String firstday = new SimpleDateFormat(format).format(f.getTime());
		return firstday;
	}

	/**
	 * 获得本年的第一天
	 * 
	 * @param month
	 *            MM
	 * @return String
	 */
	public static String getThisYearFirstDay(String timeFormat) {

		String format = (timeFormat == null) ? DEFAULT_TIME_FORMAT_DB : timeFormat;
		Calendar cal = Calendar.getInstance();
		Calendar f = (Calendar) cal.clone();
		f.clear();
		f.set(Calendar.YEAR, cal.get(Calendar.YEAR));
		String firstday = new SimpleDateFormat(format).format(f.getTime());
		return firstday;
	}

	/**
	 * 获得本年最后一天
	 * 
	 * @param month
	 *            MM
	 * @return String
	 */
	public static String getThisYearLastDay(String timeFormat) {

		String format = (timeFormat == null) ? DEFAULT_TIME_FORMAT_DB : timeFormat;
		Calendar cal = Calendar.getInstance();
		Calendar f = (Calendar) cal.clone();
		f.clear();
		f.set(Calendar.YEAR, cal.get(Calendar.YEAR) + 1);
		f.add(Calendar.SECOND, -1);
		String lastday = new SimpleDateFormat(format).format(f.getTime());
		return lastday;
	}

	/**
	 * 获取某年某月的最后一天
	 * 
	 * @param year
	 *            年
	 * @param month
	 *            月
	 * @return 最后一天
	 */
	public static int getLastDayOfMonth(int year, int month) {
		if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8
				|| month == 10 || month == 12) {
			return 31;
		}
		if (month == 4 || month == 6 || month == 9 || month == 11) {
			return 30;
		}
		if (month == 2) {
			if (isLeapYear(year)) {
				return 29;
			} else {
				return 28;
			}
		}
		return 0;
	}

	/**
	 * 是否闰年
	 * 
	 * @param year
	 *            年
	 * @return
	 */
	public static boolean isLeapYear(int year) {
		return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
	}

	/**
	 * 取得beginDayTime
	 * 
	 * @return String
	 */
	public static String getDayBegin(String inputStringDate) {
		String outStringDate = inputStringDate.replaceAll("-", "");

		if (outStringDate.length() == 8) {
			outStringDate = outStringDate + "000000";
			return outStringDate;
		} else {
			return inputStringDate;
		}
	}

	/**
	 * 取得endDayTime
	 * 
	 * @return String
	 */
	public static String getDayEnd(String inputStringDate) {
		String outStringDate = inputStringDate.replaceAll("-", "");
		if (outStringDate.length() == 8) {
			outStringDate = outStringDate + "235959";
			return outStringDate;
		} else {
			return inputStringDate;
		}
	}

	/**
	 * 
	 * @Title: getFormatMillionSeconds
	 * @Description: TODO(将时间按照格式转换为毫秒格式)
	 * @param time
	 *            时间
	 * @param timeFormat
	 *            时间格式
	 * @return long 毫秒时间
	 * @throws Exception
	 */
	public static long getFormatMillionSeconds(String time, String timeFormat) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat(timeFormat);
		long millionSeconds = sdf.parse(time).getTime(); // 毫秒
		return millionSeconds;
	}

	/**
	 * 
	 * @Title: dateTrueOrFalse
	 * @Description: TODO(判断日期格式是否正确)
	 * @param @param date
	 * @param @return
	 * @param @throws Exception
	 * @return boolean
	 * @throws
	 */
	public static boolean dateTrueOrFalse(String date, String format) {
		try {
			DateFormat f = new java.text.SimpleDateFormat(format);
			f.parse(date);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 
	 * @Title: getFullDateWeekTime
	 * @Description: TODO(查询日期是星期几)
	 * @param @param sDate
	 * @param @param formater
	 * @param @return
	 * @return String
	 * @throws
	 */
	public static String getFullDateWeekTime(String sDate, String formater) {
		try {
			SimpleDateFormat format = new SimpleDateFormat(formater);
			Date date = format.parse(sDate);
			
			Calendar cal = Calendar.getInstance();
	        cal.setTime(date);
	 
	        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
	        if (w < 0)
	            w = 0;
	        if(w==0)
	        	w=7;
	        
	        return w+"";

		} catch (Exception ex) {
			System.out.println("TimeUtil  getFullDateWeekTime"
					+ ex.getMessage());
			return "";
		}
	}
	
	/**
	 * 把字符串转换成Calendar
	 */
	public static Calendar getCalendar(String date, String format){
		SimpleDateFormat formatter = new SimpleDateFormat (format);
		ParsePosition pos = new ParsePosition(0);
		Date first = formatter.parse(date,pos);
		Calendar c = Calendar.getInstance();
		c.setTime(first);
		return c;
	}
	
	/**
	 * 
	 * @Title: getNextDate 
	 * @Description: TODO(根据执行日期和周期类型取下一个执行日) 
	 * @param nextDate 执行日期
	 * @param planCycle 周期类型
	 * @return String  下一个执行日
	 * @throws
	 */
	public static String getNextDate(String nextDate, String planCycle){
		Calendar temp = getCalendar(nextDate, DEFAULT_DATE_FORMAT);
		
		/*if(planCycle.equals("1")){//每周
			temp.add(Calendar.DATE,7);
		}else if(planCycle.equals("2")){//每月
			temp.add(Calendar.MONTH,1);
		}else if(planCycle.equals("3")){//每两个月
			temp.add(Calendar.MONTH,2);
		}else if(planCycle.equals("4")){//每季度
			temp.add(Calendar.MONTH,3);
		}else if(planCycle.equals("5")){//每半年
			temp.add(Calendar.MONTH,6);
		}else if(planCycle.equals("6")){//每年
			temp.add(Calendar.YEAR,1);
		}*/
		if(planCycle.equals("1")){//每天
			temp.add(Calendar.DATE,1);
		}else if(planCycle.equals("2")){//每周
			temp.add(Calendar.DATE,7);
		}else if(planCycle.equals("3")){//每月
			temp.add(Calendar.MONTH,1);
		}else if(planCycle.equals("4")){//每两个月
			temp.add(Calendar.MONTH,2);
		}else if(planCycle.equals("5")){//每季度
			temp.add(Calendar.MONTH,3);
		}else if(planCycle.equals("6")){//每半年
			temp.add(Calendar.MONTH,6);
		}else if(planCycle.equals("7")){//每年
			temp.add(Calendar.YEAR,1);
		}
		
		//格式转换
		SimpleDateFormat formatter = new SimpleDateFormat (DEFAULT_DATE_FORMAT);
		nextDate = formatter.format(temp.getTime());
		
		return nextDate;
	}
	
	/**
	 * 取指定日期月份
	 */
	public static int getCalendarMonth(String date, String format){
		Calendar temp = getCalendar(date, format);
		Date dates = temp.getTime();
		SimpleDateFormat formatter = new SimpleDateFormat("MM");
		return Integer.valueOf(formatter.format(dates));
	}
	
	/**
	 * 取当前月份
	 */
	public static int getCalendarMonth(){
		return Integer.valueOf(getServerTime("MM"));
    }

	/**
	 * 取当前日
	 */
    public static int getCalendarDate(){
    	return Integer.valueOf(getServerTime("dd"));
    }

    /**
	 * 取当前小时
	 */
    public static int getCalendarHour(){
        return Integer.valueOf(getServerTime("HH"));
    }
	/**
	 * 
	 * @Title: addDate 
	 * @Description: TODO(日期增加) 
	 * @param @param nowDate
	 * @param @param addDay
	 * @param @return
	 * @return String
	 * @throws
	 */
	public static String addDate(String nowDate,int addDay){
		Calendar temp = getCalendar(nowDate, DEFAULT_DATE_FORMAT);
		temp.add(Calendar.DATE,addDay);
		//格式转换
		SimpleDateFormat formatter = new SimpleDateFormat (DEFAULT_DATE_FORMAT);
		
		return formatter.format(temp.getTime());
	}
	
	/**
	 * 
	 * @Title: addMonth 
	 * @Description: TODO(月增加) 
	 * @param @param nowDate
	 * @param @param addMonth
	 * @param @return
	 * @return String
	 * @throws
	 */
	public static String addMonth(String nowDate,int addMonth){
		Calendar temp = getCalendar(nowDate, DEFAULT_DATE_FORMAT);
		temp.add(Calendar.MONTH,addMonth);
		//格式转换
		SimpleDateFormat formatter = new SimpleDateFormat (DEFAULT_DATE_FORMAT);
		
		return formatter.format(temp.getTime());
	}
	
	/**
	 * 比较开始时间和结束时间
	 * @param startDate
	 * @param endDate
	 * @param dateFmt
	 * @return
	 */
	public static boolean compareStartAndEndDate(String startDate,String endDate,String dateFmt){
		try{
			Calendar c1 = getCalendar(startDate, dateFmt);		
			Calendar c2 = getCalendar(endDate, dateFmt);
			if(c1.compareTo(c2)<=0)
				return true;
			else
				return false;
		}catch(Exception e){
			return false;
		}
	}
	
	/**
	 * 获取当前日期前/后若干天的日期
	 * 
	 * @param relativeDays 天数,如:-1(取昨天日期), 1(取明天日期)
	 * @param timeFormat 格式化标志
	 * @return String
	 */
	public static String getRelativeDate(String relativeDays, String timeFormat){
		int days = Integer.parseInt(relativeDays);
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, days);
		return (new SimpleDateFormat(timeFormat)).format(calendar.getTime());
	}
	
	/**
	 * 
	 * @Title: getDateInterval 
	 * @Description: TODO(计算两个日期相差天数) 
	 * @param beginDate  开始日期
	 * @param endDate    结束日期
	 * @param dateFmt    日期格式
	 * @return String
	 * @throws Exception
	 */
	public static String getDateInterval(String beginDate, String endDate, String dateFmt) throws Exception{
		java.text.SimpleDateFormat format = new java.text.SimpleDateFormat(dateFmt);   
		java.util.Date date1= format.parse(beginDate);   
		java.util.Date date2= format.parse(endDate);   
		long dateInterval = (date2.getTime() - date1.getTime())/(24*60*60*1000);   
		return Long.toString(dateInterval);
	}
	
	/**
	* 数据库查询专用方法
	* 为时间字符串补6位
	* 仅支持两种输入格式
	* (1)、2011-10-01
	* (2)、20111001
	* (3)、不支持其他格式
	* 输出示例
	* 如果str=9，date=2011-10-01，则格式化为20111001999999
	* 如果str=0，date=20111001，则格式化为20111001000000
	* */
	static int seat = 6;//为时间字符串补6位
	public static String dateAdd14(String date,String str){
		if(date != null && !date.trim().equals("") && date.length() == 8 && str != null && !str.trim().equals("")){
			StringBuffer tmep = new StringBuffer();
			for(int i=0;i<seat;i++){
				tmep.append(str);
			}
			return date + tmep.toString();
		}
		if(date != null && !date.trim().equals("") && date.length() == 10 && str != null && !str.trim().equals("")){
			StringBuffer tmep = new StringBuffer();
			for(int i=0;i<seat;i++){
				tmep.append(str);
			}
			String dateTemp[] = date.split("-");
			if(dateTemp != null && dateTemp.length == 3){
				return dateTemp[0] + dateTemp[1] + dateTemp[2] + tmep;
			}else{
				return "";//"日期格式不正确";
			}
		}
		return "";//"日期格式不正确";
	}
	
	/** 
	* @Title: 时间格式处理 
	* @param date 只接受10位的时间格式数据
	* 输入格式必须为：2011-11-09
	* 输出格式为：20111109
	*/ 
	public static String getTimeFormat8(String date){
		if(date != null && !date.trim().equals("") && date.length() == 10){
			String dates[] = date.split("-");
			if(dates != null && dates.length == 3){
				return dates[0] + dates[1] + dates[2];
			}
			return date;
		}
		return date;
	}
	
	/** 
	* @Title: 时间格式处理 
	* @param date 只接受8位的时间格式数据
	* 输入格式为：20111109
	* 输出格式必须为：2011-11-09
	*/ 
	public static String getTimeFormat10(String date){
		if(date != null && !date.trim().equals("") && date.length() == 8){
			return date.substring(0,4) + "-" + date.substring(4,6) + "-" + date.substring(6,8);
		}
		return date;
	}
	
	/**
	* 为月份数据补0使用
	* @author 赵斌
	* @date 2012-02-01
	* */
	public static String formatMonth(int month){
		String monthStr = "";
		if(month < 10){
			monthStr = "0" + String.valueOf(month);
		}else{
			monthStr = String.valueOf(month);
		}
		return monthStr;
	}

}
