/**
 * 
 */
package de.unirostock.sems.bives.sbml.parser;

import de.binfalse.bfutils.GeneralTools;
import de.unirostock.sems.bives.exception.BivesDocumentConsistencyException;
import de.unirostock.sems.bives.markup.Markup;
import de.unirostock.sems.bives.markup.MarkupDocument;
import de.unirostock.sems.bives.sbml.exception.BivesSBMLParseException;
import de.unirostock.sems.xmlutils.ds.DocumentNode;


/**
 * The Class SBMLUnit representing a factor based on a unit: 
 * 
 * <pre>
 * (multiplier * 10^scale * kind)^exponent
 *	</pre>
 *
 * @author Martin Scharm
 */
public class SBMLUnit
	extends SBMLSBase
	implements Markup
{

	/** those are BASE_UNITS. */
	public final static String [] BASE_UNITS = new String [] {"ampere", "farad", "joule", "lux", "radian", "volt", "avogadro", "gram", "katal", "metre", "second", "watt", "becquerel", "gray", "kelvin", "mole", "siemens", "weber", "candela", "henry", "kilogram", "newton", "sievert", "coulomb", "hertz", "litre", "ohm", "steradian", "dimensionless", "item", "lumen", "pascal", "tesla"};

	/** those are predefined units. */
	public final static String [] PREDEFINED_UNITS = new String [] {"substance", "volume", "area", "length", "time"};
	
	/** The unit this unit is based on. */
	private SBMLUnitDefinition kind;
	
	/** The exponent. */
	private double exponent;
	
	/** The scale. */
	private int scale;
	
	/** The multiplier. */
	private double multiplier;

	
	/**
	 * Instantiates a new SBML unit derived from a base unit.
	 *
	 * @param documentNode the document node encoding this entity in the corresponding XML tree
	 * @param sbmlModel the SBML model
	 * @throws BivesSBMLParseException the bives sbml parse exception
	 * @throws BivesDocumentConsistencyException the bives document consistency exception
	 */
	public SBMLUnit (DocumentNode documentNode, SBMLModel sbmlModel) throws BivesSBMLParseException, BivesDocumentConsistencyException
	{
		super (documentNode, sbmlModel);
		
		String kindStr = documentNode.getAttributeValue ("kind");
		kind = sbmlModel.getUnitDefinition (kindStr);
		
		if (kind == null || !kind.isBaseUnit ())
			throw new BivesDocumentConsistencyException ("Unit kind attribute not defined or not base unit: " + kindStr);


		if (documentNode.getAttributeValue ("multiplier") != null)
		{
			try
			{
				multiplier = Double.parseDouble (documentNode.getAttributeValue ("multiplier"));
			}
			catch (Exception e)
			{
				throw new BivesSBMLParseException ("multiplier of unexpected format: " + documentNode.getAttributeValue ("multiplier"));
			}
		}
		else
			multiplier = 1; // level <= 2

		if (documentNode.getAttributeValue ("scale") != null)
		{
			try
			{
				scale = Integer.parseInt (documentNode.getAttributeValue ("scale"));
			}
			catch (Exception e)
			{
				throw new BivesSBMLParseException ("scale of unexpected format: " + documentNode.getAttributeValue ("scale"));
			}
		}
		else
			scale = 0; // level <= 2
		
		if (documentNode.getAttributeValue ("exponent") != null)
		{
			try
			{
				exponent = Double.parseDouble (documentNode.getAttributeValue ("exponent"));
			}
			catch (Exception e)
			{
				throw new BivesSBMLParseException ("exponent of unexpected format: " + documentNode.getAttributeValue ("exponent"));
			}
		}
		else
			exponent = 1; // level <= 2
	}

	/* (non-Javadoc)
	 * @see de.unirostock.sems.bives.markup.Markup#markup()
	 */
	@Override
	public String markup ()
	{
		StringBuilder ret = new StringBuilder ("(")
		.append (GeneralTools.prettyDouble (multiplier, 1, "", MarkupDocument.multiply ()));
		
		if (scale != 0)
			ret.append ("10^").append (scale).append (MarkupDocument.multiply ());
		
		ret.append ("[").append (kind.name).append ("]")
		.append (GeneralTools.prettyDouble (exponent, 1, "^", ""));

		return ret.append (")").toString ();
	}
}
