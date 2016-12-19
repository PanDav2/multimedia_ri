package upmc.ri.struct.model;


import upmc.ri.struct.STrainingSample;
import upmc.ri.struct.instantiation.IStructInstantiation;

import java.util.Random;
import java.util.Set;
import java.lang.Object.*;

public class LinearStructModel_Ex<X, Y> extends LinearStructModel<X, Y> {

    public LinearStructModel_Ex(int dimpsi, IStructInstantiation<X, Y> Structure) {
        //initialisation structure
        this.parameters = new double[dimpsi];

        //Initialisation weights
        Random randomizer = new Random();
        for (int i = 0; i < dimpsi; i++) {
            parameters[i] = randomizer.nextDouble();
        }
        this.Structure = Structure;
    }

    public Y predict(STrainingSample<X, Y> ts) {
//        return lai(ts);
        Set<Y> labels = Structure.enumerateY();

        X x = ts.getInput();
        Y y = ts.getOutput();

        Y yhat = y;

        double temp_loss;
        double max_loss = 0;

        for (Y l : labels) {
            // We compare the two structures
            double delta = Structure.delta(l, y);
            double u = utils.scalar_product_params_jfm(parameters, Structure.psi(x, l));
            temp_loss = u;
            if (temp_loss > max_loss) {
                max_loss = temp_loss;
                yhat = l;
            }
        }
        return yhat;
    }

    public Y lai(STrainingSample<X, Y> ts) {
        // INITIALIZATION
        Set<Y> labels = Structure.enumerateY();

        X x = ts.getInput();
        Y y = ts.getOutput();

        Y yhat = y;

        double temp_loss;
        double max_loss = 0;

        for (Y l : labels) {
            // We compare the two structures
            double delta = Structure.delta(l, y);
            double u = utils.scalar_product_params_jfm(parameters, Structure.psi(x, l));
            temp_loss = delta - u;
            if (temp_loss > max_loss) {
                max_loss = temp_loss;
                yhat = l;
            }


        }

        return yhat;
    }

}
