/**
 * 
 */
package de.unirostock.sems.bives.sbml.algorithm;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;

import org.xml.sax.SAXException;

import de.unirostock.sems.bives.algorithm.ModelValidator;
import de.unirostock.sems.bives.exception.BivesException;
import de.unirostock.sems.bives.sbml.parser.SBMLDocument;
import de.unirostock.sems.xmlutils.ds.TreeDocument;
import de.unirostock.sems.xmlutils.tools.DocumentTools;


/**
 * TODO
 * @author Martin Scharm
 *
 */
public class SBMLValidator
	extends ModelValidator
{
	private SBMLDocument doc;
	/* (non-Javadoc)
	 * @see de.unirostock.sems.xmldiff.algorithm.ModelValidator#validate(de.unirostock.sems.xmldiff.ds.xml.TreeDocument)
	 */
	@Override
	public boolean validate (TreeDocument d) throws BivesException
	{
		return validate (DocumentTools.printSubDoc (d.getRoot ()));
	}
	
	
	/* (non-Javadoc)
	 * @see de.unirostock.sems.xmldiff.algorithm.ModelValidator#validate(java.lang.String)
	 */
	@Override
	public boolean validate (String d) throws BivesException
	{
		/*DocumentBuilder builder = DocumentBuilderFactory.newInstance ()
			.newDocumentBuilder ();
		TreeDocument td = new TreeDocument (builder.parse (new ByteArrayInputStream(d.getBytes ())));
		doc = new SBMLDocument (td);
		/*if (doc.checkConsistency () > 0)*/
			throw new BivesException ("not yet implemented");
		//return true;
	}


	public String getModelID () throws BivesException
	{
		throw new BivesException ("not yet implemented");
	}
	
}
