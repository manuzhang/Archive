  $(function() {
  var runtimeOrExtension = chrome.runtime && chrome.runtime.Message ? 'runtime' : 'extension';
  var weibo = "";
  chrome[runtimeOrExtension].onMessage.addListener(
    function(request, sender, sendResponse) {
      console.log(request);
      if (request.type == 'access_token') {
        var token = request.content;
        post(token, weibo);
        setAccessToken(token);
      } else if (request.type == 'message') {
        weibo = request.content;
        authorizeOrPost(weibo);
      }
  });

  function sendMessageBack(mess) {
    chrome.tabs.query({active: true, currentWindow: true}, function(tabs) {
      chrome.tabs.sendMessage(tabs[0].id, {message: mess}, function(response) {});
    });

  }

  function setAccessToken(token) {
    chrome.storage.sync.set({'access_token': token}, function() {
      console.log('access_token: ' + token);
    })
  }

  function authorizeOrPost(weibo) {
    chrome.storage.sync.get('access_token', function(data) {
      var token = data.access_token;
      if (null == token || '' == token) {
        authorize();
      } else {
        post(token, weibo);
      }
    });
  }

  function authorize() {
    var redirectURI = 'https://api.weibo.com/oauth2/default.html';
    var url = 'https://api.weibo.com/oauth2/authorize?client_id=1617638443&forecelogin=true&redirect_uri=' + redirectURI;
    window.open(url, '', 'width: 400, height: 300');
  }

  function post(token, weibo) {
    $.ajax({
      url: "https://api.weibo.com/2/statuses/update.json",
      type: "POST",
      data: {
        access_token: token,
        status: weibo
      },
      success: function()  {
        var message = "successfully post tweet to weibo";
        console.log(message);
        alert(message)
      },
      error: function(jqXHR, textStatus) {
        console.log("request failed: " + textStatus);
      }
    });
  }

});
