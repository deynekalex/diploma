package ru.ifmo.ctddev.isaev.melif.impl;

import javafx.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.ifmo.ctddev.isaev.AlgorithmConfig;
import ru.ifmo.ctddev.isaev.ScoreCalculator;
import ru.ifmo.ctddev.isaev.classifier.Classifier;
import ru.ifmo.ctddev.isaev.dataset.*;
import filter.DatasetFilter;
import ru.ifmo.ctddev.isaev.melif.MeLiF;
import ru.ifmo.ctddev.isaev.result.Point;
import ru.ifmo.ctddev.isaev.result.RunStats;
import ru.ifmo.ctddev.isaev.result.SelectionResult;
import ru.ifmo.ctddev.isaev.splitter.DatasetSplitter;

import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;


/**
 * Core single-threaded MeLiF implementation
 *
 * @author iisaev
 */
public class BasicMeLiF implements MeLiF {

    static PrintWriter out;
    
    private double bestGlobalPointScore = 0;
    
    private Point bestGlobalPoint = null;

    private volatile List<Pair<Long,Point>> bestGlobalList = Collections.synchronizedList(new ArrayList<Pair<Long,Point>>());

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    protected final Set<Point> visitedPoints = new TreeSet<>();

    protected final DatasetFilter datasetFilter;

    protected final DatasetSplitter datasetSplitter;

    private static final ScoreCalculator scoreCalculator = new ScoreCalculator();

    protected final AlgorithmConfig config;

    protected final DataSet dataSet;

    public BasicMeLiF(AlgorithmConfig config, DataSet dataSet) {
        this.config = config;
        this.datasetSplitter = config.getDataSetSplitter();
        this.datasetFilter = config.getDataSetFilter();
        this.dataSet = dataSet;
    }

    @Override
    public RunStats run(Point[] points, PrintWriter file) {
        Arrays.asList(points).forEach(p -> {
            if (p.getCoordinates().length != config.getMeasures().length) {
                throw new IllegalArgumentException("Each point must have same coordinates number as number of measures");
            }
        });

        this.out = file;

        RunStats runStats = new RunStats(config);

        LocalDateTime startTime = LocalDateTime.now();
        runStats.setStartTime(startTime);
        logger.info("<Started {} at {}>", getClass().getSimpleName(), startTime);
        List<SelectionResult> scores = Arrays.asList(points).stream()
                .map(p -> performCoordinateDescend(p, runStats))
                .collect(Collectors.toList());
        logger.info("Total scores: ");

        out.println();
        out.println("<---Global best Point--->");
        out.println();
        out.println("<Start point>"+bestGlobalPoint);
        out.println("<Best result from point>" + bestGlobalPointScore);
        out.println("<Iterations to point(s)>");
        for(int i = 0; i < bestGlobalList.size()-1; i++){
            out.print("  <" + (i+1) +">");
            out.print(bestGlobalList.get(i).getKey().toString() + " - ");
            out.println(bestGlobalList.get(i).getValue().toString());
        }
        out.print("  <" + (bestGlobalList.size()) + ">");
        out.print(bestGlobalList.get(bestGlobalList.size() - 1).getKey().toString() + " - ");
        out.println(bestGlobalList.get(bestGlobalList.size() - 1).getValue().toString());
        
        LocalDateTime finishTime = LocalDateTime.now();
        runStats.setFinishTime(finishTime);
        logger.info("Finished {} at {}", getClass().getSimpleName(), finishTime);
        logger.info("Working time: {} seconds", ChronoUnit.SECONDS.between(startTime, finishTime));
        return runStats;
    }

    @Override
    public double getSinglePointValue(Point point) {
        Arrays.asList(point).forEach(p -> {
            if (p.getCoordinates().length != config.getMeasures().length) {
                throw new IllegalArgumentException("Each point must have same coordinates number as number of measures");
            }
        });
        RunStats runStats = new RunStats(config);
        SelectionResult bestScore = getSelectionResult(point, runStats);
        return bestScore.getF1Score();
    }

    public Pair<Double, List<Pair<Long,Point>>> run(Point[] points){
        Arrays.asList(points).forEach(p -> {
            if (p.getCoordinates().length != config.getMeasures().length) {
                throw new IllegalArgumentException("Each point must have same coordinates number as number of measures");
            }
        });

        RunStats runStats = new RunStats(config);
        LocalDateTime startTime = LocalDateTime.now();
        runStats.setStartTime(startTime);
        logger.info("<Started {} at {}>", getClass().getSimpleName(), startTime);
        List<SelectionResult> scores = Arrays.asList(points).stream()
                .map(p -> performCoordinateDescend(p, runStats))
                .collect(Collectors.toList());
        //bestGlobalPointScore
        //bestGlobalList
        Pair<Double, List<Pair<Long,Point>>> result = new Pair<>(bestGlobalPointScore, bestGlobalList);
        LocalDateTime finishTime = LocalDateTime.now();
        runStats.setFinishTime(finishTime);
        logger.info("Finished {} at {}", getClass().getSimpleName(), finishTime);
        logger.info("Working time: {} seconds", ChronoUnit.SECONDS.between(startTime, finishTime));
        return result;
    }

    protected SelectionResult visitPoint(Point point, RunStats measures, SelectionResult bestResult) {
        if (!visitedPoints.contains(point)) {
            SelectionResult score = getSelectionResult(point, measures);
            visitedPoints.add(new Point(point));
            return score;
        }
        return bestResult;
    }

    protected SelectionResult performCoordinateDescend(Point point, RunStats runStats) {
        SelectionResult bestScore = getSelectionResult(point, runStats);
        visitedPoints.add(point);
        if (runStats.getBestResult() != null && runStats.getScore() > bestScore.getF1Score()) {
            bestScore = runStats.getBestResult();
        }

        boolean smthChanged = true;
        double[] coordinates = point.getCoordinates();

        while (smthChanged) {
            smthChanged = false;

            for (int i = 0; i < coordinates.length; i++) {

                Point plusDelta = new Point(coordinates);
                plusDelta.getCoordinates()[i] += config.getDelta();
                SelectionResult plusScore = visitPoint(plusDelta, runStats, bestScore);
                if (plusScore.betterThan(bestScore)) {
                    bestScore = plusScore;
                    coordinates = plusDelta.getCoordinates();
                    smthChanged = true;
                    break;
                }

                Point minusDelta = new Point(coordinates);
                minusDelta.getCoordinates()[i] -= config.getDelta();
                SelectionResult minusScore = visitPoint(minusDelta, runStats, bestScore);
                if (minusScore.betterThan(bestScore)) {
                    bestScore = minusScore;
                    coordinates = minusDelta.getCoordinates();
                    smthChanged = true;
                    break;
                }
            }
        }
        /*out.println("<Start point>"+point);
        out.println("<Best result from point>" + bestScore.getF1Score());
        out.println("<Iterations to point(s)>");*/
        for(int i = 0; i < runStats.getBestPoints().size()-1; i++){
            if (bestGlobalPointScore < bestScore.getF1Score()
                    || (bestGlobalPointScore == bestScore.getF1Score()
                            && bestGlobalList.size() > 0
                            && runStats.getBestPoints().size() > 0
                            && bestGlobalList.get(0).getKey() > runStats.getBestPoints().get(0).getKey())){
                //current is not the best ->
                bestGlobalList.clear();
                bestGlobalPoint = point;
                bestGlobalPointScore = bestScore.getF1Score();
                bestGlobalList.add(runStats.getBestPoints().get(i));
            }
            else
            if (bestGlobalPointScore == bestScore.getF1Score()
                    && bestScore.getPoint().compareTo(bestGlobalPoint) == 0){//current point
                bestGlobalList.add(runStats.getBestPoints().get(i));
            }
            /*out.print("  <" + (i+1) +">");
            out.print(runStats.getBestPoints().get(i).getKey().toString() + " - ");
            out.println(runStats.getBestPoints().get(i).getValue().toString());*/
        }
        if (bestGlobalPointScore < bestScore.getF1Score()
                || (bestGlobalPointScore == bestScore.getF1Score()
                && bestGlobalList.size() > 0
                && runStats.getBestPoints().size() > 0
                && bestGlobalList.get(0).getKey() > runStats.getBestPoints().get(0).getKey())){
            bestGlobalList.clear();
            bestGlobalPoint = point;
            bestGlobalPointScore = bestScore.getF1Score();
            bestGlobalList.add(runStats.getBestPoints().get(runStats.getBestPoints().size() - 1));
        }else
        if (bestGlobalPointScore == bestScore.getF1Score()
                && bestScore.getPoint().compareTo(bestGlobalPoint) == 0){
            bestGlobalList.add(runStats.getBestPoints().get(runStats.getBestPoints().size() - 1));
        }
       /*out.print("  <" + (runStats.getBestPoints().size()) + ">");
        out.print(runStats.getBestPoints().get(runStats.getBestPoints().size() - 1).getKey().toString() + " - ");
        out.println(runStats.getBestPoints().get(runStats.getBestPoints().size() - 1).getValue().toString());
        out.println();*/
        runStats.clearStat();
        return bestScore;
    }

    protected double getF1Score(DataSetPair dsPair) {
        Classifier classifier = config.getClassifiers().newClassifier();
        classifier.train(dsPair.getTrainSet());
        List<Integer> actual = classifier.test(dsPair.getTestSet())
                .stream()
                .map(d -> (int) Math.round(d))
                .collect(Collectors.toList());
        List<Integer> expectedValues = dsPair.getTestSet().toInstanceSet().getInstances().stream().map(DataInstance::getClazz).collect(Collectors.toList());
        if (logger.isTraceEnabled()) {
            logger.trace("Expected values: {}", Arrays.toString(expectedValues.toArray()));
            logger.trace("Actual values: {}", Arrays.toString(actual.toArray()));
        }
        return scoreCalculator.calculateF1Score(expectedValues, actual);
    }

    protected SelectionResult getSelectionResult(Point point, RunStats stats) {
        FeatureDataSet filteredDs = datasetFilter.filterDataSet(dataSet.toFeatureSet(), point, stats);
        InstanceDataSet instanceDataSet = filteredDs.toInstanceSet();
        List<Double> f1Scores = datasetSplitter.split(instanceDataSet)
                .stream().map(this::getF1Score)
                .collect(Collectors.toList());
        double f1Score = f1Scores.stream().mapToDouble(d -> d).average().getAsDouble();
        logger.debug("Point {}; F1 score: {}", Arrays.toString(point.getCoordinates()), f1Score);
        SelectionResult result = new SelectionResult(filteredDs.getFeatures(), point, point, f1Score);
        stats.updateBestResultUnsafe(result);
        return result;
    }
}
