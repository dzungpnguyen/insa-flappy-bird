import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.ArrayList;

import NeuralNetwork.*;
import java.io.*;

public class VsAIFrame extends JFrame{
	
	private Bird birdPlayer;
	private BirdAI birdAI;

	public PlayPanelVS playPanelPlayer;
	public PlayPanelVS playPanelAI;
	
	public VsAIFrame() throws IOException{
		super("VsAIFrame");
		setMinimumSize(GlobalVariable.twoPlayerSize);
		setBackground(new Color(0, 0, 0, 1));
		setLayout(null);
		setResizable(false);
		
		birdPlayer = new Bird();
		playPanelPlayer = new PlayPanelVS(birdPlayer);
		playPanelPlayer.setLocation(0, 0);
		playPanelPlayer.setBackground(true);
		playPanelPlayer.lbScore.setBounds(20, 5, 40, 40);
		getContentPane().add(playPanelPlayer);
		
		birdAI = new BirdAI(NeuralNetwork.read("NeuralNetwork_18"));
		playPanelAI = new PlayPanelVS(birdAI){
			public void actionPerformed(ActionEvent e){
				super.actionPerformed(e);
				
				if (!birdAI.isDead()) {
               				int h1 = birdAI.getY() - nearestPipe.getHeightPipeBodyUp();
                			int h2 = nearestPipe.getHeightPipeBodyUp() + Pipe.heightPipeHead + Pipe.blankSpace - birdAI.getY();
               				int d = nearestPipe.getX() + Pipe.width - birdAI.getX();
                			if (birdAI.decide(h1, h2, d)){
						jump();
					}
                		}
			}
		};
		playPanelAI.setLocation(playPanelAI.getWidth(), 0);
		playPanelAI.lbScore.setBounds(playPanelAI.getWidth() - 40, 5, 40, 40);
		playPanelAI.lbScoreWin.setBounds(15, 5, 80, 80);
		//playPanelAI.setBackground(new Color(255, 255, 0, 255));
		int l = playPanelAI.getMouseListeners().length;
		for (int i = 0; i < l; i++){
			playPanelAI.removeMouseListener(playPanelAI.getMouseListeners()[0]);
			playPanelPlayer.removeMouseListener(playPanelPlayer.getMouseListeners()[0]);
		}

		MouseListener mouseListener = new MouseListener(){
			public void mouseClicked(MouseEvent e){
				if (GlobalVariable.isClickable){
					if (!isStarted()){
						start();
					}else{
						playPanelPlayer.jump();	
					}
				}
			}
			public void mouseExited(MouseEvent e){}
			public void mousePressed(MouseEvent e){}
			public void mouseEntered(MouseEvent e){}
			public void mouseReleased(MouseEvent e){}
		};	
		
		playPanelAI.addMouseListener(mouseListener);
		playPanelPlayer.addMouseListener(mouseListener);
		getContentPane().add(playPanelAI);
		
 
		addKeyListener(new KeyListener(){
			public void keyPressed(KeyEvent e) {
				if (GlobalVariable.isPressable){
					if (GlobalVariable.KEY_SELECTED == null){
						if (!isStarted()){
							start();
						}else{
							playPanelPlayer.jump();	
						}
					}else if (e.getKeyText(e.getKeyCode()).equals(GlobalVariable.KEY_SELECTED)){
						if (!isStarted()){
							start();
						}else{
							playPanelPlayer.jump();	
						}		
					}
				}
			}  
    			public void keyReleased(KeyEvent e) {}  
    			public void keyTyped(KeyEvent e) {}  
		});

	}

	public void finish(){
		playPanelPlayer.finish();
		playPanelAI.finish();
	}

	public void restart(){
		playPanelPlayer.restart();
		playPanelAI.restart();
	}

	public void start(){
		playPanelAI.start();
		playPanelPlayer.start();
	}

	public void reset(){
		playPanelPlayer.reset();
		playPanelAI.reset();
	}

	public boolean isStarted(){
		return playPanelPlayer.isStarted() && playPanelAI.isStarted();
	}

	public boolean isFinished(){
		return playPanelPlayer.isFinished() && playPanelAI.isFinished();
	}

	public void addScoreWin(PlayPanelVS playPanel){
		playPanel.addScoreWin();
	}

	public boolean isFinished(PlayPanelVS playPanel){
		return playPanel.isFinished();
	}

	@Override
	public void setVisible(boolean b){
		super.setVisible(b);
		if (b){
			setLocationRelativeTo(GlobalVariable.LAST_VISIBLE_FRAME);
			GlobalVariable.LAST_VISIBLE_FRAME = this;
		}
		
		this.birdPlayer.setIcon(new ImageIcon(new ImageIcon(getClass().getResource("/Image/" + GlobalVariable.IMAGE_BIRD[GlobalVariable.birdSelected])).getImage().getScaledInstance(Bird.width, Bird.height, Image.SCALE_DEFAULT)));
		this.birdAI.setIcon(new ImageIcon(new ImageIcon(getClass().getResource("/Image/Bird.png")).getImage().getScaledInstance(Bird.width, Bird.height, Image.SCALE_DEFAULT)));
		playPanelPlayer.setBackground(false);
		playPanelPlayer.setBackground(0, 0, 255, 150);
		playPanelAI.setBackground(false);
		playPanelAI.setBackground(0, 255, 255, 150);
	}


	private class PlayPanelVS extends PlayPanel{ 

		private int scoreWin;
		public JLabel lbScoreWin;
	
		public PlayPanelVS(Bird bird){
			super(bird);
			
			scoreWin = 0;
			lbScoreWin = new JLabel(scoreWin + "");	
			lbScoreWin.setBounds(this.getWidth() - 55, 5, 80, 80);
			lbScoreWin.setBackground(null);
			lbScoreWin.setFont(new Font(lbScoreWin.getFont().getName(), lbScoreWin.getFont().getStyle(), 70));
			add(lbScoreWin);
			lbScoreWin.addMouseListener(new MouseListener(){
				public void mouseClicked(MouseEvent e){
					if (VsAIFrame.this.playPanelPlayer.isFinished() || VsAIFrame.this.playPanelAI.isFinished()){
						VsAIFrame.this.finish();
						VsAIFrame.this.restart();
					}
				}
				public void mouseExited(MouseEvent e){}
				public void mousePressed(MouseEvent e){}
				public void mouseEntered(MouseEvent e){}
				public void mouseReleased(MouseEvent e){}
			});	
		}
	
		

		public void restart(){
			super.restart();
			add(lbScoreWin);
			repaint();
		}

		public void reset(){
			finish();
			scoreWin = 0;
			lbScoreWin.setText(scoreWin + "");
			restart();
		}

		public void addScoreWin(){
			scoreWin++;
			lbScoreWin.setText(scoreWin + "");
			repaint();
		}

		public int getScoreWin(){
			return this.scoreWin;
		}
		
		
		public void setBackground(int r, int g, int b, int a){
			background1.setOpaque(true);
			background2.setOpaque(true);
			background1.setBackground(new Color(r, g, b, a));
			background2.setBackground(new Color(r, g, b, a));
		}
		

	}

	
}
