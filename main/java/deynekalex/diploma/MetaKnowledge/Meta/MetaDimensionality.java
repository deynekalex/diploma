package deynekalex.diploma.MetaKnowledge.Meta;

import weka.core.Instances;

/**
 * Created by deynekalex on 04.03.16.
 */
public class MetaDimensionality extends Metafeature{
    public MetaDimensionality(String filename) {
        super(filename);
    }

    public MetaDimensionality(Instances data) {
        super(data);
    }

    @Override
    public double calc() {
        if (this.data == null)
            this.load();
        avgMetadata = (double) data.numInstances()/(data.numAttributes()-1);
        return avgMetadata;
    }
}
