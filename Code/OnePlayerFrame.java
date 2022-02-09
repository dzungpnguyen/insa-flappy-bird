import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.ArrayList;

public class OnePlayerFrame extends JFrame{

	private Bird bird;
	public PlayPanel playPanel;
	
	public OnePlayerFrame(){
		super("One Player");
		setBackground(new Color(0, 0, 0, 1));
		setLayout(null);
		setMinimumSize(GlobalVariable.minimumSize);
		

		bird = new Bird();
		bird.addMouseMotionListener(new MouseMotionListener(){
			public void mouseMoved(MouseEvent e) {}
    			public void mouseDragged(MouseEvent e) {
       				bird.setX(bird.getX() + e.getX());
				bird.setY(bird.getY() + e.getY());
				repaint();
    			}
		});
		
		
		playPanel = new PlayPanel(this.bird);
		playPanel.setLocation(0, 0);
		playPanel.addHierarchyBoundsListener(new HierarchyBoundsAdapter() {
			@Override
    			public void ancestorResized(HierarchyEvent e) {
				GlobalVariable.setSize(OnePlayerFrame.this.getSize());

				playPanel.setSize((int)OnePlayerFrame.this.getSize().getWidth(), (int)OnePlayerFrame.this.getSize().getHeight());
				playPanel.lbScore.setBounds((int)OnePlayerFrame.this.getSize().getWidth() - 55, 5, 40, 40);
				bird.setX((int)(OnePlayerFrame.this.getSize().getWidth() / 3));
				bird.setY((int)(OnePlayerFrame.this.getSize().getHeight() / 2 - Bird.height / 2));
				playPanel.setBackground(true);
    			}
		});
		getContentPane().add(playPanel);

		addKeyListener(new KeyListener(){
			public void keyPressed(KeyEvent e) {
				if (GlobalVariable.isPressable){
					if (GlobalVariable.KEY_SELECTED == null){
						if (!playPanel.isStarted()){
							playPanel.start();
						}else{
							playPanel.jump();	
						}
					}else if (e.getKeyText(e.getKeyCode()).equals(GlobalVariable.KEY_SELECTED)){
						if (!playPanel.isStarted()){
							playPanel.start();
						}else{
							playPanel.jump();	
						}		
					}
				}
			}  
    			public void keyReleased(KeyEvent e) {}  
    			public void keyTyped(KeyEvent e) {}  
		});
		
	}
	
	@Override
	public void setVisible(boolean b){
		super.setVisible(b);
		if (b){
			setLocationRelativeTo(GlobalVariable.LAST_VISIBLE_FRAME);
			GlobalVariable.LAST_VISIBLE_FRAME = this;
		}
		
		this.bird.setIcon(new ImageIcon(new ImageIcon(getClass().getResource("/Image/" + GlobalVariable.IMAGE_BIRD[GlobalVariable.birdSelected])).getImage().getScaledInstance(Bird.width, Bird.height, Image.SCALE_DEFAULT)));
		playPanel.setBackground(true);
		
	}
}
