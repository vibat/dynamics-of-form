package dof.analysis;

import dof.Mark;

public class BasicAnalysis {
	
	public static int getTotalNodes(Mark current) {
		int total = 1;
		for (Mark child : current.children) {
			total += getTotalNodes(child);
		}
		return total;
	}

	public static int[] getDepthCounts(Mark head) {
		int[] counts = new int[head.getMaxDepth() + 1];
		for (int i = 0; i <= head.getMaxDepth(); i++) {
			counts[i] = 0;
		}

		recursiveDepthCounts(head, counts);

		return counts;
	}

	public static int[] getDepthTallies(Mark head) {

		int[] counts = getDepthCounts(head);
		int[] tallies = counts.clone();

		for (int i = 0; i < counts.length; i++) {
			tallies[i] = 0;
			for (int j = 0; j < i; j++) {
				tallies[i] += counts[j];
			}
		}
		return tallies;
	}

	private static void recursiveDepthCounts(Mark current, int[] counts) {

		counts[current.depth]++;

		for (Mark child : current.children) {
			recursiveDepthCounts(child, counts);
		}
	}
	
	public static int[] getGnomon(int[] array) {
		int[] out = new int[array.length - 1];

		for (int i = 0; i < array.length - 1; i++) {
			out[i] = array[i + 1] - array[i];
		}
		return out;
	}

}
