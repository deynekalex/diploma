package deynekalex.diploma.MetaKnowledge.Meta;

import weka.core.Instances;

import java.util.HashMap;

import static JavaMI.MutualInformation.calculateMutualInformation;
import static weka.core.ContingencyTables.entropy;

/**
 * Created by deynekalex on 04.03.16.
 */
public class MetaEquivalentNumberOfFeatures extends Metafeature{
    public MetaEquivalentNumberOfFeatures(String filename) {
        super(filename);
    }

    public MetaEquivalentNumberOfFeatures(Instances data) {
        super(data);
    }

    @Override
    public double calc() {
        if (this.data == null)
            this.load();
        //class entropy calculating
        double[] mas = data.attributeToDoubleArray(0);
        double hc = entropy(mas);
        //mean normalized feature entropy
        metadata = new HashMap<>();
        double[] attrClass = data.attributeToDoubleArray(0);
        for(int i = 1; i < data.numAttributes(); i++){
            mas = data.attributeToDoubleArray(i);
            metadata.put(i,calculateMutualInformation(attrClass,mas));
        }
        calcAverageMetaData();
        avgMetadata = hc / avgMetadata;
        return avgMetadata;
    }
}
