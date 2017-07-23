package com.ecnu.sei.manuzhang.servlet;

import junit.framework.Assert;

import org.junit.Test;

import com.ecnu.sei.manuzhang.servlet.GetTopicCountServlet.TopicCount;
import com.ecnu.sei.manuzhang.servlet.GetTopicCountServlet.TopicCountArray;
import com.google.gson.Gson;

public class TestWriteTopicCount {

	private final String TOPIC = "\"topicName\"";
	private final String TWEETS = "\"tweetsCount\"";
	private final String COMMENTS = "\"commentsCount\"";
	private final String REPOSTS = "\"repostsCount\"";
	private final String LIVERPOOL = "\"Liverpool\"";
	private final String SPURS = "\"Spurs\"";


	@Test
	public void testWriteTopicCount() {
		StringBuilder sb = new StringBuilder();

		sb.append("{\"topics\":[");
		sb.append("{").append(TOPIC + ":" + SPURS + ",")
		.append(TWEETS + ":" + 2222 + ",")
		.append(COMMENTS + ":" + 3333 + ",")
		.append(REPOSTS + ":" + 4444).append("},");
		sb.append("{").append(TOPIC + ":" + LIVERPOOL + ",")
		.append(TWEETS + ":" + 1111 + ",")
		.append(COMMENTS + ":" + 2222 + ",")
		.append(REPOSTS + ":" + 3333).append("}");
		sb.append("]}");

		TopicCountArray array = new TopicCountArray();
		TopicCount liverpool = new TopicCount("Liverpool", 1111, 2222, 3333);
		TopicCount spurs = new TopicCount("Spurs", 2222, 3333, 4444);
		array.addTopic(liverpool);
		array.addTopic(spurs);
		Gson gson = new Gson(); 
		String s = gson.toJson(array);
		System.out.println(s);
		Assert.assertEquals(sb.toString(), s);
	}
}

