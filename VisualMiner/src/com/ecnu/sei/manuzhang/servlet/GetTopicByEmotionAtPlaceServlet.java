package com.ecnu.sei.manuzhang.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
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
import com.ecnu.sei.manuzhang.store.Store;
import com.google.appengine.api.datastore.Entity;

@SuppressWarnings("serial")
public class GetTopicByEmotionAtPlaceServlet extends HttpServlet {

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("application/json; charset=utf-8");
		resp.setHeader("Cache-Control", "no-cache");

		String kind = "topic_by_emotion＿at_place";
		String name = "topic";
		String value = req.getParameter(name);
		PrintWriter out = resp.getWriter();
		out.print(writeJSON(Store.listEntities(kind, name, value)));
	}
	
	private String writeJSON(Iterable<Entity> entities) {
		Data<Long> data = new Data<Long>();
		Column emotion = new Column.Builder(Column.TYPE_STRING)
		.setId("emotion")
		.setLabel("情绪")
		.build();

		Column place = new Column.Builder(Column.TYPE_STRING)
		.setId("place")
		.setLabel("地域")
		.build();
		
		
		Column count = new Column.Builder(Column.TYPE_NUMBER)
		.setId("count")
		.setLabel("出现数")
		.build();
		data.addColumn(emotion)
		    .addColumn(place)
		    .addColumn(count);
		
		TreeSet<Row<Long>> rowSet = new TreeSet<Row<Long>>();
		for (Entity result : entities) {
			Map<String, Object> properties = result.getProperties();

			List<Cell> cells = new ArrayList<Cell>();
			Cell emotionCell = new Cell(String.valueOf(properties.get("emotion")) , null, null);
			Cell placeCell = new Cell(String.valueOf(properties.get("place")), null, null);
			Cell countCell = new Cell(Long.valueOf(String.valueOf(properties.get("count"))), null, null);
			cells.add(emotionCell);
			cells.add(placeCell);
			cells.add(countCell);
			Row<Long> row = new Row<Long>(cells, Long.valueOf(String.valueOf(countCell.getV())));
			rowSet.add(row);
		}

		data.setRows(rowSet);
		return data.asJSONObject();
	}

}
