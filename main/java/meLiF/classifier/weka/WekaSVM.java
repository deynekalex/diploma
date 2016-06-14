package meLiF.classifier.weka;

import weka.classifiers.AbstractClassifier;
import weka.classifiers.functions.SMO;



public class WekaSVM extends WekaClassifier {

    @Override
    protected AbstractClassifier createClassifier() {
        SMO classifier = new SMO();
        //TODO: tune
        classifier.setBuildLogisticModels(true);
        return classifier;
    }
}
