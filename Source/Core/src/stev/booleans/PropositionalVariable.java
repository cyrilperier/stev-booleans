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
	public int[][] getClauses()
	{
		Map<String,Integer> var_dict = getVariablesMap();
		int[] clause = toClause(var_dict);
		int[][] clauses = new int[1][];
		clauses[0] = clause;
		return clauses;
	}
	
	/**
	 * Gets the DIMACS clause associated to this formula
	 * @return The DIMACS clause in the form of an array of integers
	 */
	protected int[] toClause(Map<String,Integer> var_dict)
	{
		int index = var_dict.get(m_variableName);
		return new int[] {index};
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
}
