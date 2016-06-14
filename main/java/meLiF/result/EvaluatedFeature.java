package meLiF.result;

import meLiF.dataset.Feature;


/**
 * @author iisaev
 */
public class EvaluatedFeature extends Feature {

    private double measure;

    public EvaluatedFeature(Feature feature, double measure) {
        super(feature.getName(), feature.getValues());
        this.measure = measure;
    }

    public double getMeasure() {
        return measure;
    }
}
