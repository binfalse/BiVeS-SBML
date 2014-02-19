/**
 * 
 */
package de.unirostock.sems.bives.sbml.parser;


import java.util.ArrayList;
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
 * @author Martin Scharm
 *
 */
public class SBMLConstraint
	extends SBMLSBase
	implements DiffReporter
{
	private MathML math;
	private Xhtml message;
	
	/**
	 * @param documentNode
	 * @param sbmlDocument
	 * @throws BivesSBMLParseException
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
			List<TreeNode> kids = root.getChildren ();
			for (TreeNode n : kids)
				message.addXhtml (n);
		}
		
	}
	
	public MathML getMath ()
	{
		return math;
	}
	
	public Xhtml getMessage ()
	{
		return message;
	}

	@Override
	public MarkupElement reportMofification (SimpleConnectionManager conMgmt, DiffReporter docA, DiffReporter docB)
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
		return me;
	}
	
	@Override
	public MarkupElement reportInsert ()
	{
		MarkupElement me = new MarkupElement (MarkupDocument.insert ("-"));
		me.addValue (MarkupDocument.insert ("inserted"));
		return me;
	}
	
	@Override
	public MarkupElement reportDelete ()
	{
		MarkupElement me = new MarkupElement (MarkupDocument.delete ("-"));
		me.addValue (MarkupDocument.delete ("deleted"));
		return me;
	}
	
}
