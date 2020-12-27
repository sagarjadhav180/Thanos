package com.convirza.core.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.text.ParseException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {

	public static String getTodayDate() {
		Date date = new Date();
		SimpleDateFormat dateFormatter = new SimpleDateFormat("YYYY'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'");
		String todayDate = dateFormatter.format(date).toString();
		return todayDate;
	}

	public static String getDateFromDateTime(String datetime) {
		SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
		try {
			String date = dateFormatter.format(dateFormatter.parse(datetime));
			return date;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return "";
	}

	public static String getDateTimeFromDate(String date) {
		SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy'-'MM'-'dd");
		try {
			return dateFormatter.format(dateFormatter.parse(date)) + "T00:00:00.000Z";
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return "";
	}

	public static String convertISOWithoutTchar(String datetime) {
		SimpleDateFormat dateFormatterWithT = new SimpleDateFormat("yyyy'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'");
		SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy'-'MM'-'dd HH':'mm':'ss");
		try {
			Date date = dateFormatterWithT.parse(datetime);
			int milisecs = Integer.parseInt(datetime.substring(20,datetime.length()-1));
			if (milisecs > 500)
				date.setSeconds(date.getSeconds()+1);
			return dateFormatter.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String convertISOWithTchar(String datetime) {
		SimpleDateFormat dateFormatterWithT = new SimpleDateFormat("yyyy'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'");
		SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy'-'MM'-'dd HH':'mm':'ss");
		try {
			Date date = dateFormatter.parse(datetime);
			return dateFormatterWithT.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String getFutureDate() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.DATE, 15);
		SimpleDateFormat dateFormatter = new SimpleDateFormat("YYYY'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'");
		String futureDate = dateFormatter.format(cal.getTime()).toString();
		return futureDate;
	}

	public static String getDate(String Format,String days){
		DateFormat dateFormat = new SimpleDateFormat(Format);
		
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, Integer.valueOf(days));
	    
		Date todate1 = cal.getTime();
	    String date = dateFormat.format(todate1);

	    System.out.println(date);
	    return date;
		
	}
	
	public static Date parseDateInFormat(String date, String format) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		Date formattedDate = null;
		try {
			formattedDate = dateFormat.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return formattedDate;
	}

	public static String convertISOTDateIntoFormat(String date, String format) {
		SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'");
		SimpleDateFormat outputDateFormat = new SimpleDateFormat(format);
		String formattedDate = null;
		try {
			formattedDate = outputDateFormat.format(inputDateFormat.parse(date));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return formattedDate;
	}

}
