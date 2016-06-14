package meLiF.feature.measure;

import meLiF.dataset.Feature;

import java.util.List;



public interface RelevanceMeasure {
    double evaluate(Feature feature, List<Integer> classes);
}
