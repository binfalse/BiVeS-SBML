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
 * The Class SBMLEventTrigger. The exact moment at which the math in the trigger evaluates to "true" during a simulation is taken to be the time point when the Event is triggered.
.
 *
 * @author Martin Scharm
 */
public class SBMLEventTrigger
	extends SBMLSBase
{
	
	/** The math must evaluate to a boolean. */
	private MathML math;
	
	/** The initial value. */
	private Boolean initialValue;
	
	/** The persistent. */
	private Boolean persistent;
	
	/**
	 * Instantiates a new SBML event trigger.
	 *
	 * @param documentNode the document node encoding this entity in the corresponding XML tree
	 * @param sbmlModel the SBML model
	 * @throws BivesSBMLParseException the bives sbml parse exception
	 */
	public SBMLEventTrigger (DocumentNode documentNode, SBMLModel sbmlModel)
		throws BivesSBMLParseException
	{
		super (documentNode, sbmlModel);
		
		List<TreeNode> maths = documentNode.getChildrenWithTag ("math");
		if (maths.size () != 1)
			throw new BivesSBMLParseException ("event trigger has "+maths.size ()+" math elements. (expected exactly one element)");
		math = new MathML ((DocumentNode) maths.get (0));
		

		if (documentNode.getAttributeValue ("initialValue") != null)
		{
			try
			{
				initialValue = Boolean.parseBoolean (documentNode.getAttributeValue ("initialValue"));
			}
			catch (Exception e)
			{
				throw new BivesSBMLParseException ("initialValue of event trigger of unexpected format: " + documentNode.getAttributeValue ("initialValue"));
			}
		}
		else
			initialValue = null; // level <= 2
		
		if (documentNode.getAttributeValue ("persistent") != null)
		{
			try
			{
				persistent = Boolean.parseBoolean (documentNode.getAttributeValue ("persistent"));
			}
			catch (Exception e)
			{
				throw new BivesSBMLParseException ("persistent of event trigger of unexpected format: " + documentNode.getAttributeValue ("persistent"));
			}
		}
		else
			persistent = null; // level <= 2
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
	 * Report modifications.
	 *
	 * @param conMgmt the connection manager
	 * @param a the original version
	 * @param b the modified version
	 * @param me the markup element
	 */
	public void reportModification (SimpleConnectionManager conMgmt, SBMLEventTrigger a, SBMLEventTrigger b, MarkupElement me)
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

		if (!a.flagMetaModifcations (me))
			b.flagMetaModifcations (me);
		
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
