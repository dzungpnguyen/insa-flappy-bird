package NEAT;

import NeuralNetwork.*;
import Manager.*;

import java.util.ArrayList;

public class Mutate {

    public static void addConnection(ArrayList<Connection> listConnection, Neuron fromNeuron, Neuron toNeuron) {
        listConnection.add(ConnectionManager.createConnection(fromNeuron, toNeuron));
    }

    public static void addNeuron(ArrayList<Connection> listConnection, ArrayList<Neuron> listNeuronHidden, Connection connection) {
        Neuron neuron = NeuronManager.createNeuron(connection);
        listNeuronHidden.add(neuron);

        connection.disable();

        Connection newConnection = ConnectionManager.createConnection(connection.getFromNeuron(), neuron);
        newConnection.setWeight(1.0);
        listConnection.add(newConnection);

        newConnection = ConnectionManager.createConnection(neuron, connection.getToNeuron());
        newConnection.setWeight(connection.getWeight());
        listConnection.add(newConnection);

    }

    public static void perturbWeight(Connection connection) {
        double weight = connection.getWeight();
        weight += (Math.random() * 2 - 1) * weight / 10;
        connection.setWeight(weight);
    }
}
