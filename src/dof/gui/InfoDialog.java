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
import javax.swing.JButton;
import javax.swing.JSeparator;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;

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
	private JLabel lblOscillating;
	private JTextField oFraction;
	private JButton btnGraph;
	private JSeparator separator;
	
	private int[][] data = {};
	
	private GraphFrame graphDialog;
	private JLabel lblNewLabel;
	private JTextField cFraction;
	private JSeparator separator_1;
	private JLabel lblNewLabel_1;
	private JLabel lblNewLabel_2;

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
		setTitle("Interpretative Analysis");
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new MigLayout("", "[grow][grow][][grow][grow]", "[][50:n:100px][][][][][][][::300px,grow]"));
		
		lblNewLabel_1 = new JLabel("Bracket output");
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		contentPanel.add(lblNewLabel_1, "cell 0 0 5 1,alignx center");
		
		scrollPane = new JScrollPane();
		contentPanel.add(scrollPane, "cell 0 1 5 1,grow");
		
		bracketOut = new JTextArea();
		bracketOut.setLineWrap(true);
		scrollPane.setViewportView(bracketOut);
		bracketOut.setFont(new Font("Monospaced", Font.PLAIN, 12));
		bracketOut.setEditable(false);
		
		tallyLabel = new JLabel("Tally");
		contentPanel.add(tallyLabel, "cell 0 2,alignx center");
		
		gnomonLabel = new JLabel("Gnomon Series");
		contentPanel.add(gnomonLabel, "cell 1 2,alignx center");
		
		gIntervalLabel = new JLabel("Gnomon Interval");
		contentPanel.add(gIntervalLabel, "cell 3 2,alignx center");
		
		thirdDerLabel = new JLabel("3rd Derivative");
		contentPanel.add(thirdDerLabel, "cell 4 2,alignx center");
		
		tallyField = new JTextField();
		tallyField.setEditable(false);
		contentPanel.add(tallyField, "cell 0 3,growx");
		tallyField.setColumns(10);
		
		gnomonField = new JTextField();
		gnomonField.setEditable(false);
		contentPanel.add(gnomonField, "cell 1 3,growx");
		gnomonField.setColumns(10);
		
		gnomonIField = new JTextField();
		gnomonIField.setEditable(false);
		contentPanel.add(gnomonIField, "cell 3 3,growx");
		gnomonIField.setColumns(10);
		
		thirdDerivField = new JTextField();
		thirdDerivField.setEditable(false);
		contentPanel.add(thirdDerivField, "cell 4 3,growx");
		thirdDerivField.setColumns(10);
		
		separator = new JSeparator();
		contentPanel.add(separator, "cell 0 4 5 1,grow");
		
		btnGraph = new JButton("Graph");
		btnGraph.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				graphDialog.setGraph(data);
				graphDialog.setVisible(true);
			}
		});
		
		lblNewLabel = new JLabel("Continued fraction");
		contentPanel.add(lblNewLabel, "cell 0 5,alignx trailing");
		
		cFraction = new JTextField();
		cFraction.setEditable(false);
		contentPanel.add(cFraction, "cell 1 5,growx");
		cFraction.setColumns(10);
		
		separator_1 = new JSeparator();
		separator_1.setOrientation(SwingConstants.VERTICAL);
		contentPanel.add(separator_1, "cell 2 5 1 2,grow");
		contentPanel.add(btnGraph, "cell 3 5 2 2,grow");
		
		lblOscillating = new JLabel("Oscillating fraction");
		contentPanel.add(lblOscillating, "cell 0 6,alignx trailing,aligny center");
		
		oFraction = new JTextField();
		oFraction.setEditable(false);
		contentPanel.add(oFraction, "cell 1 6,growx");
		oFraction.setColumns(10);
		
		lblNewLabel_2 = new JLabel("Plaintext depth analysis");
		lblNewLabel_2.setHorizontalAlignment(SwingConstants.CENTER);
		contentPanel.add(lblNewLabel_2, "cell 0 7 5 1,alignx center");
		
		scrollPane_1 = new JScrollPane();
		contentPanel.add(scrollPane_1, "cell 0 8 5 1,grow");
		
		depthAnalysis = new JTextArea();
		depthAnalysis.setLineWrap(true);
		scrollPane_1.setViewportView(depthAnalysis);
		depthAnalysis.setFont(new Font("Monospaced", Font.PLAIN, 20));
		depthAnalysis.setEditable(false);
		
		graphDialog = new GraphFrame();
	}
	
	public void setBracketText(String text) {
		bracketOut.setText(text);
		this.pack();
	}
	
	public void setDepthText(String text) {
		depthAnalysis.setText(text);
		this.pack();
	}
	
	public void setOFraction (String text) {
		oFraction.setText(text);
		this.pack();
	}
	
	public void setCFraction(String text) {
		cFraction.setText(text);
		this.pack();
	}
	
	public void setInfo(int[] tally, int[] gnomon, int[] gInterval, int[] thirdDeriv) {
		
		tallyField.setText(Arrays.toString(tally));
		gnomonField.setText(Arrays.toString(gnomon));
		gnomonIField.setText(Arrays.toString(gInterval));
		thirdDerivField.setText(Arrays.toString(thirdDeriv));
		
		pack();
		
		data = new int[][] {tally,gnomon,gInterval,thirdDeriv};
		
	}
	
}
