package deynekalex.diploma.MetaKnowledge.Meta;

import weka.core.Instances;

import java.util.*;

import static weka.core.Utils.correlation;

/**
 * Created by deynekalex on 20.02.16.
 */
public class MetaLinearCorrelation extends Metafeature {
    public MetaLinearCorrelation(String filename) {
        super(filename);
    }

    public MetaLinearCorrelation(Instances data){
        super(data);
    }

    @Override
    public double calc() {
        if (this.data == null)
            this.load();
        Map<Object, Double> R_XY = new HashMap<>();
        double sum = 0;
        //
        int col = 0;
        int last = 1000000;
        //
        for (int i = 1; i < data.numAttributes(); i++){
            for (int j = i + 1; j < data.numAttributes(); j++){
                col++;
                if (col >= last){
                    last += 1000000;
                    System.out.println(col);
                }
                double[] mas1 = data.attributeToDoubleArray(i);
                double[] mas2 = data.attributeToDoubleArray(i);
                for(int z = 0; z < mas1.length; z++){
                    if (Double.isNaN(mas1[z])){
                        mas1[z] = 0;
                    }
                }
                for(int z = 0; z < mas2.length; z++){
                    if (Double.isNaN(mas2[z])){
                        mas2[z] = 0;
                    }
                }
                sum += correlation(mas1, mas2, data.numInstances());
            }
        }
        avgMetadata = sum/col;
        return avgMetadata;
    }

}
