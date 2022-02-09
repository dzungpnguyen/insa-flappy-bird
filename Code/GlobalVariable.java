import java.awt.Point;
import java.awt.Dimension;
import javax.swing.JFrame;


public class GlobalVariable{


	public static String KEY_SELECTED = null;
	public static boolean isClickable = true;
	public static boolean isPressable = true;

	public static final Dimension minimumSize = new Dimension(400, 650);
	public static final Dimension twoPlayerSize = new Dimension(800, 650);

	public static JFrame LAST_VISIBLE_FRAME = null;


	public static String[] IMAGE_BIRD = {"bird_yellow.png", "bird_red.png", "bird_green.png", "bird_blue.png"};
	public static String[] IMAGE_PLANET = {"Earth.png", "Moon.png", "Mars.png"};
	public static String[] IMAGE_BACKGROUND = {"earth_landscape.jpg", "moon_landscape.jpg", "mars_landscape.jpg"};
	public static int[] gravity = {1, 2, 3};
	public static int birdSelected = 0;
	public static int planetSelected = 0;
		
	public static final int dPipe = 10;
	public static final int dBackground = 5;
	public static final int vBirdInit = 10;
	public static final int t = 50;

	private static Point lastLocation;
	private static Dimension lastSize = minimumSize;

	public static void setLocation(Point newLocation){
		lastLocation = newLocation;
	}

	public static Point getLocation(){
		return lastLocation;
	}

	public static void setSize(Dimension newSize){
		lastSize = newSize;
	}

	public static Dimension getSize(){
		return lastSize;
	}



	
}