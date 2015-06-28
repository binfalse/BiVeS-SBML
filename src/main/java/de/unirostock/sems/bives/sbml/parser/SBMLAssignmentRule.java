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
 * The Class SBMLAssignmentRule used to express equations that set the values of variables.
 *
 * @author Martin Scharm
 */
public class SBMLAssignmentRule
	extends SBMLRule
{
	
	/** The variable that is modified in this rule. */
	private SBMLSBase variable;
	
	/**
	 * Instantiates a new SBML assignment rule.
	 *
	 * @param documentNode the corresponding document node in the XML tree
	 * @param sbmlModel the SBML model
	 * @throws BivesSBMLParseException the bives sbml parse exception
	 */
	public SBMLAssignmentRule (DocumentNode documentNode,
		SBMLModel sbmlModel) throws BivesSBMLParseException
	{
		super (documentNode, sbmlModel);
		type = SBMLRule.ASSIGNMENT_RULE;
		if (documentNode.getAttributeValue ("variable") == null)
			throw new BivesSBMLParseException ("rate rule doesn't define variable");
		variable = resolveVariable (documentNode.getAttributeValue ("variable"));
		if (variable == null)
			throw new BivesSBMLParseException ("cannot map varibale in rate rule: " + documentNode.getAttributeValue ("variable"));
	}
	
	/**
	 * Gets the corresponding variable.
	 *
	 * @return the variable
	 */
	public SBMLSBase getVariable ()
	{
		return variable;
	}

	/* (non-Javadoc)
	 * @see de.unirostock.sems.bives.algorithm.DiffReporter#reportMofification(de.unirostock.sems.bives.algorithm.SimpleConnectionManager, de.unirostock.sems.bives.algorithm.DiffReporter, de.unirostock.sems.bives.algorithm.DiffReporter)
	 */
	@Override
	public MarkupElement reportModification (SimpleConnectionManager conMgmt, DiffReporter docA, DiffReporter docB)
	{
		SBMLAssignmentRule a = (SBMLAssignmentRule) docA;
		SBMLAssignmentRule b = (SBMLAssignmentRule) docB;
		if (a.getDocumentNode ().getModification () == 0 && b.getDocumentNode ().getModification () == 0)
			return null;

		String idA = SBMLModel.getSidName (a.variable), idB = SBMLModel.getSidName (b.variable);
		MarkupElement me = null;
		if (idA.equals (idB))
			me = new MarkupElement ("AssignmentRule for " + idA);
		else
			me = new MarkupElement ("AssignmentRule for " + MarkupDocument.delete (idA)+ " &rarr; " + MarkupDocument.insert (idB));
		
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
		MarkupElement me = new MarkupElement (MarkupDocument.insert ("inserted AssignmentRule for "+SBMLModel.getSidName (variable)));
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
		MarkupElement me = new MarkupElement (MarkupDocument.delete ("deleted AssignmentRule for "+SBMLModel.getSidName (variable)));
		BivesTools.genMathMarkupStats (math.getDocumentNode (), null, me);
		//me.addValue (markupDocument.delete ("deleted"));
		return me;
	}
}
