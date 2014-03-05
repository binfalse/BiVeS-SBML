/**
 * 
 */
package de.unirostock.sems.bives.sbml.parser;

import de.unirostock.sems.bives.algorithm.DiffReporter;
import de.unirostock.sems.bives.algorithm.SimpleConnectionManager;
import de.unirostock.sems.bives.markup.MarkupDocument;
import de.unirostock.sems.bives.markup.MarkupElement;
import de.unirostock.sems.bives.sbml.exception.BivesSBMLParseException;
import de.unirostock.sems.bives.tools.BivesTools;
import de.unirostock.sems.xmlutils.ds.DocumentNode;


/**
 * The Class SBMLAlgebraicRule used to express equations that are neither assignments of model variables nor rates of change.
 *
 * @author Martin Scharm
 */
public class SBMLAlgebraicRule
	extends SBMLRule
{
	
	/**
	 * Instantiates a new SBML algebraic rule.
	 *
	 * @param documentNode the corresponding document node int the XML tree
	 * @param sbmlModel the SBML model
	 * @throws BivesSBMLParseException the bives sbml parse exception
	 */
	public SBMLAlgebraicRule (DocumentNode documentNode, SBMLModel sbmlModel)
		throws BivesSBMLParseException
	{
		super (documentNode, sbmlModel);
		type = SBMLRule.ALGEBRAIC_RULE;
	}

	/* (non-Javadoc)
	 * @see de.unirostock.sems.bives.algorithm.DiffReporter#reportMofification(de.unirostock.sems.bives.algorithm.SimpleConnectionManager, de.unirostock.sems.bives.algorithm.DiffReporter, de.unirostock.sems.bives.algorithm.DiffReporter)
	 */
	@Override
	public MarkupElement reportModification (SimpleConnectionManager conMgmt, DiffReporter docA, DiffReporter docB)
	{
		SBMLAlgebraicRule a = (SBMLAlgebraicRule) docA;
		SBMLAlgebraicRule b = (SBMLAlgebraicRule) docB;
		if (a.getDocumentNode ().getModification () == 0 && b.getDocumentNode ().getModification () == 0)
			return null;

		MarkupElement me = new MarkupElement ("AlgebraicRule");
		
		BivesTools.genAttributeMarkupStats (a.documentNode, b.documentNode, me);
		BivesTools.genMathMarkupStats (a.math.getDocumentNode (), b.math.getDocumentNode (), me);
		
		return me;
	}
	
	/* (non-Javadoc)
	 * @see de.unirostock.sems.bives.algorithm.DiffReporter#reportInsert()
	 */
	@Override
	public MarkupElement reportInsert ()
	{
		MarkupElement me = new MarkupElement (MarkupDocument.insert ("AlgebraicRule"));
		BivesTools.genAttributeMarkupStats (null, documentNode, me);
		//me.addValue (markupDocument.insert ("inserted"));
		return me;
	}
	
	/* (non-Javadoc)
	 * @see de.unirostock.sems.bives.algorithm.DiffReporter#reportDelete()
	 */
	@Override
	public MarkupElement reportDelete ()
	{
		MarkupElement me = new MarkupElement (MarkupDocument.delete ("AlgebraicRule"));
		BivesTools.genAttributeMarkupStats (documentNode, null, me);
		//me.addValue (markupDocument.delete ("deleted"));
		return me;
	}
	
}
