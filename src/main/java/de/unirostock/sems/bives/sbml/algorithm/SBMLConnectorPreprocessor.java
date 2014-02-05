/**
 * 
 */
package de.unirostock.sems.bives.sbml.algorithm;

import java.util.HashMap;
import java.util.Vector;

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
import de.unirostock.sems.xmlutils.ds.TreeDocument;


/**
 * @author Martin Scharm
 *
 */
public class SBMLConnectorPreprocessor
	extends Connector
{
	private Connector preprocessor;
	private SBMLDocument sbmlDocA, sbmlDocB;

	public SBMLConnectorPreprocessor (SBMLDocument sbmlDocA, SBMLDocument sbmlDocB)
	{
		super ();
		this.sbmlDocA = sbmlDocA;
		this.sbmlDocB = sbmlDocB;
	}
	
	public SBMLConnectorPreprocessor (Connector preprocessor)
	{
		super ();
		this.preprocessor = preprocessor;
	}
	
	@Override
	public void init (TreeDocument docA, TreeDocument docB) throws BivesConnectionException
	{
		super.init (sbmlDocA.getTreeDocument (), sbmlDocB.getTreeDocument ());
		
		// not yet initialized?
		if (preprocessor == null)
		{
			// then we'll use by default an id-connector...
			IdConnector id = new IdConnector ();
			id.init (docA, docB);
			id.findConnections ();
	
			conMgmt = id.getConnections ();
		}
		else
		{
			preprocessor.init (docA, docB);
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

		// ractions
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
		
		Vector<SBMLRule> rules = modelA.getRules ();
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
		//System.out.println ("after pre:");
		//System.out.println (conMgmt);
		
	}
	
}
