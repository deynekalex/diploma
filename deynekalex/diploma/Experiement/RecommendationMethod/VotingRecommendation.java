package deynekalex.diploma.Experiement.RecommendationMethod;

import deynekalex.diploma.Experiement.DatasetInfo;
import deynekalex.diploma.Experiement.Point;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

/**
 * Created by deynekalex on 08.05.16.
 */
public class VotingRecommendation extends RecommendationMethod{
    int maxNumberOfPoints;

    public VotingRecommendation(int maxNumberOfPoints){
        this.maxNumberOfPoints = maxNumberOfPoints;
    }

    @Override
    public ArrayList<Point> exec(ArrayList<DatasetInfo> melifInfo) {
        ArrayList<Pair<Point, Double>> pointsAndQuality = new ArrayList<>();
        for(DatasetInfo datasetInfo : melifInfo){
            for(Point p : datasetInfo.bestPoints){
                boolean finded = false;
                Pair<Point,Double> temp = null;
                Double lastScore = 0.0;
                for(Pair<Point, Double> pair : pointsAndQuality){
                    Point point = pair.getKey();
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
        ArrayList<Point> result = new ArrayList<>();
        for(int i = 0; i < pointsAndQuality.size(); i++){
            if (i <= maxNumberOfPoints){
                result.add(pointsAndQuality.get(i).getKey());
            }
        }
        return result;
    }
}
