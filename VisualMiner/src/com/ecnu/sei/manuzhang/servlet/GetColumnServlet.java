package com.ecnu.sei.manuzhang.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ecnu.sei.manuzhang.store.Store;


/*
 * this servlet returns a column of a kind as comma-separated values
 */

@SuppressWarnings("serial")
public class GetColumnServlet extends HttpServlet {
	
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("application/text; charset=utf-8");
		resp.setHeader("Cache-Control", "no-cache");

		String kind = req.getParameter("kind");
		String column = req.getParameter("column");
		PrintWriter out = resp.getWriter();
		out.print(Store.listColumnAsCSV(kind, column));

	}
}
