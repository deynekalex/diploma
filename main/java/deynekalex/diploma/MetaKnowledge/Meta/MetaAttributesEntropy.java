package deynekalex.diploma.MetaKnowledge.Meta;

import weka.core.Instances;

import java.util.HashMap;

import static weka.core.ContingencyTables.entropy;

/**
 * Created by deynekalex on 24.02.16.
 */
public class MetaAttributesEntropy extends Metafeature {

    public MetaAttributesEntropy(String filename) {
        super(filename);
    }

    public MetaAttributesEntropy(Instances data){
        super(data);
    }

    @Override
    public double calc() {
        if (this.data == null)
            this.load();
        metadata = new HashMap<>();
        //from one because data.attribute(0) = class
        for(int i = 1; i < data.numAttributes(); i++){
            double[] mas = data.attributeToDoubleArray(i);
            metadata.put(i,entropy(mas));
        }
        calcAverageMetaData();
        return avgMetadata;
    }
}
