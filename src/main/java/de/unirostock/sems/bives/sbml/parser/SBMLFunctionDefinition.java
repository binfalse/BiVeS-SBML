/**
 * 
 */
package de.unirostock.sems.bives.sbml.parser;


import java.util.ArrayList;
import java.util.List;

import de.unirostock.sems.bives.algorithm.DiffReporter;
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
public class SBMLFunctionDefinition
extends SBMLGenericIdNameObject
implements DiffReporter
{
	private MathML math;
	
	public SBMLFunctionDefinition (DocumentNode functionDefinition, SBMLModel sbmlModel) throws BivesSBMLParseException
	{
		super (functionDefinition, sbmlModel);
		
		List<TreeNode> maths = functionDefinition.getChildrenWithTag ("math");
		if (maths.size () != 1)
			throw new BivesSBMLParseException ("FunctionDefinition "+id+" has "+maths.size ()+" math elements. (expected exactly one element)");
		math = new MathML ((DocumentNode) maths.get (0));
	}
	
	public MathML getMath ()
	{
		return math;
	}

	@Override
	public MarkupElement reportMofification (SimpleConnectionManager conMgmt, DiffReporter docA, DiffReporter docB)
	{
		SBMLFunctionDefinition a = (SBMLFunctionDefinition) docA;
		SBMLFunctionDefinition b = (SBMLFunctionDefinition) docB;
		if (a.getDocumentNode ().getModification () == 0 && b.getDocumentNode ().getModification () == 0)
			return null;
		
		String idA = a.getNameAndId (), idB = b.getNameAndId ();
		MarkupElement me = null;
		if (idA.equals (idB))
			me = new MarkupElement (idA);
		else
			me = new MarkupElement (MarkupDocument.delete (idA) + " "+MarkupDocument.rightArrow ()+" " + MarkupDocument.insert (idB));

		BivesTools.genAttributeMarkupStats (a.documentNode, b.documentNode, me);
		BivesTools.genMathMarkupStats (a.math.getDocumentNode (), b.math.getDocumentNode (), me);
		
		return me;
	}
	
	@Override
	public MarkupElement reportInsert ()
	{
		MarkupElement me = new MarkupElement (MarkupDocument.insert (getNameAndId ()));
		BivesTools.genMathMarkupStats (null, math.getDocumentNode (), me);
		//me.addValue (markupDocument.insert ("inserted"));
		return me;
	}
	
	@Override
	public MarkupElement reportDelete ()
	{
		MarkupElement me = new MarkupElement (MarkupDocument.delete (getNameAndId ()));
		BivesTools.genMathMarkupStats (math.getDocumentNode (), null, me);
		//me.addValue (markupDocument.delete ("deleted"));
		return me;
	}
}
