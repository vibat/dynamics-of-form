package dof.gui;

import java.awt.BorderLayout;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextArea;
import java.awt.Font;
import net.miginfocom.swing.MigLayout;
import javax.swing.JScrollPane;
import java.awt.Toolkit;
import java.util.Arrays;

import javax.swing.JLabel;
import javax.swing.JTextField;

public class InfoDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextArea bracketOut;
	private JTextArea depthAnalysis;
	private JScrollPane scrollPane;
	private JScrollPane scrollPane_1;
	private JLabel tallyLabel;
	private JLabel gnomonLabel;
	private JLabel gIntervalLabel;
	private JTextField tallyField;
	private JTextField gnomonField;
	private JTextField gnomonIField;
	private JLabel thirdDerLabel;
	private JTextField thirdDerivField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			InfoDialog dialog = new InfoDialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public InfoDialog() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(InfoDialog.class.getResource("/assets/icon/fern.png")));
		setTitle("Information");
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new MigLayout("", "[grow][grow][grow][grow]", "[50:n:100px][][][::300px,grow]"));
		
		scrollPane = new JScrollPane();
		contentPanel.add(scrollPane, "cell 0 0 4 1,grow");
		
		bracketOut = new JTextArea();
		bracketOut.setLineWrap(true);
		scrollPane.setViewportView(bracketOut);
		bracketOut.setFont(new Font("Monospaced", Font.PLAIN, 12));
		bracketOut.setEditable(false);
		
		tallyLabel = new JLabel("Tally");
		contentPanel.add(tallyLabel, "cell 0 1,alignx center");
		
		gnomonLabel = new JLabel("Gnomon Series");
		contentPanel.add(gnomonLabel, "cell 1 1,alignx center");
		
		gIntervalLabel = new JLabel("Gnomon Interval");
		contentPanel.add(gIntervalLabel, "cell 2 1,alignx center");
		
		thirdDerLabel = new JLabel("3rd Derivative");
		contentPanel.add(thirdDerLabel, "cell 3 1,alignx center");
		
		tallyField = new JTextField();
		tallyField.setEditable(false);
		contentPanel.add(tallyField, "cell 0 2,growx");
		tallyField.setColumns(10);
		
		gnomonField = new JTextField();
		gnomonField.setEditable(false);
		contentPanel.add(gnomonField, "cell 1 2,growx");
		gnomonField.setColumns(10);
		
		gnomonIField = new JTextField();
		gnomonIField.setEditable(false);
		contentPanel.add(gnomonIField, "cell 2 2,growx");
		gnomonIField.setColumns(10);
		
		thirdDerivField = new JTextField();
		thirdDerivField.setEditable(false);
		contentPanel.add(thirdDerivField, "cell 3 2,growx");
		thirdDerivField.setColumns(10);
		
		scrollPane_1 = new JScrollPane();
		contentPanel.add(scrollPane_1, "cell 0 3 4 1,grow");
		
		depthAnalysis = new JTextArea();
		depthAnalysis.setLineWrap(true);
		scrollPane_1.setViewportView(depthAnalysis);
		depthAnalysis.setFont(new Font("Monospaced", Font.PLAIN, 20));
		depthAnalysis.setEditable(false);
	}
	
	public void setBracketText(String text) {
		bracketOut.setText(text);
		this.pack();
	}
	
	public void setDepthText(String text) {
		depthAnalysis.setText(text);
		this.pack();
	}
	
	public void setTally(int[] tally) {
		tallyField.setText(Arrays.toString(tally));
		this.pack();
	}
	
	public void setGnomon(int[] gnomon) {
		gnomonField.setText(Arrays.toString(gnomon));
		this.pack();
	}
	
	public void setGInterval (int[] gInterval) {
		gnomonIField.setText(Arrays.toString(gInterval));
		this.pack();
	}

	public void set3rdDeriv (int[] thirdDeriv) {
		thirdDerivField.setText(Arrays.toString(thirdDeriv));
		this.pack();
	}
	
}
