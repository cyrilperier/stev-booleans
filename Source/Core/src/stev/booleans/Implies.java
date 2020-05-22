package stev.booleans;

public class Implies extends BinaryConnective 
{
	public Implies(/*@ non_null @*/ BooleanFormula left, /*@ non_null @*/ BooleanFormula right)
	{
		super(left, right);
	}
	
	@Override
	public boolean evaluate(Valuation v)
	{
		return !m_left.evaluate(v) || m_right.evaluate(v);
	}
	
	@Override
	public boolean isCnf()
	{
		return false;
	}
	
	@Override
	protected boolean isClause()
	{
		return false;
	}
	
	@Override
	protected boolean isAtom()
	{
		return false;
	}

	@Override
	public String toString()
	{
		return "(" + m_left + " -> " + m_right + ")";
	}
	
	@Override
	protected Implies pushNegations()
	{
		return new Implies(m_left.pushNegations(), m_right.pushNegations());
	}
	
	@Override
	protected Implies flatten()
	{
		return new Implies(m_left.flatten(), m_right.flatten());
	}
	
	@Override
	protected Or keepAndOrNot()
	{
		return new Or(new Not(m_left.keepAndOrNot()), m_right.keepAndOrNot());
	}

	@Override
	public int[][] getClauses()
	{
		throw new BooleanFormulaException("Formula is not in CNF");
	}
}
