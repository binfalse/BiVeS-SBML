/**
 * 
 */
package de.unirostock.sems;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import de.binfalse.bflog.LOGGER;
import de.unirostock.sems.bives.exception.BivesConnectionException;
import de.unirostock.sems.bives.sbml.algorithm.SBMLValidator;
import de.unirostock.sems.bives.sbml.api.SBMLDiff;
import de.unirostock.sems.bives.sbml.parser.SBMLDocument;

/**
 * @author Martin Scharm
 *
 */
@RunWith(JUnit4.class)
public class TestComparison
{

	public static final String SimpleV1 = "test/simpleModelV1.sbml";
	public static final String SimpleV2 = "test/simpleModelV2.sbml";

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
			JSONObject graph = (JSONObject) (new JSONParser ()).parse (differ.getCRNJsonGraph());
			JSONArray edges = (JSONArray) ((JSONObject) graph.get ("elements")).get ("edges");
			JSONArray nodes = (JSONArray) ((JSONObject) graph.get ("elements")).get ("nodes");
			assertEquals ("unexpected number of nodes in simple model comparison graph thing. (1 compartment + 2 reactions + 5 species)", 8, nodes.size ());
			assertEquals ("unexpected number of edges in simple model comparison graph thing.", 7, edges.size ());
		}
		catch (Exception e)
		{
			fail ("error parsing json graph: " + e);
		}
		
	}
}
