package stev.booleans.examples;

import java.util.Arrays;
import java.util.Map;

import stev.booleans.And;
import stev.booleans.BooleanFormula;
import stev.booleans.Implies;
import stev.booleans.Not;
import stev.booleans.Or;
import stev.booleans.PropositionalVariable;

public class CnfExample
{

	/**
	 * Create a Boolean formula and convert it to CNF.
	 */
	public static void main(String[] args) 
	{
		// We create the formula p | (!q & (r -> p)) | q
		PropositionalVariable p = new PropositionalVariable("p");
		PropositionalVariable q = new PropositionalVariable("q");
		PropositionalVariable r = new PropositionalVariable("r");
		
		// Subformula: r -> p
		Implies imp = new Implies(r, p);
		
		// Subformula !q
		Not not = new Not(q);
		
		// Subformula !q & (r -> p)
		And and = new And(not, imp);
		
		// The whole formula
		Or big_formula = new Or(p, and, q);
		
		// We can print it
		System.out.println(big_formula);
		
		// Convert this formula to CNF
		BooleanFormula cnf = big_formula.toCnf();
		
		// Let's print it again
		System.out.println(cnf);
		
		// Export this formula as an array of clauses
		int[][] clauses = cnf.getClauses();
		
		// What's in that array? First element corresponds to first clause: [1, -2, 3]
		System.out.println(Arrays.toString(clauses[0]));
		// Second element corresponds to second clause: [1, -3, 1, 2]
		System.out.println(Arrays.toString(clauses[1]));
		
		// What is the integer associated to variable q?
		Map<String,Integer> associations = cnf.getVariablesMap();
		System.out.println("Variable q is associated to number " + associations.get("q"));
	}

}
