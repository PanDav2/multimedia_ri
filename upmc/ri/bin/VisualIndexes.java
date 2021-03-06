package upmc.ri.bin;

import upmc.ri.index.ImageFeatures;
import upmc.ri.index.VIndexFactory;
import upmc.ri.struct.DataSet;
import upmc.ri.struct.STrainingSample;
import upmc.ri.utils.PCA;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class VisualIndexes {

    // Path to computed dataset
    static String DEFAULT_OUTPUT_FILENAME = "/Users/david/Documents/Master/M2/RI/final_project/RI_2016 (2)/datasets/datasets";
    static String DEFAULT_SBOW_FILENAME = "/Users/david/Documents/Master/M2/RI/final_project/RI_2016 (2)/sbow";

    public static void main(String[] args) {

        List<STrainingSample<double[], String>> listtrain = new ArrayList<STrainingSample<double[], String>>();
        List<STrainingSample<double[], String>> listtest = new ArrayList<STrainingSample<double[], String>>();
        /*for every file*/
        File folder = new File(DEFAULT_SBOW_FILENAME);
        for (final File file : folder.listFiles()) {
            String name = file.getName();
            System.out.println("computing " + name);

            try (BufferedReader buff = new BufferedReader(new FileReader(file))) {
                String line;
                // 1st line : describing file format
                line = buff.readLine();
                //here : line ; fileformat
                int i = 0;
                while ((line = buff.readLine()) != null) {
                    // skipping Image ID

                    String ID;

                    ID = line;
                    line = buff.readLine();

                    // words
                    String[] linesplit = line.replace("[", "").replace("]", "").split(";");
                    List<Integer> wordsim = new ArrayList<Integer>();

                    // get_all word
                    for (int k = 0; k < linesplit.length - 1; k++) {

                        int word = Integer.parseInt(linesplit[k]);
                        wordsim.add(word);
                    }
                    line = buff.readLine();

                    // X : 3 lines for sikipping x-y-BB
                    List<Double> x = new ArrayList<Double>();
                    String[] linesplitx = line.replace("[", "").replace("]", "").split(";");

                    // get_all x
                    for (int k = 0; k < linesplitx.length - 1; k++) {
                        double xactual = Double.parseDouble(linesplitx[k]);
                        x.add(xactual);
                    }
                    line = buff.readLine();
                    //Y
                    List<Double> y = new ArrayList<Double>();
                    String[] linesplity = line.replace("[", "").replace("]", "").split(";");

                    // get_all Y
                    for (int k = 0; k < linesplity.length - 1; k++) {
                        double yactual = Double.parseDouble(linesplity[k]);
                        y.add(yactual);
                    }

                    line = buff.readLine();
                    //skip line for BB


                    //create Bow
                    ImageFeatures ib = new ImageFeatures(x, y, wordsim, ID);

                    VIndexFactory Bow = new VIndexFactory();
                    double[] trainBow = Bow.computeBow(ib);


                    //get label
                    String Label = name ;//file.getName();

                    //create training example
                    STrainingSample<double[], String> strainingsample = new STrainingSample<double[], String>(trainBow, Label);

                    //Train or test
                    if (i > 800) {
                        //testing
                        listtest.add(strainingsample);
                    } else {
                        //training
                        listtrain.add(strainingsample);
                    }

                    i = i + 1;

                }
                //System.out.println(i + " images dans " + file.getName());
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
        DataSet<double[], String> data = new DataSet<double[], String>(listtrain, listtest);

        //PCA
        PCA pca = new PCA();
        DataSet<double[], String> data_withpca = pca.computePCA(data, 250);

        //serialisation du data set
        try (FileOutputStream fos = new FileOutputStream(DEFAULT_OUTPUT_FILENAME)) {
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(data_withpca);
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
