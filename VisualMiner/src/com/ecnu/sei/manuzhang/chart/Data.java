package com.ecnu.sei.manuzhang.chart;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class Data<T extends Comparable<T>> {
	private List<Column> cols = new ArrayList<Column>();
	private Set<Row<T>> rows = new TreeSet<Row<T>>();

	public Data() {
	}

	public Data(List<Column> cols, Set<Row<T>> rows) {
		this.cols = cols;
		this.rows = rows;
	}

	public Data<T> setColumns(List<Column> cols) {
		this.cols = cols;
		return this;
	}

	public Data<T> setRows(Set<Row<T>> rows) {
		this.rows = rows;
		return this;
	}

	public Data<T> addColumn(Column column) {
		cols.add(column);
		return this;
	}

	public Data<T> addRow(Row<T> row) {
		rows.add(row);
		return this;
	}


	public String asJSONObject() {
		return Util.gson.toJson(this);
	}
}
