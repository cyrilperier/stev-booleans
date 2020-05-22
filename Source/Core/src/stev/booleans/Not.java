package stev.booleans;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Not extends BooleanFormula
{
	/*@ non_null @*/ protected BooleanFormula m_operand;
	
	/**
	 * Creates a new empty negation
	 */
	public Not(/*@ non_null @*/ BooleanFormula f)
	{
		super();
		m_operand = f;
	}
	
	/**
	 * Sets the formula to negate
	 * @param f The formula
	 */
	public void setOperand(/*@ non_null @*/ BooleanFormula f)
	{
		m_operand = f;
	}
	
	@Override
	public int[][] getClauses()
	{
		throw new BooleanFormulaException("Formula is not in CNF");
	}
	
	@Override
	public boolean isCnf()
	{
		return isClause();
	}
	
	@Override
	protected boolean isClause()
	{
		return isAtom();
	}
	
	@Override
	protected boolean isAtom()
	{
		return m_operand instanceof PropositionalVariable;
	}
	
	@Override
	public boolean evaluate(/*@ non_null @*/ Valuation v)
	{
		return !m_operand.evaluate(v);
	}
	
	@Override
	public String toString()
	{
		return "!" + m_operand;
	}
	
	@Override
	protected BooleanFormula pushNegations()
	{
		if (m_operand instanceof Implies)
		{
			Implies imp = (Implies) m_operand;
			return new And(imp.m_left.pushNegations(), new Not(imp.m_right).pushNegations());
		}
		if (m_operand instanceof Equivalence)
		{
			Equivalence imp = (Equivalence) m_operand;
			return new Equivalence(imp.m_left.pushNegations(), new Not(imp.m_right).pushNegations());
		}
		if (m_operand instanceof PropositionalVariable)
		{
			return new Not(m_operand);
		}
		if (m_operand instanceof And)
		{
			And and = (And) m_operand;
			List<BooleanFormula> new_list = new ArrayList<BooleanFormula>(and.m_operands.size());
			for (BooleanFormula f : and.m_operands)
			{
				new_list.add(new Not(f).pushNegations());
			}
			return new Or(new_list);
		}
		if (m_operand instanceof Or)
		{
			Or and = (Or) m_operand;
			List<BooleanFormula> new_list = new ArrayList<BooleanFormula>(and.m_operands.size());
			for (BooleanFormula f : and.m_operands)
			{
				new_list.add(new Not(f).pushNegations());
			}
			return new And(new_list);
		}
		if (m_operand instanceof Not)
		{
			return ((Not) m_operand).m_operand.pushNegations();
		}
		// Not supposed to happen
		throw new BooleanFormulaException("Unknown connective: " + m_operand);
	}
	
	@Override
	protected Not keepAndOrNot()
	{
		return new Not(m_operand.keepAndOrNot());
	}
	
	@Override
	protected Not distributeAndOr()
	{
		// We do nothing, as we assume the formula is already in NNF,
		// so the only operand inside not is a variable
		return this;
	}
	
	@Override
	protected BooleanFormula flatten()
	{
		BooleanFormula operand = m_operand.flatten();
		if (operand instanceof Not)
		{
			return operand;
		}
		return new Not(operand);
	}

	@Override
	protected void setVariablesMap(Map<String, Integer> map)
	{
		m_operand.setVariablesMap(map);
	}
}