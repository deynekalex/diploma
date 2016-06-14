package meLiF.executable;

import meLiF.dataset.DataSet;
import meLiF.result.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import meLiF.DataSetReader;



public class MeLiFStarRunner {
    private static final Logger LOGGER = LoggerFactory.getLogger(MeLiFStarRunner.class);

    public static void main(String[] args) {
        DataSetReader dataSetReader = new DataSetReader();
        DataSet dataSet = dataSetReader.readCsv(args[0]);
        Point[] points = new Point[] {
                new Point(1, 0, 0, 0),
                new Point(0, 1, 0, 0),
                new Point(0, 0, 1, 0),
                new Point(0, 0, 0, 1),
                new Point(1, 1, 1, 1)
        };
       /* RelevanceMeasure[] measures = new RelevanceMeasure[] {new VDM(), new FitCriterion(), new SymmetricUncertainty(), new SpearmanRankCorrelation()};
        AlgorithmConfig config = new AlgorithmConfig(0.3, Classifiers.WEKA_SVM, measures);
        config.setDataSetSplitter(new RandomSplitter(20, 3));
        config.setDataSetFilter(new PreferredSizeFilter(100));
        LocalDateTime startTime = LocalDateTime.now();
        MeLifStar meLifStar = new MeLifStar(config, dataSet, 20);
        meLifStar.run(points, out);
        LocalDateTime starFinish = LocalDateTime.now();
        LOGGER.info("Finished MeLifStar at {}", starFinish);
        long starWorkTime = ChronoUnit.SECONDS.between(startTime, starFinish);
        LOGGER.info("Star work time: {} seconds", starWorkTime);
        meLifStar.getExecutorService().shutdown();*/
    }
}
