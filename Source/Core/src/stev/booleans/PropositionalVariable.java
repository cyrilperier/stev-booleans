package stev.booleans;

import java.util.Map;

public class PropositionalVariable extends BooleanFormula
{
	/**
	 * The name of the propositional variable.
	 */
	/*@ non_null @*/ protected String m_variableName;
	
	/**
	 * Creates a new propositional variable
	 * @param var_name The name of the variable
	 */
	public PropositionalVariable(/*@ non_null @*/ String var_name)
	{
		super();
		m_variableName = var_name;
	}

	@Override
	public boolean evaluate(/*@ non_null @*/ Valuation v)
	{
		if (!v.containsKey(m_variableName))
		{
			throw new BooleanFormulaException("No value defined for variable " + m_variableName);
		}
		return v.get(m_variableName);
	}
	
	@Override
	public boolean isCnf()
	{
		return true;
	}
	
	@Override
	protected boolean isClause()
	{
		return true;
	}
	
	@Override
	protected boolean isAtom()
	{
		return true;
	}
	
	@Override
	protected PropositionalVariable pushNegations()
	{
		return this;
	}
	
	@Override
	public String toString()
	{
		return m_variableName;
	}
	
	@Override
	protected PropositionalVariable keepAndOrNot()
	{
		return this;
	}
	
	@Override
	protected PropositionalVariable distributeAndOr()
	{
		return this;
	}
	
	@Override
	protected PropositionalVariable flatten()
	{
		return this;
	}

	@Override
	protected void setVariablesMap(Map<String, Integer> map)
	{
		if (!map.containsKey(m_variableName))
		{
			int index = map.size() + 1;
			map.put(m_variableName, index);
		}
	}

	@Override
	public int[][] getClauses() 
	{
		throw new BooleanFormulaException("Formula is not in CNF");
	}
}
