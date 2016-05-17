package deynekalex.diploma.Experiement.RecommendationMethod;

import deynekalex.diploma.Experiement.MyPoint;
import deynekalex.diploma.Experiement.DatasetInfo;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by deynekalex on 08.05.16.
 */
public class VotingRecommendation extends RecommendationMethod{
    int maxNumberOfPoints;

    public VotingRecommendation(int maxNumberOfPoints){
        this.maxNumberOfPoints = maxNumberOfPoints;
    }

    @Override
    public ArrayList<MyPoint> exec(ArrayList<DatasetInfo> melifInfo) {
        ArrayList<Pair<MyPoint, Double>> pointsAndQuality = new ArrayList<>();
        for(DatasetInfo datasetInfo : melifInfo){
            for(MyPoint p : datasetInfo.bestPoints){
                boolean finded = false;
                Pair<MyPoint,Double> temp = null;
                Double lastScore = 0.0;
                for(Pair<MyPoint, Double> pair : pointsAndQuality){
                    MyPoint point = pair.getKey();
                    lastScore = pair.getValue();
                    if (p.equals(point)){
                        finded = true;
                        temp = pair;
                        break;
                    }
                }
                if (!finded){
                    pointsAndQuality.add(new Pair<>(p, 1/datasetInfo.distance));
                }else{
                    pointsAndQuality.remove(temp);
                    pointsAndQuality.add(new Pair<>(p,lastScore + 1/datasetInfo.distance));
                }
            }
        }
        Collections.sort(pointsAndQuality, (o1, o2) -> {
            if (o1.getValue() < o2.getValue()){
                return 1;
            }else if (o1.getValue().equals(o2.getValue())){
                return 0;
            }
            return -1;
        });
        ArrayList<MyPoint> result = new ArrayList<>();
        for(int i = 0; i < pointsAndQuality.size(); i++){
            if (i <= maxNumberOfPoints){
                result.add(pointsAndQuality.get(i).getKey());
            }
        }
        return result;
    }
}
