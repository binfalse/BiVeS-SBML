/**
 * 
 */
package de.unirostock.sems.bives.sbml.parser;

import java.util.Vector;

import de.unirostock.sems.bives.exception.BivesDocumentConsistencyException;
import de.unirostock.sems.bives.sbml.exception.BivesSBMLParseException;
import de.unirostock.sems.xmltools.ds.DocumentNode;
import de.unirostock.sems.xmltools.ds.TreeDocument;
import de.unirostock.sems.xmltools.ds.TreeNode;


/**
 * @author Martin Scharm
 *
 */
public class SBMLDocument
{
	private TreeDocument tree;
	
	private String nameSpace;
	private int level;
	private int version;
	private SBMLModel model;
	
	public SBMLDocument (TreeDocument tree) throws BivesSBMLParseException, BivesDocumentConsistencyException
	{
		this.tree = tree;
		DocumentNode root = tree.getRoot ();
		parseRoot (root);
		
		Vector<TreeNode> nodes = root.getChildrenWithTag ("model");
		if (nodes.size () != 1)
			throw new BivesSBMLParseException ("sbml document has "+nodes.size ()+" model elements. (expected exactly one element)");
		model = new SBMLModel ((DocumentNode) nodes.elementAt (0), this);
	}
	
	public TreeDocument getTreeDocument ()
	{
		return tree;
	}
	
	public int getLevel ()
	{
		return level;
	}
	
	public int getVersion ()
	{
		return version;
	}
	
	public SBMLModel getModel ()
	{
		return model;
	}

	private void parseRoot (DocumentNode root) throws BivesSBMLParseException
	{
		if (!root.getTagName ().equals ("sbml"))
			throw new BivesSBMLParseException ("sbml document doesn't start with sbml tag.");
		
		nameSpace = root.getAttribute ("xmlns");
		if (nameSpace == null)
			throw new BivesSBMLParseException ("no namespace for model defined.");
		
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
