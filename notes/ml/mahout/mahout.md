## run mahout examples

the entrance is `org.apache.mahout.driver.MahoutDriver`. It loads in `driver.classes.default.props` which contains mapping from cmd arguments to Mahout classes with main function. 

For example, with `mahout seq2sparse` Mahout will run `org.apache.mahout.vectorizer.SparseVectorsFromSequenceFiles`.