package com.ecnu.sei.manuzhang.chart;

import java.util.List;

public class Row<T extends Comparable<T>>  implements Comparable<Row<T>> {
	
	// for detailed definition, 
	// see https://developers.google.com/chart/interactive/docs/reference#dataparam

	List<Cell> c = null;
	transient T t = null; // a comparable field, should not be serialized to json
	
	public Row() {
	}
	
	public Row(List<Cell> c, T t) {
		this.c = c;
		this.t = t;
	}
	
	public String asJSONObject() {
		return Util.gson.toJson(this);
	}

	public T getT() {
		return t;
	}

	@Override
	public int compareTo(Row<T> o) {
		return -t.compareTo(o.getT());
	}
}
