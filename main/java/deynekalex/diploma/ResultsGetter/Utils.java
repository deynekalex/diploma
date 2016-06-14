package deynekalex.diploma.ResultsGetter;

import java.io.File;

/**
 * Created by deynekalex on 20.02.16.
 */
public class Utils {
    public static String getResultFileName(String curfile, String folderSuffics, String nameSuffics){
        int subDirsCount = curfile.split("/").length - 1;
        String newFolderName = "";
        for (int i = 0; i < subDirsCount; i++){
            newFolderName += curfile.split("/")[i];
        }
        newFolderName += folderSuffics;
        File newFolder = new File(newFolderName);
        newFolder.mkdir();
        String newFileName = newFolder + "/" + curfile.split("/")[subDirsCount];
        newFileName = newFileName.substring(0,newFileName.lastIndexOf('.'));
        newFileName += nameSuffics;
        return newFileName;
    }
}
