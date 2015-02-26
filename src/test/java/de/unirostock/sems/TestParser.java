/**
 * 
 */
package de.unirostock.sems;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import de.binfalse.bflog.LOGGER;
import de.unirostock.sems.bives.sbml.algorithm.SBMLValidator;

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
		
		assertEquals ("there where some errors", 0, fail);
		assertTrue ("found no file", 0 < processed);
		
	}
}
