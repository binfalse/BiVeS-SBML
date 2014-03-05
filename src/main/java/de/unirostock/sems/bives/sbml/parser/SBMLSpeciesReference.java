/**
 * 
 */
package de.unirostock.sems.bives.sbml.parser;


import java.util.List;

import de.binfalse.bfutils.GeneralTools;
import de.unirostock.sems.bives.algorithm.SimpleConnectionManager;
import de.unirostock.sems.bives.ds.MathML;
import de.unirostock.sems.bives.markup.MarkupDocument;
import de.unirostock.sems.bives.sbml.exception.BivesSBMLParseException;
import de.unirostock.sems.xmlutils.ds.DocumentNode;
import de.unirostock.sems.xmlutils.ds.TreeNode;


/**
 * The Class SBMLSpeciesReference representing a link to a species.
 *
 * @author Martin Scharm
 */
public class SBMLSpeciesReference
	extends SBMLSimpleSpeciesReference
{
	
	/** The stoichiometry factor. */
	private Double stoichiometry;
	
	/** The stoichiometry math. */
	private MathML stoichiometryMath; // only level 2
	
	/** The constant. */
	private boolean constant; // only level 3+
	
	/**
	 * Instantiates a new SBML species reference.
	 *
	 * @param documentNode the document node encoding this entity in the corresponding XML tree
	 * @param sbmlModel the corresponding SBML model
	 * @throws BivesSBMLParseException the bives sbml parse exception
	 */
	public SBMLSpeciesReference (DocumentNode documentNode,
		SBMLModel sbmlModel) throws BivesSBMLParseException
	{
		super (documentNode, sbmlModel);
		
		if (documentNode.getAttribute ("stoichiometry") != null)
		{
			try
			{
				stoichiometry = Double.parseDouble (documentNode.getAttribute ("stoichiometry"));
			}
			catch (Exception e)
			{
				throw new BivesSBMLParseException ("stoichiometry of species reference "+id+" of unexpected format: " + documentNode.getAttribute ("stoichiometry"));
			}
		}
		else // level <= 2
		{
			// is there stoichiometryMath?
			List<TreeNode> maths = documentNode.getChildrenWithTag ("stoichiometryMath");
			if (maths.size () > 1)
				throw new BivesSBMLParseException ("SpeciesReference has "+maths.size ()+" stoichiometryMath elements. (expected not more than one element)");
			if (maths.size () == 1)
			{
				maths = ((DocumentNode) maths.get (0)).getChildrenWithTag ("math");
				if (maths.size () != 1)
					throw new BivesSBMLParseException ("stoichiometryMath in SpeciesReference has "+maths.size ()+" math elements. (expected exactly one element)");
				stoichiometryMath = new MathML ((DocumentNode) maths.get (0));
			}
			else
				stoichiometry = 1.;
		}
		
		if (documentNode.getAttribute ("constant") != null)
		{
			try
			{
				constant = Boolean.parseBoolean (documentNode.getAttribute ("constant"));
			}
			catch (Exception e)
			{
				throw new BivesSBMLParseException ("constant of species reference "+id+" of unexpected format: " + documentNode.getAttribute ("constant"));
			}
		}
		else
			constant = false; // level <= 2
	}

	/**
	 * Report the modifications of this species reference.
	 *
	 * @param conMgmt the connection manager
	 * @param a the original species reference
	 * @param b the modified species reference
	 * @return the string
	 */
	public String reportModification (SimpleConnectionManager conMgmt, SBMLSpeciesReference a, SBMLSpeciesReference b)
	{
		String retA = GeneralTools.prettyDouble (a.stoichiometry, 1) + a.species.getID ();
		String retB = GeneralTools.prettyDouble (b.stoichiometry, 1) + b.species.getID ();
		
		if (retA.equals (retB))
			return retA;
		else
			return MarkupDocument.delete (retA) + " + " + MarkupDocument.insert (retB);
	}

	/* (non-Javadoc)
	 * @see de.unirostock.sems.bives.sbml.parser.SBMLSimpleSpeciesReference#reportInsert()
	 */
	@Override
	public String reportInsert ()
	{
		return MarkupDocument.insert (GeneralTools.prettyDouble (stoichiometry, 1) + species.getID ());
	}

	/* (non-Javadoc)
	 * @see de.unirostock.sems.bives.sbml.parser.SBMLSimpleSpeciesReference#reportDelete()
	 */
	@Override
	public String reportDelete ()
	{
		return MarkupDocument.delete (GeneralTools.prettyDouble (stoichiometry, 1) + species.getID ());
	}

	/* (non-Javadoc)
	 * @see de.unirostock.sems.bives.sbml.parser.SBMLSimpleSpeciesReference#report()
	 */
	@Override
	public String report ()
	{
		return GeneralTools.prettyDouble (stoichiometry, 1) + species.getID ();
	}
	
}
