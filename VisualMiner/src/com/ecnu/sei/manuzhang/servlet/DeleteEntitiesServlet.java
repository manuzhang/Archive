package com.ecnu.sei.manuzhang.servlet;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ecnu.sei.manuzhang.store.Store;

@SuppressWarnings("serial")
public class DeleteEntitiesServlet extends HttpServlet {

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		String kind = "topic_posts";
		Store.truncate(kind);
	}
}
