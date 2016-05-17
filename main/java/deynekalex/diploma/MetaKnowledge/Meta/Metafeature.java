package deynekalex.diploma.MetaKnowledge.Meta;

import weka.core.Instances;
import weka.core.converters.CSVLoader;

import java.io.*;
import java.util.Map;

/**
 * Created by deynekalex on 19.02.16.
 */
public abstract class Metafeature {
    public Map<Object, Double> metadata;
    public double avgMetadata;
    Instances data = null;
    String filename;

    public Metafeature(String filename){
        this.filename = filename;
        this.load();
        data.setClass(data.attribute(0));
    }

    public Metafeature(Instances data){
        this.data = data;
        data.setClass(data.attribute(0));
    }

    public Instances getData() {
        return data;
    }

    public void setData(Instances data) {
        this.data = data;
    }

    public abstract double calc();

    public void load(){
        try {
            CSVLoader loader = new CSVLoader();
            loader.setSource(new File(filename));
            data = loader.getDataSet();
        } catch (IOException e) {
            //e.printStackTrace();
        } catch (Exception e) {
            //e.printStackTrace();
        }
    }

    protected boolean calcAverageMetaData(){
        if (metadata == null || metadata.size() == 0){
            try {
                throw new Exception("metaData isEmpty or not initialized");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else{
            double sum = 0;
            for (Double f : metadata.values()){
                sum += f;
            }
            avgMetadata = sum/metadata.size();
            return true;
        }
        return false;
    }
}
