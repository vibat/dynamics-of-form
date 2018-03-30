/**
 * 
 */
package dof;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;

import dof.analysis.FractionalAnalysis;
import dof.exceptions.InvalidInputException;
import dof.exceptions.NoHeadException;
import dof.exceptions.ShallowReEntryException;
import visualise.Imagify;

/**
 * @author ASUS
 * The class used to parse input from the standard LoF form and build the associated structure
 */
public class ExpressionParser {
	
	private Scanner in;
	/*A character queue to determine whether the input expression is balanced*/
	private LinkedList<String> charQ;
	/*The depth at which we are printing to*/
	private int printDepth;
	public static int DEFAULT_DEPTH = 5;
	/*Whether image generation is turned on*/
	private boolean imagify = false;	
	/*A hashmap of all the marks expressed with numerals, used to determine whether they are syntactically correct*/
	private HashMap<Integer, Mark> deepHeads = new HashMap<Integer,Mark>();
	private HashMap<Integer, Integer> deepHeadCounts = new HashMap<Integer,Integer>();
	
	public ExpressionParser() {
		printDepth = DEFAULT_DEPTH;
		in = new Scanner(System.in);
		charQ = new LinkedList<String>();
	}
	
	/**
	 * Take input, return the head of given expression
	 * This functionality has been superseded by the {@link dof.gui.DoFGUI}
	 * @return The created DoF structure head
	 */
	public Mark read() {
		
		System.out.println("Waiting for input (q to quit):");
		
		while (in.hasNextLine()) {
			String input = in.nextLine();
			if (input.equals("")) { continue; }
			if (input.equals("Q") || input.equals("q")) { 
				System.out.println("Quitting...");
				System.exit(0);
			}
			
			if (input.equals("I") || input.equals("i")) { 
				imagify = !imagify;
				System.out.println("Image generation turned " + (imagify ? "on" : "off"));
				continue;
			}
			if (!isValidExpression(input)) {
				System.out.println("Bad input, try again");
				continue;
			}
			
			System.out.println("Select your depth:");
			
			while (in.hasNextLine()) {
				if (in.hasNextInt()) { 
					printDepth = in.nextInt();
					System.out.println("Building to depth " + printDepth);
					try {
						return build(input, printDepth);
					} catch (InvalidInputException | NoHeadException | ShallowReEntryException e) {
						return null;
					}
				}
				else {in.nextLine();}
			}
		
		}
		return null;	
	}
	
	/** Check the validity of the given expression
	 * @param s Inputed string
	 * @return isValid
	 */
	public boolean isValidExpression(String s) {
		
		charQ.clear();
		
		int currentNumber = 0; 
		boolean decreasing = false;

		for (String c : s.split("")) {
			try {
				int num = Integer.parseInt(c); 
				
				if (num <= 0) return false;
				
				if (num == currentNumber) {
					// we are reentering current mark
					continue;
				}
				else if ((num == currentNumber + 1) && decreasing == false) {
					currentNumber++;
					continue;
				}
				else if (num == currentNumber - 1) {
					currentNumber--;
					decreasing = true;
					continue;
				}
			} catch (NumberFormatException e) {
				// not a number
				if (c.equals("[") || c.equals("(")) {
					charQ.push(c);
				}
				else if (c.equals("]") && charQ.peek() != null && charQ.peek().equals("[")) {
					charQ.pop();
				}
				else if (c.equals(")") && charQ.peek() != null && charQ.peek().equals("(")) {
					charQ.pop();
				}
				else { return false; }
			}
		}
		
		if (!charQ.isEmpty()) { return false; }
		
		return true;
	}
	
	/**
	 * Build the mark structure using the specified and valid expression
	 * @param expression The expression in standard LoF computer notation
	 * @param depth The maximum depth we are building to
	 * @return The head of the built mark structure
	 * @throws InvalidInputException There is a syntax error
	 * @throws NoHeadException There is no current head to continue building the expression
	 */
	public Mark build(String expression, int depth) throws InvalidInputException, NoHeadException, ShallowReEntryException {
		
		// rid exp of whitespace
		expression = expression.replaceAll("\\s", "");
		
		if (!isValidExpression(expression)) {
			throw new InvalidInputException();
		}
		
		Mark head = null;
		Mark current = head;
		Mark newMark = null;
		
		int currentDeepReEntry = 0;
		
		// clear the hashtable of deep re-entry heads
		deepHeads.clear();
		deepHeadCounts.clear();
		getDeepHeadCounts(expression);
		
		for (String c: expression.split("")) {
			if (head != null && current == null) {
				throw new NoHeadException();
			}
			try {
				int markNumber = Integer.parseInt(c);
				
				deepHeadCounts.put(markNumber, deepHeadCounts.get(markNumber) - 1);
				
				if (head == null) {
					assert(markNumber == 1);
					head = new Mark(depth);
					current = head;			
					currentDeepReEntry++;
					deepHeads.put(markNumber, current);
				}
				else if (markNumber <= currentDeepReEntry) {
					// adding a deep re-entry
					Mark marker = deepHeads.get(markNumber);
					
					if (current.depth < marker.depth) {
						// we're trying to add a target to a shallower space
						throw new ShallowReEntryException();
					}
					marker.addTarget(current);
					if (deepHeadCounts.get(markNumber) == 0) { current = current.parent; }
				}
				else { // markNumber == currentDeepReEntry + 1 
					// we are making a new mark 
					newMark = new Mark(current,depth);
					current.children.add(newMark);
					current = newMark;
					currentDeepReEntry++;
					deepHeads.put(markNumber, current);
				}
			} catch (NumberFormatException e) {
				// not a number, that's OK
				if (head == null) {
					head = new Mark(depth);
					current = head;
				}
				else if (c.equals("(") || c.equals("[")) {
					newMark = new Mark(current,depth);
					current.children.add(newMark);
					current = newMark;
				}
				else if (c.equals(")")) {
					current = current.parent;
				}
				else if (c.equals("]")) {
					current.addTarget(current);
					current = current.parent;
				}
			}	
		}
		
		// Re-enter all the queued marks that have been created
		head.doReEntry();
		
		return head;
	}

	/**
	 * Console level interaction, superseded by the {@link dof.gui.DoFGUI}
	 */
	public void fullReadAndPrint() {
		
		while (true) {
			Mark m = read();
			System.out.println(m.bracketPrint());
			System.out.println("Numerical approximation: " + FractionalAnalysis.analyse(m));
			m.print(printDepth);
			if (imagify) {
				Imagify.visualise(m);
			}
		}

		
	}
	
	private void getDeepHeadCounts(String expression) {
		for (String c: expression.split("")) {
			try {
				int i = Integer.parseInt(c);
				if (!deepHeadCounts.containsKey(i)) { deepHeadCounts.put(i, 1); }
				else { 
					int count  = deepHeadCounts.get(i);
					deepHeadCounts.put(i, count + 1); 
					}
			} catch (NumberFormatException e) {
				continue;
			}
		}
	}
	
}
