package com.ecnu.sei.manuzhang.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
public class GetTopicByEmotionCategoryServlet extends HttpServlet {


	
	private final String TITLE = "情绪特征频数";
	
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("application/json; charset=utf-8");
		resp.setHeader("Cache-Control", "no-cache");

		String kind = "topic_by_emotion";
		String name = "topic";
		String value = req.getParameter(name);
		PrintWriter out = resp.getWriter();
		out.print(writeJSON(Store.listEntities(kind, name, value)));
	}

	private String writeJSON(Iterable<Entity> entities) {
		Data<Long> data = new Data<Long>();
		Column category = new Column.Builder(Column.TYPE_STRING)
		.setId(TITLE)
		.setLabel(TITLE)
		.build();


		
		data.addColumn(category);
		for (String cate : Util.CATEGORIES) {
			Column emotion = new Column.Builder(Column.TYPE_NUMBER)
			.setId(cate)
			.setLabel(cate)
			.build();
			data.addColumn(emotion);
		}

		
		// mapreduce by hand
		Map<String, Long> cateMap = new HashMap<String, Long>();
		for (Entity result : entities) {
			Map<String, Object> properties = result.getProperties();

			String cate = String.valueOf(properties.get("category"));
			long cnt = Long.valueOf(String.valueOf(properties.get("count")));
			if (cateMap.containsKey(cate)) {
				cnt += cateMap.get(cate); 
			}
			cateMap.put(cate, cnt);
		}
		
		List<Cell> cells = new ArrayList<Cell>();
		Cell title = new Cell("", null, null);
		cells.add(title);

		for (String cate : Util.CATEGORIES) {
			Cell cnt = new Cell(cateMap.get(cate), null, null);
			cells.add(cnt);
		}
		Row<Long> row = new Row<Long>(cells, (long)0);
		Set<Row<Long>> rowSet = new HashSet<Row<Long>>();
		rowSet.add(row);
		data.setRows(rowSet);
		return data.asJSONObject();
	}


}
