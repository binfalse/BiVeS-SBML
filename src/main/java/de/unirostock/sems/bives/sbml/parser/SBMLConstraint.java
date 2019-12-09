/**
 * 
 */
package de.unirostock.sems.bives.sbml.parser;


import java.util.List;

import de.unirostock.sems.bives.algorithm.DiffReporter;
import de.unirostock.sems.bives.algorithm.SimpleConnectionManager;
import de.unirostock.sems.bives.ds.MathML;
import de.unirostock.sems.bives.ds.Xhtml;
import de.unirostock.sems.bives.markup.MarkupDocument;
import de.unirostock.sems.bives.markup.MarkupElement;
import de.unirostock.sems.bives.sbml.exception.BivesSBMLParseException;
import de.unirostock.sems.bives.tools.BivesTools;
import de.unirostock.sems.xmlutils.ds.DocumentNode;
import de.unirostock.sems.xmlutils.ds.TreeNode;


/**
 * The Class SBMLConstraint, a mechanism for stating the assumptions under which a model is designed to operate.
 *
 * @author Martin Scharm
 */
public class SBMLConstraint
	extends SBMLSBase
	implements DiffReporter
{
	
	/** The math. */
	private MathML math;
	
	/** The message may be displayed to the user when the condition of the constraint in math evaluates to a value of “false”. */
	private Xhtml message;
	
	/**
	 * Instantiates a new SBML constraint.
	 *
	 * @param documentNode the document node encoding this entity in the corresponding XML tree
	 * @param sbmlModel the SBML model
	 * @throws BivesSBMLParseException the bives sbml parse exception
	 */
	public SBMLConstraint (DocumentNode documentNode, SBMLModel sbmlModel)
		throws BivesSBMLParseException
	{
		super (documentNode, sbmlModel);

		List<TreeNode> nodes = documentNode.getChildrenWithTag ("math");
		if (nodes.size () != 1)
			throw new BivesSBMLParseException ("constraint has "+nodes.size ()+" math elements. (expected exactly one element)");
		math = new MathML ((DocumentNode) nodes.get (0));

		nodes = documentNode.getChildrenWithTag ("message");
		if (nodes.size () > 1)
			throw new BivesSBMLParseException ("constraint has "+nodes.size ()+" message elements. (expected not more than one element)");
		if (nodes.size () == 1)
		{
			message = new Xhtml ();
			DocumentNode root = (DocumentNode) nodes.get (0);
				message.setXhtml (root);
		}
		
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
	
	/**
	 * Gets the message.
	 *
	 * @return the message
	 */
	public Xhtml getMessage ()
	{
		return message;
	}

	/* (non-Javadoc)
	 * @see de.unirostock.sems.bives.algorithm.DiffReporter#reportMofification(de.unirostock.sems.bives.algorithm.SimpleConnectionManager, de.unirostock.sems.bives.algorithm.DiffReporter, de.unirostock.sems.bives.algorithm.DiffReporter)
	 */
	@Override
	public MarkupElement reportModification (SimpleConnectionManager conMgmt, DiffReporter docA, DiffReporter docB)
	{
		SBMLConstraint a = (SBMLConstraint) docA;
		SBMLConstraint b = (SBMLConstraint) docB;
		if (a.getDocumentNode ().getModification () == 0 && b.getDocumentNode ().getModification () == 0)
			return null;
		
		MarkupElement me = new MarkupElement ("-");
		
		BivesTools.genMathMarkupStats (a.math.getDocumentNode (), b.math.getDocumentNode (), me);
		
		BivesTools.genAttributeMarkupStats (a.documentNode, b.documentNode, me);
		
		if (a.message != null && b.message != null)
		{
			String msgA = a.message.toString ();
			String msgB = b.message.toString ();
			
			if (!msgA.equals (msgB))
				me.addValue ("message changed from: " + MarkupDocument.delete (msgA) + " to " + MarkupDocument.insert (msgB));
		}
		else if (a.message != null)
			me.addValue ("message deleted: " + MarkupDocument.delete (a.message.toString ()));
		else if (b.message != null)
			me.addValue ("message inserted: " + MarkupDocument.insert (b.message.toString ()));

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
		MarkupElement me = new MarkupElement (MarkupDocument.insert ("-"));
		me.addValue (MarkupDocument.insert ("inserted"));
		return me;
	}
	
	/* (non-Javadoc)
	 * @see de.unirostock.sems.bives.algorithm.DiffReporter#reportDelete()
	 */
	@Override
	public MarkupElement reportDelete ()
	{
		MarkupElement me = new MarkupElement (MarkupDocument.delete ("-"));
		me.addValue (MarkupDocument.delete ("deleted"));
		return me;
	}
	
}
