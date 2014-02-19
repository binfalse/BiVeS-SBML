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
public class SBMLInitialAssignment
	extends SBMLSBase
	implements DiffReporter
{
	private SBMLSBase symbol;
	private MathML math;
	
	/**
	 * @param documentNode
	 * @param sbmlDocument
	 * @throws BivesSBMLParseException
	 */
	public SBMLInitialAssignment (DocumentNode documentNode,
		SBMLModel sbmlModel) throws BivesSBMLParseException
	{
		super (documentNode, sbmlModel);
		
		String tmp = documentNode.getAttribute ("symbol");
		symbol = sbmlModel.getCompartment (tmp);
		if (symbol == null)
			symbol = sbmlModel.getSpecies (tmp);
		if (symbol == null)
			symbol = sbmlModel.getParameter (tmp);
		if (symbol == null)
			symbol = sbmlModel.getSpeciesReference (tmp);
		if (symbol == null)
			throw new BivesSBMLParseException ("symbol "+tmp+" of initial assignment unmappable.");
		

		List<TreeNode> maths = documentNode.getChildrenWithTag ("math");
		if (maths.size () != 1)
			throw new BivesSBMLParseException ("initial assignment has "+maths.size ()+" math elements. (expected exactly one element)");
		math = new MathML ((DocumentNode) maths.get (0));
	}
	
	public SBMLSBase getSymbol ()
	{
		return symbol;
	}
	
	public MathML getMath ()
	{
		return math;
	}
	
	@Override
	public MarkupElement reportMofification (SimpleConnectionManager conMgmt, DiffReporter docA, DiffReporter docB)
	{
		SBMLInitialAssignment a = (SBMLInitialAssignment) docA;
		SBMLInitialAssignment b = (SBMLInitialAssignment) docB;
		if (a.getDocumentNode ().getModification () == 0 && b.getDocumentNode ().getModification () == 0)
			return null;
		
		String idA = SBMLModel.getSidName (a.symbol), idB = SBMLModel.getSidName (b.symbol);
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
		MarkupElement me = new MarkupElement (MarkupDocument.insert (SBMLModel.getSidName (symbol)));
		me.addValue (MarkupDocument.insert ("inserted"));
		return me;
	}
	
	@Override
	public MarkupElement reportDelete ()
	{
		MarkupElement me = new MarkupElement (MarkupDocument.delete (SBMLModel.getSidName (symbol)));
		me.addValue (MarkupDocument.delete ("deleted"));
		return me;
	}
}
