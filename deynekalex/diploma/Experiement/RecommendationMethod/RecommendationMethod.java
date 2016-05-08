package deynekalex.diploma.Experiement.RecommendationMethod;

import deynekalex.diploma.Experiement.DatasetInfo;
import deynekalex.diploma.Experiement.Point;

import java.util.ArrayList;

/**
 * Created by deynekalex on 08.05.16.
 */
public abstract class RecommendationMethod {
    public abstract ArrayList<Point> exec(ArrayList<DatasetInfo> melifInfo);
}
