package stev.booleans;

import java.util.HashMap;
import java.util.Map;

public abstract class BooleanFormula
{
	/**
	 * Creates a new boolean formula
	 */
	public BooleanFormula()
	{
		super();
	}
	
	/**
	 * Gets the value of the formula
	 * @param v The valuation used to give values to
	 * variables
	 * @return The value of the formula
	 */
	public abstract boolean evaluate(Valuation v);
	
	/**
	 * Converts the formula into conjunctive normal form (CNF).
	 * @return A new formula equivalent to the current one, but in
	 * CNF.
	 */
	public final BooleanFormula toCnf()
	{
		BooleanFormula bf1 = keepAndOrNot();
		BooleanFormula bf2 = bf1.pushNegations();
		BooleanFormula bf3 = bf2.distributeAndOr();
		return bf3.flatten();
	}
	
	/**
	 * Gets the mapping between variables and integers representing each
	 * of them in a formula
	 * @return The mapping
	 */
	/*@ non_null @*/ public final Map<String,Integer> getVariablesMap()
	{
		Map<String,Integer> map = new HashMap<String,Integer>();
		setVariablesMap(map);
		return map;
	}
	
	/**
	 * Recursively populates the map between variables and integers
	 * representing each of them in a formula
	 * @param map The map
	 */
	protected abstract void setVariablesMap(/*@ non_null @*/ Map<String,Integer> map);
	
	/**
	 * Determines if the current formula is in conjunctive normal form
	 * (CNF).
	 * @return <tt>true</tt> if the formula is in CNF, <tt>false</tt>
	 * otherwise
	 */
	public abstract boolean isCnf();
	
	/**
	 * Checks if the current operator is a clause in conjunctive normal
	 * form (CNF).
	 * @return <tt>true</tt> if it is a clause, <tt>false</tt> otherwise
	 */
	protected abstract boolean isClause();
	
	/**
	 * Gets an array of clauses in DIMACS format for this formula
	 * @return The array of clauses
	 */
	public abstract int[][] getClauses();
	
	/**
	 * Checks if the current operator is an atom.
	 * @return <tt>true</tt> if it is an atom, <tt>false</tt> otherwise
	 */
	protected abstract boolean isAtom();
	
	/**
	 * Pushes negations on atoms
	 * @return A boolean formula with negations pushed on atoms
	 */
	/*@ non_null @*/ protected abstract BooleanFormula pushNegations();
	
	/**
	 * Transforms the formula to keep only and, or and not as
	 * connectives. 
	 * @return A transformed Boolean formula
	 */
	/*@ non_null @*/ protected abstract BooleanFormula keepAndOrNot();
	
	/**
	 * Applies distributive law where a disjunction occurs over
	 * a conjunction.
	 * @return A new formula where this has been applied recursively
	 */
	/*@ non_null @*/ protected abstract BooleanFormula distributeAndOr();
	
	/**
	 * Flattens a formula.
	 * @return The flattened formula
	 */
	/*@ non_null @*/ protected abstract BooleanFormula flatten();
}
