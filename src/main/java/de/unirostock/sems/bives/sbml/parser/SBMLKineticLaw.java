package de.unirostock.sems.bives.sbml.parser;

import java.util.HashMap;

import java.util.List;

import de.unirostock.sems.bives.algorithm.SimpleConnectionManager;
import de.unirostock.sems.bives.ds.MathML;
import de.unirostock.sems.bives.markup.MarkupDocument;
import de.unirostock.sems.bives.markup.MarkupElement;
import de.unirostock.sems.bives.sbml.exception.BivesSBMLParseException;
import de.unirostock.sems.bives.tools.BivesTools;
import de.unirostock.sems.xmlutils.ds.DocumentNode;
import de.unirostock.sems.xmlutils.ds.TreeNode;



/**
 * The Class SBMLKineticLaw defines the speed at which the process defined by the reaction takes place.
 *
 * @author Martin Scharm
 */
public class SBMLKineticLaw
	extends SBMLSBase
{
	
	/** The math. */
	private MathML math;
	
	/** The list of local parameters. */
	private HashMap<String, SBMLParameter> listOfLocalParameters;
	
	/**
	 * Instantiates a new SBML kinetic law.
	 *
	 * @param documentNode the document node encoding this entity in the corresponding XML tree
	 * @param sbmlModel the SBML model
	 * @throws BivesSBMLParseException the bives sbml parse exception
	 */
	public SBMLKineticLaw (DocumentNode documentNode, SBMLModel sbmlModel)
		throws BivesSBMLParseException
	{
		super (documentNode, sbmlModel);
		
		List<TreeNode> nodes = documentNode.getChildrenWithTag ("math");
		if (nodes.size () != 1)
			throw new BivesSBMLParseException ("kinetic law has "+nodes.size ()+" math elements. (expected exactly one element)");
		math = new MathML ((DocumentNode) nodes.get (0));
		
		listOfLocalParameters = new HashMap<String, SBMLParameter> ();
		
		nodes = documentNode.getChildrenWithTag ("listOfLocalParameters");
		for (int i = 0; i < nodes.size (); i++)
		{
			List<TreeNode> paras = ((DocumentNode) nodes.get (i)).getChildrenWithTag ("localParameter");
			for (int j = 0; j < paras.size (); j++)
			{
				SBMLParameter p = new SBMLParameter ((DocumentNode) paras.get (j), sbmlModel);
				listOfLocalParameters.put (p.getID (), p);
			}
		}
		
		nodes = documentNode.getChildrenWithTag ("listOfParameters");
		for (int i = 0; i < nodes.size (); i++)
		{
			List<TreeNode> paras = ((DocumentNode) nodes.get (i)).getChildrenWithTag ("parameter");
			for (int j = 0; j < paras.size (); j++)
			{
				SBMLParameter p = new SBMLParameter ((DocumentNode) paras.get (j), sbmlModel);
				listOfLocalParameters.put (p.getID (), p);
			}
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
	 * Report modification of the entities.
	 *
	 * @param conMgmt the connection manager
	 * @param a the original version
	 * @param b the modified version
	 * @param me the markup element
	 */
	public void reportModification (SimpleConnectionManager conMgmt, SBMLKineticLaw a, SBMLKineticLaw b, MarkupElement me)
	{
		if (a.getDocumentNode ().getModification () == 0 && b.getDocumentNode ().getModification () == 0)
			return;

		//String ret = "";
		HashMap<String, SBMLParameter> locParA = a.listOfLocalParameters;
		HashMap<String, SBMLParameter> locParB = b.listOfLocalParameters;
		for (String key : locParA.keySet ())
		{
			if (locParB.get (key) == null)
				me.addValue ("local parameter: " + MarkupDocument.delete (locParA.get (key).markup ()));
				//ret += "<span class='"+CLASS_DELETED+"'>local parameter: " + locParA.get (key).htmlMarkup () + "</span><br/>";
			else
			{
				SBMLParameter parA = locParA.get (key);
				String aS = parA.markup ();
				String bS = locParB.get (key).markup ();
				if (!aS.equals (bS))
					me.addValue ("local parameter: " + parA.getNameAndId ()+ " modified from " +MarkupDocument.delete (aS) + " to " + MarkupDocument.insert (bS));
					//ret += "local parameter: "+parA.getNameAndId ()+" modified from <span class='"+CLASS_DELETED+"'>" + aS + "</span> to <span class='"+CLASS_INSERTED+"'>" + bS + "</span><br/>";
			}
		}
		for (String key : locParB.keySet ())
		{
			if (locParA.get (key) == null)
				me.addValue ("local parameter: " + MarkupDocument.insert (locParB.get (key).markup ()));
				//ret += "<span class='"+CLASS_INSERTED+"'>local parameter: " + locParA.get (key).htmlMarkup () + "</span><br/>";
		}
		
		if (a.math != null && b.math != null)
			BivesTools.genMathMarkupStats (a.math.getDocumentNode (), b.math.getDocumentNode (), me);
		else if (a.math != null)
			BivesTools.genMathMarkupStats (a.math.getDocumentNode (), null, me);
		else if (b.math != null)
			BivesTools.genMathMarkupStats (null, b.math.getDocumentNode (), me);
		
		//return ret;
	}

	/**
	 * Report insert.
	 *
	 * @param me the me
	 */
	public void reportInsert (MarkupElement me)
	{
		for (SBMLParameter locPar : listOfLocalParameters.values ())
			me.addValue ("local parameter: " + MarkupDocument.insert (locPar.markup ()));
			//ret += "<span class='"+CLASS_DELETED+"'>local parameter: " + locPar.htmlMarkup () + "</span><br/>";
		if (math != null)
			BivesTools.genAttributeMarkupStats (null, math.getDocumentNode (), me);
		/*String ret = "";
		for (SBMLParameter locPar : listOfLocalParameters.values ())
			ret += "<span class='"+CLASS_INSERTED+"'>local parameter: " + locPar.htmlMarkup () + "</span><br/>";
		if (math != null)
			ret += Tools.genAttributeHtmlStats (null, math.getMath ());
		return ret;*/
	}

	/**
	 * Report delete.
	 *
	 * @param me the me
	 */
	public void reportDelete (MarkupElement me)
	{
		for (SBMLParameter locPar : listOfLocalParameters.values ())
			me.addValue ("local parameter: " + MarkupDocument.delete (locPar.markup ()));
			//ret += "<span class='"+CLASS_DELETED+"'>local parameter: " + locPar.htmlMarkup () + "</span><br/>";
		if (math != null)
			BivesTools.genAttributeMarkupStats (math.getDocumentNode (), null, me);
	}
	
}
