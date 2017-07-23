var drawWordCloud = function(json, container, colors) {
  // no data  
  if (json.rows.length === 0) {
    return;
  }
  
  var max = json.rows[0].c[1].v;
  
  var data = json.rows.map(function(d) {
    var count = d.c[1].v;
    return {
      text : d.c[0].v,
      size : count / max * 90 + 10,
      color : colors[d.c[2].v]
    };
  });
  
  
  //d3.select(container).select("svg").remove(); // remove last svg
  d3.layout.cloud().size([300, 350])
  /*
   .words([ "Hello", "world", "normally", "you", "want", "more", "words",
   "than", "this"].map(function(d) { return {text: d, size: 10 + Math.random() *
    90}; })) */
   
  .words(data) 
/*  .rotate(function() {
    return ~~(Math.random() * 2) * 90;
  })*/
  .font("Impact").fontSize(function(d) {
    return d.size;
  }) 
  .on("end", draw).start();

  function draw(words) {
    d3.select(container).append("svg").attr("width", 300).attr("height", 350)
        .append("g")
        .attr("transform", "translate(150,175)")
        .selectAll("text")
        .data(words).enter().append("text")
        .style("font-size", function(d) {
          return d.size + "px";
        })
        .style("font-family", "Impact")
        .style("fill", function(d, i) {
          //return fill(i);
          return d.color;
        })
        .attr("text-anchor", "middle").attr("transform", function(d) {
          return "translate(" + [ d.x, d.y ] + ")rotate(" + d.rotate + ")";
        })
        .text(function(d) {
          return d.text;
        });
  }
}