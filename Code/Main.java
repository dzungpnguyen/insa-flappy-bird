import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import static java.awt.GraphicsDevice.WindowTranslucency.*;
import java.io.*;

public class Main{

	private static OptionFrame optionFrame;
	private static OnePlayerFrame onePlayerFrame;
	private static VsAIFrame vsAIFrame;
	private static MenuFrame menuFrame;


	public static void main(String[] args) throws IOException{
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        	GraphicsDevice gd = ge.getDefaultScreenDevice();
        	boolean isPerPixelTranslucencySupported = gd.isWindowTranslucencySupported(PERPIXEL_TRANSLUCENT);
        	if (!isPerPixelTranslucencySupported) {
            		System.out.println("Per-pixel translucency is not supported");
                	System.exit(0);
        	}
		JFrame.setDefaultLookAndFeelDecorated(true);

		menuFrame = new MenuFrame(){
			public void actionPerformed(ActionEvent e){
				if (e.getSource() == menuFrame.btnOnePlayer){
					onePlayerFrame.setVisible(true);
					menuFrame.setVisible(false);
				}

				if (e.getSource() == menuFrame.btnVsAI){
					vsAIFrame.setVisible(true);
					menuFrame.setVisible(false);
				}

				if (e.getSource() == menuFrame.btnOption){
					optionFrame.setVisible(true);
				}
					
				if (e.getSource() == menuFrame.btnScore){
					FinishDialog dialog = new FinishDialog(menuFrame, -1);
					dialog.setVisible(true);
				}
			}
		};
		menuFrame.setVisible(true);
		
		onePlayerFrame = new OnePlayerFrame();
		onePlayerFrame.setVisible(false);
		onePlayerFrame.addWindowListener(new WindowAdapter() {
           		@Override
            		public void windowClosing(WindowEvent e) {
                		menuFrame.setVisible(true);
            		}
        	});
		
		vsAIFrame = new VsAIFrame();
		vsAIFrame.setVisible(false);
		vsAIFrame.addWindowListener(new WindowAdapter() {
           		@Override
            		public void windowClosing(WindowEvent e) {
                		menuFrame.setVisible(true);
            		}
        	});

		
		optionFrame = new OptionFrame(){
			public void itemStateChanged(ItemEvent e) { 
				if (e.getItem() == optionFrame.cbMouse){
					if (e.getStateChange() == ItemEvent.SELECTED){
						GlobalVariable.isClickable = true;
					}else{
						GlobalVariable.isClickable = false;
					}
				}

				if (e.getItem() == optionFrame.cbKey){
					if (e.getStateChange() == ItemEvent.SELECTED){
						GlobalVariable.isPressable = true;
						optionFrame.rbAnyKey.setVisible(true);
						optionFrame.rbSpecificKey.setVisible(true);
						if (optionFrame.rbSpecificKey.isSelected()){
							optionFrame.btnChangeKey.setVisible(true);
							optionFrame.lbKeySelected.setVisible(true);
						}
					}else{	
						GlobalVariable.isPressable = false;
						optionFrame.rbAnyKey.setVisible(false);
						optionFrame.rbSpecificKey.setVisible(false);
						optionFrame.btnChangeKey.setVisible(false);
						optionFrame.lbKeySelected.setVisible(false);
					}
				}

				
				if (e.getItem() == optionFrame.cbAlwaysOnTop){
					if (e.getStateChange() == ItemEvent.SELECTED){
						onePlayerFrame.setAlwaysOnTop(true);
						vsAIFrame.setAlwaysOnTop(true);
						menuFrame.setAlwaysOnTop(true);
						optionFrame.setAlwaysOnTop(true);
					}else{
						onePlayerFrame.setAlwaysOnTop(false);
						vsAIFrame.setAlwaysOnTop(false);
						menuFrame.setAlwaysOnTop(false);
						optionFrame.setAlwaysOnTop(false);
					}
				}
				
				
				if (e.getItem() == optionFrame.cbTransparent){
					if (e.getStateChange() == ItemEvent.SELECTED){
						onePlayerFrame.playPanel.background1.setVisible(false);
						onePlayerFrame.playPanel.background2.setVisible(false);
						menuFrame.background.setVisible(false);
						optionFrame.cbClickThrough.setVisible(true);
					}else{
						onePlayerFrame.playPanel.background1.setVisible(true);
						onePlayerFrame.playPanel.background2.setVisible(true);
						menuFrame.background.setVisible(true);
						optionFrame.cbClickThrough.setVisible(false);
					}
				}
				
				
				if (e.getItem() == optionFrame.cbClickThrough){
					if (e.getStateChange() == ItemEvent.SELECTED){
						onePlayerFrame.setBackground(new Color(0, 0, 0, 0));
						if (optionFrame.cbMouse.isSelected()){
							optionFrame.cbMouse.setSelected(false);
						}
						optionFrame.cbMouse.setEnabled(false);
					}else{
						onePlayerFrame.setBackground(new Color(0, 0, 0, 1));
						optionFrame.cbMouse.setEnabled(true);
		    			}
				}
				
			}

			public void actionPerformed(ActionEvent e){
				
				if (e.getSource() == optionFrame.btnChangeKey){
					KeySelectDialog keySelectDialog = new KeySelectDialog(optionFrame);
					keySelectDialog.setVisible(true);
					optionFrame.lbKeySelected.setText("Select: " + GlobalVariable.KEY_SELECTED);
				} else {

					if (optionFrame.rbAnyKey.isSelected()){
						GlobalVariable.KEY_SELECTED = null;
						optionFrame.btnChangeKey.setVisible(false);
						optionFrame.lbKeySelected.setVisible(false);
					}

					if (optionFrame.rbSpecificKey.isSelected()){
						optionFrame.btnChangeKey.setVisible(true);
						optionFrame.lbKeySelected.setVisible(true);
						KeySelectDialog keySelectDialog = new KeySelectDialog(optionFrame);
						keySelectDialog.setVisible(true);
						optionFrame.lbKeySelected.setText("Select: " + GlobalVariable.KEY_SELECTED);
					}
					
				}
			}
		};		
		optionFrame.setLocationRelativeTo(menuFrame);
		optionFrame.setVisible(false);
		
		
		while(true){

			System.out.print("");
			
			if (onePlayerFrame.isVisible()){
				
				while(!onePlayerFrame.playPanel.isFinished() && !vsAIFrame.isVisible()){
					System.out.print("");
				}
				
				if (onePlayerFrame.playPanel.isFinished()){
					FinishDialog finishDialog = new FinishDialog(onePlayerFrame, onePlayerFrame.playPanel.getScore());
					finishDialog.setVisible(true);
					onePlayerFrame.playPanel.restart();
				} 
			}

			if (vsAIFrame.isVisible()){
				
				boolean isChanged = false;
			
           			while (!vsAIFrame.isFinished() && !onePlayerFrame.isVisible()) {

					
					System.out.print("");

					if (vsAIFrame.isFinished(vsAIFrame.playPanelAI) && !isChanged){
						isChanged = true;
						vsAIFrame.addScoreWin(vsAIFrame.playPanelPlayer);
					}

                			if (vsAIFrame.isFinished(vsAIFrame.playPanelPlayer) && !isChanged){
						isChanged = true;
						vsAIFrame.addScoreWin(vsAIFrame.playPanelAI);
					}
					
					
           			}
				
				if (vsAIFrame.isFinished()){
					vsAIFrame.restart();
				}else{
					vsAIFrame.reset();
				}

            			
			}

			
		}
		
		
	}
}
