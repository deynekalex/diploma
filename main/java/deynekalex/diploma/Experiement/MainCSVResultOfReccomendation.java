package deynekalex.diploma.Experiement;

import deynekalex.diploma.Experiement.NearestStrategy.Strategy;
import deynekalex.diploma.Experiement.NearestStrategy.StrategyFixedRadius;
import deynekalex.diploma.Experiement.NearestStrategy.StrategyKNearest;
import deynekalex.diploma.Experiement.RecommendationMethod.RecommendationMethod;
import deynekalex.diploma.Experiement.RecommendationMethod.VotingRecommendation;
import deynekalex.diploma.MelifResultsPreprocess.Preprocessor;
import javafx.util.Pair;
import deynekalex.diploma.ResultsGetter.experiment2.MelifRunner;
import deynekalex.diploma.ResultsGetter.experiment2.SinglePointResultGetter;
import meLiF.result.Point;

import java.io.*;
import java.util.*;

import static java.lang.Math.abs;
import static java.lang.Math.min;

/**
 * Created by deynekalex on 13.04.16.
 */
public class MainCSVResultOfReccomendation {
    static ArrayList<String> datasetFiles = new ArrayList<>();
    static ArrayList<String> metaFiles = new ArrayList<>();
    static ArrayList<String> melifFiles = new ArrayList<>();

    static String datasetFolderName;
    static String metaFolderName;
    static String melifFolderName;

    static ArrayList<String> names = new ArrayList<>();
    static ArrayList<String> trainNames = new ArrayList<>();
    static ArrayList<String> testNames = new ArrayList<>();

    static PrintWriter out;

    static final String sep = ";";

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
        Collections.shuffle(names);
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
    private static ArrayList<MyPoint> getMelifStat(String fileName) {
        ArrayList<MyPoint> bestPoints = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(melifFolderName + "/" + fileName));
            br.readLine();
            while ((Preprocessor.line = br.readLine()) != null) {
                String[] parts = Preprocessor.line.split(", ");
                MyPoint p = new MyPoint();
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

    private static Double getBestScoreFromStat(String fileName) {
        Double result = null;
        try {
            BufferedReader br = new BufferedReader(new FileReader(melifFolderName + "/" + fileName));
            result = Double.parseDouble(br.readLine());
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
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
        } else {
            System.out.println("Names of files are incorrect");
            return;
        }
        try {
            //experiment1();
            //experiment2();
            experiment3();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void experiment2() throws IOException {
        int nearestDataSetsCount = 5;
        //Strategy strategy = new StrategyKNearest(nearestDataSetsCount);
        double radius = 1.0;
        out = new PrintWriter("experiment41_main_mean_newmetainfo.csv");
        Strategy strategy = new StrategyFixedRadius(radius);
        NearestFinder nr = new NearestFinder(metaFiles, metaFolderName, strategy);
        int recommendedPointsCount = 1;
        RecommendationMethod recommendationMethod = new VotingRecommendation(recommendedPointsCount);
        Recommender rec = new Recommender(melifFolderName, metaFiles, melifFiles, recommendationMethod);
        Collections.sort(names);
        out.print("Dataset name;");
        out.print("Recommended point;");
        out.print("Score of recommended point;");
        out.print("Contains in best?;");
        out.print("Best points;");
        out.print("Score of best points;");
        out.print("Point to which converges;");
        out.print("Score of converges Point;");
        out.print("Contains in best?;");
        out.print("(1,0,0,0);");
        out.print("(0,1,0,0);");
        out.print("(0,0,1,0);");
        out.print("(0,0,0,1);");
        out.print("Type of point: ");
        out.print("1 - recommended point in best, ");
        out.print("2 - recommended point converges to best point, ");
        out.print("3 - recommended point converges to worst result, ");
        out.print("4 - recommended point converges to better result, ");
        out.print("5 - recommedded point converges to same result");
        out.print(";");
        out.print("Recommendation efficient?");
        out.print(";");
        out.println();
        int iteration = 1;
        boolean finded = false;
        //for every dataset
        for (String testName : names) {
            if (testName.equals("GDS5037_mul3_norm_1_ova")) {
                continue;//bad metainfo
            }
            if (!finded) {
                if (testName.equals("GDS5037_mul3_norm_1_ova")) {
                    finded = true;
                    continue;
                } else {
                    continue;
                }
            }
            if (iteration == 10) {
                break;
            }
            System.out.println("Started " + (iteration++) + ") - " + testName);
            int type = 0;
            trainNames.addAll(names);
            trainNames.remove(testName);
            ArrayList<Pair<String, Double>> nearestNamesAndDist = nr.getSimillarDatasetNames(testName, trainNames);
            ArrayList<MyPoint> recommendedPoints = rec.recommend(nearestNamesAndDist);
            Set<MyPoint> bestPoints = new HashSet<>();
            Double bestScore = null;
            Double recommendedConvergeScore = null;
            String datasetName = null;
            //getReal best Points
            for (String melifFileName : melifFiles) {
                if (melifFileName.contains(testName)) {
                    bestPoints.addAll(getMelifStat(melifFileName));
                    bestScore = getBestScoreFromStat(melifFileName);
                }
            }
            //1 - datasetName
            out.print(testName + sep);
            MyPoint recommended = recommendedPoints.get(0);
            //2 - recommended point
            out.print(recommended + sep);
            for (String fileName : datasetFiles) {
                if (fileName.contains(testName)) {
                    datasetName = fileName;
                }
            }
            Point point = new Point(recommended.coordinates);
            //3 - score of recommended Point
            /*if (bestPoints.contains(recommended)) {
                out.print("same" + sep);
            } else {
                double score = new SinglePointResultGetter().getSingleResult
                        (datasetFolderName + "/" + datasetName, 0.25, 500, 20, point);
                out.print(score + sep);
            }*/
            //4 - yes if recommended Point contains in RealBestPoints
            if (bestPoints.contains(recommended)) {
                out.print("yes" + sep);
                recommendedConvergeScore = bestScore;
                type = 1;
            } else {
                out.print("no" + sep);
            }
            //5 - list of RealBestPoints
            for (MyPoint best : bestPoints) {
                out.print(best);
                out.print(" | ");
            }
            out.print(sep);
            //6 - score of BestPoint
            out.print(bestScore + sep);
            if (bestPoints.contains(recommended)) {
                out.print("same" + sep);
                out.print("same" + sep);
                out.print("yes" + sep);
            } else {
                //7 - points to which Melif converges from recommended Point
                Point[] points = new Point[1];
                points[0] = point;
                boolean inRealBestPoints = false;
                Pair<Double, List<Pair<Long, Point>>> result = new MelifRunner().execMelif
                        (datasetFolderName + "/" + datasetName, 0.25, 500, 20, points);
                for (Pair<Long, Point> cur : result.getValue()) {
                    Long steps = cur.getKey();
                    Point curPoint = cur.getValue();
                    if (bestPoints.contains(curPoint)) {
                        inRealBestPoints = true;
                    }
                    out.print(curPoint + " - ");
                    out.print(steps + " | ");
                }
                out.print(sep);
                //8 - score Melif from recommendedPoint
                recommendedConvergeScore = result.getKey();
                out.print(result.getKey() + sep);
                if (recommendedConvergeScore > bestScore) {
                    type = 4;
                } else if (recommendedConvergeScore < bestScore) {
                    type = 3;
                } else {
                    type = 5;
                }
                //9 - yes if we converges to RealBestPoints
                if (inRealBestPoints) {
                    out.print("yes" + sep);
                    type = 2;
                } else {
                    out.print("no" + sep);
                }
            }
            //10,11,12,13 Get result from Single Measures
            double bestSingleMeasureResult = 0;
            Point point1 = new Point(1, 0, 0, 0);
            double score = new SinglePointResultGetter().getSingleResult
                    (datasetFolderName + "/" + datasetName, 0.25, 500, 20, point1);
            if (bestSingleMeasureResult < score)
                bestSingleMeasureResult = score;
            out.print(score + sep);

            point1 = new Point(0, 1, 0, 0);
            score = new SinglePointResultGetter().getSingleResult
                    (datasetFolderName + "/" + datasetName, 0.25, 500, 20, point1);
            if (bestSingleMeasureResult < score)
                bestSingleMeasureResult = score;
            out.print(score + sep);

            point1 = new Point(0, 0, 1, 0);
            score = new SinglePointResultGetter().getSingleResult
                    (datasetFolderName + "/" + datasetName, 0.25, 500, 20, point1);
            if (bestSingleMeasureResult < score)
                bestSingleMeasureResult = score;
            out.print(score + sep);

            point1 = new Point(0, 0, 0, 1);
            score = new SinglePointResultGetter().getSingleResult
                    (datasetFolderName + "/" + datasetName, 0.25, 500, 20, point1);
            if (bestSingleMeasureResult < score)
                bestSingleMeasureResult = score;
            out.print(score + sep);
            out.print(type + sep);
            if (type < 3 || betterOrNear(recommendedConvergeScore, bestScore) || betterOrNear(recommendedConvergeScore, bestSingleMeasureResult)) {
                out.print("yes" + sep);
            } else {
                out.print("no" + sep);
            }
            out.println();
        }
        out.close();
    }

    public static void experiment3() throws FileNotFoundException {
        //int nearestDataSetsCount = 8;
        //int recommendedPointsCount = 1;
        out = new PrintWriter("experiment7New(20).csv");
        for (int recommendedPointsCount = 50; recommendedPointsCount <= 50; recommendedPointsCount++) {
            int nearestDataSetsCount = 30;
            //Strategy strategy = new StrategyKNearest(nearestDataSetsCount);
            double radius = 1;
            //Strategy strategy = new StrategyFixedRadius(radius);
            Strategy strategy = new StrategyKNearest(nearestDataSetsCount);
            NearestFinder nr = new NearestFinder(metaFiles, metaFolderName, strategy);
            RecommendationMethod recommendationMethod = new VotingRecommendation(recommendedPointsCount);
            Recommender rec = new Recommender(melifFolderName, metaFiles, melifFiles, recommendationMethod);
            Collections.sort(names);
            int iteration = 1;
            boolean finded = false;
            int goodCount = 0;
            //for every dataset
            for (String testName : names) {
                if (testName.equals("GDS5037_mul3_norm_1_ova")) {
                    continue;//bad metainfo
                }
                System.out.println("Started " + (iteration++) + ") - " + testName);
                int type = 0;
                trainNames.addAll(names);
                trainNames.remove(testName);
                ArrayList<Pair<String, Double>> nearestNamesAndDist = nr.getSimillarDatasetNames(testName, trainNames);
                ArrayList<MyPoint> recommendedPoints = rec.recommend(nearestNamesAndDist);
                Set<MyPoint> bestPoints = new HashSet<>();
                Double bestScore = null;
                Double recommendedConvergeScore = null;
                String datasetName = null;
                //getReal best Points
                for (String melifFileName : melifFiles) {
                    if (melifFileName.contains(testName)) {
                        bestPoints.addAll(getMelifStat(melifFileName));
                        bestScore = getBestScoreFromStat(melifFileName);
                    }
                }
                //1 - datasetName
                ArrayList<MyPoint> recommended = new ArrayList<>();
                for (int i = 0; i < min(recommendedPoints.size(), recommendedPointsCount); i++) {
                    recommended.add(recommendedPoints.get(i));
                }
                //2 - recommended point
                for (String fileName : datasetFiles) {
                    if (fileName.contains(testName)) {
                        datasetName = fileName;
                    }
                }
                for (MyPoint recommendeP : recommended) {
                    if (bestPoints.contains(recommendeP)) {
                        goodCount++;
                        break;
                    }
                    Point point = new Point(recommendeP.coordinates);
                    //compare bestScore to result in point
                    if (new SinglePointResultGetter().getSingleResult(datasetFolderName + "/" + datasetName, 0.25, 500, 20, point) >= bestScore){
                        goodCount++;
                        break;
                    }
                }
                out.print("count = " + recommendedPointsCount + "nearest = " + nearestDataSetsCount + " - " + (double) goodCount / iteration + ",");
            }
            out.println();
        }
        out.close();
    }

    //return true if d1 is better or near d2
    private static boolean betterOrNear(Double d1, Double d2) {
        if (d1 > d2) {
            return true;
        }
        if (abs((d1 - d2) / d1) < 0.05)
            return true;
        else
            return false;
    }

    public static void experiment1() {
        //number of experiment launches
        int iterations = 100;
        double globalResult = 0;
        //
        //Experiment settings
        //
        double testPercent = 0.2;
        int nearestDataSetsCount = 5;
        //Strategy strategy = new StrategyKNearest(nearestDataSetsCount);
        double radius = 1;
        System.out.println(radius);
        Strategy strategy = new StrategyFixedRadius(radius);
        NearestFinder nr = new NearestFinder(metaFiles, metaFolderName, strategy);
        int recommendedPointsCount = 12;
        System.out.println(recommendedPointsCount);
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
                ArrayList<MyPoint> recommendedPoints = rec.recommend(nearestNamesAndDist);
                Set<MyPoint> bestPoints = new HashSet<>();
                for (String melifFileName : melifFiles) {
                    if (melifFileName.contains(testName)) {
                        bestPoints.addAll(getMelifStat(melifFileName));
                    }
                }
                for (MyPoint best : bestPoints) {
                    if (recommendedPoints.contains(best)) {
                        res++;
                        break;
                    }
                }
            }
            double result = (double) res / all;
            System.out.println("current result = " + (double) res / all);
            globalResult += result;
            /*MyPoint p1 = new MyPoint();
            p1.coordinates.add(2.0);
            p1.coordinates.add(0.0);
            p1.coordinates.add(6.0);
            p1.coordinates.add(0.0);

            MyPoint p2 = new MyPoint();
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
