package upmc.ri.struct.model;


import upmc.ri.struct.STrainingSample;
import upmc.ri.struct.instantiation.IStructInstantiation;

import java.util.Random;
import java.util.Set;
import java.lang.Object.*;

public class LinearStructModel_Ex<X,Y> extends LinearStructModel<X,Y> {

    public LinearStructModel_Ex(int dimpsi,IStructInstantiation<X,Y> Structure) {
        //initialisation structure
        this.parameters = new double[dimpsi];

        //Initialisation weights
        Random randomizer = new Random();
        for (int i =0;i<dimpsi;i++){
            parameters[i] = randomizer.nextDouble();
        }
        this.Structure = Structure;
    }

    public Y predict(STrainingSample<X,Y> ts){

        Y res = ts.output;


        Set<Y> y = Structure.enumerateY();

        X x = ts.input;


        double maxi = Double.POSITIVE_INFINITY;
        maxi = maxi*-1;
        for (Y label : y) {
            double[] psixlabel = this.Structure.psi(x, label);
            double max = 0;
            for (int i = 0; i < parameters.length;i++) {
                max += parameters[i] * psixlabel[i];
            }
            if (max > maxi) {
                maxi = max;
                res = label;
            }
        }

        return res;

    }

    public Y lai(STrainingSample<X,Y> ts){

        Set<Y> labels = Structure.enumerateY();

        int nbrlabel = labels.size();

        X x = ts.input;
        Y y = ts.output;

        Y yhat = y;
        double[] maximum = new double[nbrlabel];
        int i =0;
        double max = Double.POSITIVE_INFINITY;
        max = max*-1;

        for (Y label : labels){

            double[] psixilabel = Structure.psi(x,label);

            double produit_psixilabel_parameters = 0;

            for (int j =0;j< parameters.length;j++){
                produit_psixilabel_parameters += psixilabel[j]*parameters[j];
            }

            double delta = Structure.delta(label,y);

            maximum[i] = delta - produit_psixilabel_parameters;

            if (maximum[i]>max){
                max = maximum[i];
                yhat = label;
            }
            i = i+1;
        }
        return yhat;
    }

}
