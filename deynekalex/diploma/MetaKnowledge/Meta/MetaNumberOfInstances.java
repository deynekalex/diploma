package deynekalex.diploma.MetaKnowledge.Meta;

import weka.core.Instances;

/**
 * Created by deynekalex on 20.02.16.
 */
public class MetaNumberOfInstances extends Metafeature {

    public MetaNumberOfInstances(String filename) {
        super(filename);
    }

    public MetaNumberOfInstances(Instances data){
        super(data);
    }

    @Override
    public double calc() {
        if (this.data == null)
            this.load();
        return data.numInstances();
    }
}
