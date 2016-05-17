package ru.ifmo.ctddev.deyneka;

import java.io.File;

/**
 * Created by deynekalex on 30.04.16.
 */
public class MelifStatExtracter {
    public static void main(String[] args) {
        extractStatFromFolder(args[0],0.25,500,20);
        //extractStatFromFolder(args[0],0.25,300,20);
        //extractStatFromFolder(args[0],0.25,500,20);
        //extractStat("zdatasets_discrete(-5,5)/arizona1_mul4_norm_0_ova_discrete.csv",0.25,100,20);
    }

    private static void extractStatFromFolder(String folderPath, double val1, int val2, int val3) {
        System.out.println("Extracting from folder " + folderPath
                + ", with configuration (" + val1 + ", " + val2 + ", " + val3 + ")");
        File folder = new File(folderPath);
        StatExtracter extracter = new StatExtracter();
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.getName().equals("README") || fileEntry.isDirectory()) {
                continue;
            } else {
                System.out.println("started " + folder + "/" + fileEntry.getName());
                extracter.getStat(folder + "/" + fileEntry.getName(),val1,val2,val3);
                System.out.println("finished " + folder + "/" + fileEntry.getName());
            }
        }
    }

    private static void extractStat(String fileName, double val1, int val2, int val3){
        StatExtracter extracter = new StatExtracter();
        extracter.getStat(fileName,val1,val2,val3);
    }
}
