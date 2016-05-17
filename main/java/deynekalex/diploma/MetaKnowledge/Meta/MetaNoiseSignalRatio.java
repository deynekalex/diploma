package deynekalex.diploma.MetaKnowledge.Meta;

import weka.core.Instances;

import java.util.HashMap;

import static JavaMI.MutualInformation.calculateMutualInformation;
import static weka.core.ContingencyTables.entropy;

/**
 * Created by deynekalex on 04.03.16.
 */
public class MetaNoiseSignalRatio extends Metafeature{

    public MetaNoiseSignalRatio(String filename) {
        super(filename);
    }

    public MetaNoiseSignalRatio(Instances data) {
        super(data);
    }

    @Override
    public double calc() {
        if (this.data == null)
            this.load();
        //mean attribute entropy calculating
        metadata = new HashMap<>();
        //from one because data.attribute(0) = class
        for(int i = 1; i < data.numAttributes(); i++){
            double[] mas = data.attributeToDoubleArray(i);
            metadata.put(i,entropy(mas));
        }
        calcAverageMetaData();
        double avgEntropy = avgMetadata;
        //mean normalized feature entropy
        double[] attrClass = data.attributeToDoubleArray(0);
        for(int i = 1; i < data.numAttributes(); i++){
            double[] mas = data.attributeToDoubleArray(i);
            metadata.put(i,calculateMutualInformation(attrClass,mas));
        }
        calcAverageMetaData();
        double mi = avgMetadata;
        avgMetadata = (avgEntropy - mi)/mi;
        return avgMetadata;
    }
}
