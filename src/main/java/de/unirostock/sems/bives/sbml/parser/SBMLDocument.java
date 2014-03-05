/**
 * 
 */
package de.unirostock.sems.bives.sbml.parser;


import java.util.List;

import de.unirostock.sems.bives.ds.ModelDocument;
import de.unirostock.sems.bives.exception.BivesDocumentConsistencyException;
import de.unirostock.sems.bives.sbml.exception.BivesSBMLParseException;
import de.unirostock.sems.xmlutils.ds.DocumentNode;
import de.unirostock.sems.xmlutils.ds.TreeDocument;
import de.unirostock.sems.xmlutils.ds.TreeNode;


/**
 * The Class SBMLDocument representing a document holding an SBMLModel.
 *
 * @author Martin Scharm
 */
public class SBMLDocument
extends ModelDocument
{
	/** The level. */
	private int level;
	
	/** The version. */
	private int version;
	
	/** The model. */
	private SBMLModel model;
	
	/**
	 * Instantiates a new SBML document.
	 *
	 * @param doc the XML document
	 * @throws BivesSBMLParseException the bives sbml parse exception
	 * @throws BivesDocumentConsistencyException the bives document consistency exception
	 */
	public SBMLDocument (TreeDocument doc) throws BivesSBMLParseException, BivesDocumentConsistencyException
	{
		super (doc);
		DocumentNode root = doc.getRoot ();
		parseRoot (root);
		
		List<TreeNode> nodes = root.getChildrenWithTag ("model");
		if (nodes.size () != 1)
			throw new BivesSBMLParseException ("sbml document has "+nodes.size ()+" model elements. (expected exactly one element)");
		model = new SBMLModel ((DocumentNode) nodes.get (0), this);
	}
	
	/**
	 * Gets the level.
	 *
	 * @return the level
	 */
	public int getLevel ()
	{
		return level;
	}
	
	/**
	 * Gets the version.
	 *
	 * @return the version
	 */
	public int getVersion ()
	{
		return version;
	}
	
	/**
	 * Gets the model.
	 *
	 * @return the model
	 */
	public SBMLModel getModel ()
	{
		return model;
	}

	/**
	 * Parses the root.
	 *
	 * @param root the root
	 * @throws BivesSBMLParseException the bives sbml parse exception
	 */
	private void parseRoot (DocumentNode root) throws BivesSBMLParseException
	{
		if (!root.getTagName ().equals ("sbml"))
			throw new BivesSBMLParseException ("sbml document doesn't start with sbml tag.");
		try
		{
			level = Integer.parseInt (root.getAttribute ("level"));
		}
		catch (Exception e)
		{
			throw new BivesSBMLParseException ("unexpected format of SBML level definition: " + root.getAttribute ("level"));
		}
		
		try
		{
			version = Integer.parseInt (root.getAttribute ("version"));
		}
		catch (Exception e)
		{
			throw new BivesSBMLParseException ("unexpected format of SBML version definition: " + root.getAttribute ("version"));
		}
	}
}
