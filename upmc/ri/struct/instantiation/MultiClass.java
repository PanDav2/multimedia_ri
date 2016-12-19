package upmc.ri.struct.instantiation;

import java.util.Set;
import java.util.*;

import org.ejml.ops.*;
import org.ejml.data.*;
import upmc.ri.io.ImageNetParser;
import upmc.ri.struct.model.utils;

import static upmc.ri.struct.model.utils.calcutate_difference;

public class MultiClass implements IStructInstantiation<double[], String> {
    public Set<String> label;

    public Map<String, Integer> getMyMap() {
        return myMap;
    }

    Map<String, Integer> myMap;

    public MultiClass(Set<String> labels) {
        this.label = labels;
        this.myMap = new HashMap<String, Integer>();

        int i = 0;
        for (String Label : labels) {
            this.myMap.put(Label, i);
            i = i + 1;
        }
    }

    public double[] psi(double[] x, String y) {
        /**
         *  This module compute the joint feature map
         */
        int labels_size = this.enumerateY().size();
        int len_x = x.length;

        double[] res = new double[labels_size * len_x];
        int index = myMap.get(y);

        for (int i = 0; i < x.length; i++) {
            res[index * x.length + i] = x[i];
        }

        return res;
    }

    public static double[] create_zero_vec(int size_vec) {
        double[] res = new double[size_vec];

        for (int j = 0; j < size_vec; j++) {
            res[j] = 0;
        }
        return res;
    }


    public double delta(String y1, String y2) {
        //0-1 Loss
        double res;
        if (y1.equals(y2)) {
            res = 0;
        } else {
            res = 1;
        }
        return res;
    }

    public Set<String> enumerateY() {
        return label;
    }

    public void confusionMatrix(List<String> predictions, List<String> gt) {
        Set<String> Labels = this.enumerateY();

        int nbrLabel = Labels.size();

        double[][] confusionData = new double[nbrLabel][nbrLabel];
        int nbrpredictions = predictions.size();

        for (int j = 0; j < nbrpredictions; j++) {
            int row = myMap.get(predictions.get(j));
            int column = myMap.get(gt.get(j));
            confusionData[row][column] += 1;
        }

        DenseMatrix64F confusionMatrix = new DenseMatrix64F(confusionData);
        MatrixVisualization.show(confusionMatrix, "matrice de confusion");

    }


    public static void main(String[] args) {

        Random randomizer = new Random();
        Set<String> classes = ImageNetParser.classesImageNet();
        MultiClass m = new MultiClass(classes);

        int DIM_PARAMS = 5;

        double[] datapoint_x = utils.generate_parameters(DIM_PARAMS);
        double[] params = utils.generate_parameters(DIM_PARAMS);

        for (String c : classes) {
            System.out.println(c);
        }

        String test_label = "acoustic_guitar.txt";
        String test_label_2 = "electric_guitar.txt";

//        double[] res = m.psi_(datapoint_x, test_label_2);
//        double[] res_2 = m.psi_(datapoint_x, test_label);

//        double[] aaa = utils.calcutate_difference(res, res_2);
//
//        System.out.println(Arrays.toString(aaa));

    }


}
