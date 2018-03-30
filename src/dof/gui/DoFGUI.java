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
import java.awt.Toolkit;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.border.LineBorder;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class DoFGUI {

	private JFrame frmDynsOfForm;
	private JPanel treePanel;
	private JPanel optionsPanel;
	private JTextField expression;
	private JTextField depth;
	private JLabel depthLabel;
	private JLabel lblColor;
	private JLabel lblStyle;
	private JLabel lblDisplay;
	private JLabel lblError;
	private JButton saveButton;
	private JButton infoButton;
	private JButton buttonColor;
	private JButton clearPane;
	private JMenuBar menuBar;
	private JMenu mnFile;
	private JMenuItem mntmAbout;
	private JMenuItem mntmHelp;
	private JComboBox<String> modeSelect;
	private JComboBox<String> colorStyleSelect;
	private JCheckBox imageGen;
	private JScrollPane scrollPane;
	private InfoDialog infoDialog;
	private HelpDialog helpDialog;
	private AboutDialog aboutDialog;
	private JColorChooser ccd;

	private final Color DEFAULT_COLOR = Color.cyan;
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
				"[][20.00,left][140:n,grow,fill][30:30.00][40.00:40]", "[][1.00][::30.00px][::30.00px][grow]"));

		JLabel expLabel = new JLabel("Expression");
		expLabel.setHorizontalAlignment(SwingConstants.CENTER);
		frmDynsOfForm.getContentPane().add(expLabel, "cell 0 0 2 1,alignx center,aligny center");

		expression = new JTextField();
		expression.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				exp = expression.getText();

				generateTree();
			}
		});
		expression.setFont(new Font("Tahoma", Font.PLAIN, 17));
		frmDynsOfForm.getContentPane().add(expression, "cell 2 0,growx");
		expression.setColumns(10);

		depthLabel = new JLabel("Depth");
		frmDynsOfForm.getContentPane().add(depthLabel, "cell 3 0,alignx trailing");

		depth = new JTextField();
		depth.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent arg0) {
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
		frmDynsOfForm.getContentPane().add(depth, "cell 4 0,growx");
		depth.setColumns(10);

		lblError = new JLabel("");
		lblError.setForeground(Color.RED);
		frmDynsOfForm.getContentPane().add(lblError, "cell 2 1,alignx center");

		infoButton = new JButton("Analysis");
		infoButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				infoDialog.setVisible(true);
			}
		});

		saveButton = new JButton("Save");
		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (treeLayout != null) {
					saveFile();
				}
			}
		});
		frmDynsOfForm.getContentPane().add(saveButton, "cell 1 2 1 2,growx,aligny center");

		optionsPanel = new JPanel();
		optionsPanel.setBorder(new LineBorder(new Color(0, 0, 0)));
		frmDynsOfForm.getContentPane().add(optionsPanel, "cell 2 2 1 2,grow");
		optionsPanel.setLayout(
				new MigLayout("", "[131px,grow,fill][119px,grow,fill][98px,grow,fill][114px,grow,fill]", "[25px][]"));

		lblColor = new JLabel("Color Selection");
		lblColor.setHorizontalAlignment(SwingConstants.CENTER);
		optionsPanel.add(lblColor, "cell 1 0,alignx center");

		lblStyle = new JLabel("Coloring Style");
		lblStyle.setHorizontalAlignment(SwingConstants.CENTER);
		optionsPanel.add(lblStyle, "cell 2 0,alignx center");

		lblDisplay = new JLabel("Display Type");
		lblDisplay.setHorizontalAlignment(SwingConstants.CENTER);
		optionsPanel.add(lblDisplay, "cell 3 0,alignx center");

		colorStyleSelect = new JComboBox<String>();
		optionsPanel.add(colorStyleSelect, "cell 2 1,grow");
		colorStyleSelect.setToolTipText("Color Style");
		colorStyleSelect.setModel(new DefaultComboBoxModel<String>(new String[] { "Node weight", "Solid Color" }));
		colorStyleSelect.setSelectedIndex(0);
		colorStyleSelect.setMaximumRowCount(2);

		modeSelect = new JComboBox<String>();
		optionsPanel.add(modeSelect, "cell 3 1,grow");
		modeSelect.setModel(new DefaultComboBoxModel<String>(new String[] { "Standard", "Brownian value" }));
		modeSelect.setSelectedIndex(0);
		modeSelect.setMaximumRowCount(2);

		imageGen = new JCheckBox("Display Tree");
		imageGen.setHorizontalAlignment(SwingConstants.CENTER);
		optionsPanel.add(imageGen, "cell 0 0 1 2,grow");
		imageGen.setSelected(true);

		buttonColor = new JButton("");
		buttonColor.setBackground(selectedColor);
		buttonColor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Color newColor = JColorChooser.showDialog(frmDynsOfForm, "Color selector", ccd.getColor());
				if (newColor != null) {
					selectedColor = newColor;
					buttonColor.setBackground(selectedColor);
				}
			}
		});
		optionsPanel.add(buttonColor, "cell 1 1,grow");
		buttonColor.setHorizontalAlignment(SwingConstants.LEFT);
		frmDynsOfForm.getContentPane().add(infoButton, "cell 3 2 2 1,grow");

		clearPane = new JButton("Clear Tree");
		clearPane.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				treePanel.removeAll();
				frmDynsOfForm.pack();
			}
		});
		frmDynsOfForm.getContentPane().add(clearPane, "cell 3 3 2 1,grow");

		scrollPane = new JScrollPane();
		frmDynsOfForm.getContentPane().add(scrollPane, "cell 1 4 4 1,grow");

		treePanel = new JPanel();
		scrollPane.setViewportView(treePanel);

		menuBar = new JMenuBar();
		menuBar.setBorderPainted(false);
		frmDynsOfForm.setJMenuBar(menuBar);
		
		mnFile = new JMenu("File");
		mnFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				helpDialog.setVisible(true);
			}
		});
		menuBar.add(mnFile);
		
		mntmAbout = new JMenuItem("About");
		mntmAbout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				aboutDialog.setVisible(true);
			}
		});
		mnFile.add(mntmAbout);
		
		mntmHelp = new JMenuItem("Help");
		mntmHelp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				helpDialog.setVisible(true);
			}
		});
		mnFile.add(mntmHelp);


		infoDialog = new InfoDialog();
		helpDialog = new HelpDialog();
		aboutDialog = new AboutDialog();

		ccd = new JColorChooser(DEFAULT_COLOR);

		frmDynsOfForm.pack();
	}

	private void generateTree() {
		try {
			String mode = (String) modeSelect.getSelectedItem();
			head = ep.build(exp, printDepth);
			if (imageGen.isSelected()) {
				TreeForTreeLayout<TextInBox> tree = TreeFactory.createTree(head, selectedColor,
						(String) colorStyleSelect.getSelectedItem(), mode);
				treeLayout = new TreeLayout<TextInBox>(tree, nodeExtentProvider, configuration);

				treePanel.removeAll();
				treePanel.add((JComponent) new TextInBoxTreePane(treeLayout));
				frmDynsOfForm.pack();
			}

			setAdvancedInfo(head, mode);

			if (!lblError.getText().equals("")) {
				lblError.setText("");
				frmDynsOfForm.pack();
			}
		} catch (InvalidInputException | NoHeadException | ShallowReEntryException e) {
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

	private void setAdvancedInfo(Mark head, String selectedMode) {

		infoDialog.setBracketText(head.bracketPrint());
		infoDialog.setDepthText(head.print(printDepth));
		infoDialog.setCFraction(String.format("%.9f", FractionalAnalysis.analyse(head)));

		int[] depthTallies = BasicAnalysis.getDepthTallies(head);
		int[] gnomon = BasicAnalysis.getGnomon(depthTallies);
		int[] gnomonInterval = BasicAnalysis.getGnomon(gnomon);
		int[] thirdDeriv = BasicAnalysis.getGnomon(gnomonInterval);

		infoDialog.setInfo(depthTallies, gnomon, gnomonInterval, thirdDeriv);

		if (selectedMode.equals("Brownian value")) {
			infoDialog.setOFraction(String.format("%.9f", FractionalAnalysis.analyseO(head)));
		} else
			infoDialog.setOFraction("");
	}

}
