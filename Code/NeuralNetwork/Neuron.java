package NeuralNetwork;

import java.util.ArrayList;
import java.io.*;

public class Neuron {

    public static enum ActivateFunction {
        IDENTITY, SIGMOID, SOFTMAX;

        public static ActivateFunction getActivateFunction(int pos) {
            for (ActivateFunction activateFunction : ActivateFunction.values()) {
                if (activateFunction.ordinal() == pos) {
                    return activateFunction;
                }
            }
            return null;
        }

        public static int getSize() {
            int count = 0;
            for (ActivateFunction activateFunction : ActivateFunction.values()) {
                count++;
            }
            return count;
        }

        public static ActivateFunction random() {
            int pos = (int) (Math.random() * getSize());
            return getActivateFunction(pos);
        }

        public static double activate(ActivateFunction activateFunction, double input) {
            switch (activateFunction) {
                case IDENTITY:
                    return input;

                case SIGMOID:
                    return (double) (1 / (1 + Math.exp(-input)));

                default:
                    return input;
            }
        }

        public static double derive(ActivateFunction activateFunction, double outputReal) {
            switch (activateFunction) {
                case IDENTITY:
                    return 1;

                case SIGMOID:
                    return outputReal * (1 - outputReal);

                default:
                    return 1;
            }
        }

        //~ public static double[] getDerivation(String code, double[] array){
        //~ double[] returnArray;
        //~ TYPE type = TYPE.getType(code);
        //~ switch(type){
        //~ case NULL:
        //~ return array;
        //~ case SIGMOID:
        //~ returnArray = new double[array.length];
        //~ for (int i = 0; i < returnArray.length; i++){
        //~ returnArray[i] = (double)(1 / (1 + Math.exp(-array[i])));
        //~ }
        //~ return returnArray;
        //~ case SOFTMAX:
        //~ returnArray = new double[array.length];
        //~ double sum = 0;
        //~ for (int i = 0; i < returnArray.length; i++){
        //~ sum += Math.exp(array[i]);
        //~ }
        //~ for (int i = 0; i < returnArray.length; i++){
        //~ returnArray[i] = (double)(Math.exp(array[i]) / sum);
        //~ }
        //~ return returnArray;
        //~ default:
        //~ return array;
        //~ }
        //~ }
    }

    public static enum LossFunction {
        BINARY_CROSSENTROPY, CATEGORICAL_CROSSENTROPY, MEAN_SQUARED_ERROR;
        
        public static LossFunction getLossFunction(int pos){
            for (LossFunction lossFunction : LossFunction.values()) {
                if (lossFunction.ordinal() == pos) {
                    return lossFunction;
                }
            }
            return null;
        }
        
        public static double loss(LossFunction lossFunction, double outputExpect, double outputReal) {
            switch (lossFunction) {
                case BINARY_CROSSENTROPY:
                    return -(outputExpect * Math.log(outputReal) + (1 - outputExpect) * Math.log((1 - outputReal)));

                case MEAN_SQUARED_ERROR:
                    return 0.5 * Math.pow((outputReal - outputExpect), 2);

                default:
                    return Math.pow((outputReal - outputExpect), 2);
            }
        }

        public static double derive(LossFunction lossFunction, double outputExpect, double outputReal) {
            switch (lossFunction) {
                case BINARY_CROSSENTROPY:
                    return -(outputExpect / outputReal - (1 - outputExpect) / (1 - outputReal));

                case MEAN_SQUARED_ERROR:
                    return (outputReal - outputExpect);

                default:
                    return (outputReal - outputExpect);
            }
        }
    }

    private final int id;
    private String type;
    protected double value;
    protected ActivateFunction activateFunction;
    protected LossFunction lossFunction;

    private ArrayList<Double> derivation;

    private ArrayList<Neuron> listFromNeuron;
    private ArrayList<Neuron> listToNeuron;
    private ArrayList<Connection> listConnection;

    public Neuron(String type, int id, ActivateFunction activateFunction, LossFunction lossFunction) {
        this.type = type;
        this.id = id;

        this.resetDerivation();
        this.resetList();

        switch (this.type) {
            case "Bias":
                this.value = 1;
                this.activateFunction = ActivateFunction.IDENTITY;
                this.lossFunction = null;
                break;

            case "Input":
                this.activateFunction = ActivateFunction.IDENTITY;
                this.lossFunction = null;
                break;

            case "Hidden":
                if (activateFunction == null) {
                    this.activateFunction = ActivateFunction.random();
                } else {
                    this.activateFunction = activateFunction;
                }
                this.lossFunction = null;
                break;

            case "Output":
                this.activateFunction = activateFunction;
                this.lossFunction = lossFunction;

        }
    }

    public int getID() {
        return this.id;
    }

    public String getType() {
        return this.type;
    }

    public double getValue() {
        return this.value;
    }

    public ActivateFunction getActivateFunction() {
        return this.activateFunction;
    }

    public LossFunction getLossFunction() {
        return this.lossFunction;
    }

    public double getDerivation(int pos) {
        return this.derivation.get(pos);
    }

    public double getDerivation() {
        double sum = 0;
        for (Double d : this.derivation) {
            sum += d;
        }
        return sum;
    }

    public ArrayList<Neuron> getListFromNeuron() {
        return this.listFromNeuron;
    }

    public ArrayList<Neuron> getListToNeuron() {
        return this.listToNeuron;
    }

    public ArrayList<Connection> getListConnection() {
        return this.listConnection;
    }

    public void resetList() {
        this.listFromNeuron = new ArrayList<>();
        this.listToNeuron = new ArrayList<>();
        this.listConnection = new ArrayList<>();
    }

    public void setValue(double value) {
        if (!this.type.equals("Bias")) {
            this.value = value;
        }
    }

    public void addDerivation(double derivation) {
        this.derivation.add(derivation);
    }

    public void resetDerivation() {
        this.derivation = new ArrayList<>();
    }

    public void add(Neuron neuron, String listType) {
        if (listType.equals("f")) {
            this.listFromNeuron.add(neuron);
        }
        if (listType.equals("t")) {
            this.listToNeuron.add(neuron);
        }
    }

    public void add(Connection connection) {
        this.listConnection.add(connection);
    }

    public double activate() {
        return this.activateFunction.activate(this.activateFunction, this.value);
    }

    public double derive() {
        return ActivateFunction.derive(this.activateFunction, this.activate());
    }

    public double loss(double outputExpect) {
        return LossFunction.loss(this.lossFunction, outputExpect, this.activate());
    }

    public double derive(double outputExpect) {
        return LossFunction.derive(this.lossFunction, outputExpect, this.activate());
    }

    public Neuron clone() {
        Neuron neuronClone = new Neuron(this.type, this.id, this.activateFunction, this.lossFunction);
        neuronClone.value = this.value;
        return neuronClone;
    }

    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }

        if (!(o instanceof Neuron)) {
            return false;
        }

        Neuron neuron = (Neuron) o;

        return this.id == neuron.id;
    }

    public String toString() {
        String s = "\n";
        s += "Neuron ID: " + this.id;
        s += " Type: " + this.type;
        s += " Activation Function: " + this.activateFunction;
        s += " Loss Function: " + this.lossFunction + "\n";
        return s;
    }

    public void print() {
        System.out.println(this.toString());
    }

    public void save(String fileName) throws IOException {
        FileWriter fileWriter = new FileWriter(fileName + ".txt");
        fileWriter.append(this.type + ";");
        fileWriter.append(this.id + ";");
        fileWriter.append(this.activateFunction.ordinal() + ";");
        fileWriter.append((this.lossFunction == null ? "null" : this.lossFunction.ordinal()) + ";");
        fileWriter.flush();
        fileWriter.close();

    }

    public static Neuron read(String fileName) throws IOException {
        FileReader fileReader = new FileReader(fileName + ".txt");
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String[] info = bufferedReader.readLine().split(";");
        if (info[3].contains("null")){
            return new Neuron(info[0], Integer.valueOf(info[1]), ActivateFunction.getActivateFunction(Integer.valueOf(info[2])), null);
        }else{
            return new Neuron(info[0], Integer.valueOf(info[1]), ActivateFunction.getActivateFunction(Integer.valueOf(info[2])), LossFunction.getLossFunction(Integer.valueOf(info[3])));
        }
    }

}
