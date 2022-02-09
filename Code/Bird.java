import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class Bird extends JLabel{
	public static final int width = 30;
	public static final int height = 20;

	private int x;
	private int y;
	private int v;
	private boolean dead;
	
	public Bird(){
		super();
		setIcon(new ImageIcon(new ImageIcon(getClass().getResource("/Image/" + GlobalVariable.IMAGE_BIRD[GlobalVariable.birdSelected])).getImage().getScaledInstance(width, height, Image.SCALE_DEFAULT)));
		setOpaque(false);

		x = (int)(GlobalVariable.getSize().getWidth() / 3);
		y = (int)(GlobalVariable.getSize().getHeight() / 2 - height / 2);
		setBounds(x, y, width, height);

		v = 0;
		dead = false;
		
	}
	
	
	public void setY(int y){
		this.y = y;
		setBounds(x, y, width, height);
	}
	
	public int getY(){
		return this.y;
	}

	public void setX(int x){
		this.x = x;
		setBounds(x, y, width, height);
	}

	public int getX(){
		return this.x;
	}

	public int getV(){
		return this.v;
	}
	
	public void setV(int v){
		this.v = v;
	}

	public void setDead(boolean dead){
		this.dead = dead;
	}
	
	public boolean isDead(){
		return dead;
	}

	public void reset(){
		x = (int)(GlobalVariable.getSize().getWidth() / 3);
		y = (int)(GlobalVariable.getSize().getHeight() / 2 - height / 2);
		setBounds(x, y, width, height);

		v = 0;
		dead = false;
	}

	public void jump(){
		v = GlobalVariable.vBirdInit;
	}
}
