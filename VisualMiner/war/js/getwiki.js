(function($) {
  function getRaw(topicName) {
    $.ajax('https://zh.wikipedia.org/w/api.php', {
      type : 'GET',
      data : {
        format : "xml",
        action : "query",
        titles : topicName,
        prop : "revisions",
        rvprop : "content",
        redirects : ""
      },
      dataType : 'xml',
      success : function(result) {


      },
      error : function(result) {
        alert(result);
      }
    });  
  }

  var decode = function(xml) {
    return xml.replace(/&lt;/g, "<").replace(/&gt;/g, ">")
    .replace(/&amp;/g, "&").replace(/&quot;/g, "\"")
    .replace(/&#039;/g, "'");
  }

  function getRenderedText(topicName) {
    $.ajax('https://zh.wikipedia.org/w/api.php', {
      type : 'POST',
      data : {
        action : "parse",
        prop : "text",
        text : "{{:" + topicName + "}}"
      },
      dataType : 'xml',
      success : function(result) {
        var start = result.indexOf('>', result.indexOf('<text')) + 1;
        var end = result.indexOf("</text>");
        $('#wiki').text(decode(result.substring(start, end)));
      }
    });
  }
})(jQuery);
