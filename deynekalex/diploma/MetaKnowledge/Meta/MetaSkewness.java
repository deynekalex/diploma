package deynekalex.diploma.MetaKnowledge.Meta;

import org.apache.commons.math.stat.descriptive.moment.FourthMoment;
import org.apache.commons.math.stat.descriptive.moment.Skewness;
import weka.core.Instances;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by deynekalex on 24.02.16.
 */
public class MetaSkewness extends Metafeature{
    public MetaSkewness(String filename) {
        super(filename);
    }

    public MetaSkewness(Instances data){
        super(data);
    }

    public double calc() {
        if (this.data == null)
            this.load();
        Map<Object, Double> SKEW_X = new HashMap<>();
        for (int i = 1; i < data.numAttributes(); i++){
            double[] mas = data.attributeToDoubleArray(i);
            FourthMoment m4 = new FourthMoment();
            Skewness s = new Skewness(m4);
            for(int j = 0; j < mas.length; j++){
                m4.increment(mas[j]);
            }
            SKEW_X.put(i, s.getResult());
        }
        metadata = SKEW_X;
        calcAverageMetaData();
        return avgMetadata;
    }
}
