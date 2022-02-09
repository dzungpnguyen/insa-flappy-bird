package NeuralNetwork;

import Manager.*;
import NEAT.*;

import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Robot;
import java.awt.AWTException;
import java.lang.SecurityException;
import java.io.*;

public class NeuralNetwork {

    public static enum Type {
        NEURO_EVOLUTION, FULLY_CONNECTED_NEURAL_NETWORK;
    }
    
    
    public int id;
    public Type type;
    public Neuron neuronBias;
    public ArrayList<Neuron> listNeuronInput;
    public ArrayList<Neuron> listNeuronOutput;
    public ArrayList<Neuron> listNeuronHidden;
    public ArrayList<Connection> listConnection;

    private static double[][] input;
    private static double[][] outputExpect;
    public static int epoch = 0;

    private double compatibilityDistance;
    private FitnessFunction fitnessFunction;
    private double fitness;
    
    private boolean isLooped = false;

    public NeuralNetwork(Type type, int[] structure) {
        this.type = type;
        
        switch (type) {
            case FULLY_CONNECTED_NEURAL_NETWORK:
                this.neuronBias = new Neuron("Bias", idDistributor.getIDNeuron(), null, null);

                this.listNeuronInput = new ArrayList<>();
                for (int i = 0; i < structure[0]; i++) {
                    this.listNeuronInput.add(new Neuron("Input", idDistributor.getIDNeuron(), null, null));
                }

                this.listNeuronOutput = new ArrayList<>();
                for (int i = 0; i < structure[structure.length - 1]; i++) {
                    this.listNeuronOutput.add(new Neuron("Output", idDistributor.getIDNeuron(), Neuron.ActivateFunction.SIGMOID, Neuron.LossFunction.BINARY_CROSSENTROPY));
                }

                this.listConnection = new ArrayList<>();
                this.listNeuronHidden = new ArrayList<>();

                if (structure.length > 2) {

                    ArrayList<Neuron>[] listNeuronHidden = new ArrayList[structure.length];
                    for (int i = 1; i < structure.length - 1; i++) {
                        listNeuronHidden[i] = new ArrayList<>();
                        for (int j = 0; j < structure[i]; j++) {
                            Neuron neuron = new Neuron("Hidden", idDistributor.getIDNeuron(), Neuron.ActivateFunction.SIGMOID, null);
                            listNeuronHidden[i].add(neuron);
                            this.listNeuronHidden.add(neuron);
                        }

                        if (i == 1) {
                            for (Neuron fromNeuron : this.listNeuronInput) {
                                for (Neuron toNeuron : listNeuronHidden[i]) {
                                    this.listConnection.add(new Connection(fromNeuron, toNeuron, idDistributor.getIDConnection()));
                                }
                            }

                            for (Neuron toNeuron : listNeuronHidden[i]) {
                                this.listConnection.add(new Connection(this.neuronBias, toNeuron, idDistributor.getIDConnection()));
                            }
                        } else {
                            for (Neuron fromNeuron : listNeuronHidden[i - 1]) {
                                for (Neuron toNeuron : listNeuronHidden[i]) {
                                    this.listConnection.add(new Connection(fromNeuron, toNeuron, idDistributor.getIDConnection()));
                                }
                            }

                            for (Neuron toNeuron : listNeuronHidden[i]) {
                                this.listConnection.add(new Connection(this.neuronBias, toNeuron, idDistributor.getIDConnection()));
                            }
                        }

                        if (i == structure.length - 2) {
                            for (Neuron fromNeuron : listNeuronHidden[i]) {
                                for (Neuron toNeuron : listNeuronOutput) {
                                    this.listConnection.add(new Connection(fromNeuron, toNeuron, idDistributor.getIDConnection()));
                                }
                            }
                        }
                    }
                } else {
                    for (Neuron neuronOutput : this.listNeuronOutput) {
                        this.listConnection.add(new Connection(neuronBias, neuronOutput, idDistributor.getIDConnection()));
                    }

                    for (Neuron neuronInput : this.listNeuronInput) {
                        for (Neuron neuronOutput : this.listNeuronOutput) {
                            this.listConnection.add(new Connection(neuronInput, neuronOutput, idDistributor.getIDConnection()));
                        }
                    }
                }

                break;

            case NEURO_EVOLUTION:

                ArrayList<Neuron> listNeuronBase = NeuronManager.createNeuronBase(structure);

                this.neuronBias = listNeuronBase.get(0);

                this.listNeuronInput = new ArrayList<>();
                for (int i = 0; i < structure[0]; i++) {
                    this.listNeuronInput.add(listNeuronBase.get(i + 1));
                }

                this.listNeuronOutput = new ArrayList<>();
                for (int i = 0; i < structure[structure.length - 1]; i++) {
                    this.listNeuronOutput.add(listNeuronBase.get(i + 1 + this.listNeuronInput.size()));
                }

                this.listConnection = new ArrayList<>();
                for (Neuron neuronOutput : this.listNeuronOutput) {
                    this.listConnection.add(ConnectionManager.createConnection(neuronBias, neuronOutput));
                }

                for (Neuron neuronInput : this.listNeuronInput) {
                    for (Neuron neuronOutput : this.listNeuronOutput) {
                        this.listConnection.add(ConnectionManager.createConnection(neuronInput, neuronOutput));
                    }
                }

                this.listNeuronHidden = new ArrayList<>();

                break;
        }
    }

    public NeuralNetwork(ArrayList<Connection> listConnection) {
        
        this.type = Type.NEURO_EVOLUTION;
        
        this.listConnection = new ArrayList<>();
        this.listNeuronInput = new ArrayList<>();
        this.listNeuronOutput = new ArrayList<>();
        this.listNeuronHidden = new ArrayList<>();

        for (Connection connection : listConnection) {
            Neuron fromNeuron = connection.getFromNeuron();

            boolean b;
            switch (fromNeuron.getType()) {
                case "Bias":
                    if (this.neuronBias == null) {
                        this.neuronBias = fromNeuron;
                    }
                    break;

                case "Input":
                    b = true;
                    for (Neuron neuron : this.listNeuronInput) {
                        if (neuron.equals(fromNeuron)) {
                            b = false;
                            break;
                        }
                    }
                    if (b) {
                        this.listNeuronInput.add(fromNeuron);
                    }

                    break;

                case "Hidden":
                    b = true;
                    for (Neuron neuron : this.listNeuronHidden) {
                        if (neuron.equals(fromNeuron)) {
                            b = false;
                            break;
                        }
                    }
                    if (b) {
                        this.listNeuronHidden.add(fromNeuron);
                    }
                    break;
            }
        }

        for (Connection connection : listConnection) {
            Neuron toNeuron = connection.getToNeuron();

            boolean b;
            switch (toNeuron.getType()) {
                case "Output":
                    b = true;
                    for (Neuron neuron : this.listNeuronOutput) {
                        if (neuron.equals(toNeuron)) {
                            b = false;
                            break;
                        }
                    }
                    if (b) {
                        this.listNeuronOutput.add(toNeuron);
                    }
                    break;

                case "Hidden":
                    b = true;
                    for (Neuron neuron : this.listNeuronHidden) {
                        if (neuron.equals(toNeuron)) {
                            b = false;
                            break;
                        }
                    }
                    if (b) {
                        this.listNeuronHidden.add(toNeuron);
                    }
                    break;
            }
        }

        for (Neuron neuronHidden : this.listNeuronHidden) {
            for (Connection connection : listConnection) {
                if (connection.contains(this.neuronBias, neuronHidden)) {
                    this.listConnection.add(connection.clone(this.neuronBias, neuronHidden));
                }
            }
        }

        for (Neuron neuronOutput : this.listNeuronOutput) {
            for (Connection connection : listConnection) {
                if (connection.contains(this.neuronBias, neuronOutput)) {
                    this.listConnection.add(connection.clone(this.neuronBias, neuronOutput));
                }
            }
        }

        for (Neuron neuronInput : this.listNeuronInput) {
            for (Neuron neuronOutput : this.listNeuronOutput) {
                for (Connection connection : listConnection) {
                    if (connection.contains(neuronInput, neuronOutput)) {
                        this.listConnection.add(connection.clone(neuronInput, neuronOutput));
                    }
                }
            }
        }

        for (Neuron neuronInput : this.listNeuronInput) {
            for (Neuron neuronHidden : this.listNeuronHidden) {
                for (Connection connection : listConnection) {
                    if (connection.contains(neuronInput, neuronHidden)) {
                        this.listConnection.add(connection.clone(neuronInput, neuronHidden));
                    }
                }
            }
        }

        for (Neuron neuronHidden : this.listNeuronHidden) {
            for (Neuron neuronOutput : this.listNeuronOutput) {
                for (Connection connection : listConnection) {
                    if (connection.contains(neuronHidden, neuronOutput)) {
                        this.listConnection.add(connection.clone(neuronHidden, neuronOutput));
                    }
                }
            }
        }

        for (Neuron neuronHidden1 : this.listNeuronHidden) {
            for (Neuron neuronHidden2 : this.listNeuronHidden) {
                if (!neuronHidden1.equals(neuronHidden2)) {
                    for (Connection connection : listConnection) {
                        if (connection.contains(neuronHidden1, neuronHidden2)) {
                            this.listConnection.add(connection.clone(neuronHidden1, neuronHidden2));
                        }
                    }
                }
            }
        }

    }

    public ArrayList<Connection> getListConnection() {
        return this.listConnection;
    }

    public static double[][] getInput() {
        return input;
    }

    public static double[][] getOutputExpect() {
        return outputExpect;
    }
    
    public void setID(int id){
        this.id = id;
    }
    
    public int getID(){
        return this.id;
    }

    public double getCompatibilityDistance(NeuralNetwork neuralNetwork) {
        return CompatibilityDistance.getCompatibilityDistance(this, neuralNetwork);
    }
    
    public void calculFitness(){
        this.fitness = fitnessFunction.fitness();
    }
    
    public double getFitness() {
        return this.fitness;
    }

    public static void setInput(double[][] input_) {
        input = input_;
    }

    public static void setOutputExpect(double[][] outputExpect_) {
        outputExpect = outputExpect_;
    }

    public void setFitnessFunction(FitnessFunction fitnessFunction) {
        this.fitnessFunction = fitnessFunction;
    }
    
    public void setFitness(double fitness){
        this.fitness = fitness;
    }

    public void feedForward(double[] input) {
        for (int i = 0; i < this.listNeuronInput.size(); i++) {
            this.listNeuronInput.get(i).setValue(input[i]);
        }

        ArrayList<Neuron> listNeuronBegin = (ArrayList<Neuron>) this.listNeuronInput.clone();
        listNeuronBegin.add(this.neuronBias);

        ArrayList<Neuron> listToNeuron = getListToNeuron(listNeuronBegin);
        do {
            for (Neuron toNeuron : listToNeuron) {
                double sum = 0;
                for (Neuron fromNeuron : toNeuron.getListFromNeuron()) {
                    for (Connection connection : toNeuron.getListConnection()) {
                        if (connection.contains(fromNeuron, toNeuron) && connection.isEnabled()) {
                            sum += fromNeuron.activate() * connection.getWeight();
                        }
                    }
                }
                toNeuron.setValue(sum);
            }

            listToNeuron = getListToNeuron(listToNeuron);

        } while (listToNeuron.size() != 0 && !isLooped);
    }

    public double loss() {
        double loss = 0;
        for (int i = 0; i < this.input.length; i++) {
            feedForward(this.input[i]);
            for (int j = 0; j < this.listNeuronOutput.size(); j++) {
                loss += this.listNeuronOutput.get(j).loss(this.outputExpect[i][j]);

            }
        }
        return loss;
    }

    public void backPropagation() {

        for (int i = 0; i < this.input.length; i++) {
            feedForward(this.input[i]);

            for (int j = 0; j < this.listNeuronOutput.size(); j++) {
                Neuron neuron = this.listNeuronOutput.get(j);
                double derivation = neuron.derive(this.outputExpect[i][j]) * neuron.derive();
                neuron.addDerivation(derivation);
            }

            ArrayList<Neuron> listFromNeuron = getListFromNeuron(this.listNeuronOutput);
            do {
                for (Neuron fromNeuron : listFromNeuron) {
                    double sum = 0;
                    for (Neuron toNeuron : fromNeuron.getListToNeuron()) {
                        for (Connection connection : fromNeuron.getListConnection()) {
                            if (connection.contains(fromNeuron, toNeuron) && connection.isEnabled()) {
                                sum += toNeuron.getDerivation(i) * connection.getWeight() * fromNeuron.derive();
                                connection.addDerivation(toNeuron.getDerivation(i) * fromNeuron.activate());
                            }
                        }
                    }

                    fromNeuron.addDerivation(sum);

                }
                listFromNeuron = getListFromNeuron(listFromNeuron);
            } while (listFromNeuron.size() != 0 && !isLooped);
        }

    }

    public void train(double learning_rate, double lossMax, double differenceMax) {
        double loss1 = 0;
        double loss2 = 0;

        do {
            loss1 = loss2;
            loss2 = loss();
            if (loss1 == 0) {
                loss1 = loss2 + 1;
            }

            backPropagation();

            for (Neuron neuron : this.listNeuronInput) {
                neuron.resetDerivation();
            }

            for (Neuron neuron : this.listNeuronHidden) {
                neuron.resetDerivation();
            }

            for (Neuron neuron : this.listNeuronOutput) {
                neuron.resetDerivation();
            }

            for (Connection connection : this.listConnection) {
                if (connection.isEnabled()) {
                    connection.setWeight(connection.getWeight() - learning_rate * connection.getDerivation());
                    connection.resetDerivation();
                }
            }

            this.epoch++;
            System.out.println(loss1 + " " + loss2 + " " + this.epoch);

        } while ((loss2 > lossMax) && (Math.abs(loss2 - loss1) > differenceMax) && (loss2 < loss1));
    }

    public double[] predict(double[] input) {
        final long startTime = System.currentTimeMillis();
        final boolean[] isFinished = {false};
        
        Thread timeThread = new Thread(){
            @Override
            public void run(){
                while (!isInterrupted()){
                    if (System.currentTimeMillis() - startTime > 3000){
                        isLooped = true;
                        isFinished[0] = true;
                        break;
                    }
                }
            }
        };
        
        Thread feedForwardThread = new Thread(){
            @Override 
            public void run(){
                feedForward(input);
                timeThread.interrupt();
                isFinished[0] = true;
            }
        };
        
        timeThread.start();
        feedForwardThread.start();
        
        while (!isFinished[0]){
            System.out.print("");
        }
        
        if (!isLooped){
            double[] prediction = new double[this.listNeuronOutput.size()];
            for (int i = 0; i < this.listNeuronOutput.size(); i++) {
                prediction[i] = this.listNeuronOutput.get(i).activate();
            }
            return prediction;
        }else{
            return null;
        }
    }

    public void mutate(double ratePerturbWeight, double rateAddNeuron, double rateAddConnection) {
        for (Connection connection : this.listConnection) {
            if (connection.isEnabled()) {
                double random = Math.random();
                if (random < ratePerturbWeight) {
                    Mutate.perturbWeight(connection);
                }
            }
        }

        ArrayList<Connection> listConnectionClone = (ArrayList<Connection>) this.listConnection.clone();
        for (Connection connection : this.listConnection) {
            if (connection.isEnabled() && !connection.getFromNeuron().equals(this.neuronBias)) {
                double random = Math.random();
                if (random < rateAddNeuron) {
                    Mutate.addNeuron(listConnectionClone, this.listNeuronHidden, connection);
                }
            }
        }
        this.listConnection = listConnectionClone;

        for (Neuron neuronHidden : this.listNeuronHidden) {
            if (!this.neuronBias.getListToNeuron().contains(neuronHidden)) {
                double random = Math.random();
                if (random < rateAddConnection) {
                    Mutate.addConnection(this.listConnection, this.neuronBias, neuronHidden);
                }
            } else {
                for (Connection connection : this.neuronBias.getListConnection()) {
                    if (connection.contains(this.neuronBias, neuronHidden) && (!connection.isEnabled())) {
                        connection.enable();
                    }
                }
            }
        }

        for (Neuron neuronInput : this.listNeuronInput) {
            for (Neuron neuronHidden : this.listNeuronHidden) {
                if (!neuronInput.getListToNeuron().contains(neuronHidden)) {
                    double random = Math.random();
                    if (random < rateAddConnection) {
                        Mutate.addConnection(this.listConnection, neuronInput, neuronHidden);
                    }
                } else {
                    for (Connection connection : neuronInput.getListConnection()) {
                        if (connection.contains(neuronInput, neuronHidden) && (!connection.isEnabled())) {
                            connection.enable();
                        }
                    }
                }
            }
        }

        for (Neuron neuronOutput : this.listNeuronOutput) {
            for (Neuron neuronHidden : this.listNeuronHidden) {
                if (!neuronOutput.getListFromNeuron().contains(neuronHidden)) {
                    double random = Math.random();
                    if (random < rateAddConnection) {
                        Mutate.addConnection(this.listConnection, neuronHidden, neuronOutput);
                    }
                } else {
                    for (Connection connection : neuronOutput.getListConnection()) {
                        if (connection.contains(neuronHidden, neuronOutput) && (!connection.isEnabled())) {
                            connection.enable();
                        }
                    }
                }
            }
        }

        for (Neuron fromNeuronHidden : this.listNeuronHidden) {
            for (Neuron toNeuronHidden : this.listNeuronHidden) {
                if (!fromNeuronHidden.equals(toNeuronHidden)) {
                    if (!fromNeuronHidden.getListFromNeuron().contains(toNeuronHidden)) {
                        if (!fromNeuronHidden.getListToNeuron().contains(toNeuronHidden)) {
                            double random = Math.random();
                            if (random < rateAddConnection) {
                                Mutate.addConnection(this.listConnection, fromNeuronHidden, toNeuronHidden);
                            }
                        } else {
                            for (Connection connection : fromNeuronHidden.getListConnection()) {
                                if (connection.contains(fromNeuronHidden, toNeuronHidden) && (!connection.isEnabled())) {
                                    connection.enable();
                                }
                            }
                        }
                    }
                }
            }
        }

    }

    public ArrayList<Neuron> getListToNeuron(ArrayList<Neuron> listNeuron) {
        ArrayList<Neuron> listToNeuron = new ArrayList<>();
        for (Neuron neuron : listNeuron) {
            for (Connection connection : neuron.getListConnection()) {
                if (!listToNeuron.contains(connection.getToNeuron()) && neuron.equals(connection.getFromNeuron()) && connection.isEnabled()) {
                    listToNeuron.add(connection.getToNeuron());
                }
            }
        }
        return listToNeuron;
    }

    private ArrayList<Neuron> getListFromNeuron(ArrayList<Neuron> listNeuron) {
        ArrayList<Neuron> listFromNeuron = new ArrayList<>();
        for (Neuron neuron : listNeuron) {
            for (Connection connection : neuron.getListConnection()) {
                if (!listFromNeuron.contains(connection.getFromNeuron()) && neuron.equals(connection.getToNeuron()) && connection.isEnabled()) {
                    listFromNeuron.add(connection.getFromNeuron());
                }
            }
        }
        return listFromNeuron;
    }

    public String toString() {
        String s = "";
        for (Connection connection : this.listConnection) {
            //s += connection.getID() + " " + connection.getWeight() + "\n";
            s += connection.toString() + "\n";
        }
        s += "fitness: " + this.getFitness() + "\n";
        return s;
    }

    public void print() {
        System.out.println(this.toString());
    }

    public void toFrame() {
        final Neuron neuronBias = this.neuronBias;
        final ArrayList<Neuron> listNeuronInput = this.listNeuronInput;
        final ArrayList<Neuron> listNeuronOutput = this.listNeuronOutput;
        final ArrayList<Neuron> listNeuronHidden = this.listNeuronHidden;
        final ArrayList<Connection> listConnection = this.listConnection;

        JFrame frame = new JFrame();
        frame.setSize(1200, 600);
        JPanel panel = new JPanel() {
            public void paint(Graphics g) {
                g.setColor(Color.BLACK);
                int[][] map = new int[1200][600];

                g.drawOval(0, 0, 30, 30);
                g.drawString(neuronBias.getID() + "", 0 + 15, 0 + 15);
                map[0][0] = neuronBias.getID();

                int d = (600 - 15 * listNeuronInput.size()) / (listNeuronInput.size() + 1);
                for (int i = 0; i < listNeuronInput.size(); i++) {
                    g.drawOval(0, d * i + d, 30, 30);
                    g.drawString(listNeuronInput.get(i).getID() + "", 0 + 15, d * i + d + 15);
                    map[0][d * i + d] = listNeuronInput.get(i).getID();
                }

                d = (600 - 15 * listNeuronOutput.size()) / (listNeuronOutput.size() + 1);
                for (int i = 0; i < listNeuronOutput.size(); i++) {
                    g.drawOval(1200 - 45, d * i + d, 30, 30);
                    g.drawString(listNeuronOutput.get(i).getID() + "", 1200 - 45 + 15, d * i + d + 15);
                    map[1200 - 45][d * i + d] = listNeuronOutput.get(i).getID();
                }

                for (int i = 0; i < listNeuronHidden.size(); i++) {
                    int x, y;
                    do {
                        x = (int) (Math.random() * 1100);
                        y = (int) (Math.random() * 500);
                    } while (!canDraw(x, y));
                    g.drawOval(x, y, 30, 30);
                    g.drawString(listNeuronHidden.get(i).getID() + "", x + 15, y + 15);
                    map[x][y] = listNeuronHidden.get(i).getID();
                }

                for (int i = 0; i < listConnection.size(); i++) {
                    if (listConnection.get(i).isEnabled()) {
                        Neuron fromNeuron = listConnection.get(i).getFromNeuron();
                        Neuron toNeuron = listConnection.get(i).getToNeuron();
                        int x1 = 0, y1 = 0, x2 = 0, y2 = 0;
                        for (int j = 0; j < map.length; j++) {
                            for (int k = 0; k < map[0].length; k++) {
                                if (map[j][k] == fromNeuron.getID()) {
                                    x1 = j;
                                    y1 = k;
                                }

                                if (map[j][k] == toNeuron.getID()) {
                                    x2 = j;
                                    y2 = k;
                                }
                            }
                        }
                        g.drawLine(x1 + 15, y1 + 15, x2 + 15, y2 + 15);
                        g.drawString(String.format("%.2f", listConnection.get(i).getWeight()), (int) (0.75 * x1 + 0.25 * x2) + 15, (int) (0.75 * y1 + 0.25 * y2) + 15);
                        g.drawString("  " + listConnection.get(i).getID(), (int) (0.75 * x1 + 0.25 * x2) + 15, (int) (0.75 * y1 + 0.25 * y2) + 15 + 15);
                    }
                }
            }

            private boolean canDraw(int x, int y) {
                try {
                    int r = 60;
                    for (int i = x - r; i <= x + r; i += 30) {
                        for (int j = y - r; j <= y + r; j += 30) {
                            Color color = new Robot().getPixelColor(i, j);
                            if (color == Color.BLACK) {
                                return false;
                            }
                        }
                    }
                } catch (AWTException e) {
                    e.printStackTrace();
                } catch (SecurityException e) {
                    e.printStackTrace();
                }
                return true;
            }
        };
        panel.setBounds(0, 0, 1200, 600);
        panel.setBackground(Color.WHITE);
        frame.add(panel);
        frame.setVisible(true);
    }
    
    
    public void save(String fileName) throws IOException {
        FileWriter fileWriter = new FileWriter(fileName + ".txt");
        fileWriter.append(this.listNeuronInput.size() + ";");
        fileWriter.append(this.listNeuronHidden.size() + ";");
        fileWriter.append(this.listNeuronOutput.size() + ";");
        fileWriter.append(this.listConnection.size() + ";\n");
        fileWriter.append(this.type + "\n");
        fileWriter.append(this.id + "\n");
        fileWriter.append(this.epoch + "\n");
        fileWriter.append(this.fitness + "\n");
        fileWriter.flush();
        fileWriter.close();
        
        this.neuronBias.save(fileName + "_NeuronBias");
        
        for (int i = 0; i < this.listNeuronInput.size(); i++){
            this.listNeuronInput.get(i).save(fileName + "_NeuronInput_" + i);
        }
        
        for (int i = 0; i < this.listNeuronHidden.size(); i++){
            this.listNeuronHidden.get(i).save(fileName + "_NeuronHidden_" + i);
        }
        
        for (int i = 0; i < this.listNeuronOutput.size(); i++){
            this.listNeuronOutput.get(i).save(fileName + "_NeuronOutput_" + i);
        }
        
        for (int i = 0; i < this.listConnection.size(); i++){
            this.listConnection.get(i).save(fileName + "_Connection_" + i);
        }

    }

    public static NeuralNetwork read(String fileName) throws IOException {
        FileReader fileReader = new FileReader(fileName + ".txt");
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String[] info = bufferedReader.readLine().split(";");
        
        ArrayList<Neuron> listNeuron = new ArrayList<>();
        listNeuron.add(Neuron.read(fileName + "_NeuronBias"));
        for (int i = 0; i < Integer.valueOf(info[0]); i++){
            listNeuron.add(Neuron.read(fileName + "_NeuronInput_" + i));
        }
        
        for (int i = 0; i < Integer.valueOf(info[1]); i++){
            listNeuron.add(Neuron.read(fileName + "_NeuronHidden_" + i));
        }
        
        for (int i = 0; i < Integer.valueOf(info[2]); i++){
            listNeuron.add(Neuron.read(fileName + "_NeuronOutput_" + i));
        }
        
        ArrayList<Connection> listConnection = new ArrayList<>();
        for (int i = 0; i < Integer.valueOf(info[3]); i++){
            listConnection.add(Connection.read(fileName + "_Connection_" + i, listNeuron));
        }
        
        return new NeuralNetwork(listConnection);
    }
}
