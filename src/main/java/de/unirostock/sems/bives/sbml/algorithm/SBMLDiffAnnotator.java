/**
 * 
 */
package de.unirostock.sems.bives.sbml.algorithm;

import org.jdom2.Element;

import de.unirostock.sems.bives.algorithm.general.DefaultDiffAnnotator;
import de.unirostock.sems.comodi.Change;
import de.unirostock.sems.comodi.ChangeFactory;
import de.unirostock.sems.comodi.branches.ComodiTarget;
import de.unirostock.sems.xmlutils.ds.TextNode;
import de.unirostock.sems.xmlutils.ds.TreeNode;


/**
 * @author Martin Scharm
 *
 */
public class SBMLDiffAnnotator
	extends DefaultDiffAnnotator
{

	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.unirostock.sems.bives.algorithm.DiffAnnotator#annotateDeletion(de.unirostock
	 * .sems.xmlutils.ds.TreeNode, org.jdom2.Element,
	 * de.unirostock.sems.comodi.ChangeFactory)
	 */
	@Override
	public Change annotateDeletion (TreeNode node, Element diffNode,
		ChangeFactory changeFac)
	{
		Change change = super.annotateDeletion (node, diffNode, changeFac);
		annotateTarget (change, node, null, diffNode, false);
		return change;
	}
	
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see de.unirostock.sems.bives.algorithm.DiffAnnotator#annotateInsertion(de.
	 * unirostock.sems.xmlutils.ds.TreeNode, org.jdom2.Element,
	 * de.unirostock.sems.comodi.ChangeFactory)
	 */
	@Override
	public Change annotateInsertion (TreeNode node, Element diffNode,
		ChangeFactory changeFac)
	{
		Change change = super.annotateInsertion (node, diffNode, changeFac);
		annotateTarget (change, null, node, diffNode, false);
		return change;
	}
	
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.unirostock.sems.bives.algorithm.DiffAnnotator#annotateMove(de.unirostock
	 * .sems.xmlutils.ds.TreeNode, de.unirostock.sems.xmlutils.ds.TreeNode,
	 * org.jdom2.Element, de.unirostock.sems.comodi.ChangeFactory, boolean)
	 */
	@Override
	public Change annotateMove (TreeNode nodeA, TreeNode nodeB, Element diffNode,
		ChangeFactory changeFac, boolean permutation)
	{
		Change change = super.annotateMove (nodeA, nodeB, diffNode, changeFac,
			permutation);
		annotateTarget (change, nodeA, nodeB, diffNode, permutation);
		return change;
	}
	
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.unirostock.sems.bives.algorithm.DiffAnnotator#annotateUpdateAttribute
	 * (de.unirostock.sems.xmlutils.ds.TreeNode,
	 * de.unirostock.sems.xmlutils.ds.TreeNode, java.lang.String,
	 * org.jdom2.Element, de.unirostock.sems.comodi.ChangeFactory)
	 */
	@Override
	public Change annotateUpdateAttribute (TreeNode nodeA, TreeNode nodeB,
		String attributeName, Element diffNode, ChangeFactory changeFac)
	{
		Change change = super.annotateUpdateAttribute (nodeA, nodeB, attributeName,
			diffNode, changeFac);
		annotateTarget (change, nodeA, nodeB, diffNode, false);
		return change;
	}
	
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.unirostock.sems.bives.algorithm.DiffAnnotator#annotateUpdateText(de.
	 * unirostock.sems.xmlutils.ds.TextNode,
	 * de.unirostock.sems.xmlutils.ds.TextNode, org.jdom2.Element,
	 * de.unirostock.sems.comodi.ChangeFactory)
	 */
	@Override
	public Change annotateUpdateText (TextNode nodeA, TextNode nodeB,
		Element diffNode, ChangeFactory changeFac)
	{
		Change change = super
			.annotateUpdateText (nodeA, nodeB, diffNode, changeFac);
		annotateTarget (change, nodeA, nodeB, diffNode, false);
		return change;
	}
	
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.unirostock.sems.bives.algorithm.DiffAnnotator#annotatePatch(java.util
	 * .String, de.unirostock.sems.comodi.ChangeFactory)
	 */
	@Override
	public void annotatePatch (String rootId, ChangeFactory changeFac)
	{
		super.annotatePatch (rootId, changeFac);
	}
	
	
	/**
	 * Annotate the target of a change. Recognises the effected parts of the
	 * document and annotates the change with a term from COMODI.
	 * 
	 * @param change
	 *          the change
	 * @param nodeA
	 *          the node a
	 * @param nodeB
	 *          the node b
	 * @param diffNode
	 *          the diff node
	 * @param permutation
	 *          the permutation
	 * @return the change
	 */
	private Change annotateTarget (Change change, TreeNode nodeA, TreeNode nodeB,
		Element diffNode, boolean permutation)
	{
		return change;
	}
}
