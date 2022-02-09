package Manager;

public class idDistributor {

    private static int countNeuron = 0;
    private static int countConnection = 0;

    public static int getIDNeuron() {
        countNeuron++;
        return countNeuron;
    }

    public static int getIDConnection() {
        countConnection++;
        return countConnection;
    }
}
