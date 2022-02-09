import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

public abstract class OptionFrame extends JFrame implements ItemListener, ActionListener{

	JLabel lbControl;
	JCheckBox cbMouse, cbKey;
	JRadioButton rbAnyKey, rbSpecificKey;
	ButtonGroup btnGrp;
	JButton btnChangeKey;
	JLabel lbKeySelected;
	

	JCheckBox cbAlwaysOnTop, cbTransparent, cbClickThrough;
	
	public OptionFrame(){
		
		super("Option");
		setBackground(new Color(255, 255, 255, 1));
		setLayout(null);
		setSize(new Dimension(370, 140));
		setResizable(false);

		
		lbControl = new JLabel("  Control");
		lbControl.setBounds(5, 5, 80, 20);
		lbControl.setBackground(null);
		add(lbControl);
		
		cbMouse = new JCheckBox("Mouse", true);
		cbMouse.setBounds(85, 5, 80, 20);
		cbMouse.setOpaque(false);
		cbMouse.setFocusable(false);
		cbMouse.addItemListener(this);
		add(cbMouse);
		
		cbKey = new JCheckBox("Key", true);
		cbKey.setBounds(85, 25, 80, 20);
		cbKey.setOpaque(false);
		cbKey.setFocusable(false);
		cbKey.addItemListener(this);
		add(cbKey);
		
		rbAnyKey = new JRadioButton("Any key", true);
		rbAnyKey.setBounds(165, 25, 80, 20);
		rbAnyKey.setOpaque(false);
		rbAnyKey.setFocusable(false);
		rbAnyKey.addActionListener(this);
		add(rbAnyKey);
		
		rbSpecificKey = new JRadioButton("Specific key");
		rbSpecificKey.setBounds(165, 45, 100, 20);	
		rbSpecificKey.setOpaque(false);
		rbSpecificKey.setFocusable(false);
		rbSpecificKey.addActionListener(this);
		add(rbSpecificKey);
		
		btnGrp = new ButtonGroup();
		btnGrp.add(rbAnyKey);
		btnGrp.add(rbSpecificKey);

		btnChangeKey = new JButton("Change");
		btnChangeKey.setBounds(265, 45, 80, 20);	
		btnChangeKey.addActionListener(this);
		btnChangeKey.setFocusable(false);
		btnChangeKey.setVisible(false);
		add(btnChangeKey);

		lbKeySelected = new JLabel();
		lbKeySelected.setBounds(165, 65, 200, 20);
		lbKeySelected.setBackground(null);
		lbKeySelected.setVisible(false);
		add(lbKeySelected);
		
		cbAlwaysOnTop = new JCheckBox("Always On Top");
		cbAlwaysOnTop.setBounds(5, 65, 150, 20);
		cbAlwaysOnTop.setOpaque(false);
		cbAlwaysOnTop.setFocusable(false);
		cbAlwaysOnTop.addItemListener(this);
		add(cbAlwaysOnTop);

		cbTransparent = new JCheckBox("Transparent");
		cbTransparent.setBounds(5, 85, 150, 20);
		cbTransparent.setOpaque(false);
		cbTransparent.setFocusable(false);
		cbTransparent.addItemListener(this);
		add(cbTransparent);	
	
		cbClickThrough = new JCheckBox("Click Through");
		cbClickThrough.setBounds(165, 85, 150, 20);
		cbClickThrough.setOpaque(false);
		cbClickThrough.setFocusable(false);
		cbClickThrough.setVisible(false);
		cbClickThrough.addItemListener(this);
		add(cbClickThrough);

		
		
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
