package meLiF.classifier.weka;

import weka.classifiers.AbstractClassifier;
import weka.classifiers.lazy.IBk;



public class WekaKNN extends WekaClassifier {

    @Override
    protected AbstractClassifier createClassifier() {
        IBk classifier = new IBk();
        //TODO: tune
        return classifier;
    }
}