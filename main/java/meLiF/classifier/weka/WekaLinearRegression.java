package meLiF.classifier.weka;

import weka.classifiers.AbstractClassifier;
import weka.classifiers.functions.LinearRegression;



@Deprecated
public class WekaLinearRegression extends WekaClassifier {

    @Override
    protected AbstractClassifier createClassifier() {
        LinearRegression classifier = new LinearRegression();
        //TODO: tune
        return classifier;
    }
}
