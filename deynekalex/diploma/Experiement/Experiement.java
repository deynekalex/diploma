package deynekalex.diploma.Experiement;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by deynekalex on 13.04.16.
 */
public class Experiement {
    static ArrayList<String> datasetFiles = new ArrayList<>();
    static ArrayList<String> trainDatasets = new ArrayList<>();
    static ArrayList<String> testDatasets = new ArrayList<>();

    static ArrayList<String> metaFiles = new ArrayList<>();
    static ArrayList<String> melifFiles = new ArrayList<>();

    static void shuffleAndSplit(double testPercent){
        Collections.shuffle(datasetFiles);
        int testCount = (int) (datasetFiles.size() * testPercent);
        for (int i = 0; i < testCount; i++){
            testDatasets.add(datasetFiles.get(i));
        }
        for (int i = testCount; i < datasetFiles.size(); i++){
            trainDatasets.add(datasetFiles.get(i));
        }
    }

    public static void load(String metaFolderName, String datasetFolderName, String melifFolderName){
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

    }

    public static void main(String[] args) {
        //args[0] should contain path to folder with metainfo
        //args[1] should contain path to folder with datasets
        //args[2] should contain path to folder with melif statistic
        load(args[0],args[1],args[2]);
        if (CompatabilityChecker.check(datasetFiles,metaFiles,melifFiles)){
            System.out.println("Names of files are correct");
        }
        shuffleAndSplit(0.2);
    }
}
