package deynekalex.diploma.ResultsGetter.experiment2;

import filtering.PreferredSizeFilter;
import meLiF.feature.measure.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import meLiF.AlgorithmConfig;
import meLiF.DataSetReader;
import meLiF.classifier.Classifiers;
import meLiF.dataset.DataSet;
import meLiF.executable.ParallelRunner;
import meLiF.melif.impl.BasicMeLiF;
import meLiF.result.Point;
import meLiF.splitter.OrderSplitter;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by deynekalex on 14.05.16.
 */
public class SinglePointResultGetter {
    private static final Logger LOGGER = LoggerFactory.getLogger(ParallelRunner.class);

    public double getSingleResult(String curFile, double stepSize, int attributesToFilter, int testPercent, Point point) {
        int iterations = 1;
        double bestScore = 0;
        DataSetReader dataSetReader = new DataSetReader();
        DataSet dataSet = dataSetReader.readCsv(curFile);
        for(int j = 0; j < iterations; j++) {
            List<Integer> order = IntStream.range(0, dataSet.getInstanceCount())
                    .mapToObj(i -> i).collect(Collectors.toList());
            Collections.shuffle(order);
            RelevanceMeasure[] measures = new RelevanceMeasure[]{new VDM(), new FitCriterion(), new SymmetricUncertainty(), new SpearmanRankCorrelation()};
            AlgorithmConfig config = new AlgorithmConfig(stepSize, Classifiers.WEKA_SVM, measures);
            config.setDataSetSplitter(new OrderSplitter(testPercent, order));
            config.setDataSetFilter(new PreferredSizeFilter(attributesToFilter));
            LocalDateTime startTime = LocalDateTime.now();
            LOGGER.info("Starting SimpleMeliF at {}", startTime);
            BasicMeLiF basicMeLiF = new BasicMeLiF(config, dataSet);
            double pointValue = basicMeLiF.getSinglePointValue(point);
            LocalDateTime endTime = LocalDateTime.now();
            LOGGER.info("End time at {}", endTime);
            if (bestScore < pointValue){
                bestScore = pointValue;
            }
        }
        //return best value
        return bestScore;
    }
}
