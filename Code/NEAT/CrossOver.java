package NEAT;

import NeuralNetwork.*;

import java.util.ArrayList;

public class CrossOver {

    public static NeuralNetwork crossOver(NeuralNetwork male, NeuralNetwork female) {
        ArrayList<Connection> listConnectionOffspring = new ArrayList<>();

        int maxMale = getConnectionIDMax(male);
        int maxFemale = getConnectionIDMax(female);

        if (maxFemale > maxMale) {
            NeuralNetwork nn = male;
            male = female;
            female = nn;

            int max = maxMale;
            maxMale = maxFemale;
            maxFemale = max;
        }

        Connection[] listConnectionMale = new Connection[maxMale];
        for (Connection connection : male.getListConnection()) {
            listConnectionMale[connection.getID()] = connection;
        }

        Connection[] listConnectionFemale = new Connection[maxMale];
        for (Connection connection : female.getListConnection()) {
            listConnectionFemale[connection.getID()] = connection;
        }

        for (int i = 0; i < maxMale; i++) {
            if (listConnectionMale[i] == null && listConnectionFemale[i] != null) {
                listConnectionOffspring.add(listConnectionFemale[i].clone());
            }

            if (listConnectionMale[i] != null && listConnectionFemale[i] == null) {
                listConnectionOffspring.add(listConnectionMale[i].clone());
            }

            if (listConnectionMale[i] != null && listConnectionFemale[i] != null) {
                if (!listConnectionMale[i].isEnabled() && listConnectionFemale[i].isEnabled()) {
                    listConnectionOffspring.add(listConnectionMale[i].clone());
                } else if (listConnectionMale[i].isEnabled() && !listConnectionFemale[i].isEnabled()) {
                    listConnectionOffspring.add(listConnectionFemale[i].clone());
                } else {
                    double random = Math.random();
                    Connection connection = (random < 0.5) ? listConnectionFemale[i].clone() : listConnectionMale[i].clone();
                    listConnectionOffspring.add(connection);
                }
            }
        }

        return new NeuralNetwork(listConnectionOffspring);
    }

    public static int getConnectionIDMax(NeuralNetwork neuralNetwork) {
        int max = 0;
        for (Connection connection : neuralNetwork.getListConnection()) {
            if (connection.getID() > max) {
                max = connection.getID();
            }
        }
        return max + 1;
    }
}
