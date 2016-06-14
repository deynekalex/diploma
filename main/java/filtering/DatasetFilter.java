package filtering;

import meLiF.dataset.FeatureDataSet;
import meLiF.feature.MeasureEvaluator;
import meLiF.result.EvaluatedFeature;
import meLiF.result.Point;
import meLiF.result.RunStats;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.Stream;


/**
 * @author iisaev
 */
public abstract class DatasetFilter {
    private final MeasureEvaluator measureEvaluator = new MeasureEvaluator();

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    protected Stream<EvaluatedFeature> evaluateFeatures(FeatureDataSet original, Point measureCosts,
                                                        RunStats runStats) {
        return measureEvaluator
                .evaluateFeatureMeasures(
                        original.getFeatures().stream(),
                        original.getClasses(),
                        measureCosts,
                        runStats.getMeasures()
                ).sorted((o1, o2) -> {
                    if (o1.getMeasure() == o2.getMeasure()) {
                        return 0;
                    } else {
                        return o1.getMeasure() < o2.getMeasure() ? 1 : -1;
                    }
                });
    }

    public abstract FeatureDataSet filterDataSet(FeatureDataSet original, Point measureCosts,
                                                 RunStats runStats);
}
