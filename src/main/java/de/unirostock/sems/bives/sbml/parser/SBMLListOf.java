/**
 * 
 */
package de.unirostock.sems.bives.sbml.parser;

import de.unirostock.sems.bives.sbml.exception.BivesSBMLParseException;
import de.unirostock.sems.xmlutils.ds.DocumentNode;


/**
 * @author Martin Scharm
 *
 */
public class SBMLListOf
	extends SBMLSBase
{
	
	/**
	 * @param documentNode
	 * @param sbmlModel
	 * @throws BivesSBMLParseException
	 */
	public SBMLListOf (DocumentNode documentNode, SBMLModel sbmlModel)
		throws BivesSBMLParseException
	{
		super (documentNode, sbmlModel);
	}
	
}
