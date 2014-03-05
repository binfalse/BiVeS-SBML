/**
 * 
 */
package de.unirostock.sems.bives.sbml.algorithm;

import java.util.HashMap;

import java.util.List;

import de.binfalse.bflog.LOGGER;
import de.unirostock.sems.bives.algorithm.SimpleConnectionManager;
import de.unirostock.sems.bives.algorithm.Interpreter;
import de.unirostock.sems.bives.markup.MarkupDocument;
import de.unirostock.sems.bives.markup.MarkupElement;
import de.unirostock.sems.bives.markup.MarkupSection;
import de.unirostock.sems.bives.sbml.parser.SBMLCompartment;
import de.unirostock.sems.bives.sbml.parser.SBMLCompartmentType;
import de.unirostock.sems.bives.sbml.parser.SBMLConstraint;
import de.unirostock.sems.bives.sbml.parser.SBMLDocument;
import de.unirostock.sems.bives.sbml.parser.SBMLEvent;
import de.unirostock.sems.bives.sbml.parser.SBMLFunctionDefinition;
import de.unirostock.sems.bives.sbml.parser.SBMLInitialAssignment;
import de.unirostock.sems.bives.sbml.parser.SBMLModel;
import de.unirostock.sems.bives.sbml.parser.SBMLParameter;
import de.unirostock.sems.bives.sbml.parser.SBMLReaction;
import de.unirostock.sems.bives.sbml.parser.SBMLRule;
import de.unirostock.sems.bives.sbml.parser.SBMLSpecies;
import de.unirostock.sems.bives.sbml.parser.SBMLSpeciesType;
import de.unirostock.sems.bives.sbml.parser.SBMLUnitDefinition;
import de.unirostock.sems.xmlutils.comparison.Connection;
import de.unirostock.sems.xmlutils.ds.DocumentNode;


/**
 * The Class SBMLDiffInterpreter to interpret a mapping of SBML models.
 *
 * @author Martin Scharm
 */
public class SBMLDiffInterpreter
	extends Interpreter
{
	/** The markup document. */
	private MarkupDocument markupDocument;
	
	/** The SBML documents A and B. */
	private SBMLDocument sbmlDocA, sbmlDocB;
	
	/**
	 * Instantiates a new SBML diff interpreter.
	 *
	 * @param conMgmt the connection manager
	 * @param sbmlDocA the original document
	 * @param sbmlDocB the modified document
	 */
	public SBMLDiffInterpreter (SimpleConnectionManager conMgmt, SBMLDocument sbmlDocA,
		SBMLDocument sbmlDocB)
	{
		super (conMgmt, sbmlDocA.getTreeDocument (), sbmlDocB.getTreeDocument ());
			//report = new SBMLDiffReport ();
		markupDocument = new MarkupDocument ("SBML Differences");
			this.sbmlDocA = sbmlDocA;
			this.sbmlDocB = sbmlDocB;
	}
	
	/**
	 * Gets the produced report.
	 *
	 * @return the report
	 */
	public MarkupDocument getReport ()
	{
		return markupDocument;
	}
	

	/* (non-Javadoc)
	 * @see de.unirostock.sems.xmldiff.algorithm.Producer#produce()
	 */
	@Override
	public void interprete ()
	{
		// id's are quite critical!

		SBMLModel modelA = sbmlDocA.getModel ();
		SBMLModel modelB = sbmlDocB.getModel ();

		String lvA = "L" + sbmlDocA.getLevel () + "V" + sbmlDocA.getVersion ();
		String lvB = "L" + sbmlDocB.getLevel () + "V" + sbmlDocB.getVersion ();
		if (lvA.equals (lvB))
			markupDocument.addHeader ("Both documents have same Level/Version: " + MarkupDocument.highlight (lvA));
		else
			markupDocument.addHeader ("Level/Version has changed: from " + MarkupDocument.delete (lvA) + " to " + MarkupDocument.delete (lvB));
		
		checkUnits (modelA, modelB);
		checkParameters (modelA, modelB);
		checkCompartments (modelA, modelB);
		checkCompartmentTypes (modelA, modelB);
		checkSpecies (modelA, modelB);
		checkSpeciesTypes (modelA, modelB);
		checkReactions (modelA, modelB);
		checkRules (modelA, modelB);
		checkConstraints (modelA, modelB);
		checkInitialAssignments (modelA, modelB);
		checkFunctions (modelA, modelB);
		checkEvents (modelA, modelB);
	}
	
	/**
	 * Check the rules defined in the SBML documents.
	 *
	 * @param modelA the original model
	 * @param modelB the modified model
	 */
	private void checkRules (SBMLModel modelA, SBMLModel modelB)
	{
		MarkupSection msec = new MarkupSection ("Rules");
		LOGGER.info ("searching for rules in A");
		List<SBMLRule> rules = modelA.getRules ();
		for (SBMLRule rule : rules)
		{
			DocumentNode dn = rule.getDocumentNode ();
			LOGGER.info ("rule: ", dn.getXPath ());

			Connection con = conMgmt.getConnectionForNode (dn);
			
			if (con == null)
				msec.addValue (rule.reportDelete ());
			else
			{
				MarkupElement me = rule.reportModification (conMgmt, rule, (SBMLRule) modelB.getFromNode (con.getPartnerOf (dn)));
				if (me != null && me.getValues ().size () > 0)
				msec.addValue (me);
			}
		}
		LOGGER.info ("searching for rules in B");
		rules = modelB.getRules ();
		for (SBMLRule rule : rules)
		{
			DocumentNode dn = rule.getDocumentNode ();
			LOGGER.info ("rule: ", dn.getXPath ());

			Connection con = conMgmt.getConnectionForNode (dn);
			
			if (con == null)
				msec.addValue (rule.reportInsert());
		}
		if (msec.getValues ().size () > 0)
			markupDocument.addSection (msec);
	}
	
	/**
	 * Check compartments defined in the SBML documents.
	 *
	 * @param modelA the original model
	 * @param modelB the modified model
	 */
	private void checkCompartments (SBMLModel modelA, SBMLModel modelB)
	{
		MarkupSection msec = new MarkupSection ("Compartments");
		LOGGER.info ("searching for compartments in A");
		HashMap<String, SBMLCompartment> compartments = modelA.getCompartments ();
		for (SBMLCompartment compartment : compartments.values ())
		{
			DocumentNode dn = compartment.getDocumentNode ();
			LOGGER.info ("compartment: ", dn.getXPath ());

			Connection con = conMgmt.getConnectionForNode (dn);
			
			if (con == null)
				msec.addValue (compartment.reportDelete());
			else
			{
				MarkupElement me = compartment.reportModification (conMgmt, compartment, (SBMLCompartment) modelB.getFromNode (con.getPartnerOf (dn)));
				if (me != null && me.getValues ().size () > 0)
				msec.addValue (me);
			}
		}
		LOGGER.info ("searching for compartments in B");
		compartments = modelB.getCompartments ();
		for (SBMLCompartment compartment : compartments.values ())
		{
			DocumentNode dn = compartment.getDocumentNode ();
			LOGGER.info ("compartment: ", dn.getXPath ());

			Connection con = conMgmt.getConnectionForNode (dn);
			
			if (con == null)
				msec.addValue (compartment.reportInsert ());
		}
		if (msec.getValues ().size () > 0)
			markupDocument.addSection (msec);
	}
	
	/**
	 * Check compartment types defined in the SBML documents.
	 *
	 * @param modelA the original model
	 * @param modelB the modified model
	 */
	private void checkCompartmentTypes (SBMLModel modelA, SBMLModel modelB)
	{
		MarkupSection msec = new MarkupSection ("Compartment Types");
		LOGGER.info ("searching for compartmenttypes in A");
		HashMap<String, SBMLCompartmentType> compartmenttypes = modelA.getCompartmentTypes ();
		for (SBMLCompartmentType compartment : compartmenttypes.values ())
		{
			DocumentNode dn = compartment.getDocumentNode ();
			LOGGER.info ("compartmenttype: ", dn.getXPath ());

			Connection con = conMgmt.getConnectionForNode (dn);
			
			if (con == null)
				msec.addValue (compartment.reportDelete ());
			else
			{
				MarkupElement me = compartment.reportModification (conMgmt, compartment, (SBMLCompartmentType) modelB.getFromNode (con.getPartnerOf (dn)));
				if (me != null && me.getValues ().size () > 0)
				msec.addValue (me);
			}
		}
		LOGGER.info ("searching for compartmenttypes in B");
		compartmenttypes = modelB.getCompartmentTypes ();
		for (SBMLCompartmentType compartment : compartmenttypes.values ())
		{
			DocumentNode dn = compartment.getDocumentNode ();
			LOGGER.info ("compartmenttype: ", dn.getXPath ());

			Connection con = conMgmt.getConnectionForNode (dn);
			
			if (con == null)
				msec.addValue (compartment.reportInsert ());
		}
		if (msec.getValues ().size () > 0)
			markupDocument.addSection (msec);
	}
	
	/**
	 * Check parameters defined in the SBML documents.
	 *
	 * @param modelA the original model
	 * @param modelB the modified model
	 */
	private void checkParameters (SBMLModel modelA, SBMLModel modelB)
	{
		MarkupSection msec = new MarkupSection ("Parameters");
		LOGGER.info ("searching for parameters in A");
		HashMap<String, SBMLParameter> parameters = modelA.getParameters();
		for (SBMLParameter parameter : parameters.values ())
		{
			DocumentNode dn = parameter.getDocumentNode ();
			LOGGER.info ("parameter: ", dn.getXPath ());

			Connection con = conMgmt.getConnectionForNode (dn);
			
			if (con == null)
				msec.addValue (parameter.reportDelete ());
			else
			{
				MarkupElement me = parameter.reportModification (conMgmt, parameter, (SBMLParameter) modelB.getFromNode (con.getPartnerOf (dn)));
				if (me != null && me.getValues ().size () > 0)
				msec.addValue (me);
			}
		}
		LOGGER.info ("searching for parameters in B");
		parameters = modelB.getParameters ();
		for (SBMLParameter parameter : parameters.values ())
		{
			DocumentNode dn = parameter.getDocumentNode ();
			LOGGER.info ("parameter: ", dn.getXPath ());

			Connection con = conMgmt.getConnectionForNode (dn);
			
			if (con == null)
				msec.addValue (parameter.reportInsert ());
		}
		if (msec.getValues ().size () > 0)
			markupDocument.addSection (msec);
	}
	
	/**
	 * Check events defined in the SBML documents.
	 *
	 * @param modelA the original model
	 * @param modelB the modified model
	 */
	private void checkEvents (SBMLModel modelA, SBMLModel modelB)
	{
		MarkupSection msec = new MarkupSection ("Events");
		LOGGER.info ("searching for events in A");
		List<SBMLEvent> events = modelA.getEvents ();
		for (SBMLEvent event : events)
		{
			DocumentNode dn = event.getDocumentNode ();
			LOGGER.info ("event: ", dn.getXPath ());

			Connection con = conMgmt.getConnectionForNode (dn);
			
			if (con == null)
				msec.addValue (event.reportDelete ());
			else
			{
				MarkupElement me = event.reportModification (conMgmt, event, (SBMLEvent) modelB.getFromNode (con.getPartnerOf (dn)));
				if (me != null && me.getValues ().size () > 0)
				msec.addValue (me);
			}
		}
		LOGGER.info ("searching for events in B");
		events = modelB.getEvents ();
		for (SBMLEvent event : events)
		{
			DocumentNode dn = event.getDocumentNode ();
			LOGGER.info ("event: ", dn.getXPath ());

			Connection con = conMgmt.getConnectionForNode (dn);
			
			if (con == null)
				msec.addValue (event.reportInsert ());
		}
		if (msec.getValues ().size () > 0)
			markupDocument.addSection (msec);
	}
	
	/**
	 * Check species defined in the SBML documents.
	 *
	 * @param modelA the original model
	 * @param modelB the modified model
	 */
	private void checkSpecies (SBMLModel modelA, SBMLModel modelB)
	{
		MarkupSection msec = new MarkupSection ("Species");
		LOGGER.info ("searching for species in A");
		HashMap<String, SBMLSpecies> species = modelA.getSpecies();
		for (SBMLSpecies spec : species.values ())
		{
			DocumentNode dn = spec.getDocumentNode ();
			LOGGER.info ("species: ", dn.getXPath ());

			Connection con = conMgmt.getConnectionForNode (dn);
			
			if (con == null)
				msec.addValue (spec.reportDelete ());
			else
			{
				MarkupElement me = spec.reportModification (conMgmt, spec, (SBMLSpecies) modelB.getFromNode (con.getPartnerOf (dn)));
				if (me != null && me.getValues ().size () > 0)
				msec.addValue (me);
			}
		}
		LOGGER.info ("searching for species in B");
		species = modelB.getSpecies ();
		for (SBMLSpecies spec : species.values ())
		{
			DocumentNode dn = spec.getDocumentNode ();
			LOGGER.info ("species: ", dn.getXPath ());

			Connection con = conMgmt.getConnectionForNode (dn);
			
			if (con == null)
				msec.addValue (spec.reportInsert ());
		}
		if (msec.getValues ().size () > 0)
			markupDocument.addSection (msec);
	}
	
	/**
	 * Check species types defined in the SBML documents.
	 *
	 * @param modelA the original model
	 * @param modelB the modified model
	 */
	private void checkSpeciesTypes (SBMLModel modelA, SBMLModel modelB)
	{
		MarkupSection msec = new MarkupSection ("Species Types");
		LOGGER.info ("searching for speciestypes in A");
		HashMap<String, SBMLSpeciesType> speciestypes = modelA.getSpeciesTypes();
		for (SBMLSpeciesType spec : speciestypes.values ())
		{
			DocumentNode dn = spec.getDocumentNode ();
			LOGGER.info ("speciestype: ", dn.getXPath ());

			Connection con = conMgmt.getConnectionForNode (dn);
			
			if (con == null)
				msec.addValue (spec.reportDelete ());
			else
			{
				MarkupElement me = spec.reportModification (conMgmt, spec, (SBMLSpeciesType) modelB.getFromNode (con.getPartnerOf (dn)));
				if (me != null && me.getValues ().size () > 0)
				msec.addValue (me);
			}
		}
		LOGGER.info ("searching for speciestypes in B");
		speciestypes = modelB.getSpeciesTypes ();
		for (SBMLSpeciesType spec : speciestypes.values ())
		{
			DocumentNode dn = spec.getDocumentNode ();
			LOGGER.info ("speciestype: ", dn.getXPath ());

			Connection con = conMgmt.getConnectionForNode (dn);
			
			if (con == null)
				msec.addValue (spec.reportInsert ());
		}
		if (msec.getValues ().size () > 0)
			markupDocument.addSection (msec);
	}
	
	/**
	 * Check reactions defined in the SBML documents.
	 *
	 * @param modelA the original model
	 * @param modelB the modified model
	 */
	private void checkReactions (SBMLModel modelA, SBMLModel modelB)
	{
		MarkupSection msec = new MarkupSection ("Reactions");
		LOGGER.info ("searching for reactions in A");
		HashMap<String, SBMLReaction> reactions = modelA.getReactions();
		for (SBMLReaction reaction : reactions.values ())
		{
			DocumentNode dn = reaction.getDocumentNode ();
			LOGGER.info ("reaction: ", dn.getXPath ());

			Connection con = conMgmt.getConnectionForNode (dn);
			
			if (con == null)
				msec.addValue (reaction.reportDelete ());
			else
			{
				MarkupElement me = reaction.reportModification (conMgmt, reaction, (SBMLReaction) modelB.getFromNode (con.getPartnerOf (dn)));
				if (me != null && me.getValues ().size () > 0)
				msec.addValue (me);
			}
		}
		LOGGER.info ("searching for reactions in B");
		reactions = modelB.getReactions();
		for (SBMLReaction reaction : reactions.values ())
		{
			DocumentNode dn = reaction.getDocumentNode ();
			LOGGER.info ("reaction: ", dn.getXPath ());

			Connection con = conMgmt.getConnectionForNode (dn);
			
			if (con == null)
				msec.addValue (reaction.reportInsert ());
		}
		if (msec.getValues ().size () > 0)
			markupDocument.addSection (msec);
	}
	
	/**
	 * Check functions defined in the SBML documents.
	 *
	 * @param modelA the original model
	 * @param modelB the modified model
	 */
	private void checkFunctions (SBMLModel modelA, SBMLModel modelB)
	{
		MarkupSection msec = new MarkupSection ("Functions");
		LOGGER.info ("searching for functions in A");
		HashMap<String, SBMLFunctionDefinition> functions = modelA.getFunctionDefinitions();
		for (SBMLFunctionDefinition function : functions.values ())
		{
			DocumentNode dn = function.getDocumentNode ();
			LOGGER.info ("function: ", dn.getXPath ());

			Connection con = conMgmt.getConnectionForNode (dn);
			
			if (con == null)
				msec.addValue (function.reportDelete ());
			else
			{
				MarkupElement me = function.reportModification (conMgmt, function, (SBMLFunctionDefinition) modelB.getFromNode (con.getPartnerOf (dn)));
				if (me != null && me.getValues ().size () > 0)
				msec.addValue (me);
			}
		}
		LOGGER.info ("searching for functions in B");
		functions = modelB.getFunctionDefinitions();
		for (SBMLFunctionDefinition function : functions.values ())
		{
			DocumentNode dn = function.getDocumentNode ();
			LOGGER.info ("function: ", dn.getXPath ());

			Connection con = conMgmt.getConnectionForNode (dn);
			
			if (con == null)
				msec.addValue (function.reportInsert ());
		}
		if (msec.getValues ().size () > 0)
			markupDocument.addSection (msec);
	}
	
	/**
	 * Check units defined in the SBML documents.
	 *
	 * @param modelA the original model
	 * @param modelB the modified model
	 */
	private void checkUnits (SBMLModel modelA, SBMLModel modelB)
	{
		MarkupSection msec = new MarkupSection ("Units");
		LOGGER.info ("searching for units in A");
		HashMap<String, SBMLUnitDefinition> units = modelA.getUnitDefinitions();
		for (SBMLUnitDefinition unit : units.values ())
		{
			if (unit.isBaseUnit ())
				continue;
			DocumentNode dn = unit.getDocumentNode ();
			LOGGER.info ("unit: ", dn.getXPath ());

			Connection con = conMgmt.getConnectionForNode (dn);
			
			if (con == null)
				msec.addValue (unit.reportDelete ());
			else
			{
				MarkupElement me = unit.reportModification (conMgmt, unit, (SBMLUnitDefinition) modelB.getFromNode (con.getPartnerOf (dn)));
				if (me != null && me.getValues ().size () > 0)
				msec.addValue (me);
			}
		}
		LOGGER.info ("searching for units in B");
		units = modelB.getUnitDefinitions();
		for (SBMLUnitDefinition unit : units.values ())
		{
			if (unit.isBaseUnit ())
				continue;
			DocumentNode dn = unit.getDocumentNode ();
			LOGGER.info ("unit: ", dn.getXPath ());

			Connection con = conMgmt.getConnectionForNode (dn);
			
			if (con == null)
				msec.addValue (unit.reportInsert ());
		}
		if (msec.getValues ().size () > 0)
			markupDocument.addSection (msec);
	}
	
	/**
	 * Check initial assignments defined in the SBML documents.
	 *
	 * @param modelA the original model
	 * @param modelB the modified model
	 */
	private void checkInitialAssignments (SBMLModel modelA, SBMLModel modelB)
	{
		MarkupSection msec = new MarkupSection ("Initial Assignments");
		LOGGER.info ("searching for initial assignments in A");
		List<SBMLInitialAssignment> initAss = modelA.getInitialAssignments ();
		for (SBMLInitialAssignment ia : initAss)
		{
			DocumentNode dn = ia.getDocumentNode ();
			LOGGER.info ("init. ass.: ", dn.getXPath ());

			Connection con = conMgmt.getConnectionForNode (dn);
			
			if (con == null)
				msec.addValue (ia.reportDelete ());
			else
			{
				MarkupElement me = ia.reportModification (conMgmt, ia, (SBMLInitialAssignment) modelB.getFromNode (con.getPartnerOf (dn)));
				if (me != null && me.getValues ().size () > 0)
				msec.addValue (me);
			}
		}
		LOGGER.info ("searching for initial assignments in B");
		initAss = modelB.getInitialAssignments ();
		for (SBMLInitialAssignment ia : initAss)
		{
			DocumentNode dn = ia.getDocumentNode ();
			LOGGER.info ("init. ass.: ", dn.getXPath ());

			Connection con = conMgmt.getConnectionForNode (dn);
			
			if (con == null)
				msec.addValue (ia.reportInsert ());
		}
		if (msec.getValues ().size () > 0)
			markupDocument.addSection (msec);
	}
	
	/**
	 * Check constraints defined in the SBML documents.
	 *
	 * @param modelA the original model
	 * @param modelB the modified model
	 */
	private void checkConstraints (SBMLModel modelA, SBMLModel modelB)
	{
		MarkupSection msec = new MarkupSection ("Constraints");
		LOGGER.info ("searching for constraints in A");
		List<SBMLConstraint> constraints = modelA.getConstraints ();
		for (SBMLConstraint constraint : constraints)
		{
			DocumentNode dn = constraint.getDocumentNode ();
			LOGGER.info ("constraint: ", dn.getXPath ());

			Connection con = conMgmt.getConnectionForNode (dn);
			
			if (con == null)
				msec.addValue (constraint.reportDelete ());
			else
			{
				MarkupElement me = constraint.reportModification (conMgmt, constraint, (SBMLConstraint) modelB.getFromNode (con.getPartnerOf (dn)));
				if (me != null && me.getValues ().size () > 0)
				msec.addValue (me);
			}
		}
		LOGGER.info ("searching for constraints in B");
		constraints = modelB.getConstraints ();
		for (SBMLConstraint constraint : constraints)
		{
			DocumentNode dn = constraint.getDocumentNode ();
			LOGGER.info ("constraint: ", dn.getXPath ());

			Connection con = conMgmt.getConnectionForNode (dn);
			
			if (con == null)
				msec.addValue (constraint.reportInsert ());
		}
		if (msec.getValues ().size () > 0)
			markupDocument.addSection (msec);
	}
}
