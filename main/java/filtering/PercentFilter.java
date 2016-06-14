package filtering;

import meLiF.dataset.Feature;
import meLiF.dataset.FeatureDataSet;
import meLiF.result.EvaluatedFeature;
import meLiF.result.Point;
import meLiF.result.RunStats;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * @author iisaev
 */
public class PercentFilter extends DatasetFilter {
    private final int percents;

    public PercentFilter(int percents) {
        this.percents = percents;
    }

    public FeatureDataSet filterDataSet(FeatureDataSet original, Point measureCosts,
                                        RunStats runStats) {
        List<EvaluatedFeature> evaluatedFeatures = evaluateFeatures(original, measureCosts, runStats).collect(Collectors.toList());
        int featureToSelect = (int) (((double) evaluatedFeatures.size() * percents) / 100);
        List<Feature> filteredFeatures = new ArrayList<>(evaluatedFeatures.subList(0, featureToSelect));
        return new FeatureDataSet(filteredFeatures, original.getClasses(), original.getName());
    }

}
