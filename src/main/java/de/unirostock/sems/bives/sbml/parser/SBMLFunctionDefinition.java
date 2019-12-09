/**
 * 
 */
package de.unirostock.sems.bives.sbml.parser;


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
 * The Class SBMLFunctionDefinition associates an identifier with a function definition.
 *
 * @author Martin Scharm
 */
public class SBMLFunctionDefinition
extends SBMLGenericIdNameObject
implements DiffReporter
{
	
	/** The math. */
	private MathML math;
	
	/**
	 * Instantiates a new SBML function definition.
	 *
	 * @param functionDefinition the document node encoding this entity in the corresponding XML tree
	 * @param sbmlModel the SBML model
	 * @throws BivesSBMLParseException the bives sbml parse exception
	 */
	public SBMLFunctionDefinition (DocumentNode functionDefinition, SBMLModel sbmlModel) throws BivesSBMLParseException
	{
		super (functionDefinition, sbmlModel);
		
		List<TreeNode> maths = functionDefinition.getChildrenWithTag ("math");
		if (maths.size () != 1)
			throw new BivesSBMLParseException ("FunctionDefinition "+id+" has "+maths.size ()+" math elements. (expected exactly one element)");
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

	/* (non-Javadoc)
	 * @see de.unirostock.sems.bives.algorithm.DiffReporter#reportMofification(de.unirostock.sems.bives.algorithm.SimpleConnectionManager, de.unirostock.sems.bives.algorithm.DiffReporter, de.unirostock.sems.bives.algorithm.DiffReporter)
	 */
	@Override
	public MarkupElement reportModification (SimpleConnectionManager conMgmt, DiffReporter docA, DiffReporter docB)
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

		if (!a.flagMetaModifcations (me))
			b.flagMetaModifcations (me);
		
		return me;
	}
	
	/* (non-Javadoc)
	 * @see de.unirostock.sems.bives.algorithm.DiffReporter#reportInsert()
	 */
	@Override
	public MarkupElement reportInsert ()
	{
		MarkupElement me = new MarkupElement (MarkupDocument.insert (getNameAndId ()));
		BivesTools.genMathMarkupStats (null, math.getDocumentNode (), me);
		//me.addValue (markupDocument.insert ("inserted"));
		return me;
	}
	
	/* (non-Javadoc)
	 * @see de.unirostock.sems.bives.algorithm.DiffReporter#reportDelete()
	 */
	@Override
	public MarkupElement reportDelete ()
	{
		MarkupElement me = new MarkupElement (MarkupDocument.delete (getNameAndId ()));
		BivesTools.genMathMarkupStats (math.getDocumentNode (), null, me);
		//me.addValue (markupDocument.delete ("deleted"));
		return me;
	}
}
