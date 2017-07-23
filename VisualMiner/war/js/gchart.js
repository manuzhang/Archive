var drawLineChart = function(json, container) {
  // Create and populate the data table.
  /*
   * var data = google.visualization.arrayToDataTable([ [ 'x', 'Cats', 'Blanket
   * 1', 'Blanket 2' ], [ 'A', 1, 1, 0.5 ], [ 'B', 2, 0.5, 1 ], [ 'C', 4, 1, 0.5 ], [
   * 'D', 8, 0.5, 1 ], [ 'E', 7, 1, 0.5 ], [ 'F', 7, 0.5, 1 ], [ 'G', 8, 1, 0.5 ], [
   * 'H', 4, 0.5, 1 ], [ 'I', 2, 1, 0.5 ], [ 'J', 3.5, 0.5, 1 ], [ 'K', 3, 1,
   * 0.5 ], [ 'L', 3.5, 0.5, 1 ], [ 'M', 1, 1, 0.5 ], [ 'N', 1, 0.5, 1 ] ]);
   */
  var data = new google.visualization.DataTable(json);
  // Create and draw the visualization.
  var options = {
      curveType: "function",
      width: 600,
      height: 350 
  }
  
  new google.visualization.LineChart(document
      .getElementById(container)).draw(data, options);
}

/*var drawBarChart = function(json, container) {
  var json2 = {"cols":[{"type":"string","id":"Year","label":"Year"},
                      {"type":"number","id":"Austria","label":"Austria"}],
              "rows":[{"c":[{"v":"2003"},{"v":"1336060"}]},
                      {"c":[{"v":"2004"},{"v":"1538156"}]},
                      {"c":[{"v":"2005"},{"v":"1576579"}]},
                      {"c":[{"v":"2006"},{"v":"1600652"}]},
                      {"c":[{"v":"2007"},{"v":"1968113"}]},
                      {"c":[{"v":"2008"},{"v":"1901067"}]}]
             };

  
  var data = new google.visualization.DataTable(json);
  
  var options = {
      title:"话题各情绪特征出现次数",
      width:500, height:350,
      vAxis: {title: "情绪特征"},
      hAxis: {title: "出现次数"}
  };
  
  new google.visualization.BarChart
      (document.getElementById(container))
      .draw(data, options);
}*/

var drawColumnChart = function(json, container, colors) {
  var data = new google.visualization.DataTable(json);
  
  var options = {
      title:"话题各情绪特征出现次数",
      width:350, height:350,
      vAxis: {title: "出现次数"},
      hAxis: {title: "情绪特征"}
  };
  options.colors = colors;
  new google.visualization.ColumnChart
      (document.getElementById(container))
      .draw(data, options);
}

var drawGeoChart = function(json, container) {

  /*  var json = {
      "cols": [{"type":"string","id":"country","label":"country"},
                {"type":"number", "id":"popularity","label":"popularity"}
               ]
      ,
      "rows": [{"c":[{"v":"US", "f":"America"}, {"v":"200"}]}, 
                {"c":[{"v":"RU", "f":"Russia"}, {"v":"200"}]}
               ]

      }*/

  var data = new google.visualization.DataTable(json);
  var options = {
      displayMode: 'regions', 
      resolution: 'provinces',
      region:'CN',
      width: 600,
      height: 350,
      backgroundColor: '#555555'

  };
  new google.visualization.GeoChart(document
      .getElementById(container)).draw(data, options);
}