package dof.analysis;

import dof.Mark;

/**
 * A class to determine numerical representation of the structure created 
 */
public class FractionalAnalysis {

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
}
