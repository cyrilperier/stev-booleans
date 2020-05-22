package stev.booleans;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class And extends NaryConnective
{
	public And(/*@ non_null @*/ List<BooleanFormula> operands)
	{
		super(operands);
	}
	
	public And(/*@ non_null @*/ BooleanFormula ... operands)
	{
		super(operands);
	}

	@Override
	public boolean evaluate(/*@ non_null @*/ Valuation v)
	{
		for (BooleanFormula bf : m_operands)
		{
			if (!bf.evaluate(v))
			{
				return false;
			}
		}
		return true;
	}
	
	@Override
	public int[][] getClauses()
	{
		Map<String,Integer> var_dict = getVariablesMap();
		int[][] clauses = new int[m_operands.size()][];
		for (int i = 0; i < m_operands.size(); i++)
		{
			BooleanFormula bf = m_operands.get(i);
			if (!(bf instanceof Or))
			{
				throw new BooleanFormulaException("Formula is not in CNF");
			}
			Or o = (Or) bf;
			clauses[i] = o.toClause(var_dict);
		}
		return clauses;
	}
	
	@Override
	public boolean isCnf()
	{
		for (BooleanFormula op : m_operands)
		{
			if (!op.isClause())
			{
				return false;
			}
		}
		return true;
	}
	
	@Override
	public boolean isClause()
	{
		return false;
	}
	
	@Override
	public boolean isAtom()
	{
		return false;
	}
	
	@Override
	public String toString()
	{
		StringBuilder out = new StringBuilder();
		out.append("(");
		for (int i = 0; i < m_operands.size(); i++)
		{
			if (i > 0)
			{
				out.append(" & ");
			}
			out.append(m_operands.get(i).toString());
		}
		out.append(")");
		return out.toString();
	}
	
	@Override
	protected And pushNegations()
	{
		List<BooleanFormula> new_list = new ArrayList<BooleanFormula>(m_operands.size());
		for (BooleanFormula f : m_operands)
		{
			new_list.add(f.pushNegations());
		}
		return new And(new_list);
	}
	
	@Override
	protected And keepAndOrNot()
	{
		List<BooleanFormula> new_list = new ArrayList<BooleanFormula>(m_operands.size());
		for (BooleanFormula f : m_operands)
		{
			new_list.add(f.keepAndOrNot());
		}
		return new And(new_list);
	}
	
	@Override
	protected BooleanFormula flatten()
	{
		List<BooleanFormula> new_list = new ArrayList<BooleanFormula>();
		for (BooleanFormula f : m_operands)
		{
			BooleanFormula flattened = f.flatten();
			if (flattened instanceof And)
			{
				And af = (And) flattened;
				new_list.addAll(af.m_operands);
			}
			else
			{
				new_list.add(flattened);
			}
		}
		if (new_list.size() == 1)
		{
			return new_list.get(0);
		}
		return new And(new_list);
	}
	
	@Override
	protected And distributeAndOr()
	{
		
		List<BooleanFormula> new_list = new ArrayList<BooleanFormula>(m_operands.size());
		for (BooleanFormula f : m_operands)
		{
			new_list.add(f.distributeAndOr());
		}
		return new And(new_list);
	}
}
