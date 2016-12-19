package upmc.ri.index;
import upmc.ri.utils.VectorOperations;

import java.util.List;


public class VIndexFactory {

    public static double [] computeBow(ImageFeatures ib){
        List <Integer> words = ib.getwords();

        /*initialisation du BoW*/
        double[] result = new double[ib.tdico];

        for (Integer item : words){
            result[item]++;
        }
        /*Normalisation*/
        double norms = VectorOperations.norm(result);
        for (int k=0 ; k<result.length ; k++){
            result[k] = result[k]/norms;
        }


        return result;

    }
}

