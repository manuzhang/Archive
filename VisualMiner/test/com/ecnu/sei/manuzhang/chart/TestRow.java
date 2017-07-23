package com.ecnu.sei.manuzhang.chart;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class TestRow {
	@Test
	public void testAsJSON() {
		Cell cell = new Cell();
		cell.setF("a").setV("1");
		List<Cell> cellList = new ArrayList<Cell>();
		cellList.add(null);
		cellList.add(null);
		cellList.add(cell);
		Row<Long> row = new Row<Long>(cellList, Long.valueOf(String.valueOf(cell.getV())));
		System.out.println(row.asJSONObject());
		   
	}
}
