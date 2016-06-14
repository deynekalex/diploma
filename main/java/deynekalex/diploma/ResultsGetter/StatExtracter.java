package deynekalex.diploma.ResultsGetter;

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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by deynekalex on 30.04.16.
 */
public class StatExtracter {
    private static final Logger LOGGER = LoggerFactory.getLogger(ParallelRunner.class);
    static PrintWriter out;
    public boolean getStat(String curfile,double stepsize, int attributesToFilter, int testPercent) {
        long startTime = System.nanoTime();
        String info = "(" + stepsize + ", " + attributesToFilter + ", " + testPercent + ")";
        String filename = Utils.getResultFileName(curfile+"2","_runInfo" + info, info + ".run");
        if (new File(filename).exists()){
            return true;
        }
        try {
            out = new PrintWriter(filename);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        execMelif(curfile, stepsize, attributesToFilter, testPercent);
        long endTime = System.nanoTime();
        long duration = (endTime - startTime);
        long minutes = TimeUnit.NANOSECONDS.toMinutes(duration);
        long seconds = TimeUnit.NANOSECONDS.toSeconds(duration);
        System.out.println(minutes + " минут," + seconds + "секунд");
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
