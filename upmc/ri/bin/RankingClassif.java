package upmc.ri.bin;

import upmc.ri.struct.DataSet;
import upmc.ri.struct.Evaluator;
import upmc.ri.struct.instantiation.RankingInstantiation;
import upmc.ri.struct.model.LinearStructModel_Ex;
import upmc.ri.struct.model.RankingStructModel;
import upmc.ri.struct.ranking.RankingFunctions;
import upmc.ri.struct.ranking.RankingOutput;
import upmc.ri.struct.training.SGDTrainer;
import upmc.ri.utils.Drawing;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;


public class RankingClassif {

    static String FILENAME = "/Users/david/Documents/Master/M2/RI/final_project/RI_2016 (2)/datasets/datasets";
    static String TEST_SET = "acoustic_guitar.txt";
    static int DEFAULT_DIM_PSI = 200;
    //SGDTrainer(double pas,double lambda,int iteration){
    static double DEFAULT_STEP = 0.001;
    static double DEFAULT_LAMBDA = 0.00001;
    static int DEFAULT_ITERATION = 50;


    public static void main(String[] args) {
        //Charger les donnees
        try (FileInputStream fis = new FileInputStream(FILENAME)) {
            ObjectInputStream ois = new ObjectInputStream(fis);
            DataSet<double[], String> dataset = null;
            try {
                dataset = (DataSet<double[], String>) ois.readObject();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            //Create dataset for ranking
            DataSet<List<double[]>, RankingOutput> datasetforranking = RankingFunctions.convertClassif2Ranking(dataset, TEST_SET);

            //creer un RankingInstantiation
            RankingInstantiation rgI = new RankingInstantiation();
            RankingStructModel rkgmodel = new RankingStructModel(DEFAULT_DIM_PSI,rgI);

            //setting evaluator
            Evaluator<List<double[]>,RankingOutput> evaluator = new Evaluator<>();
            evaluator.setListtest(datasetforranking.listtest);
            evaluator.setListtrain(datasetforranking.listtrain);
            evaluator.setModel(rkgmodel);

            //training
            SGDTrainer<List<double[]>,RankingOutput> sgdTrainer = new SGDTrainer(DEFAULT_STEP,DEFAULT_LAMBDA,DEFAULT_ITERATION);
            sgdTrainer.train(datasetforranking.listtrain,rkgmodel);

            //tracer courbe précision rappel & AP & MAP


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
