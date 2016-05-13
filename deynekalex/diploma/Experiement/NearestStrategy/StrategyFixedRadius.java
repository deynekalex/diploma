package deynekalex.diploma.Experiement.NearestStrategy;

import javafx.util.Pair;

import java.util.ArrayList;

/**
 * Created by deynekalex on 09.05.16.
 */
public class StrategyFixedRadius extends Strategy{
    Double radius;

    public StrategyFixedRadius(Double radius){
        this.radius = radius;
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
                if (result.size() == 0){
                    result.add(pair);
                    continue;
                }
                if (dist <= radius){
                    result.add(pair);
                }else{
                    break;
                }
            }
        }
        return result;
    }
}
