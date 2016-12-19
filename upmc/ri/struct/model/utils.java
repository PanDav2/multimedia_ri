package upmc.ri.struct.model;

import org.ejml.data.DenseMatrix64F;
import org.ejml.factory.DecompositionFactory;
import org.ejml.interfaces.decomposition.SingularValueDecomposition;
import org.ejml.ops.CommonOps;
import org.ejml.ops.NormOps;
import org.ejml.ops.SingularOps;
import upmc.ri.io.ImageNetParser;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class utils {

    public static double[] generate_parameters(int dim_params) {
        Random randomizer = new Random();
        double[] res = new double[dim_params];
        for (int i = 0; i < dim_params; i++) {
            res[i] = randomizer.nextDouble();
        }
        return res;
    }

    public static double[][] generate_parameters(int dim_labels, int dim_params) {
        Random randomizer = new Random();
        double[][] res = new double[dim_labels][dim_params];
        for (int l = 0; l < dim_labels; l++) {
            for (int i = 0; i < dim_params; i++) {
                res[l][i] = randomizer.nextDouble();
            }
        }
        return res;
    }

    public static int compute_arg_max(double[] double_array) {
        double max = Double.NEGATIVE_INFINITY;
        int arg = 0;
        for (int i = 0; i < double_array.length; i++) {
            if (double_array[i] > max) {
                arg = i;
                max = double_array[i];
            }
        }
        return arg;
    }

    public static double[] compute_multi_value(double value, int dimension) {

        double[] res = new double[dimension];
        for (int i = 0; i < dimension; i++) {
            res[i] = value;
        }

        return res;
    }

    public static double scalar_product_params_jfm(double[] jfm, double[] a) {
        assert a.length == jfm.length;
        double res = 0;
        // Labels iteration
        for (int l = 0; l < jfm.length; l++) {
            // Parameters iteration
            res += a[l] * jfm[l];

        }
        return res;
    }

    public static double[] calcutate_difference(double[] res, double[] res_2) {

        int dim_1 = res.length;

        double[] aaa = new double[dim_1];

        for (int i = 0; i < res.length; i++) {
            for (int j = 0; j < res.length; j++) {
                aaa[i] = res[i] - res_2[i];
            }
        }
        return aaa;
    }

    public static void main(String[] args) {
        double[] p = generate_parameters(5);
        System.out.println(Arrays.toString(p));
        System.out.println(compute_arg_max(p));
    }

    public static void write_text_file (List<String> text_array) throws IOException {
        Path file = Paths.get("results.txt");
        Files.write(file,text_array, Charset.forName("UTF-8"));
    }

}
