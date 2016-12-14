package upmc.ri.struct.instantiation;

import java.util.Set;
import java.util.*;
import org.ejml.ops.*;
import org.ejml.data.*;

public class MultiClass implements IStructInstantiation<double[], String> {
    public Set<String> label;
    Map <String,Integer> myMap;

    public MultiClass(Set<String> labels){
        this.label = labels;
        this.myMap = new HashMap<String,Integer>();
        int i =0;

        for (String Label : labels) {
            this.myMap.put(Label,i);
            i = i+1;
        }
    }

    public double[] psi(double[] x,String y){

        Set<String> Labels = this.enumerateY();
        int nbrLabel = Labels.size();

        int index = myMap.get(y);

        double[][] res = new double[nbrLabel][x.length];
        for (int j =0;j<x.length;j++){
            res[index][j] = x[j];
        }
        return res[index];

    }

    public double delta(String y1,String y2){
        //0-1 Loss
        double res;
        if (y1.equals(y2)){
            res = 0;
        }
        else{
            res = 1;
        }
        return res;
    }

    public Set<String> enumerateY(){return label;}

    public void confusionMatrix(List<String> predictions,List<String> gt){
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
        MatrixVisualization.show(confusionMatrix,"matrice de confusion");

    }

}
