/**
 * 
 */
package de.unirostock.sems.bives.sbml.parser;

import de.unirostock.sems.bives.algorithm.ClearConnectionManager;
import de.unirostock.sems.bives.ds.DiffReporter;
import de.unirostock.sems.bives.markup.MarkupDocument;
import de.unirostock.sems.bives.markup.MarkupElement;
import de.unirostock.sems.bives.sbml.exception.BivesSBMLParseException;
import de.unirostock.sems.bives.tools.BivesTools;
import de.unirostock.sems.xmltools.ds.DocumentNode;


/**
 * @author Martin Scharm
 *
 */
public class SBMLRateRule
	extends SBMLRule
{
	private SBMLSBase variable;
	
	/**
	 * @param documentNode
	 * @param sbmlDocument
	 * @throws BivesSBMLParseException
	 */
	public SBMLRateRule (DocumentNode documentNode, SBMLModel sbmlModel)
		throws BivesSBMLParseException
	{
		super (documentNode, sbmlModel);
		type = SBMLRule.RATE_RULE;
		if (documentNode.getAttribute ("variable") == null)
			throw new BivesSBMLParseException ("rate rule doesn't define variable");
		variable = resolvVariable (documentNode.getAttribute ("variable"));
		if (variable == null)
			throw new BivesSBMLParseException ("cannot map varibale in rate rule: " + documentNode.getAttribute ("variable"));
	}
	
	public SBMLSBase getVariable ()
	{
		return variable;
	}

	@Override
	public MarkupElement reportMofification (ClearConnectionManager conMgmt, DiffReporter docA, DiffReporter docB, MarkupDocument markupDocument)
	{
		SBMLRateRule a = (SBMLRateRule) docA;
		SBMLRateRule b = (SBMLRateRule) docB;
		if (a.getDocumentNode ().getModification () == 0 && b.getDocumentNode ().getModification () == 0)
			return null;

		String idA = SBMLModel.getSidName (a.variable), idB = SBMLModel.getSidName (b.variable);
		MarkupElement me = null;
		if (idA.equals (idB))
			me = new MarkupElement ("RateRule for " + idA);
		else
			me = new MarkupElement ("RateRule for " + markupDocument.delete (idA)+ " &rarr; " + markupDocument.insert (idB));
		
		BivesTools.genAttributeHtmlStats (a.documentNode, b.documentNode, me, markupDocument);
		BivesTools.genMathHtmlStats (a.math.getDocumentNode (), b.math.getDocumentNode (), me, markupDocument);
		
		return me;
	}
	
	@Override
	public MarkupElement reportInsert (MarkupDocument markupDocument)
	{
		MarkupElement me = new MarkupElement (markupDocument.insert ("RateRule for "+SBMLModel.getSidName (variable)));
		BivesTools.genMathHtmlStats (null, math.getDocumentNode (), me, markupDocument);
		//me.addValue (markupDocument.insert ("inserted"));
		return me;
	}
	
	@Override
	public MarkupElement reportDelete (MarkupDocument markupDocument)
	{
		MarkupElement me = new MarkupElement (markupDocument.delete ("RateRule for "+SBMLModel.getSidName (variable)));
		BivesTools.genMathHtmlStats (math.getDocumentNode (), null, me, markupDocument);
		//me.addValue (markupDocument.delete ("deleted"));
		return me;
	}
	
}
