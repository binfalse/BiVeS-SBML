/**
 * 
 */
package de.unirostock.sems.bives.sbml.algorithm;

import de.unirostock.sems.bives.algorithm.ModelValidator;
import de.unirostock.sems.bives.sbml.parser.SBMLDocument;
import de.unirostock.sems.xmlutils.ds.TreeDocument;
import de.unirostock.sems.xmlutils.tools.XmlTools;


/**
 * The Class SBMLValidator validates SBML code.
 *
 * @author Martin Scharm
 */
public class SBMLValidator
	extends ModelValidator
{
	
	/** The doc. */
	private SBMLDocument doc;
	
	
	/** The error. */
	private Exception error;
	
	
	/* (non-Javadoc)
	 * @see de.unirostock.sems.xmldiff.algorithm.ModelValidator#validate(de.unirostock.sems.xmldiff.ds.xml.TreeDocument)
	 */
	@Override
	public boolean validate (TreeDocument d)
	{
		try
		{
			doc = new SBMLDocument (d);
		}
		catch (Exception e)
		{
			error = e;
			return false;
		}
		return true;
	}
	
	
	/* (non-Javadoc)
	 * @see de.unirostock.sems.xmldiff.algorithm.ModelValidator#validate(java.lang.String)
	 */
	@Override
	public boolean validate (String d)
	{
		try
		{
			return validate (new TreeDocument (XmlTools.readDocument (d), null));
		}
		catch (Exception e)
		{
			error = e;
			return false;
		}
	}


	/* (non-Javadoc)
	 * @see de.unirostock.sems.bives.algorithm.ModelValidator#getDocument()
	 */
	@Override
	public SBMLDocument getDocument ()
	{
		return doc;
	}


	/* (non-Javadoc)
	 * @see de.unirostock.sems.bives.algorithm.ModelValidator#getError()
	 */
	@Override
	public Exception getError ()
	{
		return error;
	}
	
}
