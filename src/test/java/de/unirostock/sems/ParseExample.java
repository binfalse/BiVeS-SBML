/**
 * 
 */
package de.unirostock.sems;

import java.io.File;

import de.unirostock.sems.bives.sbml.algorithm.SBMLValidator;
import de.unirostock.sems.bives.sbml.parser.SBMLDocument;


/**
 * @author Martin Scharm
 *
 */
public class ParseExample
{
	
	/**
	 * @param args
	 */
	public static void main (String[] args)
	{      
		File document = new File ("your/sbml/model");
		
		// create a SBML validator
		SBMLValidator validator = new SBMLValidator ();
		
		// is that document valid?
		if (!validator.validate (document))
			// if not: print the error (which is an exception)
			System.err.println (validator.getError ());
		
		// get the document
		SBMLDocument doc = validator.getDocument ();
		
		
		
		
	}
	
}
