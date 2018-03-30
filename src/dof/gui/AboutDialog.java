package dof.gui;

import java.awt.EventQueue;

import javax.swing.JDialog;
import java.awt.Toolkit;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JTextPane;
import net.miginfocom.swing.MigLayout;
import java.awt.Font;
import javax.swing.UIManager;

public class AboutDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AboutDialog dialog = new AboutDialog();
					dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					dialog.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the dialog.
	 */
	public AboutDialog() {
		setResizable(false);
		setTitle("About");
		setIconImage(Toolkit.getDefaultToolkit().getImage(AboutDialog.class.getResource("/assets/icon/fern.png")));
		setBounds(100, 100, 450, 300);
		
		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(new MigLayout("", "[432px]", "[][][253px]"));
		
		JLabel lblNewLabel = new JLabel("Dynamics of Form");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 20));
		panel.add(lblNewLabel, "cell 0 1,alignx center,aligny center");
		
		JTextPane txtpnCreatedByBen = new JTextPane();
		txtpnCreatedByBen.setEditable(false);
		txtpnCreatedByBen.setBackground(UIManager.getColor("Button.background"));
		txtpnCreatedByBen.setText("Created by Ben Hughson and Bernie Lewin\r\n\r\nVersion 1.01 released March 2018\r\n\r\nCode available at: https://github.com/vibat/dynamics-of-form");
		panel.add(txtpnCreatedByBen, "cell 0 2,alignx center,aligny center");

	}

}
