/**
 * 
 */
package de.unirostock.sems.bives.sbml.parser;

import java.util.Vector;

import de.unirostock.sems.bives.algorithm.ClearConnectionManager;
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
public class SBMLEventDelay
	extends SBMLSBase
{
	private MathML math;
	
	/**
	 * @param documentNode
	 * @param sbmlDocument
	 * @throws BivesSBMLParseException
	 */
	public SBMLEventDelay (DocumentNode documentNode, SBMLModel sbmlModel)
		throws BivesSBMLParseException
	{
		super (documentNode, sbmlModel);
		
		Vector<TreeNode> maths = documentNode.getChildrenWithTag ("math");
		if (maths.size () != 1)
			throw new BivesSBMLParseException ("event trigger has "+maths.size ()+" math elements. (expected exactly one element)");
		math = new MathML ((DocumentNode) maths.elementAt (0));
	}
	
	public MathML getMath ()
	{
		return math;
	}

	public void reportMofification (ClearConnectionManager conMgmt, SBMLEventDelay a, SBMLEventDelay b, MarkupElement me, MarkupDocument markupDocument)
	{
		if (a.getDocumentNode ().getModification () == 0 && b.getDocumentNode ().getModification () == 0)
			return;
		
		BivesTools.genAttributeHtmlStats (a.documentNode, b.documentNode, me, markupDocument);

		if (a.math != null && b.math != null)
			BivesTools.genMathHtmlStats (a.math.getDocumentNode (), b.math.getDocumentNode (), me, markupDocument);
		else if (a.math != null)
			BivesTools.genMathHtmlStats (a.math.getDocumentNode (), null, me, markupDocument);
		else if (b.math != null)
			BivesTools.genMathHtmlStats (null, b.math.getDocumentNode (), me, markupDocument);
		
	}

	public void reportInsert (MarkupElement me, MarkupDocument markupDocument)
	{
		BivesTools.genMathHtmlStats (null, math.getDocumentNode (), me, markupDocument);
	}

	public void reportDelete (MarkupElement me, MarkupDocument markupDocument)
	{
		BivesTools.genMathHtmlStats (math.getDocumentNode (), null, me, markupDocument);
	}
	
}
