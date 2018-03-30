package dof.analysis;

import dof.Mark;

/**
 * A class to determine numerical representation of the structure created 
 */
public class FractionalAnalysis {

	/**
	 * Fractional analysis 
	 * @param head The head mark
	 * @return The numerical approximation of the structure
	 */
	public static double analyse(Mark head) {
		
		return recursiveF(head);

	}
	
	private static double recursiveF(Mark current) {
		
		double childrenTotal = 0.0;
		
		for (Mark child: current.children) {
			childrenTotal += recursiveF(child);
		}
				
		return current.children.isEmpty() ? 1.0 : 1.0/childrenTotal;
	}
	
	/**
	 * Oscillating fractional analysis
	 * @param head The head mark
	 * @return The numerical approximation of the structure
	 */
	public static double analyseO(Mark head) {
		
		int[] tallies = BasicAnalysis.getDepthTallies(head);
		int[] gnomon = BasicAnalysis.getGnomon(tallies);
		double n = 0.0;
		int o = 1;
		for (int i : gnomon) {
			n += o*1.0/i;
			o*= -1;
		}
		return n;
		
	}
	
}
