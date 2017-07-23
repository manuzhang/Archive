package com.ecnu.sei.manuzhang.chart;

import org.junit.Test;

public class TestCell {
	
	@Test
	public void testAsJSON() {
		Cell cell = new Cell();
		System.out.println(cell.asJSONObject());
		
		cell.setF("a").setV("1");
		
		System.out.println(cell.asJSONObject());
	}
}
