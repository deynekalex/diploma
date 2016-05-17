package deynekalex.diploma.Experiement;

import deynekalex.diploma.Experiement.RecommendationMethod.RecommendationMethod;
import javafx.util.Pair;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import static deynekalex.diploma.MelifResultsPreprocess.Preprocessor.line;

/**
 * Created by deynekalex on 08.05.16.
 */
public class Recommender {
    String melifFolderName;
    ArrayList<String> metaFiles;
    ArrayList<String> melifFiles;
    RecommendationMethod recommendationMethod;

    Recommender(String melifFolderName, ArrayList<String> metaFiles,
                ArrayList<String> melifFiles, RecommendationMethod method){
        this.melifFolderName = melifFolderName;
        this.metaFiles = metaFiles;
        this.melifFiles = melifFiles;
        this.recommendationMethod = method;
    }

    public ArrayList<MyPoint> recommend(ArrayList<Pair<String, Double>> nearestNames) {
        //get ArrayList with names and distance
        //read all melif stat for nearestNames
        ArrayList<DatasetInfo> melifInfo = new ArrayList<>();
        for(Pair<String, Double> pair : nearestNames){
            for(String melifFileName : melifFiles){
                if (melifFileName.contains(pair.getKey())){
                    try {
                        melifInfo.add(readMelifStat(melifFileName, pair.getValue()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    continue;
                }
            }
        }
        //run RecommendationStrategy on melifInfo
        ArrayList<MyPoint> res = recommendationMethod.exec(melifInfo);
        return res;
    }

    public DatasetInfo readMelifStat(String fileName, Double distance) throws IOException {
        DatasetInfo datasetInfo = new DatasetInfo();
        datasetInfo.distance = distance;
        BufferedReader br = new BufferedReader(new FileReader(melifFolderName + "/" + fileName));
        datasetInfo.bestScore = Double.parseDouble(br.readLine());
        while ((line = br.readLine()) != null){
            String[] parts = line.split(", ");
            MyPoint p = new MyPoint();
            for(String s : parts){
                p.coordinates.add(Double.parseDouble(s));
            }
            datasetInfo.bestPoints.add(p);
        }
        br.close();
        return datasetInfo;
    }
}
