package com.rctereza.robotbx.views;

import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JDialog;
import javax.swing.JFrame;

import com.rctereza.robotbx.enums.Menu;
import com.rctereza.robotbx.interfaces.Listenable;

public class HistoricForm extends JDialog {

	private static final long serialVersionUID = -8935336038076796989L;

	private Listenable listener;

	public HistoricForm(Window parent, Menu menu) {
		super(parent, menu.getValue(), ModalityType.APPLICATION_MODAL);
		
		//setIconImage(Resources.getImage(Constants.SOFTWARE_ICON, null));

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				listener.value(Menu.CLOSE.getValue());
			}
		});
		
		
		
		
		setSize(1200, 550);
		setResizable(false);
		setLocationRelativeTo(parent);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}
	
	public void addObjectListener(Listenable listener) {
		this.listener = listener;
	}

	public void load() {
		this.setVisible(true);
	}

	public void close() {
		this.setVisible(false);
		this.dispose();
	}
}
