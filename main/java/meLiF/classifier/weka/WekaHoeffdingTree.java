package meLiF.classifier.weka;

import weka.classifiers.AbstractClassifier;
import weka.classifiers.trees.HoeffdingTree;



public class WekaHoeffdingTree extends WekaClassifier {

    @Override
    protected AbstractClassifier createClassifier() {
        HoeffdingTree classifier = new HoeffdingTree();
        //TODO: tune
        return classifier;
    }
}
