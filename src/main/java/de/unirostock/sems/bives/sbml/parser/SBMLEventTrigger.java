/**
 * 
 */
package de.unirostock.sems.bives.sbml.parser;


import java.util.ArrayList;
import java.util.List;

import de.unirostock.sems.bives.algorithm.SimpleConnectionManager;
import de.unirostock.sems.bives.ds.MathML;
import de.unirostock.sems.bives.markup.MarkupDocument;
import de.unirostock.sems.bives.markup.MarkupElement;
import de.unirostock.sems.bives.sbml.exception.BivesSBMLParseException;
import de.unirostock.sems.bives.tools.BivesTools;
import de.unirostock.sems.xmlutils.ds.DocumentNode;
import de.unirostock.sems.xmlutils.ds.TreeNode;


/**
 * @author Martin Scharm
 *
 */
public class SBMLEventTrigger
	extends SBMLSBase
{
	private MathML math;
	private Boolean initialValue;
	private Boolean persistent;
	
	/**
	 * @param documentNode
	 * @param sbmlDocument
	 * @throws BivesSBMLParseException
	 */
	public SBMLEventTrigger (DocumentNode documentNode, SBMLModel sbmlModel)
		throws BivesSBMLParseException
	{
		super (documentNode, sbmlModel);
		
		List<TreeNode> maths = documentNode.getChildrenWithTag ("math");
		if (maths.size () != 1)
			throw new BivesSBMLParseException ("event trigger has "+maths.size ()+" math elements. (expected exactly one element)");
		math = new MathML ((DocumentNode) maths.get (0));
		

		if (documentNode.getAttribute ("initialValue") != null)
		{
			try
			{
				initialValue = Boolean.parseBoolean (documentNode.getAttribute ("initialValue"));
			}
			catch (Exception e)
			{
				throw new BivesSBMLParseException ("initialValue of event trigger of unexpected format: " + documentNode.getAttribute ("initialValue"));
			}
		}
		else
			initialValue = null; // level <= 2
		
		if (documentNode.getAttribute ("persistent") != null)
		{
			try
			{
				persistent = Boolean.parseBoolean (documentNode.getAttribute ("persistent"));
			}
			catch (Exception e)
			{
				throw new BivesSBMLParseException ("persistent of event trigger of unexpected format: " + documentNode.getAttribute ("persistent"));
			}
		}
		else
			persistent = null; // level <= 2
	}
	
	public MathML getMath ()
	{
		return math;
	}

	public void reportMofification (SimpleConnectionManager conMgmt, SBMLEventTrigger a, SBMLEventTrigger b, MarkupElement me)
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

	public void reportInsert (MarkupElement me)
	{
		BivesTools.genMathMarkupStats (null, math.getDocumentNode (), me);
	}

	public void reportDelete (MarkupElement me)
	{
		BivesTools.genMathMarkupStats (math.getDocumentNode (), null, me);
	}
	
}
