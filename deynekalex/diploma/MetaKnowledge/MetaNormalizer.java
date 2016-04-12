package deynekalex.diploma.MetaKnowledge;

import deynekalex.diploma.Utils;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by deynekalex on 21.03.16.
 */
public class MetaNormalizer {
    static PrintWriter out;
    ArrayList<String> files = new ArrayList<>();
    HashMap<String, ArrayList<Double>> metaDataIn = new HashMap<>();
    ArrayList<Double> maxs;
    ArrayList<Double> mins;

    public void addNewMetaFile(String curfile){
          files.add(curfile);
    }

    private void preprocessData(String curFile){
        try (BufferedReader br = new BufferedReader(new FileReader(curFile))) {
            String line;
            ArrayList<Double> list = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                int start = line.lastIndexOf('>') + 1;
                list.add(Double.parseDouble(line.substring(start)));
            }
            metaDataIn.put(curFile, list);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void findMinAndMax(){
        maxs = null;
        mins = null;
        //get min and max for each metaInfo
        for(ArrayList<Double> cur: metaDataIn.values()){
            if (maxs == null || mins == null){
                mins = new ArrayList<>();
                maxs = new ArrayList<>();
                for(int i = 0; i < cur.size(); i++){
                    maxs.add(Double.MIN_VALUE);
                    mins.add(Double.MAX_VALUE);
                }
            }
            for(int i = 0; i < cur.size(); i++){
                if (cur.get(i) > maxs.get(i)){
                    maxs.set(i, cur.get(i));
                }
                if (cur.get(i) < mins.get(i)){
                    mins.set(i, cur.get(i));
                }
            }
        }
    }

    public void printNormalized(String curFile){
        try {
            out = new PrintWriter(Utils.getResultFileName(curFile,"_norm", ".meta"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ArrayList<Double> list = metaDataIn.get(curFile);
        for(int i = 0; i < list.size(); i++){
            out.println((list.get(i) - mins.get(i))/(maxs.get(i) - mins.get(i)));
        }
        out.close();
    }

    public boolean normolizeMetaInfo(){
        for (String curFile : files){
            preprocessData(curFile);
        }
        findMinAndMax();
        for (String curFile : files){
            printNormalized(curFile);
        }
        return true;
    }
}
