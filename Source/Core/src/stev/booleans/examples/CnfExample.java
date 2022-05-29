/*
    Simple manipulation of Boolean formulas
    Copyright (C) 2020 Sylvain Hallé
    
    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published
    by the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.
    
    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.
    
    You should have received a copy of the GNU Lesser General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package stev.booleans.examples;

import java.util.Arrays;
import java.util.Map;

import stev.booleans.And;
import stev.booleans.BooleanFormula;
import stev.booleans.Implies;
import stev.booleans.Not;
import stev.booleans.Or;
import stev.booleans.PropositionalVariable;

/**
 * Example showing the manipulation of the various classes provided by 
 * the package.
 * @author Sylvain Hallé
 */
public class CnfExample
{

	/**
	 * Create a Boolean formula and convert it to CNF.
	 */
	public static void main(String[] args) 
	{
		// We create the formula p | (!q & (r -> p)) | (q & s)
		PropositionalVariable p = new PropositionalVariable("l");
		PropositionalVariable q = new PropositionalVariable("c");
		PropositionalVariable r = new PropositionalVariable("n");
		PropositionalVariable s = new PropositionalVariable("s");


		PropositionalVariable l = new PropositionalVariable("l");
		PropositionalVariable c = new PropositionalVariable("c");
		PropositionalVariable n = new PropositionalVariable("n");
		PropositionalVariable bl = new PropositionalVariable("bl");
		PropositionalVariable bc = new PropositionalVariable("bc");

		// Subformula: r -> p
		Implies imp = new Implies(r, p);
		
		// Subformula !q
		Not not = new Not(q);
		
		// Subformula !q & (r -> p)
		And and_1 = new And(not, imp);
		
		// Subformula q & s
		And and_2 = new And(q, s);

		And First1 = new And(l,c,n);
		And First2 = new And();
		
		// The whole formula
		Or big_formula = new Or(p, and_1, and_2);
		
		// We can print it
		System.out.println(big_formula);
		
		// Convert this formula to CNF
		BooleanFormula cnf = BooleanFormula.toCnf(big_formula);
		
		// Let's print it again
		System.out.println(cnf);
		
		// Export this formula as an array of clauses
		int[][] clauses = cnf.getClauses();
		
		// What's in that array? First element corresponds to first clause: [1, -2, 3]
		System.out.println(Arrays.toString(clauses[0]));
		// Second element corresponds to second clause: [1, -3, 4]
		System.out.println(Arrays.toString(clauses[1]));
		
		// What is the integer associated to variable q?
		Map<String,Integer> associations = cnf.getVariablesMap();
		System.out.println("Variable q is associated to number " + associations.get("q"));
	}

}
