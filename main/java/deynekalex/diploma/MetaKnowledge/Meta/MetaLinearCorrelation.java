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
                sum += correlation(data.attributeToDoubleArray(i), data.attributeToDoubleArray(j), data.numInstances());
            }
        }
        avgMetadata = sum/col;
        return avgMetadata;
    }

}
