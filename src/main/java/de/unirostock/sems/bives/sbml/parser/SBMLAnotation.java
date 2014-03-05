/**
 * 
 */
package de.unirostock.sems.bives.sbml.parser;

import de.unirostock.sems.xmlutils.ds.DocumentNode;


/**
 * The Class SBMLAnotation ,a container for optional software-generated content not meant to be shown to humans.
 *
 * @author Martin Scharm
 */
public class SBMLAnotation
{
	
	/** The node rooting the annotation. */
	private DocumentNode rootNode;
	
	/**
	 * Instantiates a new SBML anotation.
	 *
	 * @param rootNode the node rooting the annotation
	 */
	public SBMLAnotation (DocumentNode rootNode)
	{
		this.rootNode = rootNode;
	}
}
