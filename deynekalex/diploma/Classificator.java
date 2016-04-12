package deynekalex.diploma;

import weka.attributeSelection.*;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.BayesianLogisticRegression;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.functions.SimpleLinearRegression;
import weka.classifiers.functions.SimpleLogistic;
import weka.classifiers.pmml.consumer.NeuralNetwork;
import weka.classifiers.trees.RandomForest;
import weka.core.Instances;
import weka.core.converters.CSVLoader;
import weka.core.converters.ConverterUtils;
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
            AttributeSelection filter = new AttributeSelection();
            GreedyStepwise search = new GreedyStepwise();
            search.setSearchBackwards(true);
            filter.setEvaluator(new CfsSubsetEval());
            filter.setSearch(search);
            loader.setSource(new File("zdatasets_csv/data1_train(123,54675)_norm_ova(123,54675).csv"));
            data = loader.getDataSet();
            data.setClassIndex(0);
            filter.setInputFormat(data);
            Instances newData1 = Filter.useFilter(data,filter);
            /*eval = new Evaluation(data);
            eval.crossValidateModel(new NaiveBayes(),data,5,new Random(1));
            System.out.println(eval.toSummaryString("\nResults\n======\n", false));
            eval = new Evaluation(data);
            eval.crossValidateModel(new SimpleLogistic(),data,5,new Random(1));
            System.out.println(eval.toSummaryString("\nResults\n======\n", false));*/
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
