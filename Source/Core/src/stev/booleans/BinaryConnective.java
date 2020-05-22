package stev.booleans;

import java.util.Map;

public abstract class BinaryConnective extends BooleanFormula
{
	/*@ non_null @*/ protected BooleanFormula m_left;
	
	/*@ non_null @*/ protected BooleanFormula m_right;
	
	/**
	 * Creates a new binary connective and specifies its two
	 * operands.
	 * @param left The left operand
	 * @param right The right operand
	 */
	public BinaryConnective(/*@ non_null @*/ BooleanFormula left, /*@ non_null @*/ BooleanFormula right)
	{
		super();
		m_left = left;
		m_right = right;
	}
	
	/**
	 * Sets the left operand of the connective
	 * @param f The operand
	 */
	public void setLeft(/*@ non_null @*/ BooleanFormula f)
	{
		m_left = f;
	}
	
	/**
	 * Sets the right operand of the connective
	 * @param f The operand
	 */
	public void setRight(/*@ non_null @*/ BooleanFormula f)
	{
		m_right = f;
	}
	
	@Override
	protected BooleanFormula distributeAndOr()
	{
		// Throw an exception: the formula is supposed to be in NNF
		throw new BooleanFormulaException("Operation not supported on this connective");
	}
	
	@Override
	protected void setVariablesMap(Map<String, Integer> map)
	{
		m_left.setVariablesMap(map);
		m_right.setVariablesMap(map);
	}
}
