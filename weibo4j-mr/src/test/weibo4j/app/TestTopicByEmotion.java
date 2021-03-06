package weibo4j.app;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mrunit.mapreduce.MapDriver;
import org.apache.hadoop.mrunit.mapreduce.MapReduceDriver;
import org.apache.hadoop.mrunit.mapreduce.ReduceDriver;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import weibo4j.app.TopicByEmotion.EmotionMapper;
import weibo4j.app.TopicByEmotion.EmotionReducer;

public class TestTopicByEmotion {
  MapDriver<LongWritable, Text, Text, LongWritable> mapDriver;
  ReduceDriver<Text, LongWritable, Text, LongWritable> reduceDriver;
  MapReduceDriver<LongWritable, Text, Text, LongWritable, Text, LongWritable> mapReduceDriver;
  String status = null;
  
  @Before
  public void setUp() throws IOException {
    DistributedCacheClass dcc = Mockito.mock(DistributedCacheClass.class);
    when(dcc.getLocalCacheFiles(any(Configuration.class))).thenReturn(
        new Path[]{
            new Path("resource/test/topic_by_emotion.txt"),
            new Path("resource/test/emotions.txt")});
    Mapper mapper = new EmotionMapper(dcc);
    Reducer reducer = new EmotionReducer();
    mapDriver = MapDriver.newMapDriver(mapper);
    reduceDriver = ReduceDriver.newReduceDriver(reducer);
    mapReduceDriver = MapReduceDriver.newMapReduceDriver(mapper, reducer);
    BufferedReader reader = new BufferedReader(new FileReader("resource/test/status_by_emotion.txt"));
    status = reader.readLine(); 
    reader.close();
  }

  @Test
  public void testMapper() throws IOException {
    mapDriver.withInput(new LongWritable(), new Text(status))
             // the output position should match exactly
             .withOutput(new Text("心情\t开心\t快乐"), new LongWritable(1))
             .withOutput(new Text("心情\t开心\t快乐"), new LongWritable(1))
             .runTest();
  }

  @Test
  public void testReducer() throws IOException {
    List<LongWritable> values1 = new ArrayList<LongWritable>();
    values1.add(new LongWritable(1));
    values1.add(new LongWritable(1));
    
    reduceDriver.withInput(new Text("心情\t开心\t快乐"), values1)
                .withOutput(new Text("心情\t开心\t快乐"), new LongWritable(2))
                .runTest();
  }
  
  @Test
  public void testMapReduce() throws IOException {
    mapReduceDriver.withInput(new LongWritable(1), new Text(status));
    mapReduceDriver.addOutput(new Text("心情\t开心\t快乐"), new LongWritable(2));
    mapReduceDriver.runTest();
  }

  
  
  
}
