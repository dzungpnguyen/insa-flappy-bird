package NEAT;

import NeuralNetwork.*;

import java.util.ArrayList;

public class CompatibilityDistance {

    public static final double compatibilityThreshold = 1;
    public static final double coefficientExcess = 1;
    public static final double coefficientDisjoint = 1;
    public static final double coefficientWeight = 3;

    public static double getCompatibilityDistance(NeuralNetwork male, NeuralNetwork female) {
        ArrayList<Connection> listMatchingConnectionMale = new ArrayList<>();
        ArrayList<Connection> listMatchingConnectionFemale = new ArrayList<>();
        int countDisjointConnection = 0;
        int countExcessConnection = 0;

        int maxMale = CrossOver.getConnectionIDMax(male);
        int maxFemale = CrossOver.getConnectionIDMax(female);

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
                countDisjointConnection++;
            }

            if (listConnectionMale[i] != null && listConnectionFemale[i] == null) {
                if (i > maxFemale) {
                    countExcessConnection++;
                } else {
                    countDisjointConnection++;
                }
            }

            if (listConnectionMale[i] != null && listConnectionFemale[i] != null) {
                listMatchingConnectionMale.add(listConnectionMale[i]);
                listMatchingConnectionFemale.add(listConnectionFemale[i]);
            }
        }

        double weightDifferent = 0;
        for (int i = 0; i < listMatchingConnectionMale.size(); i++) {
            weightDifferent += Math.abs(listMatchingConnectionMale.get(i).getWeight() - listMatchingConnectionFemale.get(i).getWeight());
        }

        int numberConnectionMax = (male.getListConnection().size() > female.getListConnection().size()) ? male.getListConnection().size() : female.getListConnection().size();
        return CompatibilityDistance.coefficientDisjoint * countDisjointConnection / (numberConnectionMax * 1.0)
                + CompatibilityDistance.coefficientExcess * countExcessConnection / (numberConnectionMax * 1.0)
                + CompatibilityDistance.coefficientWeight * weightDifferent / (listMatchingConnectionMale.size() * 1.0);
    }
}
