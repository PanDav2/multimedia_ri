package upmc.ri.bin;

import upmc.ri.io.ImageNetParser;
import upmc.ri.struct.DataSet;
import upmc.ri.struct.Evaluator;
import upmc.ri.struct.STrainingSample;
import upmc.ri.struct.instantiation.MultiClass;
import upmc.ri.struct.model.LinearStructModel_Ex;
import upmc.ri.struct.training.SGDTrainer;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class MulticlassClassif {

    static String DEFAULT_FILENAME = "/Users/david/Documents/Master/M2/RI/final_project/RI_2016 (2)/datasets/datasets";
//    (double pas, double lambda, int iteration)
    static double DEFAULT_PAS = 0.001;
    static double DEFAULT_LAMBDA = 0.001;
    static int DEFAULT_ITERATION = 100;


    public static void main(String[] args) {

        try (FileInputStream fis = new FileInputStream(DEFAULT_FILENAME)) {

            ObjectInputStream ois = new ObjectInputStream(fis);
            DataSet<double[], String> dataset = null;

            try {
                dataset = (DataSet<double[], String>) ois.readObject();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            ois.close();

            int sizeBow = dataset.listtrain.get(0).input.length;


            //instanciation des struct et model
            //setting labels

            Set<String> labels = ImageNetParser.classesImageNet();
            int nbr_labels = labels.size();
            int params_size = (int) sizeBow*nbr_labels;

            //instantiation IStructinstantiation
            MultiClass multiclass = new MultiClass(labels);

            //instantiation linear struct
            LinearStructModel_Ex<double[], String> Linearmodel = new LinearStructModel_Ex(params_size, multiclass);

            Evaluator<double[], String> evaluator = new Evaluator<>();

            SGDTrainer<double[], String> Sgdtrainer = new SGDTrainer(DEFAULT_PAS, DEFAULT_LAMBDA, DEFAULT_ITERATION);


            // TRAINING !!
            evaluator.setListtrain(dataset.listtrain);
            evaluator.setListtest(dataset.listtest);
            evaluator.setModel(Linearmodel);
            Sgdtrainer.setEvaluator(evaluator);

            Sgdtrainer.train(dataset.listtrain, Linearmodel);

//            evaluator.evaluate();
//            System.out.println("erreur train : " + evaluator.getErr_train());
//            System.out.println("erreur test : " + evaluator.getErr_test());

            // CONFUSION MATRIX PRINTING
            
            //initialisation liste
            List<String> predictions = new ArrayList<>();
            List<String> gt = new ArrayList<>();
            //get liste of prediction
            for (STrainingSample<double[], String> ts : dataset.listtest) {
                String yhat = Linearmodel.predict(ts);
                String y = ts.output;
                predictions.add(yhat);
                gt.add(y);
            }
            multiclass.confusionMatrix(predictions, gt);


        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
