## NativeTask 

### Motivation

### Design

### Implementations 
The module is located at *src/contrib/nativetask* in MRv1 and *hadoop-mapreduce-project/hadoop-mapreduce-client/hadoop-mapreduce-client-nativetask* in MRv2. 

#### Plug-in point
We use a `TaskDelegation` class to delegate Java mapper/collector/reducer to native if available. We fallback to Java implemenations should native ones throw any exceptions. It works as below in `MapTask.run()`. 

* MRv1

```java
      MapOutputCollector<K,V> delegateCollector = TaskDelegation.getOutputCollectorDelegator(umbilical, reporter, job, MapTask.this);
      if (null == delegateCollector) {
          collector = new MapOutputBuffer<K,V>(umbilical, job, reporter);
      }
      else {
        collector = delegateCollector;
      }
```

* MRv2 
MRv2 has pluggable collector, configured with `mapreduce.job.map.output.collector.class`. Hence, there is no need to plugin `TaskDelegation` for collector but we still need to handle exceptions. 

```java
    MapOutputCollector<KEY, VALUE> collector
      = (MapOutputCollector<KEY, VALUE>)
       ReflectionUtils.newInstance(
                        job.getClass(JobContext.MAP_OUTPUT_COLLECTOR_CLASS_ATTR,
                        MapOutputBuffer.class, MapOutputCollector.class), job);
    MapOutputCollector.Context context =
                           new MapOutputCollector.Context(this, job, reporter);

    try {
      collector.init(context);
    } catch (IOException e) {
      collector = new MapOutputBuffer();
      collector.init(context);
    } finally {
      LOG.info("Map output collector class = " + collector.getClass().getName());
      return collector;
    }
``` 

#### Java 

**org.apache.hadoop.mapred.nativetask.buffer** buffers for Java and native to transport data. We use DirectBuffer and since it will not be released and collected by Java we implement a DirectBufferPool to reuse buffer.

**org.apache.hadoop.mapred.nativetask.handlers** handler map/collect/reduce tasks between Java and native. They have native counterparts.

We have the following handlers for corresponding cases:
1. `NativeCollectorOnlyHandler.java` (Java RecordReader + Java Mapper + Native Collector) <-> `MCollectorOutputHandler.cc`
2. `NativeMapAndCollectorHandler.java` (Java RecordReader + Native Mapper + Native Collector) <->  `MMapperHandler.cc`
3. `NativeMapOnlyHandlerNoReducer.java` (Java RecordReader + Native Mapper) <-> `MMapeperHandler.cc`
4. NativeMapTask (Full Native MapTask) <-> `MMapTaskHandler.cc`
5. NativeReduceTask (Native ReduceTask) <-> `RReduceHandler.cc`
6. CombineHandler (Java Combiner) <-> `CombineHandler.cc`

**org.apache.hadoop.mapred.nativetask.serde**: Serializer/Deserializer for various key types. We currently support hadoop, hbase, hive, pig and mahout. 

**org.apache.hadoop.mapred.nativetask.NativeMapOutputCollectorDelegator**: 

**org.apache.hadoop.mapred.nativetask.NativeBatchProcessor**: manage buffers and handlers. Its native counterpart is `src/handler/BatchHandler.cc`

**org.apache.hadoop.mapred.nativetask.NativeRuntime**: create native handlers and write configurations to native


#### Native 

**src/lib/NativeTask.cc**

**src/lib/MapOutputCollector.cc**

**src/lib/MapOutputSpec.cc**: get configurations for `MapOutputCollector`

**src/handler/BatchHandler.cc**: counterpart for Java BatchProcessor 

### Support

Platforms support: HBase, Hive, Mahout, Pig, Tez(PoC) 

CompressionCodec support: Lz4, Gzip, Snappy




