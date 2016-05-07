package deynekalex.diploma.Experiement;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by deynekalex on 13.04.16.
 */
public class Experiement {
    static ArrayList<String> metaFiles = new ArrayList<>();
    static ArrayList<String> datasetFiles = new ArrayList<>();
    static ArrayList<String> train = new ArrayList<>();
    static ArrayList<String> test = new ArrayList<>();
    //static ArrayList<MelifStatistic> statistic = new ArrayList<>();
    private static void checkMetaFiles(String metaFolder, String datasetFolder) {
        File folder1 = new File(metaFolder);
        for (final File fileEntry : folder1.listFiles()) {
            if (fileEntry.getName().equals("README") || fileEntry.isDirectory()) {
                continue;
            } else {
                metaFiles.add(fileEntry.getName());
            }
        }
        ///////////////////////////////////////////////////////////////////////////
        File folder2 = new File(datasetFolder);
        for (final File fileEntry : folder2.listFiles()) {
            if (fileEntry.getName().equals("README") || fileEntry.isDirectory()) {
                continue;
            } else {
                datasetFiles.add(fileEntry.getName());
            }
        }

        for(String dataSetFileName : datasetFiles){
            boolean f = false;
            for (String metaFileName : metaFiles){
                if (metaFileName.contains(dataSetFileName.substring(0,dataSetFileName.length()-4))){
                    f = true;
                    break;
                }
            }
            if (!f){
                System.out.println("metainfo about " + dataSetFileName + " doesn't exists");
            }
        }
        ///////////////////////////////////////
        for(String metaFileName : metaFiles){
            boolean f = false;
            metaFileName = metaFileName.substring(0,metaFileName.lastIndexOf('('));
            for (String dataSetName : datasetFiles){
                if (dataSetName.contains(metaFileName)){
                    f = true;
                    break;
                }
            }
            if (!f){
                System.out.println("dataset" + metaFileName + " doesn't exists, but metainfo exists");
            }
        }

    }

    static void shuffleAndSplit(double testPercent){
        Collections.shuffle(datasetFiles);
        int testCount = (int) (datasetFiles.size() * testPercent);
        for (int i = 0; i < testCount; i++){
            //testDatasets.add(datasetFiles.get(i));
        }
        for (int i = testCount; i < datasetFiles.size(); i++){
            //trainDatasets.add(datasetFiles.get(i));
        }
    }

    static void calculateBestVectors(){
        for(int i = 0; i < train.size(); i++){

        }
    }

    private static void runMelif(String s) {

    }

    public static void main(String[] args) {
        //args[0] should contain path to folder with metainfo
        //args[1] should contain path to folder with datasets
        checkMetaFiles(args[0], args[1]);
        shuffleAndSplit(0.2);
        calculateBestVectors();
    }
}
