/**
 * 
 */
package de.unirostock.sems.bives.sbml.parser;


import java.util.List;

import de.unirostock.sems.bives.ds.Xhtml;
import de.unirostock.sems.bives.ds.ontology.SBOTerm;
import de.unirostock.sems.bives.sbml.exception.BivesSBMLParseException;
import de.unirostock.sems.xmlutils.ds.DocumentNode;
import de.unirostock.sems.xmlutils.ds.TreeNode;


/**
 * The Class SBMLSBase.
 * This base type is designed to allow a modeler or a software package to attach arbitrary information to each major element or list in an SBML model.
 *
 * @author Martin Scharm
 */
public abstract class SBMLSBase
	extends SBMLGenericObject
{
	
	/** The metaid. */
	private String metaid; //optional
	
	/** The SBO term. */
	private SBOTerm sboTerm; //optional
	
	/** The notes. */
	private Xhtml notes; //optional
	
	/** The annotation. */
	private DocumentNode annotation; // optional
	
	/**
	 * Instantiates a new SBMLS base.
	 *
	 * @param documentNode the document node encoding this entity in the corresponding XML tree
	 * @param sbmlModel the SBML model
	 * @throws BivesSBMLParseException the bives sbml parse exception
	 */
	public SBMLSBase(DocumentNode documentNode, SBMLModel sbmlModel) throws BivesSBMLParseException
	{
		super (documentNode, sbmlModel);
		
		if (sbmlModel != null)
			sbmlModel.mapNode (documentNode, this);

		if (documentNode != null)
		{
			metaid = documentNode.getAttributeValue ("metaid");
			if (documentNode.getAttributeValue ("sboTerm") != null)
				sboTerm = new SBOTerm (documentNode.getAttributeValue ("sboTerm"));
			
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
	
	/**
	 * Gets the SBO term.
	 *
	 * @return the SBO term
	 */
	public SBOTerm getSBOTerm ()
	{
		return sboTerm;
	}
	
	/**
	 * Gets the meta id.
	 *
	 * @return the meta id
	 */
	public String getMetaId ()
	{
		return metaid;
	}
	
	/**
	 * Gets the notes.
	 *
	 * @return the notes
	 */
	public Xhtml getNotes ()
	{
		return notes;
	}
	
	/**
	 * Gets the annotation.
	 *
	 * @return the annotation
	 */
	public DocumentNode getAnnotation ()
	{
		return annotation;
	}
}
