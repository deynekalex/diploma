package deynekalex.diploma;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by deynekalex on 20.02.16.
 */
public class CsvToWekaCsv {
    static BufferedReader in;
    static PrintWriter out;
    static ArrayList<String> datasetsInfo = new ArrayList<>();
    String name;
    ArrayList<String> classes = new ArrayList<>();
    ArrayList<String> attributes = new ArrayList<>();
    ArrayList<ArrayList<Float>> data = new ArrayList<>();
    ArrayList<String> dataClass = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        File folder = new File(args[0]);
        File[] listOfFiles = folder.listFiles();
        File newFolder = new File(args[0] + "_csv");
        newFolder.mkdir();
        for (int i = 0; i < listOfFiles.length; i++) {
            String curfile = listOfFiles[i].getName();
            System.out.println("started " + curfile);
            in = new BufferedReader(new FileReader(folder + "/" + curfile));
            out = new PrintWriter(newFolder + "/" + curfile);
            CsvToWekaCsv d = new CsvToWekaCsv();
            try {
                d.doSomething(curfile);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(curfile);
                continue;
            } finally {
                in.close();
                out.close();
            }
            File fileOld = new File(newFolder + "/" + curfile);
            File fileNew = new File(newFolder + "/" + curfile.substring(0, curfile.length() - 4)
                    + "(" + d.getInstancesCount() + "," + (d.getAttributesCount()-1) + ").csv");
            fileOld.renameTo(fileNew);
            System.out.println("ended " + curfile + " - ok");
        }
        out = new PrintWriter(newFolder + "/" + "README");
        for (String cur : datasetsInfo) {
            out.println(cur);
        }
        out.close();
    }
    private void fillDatasetInfo(String fileName) {
        datasetsInfo.add(fileName + " (" +
                + getInstancesCount() + " instances,"
                + (getAttributesCount()-1) + " attributes)");
    }

    private void doSomething(String curfile) throws IOException {
        String line = null;
        ArrayList<String> cur;
        ArrayList<Float> mas;
        while ((line = in.readLine()) != null) {
            cur = new ArrayList<>(Arrays.asList(line.split(",")));
            mas = new ArrayList<>();
            for(String s : cur){
                mas.add(Float.valueOf(s));
            }
            data.add(mas);
        }
        //Printing
        //data.get(0) <- list of classes of instances
        //data.get(1) <- list of attribute1 values

        //first row
        out.print("class,");
        for (int i = 1; i < getAttributesCount() - 1; i++){
            out.print("attribute" + i + ",");
        }
        out.println("attribute" + (data.size() - 1));

        //data
        for (int i = 0; i < getInstancesCount(); i++){
            //class type
            if (data.get(0).get(i).equals(Float.parseFloat("0")))
                out.print("zero" + ",");
            else if (data.get(0).get(i).equals(Float.parseFloat("1")))
                out.print("one" + ",");
            else
                try {
                    throw new Exception("> 2 classes in dataset" + curfile);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            //attributes
            for (int j = 1; j < getAttributesCount()-1; j++){
                out.print(data.get(j).get(i) + ",");
            }
            out.println(data.get(getAttributesCount()-1).get(i));
        }
        fillDatasetInfo(curfile);
    }

    public int getInstancesCount() {
        try {
            return data.get(0).size();
        }catch (Exception e){
            return 0;
        }
    }

    public int getAttributesCount() {
        return data.size();
    }
}
