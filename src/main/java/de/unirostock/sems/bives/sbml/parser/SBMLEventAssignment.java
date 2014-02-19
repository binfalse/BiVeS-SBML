/**
 * 
 */
package de.unirostock.sems.bives.sbml.parser;


import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.TransformerException;

import de.binfalse.bflog.LOGGER;
import de.unirostock.sems.bives.algorithm.SimpleConnectionManager;
import de.unirostock.sems.bives.ds.MathML;
import de.unirostock.sems.bives.markup.MarkupDocument;
import de.unirostock.sems.bives.markup.MarkupElement;
import de.unirostock.sems.bives.sbml.exception.BivesSBMLParseException;
import de.unirostock.sems.bives.tools.BivesTools;
import de.unirostock.sems.xmlutils.ds.DocumentNode;
import de.unirostock.sems.xmlutils.ds.TreeNode;
import de.unirostock.sems.xmlutils.tools.DocumentTools;


/**
 * @author Martin Scharm
 *
 */
public class SBMLEventAssignment
	extends SBMLSBase
{
	private MathML math;
	private SBMLSBase variable;
	
	/**
	 * @param documentNode
	 * @param sbmlDocument
	 * @throws BivesSBMLParseException
	 */
	public SBMLEventAssignment (DocumentNode documentNode,
		SBMLModel sbmlModel) throws BivesSBMLParseException
	{
		super (documentNode, sbmlModel);
		
		List<TreeNode> maths = documentNode.getChildrenWithTag ("math");
		if (maths.size () != 1)
			throw new BivesSBMLParseException ("event trigger has "+maths.size ()+" math elements. (expected exactly one element)");
		math = new MathML ((DocumentNode) maths.get (0));
		
		variable = resolvVariable (documentNode.getAttribute ("variable"));
	}

	
	protected final SBMLSBase resolvVariable (String ref) throws BivesSBMLParseException
	{
		SBMLSBase var = sbmlModel.getCompartment (ref);
		if (var == null)
			var = sbmlModel.getSpecies (ref);
		if (var == null)
			var = sbmlModel.getParameter (ref);
		if (var == null)
			throw new BivesSBMLParseException ("variable "+ref+" of rule unmappable.");
		return var;
	}
	
	
	public SBMLSBase getVariable ()
	{
		return variable;
	}
	
	public MathML getMath ()
	{
		return math;
	}

	public void reportMofification (SimpleConnectionManager conMgmt, SBMLEventAssignment a, SBMLEventAssignment b, MarkupElement me)
	{
		if (a.getDocumentNode ().getModification () == 0 && b.getDocumentNode ().getModification () == 0)
			return;
		
		String varA = SBMLModel.getSidName (a.variable);
		String varB = SBMLModel.getSidName (b.variable);
		if (varA.equals (varB))
			me.addValue ("for: " + varA);
		else
			me.addValue ("was for: " + MarkupDocument.delete (varA) + " but now for: " + MarkupDocument.insert (varB));

		BivesTools.genMathMarkupStats (a.math.getDocumentNode (), b.math.getDocumentNode (), me);
	}

	public void reportInsert (MarkupElement me)
	{
		me.addValue (MarkupDocument.insert (SBMLModel.getSidName (variable) + " = " + flattenMath (math.getDocumentNode ())));
	}

	public void reportDelete (MarkupElement me)
	{
		me.addValue (MarkupDocument.delete (SBMLModel.getSidName (variable) + " = " + flattenMath (math.getDocumentNode ())));
	}
	
	private String flattenMath (DocumentNode math)
	{
		try
		{
			return DocumentTools.transformMathML (math);
		}
		catch (TransformerException e)
		{
			LOGGER.error (e, "cannot parse math in event assignment");
			return "[math parsing err]";
		}
		
	}
	
}
