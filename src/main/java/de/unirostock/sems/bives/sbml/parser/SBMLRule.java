/**
 * 
 */
package de.unirostock.sems.bives.sbml.parser;


import java.util.List;

import de.unirostock.sems.bives.algorithm.DiffReporter;
import de.unirostock.sems.bives.ds.MathML;
import de.unirostock.sems.bives.sbml.exception.BivesSBMLParseException;
import de.unirostock.sems.xmlutils.ds.DocumentNode;
import de.unirostock.sems.xmlutils.ds.TreeNode;


/**
 * The abstract Class SBMLRule.
 * In SBML, Rules provide additional ways to define the values of variables in a model, their relationships, and the dynamical behaviors of those variables. 
 *
 * @author Martin Scharm
 */
public abstract class SBMLRule
	extends SBMLSBase
	implements DiffReporter
{
	
	/** The flag ASSIGNMENT_RULE. */
	public static final int ASSIGNMENT_RULE = 1;
	
	/** The flag ALGEBRAIC_RULE. */
	public static final int ALGEBRAIC_RULE = 2;
	
	/** The flag RATE_RULE. */
	public static final int RATE_RULE = 3;
	
	/** The math. */
	protected MathML math;
	
	/** The type flag encoding the type of rule. */
	protected int type;
	
	/**
	 * Instantiates a new SBML rule.
	 *
	 * @param documentNode the document node encoding this entity in the corresponding XML tree
	 * @param sbmlModel the SBML model
	 * @throws BivesSBMLParseException the bives sbml parse exception
	 */
	public SBMLRule (DocumentNode documentNode, SBMLModel sbmlModel)
		throws BivesSBMLParseException
	{
		super (documentNode, sbmlModel);
		
		List<TreeNode> maths = documentNode.getChildrenWithTag ("math");
		if (maths.size () != 1)
			throw new BivesSBMLParseException ("initial assignment has "+maths.size ()+" math elements. (expected exactly one element)");
		math = new MathML ((DocumentNode) maths.get (0));
	}
	
	/**
	 * Gets the math.
	 *
	 * @return the math
	 */
	public MathML getMath ()
	{
		return math;
	}
	
	/**
	 * Resolves a  variable.
	 *
	 * @param ref the reference name
	 * @return the SBML SBase node of this variable
	 * @throws BivesSBMLParseException the bives sbml parse exception
	 */
	protected final SBMLSBase resolveVariable (String ref) throws BivesSBMLParseException
	{
		SBMLSBase var = sbmlModel.resolveSymbole (ref);
		if (var == null)
			throw new BivesSBMLParseException ("variable "+ref+" of rule unmappable.");
		return var;
	}
	
	/**
	 * Gets the rule type. (see static flags)
	 *
	 * @return the rule type
	 */
	public int getRuleType ()
	{
		return type;
	}
}
