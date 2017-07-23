$(function() {

  var clientId = '1617638443';
  var clientSecret = '1b431e4d6a902bbed3f3eb9cc6df2e8a';
  var redirectUri= 'https://api.weibo.com/oauth2/default.html';
  var redirectRe = new RegExp(redirectUri + '[?]code=(.*)');

  console.log(window.location.href);
  var matches = window.location.href.match(redirectRe);
  if (matches && matches.length > 1) {
    exchangeCodeForToken(matches[1]);
  } else {
    setInterval(insertButton, 1000); 
  }
  

  function exchangeCodeForToken(authCode) {
    $.ajax({
      url: "https://api.weibo.com/oauth2/access_token",
      type: "POST",
      dataType: "json",
      data: {
        client_id: clientId,
        client_secret: clientSecret, 
        grant_type: "authorization_code",
        code: authCode,
        redirect_uri: redirectUri
      }, 
      success: function(data) {
        sendMessageToBack("access_token", data.access_token);
        window.close();
      }, 
      error: function(jqXHR, textStatus) {
        console.log('request failed: ' + textStatus);
      }
    });
  }

  function insertButton() {
    $('.tweet-actions').each(function() {
      if ($(this).hasClass('t2w-button-inserted')) {
       return;
     }
     $(this).addClass('t2w-button-inserted');
     var container = $(this);
     var t2wClassName = 'action-t2w-container';
     var tweet = container.parents('div.tweet'),
     tweetId = tweet.attr('data-tweet-id'),
     author = tweet.attr('data-screen-name'),
     content = tweet.find('p.js-tweet-text'),
     text = content.text(),
     links = content.children('a');
     links.each(function() {
      text.replace($(this).attr('href'), $(this).attr('data-expanded-url'));
    });
     var message = author + ': ' + text; 
     var t2wButton = $('<li class="' + t2wClassName + '" data-tweet-id="' + tweetId + '">'
      + '<a href="https://twitter.com/#" title="TwitterToWeibo">'
      + '<b>ToWeibo</b>'
      + '</a>'
      + '</li>');
     container.children('.action-fav-container').after(t2wButton);
     addClickOnListener(t2wButton, message);
   });
  }

  function addClickOnListener(button, message) {
    $(button).on('click', 
     function(b) {
       b.preventDefault();
       sendMessageToBack("message", message);
     });
  }

  function sendMessageToBack(id, message) {
    var request = {
      type: id,
      content: message
    };
    var runtimeOrExtension = chrome.runtime && chrome.runtime.Message ? 'runtime' : 'extension';
    chrome[runtimeOrExtension].sendMessage(request, function(response) {});
  }
});
