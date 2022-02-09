package NEAT;

import NeuralNetwork.*;

import java.util.ArrayList;

public class Species {

    public ArrayList<NeuralNetwork> listNeuralNetwork;
    private int sizeMax;

    public Species() {
        this.listNeuralNetwork = new ArrayList<>();
    }

    public ArrayList<NeuralNetwork> getListNeuralNetwork() {
        return this.listNeuralNetwork;
    }

    public int getSizeMax() {
        return this.sizeMax;
    }

    public double getFitnessTotal() {
        double fitnessTotal = 0;
        for (NeuralNetwork neuralNetwork : this.listNeuralNetwork) {
            fitnessTotal += neuralNetwork.getFitness();
        }
        return fitnessTotal;
    }

    public NeuralNetwork getRepresentative() {
        return this.listNeuralNetwork.get(0);
    }

    public void setSizeMax(int sizeMax) {
        this.sizeMax = sizeMax;
    }

    public NeuralNetwork getBestNeuralNetwork() {
        NeuralNetwork bestNeuralNetwork = null;
        for (NeuralNetwork neuralNetwork : this.listNeuralNetwork) {
            if (bestNeuralNetwork == null) {
                bestNeuralNetwork = neuralNetwork;
            } else if (neuralNetwork.getFitness() > bestNeuralNetwork.getFitness()) {
                bestNeuralNetwork = neuralNetwork;
            }
        }
        return bestNeuralNetwork;
    }

    public NeuralNetwork getSecondBestNeuralNetwork() {
        NeuralNetwork bestNeuralNetwork = this.getBestNeuralNetwork();
        NeuralNetwork secondBestNeuralNetwork = null;

        for (NeuralNetwork neuralNetwork : this.listNeuralNetwork) {
            if (!neuralNetwork.equals(bestNeuralNetwork)) {
                if (secondBestNeuralNetwork == null) {
                    secondBestNeuralNetwork = neuralNetwork;
                } else if (neuralNetwork.getFitness() > secondBestNeuralNetwork.getFitness()) {
                    secondBestNeuralNetwork = neuralNetwork;
                }
            }
        }
        
        
        return secondBestNeuralNetwork == null ? bestNeuralNetwork : secondBestNeuralNetwork;
    }

    public NeuralNetwork getWorstNeuralNetwork() {
        NeuralNetwork worstNeuralNetwork = null;
        for (NeuralNetwork neuralNetwork : this.listNeuralNetwork) {
            if (worstNeuralNetwork == null) {
                worstNeuralNetwork = neuralNetwork;
            } else if (neuralNetwork.getFitness() < worstNeuralNetwork.getFitness()) {
                worstNeuralNetwork = neuralNetwork;
            }
        }
        return worstNeuralNetwork;
    }

    public NeuralNetwork getRandomNeuralNetwork() {
        return this.listNeuralNetwork.get((int) (Math.random() * this.listNeuralNetwork.size()));
    }

}
