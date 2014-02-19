/**
 * 
 */
package de.unirostock.sems.bives.sbml.parser;

import java.util.ArrayList;
import java.util.List;

import de.unirostock.sems.bives.algorithm.DiffReporter;
import de.unirostock.sems.bives.algorithm.SimpleConnectionManager;
import de.unirostock.sems.bives.markup.MarkupDocument;
import de.unirostock.sems.bives.markup.MarkupElement;
import de.unirostock.sems.bives.sbml.exception.BivesSBMLParseException;
import de.unirostock.sems.bives.tools.BivesTools;
import de.unirostock.sems.xmlutils.comparison.Connection;
import de.unirostock.sems.xmlutils.ds.DocumentNode;
import de.unirostock.sems.xmlutils.ds.TreeNode;


/**
 * @author Martin Scharm
 *
 */
public class SBMLReaction
	extends SBMLGenericIdNameObject
	implements DiffReporter
{
	private boolean reversible;
	private boolean fast;
	private SBMLCompartment compartment; //optional

	private SBMLListOf listOfReactantsNode;
	private SBMLListOf listOfProductsNode;
	private SBMLListOf listOfModifiersNode;
	private List<SBMLSpeciesReference> listOfReactants;
	private List<SBMLSpeciesReference> listOfProducts;
	private List<SBMLSimpleSpeciesReference> listOfModifiers;
	private SBMLKineticLaw kineticLaw;
	
	
	/**
	 * @param documentNode
	 * @param sbmlDocument
	 * @throws BivesSBMLParseException
	 */
	public SBMLReaction (DocumentNode documentNode, SBMLModel sbmlModel)
		throws BivesSBMLParseException
	{
		super (documentNode, sbmlModel);
		
		if (documentNode.getAttribute ("reversible") != null)
		{
			try
			{
				reversible = Boolean.parseBoolean (documentNode.getAttribute ("reversible"));
			}
			catch (Exception e)
			{
				throw new BivesSBMLParseException ("reversible attr of reaction "+id+" of unexpected format: " + documentNode.getAttribute ("reversible"));
			}
		}
		else
			reversible = true; // level <= 2
		
		if (documentNode.getAttribute ("fast") != null)
		{
			try
			{
				fast = Boolean.parseBoolean (documentNode.getAttribute ("fast"));
			}
			catch (Exception e)
			{
				throw new BivesSBMLParseException ("fast attr of reaction "+id+" of unexpected format: " + documentNode.getAttribute ("fast"));
			}
		}
		else
			fast = false; // level <= 2

		String tmp = documentNode.getAttribute ("compartment");
		if (tmp != null)
		{
			compartment = sbmlModel.getCompartment (tmp);
			if (compartment == null)
				throw new BivesSBMLParseException ("no valid compartment for species "+id+" defined: " + tmp);
		}
		
		listOfReactants = new ArrayList<SBMLSpeciesReference> ();
		listOfProducts = new ArrayList<SBMLSpeciesReference> ();
		listOfModifiers = new ArrayList<SBMLSimpleSpeciesReference> ();
		
		List<TreeNode> nodes = documentNode.getChildrenWithTag ("listOfReactants");
		for (int i = 0; i < nodes.size (); i++)
		{
			listOfReactantsNode = new SBMLListOf ((DocumentNode) nodes.get (i), sbmlModel);
			List<TreeNode> subnodes = ((DocumentNode) nodes.get (i)).getChildrenWithTag ("speciesReference");
			for (int j = 0; j < subnodes.size (); j++)
			{
				SBMLSpeciesReference sr = new SBMLSpeciesReference ((DocumentNode) subnodes.get (j), sbmlModel);
				listOfReactants.add (sr);
			}
		}
		
		nodes = documentNode.getChildrenWithTag ("listOfProducts");
		for (int i = 0; i < nodes.size (); i++)
		{
			listOfProductsNode = new SBMLListOf ((DocumentNode) nodes.get (i), sbmlModel);
			List<TreeNode> subnodes = ((DocumentNode) nodes.get (i)).getChildrenWithTag ("speciesReference");
			for (int j = 0; j < subnodes.size (); j++)
			{
				SBMLSpeciesReference sr = new SBMLSpeciesReference ((DocumentNode) subnodes.get (j), sbmlModel);
				listOfProducts.add (sr);
			}
		}
		
		nodes = documentNode.getChildrenWithTag ("listOfModifiers");
		for (int i = 0; i < nodes.size (); i++)
		{
			listOfModifiersNode = new SBMLListOf ((DocumentNode) nodes.get (i), sbmlModel);
			List<TreeNode> subnodes = ((DocumentNode) nodes.get (i)).getChildrenWithTag ("modifierSpeciesReference");
			for (int j = 0; j < subnodes.size (); j++)
			{
				SBMLSimpleSpeciesReference sr = new SBMLSimpleSpeciesReference ((DocumentNode) subnodes.get (j), sbmlModel);
				listOfModifiers.add (sr);
			}
		}

		nodes = documentNode.getChildrenWithTag ("kineticLaw");
		if (nodes.size () > 1)
			throw new BivesSBMLParseException ("reaction "+id+" has "+nodes.size ()+" kinetic law elements. (expected not more tha one element)");
		if (nodes.size () == 1)
			kineticLaw = new SBMLKineticLaw ((DocumentNode) nodes.get (0), sbmlModel);
	}
	
	public SBMLCompartment getCompartment ()
	{
		return compartment;
	}
	
	public boolean isReversible ()
	{
		return reversible;
	}
	
	public boolean isFast ()
	{
		return fast;
	}
	
	public SBMLKineticLaw getKineticLaw ()
	{
		return kineticLaw;
	}
	
	public SBMLListOf getListOfReactantsNode ()
	{
		return listOfReactantsNode;
	}
	
	public SBMLListOf getListOfProductsNode ()
	{
		return listOfProductsNode;
	}
	
	public SBMLListOf getListOfModifiersNode ()
	{
		return listOfModifiersNode;
	}
	
	public List<SBMLSpeciesReference> getReactants ()
	{
		return listOfReactants;
	}
	
	public List<SBMLSpeciesReference> getProducts ()
	{
		return listOfProducts;
	}
	
	public List<SBMLSimpleSpeciesReference> getModifiers ()
	{
		return listOfModifiers;
	}

	@Override
	public MarkupElement reportMofification (SimpleConnectionManager conMgmt, DiffReporter docA, DiffReporter docB)
	{
		SBMLReaction a = (SBMLReaction) docA;
		SBMLReaction b = (SBMLReaction) docB;
		if (a.getDocumentNode ().getModification () == 0 && b.getDocumentNode ().getModification () == 0)
			return null;
		
		String idA = a.getNameAndId (), idB = b.getNameAndId ();
		MarkupElement me = null;
		if (idA.equals (idB))
			me = new MarkupElement (idA);
		else
			me = new MarkupElement (MarkupDocument.delete (idA) + " "+MarkupDocument.rightArrow ()+" " + MarkupDocument.insert (idB));
		
		BivesTools.genAttributeMarkupStats (a.documentNode, b.documentNode, me);

		List<SBMLSpeciesReference> aS = a.listOfReactants;
		List<SBMLSpeciesReference> bS = b.listOfReactants;
		String sub = "", ret = "";
		for (SBMLSpeciesReference sr : aS)
		{
			if (sub.length () > 0)
				sub += " + ";
			if (conMgmt.getConnectionForNode (sr.getDocumentNode ()) == null)
				sub += sr.reportDelete ();
			else
			{
				Connection c = conMgmt.getConnectionForNode (sr.getDocumentNode ());
				SBMLSpeciesReference partner = (SBMLSpeciesReference) b.sbmlModel.getFromNode (c.getPartnerOf (sr.getDocumentNode ()));
				sub += sr.reportMofification (conMgmt, sr, partner);
			}
		}
		for (SBMLSpeciesReference sr : bS)
		{
			if (conMgmt.getConnectionForNode (sr.getDocumentNode ()) == null)
			{
				if (sub.length () > 0)
					sub += " + ";
				sub += sr.reportInsert ();
			}
		}
		if (sub.length () > 0)
			ret += sub + " "+MarkupDocument.rightArrow ()+" ";
		else
			ret += "&Oslash; "+MarkupDocument.rightArrow ()+" ";

		aS = a.listOfProducts;
		bS = b.listOfProducts;
		sub = "";
		for (SBMLSpeciesReference sr : aS)
		{
			if (sub.length () > 0)
				sub += " + ";
			if (conMgmt.getConnectionForNode (sr.getDocumentNode ()) == null)
			{
				//System.out.println ("reporting delete for " + sr.getDocumentNode ().getXPath ());
				sub += sr.reportDelete ();
			}
			else
			{
				//System.out.println ("reporting mod for " + sr.getDocumentNode ().getXPath ());
				Connection c = conMgmt.getConnectionForNode (sr.getDocumentNode ());
				SBMLSpeciesReference partner = (SBMLSpeciesReference) b.sbmlModel.getFromNode (c.getPartnerOf (sr.getDocumentNode ()));
				sub += sr.reportMofification (conMgmt, sr, partner);
			}
		}
		for (SBMLSpeciesReference sr : bS)
		{
			if (conMgmt.getConnectionForNode (sr.getDocumentNode ()) == null)
			{
				//System.out.println ("reporting ins for " + sr.getDocumentNode ().getXPath ());
				if (sub.length () > 0)
					sub += " + ";
				sub += sr.reportInsert ();
			}
		}
		if (sub.length () > 0)
			ret += sub;
		else
			ret += "&Oslash;";
		
		me.addValue (ret);
		

		List<SBMLSimpleSpeciesReference> aM = a.listOfModifiers;
		List<SBMLSimpleSpeciesReference> bM = b.listOfModifiers;
		sub = "";
		for (SBMLSimpleSpeciesReference sr : aM)
		{
			if (sub.length () > 0)
				sub += "; ";
			if (conMgmt.getConnectionForNode (sr.getDocumentNode ()) == null)
				sub += sr.reportDelete ();
			else
			{
				Connection c = conMgmt.getConnectionForNode (sr.getDocumentNode ());
				SBMLSimpleSpeciesReference partner = (SBMLSimpleSpeciesReference) b.sbmlModel.getFromNode (c.getPartnerOf (sr.getDocumentNode ()));
				sub += sr.reportMofification (conMgmt, sr, partner);
			}
		}
		for (SBMLSimpleSpeciesReference sr : bM)
		{
			if (sub.length () > 0)
				sub += "; ";
			if (conMgmt.getConnectionForNode (sr.getDocumentNode ()) == null)
				sub += sr.reportInsert ();
		}
		if (sub.length () > 0)
			me.addValue ("Modifiers: " + sub);
		
		MarkupElement me2 = new MarkupElement ("Kinetic Law");
		if (a.kineticLaw != null && b.kineticLaw != null)
		{
			a.kineticLaw.reportMofification (conMgmt, a.kineticLaw, b.kineticLaw, me2);
			if (me2.getValues ().size () > 0)
				me.addSubElements (me2);
		}
			//me.addValue (markupDocument.highlight ("Kinetic Law:") + a.kineticLaw.reportMofification (conMgmt, a.kineticLaw, b.kineticLaw, markupDocument));
		else if (a.kineticLaw != null)
		{
			a.kineticLaw.reportDelete (me2);
			me.addSubElements (me2);
		}
			//me.addValue (markupDocument.highlight ("Kinetic Law:") + a.kineticLaw.reportDelete (markupDocument));
		else if (b.kineticLaw != null)
		{
			b.kineticLaw.reportInsert (me2);
			me.addSubElements (me2);
		}
			//me.addValue (markupDocument.highlight ("Kinetic Law:") + b.kineticLaw.reportInsert (markupDocument));
		
		return me;
	}

	@Override
	public MarkupElement reportInsert ()
	{

		
		
		MarkupElement me = new MarkupElement (MarkupDocument.insert (getNameAndId ()));
		report (me, true);
		return me;
	}

	@Override
	public MarkupElement reportDelete ()
	{
		MarkupElement me = new MarkupElement (MarkupDocument.delete (getNameAndId ()));
		report (me, false);
		return me;
	}
	
	public void report (MarkupElement me, boolean insert)
	{
		
		StringBuilder ret = new StringBuilder ();
		StringBuilder sub = new StringBuilder ();
		for (SBMLSpeciesReference sr : listOfReactants)
		{
			if (sub.length () > 0)
				sub.append (" + ");
			sub.append (sr.report ());
		}

		if (sub.length () > 0)
			ret.append (sub).append (" ");
		else
			ret.append ("&Oslash; ");
		ret.append (MarkupDocument.rightArrow ()).append (" ");
		

		sub = new StringBuilder ();
		for (SBMLSpeciesReference sr : listOfProducts)
		{
			if (sub.length () > 0)
				sub.append (" + ");
			sub.append (sr.report ());
		}

		if (sub.length () > 0)
			ret.append (sub);
		else
			ret.append ("&Oslash;");
		
		if (insert)
			me.addValue (MarkupDocument.insert (ret.toString ()));
		else
			me.addValue (MarkupDocument.delete (ret.toString ()));
		
		sub = new StringBuilder ();
		for (SBMLSimpleSpeciesReference sr : listOfModifiers)
		{
			if (sub.length () > 0)
				sub.append ("; ");
			sub.append (sr.report ());
		}
		
		if (sub.length () > 0)
		{
			if (insert)
				me.addValue (MarkupDocument.insert ("Modifiers: " + sub.toString ()));
			else
				me.addValue (MarkupDocument.delete ("Modifiers: " + sub.toString ()));
		}
	}
}
