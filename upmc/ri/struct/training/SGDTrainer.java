package upmc.ri.struct.training;

import upmc.ri.io.ImageNetParser;
import upmc.ri.struct.Evaluator;
import upmc.ri.struct.STrainingSample;
import upmc.ri.struct.instantiation.IStructInstantiation;
import upmc.ri.struct.model.IStructModel;
import upmc.ri.struct.model.utils;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Set;


public class SGDTrainer<X, Y> implements ITrainer<X, Y> {
    public void setEvaluator(Evaluator<X, Y> evaluator) {
        this.evaluator = evaluator;
    }

    private Evaluator<X, Y> evaluator;
    //Hyperparamètres
    private double pas;
    private double lambda;
    private int iteration;

    public SGDTrainer(double pas, double lambda, int iteration) {
        this.pas = pas;
        this.lambda = lambda;
        this.iteration = iteration;
    }

    public void train(List<STrainingSample<X, Y>> lts, IStructModel<X, Y> model) {


        // PARAMS INITIALIZATION
        double[] weights = model.getParameters();
        int weightlen = weights.length;
        Set<Y> list_of_labels = model.instantiation().enumerateY();
        String[] results = new String[iteration];

        // ACTUAL CALCULATION
//        System.out.println("Training model");
//        System.out.println("Training size :" + lts.size());

        System.out.println("iteration"+"\t"+"train"+"\t"+"test");
        for (int i = 0; i < iteration; i++) {

            for (int j = 0; j < lts.size(); j++) {
                //index aleatoire
                Random randomizer = new Random();
                STrainingSample<X, Y> randomX_Y = lts.get(randomizer.nextInt(lts.size()));

                X Xidx = randomX_Y.input;
                Y yhat = model.lai(randomX_Y);
                Y Yidx = randomX_Y.output;


                double[] gi = new double[weightlen];
                double[] psi_yhat = model.instantiation().psi(Xidx, yhat);
                double[] psi_y = model.instantiation().psi(Xidx, Yidx);

                // updating
                for (int k = 0; k < weightlen; k++) {
                    gi[k] = psi_yhat[k] - psi_y[k];
                }

                //mise a jour
                for (int k = 0; k < weights.length; k++) {
                    weights[k] = weights[k] - pas * (weights[k] * lambda + gi[k]);
                }
                evaluator.setModel(model);
            }

            evaluator.evaluate();
            // Writing for plotting

            results[i] = i+"\t"+evaluator.getErr_train()+"S\t"+evaluator.getErr_test();

            System.out.println(results[i]);
//            System.out.println("######################");
//            System.out.println("erreur on train at iteration " + i);
//            System.out.println("######################");
//            System.out.println(evaluator.getErr_train());
//            System.out.println("erreur on test at iteration " + i);
//            System.out.println(evaluator.getErr_test());
            //fin iteration i
        }
        List<String> r = Arrays.asList(results);
        try {
            utils.write_text_file(r);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Arrays.toString(results);

    }

    public double convex_loss(List<STrainingSample<X, Y>> lts, IStructModel<X, Y> model) {
        double[] parameters = model.getParameters();

        double res1 = 0;
        double res2 = 0;
        for (int i = 0; i < parameters.length; i++) {
            res1 = res1 + (lambda / 2) * Math.pow(parameters[i], 2);
        }

        int n = lts.size();
        for (int i = 0; i < n; i++) {
            STrainingSample<X, Y> ts = lts.get(i);
            X xi = ts.input;
            Y yi = ts.output;
            IStructInstantiation<X, Y> modelinstance = model.instantiation();
            Set<Y> labels = modelinstance.enumerateY();
            int nbrLabel = labels.size();


            double[] maximum = new double[nbrLabel];
            int l = 0;
            double[] psixiyi = modelinstance.psi(xi, yi);
            double produit_psixilyi_parameters = 0;
            for (int k = 0; k < parameters.length; k++) {
                produit_psixilyi_parameters += psixiyi[k] * parameters[k];
            }

            for (Y label : labels) {

                double[] psixilabel = modelinstance.psi(xi, label);

                double produit_psixilabel_parameters = 0;
                for (int j = 0; j < parameters.length; j++) {
                    produit_psixilabel_parameters += psixilabel[j] * parameters[j];
                }
                double delta = modelinstance.delta(yi, label);
                maximum[l] = delta + produit_psixilabel_parameters;
                l = l + 1;
            }

            //get max
            double max = 0;
            for (int counter = 1; counter < maximum.length; counter++) {
                if (maximum[counter] > max) {
                    max = maximum[counter];
                }
            }
//            res2 = res2 + (1 / n) * (max - produit_psixilyi_parameters);

        }
        double res = res1 + res2;
        return res;
    }
}
