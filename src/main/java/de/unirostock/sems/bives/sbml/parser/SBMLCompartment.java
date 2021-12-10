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
 * The Class SBMLCompartment, a bounded space in which species are located.
 *
 * @author Martin Scharm
 */
public class SBMLCompartment
	extends SBMLGenericIdNameObject
	implements DiffReporter
{
	
	/** The spatial dimensions. */
	private double spatialDimensions; //optional
	
	/** The size. */
	private double size; //optional
	
	/** The units. */
	private SBMLUnitDefinition units; //optional
	
	/** The constant flag. */
	private boolean constant;
	
	/** The compartment type. */
	private SBMLCompartmentType compartmentType; // only level < 3
	
	/** The compartment which hosts this compartment. */
	private String outside;

	/**
	 * Instantiates a new SBML compartment.
	 *
	 * @param documentNode the document node encoding this entity in the corresponding XML tree
	 * @param sbmlModel the SBML model
	 * @throws BivesSBMLParseException the bives sbml parse exception
	 */
	public SBMLCompartment (DocumentNode documentNode, SBMLModel sbmlModel) throws BivesSBMLParseException
	{
		super (documentNode, sbmlModel);

		String tmp = documentNode.getAttributeValue ("compartmentType");
		if (tmp != null)
		{
			compartmentType = sbmlModel.getCompartmentType (tmp);
			if (compartmentType == null)
				throw new BivesSBMLParseException ("no valid compartmentType for species "+id+" defined: " + tmp);
		}
		
		if (documentNode.getAttributeValue ("spatialDimensions") != null)
		{
			try
			{
				spatialDimensions = Double.parseDouble (documentNode.getAttributeValue ("spatialDimensions"));
			}
			catch (Exception e)
			{
				throw new BivesSBMLParseException ("spatialDimensions in compartment "+id+" of unexpected format: " + documentNode.getAttributeValue ("spatialDimensions"));
			}
		}
		String outsideTemp = documentNode.getAttributeValue ("outside");
		if (outsideTemp != null)
		{
			try
			{
				outside = outsideTemp;
			}
			catch (Exception e)
			{
				throw new BivesSBMLParseException ("Outside in compartment "+id+": " + documentNode.getAttributeValue ("outside"));
			}
		}
		
		if (documentNode.getAttributeValue ("size") != null)
		{
			try
			{
				size = Double.parseDouble (documentNode.getAttributeValue ("size"));
			}
			catch (Exception e)
			{
				throw new BivesSBMLParseException ("size in compartment "+id+" of unexpected format: " + documentNode.getAttributeValue ("size"));
			}
		}
		
		if (documentNode.getAttributeValue ("units") != null)
		{
			String unitStr = documentNode.getAttributeValue ("units");
			units = sbmlModel.getUnitDefinition (unitStr);
			
			if (units == null)
				throw new BivesSBMLParseException ("Unit attribute in compartment "+id+" not defined: " + unitStr);
		}
		
		if (documentNode.getAttributeValue ("constant") != null)
		{
			try
			{
				constant = Boolean.parseBoolean (documentNode.getAttributeValue ("constant"));
			}
			catch (Exception e)
			{
				throw new BivesSBMLParseException ("constant attr in compartment "+id+" of unexpected format: " + documentNode.getAttributeValue ("constant"));
			}
		}
		else
			constant = true; // level <= 2
	}

	/* (non-Javadoc)
	 * @see de.unirostock.sems.bives.algorithm.DiffReporter#reportMofification(de.unirostock.sems.bives.algorithm.SimpleConnectionManager, de.unirostock.sems.bives.algorithm.DiffReporter, de.unirostock.sems.bives.algorithm.DiffReporter)
	 */
	@Override
	public MarkupElement reportModification (SimpleConnectionManager conMgmt, DiffReporter docA, DiffReporter docB)
	{
		SBMLCompartment a = (SBMLCompartment) docA;
		SBMLCompartment b = (SBMLCompartment) docB;
		if (a.getDocumentNode ().getModification () == 0 && b.getDocumentNode ().getModification () == 0)
			return null;
		
		String idA = a.getNameAndId (), idB = b.getNameAndId ();
		MarkupElement me = null;
		if (idA.equals (idB))
			me = new MarkupElement (idA);
		else
			me = new MarkupElement (MarkupDocument.delete (idA) + " "+MarkupDocument.rightArrow ()+" " + MarkupDocument.insert (idB));

		BivesTools.genAttributeMarkupStats (a.documentNode, b.documentNode, me);

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

	public SBMLCompartment getCompartment() {
		return sbmlModel.getCompartment (outside);
	}
	
}
