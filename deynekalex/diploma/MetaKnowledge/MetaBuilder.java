package deynekalex.diploma.MetaKnowledge;

import java.io.File;

/**
 * Created by deynekalex on 20.02.16.
 */
public class MetaBuilder {
    public static void extractMetaInfo(String folderPath){
        File folder = new File(folderPath);
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.getName().equals("README") || fileEntry.isDirectory()) {
                continue;
            } else {
                System.out.println("started " + folder + "/" + fileEntry.getName());
                MetaExtracter metaExtracter = new MetaExtracter(folder + "/" + fileEntry.getName());
                metaExtracter.extractMetaInfo();
                System.out.println("finished " + folder + "/" + fileEntry.getName());
            }
        }
    }

    public static void normalizeMetaInfo(String folderPath){
        File folder = new File(folderPath);
        MetaNormalizer metaNormalizer = new MetaNormalizer();
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.getName().equals("README") || fileEntry.isDirectory()) {
                continue;
            } else {
                System.out.println("started " + folder + "/" + fileEntry.getName());
                metaNormalizer.addNewMetaFile(folder + "/" + fileEntry.getName());
                System.out.println("finished " + folder + "/" + fileEntry.getName());
            }
        }
        metaNormalizer.normolizeMetaInfo();
    }

    public static void main(String[] args) {
        extractMetaInfo(args[0] + "_csv");
        normalizeMetaInfo(args[0] + "_csv" + "metainfo");
    }
}
