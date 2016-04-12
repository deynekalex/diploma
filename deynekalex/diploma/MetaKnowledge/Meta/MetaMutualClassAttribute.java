package deynekalex.diploma.MetaKnowledge.Meta;

import weka.core.Instances;

import java.util.HashMap;

import static JavaMI.MutualInformation.calculateMutualInformation;

/**
 * Created by deynekalex on 04.03.16.
 */
public class MetaMutualClassAttribute extends Metafeature{
    public MetaMutualClassAttribute(String filename) {
        super(filename);
    }

    public MetaMutualClassAttribute(Instances data) {
        super(data);
    }

    @Override
    public double calc() {
        if (this.data == null)
            this.load();
        metadata = new HashMap<>();
        //from one because data.attribute(0) = class
        double[] attrClass = data.attributeToDoubleArray(0);
        for(int i = 1; i < data.numAttributes(); i++){
            double[] mas = data.attributeToDoubleArray(i);
            metadata.put(i,calculateMutualInformation(attrClass,mas));
        }
        calcAverageMetaData();
        return avgMetadata;
    }
}
