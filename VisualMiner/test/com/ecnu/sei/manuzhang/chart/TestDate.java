package com.ecnu.sei.manuzhang.chart;

import java.text.ParseException;

import junit.framework.Assert;

import org.junit.Test;

public class TestDate {
	@Test
	public void testJSDate() throws ParseException {
		String src = "Tue Feb 19 00:00:00 UTC 2013";
	//	String exp = "Tue Feb 19 08:00:00 CST 2013";
		String jsDate = "Date(2013,2,19)";
		Assert.assertEquals(jsDate, Util.toJSDate(src));
		//Assert.assertEquals(exp, Util.toDate(src));
		
	}
}
