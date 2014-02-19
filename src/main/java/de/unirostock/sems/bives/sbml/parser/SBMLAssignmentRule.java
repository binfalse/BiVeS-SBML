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
 * @author Martin Scharm
 *
 */
public class SBMLAssignmentRule
	extends SBMLRule
{
	private SBMLSBase variable;
	
	/**
	 * @param documentNode
	 * @param sbmlDocument
	 * @throws BivesSBMLParseException
	 */
	public SBMLAssignmentRule (DocumentNode documentNode,
		SBMLModel sbmlModel) throws BivesSBMLParseException
	{
		super (documentNode, sbmlModel);
		type = SBMLRule.ASSIGNMENT_RULE;
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
	public MarkupElement reportMofification (SimpleConnectionManager conMgmt, DiffReporter docA, DiffReporter docB)
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
	
	@Override
	public MarkupElement reportInsert ()
	{
		MarkupElement me = new MarkupElement (MarkupDocument.insert ("AssignmentRule for "+SBMLModel.getSidName (variable)));
		BivesTools.genMathMarkupStats (null, math.getDocumentNode (), me);
		//me.addValue (markupDocument.insert ("inserted"));
		return me;
	}
	
	@Override
	public MarkupElement reportDelete ()
	{
		MarkupElement me = new MarkupElement (MarkupDocument.delete ("AssignmentRule for "+SBMLModel.getSidName (variable)));
		BivesTools.genMathMarkupStats (math.getDocumentNode (), null, me);
		//me.addValue (markupDocument.delete ("deleted"));
		return me;
	}
}
