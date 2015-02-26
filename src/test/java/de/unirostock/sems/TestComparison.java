/**
 * 
 */
package de.unirostock.sems;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import de.binfalse.bflog.LOGGER;
import de.unirostock.sems.bives.exception.BivesConnectionException;
import de.unirostock.sems.bives.sbml.algorithm.SBMLValidator;
import de.unirostock.sems.bives.sbml.api.SBMLDiff;
import de.unirostock.sems.bives.sbml.parser.SBMLDocument;
import de.unirostock.sems.xmlutils.tools.XmlTools;

/**
 * @author Martin Scharm
 *
 */
@RunWith(JUnit4.class)
public class TestComparison
{

	/** The Constant SimpleV1. */
	public static final String SimpleV1 = "test/simpleModelV1.sbml";
	
	/** The Constant SimpleV2. */
	public static final String SimpleV2 = "test/simpleModelV2.sbml";

	/**
	 * Test simple model.
	 *
	 * @throws BivesConnectionException the bives connection exception
	 */
	@Test
	public void testSimpleModel () throws BivesConnectionException
	{
		SBMLValidator val = new SBMLValidator ();
		
		assertTrue ("simple model version 1 is unexpectingly not valid", val.validate(new File (SimpleV1)));
		SBMLDocument version1 = val.getDocument();

		assertTrue ("simple model version 2 is unexpectingly not valid", val.validate(new File (SimpleV2)));
		SBMLDocument version2 = val.getDocument();
		
		SBMLDiff differ = new SBMLDiff (version1, version2);
		differ.mapTrees();
		
		try
		{
			//LOGGER.setMinLevel (LOGGER.DEBUG);
			JSONObject graph = (JSONObject) (new JSONParser ()).parse (differ.getReactionsJsonGraph());
			JSONArray edges = (JSONArray) ((JSONObject) graph.get ("elements")).get ("edges");
			JSONArray nodes = (JSONArray) ((JSONObject) graph.get ("elements")).get ("nodes");
			//System.out.println (nodes);
			assertEquals ("unexpected number of nodes in simple model comparison graph thing. (1 compartment + 2 reactions + 5 species)", 8, nodes.size ());
			assertEquals ("unexpected number of edges in simple model comparison graph thing.", 7, edges.size ());
		}
		catch (Exception e)
		{
			fail ("error parsing json graph: " + e);
		}
		
	}
	

	/**
	 * Test MPs model as found in 2MT.
	 *
	 * @throws BivesConnectionException the bives connection exception
	 */
	@Test
	public void testMPsModel () throws BivesConnectionException
	{
		String v1 = "test/mp/2007-06-05";
		String v2 = "test/mp/2009-03-25";
		
		
		SBMLValidator val = new SBMLValidator ();
		
		assertTrue ("mp model version 1 is unexpectingly not valid", val.validate(new File (v1)));
		SBMLDocument version1 = val.getDocument();

		assertTrue ("mp model version 2 is unexpectingly not valid", val.validate(new File (v2)));
		SBMLDocument version2 = val.getDocument();
		
		SBMLDiff differ = new SBMLDiff (version1, version2);
		differ.mapTrees();
		
		try
		{
			assertTrue ("expected to see a nice diff", 100 < XmlTools.prettyPrintDocument (differ.getPatch ().getDocument ()).length ());
			assertNotNull ("expected to get an html report", differ.getHTMLReport ());
			assertNotNull ("expected to get a graphml network", differ.getReactionsGraphML ());
		}
		catch (Exception e)
		{
			e.printStackTrace ();
			fail ("error parsing json graph: " + e);
		}
		
		
	}
}
