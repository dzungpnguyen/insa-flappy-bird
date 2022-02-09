package Manager;

import NeuralNetwork.*;

import java.util.ArrayList;
import java.util.HashMap;

public class NeuronManager {

    private static ArrayList<Neuron> listNeuronBase = new ArrayList<>();
    private static HashMap<Connection, Neuron> mapConnectionNeuron = new HashMap<>();

    public static ArrayList<Neuron> createNeuronBase(int[] structure) {
        if (listNeuronBase.size() == 0) {
            listNeuronBase.add(new Neuron("Bias", idDistributor.getIDNeuron(), null, null));
            for (int i = 0; i < structure[0]; i++) {
                listNeuronBase.add(new Neuron("Input", idDistributor.getIDNeuron(), null, null));
            }
            for (int i = 0; i < structure[structure.length - 1]; i++) {
                listNeuronBase.add(new Neuron("Output", idDistributor.getIDNeuron(), Neuron.ActivateFunction.SIGMOID, Neuron.LossFunction.BINARY_CROSSENTROPY));
            }
            return listNeuronBase;
        } else {
            ArrayList<Neuron> listNeuronBaseClone = new ArrayList<>();
            for (int i = 0; i < listNeuronBase.size(); i++) {
                listNeuronBaseClone.add(listNeuronBase.get(i).clone());
            }
            return listNeuronBaseClone;
        }
    }

    public static Neuron createNeuron(Connection connection) {
        for (Connection key : mapConnectionNeuron.keySet()) {
            if (key.equals(connection)) {
                return mapConnectionNeuron.get(key).clone();
            }
        }
        Neuron newNeuron = new Neuron("Hidden", idDistributor.getIDNeuron(), Neuron.ActivateFunction.SIGMOID, null);
        mapConnectionNeuron.put(connection, newNeuron);
        return newNeuron;
    }

}
