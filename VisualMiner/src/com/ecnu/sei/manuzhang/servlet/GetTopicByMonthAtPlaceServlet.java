package com.ecnu.sei.manuzhang.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ecnu.sei.manuzhang.chart.Cell;
import com.ecnu.sei.manuzhang.chart.Column;
import com.ecnu.sei.manuzhang.chart.Row;
import com.ecnu.sei.manuzhang.chart.Util;
import com.ecnu.sei.manuzhang.store.Store;
import com.google.appengine.api.datastore.Entity;
import com.google.gson.Gson;

@SuppressWarnings("serial")
public class GetTopicByMonthAtPlaceServlet extends HttpServlet {
	
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("application/json; charset=utf-8");
		resp.setHeader("Cache-Control", "no-cache");

		// String kind = "topic_by_time_at_place";
		String kind = "topic_by_month_at_place";
		String name = "topic";
		String value = req.getParameter(name);
		PrintWriter out = resp.getWriter();
		out.print(writeJSON(Store.listEntities(kind, name, value)));
	}

	private String writeJSON(Iterable<Entity> entities) {
		StringBuilder sb = new StringBuilder();
		Gson gson = new Gson();
		sb.append("{\"cols\":");
		List<Column> columnList = new ArrayList<Column>();
		Column place = new Column.Builder(Column.TYPE_STRING)
		.setId("place")
		.setLabel("地域")
		.build();

		Column count = new Column.Builder(Column.TYPE_NUMBER)
		.setId("count")
		.setLabel("讨论数")
		.build();
	
		
		columnList.add(place);
		columnList.add(count);

		sb.append(gson.toJson(columnList));
		
		TreeMap<Date, List<Row<Long>>> rowMap = new TreeMap<Date, List<Row<Long>>>();
		for (Entity result : entities) {
			Map<String, Object> properties = result.getProperties();

			List<Cell> cells = new ArrayList<Cell>();
			String province = String.valueOf(properties.get("place"));
			Cell placeCell = new Cell(Util.getCode(province) , province, null);
			Cell countCell = new Cell(Long.parseLong(String.valueOf(properties.get("count"))), null, null);
			String d = String.valueOf(properties.get("date"));
			cells.add(placeCell);
			cells.add(countCell);
			Date time = Util.toDate(d);
			Row<Long> row = new Row<Long>(cells, (long) 1);
			if (null == rowMap.get(time)) {
				rowMap.put(time, new ArrayList<Row<Long>>());
			}
			rowMap.get(time).add(row);
		}
		int i = 0;
		DateFormat formatter = new SimpleDateFormat("yyyy-MM");
		for (Date d : rowMap.keySet()) {
			sb.append(", \"" + i + "\":");
			sb.append("{\"time\":\"" + formatter.format(d) + "\",");
			sb.append("\"rows\":" + gson.toJson(rowMap.get(d)) + "}");
			i++;
		}
		sb.append(", \"length\":" + i);
		sb.append("}");
		return sb.toString();
	}

}
