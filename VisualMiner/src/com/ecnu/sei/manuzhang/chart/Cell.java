package com.ecnu.sei.manuzhang.chart;

import java.util.LinkedHashMap;
import java.util.Map;

public class Cell {
	
	// for detailed definition of each field, 
	// see https://developers.google.com/chart/interactive/docs/reference#dataparam

	private Object v = null;
	private String f = null;
	private Map<String, Object> p = null;
	
	public Cell() {
	}
	
	public Cell(Object v, String f, Map<String, Object> p) {
		this.v = v;
		this.f = f;
		this.p = p;
	}
	
	public Cell setV(Object v) {
		this.v = v;
		return this;
	}
	
	public Cell setF(String f) {
		this.f = f;
		return this;
	}
	
	public Cell setP(Map<String, Object> p) {
		this.p = p;
		return this;
	}
	
	public Cell addP(String s, Object o) {
		if (null == p) {
			p = new LinkedHashMap<String, Object>();
		}
		p.put(s, o);
		return this;
	}
	
	public Object getV() {
		return v;
	}
	
	public String getF() {
		return f;
	}
	
	public Map<String, Object> getP() {
		return p;
	}
	
	public String asJSONObject() {
		return Util.gson.toJson(this);
	}

	
}
