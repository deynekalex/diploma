package meLiF.melif.impl;

import meLiF.AlgorithmConfig;
import meLiF.dataset.DataSet;
import meLiF.result.RunStats;
import meLiF.result.OptimizationPoint;
import meLiF.result.Point;
import meLiF.result.SelectionResult;

import java.util.Comparator;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;



public class MeLifStar extends ParallelMeLiF {

    public MeLifStar(AlgorithmConfig config, DataSet dataSet, int threads) {
        super(config, dataSet, new ForkJoinPool(threads));
    }

    protected SelectionResult performCoordinateDescend(Point point, RunStats runStats) {
        SelectionResult result = getSelectionResult(point, runStats);
        visitedPoints.add(point);
        return processChildren(result, runStats);
    }

    private SelectionResult processChildren(SelectionResult parent, RunStats runStats) {
        List<OptimizationPoint> neighbours = getNeighbours(parent.getPoint());
        CountDownLatch latch = new CountDownLatch(neighbours.size());
        Queue<SelectionResult> results = new ConcurrentLinkedQueue<>();
        neighbours.forEach(p -> {
            if (!visitedPoints.contains(p)) {
                visitedPoints.add(p);
                getExecutorService().submit(() -> {
                    results.add(getSelectionResult(p, runStats));
                    latch.countDown();
                });
            } else {
                latch.countDown();
            }
        });
        try {
            latch.await();
            return Stream.concat(
                    Stream.of(parent),
                    results.stream()
                            .filter(res -> res.getF1Score() > parent.getF1Score())
                            .map(ep -> processChildren(ep, runStats))
            ).min(Comparator.comparingDouble(SelectionResult::getF1Score)).get();
        } catch (InterruptedException e) {
            throw new RuntimeException("Operation was interrupted", e);
        }
    }

    List<OptimizationPoint> getNeighbours(Point point) {
        return IntStream.range(0, point.getCoordinates().length)
                .mapToObj(i -> i)
                .flatMap(i -> Stream.of(
                        new OptimizationPoint(point, (arr) -> {
                            arr[i] += config.getDelta();
                        }),
                        new OptimizationPoint(point, (arr) -> {
                            arr[i] -= config.getDelta();
                        })
                )).collect(Collectors.toList());
    }

}
