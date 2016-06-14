package deynekalex.diploma.MetaKnowledge;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Created by deynekalex on 20.02.16.
 */
public class MetaBuilder {
    public static void extractMetaInfo(String folderPath){
        File folder = new File(folderPath);
        int i = 0;
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.getName().equals("README") || fileEntry.isDirectory()) {
                continue;
            } else {
                System.out.println(++i);
                System.out.println("started " + folder + "/" + fileEntry.getName());
                MetaExtracter metaExtracter = new MetaExtracter(folder + "/" + fileEntry.getName());
                if (!metaExtracter.extractMetaInfo()){
                    System.out.println("fail............................");
                }
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

    public static void calcDistance(String folderPath){
        File folder = new File(folderPath);
        MetaDistanceCalculator metaCalculator = new MetaDistanceCalculator();
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.getName().equals("README") || fileEntry.isDirectory()) {
                continue;
            } else {
                System.out.println("started " + folder + "/" + fileEntry.getName());
                metaCalculator.addNewMetaNormFile(folder + "/" + fileEntry.getName());
                System.out.println("finished " + folder + "/" + fileEntry.getName());
            }
        }
        metaCalculator.calcAllDistanceVectors();
    }

    private static void getMetaInfoCsvFile(String folderPath, String resultPath) {
        File folder = new File(folderPath);
        MetaDistanceCalculator metaCalculator = new MetaDistanceCalculator();
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.getName().equals("README") || fileEntry.isDirectory()) {
                continue;
            } else {
                System.out.println("started " + folder + "/" + fileEntry.getName());
                metaCalculator.addNewMetaNormFile(folder + "/" + fileEntry.getName());
                System.out.println("finished " + folder + "/" + fileEntry.getName());
            }
        }
        try {
            metaCalculator.writeMetaFilesToCsv(resultPath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void getMeanDistToCsvFile(String folderPath, String resultPath) {
        File folder = new File(folderPath);
        MetaDistanceCalculator metaCalculator = new MetaDistanceCalculator();
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.getName().equals("README") || fileEntry.isDirectory()) {
                continue;
            } else {
                System.out.println("started " + folder + "/" + fileEntry.getName());
                metaCalculator.addNewMetaNormFile(folder + "/" + fileEntry.getName());
                System.out.println("finished " + folder + "/" + fileEntry.getName());
            }
        }
        try {
            metaCalculator.writeMeanDistToFile(resultPath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        //extractMetaInfo(args[0] + "_csv");
        //normalizeMetaInfo(args[0] + "_csv" + "_metainfo2");
        //calcDistance(args[0] + "_csv" + "_metainfo2" + "_norm");
        //getMetaInfoCsvFile(args[0] + "_csv" + "_metainfo2" + "_norm", "metaFileDataset.csv");
        getMeanDistToCsvFile(args[0] + "_csv" + "_metainfo2" + "_norm", "nearestDataset.csv");
    }


}
