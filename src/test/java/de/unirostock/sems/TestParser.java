/**
 * 
 */
package de.unirostock.sems;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import de.binfalse.bflog.LOGGER;
import de.unirostock.sems.bives.sbml.algorithm.SBMLValidator;
import de.unirostock.sems.bives.sbml.parser.SBMLModel;
import de.unirostock.sems.bives.sbml.parser.SBMLSBase;
import de.unirostock.sems.bives.sbml.parser.SBMLSpecies;

/**
 * @author Martin Scharm
 *
 */
@RunWith(JUnit4.class)
public class TestParser
{

	/**
	 * Test sbml test suite.
	 */
	@Test
	public void  testSbmlTestSuite ()
	{
		File root = new File ("/tmp/sbml-test-suite");
		
		if (!root.isDirectory ())
		{
			LOGGER.warn ("skipping sbml test suite. mount cases to ", root.getAbsolutePath (), " to run this test.");
			return;
		}
		
		List<File> toTraverse = new ArrayList<File> ();
		toTraverse.add (root);
		LOGGER.setMinLevel (LOGGER.ERROR);
		
		int fail = 0;
		int processed = 0;
		
		while (toTraverse.size () > 0)
		{
			File next = toTraverse.remove (0);
			
			File [] kids = next.listFiles ();
			for (File kid : kids)
			{
				String kidName = kid.getName ();
				
				if (kid.isDirectory ())
					toTraverse.add (kid);
				else if (kidName.endsWith (".xml") && !kidName.contains ("sedml") && !kidName.contains ("l1v2"))
				{
					if (kidName.contains ("00951-sbml-"))
						continue; // INF and NaN? maybe implement later, but...
					if (kidName.contains ("00937-sbml-l3v1.") || kidName.contains ("00950-sbml-l3v1.xm"))
						continue; // time units = time?
					
					//System.out.println (kid.getAbsolutePath ());
					SBMLValidator validator = new SBMLValidator ();
					if (validator.validate (kid))
					{
						processed++;
						
					}
					else
					{
						fail++;
						LOGGER.error (validator.getError (), "error reading ", kid.getAbsolutePath ());
					}
				}
			}
		}
		assertTrue ("checked just a small number of files...", processed > 1000);
		assertEquals ("there where some errors", 0, fail);
		assertTrue ("found no file", 0 < processed);
		
	}

	/**
	 * Test.
	 */
	@Test
	public void  testOntologyLinks ()
	{
		File testFile = new File ("test/BIOMD0000000107-2012-12-12.xml");
		SBMLValidator validator = new SBMLValidator ();
		if (!validator.validate (testFile))
		{
			validator.getError ().printStackTrace ();
			fail ("failed to validate " + testFile + ": " + validator.getError ());
		}
		
		SBMLModel model = validator.getDocument ().getModel ();
		
		SBMLSBase node = model.getSpecies ("p_dimer_p");
		assertEquals ("expected to see 1 type of ontologylinks for p_dimer_p", 1, node.getOntologyLinks ().size ());
		assertEquals ("expected to see 2 hasPart links in p_dimer_p", 2, node.getOntologyLinks ().get ("hasPart").size ());

		node = model.getSpecies ("cyclin");
		assertEquals ("expected to see 1 type of ontologylinks for cyclin", 1, node.getOntologyLinks ().size ());
		assertEquals ("expected to see 1 'is' links in cyclin", 1, node.getOntologyLinks ().get ("is").size ());
		
		
		node = model.getReaction ("R5");
		assertEquals ("expected to see 2 types of ontologylinks for R5", 2, node.getOntologyLinks ().size ());
		assertEquals ("expected to see 1 'is' links in R5", 1, node.getOntologyLinks ().get ("is").size ());
		assertEquals ("expected to see 2 'isVersionOf' links in R5", 2, node.getOntologyLinks ().get ("isVersionOf").size ());
		
		node = model;
		assertEquals ("expected to see 5 types of ontologylinks for the model node", 5, node.getOntologyLinks ().size ());
		//System.out.println (node.getOntologyLinks ());
		//System.out.println (model.getOntologyMappings ());
	}


	/**
	 * Test for predefined units.
	 */
	@Test
	public void testPredefinedUnits ()
	{
		File testFile = new File ("test/BIOMD0000000173.xml_2012-05-20");
		SBMLValidator validator = new SBMLValidator ();
		if (!validator.validate (testFile))
		{
			validator.getError ().printStackTrace ();
			fail ("failed to validate " + testFile + ": " + validator.getError ());
		}
	}
	
}
