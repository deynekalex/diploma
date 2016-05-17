package deynekalex.diploma.MetaKnowledge.Meta;

import weka.core.Instances;

/**
 * Created by deynekalex on 20.02.16.
 */
public class MetaNumberOfAttributes extends Metafeature{

    public MetaNumberOfAttributes(String filename) {
        super(filename);
    }

    public MetaNumberOfAttributes(Instances data) {
        super(data);
    }

    @Override
    public double calc() {
        if (this.data == null)
            this.load();
        return data.numAttributes() - 1;
    }
}
