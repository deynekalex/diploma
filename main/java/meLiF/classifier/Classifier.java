package meLiF.classifier;
import meLiF.dataset.DataSet;
import meLiF.DataSetTransformer;

import java.util.List;


/**
 * @author iisaev
 */
public interface Classifier {
    DataSetTransformer datasetTransformer = new DataSetTransformer();

    void train(DataSet trainDs);

    List<Double> test(DataSet testDs);
}
