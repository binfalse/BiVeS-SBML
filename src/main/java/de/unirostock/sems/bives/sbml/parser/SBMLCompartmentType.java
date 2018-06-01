/**
 * 
 */
package de.unirostock.sems.bives.sbml.parser;

import de.unirostock.sems.bives.algorithm.DiffReporter;
import de.unirostock.sems.bives.algorithm.SimpleConnectionManager;
import de.unirostock.sems.bives.markup.MarkupDocument;
import de.unirostock.sems.bives.markup.MarkupElement;
import de.unirostock.sems.bives.sbml.exception.BivesSBMLParseException;
import de.unirostock.sems.xmlutils.ds.DocumentNode;


/**
 * The Class SBMLCompartmentType, a grouping construct used to establish a relationship between multiple SBMLCompartments. (only level &lt; 3)
 *
 * @author Martin Scharm
 */
public class SBMLCompartmentType
	extends SBMLGenericIdNameObject
	implements DiffReporter
{
	
	/**
	 * Instantiates a new SBML compartment type.
	 *
	 * @param documentNode the document node encoding this entity in the corresponding XML tree
	 * @param sbmlModel the SBML model
	 * @throws BivesSBMLParseException the bives sbml parse exception
	 */
	public SBMLCompartmentType (DocumentNode documentNode, SBMLModel sbmlModel)
		throws BivesSBMLParseException
	{
		super (documentNode, sbmlModel);
	}

	/* (non-Javadoc)
	 * @see de.unirostock.sems.bives.algorithm.DiffReporter#reportMofification(de.unirostock.sems.bives.algorithm.SimpleConnectionManager, de.unirostock.sems.bives.algorithm.DiffReporter, de.unirostock.sems.bives.algorithm.DiffReporter)
	 */
	@Override
	public MarkupElement reportModification (SimpleConnectionManager conMgmt, DiffReporter docA, DiffReporter docB)
	{
		SBMLCompartmentType a = (SBMLCompartmentType) docA;
		SBMLCompartmentType b = (SBMLCompartmentType) docB;
		if (a.getDocumentNode ().getModification () == 0 && b.getDocumentNode ().getModification () == 0)
			return null;
		
		String idA = a.getNameAndId (), idB = b.getNameAndId ();
		MarkupElement me = null;
		if (idA.equals (idB))
			me = new MarkupElement (idA);
		else
			me = new MarkupElement (MarkupDocument.delete (idA) + " "+MarkupDocument.rightArrow ()+" " + MarkupDocument.insert (idB));

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
	
}
