package deynekalex.diploma.Experiement;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by deynekalex on 08.05.16.
 */
public class CompatabilityChecker {

    public static boolean check(ArrayList<String> datasetFiles, ArrayList<String> metaFiles, ArrayList<String> melifFiles) {
        //comparing datasetFiles with melifFiles
        boolean isRight = true;
        for(String dataSetFileName : datasetFiles){
            boolean f = false;
            for (String metaFileName : metaFiles){
                if (metaFileName.contains(dataSetFileName.substring(0,dataSetFileName.lastIndexOf('_')))){
                    f = true;
                    break;
                }
            }
            if (!f){
                System.out.println("metainfo about " + dataSetFileName + " doesn't exists");
                isRight = false;
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
                isRight = false;
            }
        }
        ////////////////////////////////////////
        //comparing datasetFiles with metaFiles
        for(String dataSetFileName : datasetFiles){
            boolean f = false;
            for (String melifFileName : melifFiles){
                if (melifFileName.contains(dataSetFileName.substring(0,dataSetFileName.lastIndexOf('_')))){
                    f = true;
                    break;
                }
            }
            if (!f){
                System.out.println("melif statistics about " + dataSetFileName + " doesn't exists");
                isRight = false;
            }
        }
        /////////////////////////////////////////
        for(String melifFileName : melifFiles){
            boolean f = false;
            melifFileName = melifFileName.substring(0,melifFileName.lastIndexOf('('));
            for (String dataSetName : datasetFiles){
                if (dataSetName.contains(melifFileName)){
                    f = true;
                    break;
                }
            }
            if (!f){
                System.out.println("dataset" + melifFileName + " doesn't exists, but melif statistics exists");
                isRight = false;
            }
        }
        return isRight;
    }
}
