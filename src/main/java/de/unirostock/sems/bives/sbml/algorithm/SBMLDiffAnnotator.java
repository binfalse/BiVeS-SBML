/**
 * 
 */
package de.unirostock.sems.bives.sbml.algorithm;

import java.util.regex.Pattern;

import org.jdom2.Element;

import de.unirostock.sems.bives.algorithm.general.DefaultDiffAnnotator;
import de.unirostock.sems.comodi.Change;
import de.unirostock.sems.comodi.ChangeFactory;
import de.unirostock.sems.comodi.branches.ComodiReason;
import de.unirostock.sems.comodi.branches.ComodiTarget;
import de.unirostock.sems.comodi.branches.ComodiXmlEntity;
import de.unirostock.sems.xmlutils.ds.TextNode;
import de.unirostock.sems.xmlutils.ds.TreeNode;


/**
 * @author Martin Scharm
 *
 */
public class SBMLDiffAnnotator
	extends DefaultDiffAnnotator
{

	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.unirostock.sems.bives.algorithm.DiffAnnotator#annotateDeletion(de.unirostock
	 * .sems.xmlutils.ds.TreeNode, org.jdom2.Element,
	 * de.unirostock.sems.comodi.ChangeFactory)
	 */
	@Override
	public Change annotateDeletion (TreeNode node, Element diffNode,
		ChangeFactory changeFac)
	{
		Change change = super.annotateDeletion (node, diffNode, changeFac);
		annotateTarget (change, node, null, diffNode, false);
		return change;
	}
	
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see de.unirostock.sems.bives.algorithm.DiffAnnotator#annotateInsertion(de.
	 * unirostock.sems.xmlutils.ds.TreeNode, org.jdom2.Element,
	 * de.unirostock.sems.comodi.ChangeFactory)
	 */
	@Override
	public Change annotateInsertion (TreeNode node, Element diffNode,
		ChangeFactory changeFac)
	{
		Change change = super.annotateInsertion (node, diffNode, changeFac);
		annotateTarget (change, null, node, diffNode, false);
		return change;
	}
	
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.unirostock.sems.bives.algorithm.DiffAnnotator#annotateMove(de.unirostock
	 * .sems.xmlutils.ds.TreeNode, de.unirostock.sems.xmlutils.ds.TreeNode,
	 * org.jdom2.Element, de.unirostock.sems.comodi.ChangeFactory, boolean)
	 */
	@Override
	public Change annotateMove (TreeNode nodeA, TreeNode nodeB, Element diffNode,
		ChangeFactory changeFac, boolean permutation)
	{
		Change change = super.annotateMove (nodeA, nodeB, diffNode, changeFac,
			permutation);
		annotateTarget (change, nodeA, nodeB, diffNode, permutation);
		return change;
	}
	
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.unirostock.sems.bives.algorithm.DiffAnnotator#annotateUpdateAttribute
	 * (de.unirostock.sems.xmlutils.ds.TreeNode,
	 * de.unirostock.sems.xmlutils.ds.TreeNode, java.lang.String,
	 * org.jdom2.Element, de.unirostock.sems.comodi.ChangeFactory)
	 */
	@Override
	public Change annotateUpdateAttribute (TreeNode nodeA, TreeNode nodeB,
		String attributeName, Element diffNode, ChangeFactory changeFac)
	{
		Change change = super.annotateUpdateAttribute (nodeA, nodeB, attributeName,
			diffNode, changeFac);
		annotateTarget (change, nodeA, nodeB, diffNode, false);
		return change;
	}
	
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.unirostock.sems.bives.algorithm.DiffAnnotator#annotateUpdateText(de.
	 * unirostock.sems.xmlutils.ds.TextNode,
	 * de.unirostock.sems.xmlutils.ds.TextNode, org.jdom2.Element,
	 * de.unirostock.sems.comodi.ChangeFactory)
	 */
	@Override
	public Change annotateUpdateText (TextNode nodeA, TextNode nodeB,
		Element diffNode, ChangeFactory changeFac)
	{
		Change change = super
			.annotateUpdateText (nodeA, nodeB, diffNode, changeFac);
		annotateTarget (change, nodeA, nodeB, diffNode, false);
		return change;
	}
	
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.unirostock.sems.bives.algorithm.DiffAnnotator#annotatePatch(java.util
	 * .String, de.unirostock.sems.comodi.ChangeFactory)
	 */
	@Override
	public void annotatePatch (String rootId, ChangeFactory changeFac)
	{
		super.annotatePatch (rootId, changeFac);
	}


	/** The XPATH to a function definition. */
	public static final Pattern	functionPath									= Pattern.compile ("^/sbml\\[\\d+\\]/model\\[\\d+\\]/listOfFunctionDefinitions\\[\\d+\\]/functionDefinition\\[\\d+\\]");
	/** The XPATH to an event definition. */
	public static final Pattern	eventPath									= Pattern.compile ("^/sbml\\[\\d+\\]/model\\[\\d+\\]/listOfEvents\\[\\d+\\]/event\\[\\d+\\]");
	/** The XPATH to a rule. */
	public static final Pattern	rulePath									= Pattern.compile ("^/sbml\\[\\d+\\]/model\\[\\d+\\]/listOfRules\\[\\d+\\]/[^/]*Rule\\[\\d+\\]");
	/** The XPATH to a machine readable annotation. */
	public static final Pattern	annotationPath									= Pattern.compile ("^/sbml\\[\\d+\\]/(.*/)*annotation\\[\\d+\\]");
	/** The XPATH to a human readable annotation. */
	public static final Pattern	descriptionPath									= Pattern.compile ("^/sbml\\[\\d+\\]/(.*/)*notes\\[\\d+\\]");
	/** The XPATH to a species. */
	public static final Pattern	speciesPath									= Pattern.compile ("^/sbml\\[\\d+\\]/model\\[\\d+\\]/listOfSpecies\\[\\d+\\]/species\\[\\d+\\]$");


	/** The XPATH to a reaction. */
	public static final Pattern	reactionsPath									= Pattern.compile ("^/sbml\\[\\d+\\]/model\\[\\d+\\]/listOfReactions\\[\\d+\\]/reaction\\[\\d+\\]");
	/** The XPATH to a kinetic law. */
	public static final Pattern	kineticsPath									= Pattern.compile ("^/sbml\\[\\d+\\]/model\\[\\d+\\]/listOfReactions\\[\\d+\\]/reaction\\[\\d+\\]/kineticLaw\\[\\d+\\]");


	/** The XPATH to a reaction. */
	public static final Pattern	unitsPath									= Pattern.compile ("^/sbml\\[\\d+\\]/model\\[\\d+\\]/listOfUnitDefinitions\\[\\d+\\]/unitDefinition\\[\\d+\\]");
	
	/** The XPATH to a parameter. */
	public static final Pattern	parameterPath									= Pattern.compile ("^/sbml\\[\\d+\\]/model\\[\\d+\\](/listOfReactions\\[\\d+\\]/reaction\\[\\d+\\]/kineticLaw\\[\\d+\\])?/listOf(Local)?Parameters\\[\\d+\\]/(localP|p)arameter\\[\\d+\\]");
	
	/** The XPATH to a machine readable annotation. */
	public static final Pattern	creationDatePath									= Pattern.compile ("^/sbml\\[\\d+\\]/(.*/)*annotation\\[\\d+\\]/(.*/)*created\\[\\d+\\]");
	/** The XPATH to a machine readable annotation. */
	public static final Pattern	modificationDatePath									= Pattern.compile ("^/sbml\\[\\d+\\]/(.*/)*annotation\\[\\d+\\]/(.*/)*modified\\[\\d+\\]");
	/** The XPATH to a machine readable annotation. */
	public static final Pattern	contributorPath									= Pattern.compile ("^/sbml\\[\\d+\\]/(.*/)*annotation\\[\\d+\\]/(.*/)*contributor\\[\\d+\\]");
	/** The XPATH to a machine readable annotation. */
	public static final Pattern	creatorPath									= Pattern.compile ("^/sbml\\[\\d+\\]/(.*/)*annotation\\[\\d+\\]/(.*/)*creator\\[\\d+\\]");
	

	public static final Pattern	reactionsSpeciesReference									= Pattern.compile ("^/sbml\\[\\d+\\]/model\\[\\d+\\]/listOfReactions\\[\\d+\\]/reaction\\[\\d+\\]/(.*/)*speciesReference\\[\\d+\\]");
	public static final Pattern	reactionsModifierSpeciesReference									= Pattern.compile ("^/sbml\\[\\d+\\]/model\\[\\d+\\]/listOfReactions\\[\\d+\\]/reaction\\[\\d+\\]/(.*/)*modifierSpeciesReference\\[\\d+\\]");
	
	public static final Pattern	ontologyLink									= Pattern.compile ("^/sbml\\[\\d+\\]/(.*/)*RDF\\[\\d+\\]/(.*/)*Description\\[\\d+\\]/(.*/)*Bag\\[\\d+\\]/li\\[\\d+\\]");

	
	
	
		
		
	
	
	/**
	 * Annotate the target of a change. Recognises the effected parts of the
	 * document and annotates the change with a term from COMODI.
	 * 
	 * @param change
	 *          the change
	 * @param nodeA
	 *          the node a
	 * @param nodeB
	 *          the node b
	 * @param diffNode
	 *          the diff node
	 * @param permutation
	 *          the permutation
	 * @return the change
	 */
	private Change annotateTarget (Change change, TreeNode nodeA, TreeNode nodeB,
		Element diffNode, boolean permutation)
	{
		// as nodeA or nodeB might be null, but we don't care at some points, we
		// just need one of them which is definietely not 0...
		TreeNode defNode = nodeA == null ? nodeB : nodeA;
		// the xpath in one of the documents, no matter if old or new doc
		String xPath = diffNode.getAttributeValue ("newPath") == null ? diffNode
			.getAttributeValue ("oldPath") : diffNode.getAttributeValue ("newPath");

			
			if (defNode.getParent () == null
				&& diffNode.getName ().equals ("attribute"))
			{
				String attr = diffNode.getAttributeValue ("name");
				if (attr.equals ("level") || attr.equals ("version"))
					change.hasReason (ComodiReason.getChangedSpecification ());
			}
			
			
			if (defNode.getXPath ().equals ("/sbml[1]/model[1]")
				&& diffNode.getName ().equals ("attribute"))
			{
				String attr = diffNode.getAttributeValue ("name");
				if (attr.equals ("id") || attr.equals ("metaid"))
					change.appliesTo (ComodiXmlEntity.getModelId ());
				else if (attr.equals ("name"))
					change.appliesTo (ComodiXmlEntity.getModelName ());
			}
			
			
			boolean isAnnotation = annotationPath.matcher (xPath).find () || descriptionPath.matcher (xPath).find ();

			
			
			if (parameterPath.matcher (xPath).find () && !isAnnotation)
			{
				if (diffNode.getName ().equals ("attribute"))
				{
					String attr = diffNode.getAttributeValue ("name");
					if (attr.equals ("id") || attr.equals ("metaid"))
						change.appliesTo (ComodiXmlEntity.getEntityIdentifier ());
					else if (attr.equals ("name"))
						change.appliesTo (ComodiXmlEntity.getEntityName ());
					else
						change.affects (ComodiTarget.getParameterSetup ());
				}
				else
					if (!permutation)
						change.affects (ComodiTarget.getParameterSetup ());
			}
			
			
			
			else if (speciesPath.matcher (xPath).find () && !isAnnotation)
			{
				if (diffNode.getName ().equals ("attribute"))
				{
					String attr = diffNode.getAttributeValue ("name");
					if (attr.equals ("id") || attr.equals ("metaid"))
						change.appliesTo (ComodiXmlEntity.getEntityIdentifier ());
					else if (attr.equals ("name"))
						change.appliesTo (ComodiXmlEntity.getEntityName ());
					else
					{
						if (attr.equals ("initialConcentration") || 
							attr.equals ("initialAmount") || 
							attr.equals ("substanceUnits") || 
							attr.equals ("constant") || 
							attr.equals ("hasOnlySubstanceUnits"))
							change.affects (ComodiTarget.getSpeciesSetup ());
						else if (attr.equals ("sboTerm"))
							change.affects (ComodiTarget.getReactionNetworkDefinition ());
					}
				}
				else
				{
					if (!permutation)
						change.affects (ComodiTarget.getSpeciesSetup ());
				}
			}

			
			
			
			
			else if (eventPath.matcher (xPath).find () && !isAnnotation)
				if (diffNode.getName ().equals ("attribute"))
				{
					String attr = diffNode.getAttributeValue ("name");
					if (attr.equals ("id") || attr.equals ("metaid"))
						change.appliesTo (ComodiXmlEntity.getEntityIdentifier ());
					else if (attr.equals ("name"))
						change.appliesTo (ComodiXmlEntity.getEntityName ());
					else
						change.affects (ComodiTarget.getEventDefinition ());
				}
				else
					change.affects (ComodiTarget.getEventDefinition ());

			
			
			
			
			else if (functionPath.matcher (xPath).find () && !isAnnotation)
				if (diffNode.getName ().equals ("attribute"))
				{
					String attr = diffNode.getAttributeValue ("name");
					if (attr.equals ("id") || attr.equals ("metaid"))
						change.appliesTo (ComodiXmlEntity.getEntityIdentifier ());
					else if (attr.equals ("name"))
						change.appliesTo (ComodiXmlEntity.getEntityName ());
					else
						change.affects (ComodiTarget.getFunctionDefinition ());
				}
				else
					change.affects (ComodiTarget.getFunctionDefinition ());

			
			
			
			
			else if (rulePath.matcher (xPath).find () && !isAnnotation)
				if (diffNode.getName ().equals ("attribute"))
				{
					String attr = diffNode.getAttributeValue ("name");
					if (attr.equals ("id") || attr.equals ("metaid"))
						change.appliesTo (ComodiXmlEntity.getEntityIdentifier ());
					else if (attr.equals ("name"))
						change.appliesTo (ComodiXmlEntity.getEntityName ());
					else
						change.affects (ComodiTarget.getRuleDefinition ());
				}
				else
					change.affects (ComodiTarget.getRuleDefinition ());

			
			
			
			
			else if (unitsPath.matcher (xPath).find () && !isAnnotation)
				if (diffNode.getName ().equals ("attribute"))
				{
					String attr = diffNode.getAttributeValue ("name");
					if (attr.equals ("id") || attr.equals ("metaid"))
						change.appliesTo (ComodiXmlEntity.getEntityIdentifier ());
					else if (attr.equals ("name"))
						change.appliesTo (ComodiXmlEntity.getEntityName ());
					else
						change.affects (ComodiTarget.getUnitDefinition ());
				}
				else
					change.affects (ComodiTarget.getUnitDefinition ());

			
			
			
			
			else if (functionPath.matcher (xPath).find () && !isAnnotation)
				if (diffNode.getName ().equals ("attribute"))
				{
					String attr = diffNode.getAttributeValue ("name");
					if (attr.equals ("id") || attr.equals ("metaid"))
						change.appliesTo (ComodiXmlEntity.getEntityIdentifier ());
					else if (attr.equals ("name"))
						change.appliesTo (ComodiXmlEntity.getEntityName ());
					else
						change.affects (ComodiTarget.getFunctionDefinition ());
				}
				else
					change.affects (ComodiTarget.getFunctionDefinition ());
			
			
			else if (reactionsPath.matcher (xPath).find () && !isAnnotation)
			{
				boolean go = true; // getting tired..
				if (diffNode.getName ().equals ("attribute"))
				{
					String attr = diffNode.getAttributeValue ("name");
					if (attr.equals ("id") || attr.equals ("metaid"))
					{
						change.appliesTo (ComodiXmlEntity.getEntityIdentifier ());
						go = false;
					}
					else if (attr.equals ("name"))
					{
						change.appliesTo (ComodiXmlEntity.getEntityName ());
						go = false;
					}
					else if (attr.equals ("reversible"))
					{
						change.affects (ComodiTarget.getReversibilityDefinition ());
						go = false;
					}
					else if ((reactionsSpeciesReference.matcher (xPath).find () || reactionsModifierSpeciesReference.matcher (xPath).find ()) && attr.equals ("sboTerm"))
					{
						change.affects (ComodiTarget.getParticipantDefinition ());
						go = false;
					}
					else if (attr.equals ("sboTerm"))
					{
						change.affects (ComodiTarget.getReactionDefinition ());
						go = false;
					}
				}
				
				if (go)
				{
					if (kineticsPath.matcher (xPath).find ())
						change.affects (ComodiTarget.getKineticsDefinition ());
					else if (defNode.getTagName ().equals ("reaction") && defNode.getParent ().getTagName ().equals ("listOfReactions"))
						change.affects (ComodiTarget.getReactionDefinition ());
					else if (reactionsSpeciesReference.matcher (xPath).find () || reactionsModifierSpeciesReference.matcher (xPath).find ())
						change.affects (ComodiTarget.getParticipantDefinition ());
					else
						change.affects (ComodiTarget.getReactionNetworkDefinition ());
				}
			}
			
			
			if (diffNode.getName ().equals ("attribute") && !isAnnotation && nodeA != null && nodeB != null)
			{
				if (diffNode.getAttributeValue ("name").equals ("metaid"))
				{
					String oldValue = diffNode.getAttributeValue ("oldValue");
					String newValue = diffNode.getAttributeValue ("newValue");
					boolean go = true;
					
					int countA = oldValue.length() - oldValue.replace("_", "").length();
					int countB = newValue.length() - oldValue.replace("_", "").length();
					if (go && countA != countB)
					{
						change.affects (ComodiTarget.getMetaIdEncoding ());
						go = false;
					}
					
					countA = oldValue.length() - oldValue.replace("#", "").length();
					countB = newValue.length() - oldValue.replace("#", "").length();
					if (go && countA != countB)
					{
						change.affects (ComodiTarget.getMetaIdEncoding ());
						go = false;
					}
					
					countA = oldValue.startsWith ("_") ? 1 : 0;
					countB = newValue.startsWith ("_") ? 1 : 0;
					if (go && countA != countB)
					{
						change.affects (ComodiTarget.getMetaIdEncoding ());
						go = false;
					}
					
					countA = oldValue.startsWith ("#") ? 1 : 0;
					countB = newValue.startsWith ("#") ? 1 : 0;
					if (go && countA != countB)
					{
						change.affects (ComodiTarget.getMetaIdEncoding ());
						go = false;
					}
					
					countA = oldValue.startsWith ("metaid") ? 1 : 0;
					countB = newValue.startsWith ("metaid") ? 1 : 0;
					if (go && countA != countB)
					{
						change.affects (ComodiTarget.getMetaIdEncoding ());
						go = false;
					}
					
					
				}
			}
			
			
			if (isAnnotation)
			{
				if (annotationPath.matcher (xPath).find ())
				{
					if (creatorPath.matcher (xPath).find ())
						change.affects (ComodiTarget.getCreator ());
					else if (contributorPath.matcher (xPath).find ())
						change.affects (ComodiTarget.getContributor ());
					else if (creationDatePath.matcher (xPath).find ())
						change.affects (ComodiTarget.getCreationDate ());
					else if (modificationDatePath.matcher (xPath).find ())
						change.affects (ComodiTarget.getModificationDate ());
					else if (ontologyLink.matcher (xPath).find ())
					{
						// is that annotation attribute? -> scheme changed?
						if (diffNode.getName ().equals ("attribute"))
						{
							String attr = diffNode.getAttributeValue ("name");
							if (attr.equals ("resource") && nodeA != null && nodeB != null)
							{
								change.affects (ComodiTarget.getOntologyReference ());
								String oldValue = diffNode.getAttributeValue ("oldValue");
								String newValue = diffNode.getAttributeValue ("newValue");
								if (oldValue != null && newValue != null)
								{
									if ((oldValue.startsWith ("urn:") && !newValue.startsWith ("urn:")) || (!oldValue.startsWith ("urn:") && newValue.startsWith ("urn:")))
									{
										change.affects (ComodiTarget.getIdentifierEncoding ());
									}
									if ((oldValue.startsWith ("http:") && !newValue.startsWith ("http:")) || (!oldValue.startsWith ("http:") && newValue.startsWith ("http:")))
									{
										change.affects (ComodiTarget.getIdentifierEncoding ());
									}
									if ((oldValue.contains ("identifiers.org") && !newValue.contains ("identifiers.org")) || (!oldValue.contains ("identifiers.org") && newValue.contains ("identifiers.org")))
									{
										change.affects (ComodiTarget.getIdentifierEncoding ());
									}
								}
							}
							else
								change.affects (ComodiTarget.getModelAnnotation ());
						}
						else
							change.affects (ComodiTarget.getModelAnnotation ());
					}
					else
						change.affects (ComodiTarget.getModelAnnotation ());
				}
				else if (descriptionPath.matcher (xPath).find ())
				{
					change.affects (ComodiTarget.getTextualDescription ());
				}
			}
			
			
		return change;
	}
}
