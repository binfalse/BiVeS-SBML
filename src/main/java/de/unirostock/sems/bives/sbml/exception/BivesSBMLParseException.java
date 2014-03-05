/**
 * 
 */
package de.unirostock.sems.bives.sbml.exception;

import de.unirostock.sems.bives.exception.BivesException;



/**
 * The Class BivesSBMLParseException.
 * 
 * @author Martin Scharm
 */
public class BivesSBMLParseException
	extends BivesException
{
	
	private static final long	serialVersionUID	= -3683762989632487518L;
	
	
	/**
	 * Instantiates a new BiVeS exception signalling an error while parsing an
	 * SBML document.
	 * 
	 * @param msg
	 *          the message
	 */
	public BivesSBMLParseException (String msg)
	{
		super (msg);
	}
	
}
