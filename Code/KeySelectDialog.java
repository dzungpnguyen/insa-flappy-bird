import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

//error happens when tap twice in either ] or [ always return Backspace

public class KeySelectDialog extends JDialog{
	
    	public KeySelectDialog(final JFrame frame) {
        	super(frame, true);
        
        	setSize(300,100);
                                
		JLabel label = new JLabel("<html><p align=center>" + "Press a specific KEY to select it");
        	label.setHorizontalAlignment(JLabel.CENTER);
       		setContentPane(label);
        	setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        	setLocationRelativeTo(frame);

		addKeyListener(new KeyListener(){
			public void keyPressed(KeyEvent e) {
				String key = e.getKeyText(e.getKeyCode());
				if (key.contains("Unknown keyCode: 0x0")){
					UnknownKeyDialog unknownKeyDialog = new UnknownKeyDialog(frame);
               				unknownKeyDialog.setVisible(true);
				} else {
					GlobalVariable.KEY_SELECTED = key;
					dispose();
				}
			}  
    			public void keyReleased(KeyEvent e) {}  
    			public void keyTyped(KeyEvent e) {}
		});

        
    	}

	public class UnknownKeyDialog extends JDialog{
		
		public UnknownKeyDialog(JFrame frame){
			super(frame, true);
        		setSize(300,100);
                                
			JLabel label = new JLabel("<html><p align=center>" + "Unknown keyCode: 0x0");
        		label.setHorizontalAlignment(JLabel.CENTER);
       			setContentPane(label);
        		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
			setLocationRelativeTo(frame);
			
			
			Timer timer = new Timer(500, new ActionListener(){
				public void actionPerformed(ActionEvent e){
					dispose();
				}
			});
			timer.setRepeats(false);
			timer.start();
		}
		
	}

	public static void main(String[] args){
		
		JFrame frame = new JFrame();
		frame.setSize(500, 500);
		frame.setVisible(true);
		
		KeySelectDialog dialog = new KeySelectDialog(frame);
		dialog.setVisible(true);
	}
}