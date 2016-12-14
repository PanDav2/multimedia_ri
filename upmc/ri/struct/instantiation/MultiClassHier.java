package upmc.ri.struct.instantiation;
import java.util.*;
import edu.cmu.lti.ws4j.*;
import edu.cmu.lti.ws4j.impl.WuPalmer;
import edu.cmu.lti.lexical_db.ILexicalDatabase;
import edu.cmu.lti.lexical_db.NictWordNet;
import edu.cmu.lti.ws4j.RelatednessCalculator;


public class MultiClassHier extends MultiClass {


    public double[][] distances;
    public Map <String,Integer> myMap;


    public MultiClassHier(Set<String> labels){
        super(labels);
        //constructor
        int nbrLabel = labels.size();
        myMap = new HashMap<String,Integer>();
        int i =0;
        for (String label : labels) {
            myMap.put(label,i);
            i = i+1;
        }


        //Calcul des distances
        ILexicalDatabase db = new NictWordNet();
        RelatednessCalculator rc = new WuPalmer(db);
        this.distances = new double[nbrLabel][nbrLabel];

        for (String label1 : labels){
            for (String label2 : labels){
                if (label1 != label2){
                    double s = rc.calcRelatednessOfWords(label1, label2);
                    this.distances[myMap.get(label1)][myMap.get(label2)] = s;
                }
            }
        }

        //Passage au dissimilarite
        for (int l =0;l<nbrLabel;l++){
            for (int c = 0;c<nbrLabel;c++){
                if (l!=c) {
                    this.distances[l][c] = 1 - this.distances[l][c];
                }else{
                    this.distances[l][c] = 0 ;
                }
            }
        }

        //normalisation des distances
        //TODO

    }
    public double delta(String y1,String y2) {
        double res = distances[myMap.get(y1)][myMap.get(y2)];
        return res;
    }

}
