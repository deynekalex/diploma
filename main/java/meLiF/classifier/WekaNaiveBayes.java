package meLiF.classifier;

import meLiF.classifier.weka.WekaClassifier;
import weka.classifiers.AbstractClassifier;
import weka.classifiers.bayes.NaiveBayes;


/**
 * @author iisaev
 */
public class WekaNaiveBayes extends WekaClassifier {
    @Override
    protected AbstractClassifier createClassifier() {
        NaiveBayes classifier = new NaiveBayes();
        //TODO: train
        return classifier;
    }
}
