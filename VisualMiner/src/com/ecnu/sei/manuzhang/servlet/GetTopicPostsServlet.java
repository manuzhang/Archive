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
public class GetTopicPostsServlet extends HttpServlet {

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("application/json; charset=utf-8");
		resp.setHeader("Cache-Control", "no-cache");

		String kind = "topic_posts";
		String name = "topic";
		String value = req.getParameter(name);
		PrintWriter out = resp.getWriter();
		out.print(writeJSON(Store.listEntities(kind, name, value)));
	}

	private String writeJSON(Iterable<Entity> entities) {
		StringBuilder posts = new StringBuilder();
		posts.append("[");
		for (Entity result : entities) {
			Map<String, Object> properties = result.getProperties();
			posts.append(String.valueOf(properties.get("posts")) + ",");
		}
		int index = posts.lastIndexOf(",");
		if (index >= 0) {
			posts.deleteCharAt(index);
		}
		posts.append("]");
		return posts.toString();
	}

}
