package deynekalex.diploma.MetaKnowledge.Meta;

import org.apache.commons.math.stat.descriptive.moment.FourthMoment;
import org.apache.commons.math.stat.descriptive.moment.Kurtosis;
import weka.core.Instances;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by deynekalex on 24.02.16.
 */
public class MetaKurtosis extends Metafeature{
    public MetaKurtosis(String filename) {
        super(filename);
    }

    public MetaKurtosis(Instances data){
        super(data);
    }

    public double calc() {
        if (this.data == null)
            this.load();
        Map<Object, Double> KURT_X = new HashMap<>();
        for (int i = 1; i < data.numAttributes(); i++){
            double[] mas = data.attributeToDoubleArray(i);
            FourthMoment m4 = new FourthMoment();
            Kurtosis k = new Kurtosis(m4);
            for(int j = 0; j < mas.length; j++){
                m4.increment(mas[j]);
            }
            KURT_X.put(i, k.getResult());
        }
        metadata = KURT_X;
        calcAverageMetaData();
        return avgMetadata;
    }
}
