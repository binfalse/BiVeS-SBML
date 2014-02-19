/**
 * 
 */
package de.unirostock.sems.bives.sbml.parser;

import de.unirostock.sems.bives.algorithm.SimpleConnectionManager;
import de.unirostock.sems.bives.ds.ontology.SBOTerm;
import de.unirostock.sems.bives.markup.MarkupDocument;
import de.unirostock.sems.bives.sbml.exception.BivesSBMLParseException;
import de.unirostock.sems.xmlutils.ds.DocumentNode;


/**
 * @author Martin Scharm
 *
 */
public class SBMLSimpleSpeciesReference
extends SBMLSBase
{
	protected String id; //optional
	protected String name; //optional
	protected SBMLSpecies species;
	
	public SBMLSimpleSpeciesReference (DocumentNode documentNode,
		SBMLModel sbmlModel) throws BivesSBMLParseException
	{
		super (documentNode, sbmlModel);

		id = documentNode.getAttribute ("id");
		name = documentNode.getAttribute ("name");
		
		
		String tmp = documentNode.getAttribute ("species");
		species = sbmlModel.getSpecies (tmp);
		if (species == null)
			throw new BivesSBMLParseException ("species reference "+tmp+" is not a valid species.");
		
	}
	
	public SBMLSpecies getSpecies ()
	{
		return species;
	}

	public String reportMofification (SimpleConnectionManager conMgmt, SBMLSimpleSpeciesReference a, SBMLSimpleSpeciesReference b)
	{
		/*SBMLSimpleSpeciesReference a = (SBMLSimpleSpeciesReference) docA;
		SBMLSimpleSpeciesReference b = (SBMLSimpleSpeciesReference) docB;*/
		//if (a.getDocumentNode ().getModification () == 0 && b.getDocumentNode ().getModification () == 0)
		//	return species.getNameAndId ();
		
		SBOTerm sboA = a.getSBOTerm (), sboB = b.getSBOTerm ();
		String retA = a.species.getID () + (sboA == null? "("+SBOTerm.resolveModifier (SBOTerm.MOD_UNKNOWN)+")" : "("+sboA.resolveModifier ()+")");
		String retB = b.species.getID () + (sboB == null? "("+SBOTerm.resolveModifier (SBOTerm.MOD_UNKNOWN)+")" : "("+sboB.resolveModifier ()+")");
		
		if (retA.equals (retB))
			return retA;
		else
			return MarkupDocument.delete (retA) + " + " + MarkupDocument.insert (retB);
	}

	public String reportInsert ()
	{
		SBOTerm sbo = getSBOTerm ();
		return MarkupDocument.insert (species.getID () + (sbo == null? "("+SBOTerm.resolveModifier (SBOTerm.MOD_UNKNOWN)+")" : "("+sbo.resolveModifier ()+")"));
	}

	public String reportDelete ()
	{
		SBOTerm sbo = getSBOTerm ();
		return MarkupDocument.delete (species.getID () + (sbo == null? "("+SBOTerm.resolveModifier (SBOTerm.MOD_UNKNOWN)+")" : "("+sbo.resolveModifier ()+")"));
	}
	
	public String report ()
	{
		SBOTerm sbo = getSBOTerm ();		
		return species.getID () + (sbo == null? "("+SBOTerm.resolveModifier (SBOTerm.MOD_UNKNOWN)+")" : "("+sbo.resolveModifier ()+")");
	}
}
