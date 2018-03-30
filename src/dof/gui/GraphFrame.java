package dof.gui;

import java.awt.EventQueue;

import javax.swing.JDialog;
import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import java.awt.Toolkit;


public class GraphFrame extends JFrame {

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
					GraphFrame dialog = new GraphFrame();
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
	public GraphFrame() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(GraphFrame.class.getResource("/assets/icon/fern.png")));
		setBounds(100, 100, 450, 300);
		

	}
	
	public void setGraph(int[][] data) {
		
		XYDataset dataset = createDataset(data);
		
		JFreeChart chart = ChartFactory.createScatterPlot(
		        "", 
		        "Depth", "Tally", dataset);
		
		// Create Panel
	    ChartPanel panel = new ChartPanel(chart);
	    setContentPane(panel);
	    pack();
		
	}

	private XYDataset createDataset(int[][] data) {
	    XYSeriesCollection dataset = new XYSeriesCollection();
	    
	    int i = 0;
	    for (int[] row : data) {
	    	int j = 0;
	    	XYSeries series = new XYSeries(String.format("%d%s derivative", i, getIntSuffix(i)));
	    	for (int col: row) {
	    		series.add(j,col);
	    		j++;
	    	}
	    	i++;
	    	dataset.addSeries(series);
	    }
	    
	    return dataset;
	}
	
	/**
	 * Thanks to Greg Mattes
	 * https://stackoverflow.com/a/4011232
	 * @param n number
	 * @return number's suffix
	 */
	private String getIntSuffix(final int n) {
	    if (n >= 11 && n <= 13) {
	        return "th";
	    }
	    switch (n % 10) {
	        case 1:  return "st";
	        case 2:  return "nd";
	        case 3:  return "rd";
	        default: return "th";
	    }
	}
	
}
