package ru.ifmo.ctddev.deyneka;

import filter.PercentFilter;
import filter.PreferredSizeFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.ifmo.ctddev.isaev.AlgorithmConfig;
import ru.ifmo.ctddev.isaev.DataSetReader;
import ru.ifmo.ctddev.isaev.classifier.Classifiers;
import ru.ifmo.ctddev.isaev.dataset.DataSet;
import ru.ifmo.ctddev.isaev.executable.ParallelRunner;
import ru.ifmo.ctddev.isaev.feature.measure.*;
import ru.ifmo.ctddev.isaev.melif.impl.BasicMeLiF;
import ru.ifmo.ctddev.isaev.result.Point;
import ru.ifmo.ctddev.isaev.result.RunStats;
import ru.ifmo.ctddev.isaev.splitter.OrderSplitter;
import ru.ifmo.ctddev.isaev.splitter.SequentalSplitter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by deynekalex on 30.04.16.
 */
public class StatExtracter {
    private static final Logger LOGGER = LoggerFactory.getLogger(ParallelRunner.class);
    static PrintWriter out;
    public boolean getStat(String curfile,double stepsize, int attributesToFilter, int testPercent) {
        String info = "(" + stepsize + ", " + attributesToFilter + ", " + testPercent + ")";
        String filename = Utils.getResultFileName(curfile,"_runInfo" + info, info + ".run");
        if (new File(filename).exists()){
            return true;
        }
        try {
            out = new PrintWriter(filename);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        execMelif(curfile, stepsize, attributesToFilter, testPercent);
        out.close();
        return true;
    }

    private void execMelif(String curFile,double stepSize, int attributesToFileter, int testPercent) {
        DataSetReader dataSetReader = new DataSetReader();
        DataSet dataSet = dataSetReader.readCsv(curFile);
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
        AlgorithmConfig config = new AlgorithmConfig(stepSize, Classifiers.WEKA_SVM, measures);
        config.setDataSetSplitter(new OrderSplitter(testPercent, order));
        config.setDataSetFilter(new PreferredSizeFilter(attributesToFileter));
        LocalDateTime startTime = LocalDateTime.now();
        LOGGER.info("Starting SimpleMeliF at {}", startTime);
        BasicMeLiF basicMeLiF = new BasicMeLiF(config, dataSet);
        basicMeLiF.run(points, out);
        LocalDateTime endTime = LocalDateTime.now();
        LOGGER.info("End time at {}", endTime);
    }
}
