package deynekalex.diploma.MetaKnowledge.Meta;

import weka.core.Instances;

import static weka.core.ContingencyTables.entropy;

/**
 * Created by deynekalex on 04.03.16.
 */
public class MetaClassEntropy extends Metafeature{

    public MetaClassEntropy(String filename) {
        super(filename);
    }

    public MetaClassEntropy(Instances data) {
        super(data);
    }

    @Override
    public double calc() {
        if (this.data == null)
            this.load();
        double[] mas = data.attributeToDoubleArray(0);
        avgMetadata = entropy(mas);
        return avgMetadata;
    }
}
