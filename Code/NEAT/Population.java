package NEAT;

import NeuralNetwork.*;

import java.util.ArrayList;

public class Population {

    public static int populationSize;
    public ArrayList<Species> listSpecies;
    
    public Population(int size) {
        this.listSpecies = new ArrayList<>();
        populationSize = size;
    }

    public ArrayList<NeuralNetwork> reproduce(double ratePerturbWeight, double rateAddNeuron, double rateAddConnection) {
        NeuralNetwork.epoch++;

        ArrayList<NeuralNetwork> listNeuralNetworkOffspring = new ArrayList<>();
        int count = 0;

        double averageFitness = 0;
        for (Species species : this.listSpecies) {
            averageFitness += species.getFitnessTotal();
        }
        averageFitness /= Population.populationSize;

        for (Species species : this.listSpecies) {
            species.setSizeMax((int) (species.getFitnessTotal() / averageFitness));
        }

        for (Species species : this.listSpecies) {
            if (species.getSizeMax() > 0) {
                NeuralNetwork bestNeuralNetwork = species.getBestNeuralNetwork();
                bestNeuralNetwork.setID(count);
                count++;
                listNeuralNetworkOffspring.add(bestNeuralNetwork);
                NeuralNetwork secondBestNeuralNetwork = species.getSecondBestNeuralNetwork();
                secondBestNeuralNetwork.setID(count);
                count++;
                listNeuralNetworkOffspring.add(secondBestNeuralNetwork);

                for (int i = 0; i < species.getSizeMax() - 2; i++) {
                    NeuralNetwork neuralNetworkOffspring = CrossOver.crossOver(bestNeuralNetwork, secondBestNeuralNetwork);
                    neuralNetworkOffspring.mutate(ratePerturbWeight, rateAddNeuron, rateAddConnection);
                    neuralNetworkOffspring.setID(count);
                    count++;
                    listNeuralNetworkOffspring.add(neuralNetworkOffspring);
                }
            }
        }

        int slotLeft = Population.populationSize - listNeuralNetworkOffspring.size();
        Species bestSpecies = this.listSpecies.get(0);
        for (Species species : this.listSpecies) {
            if (species.getFitnessTotal() / (species.getListNeuralNetwork().size() * 1.0) > bestSpecies.getFitnessTotal() / (bestSpecies.getListNeuralNetwork().size() * 1.0)) {
                bestSpecies = species;
            }
        }

        for (int i = 0; i < slotLeft; i++) {
            NeuralNetwork neuralNetworkOffspring = CrossOver.crossOver(bestSpecies.getRandomNeuralNetwork(), bestSpecies.getRandomNeuralNetwork());
            neuralNetworkOffspring.mutate(ratePerturbWeight, rateAddNeuron, rateAddConnection);
            neuralNetworkOffspring.setID(count);
            count++;
            listNeuralNetworkOffspring.add(neuralNetworkOffspring);
        }

        return listNeuralNetworkOffspring;
    }
    
    public ArrayList<NeuralNetwork> reproduceRandom(double ratePerturbWeight, double rateAddNeuron, double rateAddConnection) {
        NeuralNetwork.epoch++;

        ArrayList<NeuralNetwork> listNeuralNetworkOffspring = new ArrayList<>();
        int count = 0;

        double averageFitness = 0;
        for (Species species : this.listSpecies) {
            averageFitness += species.getFitnessTotal();
        }
        averageFitness /= Population.populationSize;
        
        for (Species species : this.listSpecies) {
            species.setSizeMax((int) (species.getFitnessTotal() / averageFitness));
            if (species.getListNeuralNetwork().size() > 1){
                species.getListNeuralNetwork().remove(species.getWorstNeuralNetwork());
            }
        }

        for (Species species : this.listSpecies) {
            if (species.getSizeMax() > 0) {
                 for (int i = 0; i < species.getSizeMax(); i++) {
                    NeuralNetwork neuralNetworkOffspring = CrossOver.crossOver(species.getRandomNeuralNetwork(), species.getRandomNeuralNetwork());
                    neuralNetworkOffspring.mutate(ratePerturbWeight, rateAddNeuron, rateAddConnection);
                    neuralNetworkOffspring.setID(count);
                    count++;
                    listNeuralNetworkOffspring.add(neuralNetworkOffspring);
                }
            }
        }

        int slotLeft = Population.populationSize - listNeuralNetworkOffspring.size();
        Species bestSpecies = this.listSpecies.get(0);
        for (Species species : this.listSpecies) {
            if (species.getFitnessTotal() / (species.getListNeuralNetwork().size() * 1.0) > bestSpecies.getFitnessTotal() / (bestSpecies.getListNeuralNetwork().size() * 1.0)) {
                bestSpecies = species;
            }
        }

        for (int i = 0; i < slotLeft; i++) {
            NeuralNetwork neuralNetworkOffspring = CrossOver.crossOver(bestSpecies.getRandomNeuralNetwork(), bestSpecies.getRandomNeuralNetwork());
            neuralNetworkOffspring.mutate(ratePerturbWeight, rateAddNeuron, rateAddConnection);
            neuralNetworkOffspring.setID(count);
            count++;
            listNeuralNetworkOffspring.add(neuralNetworkOffspring);
        }

        return listNeuralNetworkOffspring;
    }

    public void speciate(ArrayList<NeuralNetwork> listNeuralNetwork) {
        this.listSpecies = new ArrayList<>();

        for (NeuralNetwork neuralNetwork : listNeuralNetwork) {
            if (this.listSpecies.isEmpty()) {
                Species species = new Species();
                species.getListNeuralNetwork().add(neuralNetwork);
                this.listSpecies.add(species);
            } else {
                boolean isSpeciated = false;
                for (Species species : this.listSpecies) {
                    if (neuralNetwork.getCompatibilityDistance(species.getRepresentative()) < CompatibilityDistance.compatibilityThreshold) {
                        isSpeciated = true;
                        species.getListNeuralNetwork().add(neuralNetwork);
                        break;
                    }
                }

                if (!isSpeciated) {
                    Species species = new Species();
                    species.getListNeuralNetwork().add(neuralNetwork);
                    this.listSpecies.add(species);
                }
            }
        }

    }
    
    
    
    public void setFitnessFunction(ArrayList<NeuralNetwork> listNeuralNetwork, FitnessFunction fitnessFunction){
        for (NeuralNetwork neuralNetwork: listNeuralNetwork){
            neuralNetwork.setFitnessFunction(fitnessFunction);
        }
    }
    
    public void calculFitness(ArrayList<NeuralNetwork> listNeuralNetwork){
        for (NeuralNetwork neuralNetwork: listNeuralNetwork){
            neuralNetwork.calculFitness();
        }
    }

    public NeuralNetwork getBestNeuralNetwork() {
        NeuralNetwork bestNeuralNetwork = this.listSpecies.get(0).getBestNeuralNetwork();
        for (Species species : this.listSpecies) {
            NeuralNetwork neuralNetwork = species.getBestNeuralNetwork();
            if (neuralNetwork == null){
                System.out.println("null1" + species.getListNeuralNetwork().size());
            }
            if (bestNeuralNetwork == null){
                System.out.println("null2");
            }
            if (neuralNetwork.getFitness() > bestNeuralNetwork.getFitness()) {
                bestNeuralNetwork = neuralNetwork;
            }
        }
        return bestNeuralNetwork;
    }
}
