/**
 * 
 */
package de.unirostock.sems.bives.sbml.parser;

import de.unirostock.sems.bives.sbml.exception.BivesSBMLParseException;
import de.unirostock.sems.xmlutils.ds.DocumentNode;


/**
 * The Class SBMLGenericIdNameObject representing a generic object that has an id and a name.
 *
 * @author Martin Scharm
 */
public abstract class SBMLGenericIdNameObject
extends SBMLSBase
{
	
	/** The id. */
	protected String id;
	
	/** The name. */
	protected String name; // optional
	
	/**
	 * Instantiates a new SBML generic id-name-object.
	 *
	 * @param documentNode the document node encoding this entity in the corresponding XML tree
	 * @param sbmlModel the SBML model
	 * @throws BivesSBMLParseException the bives sbml parse exception
	 */
	public SBMLGenericIdNameObject (DocumentNode documentNode, SBMLModel sbmlModel)
		throws BivesSBMLParseException
	{
		super (documentNode, sbmlModel);
		
		if (documentNode != null)
		{
			id = documentNode.getAttribute ("id");
			if (id == null || id.length () < 1)
				throw new BivesSBMLParseException ("node doesn't provide a valid id: " + documentNode.getXPath ());
			
			name = documentNode.getAttribute ("name");
		}
	}
	
	/**
	 * Gets the name (if defined) or the id (if name undefined).
	 *
	 * @return the name or id
	 */
	public final String getNameOrId ()
	{
		if (name == null)
			return id;
		return name;
	}
	
	/**
	 * Gets the name (if defined) and the id as: <code>NAME (ID)</code>.
	 *
	 * @return the name and id
	 */
	public final String getNameAndId ()
	{
		if (name == null)
			return id;
		return id + " (" + name + ")";
	}
	
	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public final String getID ()
	{
		return id;
	}
	
	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public final String getName ()
	{
		return name;
	}
}
