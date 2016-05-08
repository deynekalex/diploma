package deynekalex.diploma.Experiement.NearestStrategy;

import javafx.util.Pair;

import java.util.ArrayList;

/**
 * Created by deynekalex on 08.05.16.
 */
public class StrategyKNearest extends Strategy{
    int k = 0;
    public StrategyKNearest(int k){
        this.k = k;
    }

    @Override
    public ArrayList<Pair<String, Double>> choose(ArrayList<Pair<String, Double>> metaInfo, ArrayList<String> trainNames) {
        ArrayList<Pair<String,Double>> result = new ArrayList<>();
        for(Pair<String, Double> pair : metaInfo){
            Double dist = pair.getValue();
            String fileName = pair.getKey();
            if (!trainNames.contains(fileName)){
                continue;
            }else{
                result.add(pair);
            }
            if (result.size() == k){
                break;
            }
        }
        return result;
    }
}
