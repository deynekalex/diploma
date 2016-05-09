package deynekalex.diploma.Experiement;

import deynekalex.diploma.Experiement.NearestStrategy.Strategy;
import deynekalex.diploma.Experiement.NearestStrategy.StrategyKNearest;
import javafx.util.Pair;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import static deynekalex.diploma.MelifResultsPreprocess.Preprocessor.line;

/**
 * Created by deynekalex on 08.05.16.
 */
public class NearestFinder {
    ArrayList<String> metaFiles;
    String metaFolderName;
    int nearestDatasetsCount;

    NearestFinder(ArrayList<String> metaFiles, String metaFolderName, int nearestDatasetsCount){
        this.metaFiles = metaFiles;
        this.metaFolderName = metaFolderName;
        this.nearestDatasetsCount = nearestDatasetsCount;
    }

    public ArrayList<Pair<String, Double>> getSimillarDatasetNames(String testName, ArrayList<String> trainNames) {
        ArrayList<Pair<String,Double>> nearestNames = null;
        for(String metaFileName : metaFiles){
            if (metaFileName.contains(testName)){
                ArrayList<Pair<String, Double>> metaInfo = null;
                try {
                    metaInfo = readMetaInfo(metaFileName);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                nearestNames = getNearest(metaInfo, trainNames, new StrategyKNearest(5));
            }
        }
        return nearestNames;
    }

    private static ArrayList<Pair<String,Double>> getNearest(ArrayList<Pair<String, Double>> metaInfo, ArrayList<String> trainNames, Strategy strategy) {
        return strategy.choose(metaInfo,trainNames);
    }

    private ArrayList<Pair<String, Double>> readMetaInfo(String metaFileName) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(metaFolderName + "/" + metaFileName));
        ArrayList<Pair<String, Double>> result = new ArrayList<>();
        //reading results from points
        int pointSize;
        line = br.readLine();
        while ((line = br.readLine()) != null){
            String parts[] = line.split(" - ");
            Pair<String,Double> t = new Pair<>(
                    parts[1].substring(parts[1].lastIndexOf('/') + 1, parts[1].lastIndexOf("(")),
                    Double.parseDouble(parts[0]));
            result.add(t);
        }
        br.close();
        return result;
    }
}
