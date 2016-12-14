package upmc.ri.struct.instantiation;

import upmc.ri.struct.ranking.RankingFunctions;
import upmc.ri.struct.ranking.RankingOutput;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by gozuslayer on 13/12/16.
 */
public class RankingInstantiation implements IStructInstantiation<List<double[]>, RankingOutput>{


    public double[] psi(List<double[]> x,RankingOutput y){

        List<Integer> ranks = y.getRanking();

        List<Integer> positionning = y.getPositionningFromRanking();

        int nbrImages = x.size();

        int sizex = x.get(0).length;

        List<Integer> LabelGT = y.getLabelsGT();

        double[] res = new double[sizex];

        //Calculate ranking matrice
        int[][] rankingMatrix = new int[nbrImages][nbrImages];

        for (int i = 0;i<nbrImages;i++){
            int positionxi = positionning.get(i);

            for (int j =0;j<nbrImages;j++){
                int positionxj = positionning.get(j);

                if (positionxi > positionxj){
                    rankingMatrix[i][j] = 1;
                }else{
                    rankingMatrix[i][j] = -1;
                }
            }
        }

        List<Integer> indexpositif = new ArrayList<>();
        List<Integer> indexnegatif = new ArrayList<>();

        for (int l = 0;l<nbrImages;l++){
            if(LabelGT.get(l) == 1){
                indexpositif.add(l);
            }else{
                indexnegatif.add(l);
            }
        }
        //pour chaque composante
        for (int n =0;n<sizex;n++){
            for (int h : indexpositif){
                for (int m : indexnegatif){
                    res[n] += rankingMatrix[h][m]*(x.get(h)[n]-x.get(m)[n]);
                }
            }
        }

        return res;



    }
    public double delta(RankingOutput y1,RankingOutput y2){
        double res = 1 - RankingFunctions.averagePrecision(y2);
        return res;
    }
    public Set<RankingOutput> enumerateY(){
        return null;
    }
}
