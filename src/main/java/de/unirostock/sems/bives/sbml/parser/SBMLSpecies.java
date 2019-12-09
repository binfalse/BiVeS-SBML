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
 * The Class SBMLSpecies representing a species in an SBML document.
 *
 * @author Martin Scharm
 */
public class SBMLSpecies
	extends SBMLGenericIdNameObject
	implements DiffReporter
{
	
	/** The compartment which hosts this species. */
	private SBMLCompartment compartment;
	
	/** The initial amount of the species. */
	private Double initialAmount; //optional
	
	/** The initial concentration. */
	private Double initialConcentration; //optional
	
	/** The units. */
	private	SBMLUnitDefinition substanceUnits; //optional
	
	/** The has only substance units. */
	private boolean hasOnlySubstanceUnits; // level 2+
	
	/** The boundary condition. */
	private boolean boundaryCondition; // optional before 3, defaults to false
	
	/** The constant. */
	private boolean constant;
	
	/** The charge. */
	private Integer charge; //optional, only < level 3
	
	/** The conversion factor. */
	private	SBMLParameter conversionFactor; //optional only level 3+
	
	/** The species type. */
	private SBMLSpeciesType speciesType; //optional only level < 3
	
	/**
	 * Instantiates a new SBML species.
	 *
	 * @param documentNode the document node encoding this entity in the corresponding XML tree
	 * @param sbmlModel the SBML model
	 * @throws BivesSBMLParseException the bives sbml parse exception
	 */
	public SBMLSpecies (DocumentNode documentNode, SBMLModel sbmlModel)
		throws BivesSBMLParseException
	{
		super (documentNode, sbmlModel);
		
		String tmp = documentNode.getAttributeValue ("compartment");
		compartment = sbmlModel.getCompartment (tmp);
		if (compartment == null)
			throw new BivesSBMLParseException ("no valid compartment for species "+id+" defined: " + tmp);
		
		initialAmount = null;
		initialConcentration = null;

		tmp = documentNode.getAttributeValue ("speciesType");
		if (tmp != null)
		{
			speciesType = sbmlModel.getSpeciesType (tmp);
			if (speciesType == null)
				throw new BivesSBMLParseException ("no valid speciesType for species "+id+" defined: " + tmp);
		}
		
		if (documentNode.getAttributeValue ("charge") != null)
		{
			try
			{
				charge = Integer.parseInt (documentNode.getAttributeValue ("charge"));
			}
			catch (Exception e)
			{
				throw new BivesSBMLParseException ("charge of species "+id+" of unexpected format: " + documentNode.getAttributeValue ("charge"));
			}
		}
		
		if (documentNode.getAttributeValue ("initialAmount") != null)
		{
			try
			{
				initialAmount = Double.parseDouble (documentNode.getAttributeValue ("initialAmount"));
			}
			catch (Exception e)
			{
				throw new BivesSBMLParseException ("initialAmount of species "+id+" of unexpected format: " + documentNode.getAttributeValue ("initialAmount"));
			}
		}
		
		if (documentNode.getAttributeValue ("initialConcentration") != null)
		{
			try
			{
				initialConcentration = Double.parseDouble (documentNode.getAttributeValue ("initialConcentration"));
			}
			catch (Exception e)
			{
				throw new BivesSBMLParseException ("initialConcentration of species "+id+" of unexpected format: " + documentNode.getAttributeValue ("initialConcentration"));
			}
		}
		
		if (initialAmount != null && initialConcentration != null)
			throw new BivesSBMLParseException ("initialAmount AND initialConcentration of species "+id+" defined. ");
		
		if (documentNode.getAttributeValue ("substanceUnits") != null)
		{
			tmp = documentNode.getAttributeValue ("substanceUnits");
			substanceUnits = sbmlModel.getUnitDefinition (tmp);
			if (substanceUnits == null)
				throw new BivesSBMLParseException ("substanceUnits attribute in species "+id+" not defined: " + tmp);
		}
		
		if (documentNode.getAttributeValue ("hasOnlySubstanceUnits") != null)
		{
			try
			{
				hasOnlySubstanceUnits = Boolean.parseBoolean (documentNode.getAttributeValue ("hasOnlySubstanceUnits"));
			}
			catch (Exception e)
			{
				throw new BivesSBMLParseException ("hasOnlySubstanceUnits of species "+id+" of unexpected format: " + documentNode.getAttributeValue ("hasOnlySubstanceUnits"));
			}
		}
		else
			hasOnlySubstanceUnits = false; // level <= 2

		
		if (documentNode.getAttributeValue ("boundaryCondition") != null)
		{
			try
			{
				boundaryCondition = Boolean.parseBoolean (documentNode.getAttributeValue ("boundaryCondition"));
			}
			catch (Exception e)
			{
				throw new BivesSBMLParseException ("boundaryCondition of species "+id+" of unexpected format: " + documentNode.getAttributeValue ("boundaryCondition"));
			}
		}
		else
			boundaryCondition = false; // level <= 2

		
		if (documentNode.getAttributeValue ("constant") != null)
		{
			try
			{
				constant = Boolean.parseBoolean (documentNode.getAttributeValue ("constant"));
			}
			catch (Exception e)
			{
				throw new BivesSBMLParseException ("constant attr of species "+id+" of unexpected format: " + documentNode.getAttributeValue ("constant"));
			}
		}
		else
			constant = false; // level <= 2

		
		if (documentNode.getAttributeValue ("conversionFactor") != null)
		{
			tmp = documentNode.getAttributeValue ("conversionFactor");
			conversionFactor = sbmlModel.getParameter (tmp);
			if (conversionFactor == null)
				throw new BivesSBMLParseException ("conversionFactor attribute in species "+id+" not defined: " + tmp);
			if (!conversionFactor.isConstant ())
				throw new BivesSBMLParseException ("conversionFactor attribute in species "+id+" is not constant: " + tmp);
		}
	}
	
	/**
	 * Returns the initial amount of this species or null if not defined.
	 *
	 * @return the initial amount
	 */
	public double getInitialAmount ()
	{
		return initialAmount;
	}
	
	/**
	 * Returns the initial concentration of this species or null if not defined.
	 *
	 * @return the initial concentration
	 */
	public double getInitialConcentration ()
	{
		return initialConcentration;
	}
	
	/**
	 * Gets the compartment that hosts this species.
	 *
	 * @return the compartment
	 */
	public SBMLCompartment getCompartment ()
	{
		return compartment;
	}
	
	/**
	 * Is this species allowed to have an assignment rule.
	 *
	 * @return true, if allowed
	 */
	public boolean canHaveAssignmentRule ()
	{
		return !constant;
	}
	
	/**
	 * Can this species take part in a reaction as a reactant or product?
	 *
	 * @return true, if that is possible
	 */
	public boolean canBeReactantOrProduct ()
	{
		return boundaryCondition || (!boundaryCondition && !constant);
	}
	
	/**
	 * Checks if this species is constant.
	 *
	 * @return true, if it is constant
	 */
	public boolean isConstant ()
	{
		return constant;
	}
	
	/**
	 * Checks for a boundary condition.
	 *
	 * @return true, if successful
	 */
	public boolean hasBoundaryCondition ()
	{
		return boundaryCondition;
	}
	
	/**
	 * Checks if this species has only substance units.
	 *
	 * @return true, if it has only substance units
	 */
	public boolean hasOnlySubstanceUnits ()
	{
		return hasOnlySubstanceUnits;
	}

	/* (non-Javadoc)
	 * @see de.unirostock.sems.bives.algorithm.DiffReporter#reportMofification(de.unirostock.sems.bives.algorithm.SimpleConnectionManager, de.unirostock.sems.bives.algorithm.DiffReporter, de.unirostock.sems.bives.algorithm.DiffReporter)
	 */
	@Override
	public MarkupElement reportModification (SimpleConnectionManager conMgmt, DiffReporter docA, DiffReporter docB)
	{
		SBMLSpecies a = (SBMLSpecies) docA;
		SBMLSpecies b = (SBMLSpecies) docB;
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
}
