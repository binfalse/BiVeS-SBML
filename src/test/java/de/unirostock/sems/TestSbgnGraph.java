/**
 * 
 */
package de.unirostock.sems;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;

import org.jdom2.Document;
import org.jdom2.input.SAXBuilder;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import de.binfalse.bflog.LOGGER;
import de.unirostock.sems.bives.api.Diff;
import de.unirostock.sems.bives.api.RegularDiff;
import de.unirostock.sems.bives.ds.Patch;
import de.unirostock.sems.bives.ds.graph.GraphTranslatorSbgnJson;
import de.unirostock.sems.bives.ds.rn.ReactionNetwork;
import de.unirostock.sems.bives.sbml.api.SBMLDiff;
import de.unirostock.sems.bives.tools.BivesTools;
import de.unirostock.sems.xmlutils.ds.TreeDocument;
import de.unirostock.sems.xmlutils.tools.XmlTools;





/**
 * @author Martin Scharm
 * @author Tom Gebhardt
 */
@RunWith(JUnit4.class)
public class TestSbgnGraph
{
	private static final File		SIMPLE_DOC	= new File ("test/GlyphsAndArcs_noTypo1.xml");

	private static TreeDocument simpleFile;

	/**
	 * Read files.
	 */
	@BeforeClass
	public static void readFiles ()
	{
		if (SIMPLE_DOC.canRead ())
		{
			try
			{
				simpleFile = new TreeDocument (XmlTools.readDocument (SIMPLE_DOC), SIMPLE_DOC.toURI ());
				System.out.println("tried reading file");
			}
			catch (Exception e)
			{
				LOGGER.error (e, "cannot read ", SIMPLE_DOC, " -> skipping tests");
			}
		}
		else
		{
			LOGGER.error ("cannot read ", SIMPLE_DOC, " -> skipping tests");
		}
	}
	/**
	 * Test SBGN graph.
	 */
	@Test
	public void testSbgnGraph ()
	{		
		try
		{
			//get Files
			//File a = SIMPLE_DOC;
			//File b = SIMPLE_DOC;
			//File a = new File ("test/BIOMD0000000329-v5.xml");
			//File b = new File ("test/BIOMD0000000329-v6.xml");
			File a = new File ("test/allGlyphsAndArcs.xml");
			File b = new File ("test/GlyphsAndArcs_noTypo1.xml");

			SAXBuilder builder = new SAXBuilder();
			Document d1 = builder.build(a);
			Document d2 = builder.build(b);
			
			TreeDocument td1 = new TreeDocument (d1, null);
			TreeDocument td2 = new TreeDocument (d2, null);
			SBMLDiff differ = new SBMLDiff (td1, td2);
			
			differ.mapTrees(true, false, false);
			//differ.getPatch();
			//System.out.println(differ.getPatch());
			//differ.getReactionsSbgnJsonGraph();
			System.out.println("...");
			System.out.println(differ.getDiff(true));
			System.out.println("...");
			System.out.println(differ.getReactionsSbgnJsonGraph());

		}
		catch (Exception e) {
			System.out.println("SBML Error: " + e);
		}
	}
}
