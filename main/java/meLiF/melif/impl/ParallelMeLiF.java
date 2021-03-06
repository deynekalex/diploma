package meLiF.melif.impl;

import meLiF.dataset.DataSet;
import meLiF.dataset.DataSetPair;
import meLiF.result.Point;
import meLiF.result.RunStats;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import meLiF.AlgorithmConfig;
import meLiF.dataset.FeatureDataSet;
import meLiF.dataset.InstanceDataSet;
import meLiF.result.SelectionResult;

import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;



public class ParallelMeLiF extends BasicMeLiF {
    private final ExecutorService executorService;

    private static final Logger LOGGER = LoggerFactory.getLogger(ParallelMeLiF.class);

    public ExecutorService getExecutorService() {
        return executorService;
    }

    protected final Set<Point> visitedPoints = new ConcurrentSkipListSet<>();

    public ParallelMeLiF(AlgorithmConfig config, DataSet dataSet, int threads) {
        this(config, dataSet, Executors.newFixedThreadPool(threads));
    }

    public ParallelMeLiF(AlgorithmConfig config, DataSet dataSet, ExecutorService executorService) {
        super(config, dataSet);
        this.executorService = executorService;
    }

    @Override
    public RunStats run(Point[] points, PrintWriter out) {
        Arrays.asList(points).forEach(p -> {
            if (p.getCoordinates().length != config.getMeasures().length) {
                throw new IllegalArgumentException("Each point must have same coordinates number as number of measures");
            }
        });

        RunStats runStats = new RunStats(config);
        LocalDateTime startTime = LocalDateTime.now();
        runStats.setStartTime(startTime);
        LOGGER.info("Started {} at {}", getClass().getSimpleName(), startTime);
        CountDownLatch pointsLatch = new CountDownLatch(points.length);
        List<Future<SelectionResult>> scoreFutures = Arrays.asList(points).stream()
                .map(p -> executorService.submit(() -> {
                    SelectionResult result = performCoordinateDescend(p, runStats);
                    pointsLatch.countDown();
                    return result;
                })).collect(Collectors.toList());
        try {
            pointsLatch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        List<SelectionResult> scores = scoreFutures.stream().map(f -> {
            try {
                return f.get();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList());
        LOGGER.info("Total scores: ");
        for(SelectionResult result : scores){
            System.out.print(result.getF1Score() + " - ");
            System.out.print(result.getPoint() + " initial point");
            System.out.println(result.getInitialPoint());
        }
        LOGGER.info("Max score: {} at point {}",
                runStats.getBestResult().getF1Score(),
                runStats.getBestResult().getPoint().getCoordinates()
        );
        LocalDateTime finishTime = LocalDateTime.now();
        runStats.setFinishTime(finishTime);
        LOGGER.info("Finished {} at {}", getClass().getSimpleName(), finishTime);
        LOGGER.info("Working time: {} seconds", ChronoUnit.SECONDS.between(startTime, finishTime));
        getExecutorService().shutdown();
        return runStats;
    }

    @Override
    public double getSinglePointValue(Point point) {
        return 0;
    }

    @Override
    protected SelectionResult visitPoint(Point point, RunStats measures, SelectionResult bestResult) {
        SelectionResult score = getSelectionResult(point, measures);
        visitedPoints.add(new Point(point));
        return score;
    }

    protected SelectionResult performCoordinateDescend(Point point, RunStats runStats) {
        SelectionResult bestScore = getSelectionResult(point, runStats);
        visitedPoints.add(point);
        if (runStats.getBestResult() != null && runStats.getScore() > bestScore.getF1Score()) {
            bestScore = runStats.getBestResult();
        }

        double[] coordinates = point.getCoordinates();
        boolean smthChanged = true;
        while (smthChanged) {
            smthChanged = false;
            for (int i = 0; i < coordinates.length; i++) {
                CountDownLatch latch = new CountDownLatch(2);

                Point plusDelta = new Point(coordinates);
                plusDelta.getCoordinates()[i] += config.getDelta();
                Future<SelectionResult> plusDeltaScore = getSelectionResultFuture(runStats, bestScore, plusDelta, latch);

                Point minusDelta = new Point(coordinates);
                minusDelta.getCoordinates()[i] -= config.getDelta();
                Future<SelectionResult> minusDeltaScore = getSelectionResultFuture(runStats, bestScore, minusDelta, latch);
                try {
                    latch.await();
                    if (plusDeltaScore.get().betterThan(bestScore)) {
                        bestScore = plusDeltaScore.get();
                        coordinates = plusDelta.getCoordinates();
                        smthChanged = true;
                    }
                    if (minusDeltaScore.get().betterThan(bestScore)) {
                        bestScore = minusDeltaScore.get();
                        coordinates = minusDelta.getCoordinates();
                        smthChanged = true;
                    }
                    if (smthChanged) {
                        break;
                    }
                } catch (InterruptedException e) {
                    throw new IllegalArgumentException("Waiting on latch interrupted!");
                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        System.out.println("<Best result>" + bestScore.getF1Score());
        System.out.println("<At point>" + bestScore.getPoint());
        System.out.println("<Initial point was>"+point);
        System.out.print("<Iterations to best point's>");
        for(int i = 0; i < runStats.getBestPoints().size()-1; i++){
            System.out.print(runStats.getBestPoints().get(i).toString() + ", ");
        }
        System.out.println(runStats.getBestPoints().get(runStats.getBestPoints().size() - 1));
        return bestScore;
    }

    private Future<SelectionResult> getSelectionResultFuture(RunStats runStats, SelectionResult bestScore, Point plusDelta, CountDownLatch latch) {
        Future<SelectionResult> plusDeltaScore;
        if (!visitedPoints.contains(plusDelta)) {
            plusDeltaScore = executorService.submit(() -> {
                SelectionResult result = visitPoint(plusDelta, runStats, bestScore);
                latch.countDown();
                return result;
            });
        } else {
            latch.countDown();
            plusDeltaScore = CompletableFuture.completedFuture(bestScore);
        }
        return plusDeltaScore;
    }

    protected SelectionResult getSelectionResult(Point point, RunStats stats) {
        FeatureDataSet filteredDs = datasetFilter.filterDataSet(dataSet.toFeatureSet(), point, stats);
        InstanceDataSet instanceDataSet = filteredDs.toInstanceSet();
        List<DataSetPair> dataSetPairs = datasetSplitter.split(instanceDataSet);
        CountDownLatch latch = new CountDownLatch(dataSetPairs.size());
        List<Double> f1Scores = Collections.synchronizedList(new ArrayList<>(dataSetPairs.size()));
        dataSetPairs.forEach(ds -> executorService.submit(() -> {
            double score = getF1Score(ds);
            f1Scores.add(score);
            latch.countDown();
        }));
        try {
            latch.await();
            double f1Score = f1Scores.stream().mapToDouble(d -> d).average().getAsDouble();
            LOGGER.debug("Point {}; F1 score: {}", Arrays.toString(point.getCoordinates()), f1Score);
            SelectionResult result = new SelectionResult(filteredDs.getFeatures(), point, point, f1Score);
            stats.updateBestResult(result);
            return result;
        } catch (InterruptedException e) {
            throw new IllegalStateException("Waiting on latch interrupted! ", e);
        }
    }
}
