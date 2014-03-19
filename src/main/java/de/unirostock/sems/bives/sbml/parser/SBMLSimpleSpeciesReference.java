/**
 * 
 */
package de.unirostock.sems.bives.sbml.parser;

import de.unirostock.sems.bives.algorithm.SimpleConnectionManager;
import de.unirostock.sems.bives.ds.ModelDocument;
import de.unirostock.sems.bives.ds.ontology.SBOTerm;
import de.unirostock.sems.bives.markup.MarkupDocument;
import de.unirostock.sems.bives.sbml.exception.BivesSBMLParseException;
import de.unirostock.sems.xmlutils.ds.DocumentNode;


/**
 * The Class SBMLSimpleSpeciesReference representing a link to a species (mainly for modifiers, parent class for substrates and products).
 *
 * @author Martin Scharm
 */
public class SBMLSimpleSpeciesReference
extends SBMLSBase
{
	
	/** The id of this species reference. */
	protected String id; //optional
	
	/** The name. */
	protected String name; //optional
	
	/** The species it is linking to. */
	protected SBMLSpecies species;
	
	/**
	 * Instantiates a new SBML simple species reference.
	 *
	 * @param documentNode the document node encoding this entity in the corresponding XML tree
	 * @param sbmlModel the SBML model
	 * @throws BivesSBMLParseException the bives sbml parse exception
	 */
	public SBMLSimpleSpeciesReference (DocumentNode documentNode,
		SBMLModel sbmlModel) throws BivesSBMLParseException
	{
		super (documentNode, sbmlModel);

		id = documentNode.getAttributeValue ("id");
		name = documentNode.getAttributeValue ("name");
		
		if (id != null)
			sbmlModel.registerSpeciesReference (id, this);
		
		String tmp = documentNode.getAttributeValue ("species");
		species = sbmlModel.getSpecies (tmp);
		if (species == null)
			throw new BivesSBMLParseException ("species reference "+tmp+" is not a valid species.");
		
	}
	
	/**
	 * Gets the corresponding species.
	 *
	 * @return the species
	 */
	public SBMLSpecies getSpecies ()
	{
		return species;
	}

	/**
	 * Report modification between two versions of this species reference.
	 *
	 * @param conMgmt the connection manager
	 * @param a the original reference
	 * @param b the modified reference
	 * @return the textual representation of this modification
	 */
	public String reportModification (SimpleConnectionManager conMgmt, SBMLSimpleSpeciesReference a, SBMLSimpleSpeciesReference b)
	{
		SBOTerm sboA = a.getSBOTerm (), sboB = b.getSBOTerm ();
		String retA = a.species.getID () + (sboA == null? "("+SBOTerm.resolveModifier (SBOTerm.MOD_UNKNOWN)+")" : "("+sboA.resolveModifier ()+")");
		String retB = b.species.getID () + (sboB == null? "("+SBOTerm.resolveModifier (SBOTerm.MOD_UNKNOWN)+")" : "("+sboB.resolveModifier ()+")");
		
		if (retA.equals (retB))
			return retA;
		else
			return MarkupDocument.delete (retA) + " + " + MarkupDocument.insert (retB);
	}

	/**
	 * Report an insert.
	 *
	 * @return the string
	 */
	public String reportInsert ()
	{
		SBOTerm sbo = getSBOTerm ();
		return MarkupDocument.insert (species.getID () + (sbo == null? "("+SBOTerm.resolveModifier (SBOTerm.MOD_UNKNOWN)+")" : "("+sbo.resolveModifier ()+")"));
	}

	/**
	 * Report a delete.
	 *
	 * @return the string
	 */
	public String reportDelete ()
	{
		SBOTerm sbo = getSBOTerm ();
		return MarkupDocument.delete (species.getID () + (sbo == null? "("+SBOTerm.resolveModifier (SBOTerm.MOD_UNKNOWN)+")" : "("+sbo.resolveModifier ()+")"));
	}
	
	/**
	 * Typeset this reference.
	 *
	 * @return the string
	 */
	public String report ()
	{
		SBOTerm sbo = getSBOTerm ();		
		return species.getID () + (sbo == null? "("+SBOTerm.resolveModifier (SBOTerm.MOD_UNKNOWN)+")" : "("+sbo.resolveModifier ()+")");
	}
}
