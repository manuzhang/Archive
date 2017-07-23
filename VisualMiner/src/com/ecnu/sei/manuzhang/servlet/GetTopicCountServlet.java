package com.ecnu.sei.manuzhang.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ecnu.sei.manuzhang.store.Store;
import com.google.appengine.api.datastore.Entity;
import com.google.gson.Gson;

@SuppressWarnings("serial")
public class GetTopicCountServlet extends HttpServlet {
	//private static final String KIND = "TopicByCount";
	
	private final Logger logger = Logger.getLogger(GetTopicCountServlet.class.getCanonicalName());
	
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.setContentType("application/text; charset=utf-8");
		resp.setHeader("Cache-Control", "no-cache");
		
		String kind = "topic_count";
	
		Iterable<Entity> entities = Store.listEntities(kind);
		
		PrintWriter out = resp.getWriter();
		out.print(writeJSON(entities));
	}
	
	protected String writeJSON(Iterable<Entity> entities) throws NumberFormatException, UnsupportedEncodingException {
		TopicCountArray topicArray = new TopicCountArray();
		for (Entity result : entities) {
			Map<String, Object> properties = result.getProperties();
			TopicCount topicCount = new TopicCount(
					String.valueOf(properties.get(TopicCount.TOPIC)),
					Long.valueOf(String.valueOf(properties.get(TopicCount.TWEETS))),
					Long.valueOf(String.valueOf(properties.get(TopicCount.COMMENTS))),
					Long.valueOf(String.valueOf(properties.get(TopicCount.REPOSTS)))
					);
			topicArray.addTopic(topicCount);
		}
		Gson gson = new Gson();
		return gson.toJson(topicArray);
	}
	
	
	protected static class TopicCountArray {
		private Set<TopicCount> topics = new TreeSet<TopicCount>();
		
		public TopicCountArray() {
		}
		
		public void addTopic(TopicCount tc) {
			topics.add(tc);
		}
	
	}
	
	protected static class TopicCount implements Comparable<TopicCount> {
		public static final String TOPIC = "topic";
		public static final String TWEETS = "tweets_count";
		public static final String COMMENTS = "comments_count";
		public static final String REPOSTS = "repost_count";
		                            
		
		private String topicName = null;
		private long tweetsCount = 0;
		private long commentsCount = 0;
		private long repostsCount = 0;
		
		public TopicCount() {
		}
		
		public TopicCount(String topicName, long tweetsCount,
				long commentsCount, long repostsCount) {
			this.topicName = topicName;
			this.tweetsCount = tweetsCount;
			this.commentsCount = commentsCount;
			this.repostsCount = repostsCount;
		}

		public long getCount() {
			return tweetsCount;
		}
		
		@Override
		public int compareTo(TopicCount other) {
			if (null == other) {
				return -1;
			}
			long thisCount = getCount();
		    long otherCount = other.getCount();
		    if (thisCount < otherCount) {
		    	return 1;
		    } else if (thisCount > otherCount) {
		    	return -1;
		    } else {
		    	return 0;
		    }
		}
	}

}
