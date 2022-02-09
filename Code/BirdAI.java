import NeuralNetwork.*;

public class BirdAI extends Bird{
	
    	private NeuralNetwork neuralNetwork;

    	public BirdAI(NeuralNetwork neuralNetwork) {
        	super();
        	this.neuralNetwork = neuralNetwork;
    	}
    

	public boolean decide(double h1, double h2, double d){
		double[] predict = this.neuralNetwork.predict(new double[]{h1, h2, d});
		if (predict == null) {
			return false;
		}
		return predict[0] >= 0.5;
	}

}
