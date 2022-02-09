import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.io.*;

public class FinishDialog extends JDialog{
	
    	public FinishDialog(JFrame frame, int score) {
        	super(frame, true);
        	setSize(300,100);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);	
        	setLocationRelativeTo(frame);
		
		JLabel label;
		int highScore = readData();
		highScore = highScore > score ? highScore : score;
		saveData(highScore);

		if (score >= 0){    
			label = new JLabel("<html><p align=center>" + "Your Score: " + score + "<br>High Score: " + highScore + "<br>Press any key or click to continue");
        	}else{
			label = new JLabel("<html><p align=center>" + "High Score: " + highScore + "<br>Press any key or click to continue");
		}

		label.setHorizontalAlignment(JLabel.CENTER);
       		setContentPane(label);
        	

		addKeyListener(new KeyListener(){
			public void keyPressed(KeyEvent e) {
				dispose();
			}  
    			public void keyReleased(KeyEvent e) {}  
    			public void keyTyped(KeyEvent e) {}
		});

		addMouseListener(new MouseListener(){
			public void mouseClicked(MouseEvent e){
				dispose();
			}
			public void mouseExited(MouseEvent e){}
			public void mousePressed(MouseEvent e){}
			public void mouseEntered(MouseEvent e){}
			public void mouseReleased(MouseEvent e){}
		});		
    	}

	public int readData(){
		int highScore = 0;
		try (BufferedReader br = new BufferedReader(new FileReader("Data.txt"))) {
			highScore = Integer.valueOf(br.readLine());
        	} catch (IOException e) {
           		e.printStackTrace();
        	}
		return highScore;
	}

	public static void saveData(int newHighScore){
		try{
			FileWriter csvWriter = new FileWriter("Data.txt"); 
			csvWriter.append(newHighScore + ""); 
			
			csvWriter.flush();
			csvWriter.close();
		}catch (IOException e) {
            		e.printStackTrace();
        	}
	}

	public static void main(String[] args){
		
		JFrame frame = new JFrame();
		frame.setSize(500, 500);
		frame.setVisible(true);
		
		saveData(0);

		FinishDialog dialog = new FinishDialog(frame, 0);
		dialog.setVisible(true);
	}
}