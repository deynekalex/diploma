package deynekalex.diploma.Experiement;

import deynekalex.diploma.Experiement.NearestStrategy.StrategyFixedRadius;
import deynekalex.diploma.Experiement.NearestStrategy.StrategyKNearest;
import deynekalex.diploma.Experiement.NearestStrategy.Strategy;
import deynekalex.diploma.Experiement.RecommendationMethod.RecommendationMethod;
import deynekalex.diploma.Experiement.RecommendationMethod.VotingRecommendation;
import javafx.util.Pair;

import java.io.*;
import java.util.*;

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

    //load datasetFiles, metaFiles, melifFiles
    public static void load() {
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

        for (String curFile : datasetFiles) {
            names.add(curFile.substring(0, curFile.lastIndexOf('_')));
        }
    }

    //fill trainNames and testNames with testPercent proportion
    static void shuffleAndSplit(double testPercent) {
        Collections.shuffle(names);
        int testCount = (int) (names.size() * testPercent);
        for (int i = 0; i < testCount; i++) {
            testNames.add(names.get(i));
        }
        for (int i = testCount; i < names.size(); i++) {
            trainNames.add(names.get(i));
        }
    }

    //read MelifStat from file
    private static ArrayList<Point> getMelifStat(String fileName) {
        ArrayList<Point> bestPoints = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(melifFolderName + "/" + fileName));
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(", ");
                Point p = new Point();
                for (String s : parts) {
                    p.coordinates.add(Double.parseDouble(s));
                }
                bestPoints.add(p);
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bestPoints;
    }

    public static void main(String[] args) {
        //args[0] should contain path to folder with metainfo
        //args[1] should contain path to folder with datasets
        //args[2] should contain path to folder with melif statistic
        metaFolderName = args[0];
        datasetFolderName = args[1];
        melifFolderName = args[2];
        load();
        if (CompatabilityChecker.check(datasetFiles, metaFiles, melifFiles)) {
            System.out.println("Names of files are correct");
        }else{
            System.out.println("Names of files are incorrect");
            return;
        }
        //number of experiment launches
        int iterations = 10;
        double globalResult = 0;
        //
        //Experiment settings
        //
        double testPercent = 0.2;
        int nearestDataSetsCount = 5;
        //Strategy strategy = new StrategyKNearest(nearestDataSetsCount);
        double radius = 1.0;
        Strategy strategy = new StrategyFixedRadius(radius);
        NearestFinder nr = new NearestFinder(metaFiles, metaFolderName, strategy);
        int recommendedPointsCount = 10;
        RecommendationMethod recommendationMethod = new VotingRecommendation(recommendedPointsCount);
        Recommender rec = new Recommender(melifFolderName, metaFiles, melifFiles, recommendationMethod);
        //
        //
        //
        for (int z = 0; z < iterations; z++) {
            shuffleAndSplit(testPercent);
            int res = 0;
            int all = 0;
            for (String testName : testNames) {
                all++;
                ArrayList<Pair<String, Double>> nearestNamesAndDist = nr.getSimillarDatasetNames(testName, trainNames);
                ArrayList<Point> recommendedPoints = rec.recommend(nearestNamesAndDist);
                Set<Point> bestPoints = new HashSet<>();
                for (String melifFileName : melifFiles) {
                    if (melifFileName.contains(testName)) {
                        bestPoints.addAll(getMelifStat(melifFileName));
                    }
                }
                for (Point best : bestPoints) {
                    if (recommendedPoints.contains(best)) {
                        res++;
                        break;
                    }
                }
            }
            double result = (double) res / all;
            System.out.println("current result = " + (double) res / all);
            globalResult += result;
            /*Point p1 = new Point();
            p1.coordinates.add(2.0);
            p1.coordinates.add(0.0);
            p1.coordinates.add(6.0);
            p1.coordinates.add(0.0);

            Point p2 = new Point();
            p2.coordinates.add(1.0);
            p2.coordinates.add(0.0);
            p2.coordinates.add(3.0);
            p2.coordinates.add(0.0);

            boolean b = p1.equals(p2);
            System.out.println(b);*/
        }
        globalResult = globalResult / iterations * 100;
        System.out.println("Result = " + (globalResult));
    }
}
