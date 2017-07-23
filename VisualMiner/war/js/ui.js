(function($) {
  $(document).ready(
      function() {

        // sidebar behaviors
        $('#simple-menu').sidr();

        $('#sidr').on('click', 'li', function() {
          $('#sidr').find('li').removeClass('active');
          $(this).addClass('active');
        });

        $('#sidr').on('click', '#title', function(event) {
          event.preventDefault();
          $('#unit').hide('blind');
          $('#cate').show('blind');
          $('#overview').hide('fade');
          $('.dimension').hide('fade');
          $('#list').show('fade');
        });

        // content behaviors
        var getPosts = function(topicName) {
          $('.dimension').hide('fade');

          $.ajax('/gettopicposts', {
            type : 'GET',
            data : {
              topic : topicName
            },
            dataType : 'json',
            success : function(data) {
              $('#weibo').find('li').remove();
              listPost(data);
            }
          });

          $.ajax('/gettopicintro', {
            type : 'GET',
            data : {
              topic : topicName
            },
            dataType : 'text',
            success : function(data) {
              if (data !== "null") {
                $('#wiki').text(data);
              }
            }
          });

          $('#page').show('fade');

        };

        $('.selectable').on('click', 'li', function(event) {
          event.preventDefault();
          $('#cate').hide('blind');
          $('#unit').show('blind');
          $('#title').removeClass('active');
          $('#li-page').addClass('active');
          $('#list').hide('fade');
          var topicName = $(this).find('a').text();
          $('#overview').find('h2').text(topicName);
          getPosts(topicName);
          $('#overview').show('fade');
          $('#page').show('fade');
        });

        // get topicName
        var getTopic = function() {
          return $('#overview').find('h2').text();
        };

        $('#li-page').on('click', function(event) {
          event.preventDefault(getTopic());
          getPosts(getTopic());
        });

        $('#li-time').on('click', function(event) {
          event.preventDefault();
          $.ajax('/gettopicbytime', {
            type : 'GET',
            data : {
              topic : getTopic()
            },
            dataType : 'json',
            success : function(data) {
              $('.dimension').hide('fade');

              drawLineChart(data, 'time');
              $('#time').show('fade');
            }
          });
        });

        $('#li-location').on('click', function(event) {
          event.preventDefault();

          $.ajax('/gettopicbyplace', {
            type : 'GET',
            data : {
              topic : getTopic()
            },
            dataType : 'json',
            success : function(data) {
              $('.dimension').hide('fade');
              // var minRange = 1;
              // var maxRange = data.length;
              // $('#slide_range').slider({
              // animate : 'fast',
              // min : minRange,
              // max : maxRange,
              // slide : function(event, ui) {
              // var v = ui.value;
              // var dataRange = {
              // "cols" : data.cols,
              // "rows" : data[v].rows
              // };
              // drawGeoChart(dataRange, 'location_map');
              // $('#slide_value').text(data[v].time);
              // }
              // });
              // var v = $('#slide_range').slider('value');
              // var dataRange = {
              // "cols" : data.cols,
              // "rows" : data[v].rows
              // };
              // drawGeoChart(dataRange, 'location_map');
              // $('#slide_value').text(data[v].time);
              drawGeoChart(data, 'location_map')
              $('#location').show('fade');
            }
          });
        });

        $('#li-emotion').on(
            'click',
            function(event) {
              event.preventDefault();
              // colors for different emotion categories
              var colors = [ "#ff9900", "#3366cc", "#dc3912", "#dd4477",
                             "#990099", "#109618", "#0099c6" ];

              $('.dimension').hide('fade');
              $.ajax('/gettopicbyemotion', {
                type : 'GET',
                data : {
                  topic : getTopic()
                },
                dataType : 'json',
                success : function(data) {
                  $('#emotion_wordcloud').find('svg').remove();
                  drawWordCloud(data, '#emotion_wordcloud', colors);
                }
              });

              $.ajax('/gettopicbyemotioncategory', {
                type : 'GET',
                data : {
                  topic : getTopic()
                },
                datatType : 'json',
                success : function(data) {
                  // drawBarChart(data, "emotion_category");
                  drawColumnChart(data, "emotion_category", colors);
                }
              });

              $('#emotion').show('fade');

            });

        // get topic list from server
        $.ajax('/gettopiccount', {
          type : 'GET',
          dataType : 'json',
          success : function(data) {
            var selectable = $('.content').find('.selectable');
            var list = selectable.children('li');
            var topics = data.topics;

            var pageId = 0;

            var listLength = list.length;
            var topicNum = topics.length;

            if (1 == topicNum && 0 == topics[0].length) {
              topicNum = 0;
            }

            listItem(topics, topicNum, list, listLength, pageId);

            var pageNum = Math.ceil(topicNum / listLength);
            $('#prev-page').button().click(function(event) {
              event.preventDefault();
              if (pageId <= pageNum - 1) {
                $('#next-page').button('enable');
              }
              listItem(topics, topicNum, list, listLength, --pageId);
              if (pageId == 0) {
                $(this).button('disable');
              }
            });

            $('#prev-page').button('disable'); // disabled on first page

            $('#next-page').button().click(function(event) {
              event.preventDefault();
              if (pageId >= 0) {
                $('#prev-page').button('enable');
              }
              listItem(topics, topicNum, list, listLength, ++pageId);
              if (pageId == pageNum - 1) {
                $(this).button('disable');
              }
            });

            // show the content after the list and buttons have rendered
            $('.content').show('fade');

          }
        });

      });
})(jQuery);
