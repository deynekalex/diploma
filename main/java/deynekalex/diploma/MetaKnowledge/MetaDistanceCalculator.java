package deynekalex.diploma.MetaKnowledge;

import deynekalex.diploma.Utils;

import java.io.*;
import java.util.*;

/**
 * Created by deynekalex on 12.04.16.
 */
public class MetaDistanceCalculator {
    static PrintWriter out;
    ArrayList<String> files = new ArrayList<>();
    HashMap<String, ArrayList<Double>> metaDataIn = new HashMap<>();

    public void addNewMetaNormFile(String curfile) {
        files.add(curfile);
    }

    private void printDistanceVector(String curFile) {
        try {
            out = new PrintWriter(Utils.getResultFileName(curFile, "_dist", ".meta"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //найти все пары расстояний до датасетов и отсортировать по возрастанию
        TreeMap<Double, String> dist = new TreeMap<>();
        for (int i = 0; i < files.size(); i++) {
            dist.put(calcDistance(metaDataIn.get(curFile), metaDataIn.get(files.get(i))), files.get(i));
        }
        Set set = dist.entrySet();
        Iterator iterator = set.iterator();
        while (iterator.hasNext()) {
            Map.Entry mentry = (Map.Entry) iterator.next();
            //System.out.print("key is: "+ mentry.getKey() + " & Value is: ");
            out.print(mentry.getKey() + " - ");
            out.println(mentry.getValue());
        }
        out.close();
    }

    private double calcDistance(ArrayList<Double> first, ArrayList<Double> second) {
        try {
            if (first.size() != second.size())
                throw new Exception("Metafeature count size are not equal");
        } catch (Exception e) {
            e.printStackTrace();
        }
        double sum = 0;
        for (int i = 0; i < first.size(); i++) {
            if (Double.isNaN(first.get(i)) || Double.isNaN(second.get(i))) {
                sum += 0.5;
            } else {
                sum += Math.abs(first.get(i) - second.get(i));
            }
        }
        return sum;
    }

    private void loadMetaInfo(String curFile) {
        try (BufferedReader br = new BufferedReader(new FileReader(curFile))) {
            String line = null;
            ArrayList<Double> list = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                list.add(Double.parseDouble(line));
            }
            metaDataIn.put(curFile, list);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void calcAllDistanceVectors() {
        for (String curFile : files) {
            loadMetaInfo(curFile);
        }
        for (String curFile : files) {
            printDistanceVector(curFile);
        }
    }

    public void writeMetaFilesToCsv(String resultPath) throws FileNotFoundException {
        PrintWriter out = new PrintWriter(resultPath);
        for (String curFile : files) {
            loadMetaInfo(curFile);
        }
        Collections.sort(files);
        for (int i = 0; i < metaDataIn.get(files.get(0)).size(); i++) {
            if (i == 0) {
                for (String curFile : files) {
                    out.print(curFile + ";");
                }
                out.println();
            }
            for (String curFile : files) {
                if (curFile.contains("GDS5037_mul3_norm_1_ova")) {
                    continue;//bad metainfo
                }
                out.print(metaDataIn.get(curFile).get(i) + ";");
            }
            out.println();
        }
        out.close();
    }

    private void printMeanDist(String curFile, PrintWriter out) {
        TreeMap<Double, String> dist = new TreeMap<>();
        for (int i = 0; i < files.size(); i++) {
            dist.put(calcDistance(metaDataIn.get(curFile), metaDataIn.get(files.get(i))), files.get(i));
        }
        Set set = dist.entrySet();
        Iterator iterator = set.iterator();
        iterator.next();
        double sum = 0;
        int i = 1;
        while (iterator.hasNext()) {
            if (i >= 4) {
                break;
            }
            Map.Entry<Double, String> mentry = (Map.Entry) iterator.next();
            //System.out.print("key is: "+ mentry.getKey() + " & Value is: ");
            sum += 1 / mentry.getKey();
            i++;
        }
        out.print(sum + ";");
    }

    private void printNearestDist(String curFile, PrintWriter out){
        TreeMap<Double, String> dist = new TreeMap<>();
        for (int i = 0; i < files.size(); i++) {
            dist.put(calcDistance(metaDataIn.get(curFile), metaDataIn.get(files.get(i))), files.get(i));
        }
        Set set = dist.entrySet();
        Iterator iterator = set.iterator();
        iterator.next();
        if (iterator.hasNext()) {
            Map.Entry<Double, String> mentry = (Map.Entry) iterator.next();
            out.print(mentry.getKey() + ";");
        }
    }

    public void writeMeanDistToFile(String resultPath) throws FileNotFoundException {
        PrintWriter out = new PrintWriter(resultPath);
        for (String curFile : files) {
            loadMetaInfo(curFile);
        }
        Collections.sort(files);
        for (String curFile : files) {
            out.print(curFile + ";");
        }
        out.println();
        for (String curFile : files) {
            if (curFile.contains("GDS5037_mul3_norm_1_ova")) {
                continue;//bad metainfo
            }
            //printMeanDist(curFile, out);
            printNearestDist(curFile, out);
        }
        out.println();
        out.close();
    }
}
