package stev.booleans;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Or extends NaryConnective
{
	public Or(/*@ non_null @*/ List<BooleanFormula> operands)
	{
		super(operands);
	}
	
	public Or(/*@ non_null @*/ BooleanFormula ... operands)
	{
		super(operands);
	}
	
	@Override
	public boolean evaluate(Valuation v)
	{
		for (BooleanFormula bf : m_operands)
		{
			if (bf.evaluate(v))
			{
				return true;
			}
		}
		return false;
	}
	
	@Override
	public int[][] getClauses()
	{
		throw new BooleanFormulaException("Formula is not in CNF");
	}
	
	/**
	 * Gets the DIMACS clause associated to this formula
	 * @return The DIMACS clause in the form of an array of integers
	 */
	protected int[] toClause(Map<String,Integer> var_dict)
	{
		int len = m_operands.size();
		int[] clause = new int[len];
		for (int i = 0; i < len; i++)
		{
			BooleanFormula bf = m_operands.get(i);
			if (bf instanceof PropositionalVariable)
			{
				PropositionalVariable p = (PropositionalVariable) bf;
				clause[i] = var_dict.get(p.m_variableName);
			}
			else if (bf instanceof Not && ((Not) bf).m_operand instanceof PropositionalVariable)
			{
				PropositionalVariable p = (PropositionalVariable) ((Not) bf).m_operand;
				clause[i] = -var_dict.get(p.m_variableName);
			}
			else
			{
				throw new BooleanFormulaException("Formula is not a clause");
			}
		}
		return clause;
	}
	
	@Override
	public boolean isCnf()
	{
		for (BooleanFormula bf : m_operands)
		{
			if (!bf.isAtom())
			{
				return false;
			}
		}
		return true;
	}
	
	@Override
	protected boolean isClause()
	{
		for (BooleanFormula bf : m_operands)
		{
			if (!bf.isAtom())
			{
				return false;
			}
		}
		return true;
	}
	
	@Override
	protected boolean isAtom()
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
				out.append(" | ");
			}
			out.append(m_operands.get(i).toString());
		}
		out.append(")");
		return out.toString();
	}
	
	@Override
	protected Or pushNegations()
	{
		List<BooleanFormula> new_list = new ArrayList<BooleanFormula>(m_operands.size());
		for (BooleanFormula f : m_operands)
		{
			new_list.add(f.pushNegations());
		}
		return new Or(new_list);
	}
	
	@Override
	protected BooleanFormula flatten()
	{
		List<BooleanFormula> new_list = new ArrayList<BooleanFormula>();
		for (BooleanFormula f : m_operands)
		{
			BooleanFormula flattened = f.flatten();
			if (flattened instanceof Or)
			{
				new_list.addAll(((Or) flattened).m_operands);
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
		return new Or(new_list);
	}
	
	@Override
	protected Or keepAndOrNot()
	{
		List<BooleanFormula> new_list = new ArrayList<BooleanFormula>(m_operands.size());
		for (BooleanFormula f : m_operands)
		{
			new_list.add(f.keepAndOrNot());
		}
		return new Or(new_list);
	}
	
	@Override
	protected BooleanFormula distributeAndOr()
	{
		int num_terms = m_operands.size();
		List<BooleanFormula> new_list = new ArrayList<BooleanFormula>(num_terms);
		int[] sizes = new int[num_terms];
		int[] cursor = new int[num_terms];
		for (int i = 0; i < num_terms; i++)
		{
			cursor[i] = 0;
			BooleanFormula bf = m_operands.get(i).keepAndOrNot();
			new_list.add(bf);
			sizes[i] = 1;
			if (bf instanceof And)
			{
				sizes[i] = ((And) bf).m_operands.size();
			}
		}
		boolean run = true;
		And big_and = new And();
		while (run)
		{
			Or clause = new Or();
			for (int i = 0; i < num_terms; i++)
			{
				BooleanFormula bf = new_list.get(i);
				if (bf instanceof And)
				{
					clause.addOperand(((And) bf).m_operands.get(cursor[i]));
				}
				else
				{
					clause.addOperand(bf);
				}
			}
			big_and.addOperand(clause);
			int reset = 0;
			for (int i = 0; i < num_terms; i++)
			{
				cursor[i]++;
				if (cursor[i] == sizes[i])
				{
					cursor[i] = 0;
					reset++;
				}
				else
				{
					break;
				}
			}
			if (reset == num_terms)
			{
				// We enumerated them all
				run = false;
			}
		}
		return big_and.flatten();
	}
}
