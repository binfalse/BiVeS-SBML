/**
 * 
 */
package de.unirostock.sems.bives.sbml.algorithm;

import java.io.File;

import de.binfalse.bflog.LOGGER;
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
			LOGGER.info (e, "error validating document");
			return false;
		}
		return true;
	}


	/* (non-Javadoc)
	 * @see de.unirostock.sems.bives.algorithm.ModelValidator#validate(java.io.File)
	 */
	@Override
	public boolean validate (File d)
	{
		try
		{
			return validate (new TreeDocument (XmlTools.readDocument (d), d.toURI ()));
		}
		catch (Exception e)
		{
			error = e;
			LOGGER.info (e, "error validating document");
			return false;
		}
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
			LOGGER.info (e, "error validating document");
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
	
}
