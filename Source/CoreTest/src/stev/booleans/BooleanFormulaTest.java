package stev.booleans;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Map;
import java.util.Set;

import org.junit.Test;

import stev.booleans.And;
import stev.booleans.BooleanFormula;
import stev.booleans.BooleanFormulaException;
import stev.booleans.Equivalence;
import stev.booleans.Implies;
import stev.booleans.Not;
import stev.booleans.Or;
import stev.booleans.PropositionalVariable;
import stev.booleans.Valuation;

public class BooleanFormulaTest
{
	public static final PropositionalVariable X = new PropositionalVariable("x");
	
	public static final PropositionalVariable Y = new PropositionalVariable("y");
	
	public static final PropositionalVariable Z = new PropositionalVariable("z");
	
	public static final PropositionalVariable T = new PropositionalVariable("t");
	
	public static final PropositionalVariable U = new PropositionalVariable("u");
	
	public static final PropositionalVariable P = new PropositionalVariable("p");
	
	public static final PropositionalVariable Q = new PropositionalVariable("q");
	
	@Test
	public void testEvaluateVariableExists()
	{
		Valuation v = new Valuation();
		v.put("x", true);
		assertEquals(true, X.evaluate(v));
	}
	
	@Test(expected = BooleanFormulaException.class)
	public void testEvaluateVariableNotExists()
	{
		Valuation v = new Valuation();
		X.evaluate(v);
	}
	
	@Test
	public void testNnfNegationAnd()
	{
		Not n = new Not(new And(X, Y));
		BooleanFormula bf = n.pushNegations();
		assertEquals("(!x | !y)", bf.toString());
	}
	
	@Test
	public void testNnfNegationOr()
	{
		Not n = new Not(new Or(X, Y));
		BooleanFormula bf = n.pushNegations();
		assertEquals("(!x & !y)", bf.toString());
	}
	
	@Test
	public void testNnfNegationImplies()
	{
		Not n = new Not(new Implies(X, Y));
		BooleanFormula bf = n.pushNegations();
		assertEquals("(x & !y)", bf.toString());
	}
	
	@Test
	public void testNnfNegationEquivalence()
	{
		Not n = new Not(new Equivalence(X, Y));
		BooleanFormula bf = n.pushNegations();
		assertEquals("x <-> !y", bf.toString());
	}
	
	@Test
	public void testNnfNegationVariable()
	{
		Not n = new Not(X);
		BooleanFormula bf = n.pushNegations();
		assertEquals("!x", bf.toString());
	}
	
	@Test
	public void testNnfNegationNegation()
	{
		Not n = new Not(new Not(X));
		BooleanFormula bf = n.pushNegations();
		assertEquals("x", bf.toString());
	}
	
	@Test
	public void testDistributeOrVarVar()
	{
		Or or = new Or(X, Y);
		BooleanFormula bf = or.distributeAndOr();
		assertEquals("(x | y)", bf.toString());
	}
	
	@Test
	public void testDistributeOrVarAnd()
	{
		Or or = new Or(X, new And(Y, Z));
		BooleanFormula bf = or.distributeAndOr();
		assertEquals("((x | y) & (x | z))", bf.toString());
	}
	
	@Test
	public void testDistributeOrAndAnd()
	{
		Or or = new Or(new And(X, Y), new And(Z, T));
		BooleanFormula bf = or.distributeAndOr();
		assertEquals("((x | z) & (y | z) & (x | t) & (y | t))", bf.toString());
	}
	
	@Test
	public void testDistributeOrAndVarAnd()
	{
		Or or = new Or(new And(X, Y), P, new And(Z, T));
		BooleanFormula bf = or.distributeAndOr();
		assertEquals("((x | p | z) & (y | p | z) & (x | p | t) & (y | p | t))", bf.toString());
	}
	
	@Test
	public void testDistributeOrAndVarNegAnd()
	{
		Or or = new Or(new And(X, Y), new Not(P), new And(Z, T));
		BooleanFormula bf = or.distributeAndOr();
		assertEquals("((x | !p | z) & (y | !p | z) & (x | !p | t) & (y | !p | t))", bf.toString());
	}
	
	@Test
	public void testCnf1()
	{
		Or or = new Or(new And(X, Y), P, new And(Z, T));
		BooleanFormula bf = or.toCnf();
		assertTrue(bf.isCnf());
		System.out.println(bf);
	}
	
	@Test
	public void testCnf2()
	{
		Or or = new Or(new And(X, Y), P, new And(Z, new Or(T, U)));
		BooleanFormula bf = or.toCnf();
		assertTrue(bf.isCnf());
		System.out.println(bf);
	}
	
	@Test
	public void testCnf3()
	{
		Or or = new Or(new And(X, Y), P, new And(Z, T, U));
		BooleanFormula bf = or.toCnf();
		assertTrue(bf.isCnf());
		System.out.println(bf);
	}
	
	@Test
	public void testVariablesMap()
	{
		Or or = new Or(new And(X, Y), new Not(P), new And(Z, T));
		Map<String,Integer> map = or.getVariablesMap();
		assertEquals(5, map.size());
		Set<String> keys = map.keySet();
		assertTrue(keys.contains(X.m_variableName));
		assertTrue(keys.contains(Y.m_variableName));
		assertTrue(keys.contains(Z.m_variableName));
		assertTrue(keys.contains(T.m_variableName));
		assertTrue(keys.contains(P.m_variableName));
	}
	
	@Test
	public void testToClause()
	{
		Or or = new Or(X, Y, new Not(Z));
		Map<String,Integer> map = or.getVariablesMap();
		int[] clause = or.toClause(map);
		assertEquals(3, clause.length);
		assertEquals(1, clause[0]);
		assertEquals(2, clause[1]);
		assertEquals(-3, clause[2]);
	}
	
	@Test
	public void testGetClauses()
	{
		Or cl1 = new Or(X, Y, new Not(Z));
		Or cl2 = new Or(U, new Not(T), new Not(X), Y);
		And a = new And(cl1, cl2);
		int[][] clauses = a.getClauses();
		int[] i_cl1 = clauses[0];
		int[] i_cl2 = clauses[1];
		assertEquals(3, i_cl1.length);
		assertEquals(4, i_cl2.length);
		assertEquals(1, i_cl1[0]);
		assertEquals(2, i_cl1[1]);
		assertEquals(-3, i_cl1[2]);
		assertEquals(4, i_cl2[0]);
		assertEquals(-5, i_cl2[1]);
		assertEquals(-1, i_cl2[2]);
		assertEquals(2, i_cl2[3]);
	}
}
