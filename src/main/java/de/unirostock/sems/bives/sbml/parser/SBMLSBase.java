/**
 * 
 */
package de.unirostock.sems.bives.sbml.parser;


import java.util.ArrayList;
import java.util.List;

import de.unirostock.sems.bives.ds.Xhtml;
import de.unirostock.sems.bives.ds.ontology.SBOTerm;
import de.unirostock.sems.bives.sbml.exception.BivesSBMLParseException;
import de.unirostock.sems.xmlutils.ds.DocumentNode;
import de.unirostock.sems.xmlutils.ds.TreeNode;


/**
 * @author Martin Scharm
 *
 */
public abstract class SBMLSBase
	extends SBMLGenericObject
{
	private String metaid;
	private SBOTerm sboTerm;
	private Xhtml notes;
	private DocumentNode annotation;
	
	public SBMLSBase(DocumentNode documentNode, SBMLModel sbmlModel) throws BivesSBMLParseException
	{
		super (documentNode, sbmlModel);
		
		if (sbmlModel != null)
			sbmlModel.mapNode (documentNode, this);

		if (documentNode != null)
		{
			metaid = documentNode.getAttribute ("metaid");
			if (documentNode.getAttribute ("sboTerm") != null)
				sboTerm = new SBOTerm (documentNode.getAttribute ("sboTerm"));
			
			List<TreeNode> nodeList = documentNode.getChildrenWithTag ("notes");
			if (nodeList.size () > 1)
				throw new BivesSBMLParseException ("SBase with "+nodeList.size ()+" notes. (expected max one notes)");
			if (nodeList.size () == 1)
			{
				notes = new Xhtml  ();
				DocumentNode root = (DocumentNode) nodeList.get (0);
				List<TreeNode> kids = root.getChildren ();
				for (TreeNode n : kids)
					notes.addXhtml (n);
			}
			
			nodeList = documentNode.getChildrenWithTag ("annotation");
			if (nodeList.size () > 1)
				throw new BivesSBMLParseException ("SBase with "+nodeList.size ()+" annotations. (expected max one annotation)");
			if (nodeList.size () == 1)
				annotation = (DocumentNode) nodeList.get (0);
		}
	}
	
	public SBOTerm getSBOTerm ()
	{
		return sboTerm;
	}
	
	public String getMetaId ()
	{
		return metaid;
	}
	
	public Xhtml getNotes ()
	{
		return notes;
	}
	
	public DocumentNode getAnnotation ()
	{
		return annotation;
	}
	
	protected String reportAnnotation ()
	{
		return "";
	}
	
	protected String reportNotes ()
	{
		return "";
	}
}
