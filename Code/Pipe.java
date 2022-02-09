import javax.swing.*;
import java.awt.*;

public class Pipe extends JPanel{
	public static final int width = 50;
	public static final int y = 0;
	public static final int blankSpace = 200; // Distance réservée  au passage de l'oiseau
	public static final int heightPipeHead = 20;

	private int height;
	private int x;
	private int heightPipeBodyUp;
	
	public Pipe(){
		super();
		setLayout(null);
		setOpaque(false);
		x = (int)GlobalVariable.getSize().getWidth();
		height = (int)GlobalVariable.getSize().getHeight();
		
		setBounds(x, y, width, height); 
		
		heightPipeBodyUp = (int)(Math.random() * (height - blankSpace) / 2 + (height - blankSpace) / 4); 
		JLabel pipeBodyUp = new JLabel();
		pipeBodyUp.setIcon(new ImageIcon(new ImageIcon(getClass().getResource("/Image/PipeBody.jpg")).getImage().getScaledInstance(width - 10, heightPipeBodyUp, Image.SCALE_DEFAULT)));
		pipeBodyUp.setHorizontalAlignment(SwingConstants.CENTER);
		pipeBodyUp.setBounds(0, 0, width, heightPipeBodyUp);   
		

		JLabel pipeHeadUp = new JLabel();
		pipeHeadUp.setIcon(new ImageIcon(new ImageIcon(getClass().getResource("/Image/PipeHead.jpg")).getImage().getScaledInstance(width, heightPipeHead, Image.SCALE_DEFAULT)));
		pipeHeadUp.setBounds(0, heightPipeBodyUp, width, heightPipeHead); 


		JLabel pipeHeadDown = new JLabel();
		pipeHeadDown.setIcon(new ImageIcon(new ImageIcon(getClass().getResource("/Image/PipeHead.jpg")).getImage().getScaledInstance(width, heightPipeHead, Image.SCALE_DEFAULT)));
		pipeHeadDown.setBounds(0, heightPipeBodyUp + heightPipeHead + blankSpace, width, heightPipeHead);
		

		int heightPipeBodyDown = height - heightPipeBodyUp - 2 * heightPipeHead - blankSpace; 
		JLabel pipeBodyDown = new JLabel();
		pipeBodyDown.setIcon(new ImageIcon(new ImageIcon(getClass().getResource("/Image/PipeBody.jpg")).getImage().getScaledInstance(width - 10, heightPipeBodyDown, Image.SCALE_DEFAULT)));
		pipeBodyDown.setHorizontalAlignment(SwingConstants.CENTER);
		pipeBodyDown.setBounds(0, height - heightPipeBodyDown, width, heightPipeBodyDown);
		
		add(pipeBodyUp);
		add(pipeHeadUp);
		add(pipeHeadDown);   
		add(pipeBodyDown);
	}

	
	public int getX(){
		return this.x;
	}
	
	public void setX(int x){
		this.x = x;
		setBounds(x, y, width, height);
	}
	
	public int getHeightPipeBodyUp(){
		return this.heightPipeBodyUp;
	}

	public Pipe clone(){
		Pipe pipe = new Pipe();
		pipe.removeAll();
		pipe.setBounds(x, y, width, height); 
		
		JLabel pipeBodyUp = new JLabel();
		pipeBodyUp.setIcon(new ImageIcon(new ImageIcon(getClass().getResource("/Image/PipeBody.jpg")).getImage().getScaledInstance(width - 10, heightPipeBodyUp, Image.SCALE_DEFAULT)));
		pipeBodyUp.setHorizontalAlignment(SwingConstants.CENTER);
		pipeBodyUp.setBounds(0, 0, width, heightPipeBodyUp);   
		

		JLabel pipeHeadUp = new JLabel();
		pipeHeadUp.setIcon(new ImageIcon(new ImageIcon(getClass().getResource("/Image/PipeHead.jpg")).getImage().getScaledInstance(width, heightPipeHead, Image.SCALE_DEFAULT)));
		pipeHeadUp.setBounds(0, heightPipeBodyUp, width, heightPipeHead); 


		JLabel pipeHeadDown = new JLabel();
		pipeHeadDown.setIcon(new ImageIcon(new ImageIcon(getClass().getResource("/Image/PipeHead.jpg")).getImage().getScaledInstance(width, heightPipeHead, Image.SCALE_DEFAULT)));
		pipeHeadDown.setBounds(0, heightPipeBodyUp + heightPipeHead + blankSpace, width, heightPipeHead);
		

		int heightPipeBodyDown = height - heightPipeBodyUp - 2 * heightPipeHead - blankSpace; 
		JLabel pipeBodyDown = new JLabel();
		pipeBodyDown.setIcon(new ImageIcon(new ImageIcon(getClass().getResource("/Image/PipeBody.jpg")).getImage().getScaledInstance(width - 10, heightPipeBodyDown, Image.SCALE_DEFAULT)));
		pipeBodyDown.setHorizontalAlignment(SwingConstants.CENTER);
		pipeBodyDown.setBounds(0, height - heightPipeBodyDown, width, heightPipeBodyDown);
		
		pipe.add(pipeBodyUp);
		pipe.add(pipeHeadUp);
		pipe.add(pipeHeadDown);   
		pipe.add(pipeBodyDown);

		return pipe;
		
	}
	
}
