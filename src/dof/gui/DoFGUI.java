package dof.gui;

import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;

import net.miginfocom.swing.MigLayout;
import visualise.TextInBox;
import visualise.TextInBoxNodeExtentProvider;
import visualise.TreeFactory;
import visualise.svg.SVGForTextInBoxTree;
import visualise.swing.TextInBoxTreePane;

import javax.swing.SwingConstants;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

import org.abego.treelayout.TreeForTreeLayout;
import org.abego.treelayout.TreeLayout;
import org.abego.treelayout.util.DefaultConfiguration;

import dof.ExpressionParser;
import dof.Mark;
import dof.analysis.BasicAnalysis;
import dof.analysis.FractionalAnalysis;
import dof.exceptions.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import java.awt.Toolkit;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

public class DoFGUI {

	private JFrame frmDynsOfForm;
	private JPanel treePanel;
	private JTextField expression;
	private JTextField numApprox;
	private JTextField depth;
	private JLabel depthLabel;
	private JLabel numApproxLabel;
	private JButton saveButton;
	private JButton infoButton;
	private JMenuBar menuBar;
	private JMenuItem mntmColor;
	private JMenuItem mntmAbout;
	private JMenuItem mntmHelp;
	private JMenuItem clearPane;
	private JCheckBoxMenuItem imageGen;
	private JComboBox<String> colorStyleSelect;
	private JScrollPane scrollPane;
	private InfoDialog infoDialog;
	private HelpDialog helpDialog;
	private AboutDialog aboutDialog;
	private JColorChooser ccd;

	private final Color DEFAULT_COLOR = Color.yellow;
	private Color selectedColor = DEFAULT_COLOR;

	private ExpressionParser ep = new ExpressionParser();

	private Mark head = null;
	private TreeLayout<TextInBox> treeLayout = null;
	private int printDepth = ExpressionParser.DEFAULT_DEPTH;
	private String exp = null;

	// setup the tree layout configuration
	private double gapBetweenLevels = 50;
	private double gapBetweenNodes = 10;
	private DefaultConfiguration<TextInBox> configuration = new DefaultConfiguration<TextInBox>(gapBetweenLevels,
			gapBetweenNodes);

	// create the NodeExtentProvider for TextInBox nodes
	private TextInBoxNodeExtentProvider nodeExtentProvider = new TextInBoxNodeExtentProvider();
	private JLabel lblError;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DoFGUI window = new DoFGUI();
					window.frmDynsOfForm.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public DoFGUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmDynsOfForm = new JFrame();
		frmDynsOfForm
				.setIconImage(Toolkit.getDefaultToolkit().getImage(DoFGUI.class.getResource("/assets/icon/fern.png")));
		frmDynsOfForm.setTitle("Dynamics of Form");
		frmDynsOfForm.setBounds(100, 100, 600, 400);
		frmDynsOfForm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmDynsOfForm.getContentPane().setLayout(new MigLayout("",
				"[][60.00,grow,left][180.00][140:140.00,grow][30:30.00][40.00:40]", "[][][][][grow]"));

		JLabel expLabel = new JLabel("Expression");
		expLabel.setHorizontalAlignment(SwingConstants.CENTER);
		frmDynsOfForm.getContentPane().add(expLabel, "cell 0 0 2 1,alignx center");

		expression = new JTextField();
		expression.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				exp = expression.getText();

				generateTree();
			}
		});
		expression.setFont(new Font("Tahoma", Font.PLAIN, 17));
		frmDynsOfForm.getContentPane().add(expression, "cell 2 0 2 1,growx");
		expression.setColumns(10);

		depthLabel = new JLabel("Depth");
		frmDynsOfForm.getContentPane().add(depthLabel, "cell 4 0,alignx trailing");

		depth = new JTextField();
		depth.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				try {
					printDepth = Integer.parseInt(depth.getText());
					if (exp != null) {
						generateTree();
					}
				} catch (NumberFormatException e) {
					depth.setText(Integer.toString(printDepth));
				}
			}
		});
		depth.setText(Integer.toString(printDepth));
		frmDynsOfForm.getContentPane().add(depth, "cell 5 0,growx");
		depth.setColumns(10);

		saveButton = new JButton("Save");
		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (treeLayout != null) {
					saveFile();
				}

			}
		});

		lblError = new JLabel("");
		lblError.setForeground(Color.RED);
		frmDynsOfForm.getContentPane().add(lblError, "cell 2 1 2 1,alignx center");
		frmDynsOfForm.getContentPane().add(saveButton, "cell 1 3,alignx center");

		numApproxLabel = new JLabel("Continued fraction");
		frmDynsOfForm.getContentPane().add(numApproxLabel, "cell 2 3,alignx center");

		numApprox = new JTextField();
		numApprox.setEditable(false);
		frmDynsOfForm.getContentPane().add(numApprox, "cell 3 3,growx");
		numApprox.setColumns(10);

		infoButton = new JButton("Information");
		infoButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				infoDialog.setVisible(true);
			}
		});
		frmDynsOfForm.getContentPane().add(infoButton, "cell 4 3 2 1,alignx center");

		scrollPane = new JScrollPane();
		frmDynsOfForm.getContentPane().add(scrollPane, "cell 1 4 5 1,grow");

		treePanel = new JPanel();
		scrollPane.setViewportView(treePanel);

		menuBar = new JMenuBar();
		menuBar.setBorderPainted(false);
		frmDynsOfForm.setJMenuBar(menuBar);

		mntmColor = new JMenuItem("Color");
		mntmColor.setHorizontalAlignment(SwingConstants.LEFT);
		mntmColor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Color newColor = JColorChooser.showDialog(frmDynsOfForm, "Color selector", ccd.getColor());
				if (newColor != null) {
					selectedColor = newColor;
				}
			}
		});
		menuBar.add(mntmColor);

		colorStyleSelect = new JComboBox<String>();
		colorStyleSelect.setToolTipText("Color Style");
		menuBar.add(colorStyleSelect);
		colorStyleSelect
				.setModel(new DefaultComboBoxModel<String>(new String[] { "Node weight", "Stemming", "Solid Color" }));
		colorStyleSelect.setSelectedIndex(2);
		colorStyleSelect.setMaximumRowCount(3);

		imageGen = new JCheckBoxMenuItem("Image Generation");
		imageGen.setSelected(true);
		menuBar.add(imageGen);

		clearPane = new JMenuItem("Clear Pane");
		clearPane.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				treePanel.removeAll();
				frmDynsOfForm.pack();
			}
		});
		menuBar.add(clearPane);

		mntmHelp = new JMenuItem("Help");
		mntmHelp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				helpDialog.setVisible(true);
			}
		});
		mntmHelp.setHorizontalAlignment(SwingConstants.LEFT);
		menuBar.add(mntmHelp);

		mntmAbout = new JMenuItem("About");
		mntmAbout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				aboutDialog.setVisible(true);
			}
		});
		mntmAbout.setHorizontalAlignment(SwingConstants.LEFT);
		menuBar.add(mntmAbout);

		infoDialog = new InfoDialog();
		helpDialog = new HelpDialog();
		aboutDialog = new AboutDialog();
		
		ccd = new JColorChooser(DEFAULT_COLOR);

		frmDynsOfForm.pack();
	}

	private void generateTree() {
		try {
			head = ep.build(exp, printDepth);
			if (imageGen.isSelected()) {
				TreeForTreeLayout<TextInBox> tree = TreeFactory.createTree(head, selectedColor,
						(String) colorStyleSelect.getSelectedItem());
				treeLayout = new TreeLayout<TextInBox>(tree, nodeExtentProvider, configuration);

				treePanel.removeAll();
				treePanel.add((JComponent) new TextInBoxTreePane(treeLayout));
				frmDynsOfForm.pack();
			}

			numApprox.setText(String.format("%.9f", FractionalAnalysis.analyse(head)));

			setAdvancedInfo(head);

			if (!lblError.getText().equals("")) {
				lblError.setText("");
				frmDynsOfForm.pack();
			}
		} catch (InvalidInputException | NoHeadException e) {
			lblError.setText(e.getMessage());
			frmDynsOfForm.pack();
		}

	}

	private void saveFile() {
		SVGForTextInBoxTree generator = new SVGForTextInBoxTree(treeLayout);

		PrintStream fileStream;

		JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
		jfc.setFileFilter(new FileNameExtensionFilter("Scalable Vector Graphics (.svg)", "svg", "*.svg"));

		int returnValue = jfc.showSaveDialog(null);

		if (returnValue == JFileChooser.APPROVE_OPTION) {
			File selectedFile = jfc.getSelectedFile();
			try {
				String filePath = selectedFile.getAbsolutePath();
				if (!filePath.endsWith(".svg")) {
					filePath += ".svg";
				}
				fileStream = new PrintStream(filePath);
				fileStream.println(generator.getSVG());
				fileStream.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	private void setAdvancedInfo(Mark head) {

		infoDialog.setBracketText(head.bracketPrint());
		infoDialog.setDepthText(head.print(printDepth));

		int[] depthTallies = BasicAnalysis.getDepthTallies(head);
		int[] gnomon = BasicAnalysis.getGnomon(depthTallies);
		int[] gnomonInterval = BasicAnalysis.getGnomon(gnomon);

		infoDialog.setTally(depthTallies);
		infoDialog.setGnomon(gnomon);
		infoDialog.setGInterval(gnomonInterval);
		infoDialog.set3rdDeriv(BasicAnalysis.getGnomon(gnomonInterval));
	}



}
