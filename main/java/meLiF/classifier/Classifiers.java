package meLiF.classifier;

import meLiF.classifier.weka.*;


public enum Classifiers {
    WEKA_HOEFD(WekaHoeffdingTree.class),
    WEKA_J48(WekaJ48.class),
    WEKA_KNN(WekaKNN.class),
    WEKA_LMT(WekaLMT.class),
    WEKA_PERCEPTRON(WekaMultilayerPerceptron.class),
    WEKA_PART(WekaPART.class),
    WEKA_RAND_FOREST(WekaRandomForest.class),
    WEKA_SVM(WekaSVM.class),
    WEKA_NAIVE_BAYES(WekaNaiveBayes.class);

    private Class<? extends Classifier> typeToken;

    Classifiers(Class<? extends Classifier> typeToken) {
        this.typeToken = typeToken;
    }

    public Classifier newClassifier() {
        try {
            return typeToken.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new IllegalStateException("Failed to instantiate Classifier");
        }
    }
}
