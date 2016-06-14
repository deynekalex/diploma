package meLiF.executable;

import meLiF.feature.measure.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import meLiF.DataSetReader;
import meLiF.dataset.DataSet;
import meLiF.result.Point;


/**
 * @author iisaev
 */
public class ClassifiersComparison extends Comparison {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClassifiersComparison.class);

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
        RelevanceMeasure[] measures = new RelevanceMeasure[] {new VDM(), new FitCriterion(), new SymmetricUncertainty(), new SpearmanRankCorrelation()};
        /*List<RunStats> allStats = IntStream.range(0, Classifiers.values().length)
                .mapToObj(i -> Classifiers.values()[i])
                .filtering(clf -> clf == Classifiers.WEKA_SVM)
                .map(clf -> {
                    LOGGER.info("Classifier: {}", clf);
                    AlgorithmConfig config = new AlgorithmConfig(0.1, clf, measures);
                    config.setDataSetSplitter(new RandomSplitter(20, 3));
                    config.setDataSetFilter(new PreferredSizeFilter(100));
                    ParallelMeLiF meLiF = new ParallelMeLiF(config, dataSet, 20);
                    RunStats result = null;
                    try {
                        result = meLiF.run(points, new PrintWriter(new File("1asdf")));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    return result;
                })
                .collect(Collectors.toList());
        RunStats svmStats = allStats.stream().filtering(s -> s.getUsedClassifier() == Classifiers.WEKA_SVM).findAny().get();
        AlgorithmConfig nopMelifConfig = new AlgorithmConfig(0.1, Classifiers.WEKA_SVM, measures);
        nopMelifConfig.setDataSetSplitter(new RandomSplitter(20, 3));
        nopMelifConfig.setDataSetFilter(new PreferredSizeFilter(100));
        RunStats nopMelifStats = new ParallelNopMeLiF(nopMelifConfig, 20, (int) svmStats.getVisitedPoints()).run(points, out);
        allStats.forEach(stats ->
                LOGGER.info("Classifier: {}; f1Score: {}; work time: {} seconds; visited points: {}", new Object[] {
                        stats.getUsedClassifier(),
                        stats.getBestResult().getF1Score(),
                        stats.getWorkTime(),
                        stats.getVisitedPoints()
                }));
        LOGGER.info("Nop classifier work time: {}; visitedPoints: {}", new Object[] {
                nopMelifStats.getWorkTime(),
                nopMelifStats.getVisitedPoints()
        });
        LOGGER.info("Percent of time spent to classifying for svm: {}%",
                getPercentImprovement(
                        svmStats.getWorkTime() / svmStats.getVisitedPoints(),
                        nopMelifStats.getWorkTime() / nopMelifStats.getVisitedPoints()
                ));*/
    }
}
