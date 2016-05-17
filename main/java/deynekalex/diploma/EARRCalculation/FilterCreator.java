package deynekalex.diploma.EARRCalculation;

import weka.attributeSelection.*;
import weka.filters.Filter;
import weka.filters.supervised.attribute.AttributeSelection;

/**
 * Created by deynekalex on 11.04.16.
 */
public class FilterCreator{
    AttributeSelection filter = new AttributeSelection();

    public FilterCreator(ASEvaluation eval, ASSearch search, String options){
        /*String[] opt = null;
        try {
            opt = weka.core.Utils.splitOptions(options);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            filter.setOptions(opt);
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        filter.setEvaluator(eval);
        filter.setSearch(search);
    }

    public FilterCreator(ASEvaluation eval, ASSearch search){
        filter.setEvaluator(eval);
        filter.setSearch(search);
    }

    public Filter get(){
        return filter;
    }
}