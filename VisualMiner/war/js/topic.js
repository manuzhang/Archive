var listItem = function(topics, topicNum, list, listLength, pageId) {
	if (0 == topicNum) {

		for ( var i = 0; i < listLength; i++) {
			var item = list.eq(i);
			item.find('a').text('话题');
			item.find('.count').text('微博数');
		}
	} else {
		// var pageNum = Math.ceil(topicNum / listLength);

		var listNum = listLength;
		if (0 == pageId && topicNum < listLength) {
			listNum = topicNum;
		}

		var max = topics[0].tweetsCount;
		for ( var i = 0; i < listNum; i++) {
			var item = list.eq(i);
			var t = topics[pageId * listLength + i];
			item.find('.prog-bar').find('div').remove();
			item.find('a').text(t.topicName);
			item.find('.count').text(t.tweetsCount);
			var progress = t.tweetsCount / max * 100;
			if (progress < 1) {
				progress = 1;
			}
			item.find('.prog-bar').append(
					'<div style="height:100%; width:' + progress
							+ '%; background-color:#1155cc;"></div>')
			item.show();
		}

		var rest = topicNum - (pageId + 1) * listLength;

		if (rest < listLength) {
			$('#next-page').button('disable');
		}

	}
};

var listPost = function(posts) {
	var lists = $('#weibo');
	var num = posts.length < 5 ? posts.length : 5;
	for ( var i = 0; i < num; i++) {
		lists.append('<li style="list-style-type:none"><i style="color:#11ccff">@'
						+ posts[i].user_name
						+ ': </i>'
						+ posts[i].text
						+ "</li>");
	}
}