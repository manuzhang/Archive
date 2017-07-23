package com.ecnu.sei.manuzhang.chart;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;


public class TestData {
	@Test
	public void testAsJSON() {
		Data<Long> data = new Data<Long>();
		System.out.println(data.asJSONObject());
		
		Column.Builder builder = new Column.Builder("date");	
		builder = new Column.Builder("number");
		Column col = builder.setId("Sold Pencils")
				            .setLabel("Sold Pencils per day")
				            .build();
				
		data.addColumn(col);	
		
		Cell cell1 = new Cell(30000, null, null);
		List<Cell> cells = new ArrayList<Cell>();
		cells.add(cell1);
		Row<Long> row1 = new Row<Long>(cells, Long.valueOf(String.valueOf(cell1.getV())));

		cells = new ArrayList<Cell>();
		Cell cell2 = new Cell(14045, null, null);
		cells.add(cell2);
		Row<Long> row2 = new Row<Long>(cells, Long.valueOf(String.valueOf(cell2.getV())));
		
		cells = new ArrayList<Cell>();
		Cell cell3 = new Cell(20000, null, null);
		cells.add(cell3);
		Row<Long> row3 = new Row<Long>(cells, Long.valueOf(String.valueOf(cell3.getV())));
		data.addRow(row1).addRow(row2).addRow(row3);
		
		System.out.println(data.asJSONObject());
    }
}
