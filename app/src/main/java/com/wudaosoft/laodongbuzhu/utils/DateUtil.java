/**
 *    Copyright 2009-2017 Wudao Software Studio(wudaosoft.com)
 * 
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.wudaosoft.laodongbuzhu.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 
 * @author Changsoul.Wu
 */
public class DateUtil {
	
    public static final long ONE_MINUTE = 60 * 1000;
  
    public static final long ONE_HOUR = 60 * ONE_MINUTE;  
  
    public static final long ONE_DAY = 24 * ONE_HOUR;
    
    public static final long TWO_DAY = 2 * ONE_DAY;
    
    public static final long THREE_DAY = 3 * ONE_DAY;
	
	public static Date string2Date(String str) {
		return string2Date(str, "yyyy-MM-dd");
	}
	
	public static Date string2Date(String str, String format) {
		try {
			if (str == null)
				return null;
			return new SimpleDateFormat(format).parse(str);
		} catch (ParseException localParseException) {
		}
		return null;
	}

	public static Date string2Date(String str, String format, Date defaultValue) {
		try {
			if (str == null)
				return defaultValue;
			return new SimpleDateFormat(format).parse(str);
		} catch (ParseException localParseException) {

		}
		return defaultValue;
	}

	public static String date2String(Date date, String format) {
		if (date != null)
			return new SimpleDateFormat(format,Locale.CHINA).format(date);

		return null;
	}

	/**
	 * 判断当前时间是否与compare_time在同一天
	 * 
	 * @param compare_time
	 * @return
	 */
	public static boolean isSameDay(Date compare_time) {
		if (compare_time == null) {
			return false;
		}
		boolean isSameDay = false;
		// 当前时间
		Calendar current_time = Calendar.getInstance();
		int current_year = current_time.get(Calendar.YEAR);
		int current_day_of_year = current_time.get(Calendar.DAY_OF_YEAR);

		Calendar compare_datetime = Calendar.getInstance();
		compare_datetime.setTime(compare_time);
		int compare_year = compare_datetime.get(Calendar.YEAR);
		int compare__day_of_year = compare_datetime.get(Calendar.DAY_OF_YEAR);

		if (current_year == compare_year
				&& current_day_of_year == compare__day_of_year) {
			isSameDay = true;
		}
		return isSameDay;
	}

	public static String yesterDay() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.DATE, -1);
		return date2String(calendar.getTime(), "yyyy-MM-dd");
	}
	
	public static Calendar todayCa(){
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		return calendar;
	}
	
	public static Date todayToDate(){
		return todayCa().getTime();
	}
	
	public static Date nextDayToDate() {
		Calendar calendar = todayCa();
		calendar.add(Calendar.DATE, +1);
		return calendar.getTime();
	}
	
	public static Date firstDate(String date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(string2Date(date));
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		return calendar.getTime();
	}

	public static Date lastDate(String date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(string2Date(date));
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		return calendar.getTime();
	}
	
	public static Date firstToday() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		return calendar.getTime();
	}

	public static Date lastToday() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		return calendar.getTime();
	}

	public static String nextDay() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.DATE, +1);
		return date2String(calendar.getTime(), "yyyy-MM-dd");
	}

	public static String today() {
		return date2String(new Date(), "yyyy-MM-dd");
	}

	public static String date2String(Date date) {
		if (date == null)
			return null;

		return date2String(date, "yyyy-MM-dd HH:mm:ss");
	}
	
	public static String time2String(Date date) {
		if (date == null)
			return null;

		return date2String(date, "M-d H:m:s");
	}
	
	public static String long2MinuString(long time) {
		String str = "";
		int ss = 1000;
		int mi = ss * 60;
		int hh = mi * 60;

		long hour = (time) / hh;
		long minute = (time - hour * hh) / mi;
		long second = (time - hour * hh - minute * mi) / ss;

		if (hour > 0) {
			str = hour + "小时" + minute + "分" + second+ "秒";
		} else if(hour <= 0 && minute > 0){
			str = minute + "分" + second+ "秒";
		} else {
			str = second+ "秒";
		}
		return str;
	}

	public static String dateDiff2String(Date date, String postfix) {
		long longTime = date.getTime();
		long aa = System.currentTimeMillis() - longTime;
		if (postfix == null)
			postfix = "";

		if (aa <= 1000L)
			return "1秒" + postfix;

		long bb = aa / 1000L;
		if (bb < 60L)
			return bb + "秒" + postfix;

		long cc = bb / 60L;
		if (cc < 60L)
			return cc + "分钟" + postfix;

		long dd = cc / 60L;
		if (dd < 24L)
			return dd + "小时" + postfix;

		long ee = dd / 24L;
		if (ee < 7L)
			return ee + "天" + postfix;

		long ff = ee / 7L;
		if (ff <= 4L)
			return ff + "周" + postfix;

		return date2String(date);
	}

	public static String dateDiff2String(Date date, String postfix,
			String format) {
		long longTime = date.getTime();
		long aa = System.currentTimeMillis() - longTime;
		if (postfix == null)
			postfix = "";

		if (aa <= 1000L)
			return "1秒" + postfix;

		long bb = aa / 1000L;
		if (bb < 60L)
			return bb + "秒" + postfix;

		long cc = bb / 60L;
		if (cc < 60L)
			return cc + "分钟" + postfix;

		long dd = cc / 60L;
		if (dd < 24L)
			return dd + "小时" + postfix;

		long ee = dd / 24L;
		if (ee < 7L)
			return ee + "天" + postfix;

		long ff = ee / 7L;
		if (ff <= 4L)
			return ff + "周" + postfix;

		return date2String(date, format);
	}
	
	public static Date getChinaDate(){
		Calendar ca = Calendar.getInstance(Locale.CHINA);
		return ca.getTime();
	}

	public static Date getCurrYearLast(){
		Calendar ca = Calendar.getInstance();
		ca.set(Calendar.MONTH, Calendar.DECEMBER);
		ca.set(Calendar.DAY_OF_MONTH, 31);
		ca.set(Calendar.HOUR_OF_DAY, 23);
		ca.set(Calendar.MINUTE, 59);
		ca.set(Calendar.SECOND, 59);
		return ca.getTime();
	}
	
	public static Date getLastYearLast(){
		Calendar ca = Calendar.getInstance();
		ca.add(Calendar.YEAR, -1);
		ca.set(Calendar.MONTH, Calendar.DECEMBER);
		ca.set(Calendar.DAY_OF_MONTH, 31);
		ca.set(Calendar.HOUR_OF_DAY, 23);
		ca.set(Calendar.MINUTE, 59);
		ca.set(Calendar.SECOND, 59);
		return ca.getTime();
	}
	
	public static long nowToTarget(int hour,int minute){
		Calendar target = Calendar.getInstance(Locale.CHINA);
		target.set(Calendar.HOUR_OF_DAY, hour);
		target.set(Calendar.MINUTE, minute);
		target.set(Calendar.SECOND, 3);
		Calendar now = Calendar.getInstance();
		if(now.before(target)){
			return target.getTimeInMillis();
		}else{
			target.add(Calendar.DAY_OF_MONTH, 1);
			return target.getTimeInMillis();
		}
	}
	
	public static void main(String[] args) {
		System.out.println(DateUtil.yesterDay());
		System.out.println(DateUtil.today());
		System.out.println(DateUtil.todayToDate());
		System.out.println(DateUtil.nextDayToDate());
		System.out.println(date2String(DateUtil.todayToDate(),"yyyy-MM-dd HH:mm:ss"));
		System.out.println(date2String(DateUtil.firstDate("2013-5-6"),"yyyy-MM-dd HH:mm:ss"));
		System.out.println(date2String(DateUtil.lastDate("2013-5-6"),"yyyy-MM-dd HH:mm:ss"));
		System.out.println(getChinaDate());
		System.out.println(date2String(getLastYearLast()));
	}
}
