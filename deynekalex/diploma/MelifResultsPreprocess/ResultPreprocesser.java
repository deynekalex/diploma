package deynekalex.diploma.MelifResultsPreprocess;

import deynekalex.diploma.MetaKnowledge.MetaNormalizer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;

/**
 * Created by deynekalex on 07.05.16.
 */
public class ResultPreprocesser {

    public static void main(String[] args) {
        preprocessFolder("zmelif_stat/" + "part4/" + "zdatasets_discrete(-5,5)part4_runInfo(0.25, 500, 20)");
    }

    private static void preprocessFolder(String folderPath) {
        File folder = new File(folderPath);
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.getName().equals("README") || fileEntry.isDirectory()) {
                continue;
            } else {
                Preprocessor preprocessor = new Preprocessor();
                System.out.println("started " + folder + "/" + fileEntry.getName());
                String inName = folder + "/" + fileEntry.getName();
                String outName = "zdist_" + folder + "/" + fileEntry.getName();
                File newFile = new File(outName);
                if(!newFile.exists()) {
                    try {
                        newFile.getParentFile().mkdirs();
                        newFile.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                preprocessor.preprocess(inName, outName);
                preprocessor.clear();
                System.out.println("finished " + folder + "/" + fileEntry.getName());
            }
        }
    }
}
