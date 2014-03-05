/**
 * 
 */
package de.unirostock.sems.bives.sbml.parser;

import de.unirostock.sems.xmlutils.ds.DocumentNode;


/**
 * The Class SBMLGenericObject representing a generic SBML object.
 *
 * @author Martin Scharm
 */
public class SBMLGenericObject
{
	
	/** The document node. */
	protected DocumentNode documentNode;
	
	/** The sbml model. */
	protected SBMLModel sbmlModel;
	
	/**
	 * Instantiates a new generic SBML object.
	 *
	 * @param documentNode the document node encoding this entity in the corresponding XML tree
	 * @param sbmlModel the SBML model
	 */
	public SBMLGenericObject (DocumentNode documentNode, SBMLModel sbmlModel)
	{
		this.documentNode = documentNode;
		this.sbmlModel = sbmlModel;
	}
	
	/**
	 * Gets the document node.
	 *
	 * @return the document node
	 */
	public DocumentNode getDocumentNode ()
	{
		return documentNode;
	}
	
	/**
	 * Gets the model.
	 *
	 * @return the model
	 */
	public SBMLModel getModel ()
	{
		return sbmlModel;
	}
}
