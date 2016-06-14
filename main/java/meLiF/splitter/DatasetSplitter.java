package meLiF.splitter;

import meLiF.dataset.DataSet;
import meLiF.dataset.DataSetPair;

import java.util.List;



public interface DatasetSplitter {
    List<DataSetPair> split(DataSet original);
}
