package dof;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;

class Checker implements Comparator<Mark> {

	@Override
	public int compare(Mark m0, Mark m1) {
		return m0.depth - m1.depth;
	}

}

/**
 * A basic single mark
 */
public class Mark {

	/* The bag of children this mark has */
	public ArrayList<Mark> children;
	/* The parent mark (1 shallower depth) */
	public Mark parent;
	/* The depth of this mark */
	public int depth;
	/* If this mark has been copied, reference the original mark */
	public Mark originalMark;
	/* The maximum depth we go to */
	private int maxDepth;
	/* A priority queue to ensure re-entry occurs in depth order */
	private static LinkedList<Mark> REENTRY_Q = new LinkedList<Mark>();
	/* The re-entry targets */
	private ArrayList<Mark> targets = new ArrayList<Mark>();
	

	public Mark(int maxDepth) {
		children = new ArrayList<Mark>();
		parent = null;
		depth = 0;
		originalMark = null;
		this.maxDepth = maxDepth;
	}

	public Mark(Mark parent, int maxDepth) {
		children = new ArrayList<Mark>();
		this.parent = parent;
		depth = (parent != null) ? parent.depth + 1 : 0;
		originalMark = null;
		this.maxDepth = maxDepth;
	}

	/**
	 * Prints out asterisks representing the number of marks at each depth
	 * 
	 * @param maxDepth
	 *            The maximum depth to print to
	 * @return The accompanying string
	 */
	public String print(int maxDepth) {
		String out = "";
		for (int i = 0; i <= maxDepth; i++) {
			out += i + ":";
			// out.format("%d: ", i);
			out += printRecursive(this, i);
			out += "\n";
		}
		return out;
	}

	private String printRecursive(Mark m, int currDepth) {
		String out = "";
		if (m.depth == currDepth) {
			out += "*";
		}
		for (Mark child : m.children) {
			out += printRecursive(child, currDepth);
		}
		return out;
	}

	private String bracketPrintRecursive(Mark m) {
		String out = "";
		out += "(";
		for (Mark child : m.children) {
			out += bracketPrintRecursive(child);
		}
		out += ")";
		return out;
	}

	/**
	 * Generates the structure of this mark and its children in the recognisable
	 * bracket form
	 * 
	 * @return The bracket notation as a string
	 */
	public String bracketPrint() {
		String out = "";
		out += bracketPrintRecursive(this);
		out += "\n";
		return out;
	}

	/**
	 * Creates a carbon copy of the current mark and assigns the given parent as its
	 * parent
	 * 
	 * @param parent
	 *            The new parent
	 * @return The copy of the current mark
	 */
	public Mark copy(Mark parent) {

		if (parent.depth >= getMaxDepth()) {
			// we are beyond a workable depth
			return null;
		}

		// create the new copy
		Mark newMark = new Mark(parent, getMaxDepth());
		newMark.originalMark = this;

		// copy all the children and add them to your copy
		for (Mark child : children) {
			Mark childCopy = child.copy(newMark);
			if (childCopy != null) {
				newMark.children.add(childCopy);
			}
		}

		// copy the targets of the old mark too
		if (hasTargets()) {
			newMark.assignTargets(targets);

			// the new mark is re-entering, we need to queue it
			REENTRY_Q.push(newMark);
		}

		return newMark;

	}

	/**
	 * A simple re-entry of a mark. Make a copy of yourself and add it as a child of
	 * each target
	 * 
	 */
	public void reEnter() {
		// check that we are re-entering at an acceptable depth
		if (depth >= getMaxDepth()) {
			return;
		}

		// make the copies of yourself to add to the targets
		ArrayList<Mark> copies = makeCopies(targets);

		for (Mark target : targets) {
			assert (target != null);
			// add each copy to the child of the target
			if (target.depth < getMaxDepth()) {
				target.children.add(getCopy(copies, target));
			}
		}
	}

	/**
	 * Recursively find all the new targets for the copied mark by checking their
	 * originals
	 * 
	 * @param oldTargets
	 *            The array of targets assigned to the mark we are copying
	 * @param newTargets
	 *            The array of targets cloned in copy method
	 * @return The targets for the newly copied mark
	 */
	public ArrayList<Mark> findTargets(ArrayList<Mark> oldTargets, ArrayList<Mark> newTargets) {
		for (Mark target : oldTargets) {
			if (target == this.originalMark) {
				newTargets.add(this);
			}
		}

		for (Mark child : children) {
			child.findTargets(oldTargets, newTargets);
		}

		return newTargets;
	}

	/**
	 * Assign the newly created new targets as targets of this newly copied mark
	 * 
	 * @param oldTargets
	 *            The targets of the mark that we are copying
	 */
	public void assignTargets(ArrayList<Mark> oldTargets) {
		targets = findTargets(oldTargets, new ArrayList<Mark>());
		assert (targets != null);
		assert (targets.size() == oldTargets.size());
	}

	/**
	 * Adds a target to the bag of targets and queues it for re-entry
	 * 
	 * @param target The specified mark to be added as a target
	 */
	public void addTarget(Mark target) {
		if (!REENTRY_Q.contains(this)) {
			REENTRY_Q.push(this);
		}
		targets.add(target);
	}

	/**
	 * Once the initial structure is built, completely clear the re-entry queue
	 */
	public void doReEntry() {

		while (!REENTRY_Q.isEmpty()) {
			REENTRY_Q.poll().reEnter();
		}
	}

	/**
	 * Make the copies of yourself required to re-enter. One copy is required for
	 * each target.
	 * 
	 * @param targets
	 *            The targets which we will be copying ourselves into.
	 * @return A bag of copies of this mark.
	 */
	private ArrayList<Mark> makeCopies(ArrayList<Mark> targets) {

		ArrayList<Mark> copies = new ArrayList<Mark>();

		for (Mark target : targets) {
			Mark copy = copy(target);
			copies.add(copy);
		}

		assert (targets.size() == copies.size());

		return copies;

	}

	/**
	 * @param copies
	 *            The bag of copies of ourself
	 * @param target
	 *            The target which we will be copying into
	 * @return The associated mark which will be used to copy to the target
	 */
	private Mark getCopy(ArrayList<Mark> copies, Mark target) {
		for (Mark copy : copies) {
			if (copy.parent == target) {
				return copy;
			}
		}
		return null;
	}

	/**
	 * Determines whether this is a re-entry mark
	 * 
	 * @return Whether the mark is re-entering or not
	 */
	public boolean hasTargets() {
		if (!targets.isEmpty())
			return true;
		return false;
	}

	/**
	 * @return the maxDepth
	 */
	public int getMaxDepth() {
		return maxDepth;
	}	

}
