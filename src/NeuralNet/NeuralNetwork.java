package NeuralNet;

import java.util.List;
import java.util.function.Function;

public class NeuralNetwork {

    public int layers;
    public int[] layerSizes;
    Matrix[] weights;
    int totalParameters;

    double MUTATION_RATE;

    Function<Double, Double> sigmoid = x -> Math.tanh(x);
    Function<Double, Double> mutate = x -> (Math.random() < MUTATION_RATE) ? x + (Math.random() - 0.5) * 2 : x;

    public NeuralNetwork(int... size) {
        layers = size.length;
        layerSizes = size;
        weights = new Matrix[layers - 1];

        for (int i = 0; i < layers - 1; i++) {
            weights[i] = new Matrix(layerSizes[i + 1], layerSizes[i] + 1);
            weights[i].randomize();

            totalParameters += layerSizes[i + 1] * (layerSizes[i] + 1);
        }
    }

    public Matrix evaluate(double[] input) {

        // Convert to neuralArray, which adds an additional entry 1 for bias
        Matrix values = Matrix.getNeuralVector(input);

        // Loop through weights and propagate values
        for (int i = 0; i < layers - 1; i++) {
            Matrix newValues = Matrix.multiply(weights[i], values);
            newValues.map(sigmoid);
            if (i < layers - 2)
                newValues.extendByOne();
            values = newValues;
        }
        return values;
    }

    public Matrix evaluate(List<Double> input) {
        double[] inputArray = new double[input.size()];
        for (int i = 0; i < input.size(); i++) {
            inputArray[i] = input.get(i);
        }
        return evaluate(inputArray);
    }

    public void setMutationRate(double mutationRate) {
        MUTATION_RATE = mutationRate;
    }

    // Randomly changes each weight and bias with chance mutationRate
    public void mutate() {
        /*int approxMutations = (int)(MUTATION_RATE*totalParameters);
        System.out.println("~"+approxMutations+" mutations.");*/
        for (int i = 0; i < layers - 1; i++) {
            weights[i].map(mutate);
        }
    }

    // Returns a new but identical neural network
    public NeuralNetwork copy() {

        NeuralNetwork net = new NeuralNetwork(0);

        net.layers = layers;
        net.layerSizes = new int[layers];
        for (int i = 0; i < layers; i++)
            net.layerSizes[i] = layerSizes[i];

        net.totalParameters = totalParameters;
        net.MUTATION_RATE = MUTATION_RATE;

        net.weights = new Matrix[weights.length];
        for (int i = 0; i < weights.length; i++)
            net.weights[i] = weights[i].copy();

        return net;
    }

    public int getParameterCount() {
        return totalParameters;
    }
}