package meLiF.classifier.weka;

import weka.classifiers.AbstractClassifier;
import weka.classifiers.functions.MultilayerPerceptron;



public class WekaMultilayerPerceptron extends WekaClassifier {

    @Override
    protected AbstractClassifier createClassifier() {
        MultilayerPerceptron classifier = new MultilayerPerceptron();
        //TODO: tune
        return classifier;
    }
}
