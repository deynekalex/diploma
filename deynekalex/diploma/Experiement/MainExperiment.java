package deynekalex.diploma.Experiement;

import deynekalex.diploma.Experiement.NearestStrategy.StrategyKNearest;
import deynekalex.diploma.Experiement.NearestStrategy.Strategy;
import deynekalex.diploma.Experiement.RecommendationMethod.VotingRecommendation;
import javafx.util.Pair;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import static deynekalex.diploma.MelifResultsPreprocess.Preprocessor.line;

/**
 * Created by deynekalex on 13.04.16.
 */
public class MainExperiment {
    static ArrayList<String> datasetFiles = new ArrayList<>();
    static ArrayList<String> metaFiles = new ArrayList<>();
    static ArrayList<String> melifFiles = new ArrayList<>();

    static String datasetFolderName;
    static String metaFolderName;
    static String melifFolderName;

    static ArrayList<String> names = new ArrayList<>();
    static ArrayList<String> trainNames = new ArrayList<>();
    static ArrayList<String> testNames = new ArrayList<>();

    static void shuffleAndSplit(double testPercent){
        Collections.shuffle(names);
        int testCount = (int) (names.size() * testPercent);
        for (int i = 0; i < testCount; i++){
            testNames.add(names.get(i));
        }
        for (int i = testCount; i < names.size(); i++){
            trainNames.add(names.get(i));
        }
    }

    public static void load(){
        File metaFolder = new File(metaFolderName);
        for (final File fileEntry : metaFolder.listFiles()) {
            if (fileEntry.getName().equals("README") || fileEntry.isDirectory()) {
                continue;
            } else {
                metaFiles.add(fileEntry.getName());
            }
        }
        ///////////////////////////////////////////////////////////////////////////
        File datasetFolder = new File(datasetFolderName);
        for (final File fileEntry : datasetFolder.listFiles()) {
            if (fileEntry.getName().equals("README") || fileEntry.isDirectory()) {
                continue;
            } else {
                datasetFiles.add(fileEntry.getName());
            }
        }
        ////////////////////////////////////////////////////////////////////////////
        File melifFolder = new File(melifFolderName);
        for (final File fileEntry : melifFolder.listFiles()) {
            if (fileEntry.getName().equals("README") || fileEntry.isDirectory()) {
                continue;
            } else {
                melifFiles.add(fileEntry.getName());
            }
        }

        for(String curFile : datasetFiles){
            names.add(curFile.substring(0,curFile.lastIndexOf('_')));
        }
    }

    public static void main(String[] args) {
        //args[0] should contain path to folder with metainfo
        //args[1] should contain path to folder with datasets
        //args[2] should contain path to folder with melif statistic
        metaFolderName = args[0];
        datasetFolderName = args[1];
        melifFolderName = args[2];
        load();
        if (CompatabilityChecker.check(datasetFiles, metaFiles, melifFiles)){
            System.out.println("Names of files are correct");
        }
        shuffleAndSplit(0.2);
        for(String testName : testNames){
            NearestFinder nr = new NearestFinder(metaFiles, metaFolderName);
            ArrayList<Pair<String, Double>> nearestNamesAndDist = nr.getSimillarDatasetNames(testName, trainNames);
            Recommender rec = new Recommender(melifFolderName, metaFiles, melifFiles, new VotingRecommendation(4));
            ArrayList<Point> recommendedPoints = rec.recommend(nearestNamesAndDist);
            boolean b = false;
            /*ArrayList<Point> bestPoints = getbest(testName);
            System.out.println("Recomended Points for " + testName);
            for(Point p : recommendedPoints){
                System.out.println(p);
            }
            System.out.println("Best Points for " + testName);
            for(Point p : bestPoints){
                System.out.println(p);
            }*/
        }
    }
}
