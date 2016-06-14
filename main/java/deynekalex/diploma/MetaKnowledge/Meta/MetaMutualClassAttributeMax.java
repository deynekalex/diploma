package deynekalex.diploma.MetaKnowledge.Meta;

import weka.core.Instances;

import static JavaMI.MutualInformation.calculateMutualInformation;

/**
 * Created by deynekalex on 04.03.16.
 */
public class MetaMutualClassAttributeMax extends Metafeature{
    public MetaMutualClassAttributeMax(Instances data) {
        super(data);
    }

    public MetaMutualClassAttributeMax(String filename) {
        super(filename);
    }

    @Override
    public double calc() {
        if (this.data == null)
            this.load();
        avgMetadata = Double.MIN_VALUE;
        //from one because data.attribute(0) = class
        double[] attrClass = data.attributeToDoubleArray(0);
        for(int i = 1; i < data.numAttributes(); i++){
            double[] mas = data.attributeToDoubleArray(i);
            for(int z = 0; z < mas.length; z++){
                if (Double.isNaN(mas[z])){
                    mas[z] = 0;
                }
            }
            if (calculateMutualInformation(attrClass,mas) > avgMetadata) {
                avgMetadata = calculateMutualInformation(attrClass,mas);
            }
        }
        return avgMetadata;
    }
}
