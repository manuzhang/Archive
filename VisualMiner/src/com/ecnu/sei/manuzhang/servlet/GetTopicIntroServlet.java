package com.ecnu.sei.manuzhang.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ecnu.sei.manuzhang.store.Store;
import com.google.appengine.api.datastore.Entity;
import com.google.gson.Gson;

@SuppressWarnings("serial")
public class GetTopicIntroServlet extends HttpServlet {
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("application/json; charset=utf-8");
		resp.setHeader("Cache-Control", "no-cache");

		String kind = "topic_introduction";
		String name = "topic";
		String value = req.getParameter(name);
		PrintWriter out = resp.getWriter();
		out.print(writeText(Store.listEntities(kind, name, value)));
	}

/*	private String writeJSON(Iterable<Entity> entities) {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for (Entity result : entities) {
			sb.append("{");
			Map<String, Object> properties = result.getProperties();
			String topic = String.valueOf(properties.get("topic"));
			String intro = String.valueOf(properties.get("introduction"));
			sb.append("\"" + topic + "\":" + "\"" + intro + "\"");
			sb.append("},");
		}
		sb.append("]");
		int index = sb.lastIndexOf(",");
		if (index >= 0) {
			sb.deleteCharAt(index);
		}
		
		return sb.toString();
	}
*/
	private String writeText(Iterable<Entity> entities) {
		for (Entity result : entities) {
			Map<String, Object> properties = result.getProperties();
			return String.valueOf(properties.get("introduction"));
		}
		return null;
	}
}
