/**
 * 
 */
package de.unirostock.sems.bives.sbml.algorithm;

import java.util.HashMap;
import java.util.Vector;

import de.binfalse.bflog.LOGGER;
import de.unirostock.sems.bives.algorithm.ClearConnectionManager;
import de.unirostock.sems.bives.algorithm.GraphProducer;
import de.unirostock.sems.bives.ds.graph.CRNCompartment;
import de.unirostock.sems.bives.ds.graph.CRNReaction;
import de.unirostock.sems.bives.ds.graph.CRNSubstance;
import de.unirostock.sems.bives.sbml.parser.SBMLCompartment;
import de.unirostock.sems.bives.sbml.parser.SBMLDocument;
import de.unirostock.sems.bives.sbml.parser.SBMLModel;
import de.unirostock.sems.bives.sbml.parser.SBMLReaction;
import de.unirostock.sems.bives.sbml.parser.SBMLSimpleSpeciesReference;
import de.unirostock.sems.bives.sbml.parser.SBMLSpecies;
import de.unirostock.sems.bives.sbml.parser.SBMLSpeciesReference;
import de.unirostock.sems.xmlutils.comparison.Connection;
import de.unirostock.sems.xmlutils.ds.DocumentNode;


/**
 * @author Martin Scharm
 *
 */
public class SBMLGraphProducer
extends GraphProducer
{
	private SBMLDocument sbmlDocA, sbmlDocB;
	private ClearConnectionManager conMgmt;
	
	public SBMLGraphProducer (ClearConnectionManager conMgmt, SBMLDocument sbmlDocA, SBMLDocument sbmlDocB)
	{
		super (false);
		this.sbmlDocA = sbmlDocA;
		this.sbmlDocB = sbmlDocB;
		this.conMgmt = conMgmt;
	}
	
	public SBMLGraphProducer (SBMLDocument sbmlDoc)
	{
		super (true);
		this.sbmlDocA = sbmlDoc;
	}
	


	@Override
	protected void produceCRN ()
	{
		processCrnA ();
		if (single)
			crn.setSingleDocument ();
		else
			processCrnB ();
	}

	@Override
	protected void produceHierachyGraph ()
	{
		// nothing to do for SBML
	}
	
	protected void processCrnA ()
	{
		SBMLModel modelA = sbmlDocA.getModel ();
		LOGGER.info ("searching for compartments in A");
		HashMap<String, SBMLCompartment> compartments = modelA.getCompartments ();
		for (SBMLCompartment c : compartments.values ())
			crn.setCompartment (c.getDocumentNode (), new CRNCompartment (crn, c.getNameOrId (), null, c.getDocumentNode (), null));
		
		LOGGER.info ("searching for species in A");
		HashMap<String, SBMLSpecies> species = modelA.getSpecies ();
		for (SBMLSpecies s : species.values ())
			crn.setSubstance (s.getDocumentNode (), new CRNSubstance (crn, s.getNameOrId (), null, s.getDocumentNode (), null, crn.getCompartment (s.getCompartment ().getDocumentNode ()), null));
		
		LOGGER.info ("searching for reactions in A");
		HashMap<String, SBMLReaction> reactions = modelA.getReactions ();
		for (SBMLReaction r : reactions.values ())
		{
			CRNReaction reaction = new CRNReaction (crn, r.getNameOrId (), null, r.getDocumentNode (), null, r.isReversible ());
			if (r.getCompartment () != null)
				reaction.setCompartmentA (crn.getCompartment (r.getCompartment ().getDocumentNode ()));
			crn.setReaction (r.getDocumentNode (), reaction);
			
			Vector<SBMLSpeciesReference> sRefs = r.getReactants ();
			for (SBMLSpeciesReference sRef : sRefs)
			{
				reaction.addInputA (crn.getSubstance (sRef.getSpecies ().getDocumentNode ()), sRef.getSBOTerm ());
			}
			
			sRefs = r.getProducts ();
			for (SBMLSpeciesReference sRef : sRefs)
			{
				reaction.addOutputA (crn.getSubstance (sRef.getSpecies ().getDocumentNode ()), sRef.getSBOTerm ());
			}
			
			Vector<SBMLSimpleSpeciesReference> ssRefs = r.getModifiers ();
			for (SBMLSimpleSpeciesReference sRef : ssRefs)
			{
				SBMLSpecies spec = sRef.getSpecies ();
					reaction.addModA (crn.getSubstance (spec.getDocumentNode ()), sRef.getSBOTerm ());
			}
		}
	}
	
	protected void processCrnB ()
	{
		SBMLModel modelB = sbmlDocB.getModel ();
		LOGGER.info ("searching for compartments in A");
		HashMap<String, SBMLCompartment> compartments = modelB.getCompartments ();
		for (SBMLCompartment c : compartments.values ())
		{
			DocumentNode cDoc = c.getDocumentNode ();
			Connection con = conMgmt.getConnectionForNode (cDoc);
			if (con == null)
			{
				// no equivalent in doc a
				crn.setCompartment (c.getDocumentNode (), new CRNCompartment (crn, c.getNameOrId (), null, c.getDocumentNode (), null));
			}
			else
			{
				CRNCompartment comp = crn.getCompartment (con.getPartnerOf (cDoc));
				comp.setDocB (cDoc);
				comp.setLabelB (c.getNameOrId ());
				crn.setCompartment (cDoc, comp);
			}
		}
		
		LOGGER.info ("searching for species in B");
		HashMap<String, SBMLSpecies> species = modelB.getSpecies ();
		for (SBMLSpecies s : species.values ())
		{
			DocumentNode sDoc = s.getDocumentNode ();
			Connection c = conMgmt.getConnectionForNode (sDoc);
			if (c == null)
			{
				// no equivalent in doc a
				crn.setSubstance (sDoc, new CRNSubstance (crn, null, s.getNameOrId (), null, sDoc, null, crn.getCompartment (s.getCompartment ().getDocumentNode ())));
			}
			else
			{
				CRNSubstance subst = crn.getSubstance (c.getPartnerOf (sDoc));
				subst.setDocB (sDoc);
				subst.setLabelB (s.getNameOrId ());
				subst.setCompartmentB (crn.getCompartment (s.getCompartment ().getDocumentNode ()));
				crn.setSubstance (sDoc, subst);
			}
		}
		
		LOGGER.info ("searching for reactions in B");
		HashMap<String, SBMLReaction> reactions = modelB.getReactions ();
		for (SBMLReaction r : reactions.values ())
		{
			DocumentNode rNode = r.getDocumentNode ();
			Connection c = conMgmt.getConnectionForNode (rNode);
			CRNReaction reaction = null;
			if (c == null)
			{
				// no equivalent in doc a
				reaction = new CRNReaction (crn, null, r.getNameOrId (), null, r.getDocumentNode (), r.isReversible ());
				crn.setReaction (rNode, reaction);
			}
			else
			{
				reaction = crn.getReaction (c.getPartnerOf (rNode));
				reaction.setDocB (rNode);
				crn.setReaction (rNode, reaction);
			}
			if (r.getCompartment () != null)
				reaction.setCompartmentB (crn.getCompartment (r.getCompartment ().getDocumentNode ()));
				
			Vector<SBMLSpeciesReference> sRefs = r.getReactants ();
			for (SBMLSpeciesReference sRef : sRefs)
			{
				reaction.addInputB (crn.getSubstance (sRef.getSpecies ().getDocumentNode ()), sRef.getSBOTerm ());
			}
			
			sRefs = r.getProducts ();
			for (SBMLSpeciesReference sRef : sRefs)
			{
				reaction.addOutputB (crn.getSubstance (sRef.getSpecies ().getDocumentNode ()), sRef.getSBOTerm ());
			}
			
			Vector<SBMLSimpleSpeciesReference> ssRefs = r.getModifiers ();
			for (SBMLSimpleSpeciesReference sRef : ssRefs)
			{
				SBMLSpecies spec = sRef.getSpecies ();
				//if (spec.getSBOTerm () == null)
					reaction.addModB (crn.getSubstance (spec.getDocumentNode ()), sRef.getSBOTerm ());
			}
		}
	}
	
}
