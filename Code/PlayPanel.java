import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.ArrayList;

public class PlayPanel extends JPanel implements ActionListener{ 
		
	private Bird bird;
	
	private ArrayList<Pipe> listPipe;
	public Pipe nearestPipe;
	private Pipe lastNearestPipe;
	
	public Timer timer;
	private int counter;
	private boolean isStarted;

	private int score;
	public JLabel lbScore;

	public JLabel background1;
	public JLabel background2;
	
	public PlayPanel(Bird bird){
		super();
		setLayout(null);
		setSize(GlobalVariable.minimumSize);
		setOpaque(false);

		this.bird = bird;
		add(this.bird);
			
		listPipe = new ArrayList<>();
		nearestPipe = null;
		lastNearestPipe = null;

		timer = new Timer(GlobalVariable.t, this);
		counter = -1;
		isStarted = false;

		score = 0;
		lbScore = new JLabel(score + "");	
		lbScore.setBounds(getWidth() - 55, 5, 40, 40);
		lbScore.setBackground(null);
		lbScore.setFont(new Font(lbScore.getFont().getName(), lbScore.getFont().getStyle(), 30));
		add(lbScore);

		addMouseListener(new MouseListener(){
			public void mouseClicked(MouseEvent e){
				if (GlobalVariable.isClickable){
					if (!isStarted){
						start();
					}else{
						jump();	
					}
				}
			}
			public void mouseExited(MouseEvent e){}
			public void mousePressed(MouseEvent e){}
			public void mouseEntered(MouseEvent e){}
			public void mouseReleased(MouseEvent e){}
		});
		
		
		background1 = new JLabel();
		background1.setBounds(-5, -5, (int)(GlobalVariable.getSize().getHeight() * 2), (int)GlobalVariable.getSize().getHeight());
		add(background1);

		background2 = new JLabel();
		background2.setBounds((int)(background1.getLocation().getX() + background1.getSize().getWidth() - 5), -5, (int)(GlobalVariable.getSize().getHeight() * 2), (int)GlobalVariable.getSize().getHeight());
		add(background2);
	}
	
	public void actionPerformed(ActionEvent e){
		updateFrame();
		checkBird();
		
		if (isFinished()){
			finish();
		}
	}
	
	private void updateFrame(){
		counter++;
		
		background1.setLocation((int)background1.getLocation().getX() - GlobalVariable.dBackground, -5);
		background2.setLocation((int)background2.getLocation().getX() - GlobalVariable.dBackground, -5);
		remove(background1);
		remove(background2);
		
		if (counter % 25 == 0){
			Pipe pipe = new Pipe();
			listPipe.add(pipe);
			add(pipe);
		}
		add(background1);
		add(background2);

		if (background1.getLocation().getX() + background1.getSize().getWidth() < 0){
			JLabel background = background1;
			background1 = background2;
			background2 = background;
			background2.setLocation((int)(background1.getLocation().getX() + background1.getSize().getWidth() - 5), -5);
		}

			
		Pipe pipeRemoved = null;
		for (Pipe pipe: listPipe){
			pipe.setX(pipe.getX() - GlobalVariable.dPipe);
			if (pipe.getX() + Pipe.width < 0){
				remove(pipe);
				pipeRemoved = pipe;
			}
		}
		if (pipeRemoved != null){
			listPipe.remove(pipeRemoved);
		}


		nearestPipe = getNearestPipe();
		if (lastNearestPipe == null) {
			lastNearestPipe = nearestPipe;
		}
		if (!lastNearestPipe.equals(nearestPipe)){
			lastNearestPipe = nearestPipe;
			score++;
			lbScore.setText(score + "");
		}		


		if (!bird.isDead()){
			int d = (int)(bird.getV() * GlobalVariable.t / 10 - 0.5 * GlobalVariable.gravity[GlobalVariable.planetSelected] * GlobalVariable.t * GlobalVariable.t / 100); 
			bird.setV(bird.getV() - GlobalVariable.gravity[GlobalVariable.planetSelected]);
			bird.setY(bird.getY() - d);	
		}

		repaint();
	}
	
	
	private void checkBird(){
		if (!bird.isDead()){
			if ((bird.getX() + Bird.width > nearestPipe.getX()) && (bird.getX() < nearestPipe.getX() + Pipe.width)){
				if ((bird.getY() < nearestPipe.getHeightPipeBodyUp() + Pipe.heightPipeHead) || (bird.getY() + Bird.height > nearestPipe.getHeightPipeBodyUp() + Pipe.blankSpace + Pipe.heightPipeHead)){
					bird.setDead(true);
				}
			}

			if (bird.getY() + Bird.height > this.getSize().getHeight()){
				bird.setDead(true);
			}	
	
			if (bird.getY() < 0){
				bird.setDead(true);
			}
		}

	}

	private Pipe getNearestPipe() {
        	for (Pipe pipe : listPipe) {
           		if (pipe.getX() + Pipe.width >= bird.getX()) {
              	  		return pipe;
           	 	}
    	    	}
       	 	return null;
   	}

	public void jump(){
		bird.jump();
	}

	public boolean isStarted(){
		return this.isStarted;
	}

	public boolean isFinished(){
		return bird.isDead();
	}
	
	public void start(){
		this.isStarted = true;
		timer.start();
	}

	public void finish(){
		isStarted = false;
		timer.stop();
		bird.setDead(true);
	}

	public void restart(){
		removeAll();
		
		bird.reset();
		add(bird);
		
		add(background1);
		add(background2);


		listPipe = new ArrayList<>();
		nearestPipe = null;
		lastNearestPipe = null;
		
		timer = new Timer(GlobalVariable.t, this);
		counter = -1;

		score = 0;
		lbScore.setText(score + "");
		add(lbScore);

		repaint();
	}


	public int getScore(){
		return this.score;
	}

	public void setBackground(boolean b){
		if (b){
			background1.setIcon(new ImageIcon(new ImageIcon(getClass().getResource("/Image/" + GlobalVariable.IMAGE_BACKGROUND[GlobalVariable.planetSelected])).getImage().getScaledInstance((int)(GlobalVariable.getSize().getHeight() * 2), (int)GlobalVariable.getSize().getHeight(), Image.SCALE_DEFAULT)));
			background2.setIcon(new ImageIcon(new ImageIcon(getClass().getResource("/Image/" + GlobalVariable.IMAGE_BACKGROUND[GlobalVariable.planetSelected])).getImage().getScaledInstance((int)(GlobalVariable.getSize().getHeight() * 2), (int)GlobalVariable.getSize().getHeight(), Image.SCALE_DEFAULT)));
		}else{
			background1.setIcon(null);
			background2.setIcon(null);
		}
		background1.setSize((int)(GlobalVariable.getSize().getHeight() * 2), (int)GlobalVariable.getSize().getHeight());
		background2.setBounds((int)(background1.getLocation().getX() + background1.getSize().getWidth() - 5), -5, (int)(GlobalVariable.getSize().getHeight() * 2), (int)GlobalVariable.getSize().getHeight());
	}
	
}