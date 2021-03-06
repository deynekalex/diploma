package meLiF.feature.measure;

import meLiF.dataset.Feature;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;



public class SymmetricUncertainty extends CorrelationBasedMeasure {
    private static final double LOG_2 = Math.log(2);

    @Override
    public double evaluate(Feature feature, List<Integer> classes) {
        double xPriorEntropy = getPriorEntropy(calculateDistribution(feature.getValues()));
        double yPriorEntropy = getPriorEntropy(calculateDistribution(classes));
        double posteriorEntropy = getConditionalEntropy(feature.getValues(), classes);
        return 2 * (xPriorEntropy - posteriorEntropy) / (xPriorEntropy + yPriorEntropy);
    }

    /**
     * <a href="https://en.wikipedia.org/wiki/Conditional_entropy">link to formula</a>
     *
     * @param featureValues
     * @param classes
     *
     * @return
     */
    private double getConditionalEntropy(List<Integer> featureValues, List<Integer> classes) {
        Map<Integer, Distribution> yDistribution = calculateDistribution(classes, featureValues);
        return yDistribution.entrySet().stream()
                .mapToDouble(xEntry -> {
                    Distribution distrib = xEntry.getValue();
                    Double px = (double) distrib.getSum() / classes.size();
                    return px * getPriorEntropy(distrib);
                }).sum();
    }

    private double getPriorEntropy(Distribution distribution) {
        return -distribution.getDistribution().entrySet().stream().map(e -> {
            double p = (double) e.getValue() / distribution.getSum();
            double log2P = Math.log(p) / LOG_2;
            return p * log2P;
        }).collect(Collectors.summingDouble(d -> d));
    }
}
