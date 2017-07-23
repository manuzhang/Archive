package com.ecnu.sei.manuzhang.chart;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class TestColumn {

	@Test
	public void testAsJSON() {
		Column.Builder builder = new Column.Builder("string");
		Column col1 = builder.setId("date")
		                     .setLabel("date")
		                     .build();
		
		System.out.println(col1.asJSONObject());
		
		builder = new Column.Builder("number");
		Column col2 = builder.setId("Sold Pencils")
				             .setLabel("Sold Pencils per day")
				             .build();
		
		System.out.println(col2.asJSONObject());
		
		List<Column> colList = new ArrayList<Column>();
		colList.add(col1);
		colList.add(col2);
		System.out.println(Util.writeColumns(colList));
	}
}
