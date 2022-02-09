package NeuralNetwork;

import java.util.ArrayList;
import java.io.*;

public class Connection {

    private Neuron fromNeuron;
    private Neuron toNeuron;
    private final int id;
    private double weight;
    private ArrayList<Double> derivation;
    private boolean isEnabled;

    public Connection(Neuron fromNeuron, Neuron toNeuron, int id) {
        this.fromNeuron = fromNeuron;
        this.toNeuron = toNeuron;
        this.id = id;
        this.weight = Math.random() * 2 - 1;
        this.isEnabled = true;
        this.resetDerivation();

        this.fromNeuron.add(this.toNeuron, "t");
        this.fromNeuron.add(this);
        this.toNeuron.add(this.fromNeuron, "f");
        this.toNeuron.add(this);
    }

    public double getWeight() {
        return this.weight;
    }

    public Neuron getFromNeuron() {
        return this.fromNeuron;
    }

    public Neuron getToNeuron() {
        return this.toNeuron;
    }

    public int getID() {
        return this.id;
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

    public void setFromNeuron(Neuron fromNeuron) {
        this.fromNeuron = fromNeuron;
    }

    public void setToNeuron(Neuron toNeuron) {
        this.toNeuron = toNeuron;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public void setDerivation(double derivation, int pos) {
        this.derivation.set(pos, derivation);
    }

    public void addDerivation(double derivation) {
        this.derivation.add(derivation);
    }

    public void resetDerivation() {
        this.derivation = new ArrayList<>();
    }

    public void enable() {
        this.isEnabled = true;
    }

    public void disable() {
        this.isEnabled = false;
    }

    public boolean isEnabled() {
        return this.isEnabled;
    }

    public boolean contains(Neuron fromNeuron, Neuron toNeuron) {
        if (this.fromNeuron.equals(fromNeuron) && this.toNeuron.equals(toNeuron)) {
            return true;
        }
        return false;
    }

    public Connection clone(Neuron fromNeuron, Neuron toNeuron) {
        Connection connection = new Connection(fromNeuron, toNeuron, this.id);
        connection.setWeight(this.weight);
        if (this.isEnabled()) {
            connection.enable();
        } else {
            connection.disable();
        }
        return connection;
    }

    //use for reproduce offspring
    public Connection clone() {
        Neuron fromNeuron = this.fromNeuron.clone();
        Neuron toNeuron = this.toNeuron.clone();

        Connection connection = new Connection(fromNeuron, toNeuron, this.id);
        connection.setWeight(this.weight);
        if (this.isEnabled()) {
            connection.enable();
        } else {
            connection.disable();
        }

        fromNeuron.resetList();
        toNeuron.resetList();

        return connection;
    }

    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }

        if (!(o instanceof Connection)) {
            return false;
        }

        Connection connection = (Connection) o;

        return this.id == connection.id;
    }

    public String toString() {
        String s = "";
        s += "Connection ID: " + this.id;
        s += " State: " + this.isEnabled;
        s += " Weight: " + this.weight;
        s += "\nfromNeuron: " + this.fromNeuron.toString();
        s += "toNeuron: " + this.toNeuron.toString();
        return s;
    }

    public void print() {
        System.out.println(this.toString());
    }
    
    public void save(String fileName) throws IOException {
        FileWriter fileWriter = new FileWriter(fileName + ".txt");
        fileWriter.append(this.id + ";");
        fileWriter.append(this.weight + ";");
        fileWriter.append(this.isEnabled + ";");
        fileWriter.append(this.fromNeuron.getID() + ";");
        fileWriter.append(this.toNeuron.getID() + ";");
        fileWriter.flush();
        fileWriter.close();
    }

    public static Connection read(String fileName, ArrayList<Neuron> listNeuron) throws IOException {
        FileReader fileReader = new FileReader(fileName + ".txt");
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String[] info = bufferedReader.readLine().split(";");
        
        Neuron fromNeuron = null;
        Neuron toNeuron = null;
                
        for (Neuron neuron: listNeuron){
            if (neuron.getID() == Integer.valueOf(info[3])){
                fromNeuron = neuron;
            }
            
            if (neuron.getID() == Integer.valueOf(info[4])){
                toNeuron = neuron;
            }
        }
        
        Connection connection = new Connection(fromNeuron, toNeuron, Integer.valueOf(info[0]));
        connection.setWeight(Double.valueOf(info[1]));
        if (Boolean.valueOf(info[2])){
            connection.enable();
        }else{
            connection.disable();
        }
        
        return connection.clone();
    }

}
