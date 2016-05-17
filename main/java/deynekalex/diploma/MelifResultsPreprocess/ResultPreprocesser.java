package deynekalex.diploma.MelifResultsPreprocess;

import java.io.File;
import java.io.IOException;

/**
 * Created by deynekalex on 07.05.16.
 */
public class ResultPreprocesser {

    public static void main(String[] args) {
        preprocessFolder("zmelif_stat/" + "part4/" + "zdatasets_discrete(-5,5)part4_runInfo(0.25, 100, 20)");
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
                String outName = "zbestpoints_" + folder + "/" + fileEntry.getName();
                File newFile = new File(outName);
                if(!newFile.exists()) {
                    try {
                        newFile.getParentFile().mkdirs();
                        newFile.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                preprocessor.bestPointGetter(inName, outName);
                preprocessor.clear();
                System.out.println("finished " + folder + "/" + fileEntry.getName());
            }
        }
    }
}
