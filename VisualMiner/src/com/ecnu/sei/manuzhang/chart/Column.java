package com.ecnu.sei.manuzhang.chart;

import java.util.LinkedHashMap;
import java.util.Map;


public class Column {
	public static final String TYPE_BOOLEAN = "boolean";
	public static final String TYPE_NUMBER = "number";
	public static final String TYPE_STRING = "string";
	public static final String TYPE_DATE = "date";
	public static final String TYPE_DATETIME = "datetime";
	public static final String TYPE_TIMEOFDAY = "timeofday";
	
	// for detailed definition of each field, 
	// see https://developers.google.com/chart/interactive/docs/reference#dataparam
	
	private final String type;             // Data type of the data in the column
	private final String id;               // String ID of the column
	private final String label;            // String value that some visualizations 
	                                       // display for this column 
	private final String pattern;          // String pattern that was used by a data source 
	                                       // to format numeric, date, or time column values
	private final Map<String, Object> p;   // An object that is a map of custom values
	                                       // applied to the cell
	
	public static class Builder {

		// required
		private final String type;
		
		// optional
		private String id = null;
		private String label = null;
		private String pattern = null;
		private Map<String, Object> p = null;
		
		public Builder(String type) {
			this.type = type;
		}
		
		public Builder setId(String id) {
			this.id = id;
			return this;
		}

		public Builder setLabel(String label) {
			this.label = label;
			return this;
		}

		public Builder setPattern(String pattern) {
			this.pattern = pattern;
			return this;
		}
		
		public Builder setP(Map<String, Object> p) {
			this.p = p;
			return this;
		}

		public Builder addP(String s, Object o) {
			if (null == p) {
				p = new LinkedHashMap<String, Object>();
			}
			p.put(s, o);
			return this;
		}
		
		public Column build() {
			return new Column(this);
		}
	}
	
	
	private Column(Builder builder) {
		this.type = builder.type;
		this.id = builder.id;
		this.label = builder.label;
		this.pattern = builder.pattern;
		this.p = builder.p;
	}
	
	public String asJSONObject() {
		return Util.gson.toJson(this);
	}
}
