package com.ecnu.sei.manuzhang.chart;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import com.google.gson.Gson;


public class Util {

	public static final String[] CATEGORIES = 
		{"快乐", "悲伤", "惊奇", "愤怒", "厌恶", "恐惧", "其他"};

	private static Map<String, String> provinceCodeMap = new HashMap<String, String>();

	public static final Gson gson = new Gson();

	public static String writeColumns(List<Column> colList) {
		return gson.toJson(colList);
	}

	public static String writeRows(List<Row> rowList) {
		return gson.toJson(rowList);
	}

	public static Date toDate(String src) {
		String[] parts = src.split(" ");
		int year = Integer.valueOf(parts[parts.length - 1]);
		int month = Integer.valueOf(getMonth(parts[1])) - 1;
		int day = Integer.valueOf(parts[2]);
		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month, day, 0, 0, 0);
		return calendar.getTime();
	}

	// extract year, month, day_of_month from "Tue Feb 19 00:00:00 UTC 2013"
	public static String toJSDate(String src) {
		StringBuilder sb = new StringBuilder();
		sb.append("Date(");

		String[] parts = src.split(" ");
		String year = parts[parts.length - 1];
		String date = parts[2];
		String month = getMonth(parts[1]);
		sb.append(year + ",");
		sb.append(month + ",");	    
		sb.append(date + ")");

		return sb.toString();
	}

	public static String getMonth(String src) {
		String month = null;
		if (src.equals("Jan")) {
			month = "1";
		} else if (src.equals("Feb")) {
			month = "2";
		} else if (src.equals("Mar")) {
			month = "3";
		} else if (src.equals("Apr")) {
			month = "4";
		} else if (src.equals("May")) {
			month = "5";
		} else if (src.equals("Jun")) {
			month = "6";
		} else if (src.equals("Jul")) {
			month = "7";
		} else if (src.equals("Aug")) {
			month = "8";
		} else if (src.equals("Sep")) {
			month = "9";
		} else if (src.equals("Oct")) {
			month = "10";
		} else if (src.equals("Nov")) {
			month = "11";
		} else if (src.equals("Dec")) {
			month = "12";
		}
		return month;

	}

	public static String getCode(String province) {
		if (provinceCodeMap.isEmpty()) {
			loadCode();
		}
		return provinceCodeMap.get(province);
	}

	private static void loadCode() {
		provinceCodeMap.put("北京", "CN-11");
		provinceCodeMap.put("重庆", "CN-50");
		provinceCodeMap.put("上海", "CN-31");
		provinceCodeMap.put("天津", "CN-12");
		provinceCodeMap.put("安徽", "CN-34");
		provinceCodeMap.put("福建", "CN-35");
		provinceCodeMap.put("甘肃", "CN-62");
		provinceCodeMap.put("广东", "CN-44");
		provinceCodeMap.put("贵州", "CN-52");
		provinceCodeMap.put("海南", "CN-46");
		provinceCodeMap.put("河北", "CN-13");
		provinceCodeMap.put("黑龙江", "CN-23");
		provinceCodeMap.put("河南", "CN-41");
		provinceCodeMap.put("湖北", "CN-42");
		provinceCodeMap.put("湖南", "CN-43");
		provinceCodeMap.put("江苏", "CN-32");
		provinceCodeMap.put("江西", "CN-26");
		provinceCodeMap.put("吉林", "CN-22");
		provinceCodeMap.put("辽宁", "CN-21");
		provinceCodeMap.put("青海", "CN-63");
		provinceCodeMap.put("陕西", "CN-61");
		provinceCodeMap.put("山东", "CN-37");
		provinceCodeMap.put("山西", "CN-14");
		provinceCodeMap.put("四川", "CN-51");
		provinceCodeMap.put("台湾", "CN-71");
		provinceCodeMap.put("云南", "CN-53");
		provinceCodeMap.put("浙江", "CN-33");
		provinceCodeMap.put("广西", "CN-45");
		provinceCodeMap.put("内蒙古", "CN-15");
		provinceCodeMap.put("宁夏", "CN-64");
		provinceCodeMap.put("新疆", "CN-65");
		provinceCodeMap.put("西藏", "CN-54");
		provinceCodeMap.put("香港", "CN-91");
		provinceCodeMap.put("澳门", "CN-92");
		provinceCodeMap.put("海外", "KR");
		provinceCodeMap.put("其他", "JP");
	}
}
