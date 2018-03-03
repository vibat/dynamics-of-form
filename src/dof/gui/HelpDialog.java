package dof.gui;

import java.awt.EventQueue;

import javax.swing.JDialog;
import net.miginfocom.swing.MigLayout;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import java.awt.Font;
import javax.swing.SwingConstants;
import java.awt.Color;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import java.awt.Toolkit;

public class HelpDialog extends JDialog {

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
					HelpDialog dialog = new HelpDialog();
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
	public HelpDialog() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(HelpDialog.class.getResource("/assets/icon/fern.png")));
		setResizable(false);
		setTitle("Help");
		setBounds(100, 100, 459, 670);
		getContentPane().setLayout(new MigLayout("", "[280.00,grow]", "[][][grow]"));
		
		JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		getContentPane().add(panel, "cell 0 0,grow");
		panel.setLayout(new MigLayout("", "[280.00,grow][280.00,grow][pref!,grow]", "[][grow][grow]"));
		
		JLabel lblNewLabel_3 = new JLabel("Basic marks");
		panel.add(lblNewLabel_3, "cell 0 0 3 1,alignx center");
		lblNewLabel_3.setFont(new Font("Tahoma", Font.BOLD, 13));
		
		JLabel lblNewLabel = new JLabel("");
		panel.add(lblNewLabel, "cell 0 1,alignx center");
		lblNewLabel.setIcon(new ImageIcon(HelpDialog.class.getResource("/assets/markimages/mark.png")));
		
		JLabel lblNewLabel_1 = new JLabel("");
		panel.add(lblNewLabel_1, "cell 1 1,alignx center");
		lblNewLabel_1.setIcon(new ImageIcon(HelpDialog.class.getResource("/assets/markimages/markchild.png")));
		
		JLabel lblNewLabel_2 = new JLabel("");
		panel.add(lblNewLabel_2, "cell 2 1,alignx center");
		lblNewLabel_2.setIcon(new ImageIcon(HelpDialog.class.getResource("/assets/markimages/multichild.png")));
		
		JLabel lblNewLabel_4 = new JLabel("()");
		panel.add(lblNewLabel_4, "cell 0 2,alignx center");
		
		JLabel lblNewLabel_5 = new JLabel("(())");
		panel.add(lblNewLabel_5, "cell 1 2,alignx center");
		
		JLabel lblNewLabel_6 = new JLabel("(()())");
		panel.add(lblNewLabel_6, "cell 2 2,alignx center");
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		getContentPane().add(panel_1, "cell 0 1,grow");
		panel_1.setLayout(new MigLayout("", "[grow][grow][grow]", "[][grow][grow]"));
		
		JLabel lblNewLabel_7 = new JLabel("Re-entry marks");
		panel_1.add(lblNewLabel_7, "cell 0 0 3 1,alignx center");
		lblNewLabel_7.setFont(new Font("Tahoma", Font.BOLD, 13));
		
		JLabel lblNewLabel_8 = new JLabel("");
		panel_1.add(lblNewLabel_8, "cell 0 1,alignx center");
		lblNewLabel_8.setIcon(new ImageIcon(HelpDialog.class.getResource("/assets/markimages/reentry.png")));
		lblNewLabel_8.setHorizontalAlignment(SwingConstants.CENTER);
		
		JLabel lblNewLabel_9 = new JLabel("");
		panel_1.add(lblNewLabel_9, "cell 2 1,alignx center");
		lblNewLabel_9.setIcon(new ImageIcon(HelpDialog.class.getResource("/assets/markimages/reentrychild.png")));
		
		JLabel lblNewLabel_11 = new JLabel("[]");
		panel_1.add(lblNewLabel_11, "cell 0 2,alignx center");
		
		JLabel lblNewLabel_10 = new JLabel("([])");
		panel_1.add(lblNewLabel_10, "cell 2 2,alignx center");
		
		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		getContentPane().add(panel_2, "cell 0 2,grow");
		panel_2.setLayout(new MigLayout("", "[280.00,grow][280.00,grow][pref!,grow]", "[][grow][grow][grow][grow]"));
		
		JLabel lblNewLabel_12 = new JLabel("Deep re-entry marks");
		panel_2.add(lblNewLabel_12, "cell 0 0 3 1,alignx center");
		lblNewLabel_12.setFont(new Font("Tahoma", Font.BOLD, 13));
		
		JLabel lblNewLabel_17 = new JLabel("");
		lblNewLabel_17.setIcon(new ImageIcon(HelpDialog.class.getResource("/assets/markimages/double.png")));
		panel_2.add(lblNewLabel_17, "cell 0 1,alignx center");
		
		JLabel lblNewLabel_14 = new JLabel("");
		panel_2.add(lblNewLabel_14, "cell 1 1,alignx center");
		lblNewLabel_14.setIcon(new ImageIcon(HelpDialog.class.getResource("/assets/markimages/deep.png")));
		lblNewLabel_14.setHorizontalAlignment(SwingConstants.CENTER);
		
		JLabel lblNewLabel_13 = new JLabel("");
		panel_2.add(lblNewLabel_13, "cell 2 1");
		lblNewLabel_13.setIcon(new ImageIcon(HelpDialog.class.getResource("/assets/markimages/fib.png")));
		
		JLabel lblNewLabel_18 = new JLabel("111");
		panel_2.add(lblNewLabel_18, "cell 0 2,alignx center");
		
		JLabel lblNewLabel_16 = new JLabel("1(1)");
		panel_2.add(lblNewLabel_16, "cell 1 2,alignx center");
		
		JLabel lblNewLabel_15 = new JLabel("1(1)1");
		panel_2.add(lblNewLabel_15, "cell 2 2,alignx center");
		
		JLabel lblNewLabel_19 = new JLabel("");
		lblNewLabel_19.setIcon(new ImageIcon(HelpDialog.class.getResource("/assets/markimages/2doubles.png")));
		lblNewLabel_19.setHorizontalAlignment(SwingConstants.CENTER);
		panel_2.add(lblNewLabel_19, "cell 0 3 3 1,alignx center");
		
		JLabel lblNewLabel_20 = new JLabel("(111222)");
		panel_2.add(lblNewLabel_20, "cell 1 4,alignx center");

	}

}
