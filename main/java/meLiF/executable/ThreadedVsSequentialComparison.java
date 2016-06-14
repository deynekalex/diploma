package meLiF.executable;

import filtering.PercentFilter;
import meLiF.feature.measure.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import meLiF.AlgorithmConfig;
import meLiF.DataSetReader;
import meLiF.classifier.Classifiers;
import meLiF.dataset.DataSet;
import meLiF.melif.impl.BasicMeLiF;
import meLiF.result.Point;
import meLiF.result.RunStats;
import meLiF.splitter.OrderSplitter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;



public class ThreadedVsSequentialComparison extends Comparison {
    private static final Logger LOGGER = LoggerFactory.getLogger(ThreadedVsSequentialComparison.class);

    public static void main(String[] args) {
        DataSetReader dataSetReader = new DataSetReader();
        DataSet dataSet = dataSetReader.readCsv(args[0]);
        List<Integer> order = IntStream.range(0, dataSet.getInstanceCount())
                .mapToObj(i -> i).collect(Collectors.toList());
        Collections.shuffle(order);
        Point[] points = new Point[] {
                new Point(1, 0, 0, 0),
                new Point(0, 1, 0, 0),
                new Point(0, 0, 1, 0),
                new Point(0, 0, 0, 1),
                new Point(1, 1, 1, 1),
        };
        RelevanceMeasure[] measures = new RelevanceMeasure[] {new VDM(), new FitCriterion(), new SymmetricUncertainty(), new SpearmanRankCorrelation()};
        AlgorithmConfig config = new AlgorithmConfig(0.25, Classifiers.WEKA_SVM, measures);
        config.setDataSetSplitter(new OrderSplitter(20, order));
        config.setDataSetFilter(new PercentFilter(1));
        LocalDateTime startTime = LocalDateTime.now();
        LOGGER.info("Starting SimpleMeliF at {}", startTime);
        BasicMeLiF basicMeLiF = new BasicMeLiF(config, dataSet);
        RunStats simpleStats = null;
        PrintWriter out = null;
        try {
            out = new PrintWriter(new File("123"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        simpleStats = basicMeLiF.run(points, out);
        LocalDateTime simpleFinish = LocalDateTime.now();
        LOGGER.info("Starting ParallelMeliF at {}", simpleFinish);
        out.close();
        /*ParallelMeLiF parallelMeLiF = new ParallelMeLiF(config, dataSet, threads);
        RunStats parallelStats = null;
        try {
            parallelStats = parallelMeLiF.run(points,new PrintWriter(new File("123")));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        LocalDateTime parallelFinish = LocalDateTime.now();

        long simpleWorkTime = ChronoUnit.SECONDS.between(startTime, simpleFinish);
        long parallelWorkTime = ChronoUnit.SECONDS.between(simpleFinish, parallelFinish);
        LOGGER.info("Single-threaded work time: {} seconds", simpleWorkTime);
        LOGGER.info("Visited {} points; best point is {} with score {}", new Object[] {
                simpleStats.getVisitedPoints(),
                simpleStats.getBestResult().getPoint().getCoordinates(),
                simpleStats.getBestResult().getF1Score()
        });
        LOGGER.info("Multi-threaded work time: {} seconds", parallelWorkTime);
        LOGGER.info("Visited {} points; best point is {} with score {}", new Object[] {
                parallelStats.getVisitedPoints(),
                parallelStats.getBestResult().getPoint().getCoordinates(),
                parallelStats.getBestResult().getF1Score()
        });
        LOGGER.info("Multi-threaded to single-threaded version speed improvement: {}%",
                getSpeedImprovementPercent(simpleStats.getWorkTime(), parallelStats.getWorkTime()));
        parallelMeLiF.getExecutorService().shutdown()*/;
    }
}
