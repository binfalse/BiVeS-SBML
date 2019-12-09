/**
 * 
 */
package de.unirostock.sems.bives.sbml.parser;


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
 * The Class SBMLEventAssignment. An "event assignment" has effect when the event is executed.
 *
 * @author Martin Scharm
 */
public class SBMLEventAssignment
	extends SBMLSBase
{
	
	/** The math. */
	private MathML math;
	
	/** The variable of interest. */
	private SBMLSBase variable;
	
	/**
	 * Instantiates a new SBML event assignment.
	 *
	 * @param documentNode the document node encoding this entity in the corresponding XML tree
	 * @param sbmlModel the SBML model
	 * @throws BivesSBMLParseException the bives sbml parse exception
	 */
	public SBMLEventAssignment (DocumentNode documentNode,
		SBMLModel sbmlModel) throws BivesSBMLParseException
	{
		super (documentNode, sbmlModel);
		
		List<TreeNode> maths = documentNode.getChildrenWithTag ("math");
		if (maths.size () != 1)
			throw new BivesSBMLParseException ("event trigger has "+maths.size ()+" math elements. (expected exactly one element)");
		math = new MathML ((DocumentNode) maths.get (0));
		
		variable = resolveVariable (documentNode.getAttributeValue ("variable"));
	}

	
	/**
	 * Resolve variable.
	 *
	 * @param ref the reference to a variable name
	 * @return the SBML SBase representing the variable
	 * @throws BivesSBMLParseException the bives sbml parse exception
	 */
	protected final SBMLSBase resolveVariable (String ref) throws BivesSBMLParseException
	{
		SBMLSBase var = sbmlModel.resolveSymbole (ref);
		if (var == null)
			throw new BivesSBMLParseException ("variable "+ref+" of rule unmappable.");
		return var;
	}
	
	
	/**
	 * Gets the variable of interest.
	 *
	 * @return the variable
	 */
	public SBMLSBase getVariable ()
	{
		return variable;
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
	 * Report modification on this entity.
	 *
	 * @param conMgmt the connection manager
	 * @param a the original version
	 * @param b the modified version
	 * @param me the markup element
	 */
	public void reportModification (SimpleConnectionManager conMgmt, SBMLEventAssignment a, SBMLEventAssignment b, MarkupElement me)
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

		if (!a.flagMetaModifcations (me))
			b.flagMetaModifcations (me);
	}

	/**
	 * Report insert.
	 *
	 * @param me the me
	 */
	public void reportInsert (MarkupElement me)
	{
		me.addValue (MarkupDocument.insert (SBMLModel.getSidName (variable) + " = " + flattenMath (math.getDocumentNode ())));
	}

	/**
	 * Report delete.
	 *
	 * @param me the me
	 */
	public void reportDelete (MarkupElement me)
	{
		me.addValue (MarkupDocument.delete (SBMLModel.getSidName (variable) + " = " + flattenMath (math.getDocumentNode ())));
	}
	
	/**
	 * Flatten math.
	 *
	 * @param math the math
	 * @return the string
	 */
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
