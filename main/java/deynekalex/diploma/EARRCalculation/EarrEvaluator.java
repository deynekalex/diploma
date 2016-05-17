package deynekalex.diploma.EARRCalculation;

import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.core.Instances;
import weka.filters.Filter;

import java.util.Random;

import static java.lang.StrictMath.log10;

/**
 * Created by deynekalex on 10.04.16.
 */
public class EarrEvaluator {
    double alpha = 1;
    double betha = 1;
    Instances dataset;
    Filter filter1;
    Filter filter2;
    EarrEvaluator(Instances dataset, Filter filter1, Filter filter2){
        this.dataset = dataset;
        this.filter1 = filter1;
        this.filter2 = filter2;
        try {
            filter1.setInputFormat(dataset);
            filter2.setInputFormat(dataset);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public double evaluate() throws Exception {
        long startTime = System.nanoTime();
        Instances newData1 = Filter.useFilter(dataset,filter1);
        long endTime = System.nanoTime();
        long timeDuration1 = (endTime - startTime);
        System.out.println(timeDuration1);
        return 1;
        /*
        double accuracy1 = accuracy(newData1);
        startTime = System.nanoTime();
        Instances newData2 = Filter.useFilter(dataset,filter2);
        endTime = System.nanoTime();
        long timeDuration2 = (endTime - startTime);
        double accuracy2 = accuracy(newData2);
        return (accuracy1/accuracy2)/
                (1 + alpha * log10(timeDuration1/timeDuration2)
                        + betha * log10(newData1.numAttributes()/newData2.numAttributes()));*/
    }

    private double accuracy(Instances data) throws Exception {
        data.setClassIndex(0);
        Evaluation eval = new Evaluation(data);
        eval.crossValidateModel(new NaiveBayes(),data,5,new Random(1));
        return eval.fMeasure(0);//TODO needed to be tested
    }
//    They are bayes-based
//    Naive Bayes (John & Langley, 1995) and Bayes Network (Friedman, Geiger, & Goldszmidt,
//            1997), information gain-based C4.5 (Quinlan, 1993), rule-based PART (Frank &
//     Witten,1998), and instance-based IB1 (Aha, Kibler, & Albert, 1991), respectively
}
