package upmc.ri.struct.model;

import upmc.ri.struct.STrainingSample;
import upmc.ri.struct.instantiation.IStructInstantiation;
import upmc.ri.struct.ranking.RankingFunctions;
import upmc.ri.struct.ranking.RankingOutput;

import java.util.*;

/**
 * Created by gozuslayer on 13/12/16.
 */
public class RankingStructModel extends LinearStructModel<List<double[]>,RankingOutput> {

    public RankingStructModel(int dimpsi,IStructInstantiation<List<double[]>,RankingOutput> Structure){
        //initialisation structure
        this.parameters = new double[dimpsi];

        //Initialisation weights
        Random randomizer = new Random();
        for (int i =0;i<dimpsi;i++){
            parameters[i] = randomizer.nextDouble();
        }
        this.Structure = Structure;
    }

    public RankingOutput predict(STrainingSample<List<double[]>,RankingOutput> ts){

        List<double[]> Images = ts.input;

        int nbrImages = Images.size();

        double[] listescore = new double[nbrImages];

        int[] index = new int[nbrImages];

        //Calculating score for all image
        for (int i=0;i<nbrImages;i++){
            double[] Bow = Images.get(i);
            int nbrBow = Bow.length;
            double score = 0;
            for (int j =0;j<nbrBow;j++){
                score += Bow[j]*parameters[j];
            }

            listescore[i] = score;
            index[i] = i;
        }

        //sorting list
        quicksort(listescore,index);
        RankingOutput rankingoutput = ts.output;

        List<Integer> labelsGT = rankingoutput.getLabelsGT();
        int nbplus = rankingoutput.getNbPlus();

        int[] ranking = index;
        //convert to list
        List<Integer> Rankings = new ArrayList<>();
        for (int u : ranking){
            Rankings.add(u);
        }


        RankingOutput res = new RankingOutput(nbplus,Rankings,labelsGT);


        return res;


    }


    public RankingOutput lai(STrainingSample<List<double[]>,RankingOutput> ts){
        RankingOutput res = RankingFunctions.loss_augmented_inference(ts,parameters);
        return res;
    }

    //quicksort
    public static void quicksort(double[] main, int[] index) {
        quicksort(main, index, 0, index.length - 1);
    }


    public static void quicksort(double[] a, int[] index, int left, int right) {
        if (right <= left) return;
        int i = partition(a, index, left, right);
        quicksort(a, index, left, i-1);
        quicksort(a, index, i+1, right);
    }

    private static int partition(double[] a, int[] index,
                                 int left, int right) {
        int i = left - 1;
        int j = right;
        while (true) {
            while (less(a[++i],  a[right]))
                ;
            while (less(a[right], a[--j]))
                if (j == left) break;
            if (i >= j) break;
            exch(a, index, i, j);
        }
        exch(a, index, i, right);
        return i;
    }


    private static boolean less(double x, double y) {
        return (x < y);
    }

    private static void exch(double[] a, int[] index, int i, int j) {
        double swap = a[i];
        a[i] = a[j];
        a[j] = swap;
        int b = index[i];
        index[i] = index[j];
        index[j] = b;
    }
}
