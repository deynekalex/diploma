package deynekalex.diploma;

import weka.attributeSelection.*;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.Logistic;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.functions.SMO;
import weka.classifiers.lazy.IBk;
import weka.classifiers.rules.PART;
import weka.classifiers.trees.J48;
import weka.classifiers.trees.RandomForest;
import weka.core.Instances;
import weka.core.converters.CSVLoader;
import weka.filters.Filter;
import weka.filters.supervised.attribute.AttributeSelection;

import java.io.*;
import java.util.Random;

/**
 * Created by deynekalex on 22.03.16.
 */
public class Classificator {
    public static void main(String[] args) {
        CSVLoader loader = new CSVLoader();
        Instances data = null;
        Evaluation eval = null;
        try {
            WrapperSubsetEval wrapperSubsetEval = new WrapperSubsetEval();
            GreedyStepwise search = new GreedyStepwise();
            search.setSearchBackwards(true);
            /*filtering.setEvaluator(new CfsSubsetEval());
            filtering.setSearch(search);*/
            loader.setSource(new File("metaFileDataset.csv"));
            data = loader.getDataSet();
            data.setClassIndex(0);
            //filtering.setInputFormat(data);
            //Instances newData1 = Filter.useFilter(data,filtering);
            eval = new Evaluation(data);
            eval.crossValidateModel(new J48(),data,5,new Random(1));
            System.out.println(eval.toSummaryString("\nResults\n======\n", false));
            System.out.println(eval.toClassDetailsString());
            ///////////////////////////////////////////////////
            /*eval = new Evaluation(data);
            eval.crossValidateModel(new LinearRegression(),data,246,new Random(1));
            System.out.println(eval.toSummaryString("\nResults\n======\n", false));
            System.out.println(eval.toClassDetailsString());*/
            //////////////////////////////////////////////////
            System.out.println("Logistic");
            eval = new Evaluation(data);
            eval.crossValidateModel(new Logistic(),data,5,new Random(1));
            System.out.println(eval.toSummaryString("\nResults\n======\n", false));
            System.out.println(eval.toClassDetailsString());
            //////////////////////////////////////////////////
            System.out.println("SVM");
            eval = new Evaluation(data);
            eval.crossValidateModel(new SMO(),data,5,new Random(1));
            System.out.println(eval.toSummaryString("\nResults\n======\n", false));
            System.out.println(eval.toClassDetailsString());
            //////////////////////////////////////////////////
            System.out.println("J48");
            eval = new Evaluation(data);
            eval.crossValidateModel(new J48(), data, 5, new Random(1));
            System.out.println(eval.toSummaryString("\nResults\n======\n", false));
            System.out.println(eval.toClassDetailsString());
            //////////////////////////////////////////////////
            System.out.println("IBk");
            eval = new Evaluation(data);
            eval.crossValidateModel(new IBk(), data, 5, new Random(1));
            System.out.println(eval.toSummaryString("\nResults\n======\n", false));
            System.out.println(eval.toClassDetailsString());
            //////////////////////////////////////////////////
            System.out.println("MultilayerPerceptron");
            eval = new Evaluation(data);
            eval.crossValidateModel(new MultilayerPerceptron(), data, 5, new Random(1));
            System.out.println(eval.toSummaryString("\nResults\n======\n", false));
            System.out.println(eval.toClassDetailsString());
            //////////////////////////////////////////////////*/
            System.out.println("Part");
            eval = new Evaluation(data);
            eval.crossValidateModel(new PART(), data, 5, new Random(1));
            System.out.println(eval.toSummaryString("\nResults\n======\n", false));
            System.out.println(eval.toClassDetailsString());
            //////////////////////////////////////////////////
            System.out.println("Random Forest");
            eval = new Evaluation(data);
            eval.crossValidateModel(new RandomForest(), data, 5, new Random(1));
            System.out.println(eval.toSummaryString("\nResults\n======\n", false));
            System.out.println(eval.toClassDetailsString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void filter(Instances initialData) throws Exception {
        AttributeSelection filter = new AttributeSelection();  // package weka.filters.supervised.attribute!
        CfsSubsetEval eval = new CfsSubsetEval();
        GreedyStepwise search = new GreedyStepwise();
        search.setSearchBackwards(true);
        filter.setEvaluator(eval);
        filter.setSearch(search);
        filter.setInputFormat(initialData);
        // generate new data
        Instances newData = Filter.useFilter(initialData,filter);
        System.out.println(newData);
    }
}
