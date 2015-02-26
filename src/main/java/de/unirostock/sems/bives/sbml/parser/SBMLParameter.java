/**
 * 
 */
package de.unirostock.sems.bives.sbml.parser;

import de.unirostock.sems.bives.algorithm.DiffReporter;
import de.unirostock.sems.bives.algorithm.SimpleConnectionManager;
import de.unirostock.sems.bives.markup.Markup;
import de.unirostock.sems.bives.markup.MarkupDocument;
import de.unirostock.sems.bives.markup.MarkupElement;
import de.unirostock.sems.bives.sbml.exception.BivesSBMLParseException;
import de.unirostock.sems.bives.tools.BivesTools;
import de.unirostock.sems.xmlutils.ds.DocumentNode;


/**
 * The Class SBMLParameter is used to define a symbol associated with a value; this symbol can then be used in mathematical formulas in a model.
 *
 * @author Martin Scharm
 */
public class SBMLParameter
	extends SBMLGenericIdNameObject
	implements DiffReporter, Markup
{
	
	/** The value. */
	private Double value; //optional
	
	/** The units. */
	private SBMLUnitDefinition units; //optional
	
	/** The constant. */
	private boolean constant; //optional pre level 3
	
	
	/**
	 * Instantiates a new SBML parameter.
	 *
	 * @param documentNode the document node encoding this entity in the corresponding XML tree
	 * @param sbmlModel the SBML model
	 * @throws BivesSBMLParseException the bives sbml parse exception
	 */
	public SBMLParameter (DocumentNode documentNode, SBMLModel sbmlModel)
		throws BivesSBMLParseException
	{
		super (documentNode, sbmlModel);
		

		if (documentNode.getAttributeValue ("value") != null)
		{
			try
			{
				value = Double.parseDouble (documentNode.getAttributeValue ("value"));
			}
			catch (Exception e)
			{
				throw new BivesSBMLParseException ("value of parameter "+id+" of unexpected format: " + documentNode.getAttributeValue ("value"));
			}
		}
		
		if (documentNode.getAttributeValue ("units") != null)
		{
			String tmp = documentNode.getAttributeValue ("units");
			units = sbmlModel.getUnitDefinition (tmp);
			if (units == null)
				throw new BivesSBMLParseException ("units attribute in parameter "+id+" not defined: " + tmp);
		}
		
		if (documentNode.getAttributeValue ("constant") != null)
		{
			try
			{
				constant = Boolean.parseBoolean (documentNode.getAttributeValue ("constant"));
			}
			catch (Exception e)
			{
				throw new BivesSBMLParseException ("constant of parameter "+id+" of unexpected format: " + documentNode.getAttributeValue ("constant"));
			}
		}
		else
			constant = true; // level <= 2
		
	}

	/**
	 * Gets the value.
	 *
	 * @return the value
	 */
	public double getValue ()
	{
		return value;
	}
	
	/**
	 * Checks if the parameter is constant.
	 *
	 * @return true, if is constant
	 */
	public boolean isConstant ()
	{
		return constant;
	}
	
	/* (non-Javadoc)
	 * @see de.unirostock.sems.bives.markup.Markup#markup()
	 */
	public String markup ()
	{
		String u = "[no units]";
		if (units != null)
			u = units.getName ();
		return getNameAndId () + "=" + value + " " + u + (constant ? " [const]" : "");
	}

	/* (non-Javadoc)
	 * @see de.unirostock.sems.bives.algorithm.DiffReporter#reportMofification(de.unirostock.sems.bives.algorithm.SimpleConnectionManager, de.unirostock.sems.bives.algorithm.DiffReporter, de.unirostock.sems.bives.algorithm.DiffReporter)
	 */
	@Override
	public MarkupElement reportModification (SimpleConnectionManager conMgmt, DiffReporter docA, DiffReporter docB)
	{
		SBMLParameter a = (SBMLParameter) docA;
		SBMLParameter b = (SBMLParameter) docB;
		if (a.getDocumentNode ().getModification () == 0 && b.getDocumentNode ().getModification () == 0)
			return null;
		
		String idA = a.getNameAndId ();
		String idB = b.getNameAndId ();
		MarkupElement me = null;
		if (idA.equals (idB))
			me = new MarkupElement (idA);
		else
			me = new MarkupElement (MarkupDocument.delete (idA) + " "+MarkupDocument.rightArrow ()+" " + MarkupDocument.insert (idB));
		
		BivesTools.genAttributeMarkupStats (a.documentNode, b.documentNode, me);
		
		return me;
	}
	
	/* (non-Javadoc)
	 * @see de.unirostock.sems.bives.algorithm.DiffReporter#reportInsert()
	 */
	@Override
	public MarkupElement reportInsert ()
	{
		MarkupElement me = new MarkupElement (MarkupDocument.insert (getNameAndId ()));
		me.addValue (MarkupDocument.insert ("inserted"));
		return me;
	}
	
	/* (non-Javadoc)
	 * @see de.unirostock.sems.bives.algorithm.DiffReporter#reportDelete()
	 */
	@Override
	public MarkupElement reportDelete ()
	{
		MarkupElement me = new MarkupElement (MarkupDocument.delete (getNameAndId ()));
		me.addValue (MarkupDocument.delete ("deleted"));
		return me;
	}
}
