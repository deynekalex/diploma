package deynekalex.diploma.MetaKnowledge;

import deynekalex.diploma.MetaKnowledge.Meta.*;
import deynekalex.diploma.Utils;
import weka.core.Instances;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * Created by deynekalex on 04.03.16.
 */
public class MetaExtracter {
    static PrintWriter out;
    static ArrayList<Metafeature> metafeatures;
    String curfile;

    MetaExtracter(String curfile){
        this.curfile = curfile;
    }

    private boolean initMetaFeatureSet(){
        metafeatures = new ArrayList<>();
        Instances instances = new MetaNumberOfInstances(curfile).getData();
        if (instances.equals(null)){
            return false;
        }
        metafeatures.add(new MetaNumberOfInstances(instances));
        metafeatures.add(new MetaNumberOfAttributes(instances));
        metafeatures.add(new MetaDimensionality(instances));
        metafeatures.add(new MetaSkewness(instances));
        metafeatures.add(new MetaKurtosis(instances));
        //metafeatures.add(new MetaLinearCorrelation(instances));
        metafeatures.add(new MetaAttributesEntropy(instances));
        metafeatures.add(new MetaClassEntropy(instances));
        metafeatures.add(new MetaMutualClassAttribute(instances));
        metafeatures.add(new MetaMutualClassAttributeMax(instances));
        metafeatures.add(new MetaEquivalentNumberOfFeatures(instances));
        metafeatures.add(new MetaNoiseSignalRatio(instances));
        return true;
    }

    public boolean extractMetaInfo(){
        if (!initMetaFeatureSet()){
            return false;
        }
        try {
            out = new PrintWriter(Utils.getResultFileName(curfile,"_metainfo", ".meta"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        for(Metafeature m : metafeatures){
            out.print("<" + m.getClass() + ">");
            out.println(m.calc());
            System.out.println(curfile + " - " + m.getClass() + "-> OK");
        }
        out.close();
        return true;
    }
}
