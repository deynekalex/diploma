package filtering;

import meLiF.dataset.Feature;
import meLiF.dataset.FeatureDataSet;
import meLiF.result.Point;
import meLiF.result.RunStats;

import java.util.List;
import java.util.stream.Collectors;


/**
 * @author iisaev
 */
public class PreferredSizeFilter extends DatasetFilter {
    private final int preferredSize;

    public PreferredSizeFilter(int preferredSize) {
        this.preferredSize = preferredSize;
        logger.info("Initialized dataset filtering with preferred size {}", preferredSize);
    }

    public FeatureDataSet filterDataSet(FeatureDataSet original, Point measureCosts,
                                        RunStats runStats) {
        List<Feature> filteredFeatures = evaluateFeatures(original, measureCosts, runStats)
                .limit(preferredSize)
                .collect(Collectors.toList());
        return new FeatureDataSet(filteredFeatures, original.getClasses(), original.getName());
    }

}
