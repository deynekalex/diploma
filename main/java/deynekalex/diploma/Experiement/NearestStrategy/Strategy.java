package deynekalex.diploma.Experiement.NearestStrategy;

import javafx.util.Pair;

import java.util.ArrayList;

/**
 * Created by deynekalex on 08.05.16.
 */
public abstract class Strategy {
    public abstract ArrayList<Pair<String,Double>> choose
            (ArrayList<Pair<String, Double>> metaInfo, ArrayList<String> trainNames);
}
