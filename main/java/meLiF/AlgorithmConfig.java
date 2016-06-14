package meLiF;

import meLiF.classifier.Classifiers;
import meLiF.feature.measure.RelevanceMeasure;
import meLiF.splitter.DatasetSplitter;
import filtering.DatasetFilter;



public class AlgorithmConfig {
    private final double delta;

    private final Classifiers classifiers;

    private final RelevanceMeasure[] measures;

    private DatasetFilter dataSetFilter;

    private DatasetSplitter dataSetSplitter;

    public AlgorithmConfig(double delta, Classifiers classifiers, RelevanceMeasure[] measures) {
        this.delta = delta;
        this.classifiers = classifiers;
        this.measures = measures;
    }

    public double getDelta() {
        return delta;
    }

    public Classifiers getClassifiers() {
        return classifiers;
    }

    public RelevanceMeasure[] getMeasures() {
        return measures;
    }

    public DatasetSplitter getDataSetSplitter() {
        return dataSetSplitter;
    }

    public DatasetFilter getDataSetFilter() {
        return dataSetFilter;
    }

    public void setDataSetFilter(DatasetFilter dataSetFilter) {
        this.dataSetFilter = dataSetFilter;
    }

    public void setDataSetSplitter(DatasetSplitter datasetSplitter) {
        this.dataSetSplitter = datasetSplitter;
    }
}
