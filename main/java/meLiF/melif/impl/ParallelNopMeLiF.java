package meLiF.melif.impl;

import meLiF.dataset.DataSetPair;
import meLiF.result.Point;
import meLiF.result.RunStats;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import meLiF.AlgorithmConfig;
import meLiF.dataset.FeatureDataSet;
import meLiF.result.SelectionResult;

import java.util.Collections;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * Implementation, that returns better result for each point until it reaches
 *
 * @author iisaev
 */
public class ParallelNopMeLiF extends ParallelMeLiF {

    private static final Logger LOGGER = LoggerFactory.getLogger(ParallelNopMeLiF.class);

    private final AtomicInteger visitedPointsCounter = new AtomicInteger(0);

    private final int pointsToVisit;

    public ParallelNopMeLiF(AlgorithmConfig config, int threads, int pointsToVisit) {
        super(config, new FeatureDataSet(Collections.emptyList(), Collections.emptyList(), "none"), threads);
        this.pointsToVisit = pointsToVisit;
    }

    @Override
    protected SelectionResult visitPoint(Point point, RunStats measures, SelectionResult bestResult) {
        SelectionResult score = getSelectionResult(point, measures);
        visitedPoints.add(new Point(point));
        if (score.compareTo(bestResult) == 1) {
            return score;
        } else {
            return bestResult;
        }
    }

    @Override
    protected double getF1Score(DataSetPair dsPair) {
        int visitedPoints = visitedPointsCounter.getAndIncrement();
        if (visitedPoints < pointsToVisit) {
            return (double) visitedPoints / pointsToVisit;
        } else {
            return 0;
        }
    }


    @Override
    public double getSinglePointValue(Point point) {
        return 0;
    }
}
