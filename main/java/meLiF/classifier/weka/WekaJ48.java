package meLiF.classifier.weka;

import weka.classifiers.AbstractClassifier;
import weka.classifiers.trees.J48;



public class WekaJ48 extends WekaClassifier {

    @Override
    protected AbstractClassifier createClassifier() {
        J48 classifier = new J48();
        //TODO: tune
        return classifier;
    }
}
