/**
 * 
 */
package de.unirostock.sems.bives.sbml.parser;

import java.util.HashMap;

import java.util.ArrayList;
import java.util.List;

import de.binfalse.bflog.LOGGER;
import de.unirostock.sems.bives.exception.BivesDocumentConsistencyException;
import de.unirostock.sems.bives.sbml.exception.BivesSBMLParseException;
import de.unirostock.sems.xmlutils.ds.DocumentNode;
import de.unirostock.sems.xmlutils.ds.TreeNode;


/**
 * The Class SBMLModel representing a model encoded in SBML.
 *
 * @author Martin Scharm
 */
public class SBMLModel
	extends SBMLSBase
{
	
	/** The document. */
	private SBMLDocument document;

	/** The node mapper. */
	private HashMap<TreeNode, SBMLSBase> nodeMapper;
	
	/** The list of function definitions. */
	private HashMap<String, SBMLFunctionDefinition> listOfFunctionDefinitions;
	
	/** The list of unit definitions. */
	private HashMap<String, SBMLUnitDefinition> listOfUnitDefinitions;
	
	/** The list of compartments. */
	private HashMap<String, SBMLCompartment> listOfCompartments;
	
	/** The list of compartment types. */
	private HashMap<String, SBMLCompartmentType> listOfCompartmentTypes;
	
	/** The list of species. */
	private HashMap<String, SBMLSpecies> listOfSpecies;
	
	/** The list of species types. */
	private HashMap<String, SBMLSpeciesType> listOfSpeciesTypes;
	
	/** The list of parameters. */
	private HashMap<String, SBMLParameter> listOfParameters;
	
	/** The list of initial assignments. */
	private List<SBMLInitialAssignment> listOfInitialAssignments;
	
	/** The list of rules. */
	private List<SBMLRule> listOfRules;
	
	/** The list of constraints. */
	private List<SBMLConstraint> listOfConstraints;
	
	/** The list of reactions. */
	private HashMap<String, SBMLReaction> listOfReactions;
	
	/** The list of events. */
	private List<SBMLEvent> listOfEvents;
	
	/** The list of species references. */
	private HashMap<String, SBMLSimpleSpeciesReference> listOfSpeciesReferences;

	/** The id. */
	private String id; //optional
	
	/** The name. */
	private String name; //optional
	
	/** The substance units. */
	private SBMLUnitDefinition substanceUnits; //optional
	
	/** The time units. */
	private SBMLUnitDefinition timeUnits; //optional
	
	/** The volume units. */
	private SBMLUnitDefinition volumeUnits; //optional
	
	/** The area units. */
	private SBMLUnitDefinition areaUnits; //optional
	
	/** The length units. */
	private SBMLUnitDefinition lengthUnits; //optional
	
	/** The extent units. */
	private SBMLUnitDefinition extentUnits; //optional
	
	/** The conversion factor. */
	private SBMLParameter conversionFactor; //optional
	
	/**
	 * Instantiates a new SBML model.
	 *
	 * @param documentNode the document node encoding this entity in the corresponding XML tree
	 * @param sbmlDocument the SBML document
	 * @throws BivesSBMLParseException the bives sbml parse exception
	 * @throws BivesDocumentConsistencyException the bives document consistency exception
	 */
	public SBMLModel (DocumentNode documentNode, SBMLDocument sbmlDocument)
		throws BivesSBMLParseException, BivesDocumentConsistencyException
	{
		super (documentNode, null);
		sbmlModel = this;
		this.document = sbmlDocument;
		
		nodeMapper = new HashMap<TreeNode, SBMLSBase> ();
		
		listOfFunctionDefinitions = new  HashMap<String, SBMLFunctionDefinition> ();
		listOfUnitDefinitions = new  HashMap<String, SBMLUnitDefinition> ();
		listOfCompartments = new  HashMap<String, SBMLCompartment> ();
		listOfCompartmentTypes = new  HashMap<String, SBMLCompartmentType> ();
		listOfSpecies = new  HashMap<String, SBMLSpecies> ();
		listOfSpeciesTypes = new  HashMap<String, SBMLSpeciesType> ();
		listOfParameters = new  HashMap<String, SBMLParameter> ();
		listOfInitialAssignments = new  ArrayList<SBMLInitialAssignment> ();
		listOfRules = new  ArrayList<SBMLRule> ();
		listOfConstraints = new  ArrayList<SBMLConstraint> ();
		listOfReactions = new  HashMap<String, SBMLReaction> ();
		listOfEvents = new  ArrayList<SBMLEvent> ();
		listOfSpeciesReferences = new  HashMap<String, SBMLSimpleSpeciesReference> ();
		
		parseTree ();
		
	}
	
	/**
	 * Parses the tree.
	 *
	 * @throws BivesSBMLParseException the bives sbml parse exception
	 * @throws BivesDocumentConsistencyException the bives document consistency exception
	 */
	private void parseTree () throws BivesSBMLParseException, BivesDocumentConsistencyException
	{
		DocumentNode modelRoot = documentNode;
		
		// sequence important!
		parseFunctions (modelRoot);
		parseUnits (modelRoot);
		parseCompartmentTypes (modelRoot);
		parseCompartments (modelRoot);
		parseParameters(modelRoot);
		parseSpeciesTypes (modelRoot);
		parseSpecies (modelRoot);
		parseReactions (modelRoot);
		parseInitialAssignments (modelRoot);
		parseRules (modelRoot);
		parseConstraints (modelRoot);
		parseEvents (modelRoot);
		
		parseModelRoot (modelRoot);
	}

	/**
	 * Parses the model root.
	 *
	 * @param modelRoot the node rooting the model
	 * @throws BivesSBMLParseException the bives sbml parse exception
	 */
	private void parseModelRoot (DocumentNode modelRoot) throws BivesSBMLParseException
	{
		id = documentNode.getAttributeValue ("id");
		name = documentNode.getAttributeValue ("name");
		
		if (documentNode.getAttributeValue ("substanceUnits") != null)
		{
			String tmp = documentNode.getAttributeValue ("substanceUnits");
			substanceUnits = sbmlModel.getUnitDefinition (tmp);
			if (substanceUnits == null)
				throw new BivesSBMLParseException ("substanceUnits attribute in model root not defined: " + tmp);
		}
		
		if (documentNode.getAttributeValue ("timeUnits") != null)
		{
			String tmp = documentNode.getAttributeValue ("timeUnits");
			timeUnits = sbmlModel.getUnitDefinition (tmp);
			if (timeUnits == null)
				throw new BivesSBMLParseException ("timeUnits attribute in model root not defined: " + tmp);
		}
		
		if (documentNode.getAttributeValue ("volumeUnits") != null)
		{
			String tmp = documentNode.getAttributeValue ("volumeUnits");
			volumeUnits = sbmlModel.getUnitDefinition (tmp);
			if (volumeUnits == null)
				throw new BivesSBMLParseException ("volumeUnits attribute in model root not defined: " + tmp);
		}
		
		if (documentNode.getAttributeValue ("areaUnits") != null)
		{
			String tmp = documentNode.getAttributeValue ("areaUnits");
			areaUnits = sbmlModel.getUnitDefinition (tmp);
			if (areaUnits == null)
				throw new BivesSBMLParseException ("areaUnits attribute in model root not defined: " + tmp);
		}
		
		if (documentNode.getAttributeValue ("lengthUnits") != null)
		{
			String tmp = documentNode.getAttributeValue ("lengthUnits");
			lengthUnits = sbmlModel.getUnitDefinition (tmp);
			if (lengthUnits == null)
				throw new BivesSBMLParseException ("lengthUnits attribute in model root not defined: " + tmp);
		}
		
		if (documentNode.getAttributeValue ("extentUnits") != null)
		{
			String tmp = documentNode.getAttributeValue ("extentUnits");
			extentUnits = sbmlModel.getUnitDefinition (tmp);
			if (extentUnits == null)
				throw new BivesSBMLParseException ("extentUnits attribute in model root not defined: " + tmp);
		}
		
		if (documentNode.getAttributeValue ("conversionFactor") != null)
		{
			String tmp = documentNode.getAttributeValue ("conversionFactor");
			conversionFactor = sbmlModel.getParameter (tmp);
			if (conversionFactor == null)
				throw new BivesSBMLParseException ("conversionFactor attribute in model root not defined: " + tmp);
		}
		
	}

	/**
	 * Parses the events.
	 *
	 * @param root the root
	 * @throws BivesSBMLParseException the bives sbml parse exception
	 */
	private void parseEvents (DocumentNode root) throws BivesSBMLParseException
	{
		List<TreeNode> loss = root.getChildrenWithTag ("listOfEvents");
		for (int i = 0; i < loss.size (); i++)
		{
			DocumentNode los = (DocumentNode) loss.get (i);
			
			List<TreeNode> node = los.getChildrenWithTag ("event");
			for (int j = 0; j < node.size (); j++)
			{
				SBMLEvent n = new SBMLEvent ((DocumentNode) node.get (j), this);
				listOfEvents.add (n);
			}
		}
	}

	/**
	 * Parses the reactions.
	 *
	 * @param root the root
	 * @throws BivesSBMLParseException the bives sbml parse exception
	 */
	private void parseReactions (DocumentNode root) throws BivesSBMLParseException
	{
		List<TreeNode> loss = root.getChildrenWithTag ("listOfReactions");
		for (int i = 0; i < loss.size (); i++)
		{
			DocumentNode los = (DocumentNode) loss.get (i);
			
			List<TreeNode> node = los.getChildrenWithTag ("reaction");
			for (int j = 0; j < node.size (); j++)
			{
				SBMLReaction n = new SBMLReaction ((DocumentNode) node.get (j), this);
				listOfReactions.put (n.getID (), n);
			}
		}
		
	}

	/**
	 * Parses the constraints.
	 *
	 * @param root the root
	 * @throws BivesSBMLParseException the bives sbml parse exception
	 */
	private void parseConstraints (DocumentNode root) throws BivesSBMLParseException
	{
		List<TreeNode> loss = root.getChildrenWithTag ("listOfConstraints");
		for (int i = 0; i < loss.size (); i++)
		{
			DocumentNode los = (DocumentNode) loss.get (i);
			
			List<TreeNode> node = los.getChildrenWithTag ("constraint");
			for (int j = 0; j < node.size (); j++)
			{
				SBMLConstraint n = new SBMLConstraint ((DocumentNode) node.get (j), this);
				listOfConstraints.add (n);
			}
		}
		
	}

	/**
	 * Parses the rules.
	 *
	 * @param root the root
	 * @throws BivesSBMLParseException the bives sbml parse exception
	 */
	private void parseRules (DocumentNode root) throws BivesSBMLParseException
	{
		List<TreeNode> loss = root.getChildrenWithTag ("listOfRules");
		for (int i = 0; i < loss.size (); i++)
		{
			DocumentNode los = (DocumentNode) loss.get (i);
			
			List<TreeNode> node = los.getChildrenWithTag ("algebraicRule");
			for (int j = 0; j < node.size (); j++)
			{
				SBMLAlgebraicRule n = new SBMLAlgebraicRule ((DocumentNode) node.get (j), this);
				listOfRules.add (n);
			}
			
			node = los.getChildrenWithTag ("assignmentRule");
			for (int j = 0; j < node.size (); j++)
			{
				SBMLAssignmentRule n = new SBMLAssignmentRule ((DocumentNode) node.get (j), this);
				listOfRules.add (n);
			}
			
			node = los.getChildrenWithTag ("rateRule");
			for (int j = 0; j < node.size (); j++)
			{
				SBMLRateRule n = new SBMLRateRule ((DocumentNode) node.get (j), this);
				listOfRules.add (n);
			}
		}
	}

	/**
	 * Parses the initial assignments.
	 *
	 * @param root the root
	 * @throws BivesSBMLParseException the bives sbml parse exception
	 */
	private void parseInitialAssignments (DocumentNode root) throws BivesSBMLParseException
	{
		List<TreeNode> loss = root.getChildrenWithTag ("listOfInitialAssignments");
		for (int i = 0; i < loss.size (); i++)
		{
			DocumentNode los = (DocumentNode) loss.get (i);
			
			List<TreeNode> node = los.getChildrenWithTag ("initialAssignment");
			for (int j = 0; j < node.size (); j++)
			{
				SBMLInitialAssignment n = new SBMLInitialAssignment ((DocumentNode) node.get (j), this);
				listOfInitialAssignments.add (n);
			}
		}
	}

	/**
	 * Parses the species.
	 *
	 * @param root the root
	 * @throws BivesSBMLParseException the bives sbml parse exception
	 */
	private void parseSpecies (DocumentNode root) throws BivesSBMLParseException
	{
		List<TreeNode> lospeciess = root.getChildrenWithTag ("listOfSpecies");
		for (int i = 0; i < lospeciess.size (); i++)
		{
			DocumentNode lospecies = (DocumentNode) lospeciess.get (i);
			
			List<TreeNode> species = lospecies.getChildrenWithTag ("species");
			for (int j = 0; j < species.size (); j++)
			{
				SBMLSpecies s = new SBMLSpecies ((DocumentNode) species.get (j), this);
				listOfSpecies.put (s.getID (), s);
			}
		}
	}

	/**
	 * Parses the species types.
	 *
	 * @param root the root
	 * @throws BivesSBMLParseException the bives sbml parse exception
	 */
	private void parseSpeciesTypes (DocumentNode root) throws BivesSBMLParseException
	{
		List<TreeNode> loss = root.getChildrenWithTag ("listOfSpeciesTypes");
		for (int i = 0; i < loss.size (); i++)
		{
			DocumentNode los = (DocumentNode) loss.get (i);
			
			List<TreeNode> node = los.getChildrenWithTag ("speciesType");
			for (int j = 0; j < node.size (); j++)
			{
				SBMLSpeciesType n = new SBMLSpeciesType ((DocumentNode) node.get (j), this);
				listOfSpeciesTypes.put (n.getID (), n);
			}
		}
	}

	/**
	 * Parses the parameters.
	 *
	 * @param root the root
	 * @throws BivesSBMLParseException the bives sbml parse exception
	 */
	private void parseParameters (DocumentNode root) throws BivesSBMLParseException
	{
		List<TreeNode> loss = root.getChildrenWithTag ("listOfParameters");
		for (int i = 0; i < loss.size (); i++)
		{
			DocumentNode los = (DocumentNode) loss.get (i);
			
			List<TreeNode> node = los.getChildrenWithTag ("parameter");
			for (int j = 0; j < node.size (); j++)
			{
				SBMLParameter n = new SBMLParameter ((DocumentNode) node.get (j), this);
				listOfParameters.put (n.getID (), n);
			}
		}
	}

	/**
	 * Parses the compartments.
	 *
	 * @param root the root
	 * @throws BivesSBMLParseException the bives sbml parse exception
	 */
	private void parseCompartments (DocumentNode root) throws BivesSBMLParseException
	{
		List<TreeNode> locompartments = root.getChildrenWithTag ("listOfCompartments");
		for (int i = 0; i < locompartments.size (); i++)
		{
			DocumentNode locompartment = (DocumentNode) locompartments.get (i);
			
			List<TreeNode> compartments = locompartment.getChildrenWithTag ("compartment");
			for (int j = 0; j < compartments.size (); j++)
			{
				SBMLCompartment c = new SBMLCompartment ((DocumentNode) compartments.get (j), this);
				listOfCompartments.put (c.getID (), c);
			}
		}
	}

	/**
	 * Parses the compartment types.
	 *
	 * @param root the root
	 * @throws BivesSBMLParseException the bives sbml parse exception
	 */
	private void parseCompartmentTypes (DocumentNode root) throws BivesSBMLParseException
	{
		List<TreeNode> loss = root.getChildrenWithTag ("listOfCompartmentTypes");
		for (int i = 0; i < loss.size (); i++)
		{
			DocumentNode los = (DocumentNode) loss.get (i);
			
			List<TreeNode> node = los.getChildrenWithTag ("compartmentType");
			for (int j = 0; j < node.size (); j++)
			{
				SBMLCompartmentType n = new SBMLCompartmentType ((DocumentNode) node.get (j), this);
				listOfCompartmentTypes.put (n.getID (), n);
			}
		}
	}

	/**
	 * Parses the units.
	 *
	 * @param root the root
	 * @throws BivesSBMLParseException the bives sbml parse exception
	 * @throws BivesDocumentConsistencyException the bives document consistency exception
	 */
	private void parseUnits (DocumentNode root) throws BivesSBMLParseException, BivesDocumentConsistencyException
	{
		
		for (int i = 0; i < SBMLUnit.BASE_UNITS.length; i++)
		{
			SBMLUnitDefinition ud = new SBMLUnitDefinition (SBMLUnit.BASE_UNITS[i], this);
			listOfUnitDefinitions.put (ud.getID (), ud);
		}
		
		List<TreeNode> lounits = root.getChildrenWithTag ("listOfUnitDefinitions");
		for (int i = 0; i < lounits.size (); i++)
		{
			DocumentNode lounit = (DocumentNode) lounits.get (i);
			
			List<TreeNode> units = lounit.getChildrenWithTag ("unitDefinition");
			List<String> problems = new ArrayList<String> ();
			boolean nextRound = true;
			while (nextRound && units.size () > 0)
			{
				nextRound = false;
				problems.clear ();
				for (int j = units.size () - 1; j >= 0; j--)
				{
					SBMLUnitDefinition ud = null;
					try
					{
						ud = new SBMLUnitDefinition ((DocumentNode) units.get (j), this);
						String id = ud.getID ();
						if (listOfUnitDefinitions.get (id) != null)
						{
							if (id.equals ("substance") || id.equals ("volume") || id.equals ("area") || id.equals ("length"))
								LOGGER.warn ("std unit ", id, " redefined");
							else
								throw new BivesSBMLParseException ("Multiple definitions of unit " + ud.getID ());
						}
						//System.out.println ("adde unit " + id);
						listOfUnitDefinitions.put (id, ud);
						units.remove (j);
						nextRound = true;
					}
					catch (BivesDocumentConsistencyException ex)
					{
						problems.add (ex.getMessage ());
						continue;
					}
				}
			}
			if (units.size () != 0)
				throw new BivesDocumentConsistencyException ("inconsistencies for "+units.size ()+" units, problems: " + problems);
		}
	}

	/**
	 * Parses the functions.
	 *
	 * @param root the root
	 * @throws BivesSBMLParseException the bives sbml parse exception
	 */
	private void parseFunctions (DocumentNode root) throws BivesSBMLParseException
	{
		List<TreeNode> lofunctions = root.getChildrenWithTag ("listOfFunctionDefinitions");
		for (int i = 0; i < lofunctions.size (); i++)
		{
			DocumentNode lofunction = (DocumentNode) lofunctions.get (i);
			
			List<TreeNode> functions = lofunction.getChildrenWithTag ("functionDefinition");
			for (int j = 0; j < functions.size (); j++)
			{
				SBMLFunctionDefinition fd = new SBMLFunctionDefinition ((DocumentNode) functions.get (j), this);
				listOfFunctionDefinitions.put (fd.getID (), fd);
			}
		}
	}

	
	/**
	 * Gets the function definitions.
	 *
	 * @return the function definitions
	 */
	public HashMap<String, SBMLFunctionDefinition> getFunctionDefinitions ()
	{
		return listOfFunctionDefinitions;
	}
	
	/**
	 * Gets the unit definitions.
	 *
	 * @return the unit definitions
	 */
	public HashMap<String, SBMLUnitDefinition> getUnitDefinitions ()
	{
		return listOfUnitDefinitions;
	}
	
	/**
	 * Gets the unit definition.
	 *
	 * @param kind the kind
	 * @return the unit definition
	 */
	public SBMLUnitDefinition getUnitDefinition (String kind)
	{
		return listOfUnitDefinitions.get (kind);
	}
	
	/**
	 * Gets the compartment type.
	 *
	 * @param id the id
	 * @return the compartment type
	 */
	public SBMLCompartmentType getCompartmentType (String id)
	{
		return listOfCompartmentTypes.get (id);
	}
	
	/**
	 * Gets the compartment types.
	 *
	 * @return the compartment types
	 */
	public HashMap<String, SBMLCompartmentType> getCompartmentTypes ()
	{
		return listOfCompartmentTypes;
	}
	
	/**
	 * Gets the compartments.
	 *
	 * @return the compartments
	 */
	public HashMap<String, SBMLCompartment> getCompartments ()
	{
		return listOfCompartments;
	}
	
	/**
	 * Gets the compartment.
	 *
	 * @param id the id
	 * @return the compartment
	 */
	public SBMLCompartment getCompartment (String id)
	{
		return listOfCompartments.get (id);
	}
	
	/**
	 * Gets the species.
	 *
	 * @param id the id
	 * @return the species
	 */
	public SBMLSpecies getSpecies (String id)
	{
		return listOfSpecies.get (id);
	}
	
	/**
	 * Gets the species.
	 *
	 * @return the species
	 */
	public HashMap<String, SBMLSpecies> getSpecies ()
	{
		return listOfSpecies;
	}
	
	/**
	 * Gets the species type.
	 *
	 * @param id the id
	 * @return the species type
	 */
	public SBMLSpeciesType getSpeciesType (String id)
	{
		return listOfSpeciesTypes.get (id);
	}
	
	/**
	 * Gets the species types.
	 *
	 * @return the species types
	 */
	public HashMap<String, SBMLSpeciesType> getSpeciesTypes ()
	{
		return listOfSpeciesTypes;
	}
	
	/**
	 * Gets the parameters.
	 *
	 * @return the parameters
	 */
	public HashMap<String, SBMLParameter> getParameters ()
	{
		return listOfParameters;
	}
	
	/**
	 * Gets a specific parameter.
	 *
	 * @param id the id of the parameter
	 * @return the parameter
	 */
	public SBMLParameter getParameter (String id)
	{
		return listOfParameters.get (id);
	}
	
	/**
	 * Registers a species reference.
	 *
	 * @param id the id of the species reference
	 * @param ref the reference
	 */
	public void registerSpeciesReference (String id, SBMLSimpleSpeciesReference ref)
	{
		listOfSpeciesReferences.put (id, ref);
	}
	
	/**
	 * Gets the species reference.
	 *
	 * @param id the id of the species reference
	 * @return the species reference
	 */
	public SBMLSimpleSpeciesReference getSpeciesReference (String id)
	{
		// search for species reference w/ spec. id
		return listOfSpeciesReferences.get (id);
	}
	
	/**
	 * Gets a specific reaction.
	 *
	 * @param id the id of the reaction
	 * @return the reaction
	 */
	public SBMLReaction getReaction (String id)
	{
		return listOfReactions.get (id);
	}
	
	/**
	 * Gets the reactions.
	 *
	 * @return the reactions
	 */
	public HashMap<String, SBMLReaction> getReactions ()
	{
		return listOfReactions;
	}
	
	/**
	 * Gets the constraints.
	 *
	 * @return the constraints
	 */
	public List<SBMLConstraint> getConstraints()
	{
		return listOfConstraints;
	}
	
	/**
	 * Gets the initial assignments.
	 *
	 * @return the initial assignments
	 */
	public List<SBMLInitialAssignment> getInitialAssignments()
	{
		return listOfInitialAssignments;
	}
	
	/**
	 * Gets the events.
	 *
	 * @return the events
	 */
	public List<SBMLEvent> getEvents ()
	{
		return listOfEvents;
	}
	
	/**
	 * Gets the rules.
	 *
	 * @return the rules
	 */
	public List<SBMLRule> getRules ()
	{
		return listOfRules;
	}
	
	/**
	 * Gets the id of the model.
	 *
	 * @return the id
	 */
	public String getID ()
	{
		return id;
	}
	
	/**
	 * Gets the name of the model.
	 *
	 * @return the name
	 */
	public String getName ()
	{
		return name;
	}
	
	/**
	 * Map node model node to its entity.
	 *
	 * @param node the document node in the corresponding XML tree
	 * @param sbase the entity to get mapped
	 */
	public void mapNode (DocumentNode node, SBMLSBase sbase)
	{
		nodeMapper.put (node, sbase);
	}
	
	/**
	 * Gets an entity given its tree node.
	 *
	 * @param node the node in the XML tree
	 * @return the entity registered for this node
	 */
	public SBMLSBase getFromNode (TreeNode node)
	{
		return nodeMapper.get (node);
	}
	
	/**
	 * Gets the SBML document.
	 *
	 * @return the document
	 */
	public SBMLDocument getDocument ()
	{
		return document;
	}
	
	public SBMLSBase resolveSymbole (String symbol)
	{
		SBMLSBase entity = sbmlModel.getCompartment (symbol);
		if (entity == null)
			entity = sbmlModel.getSpecies (symbol);
		if (entity == null)
			entity = sbmlModel.getParameter (symbol);
		if (entity == null)
			entity = sbmlModel.getSpeciesReference (symbol);
		return entity;
	}
	
	/**
	 * Gets the SId.
	 *
	 * @param ref the ref
	 * @return the sid name
	 */
	public static String getSidName (SBMLSBase ref)
	{
		if (ref instanceof SBMLParameter)
			return ((SBMLParameter) ref).getNameAndId ();
		if (ref instanceof SBMLSpecies)
			return ((SBMLSpecies) ref).getNameAndId ();
		if (ref instanceof SBMLCompartment)
			return ((SBMLCompartment) ref).getNameAndId ();
		if (ref instanceof SBMLSimpleSpeciesReference)
			return ((SBMLSimpleSpeciesReference) ref).getSpecies ().getNameAndId ();
		return null;
	}
}
