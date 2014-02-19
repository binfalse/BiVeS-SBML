/**
 * 
 */
package de.unirostock.sems.bives.sbml.parser;

import java.util.ArrayList;
import java.util.List;

import de.unirostock.sems.bives.algorithm.DiffReporter;
import de.unirostock.sems.bives.algorithm.SimpleConnectionManager;
import de.unirostock.sems.bives.exception.BivesDocumentConsistencyException;
import de.unirostock.sems.bives.markup.Markup;
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
public class SBMLUnitDefinition
	extends SBMLGenericIdNameObject
	implements DiffReporter, Markup
{
	private boolean baseUnit; // is this a base unit?
	private List<SBMLUnit> listOfUnits;
	
	/**
	 * Instantiates a new SBML base unit.
	 * 
	 * @param name the name of the unit
	 * @throws BivesSBMLParseException 
	 */
	public SBMLUnitDefinition (String name, SBMLModel sbmlModel) throws BivesSBMLParseException
	{
		super (null, sbmlModel);
		id = name;
		this.name = name;
		baseUnit = true;
	}
	
	/**
	 * @param documentNode
	 * @throws BivesSBMLParseException 
	 * @throws BivesConsistencyException 
	 */
	public SBMLUnitDefinition (DocumentNode documentNode, SBMLModel sbmlModel) throws BivesSBMLParseException, BivesDocumentConsistencyException
	{
		super (documentNode, sbmlModel);
		baseUnit = false;
		
		listOfUnits = new ArrayList<SBMLUnit> ();
		List<TreeNode> lounits = documentNode.getChildrenWithTag ("listOfUnits");
		for (int i = 0; i < lounits.size (); i++)
		{
			DocumentNode lounit = (DocumentNode) lounits.get (i);
			
			List<TreeNode> unit = lounit.getChildrenWithTag ("unit");
			for (int j = 0; j < unit.size (); j++)
			{
				SBMLUnit u = new SBMLUnit ((DocumentNode) unit.get (j), sbmlModel);
				listOfUnits.add (u);
			}
		}
		
		if (listOfUnits.size () < 1)
			throw new BivesSBMLParseException ("UnitDefinition "+id+" has "+listOfUnits.size ()+" units. (expected at least one unit)");
	}
	
	public boolean isBaseUnit ()
	{
		return baseUnit;
	}

	@Override
	public MarkupElement reportMofification (SimpleConnectionManager conMgmt, DiffReporter docA, DiffReporter docB)
	{
		SBMLUnitDefinition a = (SBMLUnitDefinition) docA;
		SBMLUnitDefinition b = (SBMLUnitDefinition) docB;
		if (a.getDocumentNode ().getModification () == 0 && b.getDocumentNode ().getModification () == 0)
			return null;
		
		String idA = a.getNameAndId (), idB = b.getNameAndId ();
		MarkupElement me = null;
		if (idA.equals (idB))
			me = new MarkupElement (idA);
		else
			me = new MarkupElement (MarkupDocument.delete (idA) + " "+MarkupDocument.rightArrow ()+" " + MarkupDocument.insert (idB));
		
		// check whether unit definition has changed
		String oldDef = a.markup ();
		String newDef = a.markup ();
		if (oldDef.equals (newDef))
			me.addValue ("Defined by: " + oldDef);
		else
			me.addValue ("Definition changed from " + MarkupDocument.delete (oldDef) + " to " + MarkupDocument.insert (newDef));
		
		BivesTools.genAttributeMarkupStats (a.documentNode, b.documentNode, me);
		
		return me;
	}

	@Override
	public MarkupElement reportInsert ()
	{
		MarkupElement me = new MarkupElement (MarkupDocument.insert (getNameAndId ()));
		me.addValue (MarkupDocument.insert ("inserted"));
		return me;
	}

	@Override
	public MarkupElement reportDelete ()
	{
		MarkupElement me = new MarkupElement (MarkupDocument.delete (getNameAndId ()));
		me.addValue (MarkupDocument.delete ("deleted"));
		return me;
	}

	@Override
	public String markup ()
	{
		String ret = "";
		for (int i = 0; i < listOfUnits.size (); i++)
		{
			ret += listOfUnits.get (i).markup ();//.unitToHTMLString ();
			if (i+1 < listOfUnits.size ())
				ret += " "+MarkupDocument.multiply ()+" ";
		}
		return ret;
	}
}
