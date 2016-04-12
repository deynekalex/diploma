package deynekalex.diploma.EARRCalculation;

import deynekalex.diploma.Utils;
import weka.attributeSelection.*;
import weka.core.Instances;
import weka.core.converters.CSVLoader;
import weka.filters.Filter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * Created by deynekalex on 10.04.16.
 */
public class EvaluatorBuilder {
    static ArrayList<Filter> filters = new ArrayList<>();
    static PrintWriter out;
    public static void fillDatasets(String folderPath){
        File folder = new File(folderPath);
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.getName().equals("README") || fileEntry.isDirectory()) {
                continue;
            } else {
                try {
                    out = new PrintWriter(Utils.getResultFileName(folder + "/" + fileEntry.getName(),"_earr", ".earr"));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                System.out.println("started " + folder + "/" + fileEntry.getName());
                //calc EARR of all possible pairs of feature selection algorithms
                Instances data = null;
                try {
                    CSVLoader loader = new CSVLoader();
                    loader.setSource(new File(folder + "/" + fileEntry.getName()));
                    data = loader.getDataSet();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                for(int i = 0; i < filters.size() - 1; i++){
                    for(int j = i + 1; j < filters.size(); j++){
                        System.out.println("started" + filters.get(i).getClass() + "vs" + filters.get(j).getClass());
                        out.print("<" + filters.get(i).getClass() + "," + filters.get(j).getClass() + ">");
                        try {
                            out.println(new EarrEvaluator(data, filters.get(i), filters.get(j)).evaluate());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                out.close();
                System.out.println("finished " + folder + "/" + fileEntry.getName());
            }
        }
    }

    public static void initFilterSet(){
        //adding filters set
        //filters.add(new FilterCreator(new CfsSubsetEval(), new BestFirst(), "-D 1").get());//1 - forward
        //filters.add(new FilterCreator(new CfsSubsetEval(), new BestFirst(), "-D 0").get());//0 - backward
        //filters.add(new FilterCreator(new CfsSubsetEval(), new BestFirst(), "-D 2").get());//2 - bidirectional
        filters.add(new FilterCreator(new CfsSubsetEval(), new GeneticSearch()).get());
        filters.add(new FilterCreator(new CfsSubsetEval(), new LinearForwardSelection()).get());
        filters.add(new FilterCreator(new CfsSubsetEval(), new RankSearch()).get());
        filters.add(new FilterCreator(new CfsSubsetEval(), new ScatterSearchV1()).get());
        filters.add(new FilterCreator(new CfsSubsetEval(), new GreedyStepwise()).get());
        filters.add(new FilterCreator(new ReliefFAttributeEval(), new RankSearch()).get());
    }

    public static void main(String[] args) {
        initFilterSet();
        fillDatasets(args[0] + "_csv");
    }
}
