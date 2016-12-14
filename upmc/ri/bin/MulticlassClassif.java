package upmc.ri.bin;

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
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class MulticlassClassif {

    static String DEFAULT_FILENAME = "/Users/david/Documents/Master/M2/RI/final_project/RI_2016 (2)/sbow/";

    public static void main(String[] args) {

        try (FileInputStream fis = new FileInputStream(DEFAULT_FILENAME)) {
            ObjectInputStream ois = new ObjectInputStream(fis);
            DataSet<double[], String> dataset = null;
            try {
                dataset = (DataSet<double[], String>) ois.readObject();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            int sizeBow = dataset.listtrain.get(0).input.length;

            //instanciation des struct et model
            //setting labels
            Set<String> labels = new HashSet<String>();
            labels.add("acoustic_guitar.txt");
            labels.add("harp.txt");
            labels.add("taxi.txt");
            labels.add("minivan.txt");
            labels.add("wood-frog.txt");
            labels.add("tree-frog.txt");
            labels.add("electric_guitar.txt");
            labels.add("ambulance.txt");
            labels.add("european_fire_salamander.txt");

            //instantiation IStructinstantiation
            MultiClass multiclass = new MultiClass(labels);

            //instantiation linear struct
            LinearStructModel_Ex<double[], String> Linearmodel = new LinearStructModel_Ex(sizeBow, multiclass);


            Evaluator<double[], String> evaluator = new Evaluator<>();

            SGDTrainer<double[], String> Sgdtrainer = new SGDTrainer<double[], String>(0.001, 0.001, 100);

            Sgdtrainer.train(dataset.listtrain, Linearmodel);


            evaluator.setListtrain(dataset.listtrain);
            evaluator.setListtest(dataset.listtest);
            evaluator.setModel(Linearmodel);

            evaluator.evaluate();
            System.out.println("erreur test : " + evaluator.getErr_test());
            System.out.println("erreur train : " + evaluator.getErr_train());

            //affichage matrice de confusion
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


            ois.close();

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
