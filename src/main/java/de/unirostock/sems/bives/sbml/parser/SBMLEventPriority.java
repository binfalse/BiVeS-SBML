/**
 * 
 */
package de.unirostock.sems.bives.sbml.parser;


import java.util.List;

import de.unirostock.sems.bives.algorithm.SimpleConnectionManager;
import de.unirostock.sems.bives.ds.MathML;
import de.unirostock.sems.bives.markup.MarkupElement;
import de.unirostock.sems.bives.sbml.exception.BivesSBMLParseException;
import de.unirostock.sems.bives.tools.BivesTools;
import de.unirostock.sems.xmlutils.ds.DocumentNode;
import de.unirostock.sems.xmlutils.ds.TreeNode;


/**
 * The Class SBMLEventPriority. This formula is used to compute a dimensionless numerical value that influences the order in which a simulator is to perform the assignments of two or more events that happen to be executed simultaneously.
 *
 * @author Martin Scharm
 */
public class SBMLEventPriority
	extends SBMLSBase
{
	
	/** The math. */
	private MathML math;
	
	/**
	 * Instantiates a new SBML event priority.
	 *
	 * @param documentNode the document node encoding this entity in the corresponding XML tree
	 * @param sbmlModel the SBML model
	 * @throws BivesSBMLParseException the bives sbml parse exception
	 */
	public SBMLEventPriority (DocumentNode documentNode, SBMLModel sbmlModel)
		throws BivesSBMLParseException
	{
		super (documentNode, sbmlModel);
		
		List<TreeNode> maths = documentNode.getChildrenWithTag ("math");
		if (maths.size () != 1)
			throw new BivesSBMLParseException ("event priority has "+maths.size ()+" math elements. (expected exactly one element)");
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
	 * Report modifications on the entity.
	 *
	 * @param conMgmt the connection manager
	 * @param a the original version
	 * @param b the modified version
	 * @param me the markup element
	 */
	public void reportModification (SimpleConnectionManager conMgmt, SBMLEventPriority a, SBMLEventPriority b, MarkupElement me)
	{
		if (a.getDocumentNode ().getModification () == 0 && b.getDocumentNode ().getModification () == 0)
			return;
		
		BivesTools.genAttributeMarkupStats (a.documentNode, b.documentNode, me);

		if (a.math != null && b.math != null)
			BivesTools.genMathMarkupStats (a.math.getDocumentNode (), b.math.getDocumentNode (), me);
		else if (a.math != null)
			BivesTools.genMathMarkupStats (a.math.getDocumentNode (), null, me);
		else if (b.math != null)
			BivesTools.genMathMarkupStats (null, b.math.getDocumentNode (), me);
	}

	/**
	 * Report insert.
	 *
	 * @param me the me
	 */
	public void reportInsert (MarkupElement me)
	{
		BivesTools.genMathMarkupStats (null, math.getDocumentNode (), me);
	}

	/**
	 * Report delete.
	 *
	 * @param me the me
	 */
	public void reportDelete (MarkupElement me)
	{
		BivesTools.genMathMarkupStats (math.getDocumentNode (), null, me);
	}
	
}
