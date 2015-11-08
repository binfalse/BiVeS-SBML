/**
 * 
 */
package de.unirostock.sems.bives.sbml.parser;

import de.binfalse.bflog.LOGGER;
import de.unirostock.sems.xmlutils.ds.DocumentNode;
import de.unirostock.sems.xmlutils.ds.TreeNode;


/**
 * The Class SBMLMeta to handle annotations and stuff.
 *
 * @author Martin Scharm
 */
public class SBMLMeta
{
	
	/**
	 * Extract ontology links from an annotation element.
	 *
	 * @param rdf the RDF node hosting the description blocks
	 * @param model the corresponding model that hosts the node described in this annotation
	 */
	public static void extractOntologyLinks (DocumentNode rdf, SBMLModel model)
	{
		for (TreeNode descr : rdf.getChildrenWithTag ("Description"))
		{
			DocumentNode descrNode = ((DocumentNode) descr);
			String about = descrNode.getAttributeValue ("about");
			if (about == null)
			{
				LOGGER.info ("cannot find 'about' attribute in rdf:description element ", descr.getXPath ());
				continue;
			}
			if (!about.startsWith ("#"))
			{
				LOGGER.info ("'about' attribute in rdf:description element does not start with an # ", descr.getXPath ());
				continue;
			}
			
			SBMLSBase describedNode = model.getFromMetaId (about.substring (1));
			if (describedNode == null)
			{
				LOGGER.error ("node reffed in 'about' attribute in rdf:description element does not exist? ", descr.getXPath (), " about: ", about.substring (1));
				continue;
			}
			
			
			for (TreeNode c : descrNode.getChildren ())
			{
				DocumentNode child = ((DocumentNode) c);
				String qualifier = child.getTagName ();
				
				if (
					qualifier.equals ("encodes") ||
					qualifier.equals ("hasPart") ||
					qualifier.equals ("hasProperty") ||
					qualifier.equals ("hasTaxon") ||
					qualifier.equals ("hasVersion") ||
					qualifier.equals ("is") ||
					qualifier.equals ("isDerivedFrom") ||
					qualifier.equals ("isDescribedBy") ||
					qualifier.equals ("isEncodedBy") ||
					qualifier.equals ("isHomologTo") ||
					qualifier.equals ("isPartOf") ||
					qualifier.equals ("isPropertyOf") ||
					qualifier.equals ("isVersionOf") ||
					qualifier.equals ("occursIn")
					)
				{
					extractLinks (child, qualifier, describedNode);
				}
			}
		}
	}
	
	/**
	 * Extract links.
	 *
	 * @param container the container
	 * @param qualifier the qualifier
	 * @param describedNode the described node
	 */
	private static void extractLinks (DocumentNode container, String qualifier, SBMLSBase describedNode)
	{

		for (TreeNode bag : container.getChildrenWithTag ("Bag"))
		{
			for (TreeNode li : ((DocumentNode) bag).getChildrenWithTag ("li"))
			{
				DocumentNode linode = ((DocumentNode) li);
				
				if (linode.getAttribute ("resource") != null)
					describedNode.addOntologyLink (qualifier, linode.getAttribute ("resource").getValue ());
					//links.add (linode.getAttribute ("resource").getValue ());
			}
		}
	}
}
