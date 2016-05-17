package ru.ifmo.ctddev.isaev.executable;

import filter.PreferredSizeFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.ifmo.ctddev.isaev.AlgorithmConfig;
import ru.ifmo.ctddev.isaev.DataSetReader;
import ru.ifmo.ctddev.isaev.classifier.Classifiers;
import ru.ifmo.ctddev.isaev.dataset.DataSet;
import ru.ifmo.ctddev.isaev.feature.measure.*;
import ru.ifmo.ctddev.isaev.melif.impl.BasicMeLiF;
import ru.ifmo.ctddev.isaev.melif.impl.ParallelMeLiF;
import ru.ifmo.ctddev.isaev.result.Point;
import ru.ifmo.ctddev.isaev.splitter.SequentalSplitter;

import java.io.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


/**
 * @author iisaev
 */
public class ParallelRunner {
    private static final Logger LOGGER = LoggerFactory.getLogger(ParallelRunner.class);

    public static void main(String[] args) {
        DataSetReader dataSetReader = new DataSetReader();
        DataSet dataSet = dataSetReader.readCsv(args[0]);
        List<Integer> order = IntStream.range(0, dataSet.getInstanceCount())
                .mapToObj(i -> i).collect(Collectors.toList());
        Collections.shuffle(order);
        Point[] points = new Point[] {
                new Point(1, 1, 1, 1),
                new Point(1, 0, 0, 0),
                new Point(0, 1, 0, 0),
                new Point(0, 0, 1, 0),
                new Point(0, 0, 0, 1),
        };
        RelevanceMeasure[] measures = new RelevanceMeasure[] {new VDM(), new FitCriterion(), new SymmetricUncertainty(), new SpearmanRankCorrelation()};
        AlgorithmConfig config = new AlgorithmConfig(0.25, Classifiers.WEKA_SVM, measures);
        config.setDataSetFilter(new PreferredSizeFilter(100));
        config.setDataSetSplitter(new SequentalSplitter(20));
        LocalDateTime startTime = LocalDateTime.now();
        BasicMeLiF meLif = new BasicMeLiF(config, dataSet);
        try {
            meLif.run(points, new PrintWriter(new File("123")));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        LocalDateTime starFinish = LocalDateTime.now();
        LOGGER.info("Finished BasicMeLiF at {}", starFinish);
        long starWorkTime = ChronoUnit.SECONDS.between(startTime, starFinish);
        LOGGER.info("Star work time: {} seconds", starWorkTime);
    }
}