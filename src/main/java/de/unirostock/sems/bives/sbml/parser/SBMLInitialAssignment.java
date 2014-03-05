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
 * The Class SBMLInitialAssignment assigning initial values to entities in a model.
 *
 * @author Martin Scharm
 */
public class SBMLInitialAssignment
	extends SBMLSBase
	implements DiffReporter
{
	
	/** The entity to assign. */
	private SBMLSBase symbol;
	
	/** The math. */
	private MathML math;
	
	/**
	 * Instantiates a new SBML initial assignment.
	 *
	 * @param documentNode the document node encoding this entity in the corresponding XML tree
	 * @param sbmlModel the SBML model
	 * @throws BivesSBMLParseException the bives sbml parse exception
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
	
	/**
	 * Gets the symbol.
	 *
	 * @return the symbol
	 */
	public SBMLSBase getSymbol ()
	{
		return symbol;
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
	
	/* (non-Javadoc)
	 * @see de.unirostock.sems.bives.algorithm.DiffReporter#reportInsert()
	 */
	@Override
	public MarkupElement reportInsert ()
	{
		MarkupElement me = new MarkupElement (MarkupDocument.insert (SBMLModel.getSidName (symbol)));
		me.addValue (MarkupDocument.insert ("inserted"));
		return me;
	}
	
	/* (non-Javadoc)
	 * @see de.unirostock.sems.bives.algorithm.DiffReporter#reportDelete()
	 */
	@Override
	public MarkupElement reportDelete ()
	{
		MarkupElement me = new MarkupElement (MarkupDocument.delete (SBMLModel.getSidName (symbol)));
		me.addValue (MarkupDocument.delete ("deleted"));
		return me;
	}
}
