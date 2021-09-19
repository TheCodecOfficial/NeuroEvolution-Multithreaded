package NeuralNet;

import java.util.Arrays;
import java.util.function.Function;

public class Matrix {

    public int rows, cols;

    public double[][] data;

    public Matrix(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;

        data = new double[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                data[i][j] = 0;
            }
        }
    }

    public double get(int i, int j){
        return data[i][j];
    }

    // #region Static Vector Functions
    static Matrix getColumnVector(double[] array) {
        Matrix m = new Matrix(array.length, 1);

        for (int i = 0; i < array.length; i++) {
            m.data[i][0] = array[i];
        }

        return m;
    }

    static Matrix getRowVector(double[] array) {
        Matrix m = Matrix.getColumnVector(array);
        return Matrix.transpose(m);
    }

    static Matrix getNeuralVector(double[] array) {
        Matrix m = Matrix.getColumnVector(array);
        m.extendByOne();
        return m;
    }
    // #endregion

    // #region Static Jagged Array Functions
    static Matrix fromRowArray(double[][] array) {
        Matrix m = new Matrix(array.length, array[0].length);

        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[0].length; j++) {
                m.data[i][j] = array[i][j];
            }
        }

        return m;
    }

    static Matrix fromColumnArray(double[][] array) {
        Matrix m = Matrix.fromRowArray(array);
        return Matrix.transpose(m);
    }
    // #endregion

    void extendByOne() {
        double[] oldData = new double[rows];
        for (int i = 0; i < rows; i++) oldData[i] = data[i][0];
        rows++;

        data = new double[rows][cols];

        for (int i = 0; i < rows - 1; i++) {
            data[i][0] = oldData[i];
        }
        data[rows - 1][0] = 1;

    }

    public void randomize() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                data[i][j] = Math.random() * 2 - 1;
            }
        }
    }

    static Matrix transpose(Matrix m) {
        Matrix result = new Matrix(m.cols, m.rows);

        for (int i = 0; i < m.rows; i++) {
            for (int j = 0; j < m.cols; j++) {
                result.data[j][i] = m.data[i][j];
            }
        }
        return result;
    }

    static Matrix multiply(Matrix A, Matrix B) {
        if (A.cols != B.rows) {
            System.out.println("A must have the same amount of columns as B has rows.");
            return null;
        }

        Matrix result = new Matrix(A.rows, B.cols);

        for (int i = 0; i < result.rows; i++) {
            for (int j = 0; j < result.cols; j++) {

                double sum = 0;

                for (int k = 0; k < A.cols; k++) {
                    sum += A.data[i][k] * B.data[k][j];
                }
                result.data[i][j] = sum;
            }
        }

        return result;
    }

    public void map(Function<Double, Double> f) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                double x = data[i][j];
                data[i][j] = f.apply(x);
            }
        }
    }

    static Matrix map(Matrix m, Function<Double, Double> f) {
        m.map(f);
        return m;
    }

    public void multiply(double n) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                data[i][j] *= n;
            }
        }
    }

    public void add(Matrix m) {
        if (m.rows != rows || m.cols != cols) {
            System.out.println("Matrices must have same size");
            return;
        }
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                data[i][j] += m.data[i][j];
            }
        }
    }

    public Matrix copy() {
        Matrix matrix = new Matrix(rows, cols);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                matrix.data[i][j] = data[i][j];
            }
        }
        return matrix;
    }

    public void print() {
        for (double[] row : data) {
            System.out.println(Arrays.toString(row));
        }
    }

}