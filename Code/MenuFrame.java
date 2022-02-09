import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

public abstract class MenuFrame extends JFrame implements ActionListener{
	
	JButton btnOnePlayer;
	JButton btnVsAI;
	JButton btnOption;
	JButton btnScore;
	
	
	JLabel background, intro;

	JLabel btnNextBird, btnBackBird, btnNextPlanet, btnBackPlanet;
	JLabel lbBird, lbPlanet;

	
	public MenuFrame(){
		super("Flappy Bird");
		setBackground(new Color(0,0,0,1));
		setMinimumSize(GlobalVariable.minimumSize);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		

		background = new JLabel();
		background.setIcon(new ImageIcon(new ImageIcon(getClass().getResource("/Image/" + GlobalVariable.IMAGE_BACKGROUND[GlobalVariable.planetSelected])).getImage().getScaledInstance(1000, 650, Image.SCALE_DEFAULT)));
		background.setBounds(-5, -5, 1000, 650);
		
		
		lbBird = new JLabel();
		lbBird.setIcon(new ImageIcon(new ImageIcon(getClass().getResource("/Image/" + GlobalVariable.IMAGE_BIRD[GlobalVariable.birdSelected])).getImage().getScaledInstance(Bird.width * 2, Bird.height * 2, Image.SCALE_DEFAULT)));
		lbBird.setBounds(60 + 50 - Bird.width, 370, Bird.width * 2, Bird.height * 2);
		getContentPane().add(lbBird);

		lbPlanet = new JLabel();
		lbPlanet.setIcon(new ImageIcon(new ImageIcon(getClass().getResource("/Image/" + GlobalVariable.IMAGE_PLANET[GlobalVariable.planetSelected])).getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT)));
		lbPlanet.setBounds(240 + 25, 370, 50, 50);
		getContentPane().add(lbPlanet);

		btnBackBird = new JLabel("<");
		btnBackBird.setBounds(50, 370, 50, 50);
		btnBackBird.setFont(new Font(btnBackBird.getFont().getName(), btnBackBird.getFont().getStyle(), 50));
		btnBackBird.setForeground(Color.BLACK);
		btnBackBird.addMouseListener(new MouseListener(){
			public void mouseClicked(MouseEvent e){
				if (GlobalVariable.birdSelected == 0){
					GlobalVariable.birdSelected = GlobalVariable.IMAGE_BIRD.length - 1;
				}else{
					GlobalVariable.birdSelected--;
				}
				lbBird.setIcon(new ImageIcon(new ImageIcon(getClass().getResource("/Image/" + GlobalVariable.IMAGE_BIRD[GlobalVariable.birdSelected])).getImage().getScaledInstance(Bird.width * 2, Bird.height * 2, Image.SCALE_DEFAULT)));
			}
			public void mouseExited(MouseEvent e){}
			public void mousePressed(MouseEvent e){}
			public void mouseEntered(MouseEvent e){}
			public void mouseReleased(MouseEvent e){}
		});
		getContentPane().add(btnBackBird);
		
		btnNextBird = new JLabel(">");
		btnNextBird.setBounds(140, 370, 50, 50);
		btnNextBird.setFont(new Font(btnNextBird.getFont().getName(), btnNextBird.getFont().getStyle(), 50));
		btnNextBird.setForeground(Color.BLACK);
		btnNextBird.addMouseListener(new MouseListener(){
			public void mouseClicked(MouseEvent e){
				if (GlobalVariable.birdSelected == GlobalVariable.IMAGE_BIRD.length - 1){
					GlobalVariable.birdSelected = 0;
				}else{
					GlobalVariable.birdSelected++;
				}
				lbBird.setIcon(new ImageIcon(new ImageIcon(getClass().getResource("/Image/" + GlobalVariable.IMAGE_BIRD[GlobalVariable.birdSelected])).getImage().getScaledInstance(Bird.width * 2, Bird.height * 2, Image.SCALE_DEFAULT)));
			}
			public void mouseExited(MouseEvent e){}
			public void mousePressed(MouseEvent e){}
			public void mouseEntered(MouseEvent e){}
			public void mouseReleased(MouseEvent e){}
		});
		getContentPane().add(btnNextBird);

		
		btnBackPlanet = new JLabel("<");
		btnBackPlanet.setBounds(230, 370, 50, 50);
		btnBackPlanet.setFont(new Font(btnBackPlanet.getFont().getName(), btnBackPlanet.getFont().getStyle(), 50));
		btnBackPlanet.setForeground(Color.BLACK);
		btnBackPlanet.addMouseListener(new MouseListener(){
			public void mouseClicked(MouseEvent e){
				if (GlobalVariable.planetSelected == 0){
					GlobalVariable.planetSelected = GlobalVariable.IMAGE_BIRD.length - 1;
				}else{
					GlobalVariable.planetSelected--;
				}
				lbPlanet.setIcon(new ImageIcon(new ImageIcon(getClass().getResource("/Image/" + GlobalVariable.IMAGE_PLANET[GlobalVariable.planetSelected])).getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT)));
				background.setIcon(new ImageIcon(new ImageIcon(getClass().getResource("/Image/" + GlobalVariable.IMAGE_BACKGROUND[GlobalVariable.planetSelected])).getImage().getScaledInstance(1013, 650, Image.SCALE_DEFAULT)));
			}
			public void mouseExited(MouseEvent e){}
			public void mousePressed(MouseEvent e){}
			public void mouseEntered(MouseEvent e){}
			public void mouseReleased(MouseEvent e){}
		});
		getContentPane().add(btnBackPlanet);


		btnNextPlanet = new JLabel(">");
		btnNextPlanet.setBounds(320, 370, 50, 50);
		btnNextPlanet.setFont(new Font(btnNextPlanet.getFont().getName(), btnNextPlanet.getFont().getStyle(), 50));
		btnNextPlanet.setForeground(Color.BLACK);
		btnNextPlanet.addMouseListener(new MouseListener(){
			public void mouseClicked(MouseEvent e){
				if (GlobalVariable.planetSelected == GlobalVariable.IMAGE_PLANET.length - 1){
					GlobalVariable.planetSelected = 0;
				}else{
					GlobalVariable.planetSelected++;
				}
				lbPlanet.setIcon(new ImageIcon(new ImageIcon(getClass().getResource("/Image/" + GlobalVariable.IMAGE_PLANET[GlobalVariable.planetSelected])).getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT)));
				background.setIcon(new ImageIcon(new ImageIcon(getClass().getResource("/Image/" + GlobalVariable.IMAGE_BACKGROUND[GlobalVariable.planetSelected])).getImage().getScaledInstance(1013, 650, Image.SCALE_DEFAULT)));
			}
			public void mouseExited(MouseEvent e){}
			public void mousePressed(MouseEvent e){}
			public void mouseEntered(MouseEvent e){}
			public void mouseReleased(MouseEvent e){}
		});
		getContentPane().add(btnNextPlanet);


		btnOnePlayer = new JButton("One Player");
		btnOnePlayer.setBounds(60, 450, 100, 50);
		btnOnePlayer.setFocusable(false);
		btnOnePlayer.addActionListener(this);
		btnOnePlayer.setBackground(Color.ORANGE);
		getContentPane().add(btnOnePlayer);

		btnVsAI = new JButton("vs AI");
		btnVsAI.setBounds(240, 450, 100, 50);
		btnVsAI.setFocusable(false);
		btnVsAI.addActionListener(this);
		btnVsAI.setBackground(Color.ORANGE);
		getContentPane().add(btnVsAI);

		btnOption = new JButton("Option");
		btnOption.setBounds(60, 530, 100, 50);
		btnOption.setFocusable(false);
		btnOption.addActionListener(this);
		btnOption.setBackground(Color.ORANGE);
		getContentPane().add(btnOption);

		btnScore = new JButton("Score");
		btnScore.setBounds(240, 530, 100, 50);
		btnScore.setFocusable(false);
		btnScore.addActionListener(this);
		btnScore.setBackground(Color.ORANGE);
		getContentPane().add(btnScore);
		
		intro = new JLabel("Flappy Bird", JLabel.CENTER);
		intro.setFont(new Font(intro.getFont().getName(), intro.getFont().getStyle(), 50));
		intro.setBounds(0,100,400,100);
		getContentPane().add(intro);

		getContentPane().add(background);
		

	}
	
	
	@Override
	public void setVisible(boolean b){
		super.setVisible(b);
		if (b){
			setLocationRelativeTo(GlobalVariable.LAST_VISIBLE_FRAME);
			GlobalVariable.LAST_VISIBLE_FRAME = this;
		}
	}

}
		
