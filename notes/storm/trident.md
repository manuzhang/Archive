A StormTopology is essentially a Directed Acyclic Graph (DAG). TridentTopology is more complex as it is a logical DAG and needs to be translated to a StormTopology. Multiple logical nodes could be merged into a single one since operations requiring no data shuffle can be executed on one bolt.  

Storm uses [jgrapht](http://jgrapht.org/) to construct a graph. A graph has 3 kinds of `Node`s, `ProcessorNode` (operation), `PartitionNode` (partititon) and `SpoutNode` (spout) which are connected by `IndexedEdge`.

A `SpoutNode` is added when a new stream is created. 

```
   newStream / newDRPCStream -> addNode -> registerNode
```

A `PartitionNode` is added through `partition`

```
   broadcast / shuffle / partitionBy -> partition -> addSourcedNode -> registerSourcedNode
```

A `ProcessorNode` is added in the other operations.

```
  each / project / partitionAggregate / stateQuery -> addSourcedNode -> registerSourcedNode
```

### operation to node

* partition

  Call `stream.partition(grouping)` 

  if `_node` is a `PartitionNode`
    ```
      _node -> ProcessorNode(EachProcessor(TrueFilter())) -> PartitionNode(grouping)
    ```
  else 
    ```
      _node -> PartitionNode(grouping)
    ```

* each

  `ProcessorNode(EachProcessor)`

  `EachProcessor` is binded with `AppendCollector` which will append what user emits to input tuple.

* partitionAggregate

  `ProcessorNode(AggregateProcessor)`

* stateQuery

  `ProcessorNode(StateQueryProcessor)`

* partitionPersist
  `ProcessorNode(PartitionPersistProcessor)`

## TridentWordCount

```java
    IPartitionedTridentSpout spout = new TransactionalTridentKafkaSpout(
            KafkaUtils.getTridentKafkaConfig(config, new SchemeAsMultiScheme(new StringScheme())));
    TridentTopology trident = new TridentTopology();
    trident.newStream("wordcount", spout)
      .each(new Fields(StringScheme.STRING_SCHEME_KEY), new WordSplit(), new Fields("word"))
      .groupBy(new Fields("word"))
      .persistentAggregate(new MemoryMapState.Factory(), new Count(), new Fields("count"));
```

In trident, parallelisms are the same for operations with no partition (broadcast / shuffle / partitionBy / groupBy) in between. The value would be the maxium set by `.parallelismHint`.


### MasterBatchCoordinator
In a trident topology, the "spout" set by a user would be executed in a bolt while the actual spout is `MasterBatchCoordinator`. The role of `MasterBatchCoordinator` is to ensure that tuples are processed in order and every tuple has been processed exact-once.

The lifetime of each tuple is a transaction. A transaction is only committed when a tuple has been fully processed and all former transactions have been committed. A uncommitted transaction is an active transaction. `MasterBatchCoordinator` could have `Config.TOPOLOGY_MAX_SPOUT_PENDING` number of active transactions.  Transaction status is stored in Zookeeper.

On status (PROCESSING, PROCESSED, COMMITTING) of a transaction, `MasterBatchCoordinator` would emit tuples into corresponding streams ($batch,  $commit, $success). 

1. A transaction starts with PROCESSING status, and `MasterBatchCoordinator` emits `TransactionAttempt` into $batch stream to kick off a new batch.
2. When the batch is acked, the transaction status turns into PROCESSED and `MasterBatchCoordinator` emits into $commit stream.
3. When the commit tuple is acked, the transaction status turns into COMMITTING and `MasterBatchCoordinator` emits into $success stream.
4. When the success tuple is acked, the transaction is removed.

`TransactionAttempt` consists of a transactionId and an attemptId. 


### TridentSpoutCoordinator
`TridentSpoutCoordinator` gets coordinator from the user-defined "spout" and coordinates the batch replay of a transaction. It stores whatever needed metadata in zookeeper.

Take `TransactionalTridentKafkaSpout` for example, the metadata includes topic, partition, offset and other needed information to re-pull message from Kafka.  

`TridentSpoutCoordinator` emits `Fields("tx", "metadata")` into `$batch` stream.

### TridentSpoutExecutor
`TridentSpoutExecutor` gets emitter from the user-defined "spout" and calls the emitter's `emitBatch` 


### TridentBoltExecutor

It's very similar to `CoordinatedBolt`.

5s to fail a batch

### SubTopologyBolt

A topology is divided into sub-topologies by `PatitionNode` and each sub-topology may have one or more `ProcessorNode`s. `SubTopologyBolt` would organize the execute of the corresponding `Processor`s.

### Coordinator

### Emitter


## Interfaces

