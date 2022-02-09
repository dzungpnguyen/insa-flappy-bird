package Manager;

import NeuralNetwork.*;

import java.util.ArrayList;

public class ConnectionManager {

    private static ArrayList<Connection> listConnectionCreated = new ArrayList<>();

    public static Connection createConnection(Neuron fromNeuron, Neuron toNeuron) {
        for (Connection connection : listConnectionCreated) {
            if (connection.contains(fromNeuron, toNeuron)) {
                Connection connectionClone = connection.clone(fromNeuron, toNeuron);
//                connectionClone.setWeight(Math.random() * 20 - 10);
                connectionClone.setWeight(Math.random() * 2 - 1);
                return connectionClone;
            }
        }
        Connection newConnection = new Connection(fromNeuron, toNeuron, idDistributor.getIDConnection());
        listConnectionCreated.add(newConnection);
        return newConnection;
    }

}
