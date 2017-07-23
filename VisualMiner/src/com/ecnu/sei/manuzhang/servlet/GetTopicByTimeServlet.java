package com.ecnu.sei.manuzhang.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ecnu.sei.manuzhang.chart.Cell;
import com.ecnu.sei.manuzhang.chart.Column;
import com.ecnu.sei.manuzhang.chart.Data;
import com.ecnu.sei.manuzhang.chart.Row;
import com.ecnu.sei.manuzhang.chart.Util;
import com.ecnu.sei.manuzhang.store.Store;
import com.google.appengine.api.datastore.Entity;

@SuppressWarnings("serial")
public class GetTopicByTimeServlet extends HttpServlet {

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("application/json; charset=utf-8");
		resp.setHeader("Cache-Control", "no-cache");

		String kind = "topic_by_time";
		String name = "topic";
		String value = req.getParameter(name);
		PrintWriter out = resp.getWriter();
		out.print(writeJSON(Store.listEntities(kind, name, value)));
	}

	private String writeJSON(Iterable<Entity> entities) {
		Data<Date> data = new Data<Date>();
		
		Column date = new Column.Builder(Column.TYPE_DATE)
		.setId("date")
		.setLabel("日期")
		.build();

		Column count = new Column.Builder(Column.TYPE_NUMBER)
		.setId("count")
		.setLabel("讨论数")
		.build();
		data.addColumn(date).addColumn(count);

		TreeSet<Row<Date>> rowSet = new TreeSet<Row<Date>>();
		for (Entity result : entities) {
			Map<String, Object> properties = result.getProperties();


			List<Cell> cells = new ArrayList<Cell>();
			String d = String.valueOf(String.valueOf(properties.get("date")));
			Date day = Util.toDate(d);
			Cell dateCell = new Cell(Util.toJSDate(d) , null, null);
			Cell countCell = new Cell(Integer.parseInt(String.valueOf(properties.get("count"))), null, null);
			cells.add(dateCell);
			cells.add(countCell);
			Row<Date> row = new Row<Date>(cells, day);
			rowSet.add(row);
		}

		data.setRows(rowSet);
		return data.asJSONObject();
	}
}
