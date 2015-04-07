/**
 * 
 */
package de.unirostock.sems.bives.sbml.algorithm;

import java.util.HashMap;
import java.util.List;

import de.unirostock.sems.bives.algorithm.Connector;
import de.unirostock.sems.bives.algorithm.NodeConnection;
import de.unirostock.sems.bives.algorithm.general.IdConnector;
import de.unirostock.sems.bives.exception.BivesConnectionException;
import de.unirostock.sems.bives.sbml.parser.SBMLAssignmentRule;
import de.unirostock.sems.bives.sbml.parser.SBMLDocument;
import de.unirostock.sems.bives.sbml.parser.SBMLListOf;
import de.unirostock.sems.bives.sbml.parser.SBMLModel;
import de.unirostock.sems.bives.sbml.parser.SBMLRateRule;
import de.unirostock.sems.bives.sbml.parser.SBMLReaction;
import de.unirostock.sems.bives.sbml.parser.SBMLRule;
import de.unirostock.sems.bives.sbml.parser.SBMLSBase;


/**
 * The Class SBMLConnectorPreprocessor to pre-compute a mapping.
 *
 * @author Martin Scharm
 */
public class SBMLConnectorPreprocessor
	extends Connector
{
	
	/** The preprocessor. */
	private Connector preprocessor;
	
	/** The SBML documents A and B. */
	private SBMLDocument sbmlDocA, sbmlDocB;

	/**
	 * Instantiates a new sBML connector preprocessor.
	 *
	 * @param sbmlDocA the original document
	 * @param sbmlDocB the modified document
	 */
	public SBMLConnectorPreprocessor (SBMLDocument sbmlDocA, SBMLDocument sbmlDocB)
	{
		super (sbmlDocA.getTreeDocument (), sbmlDocB.getTreeDocument ());
		this.sbmlDocA = sbmlDocA;
		this.sbmlDocB = sbmlDocB;
	}
	
	/**
	 * Instantiates a new sBML connector preprocessor.
	 *
	 * @param preprocessor the preprocessor
	 */
	public SBMLConnectorPreprocessor (Connector preprocessor)
	{
		super (preprocessor.getDocA (), preprocessor.getDocB ());
		this.preprocessor = preprocessor;
	}
	
	/* (non-Javadoc)
	 * @see de.unirostock.sems.bives.algorithm.Connector#init()
	 */
	@Override
	protected void init () throws BivesConnectionException
	{
		
		// not yet initialized?
		if (preprocessor == null)
		{
			// then we'll use by default an id-connector...
			IdConnector id = new IdConnector (docA, docB, true);
			id.findConnections ();
	
			conMgmt = id.getConnections ();
		}
		else
		{
			preprocessor.findConnections ();
	
			conMgmt = preprocessor.getConnections ();
		}
		
	}
	
	
	/* (non-Javadoc)
	 * @see de.unirostock.sems.xmldiff.algorithm.Connector#findConnections()
	 */
	@Override
	protected void connect () throws BivesConnectionException
	{
		SBMLModel modelA = sbmlDocA.getModel ();
		SBMLModel modelB = sbmlDocB.getModel ();
		
		// connect the nodes that have the same annotations
		{// gc: please delete those structures...
			HashMap<String, List<SBMLSBase>> ontoMapA = modelA.getOntologyMappings ();
			if (ontoMapA != null && ontoMapA.size () > 0)
			{
				HashMap<String, List<SBMLSBase>> ontoMapB = modelB.getOntologyMappings ();
				for (String id : ontoMapB.keySet ())
				{
					List<SBMLSBase> aBases = ontoMapA.get (id);
					if (aBases != null && aBases.size () == 1)
					{
						List<SBMLSBase> bBases = ontoMapB.get (id);
						if (bBases.size () == 1)
						{
							// map these two nodes! :)
							nodeAssign (aBases.get (0), bBases.get (0));
						}
					}
				}
			}
		}
		

		// reactions
		HashMap<String, SBMLReaction> reactionsA = modelA.getReactions ();
		HashMap<String, SBMLReaction> reactionsB = modelB.getReactions ();
		for (String id : reactionsA.keySet ())
		{
			SBMLReaction rB = reactionsB.get (id);
			if (rB == null)
				continue;
			SBMLReaction rA = reactionsA.get (id);

			if (conMgmt.getConnectionForNode (rA.getDocumentNode ()) == null && conMgmt.getConnectionForNode (rB.getDocumentNode ()) == null)
			{
				conMgmt.addConnection (new NodeConnection (rA.getDocumentNode (), rB.getDocumentNode ()));
			}
			else
			{
				if (conMgmt.getConnectionForNode (rA.getDocumentNode ()) == null || conMgmt.getConnectionForNode (rB.getDocumentNode ()) == null)
					continue;
				if (conMgmt.getConnectionForNode (rA.getDocumentNode ()).getPartnerOf (rA.getDocumentNode ()) != rB.getDocumentNode ())
					continue;
			}
			
			SBMLListOf loA = rA.getListOfReactantsNode (), loB = rB.getListOfReactantsNode ();
			if (loA != null && loB != null)
				conMgmt.addConnection (new NodeConnection (loA.getDocumentNode (), loB.getDocumentNode ()));
			
			loA = rA.getListOfProductsNode ();
			loB = rB.getListOfProductsNode ();
			if (loA != null && loB != null)
				conMgmt.addConnection (new NodeConnection (loA.getDocumentNode (), loB.getDocumentNode ()));
			
			loA = rA.getListOfModifiersNode ();
			loB = rB.getListOfModifiersNode ();
			if (loA != null && loB != null)
				conMgmt.addConnection (new NodeConnection (loA.getDocumentNode (), loB.getDocumentNode ()));
		}
		
		
		// rules
		HashMap<SBMLSBase, SBMLRule> aRuleMapper = new HashMap<SBMLSBase, SBMLRule> ();
		HashMap<SBMLSBase, SBMLRule> rRuleMapper = new HashMap<SBMLSBase, SBMLRule> ();
		
		List<SBMLRule> rules = modelA.getRules ();
		for (SBMLRule rule : rules)
		{
			if (rule.getRuleType () == SBMLRule.ASSIGNMENT_RULE)
			{
				aRuleMapper.put (((SBMLAssignmentRule) rule).getVariable (), rule);
			}
			if (rule.getRuleType () == SBMLRule.RATE_RULE)
			{
				rRuleMapper.put (((SBMLRateRule) rule).getVariable (), rule);
			}
		}
		rules = modelB.getRules ();
		for (SBMLRule rule : rules)
		{
			if (rule.getRuleType () == SBMLRule.ASSIGNMENT_RULE)
			{
				SBMLRule a = aRuleMapper.get (((SBMLAssignmentRule) rule).getVariable ());
				if (a != null)
					conMgmt.addConnection (new NodeConnection (a.getDocumentNode (), rule.getDocumentNode ()));
			}
			if (rule.getRuleType () == SBMLRule.RATE_RULE)
			{
				SBMLRule a = rRuleMapper.get (((SBMLRateRule) rule).getVariable ());
				if (a != null)
					conMgmt.addConnection (new NodeConnection (a.getDocumentNode (), rule.getDocumentNode ()));
			}
		}
	}
	
	private boolean nodeAssign (SBMLSBase sA, SBMLSBase sB) throws BivesConnectionException
	{
		if (sA == null || sB == null)
			return false;
		
		return nodeAssign (sA.getDocumentNode (), sB.getDocumentNode ());
	}
}
