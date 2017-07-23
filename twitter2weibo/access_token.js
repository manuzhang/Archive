$(function() {

    var clientId = '1617638443';
    var clientSecret = '1b431e4d6a902bbed3f3eb9cc6df2e8a';
    var forceLogin = 'true';
    var redirectUri= 'http://open.weibo.com/apps/1617638443/privilege/oauth';
    var redirectRe = new RegExp(redirectUri + '&code=(.*)');

    if(window.location.href.match(redirectRe)) {
       exchangeCodeForToken(parseRedirectURI(window.location.href));
    }

    function parseRedirectURI(uri) {
      var code = uri.split(/=/)[1];
      return code;
    }

    function exchangeCodeForToken(authcode) {
      $.ajax({
        url: "https://api.weibo.com/oauth2/access_token",
        type: "POST",
        data: {
        client_id: clientId,
        client_secret: clientSecret, 
        grant_type: "authorization_code",
        code: authCode,
        redirect_url: "http://open.weibo.com/apps/1617638443/privilege/oauth",
	}, 
        success: function(data) {
	  if (data.hasOwnProperty('access_token')) {
	    setAccessToken(data.access_token);
	  }
	}, 
        error: function() {
	
	}
      });
    }

   function setAccessToken(token) {
    chrome.storage.sync.set({'access_token': token}, function() {});
   }

  });
});
