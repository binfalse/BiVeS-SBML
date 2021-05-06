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
import de.unirostock.sems.bives.api.Diff;
import de.unirostock.sems.bives.ds.Patch;
import de.unirostock.sems.bives.exception.BivesConnectionException;
import de.unirostock.sems.bives.sbml.algorithm.SBMLValidator;
import de.unirostock.sems.bives.sbml.api.SBMLDiff;
import de.unirostock.sems.bives.sbml.parser.SBMLDocument;
import de.unirostock.sems.xmlutils.tools.XmlTools;


//import XPath packages
import java.io.IOException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

/**
 * @author Tom Gebhardt
 *
 */
@RunWith(JUnit4.class)
public class TestMerge
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
	public void testDiffer () throws BivesConnectionException
	{
		SBMLValidator val = new SBMLValidator ();
		
		assertTrue ("simple model version 1 is unexpectingly not valid", val.validate(new File (SimpleV1)));
		SBMLDocument version1 = val.getDocument();

		assertTrue ("simple model version 2 is unexpectingly not valid", val.validate(new File (SimpleV2)));
		SBMLDocument version2 = val.getDocument();
		
		SBMLDiff differ = new SBMLDiff (version1, version2);

		
		try
		{
			differ.mapTrees ();
			
			Patch patch = differ.getPatch ();
			
			String diff = differ.getDiff ();
			System.out.println(differ.getDiff());
			
			
			//LOGGER.setMinLevel (LOGGER.DEBUG);
//			JSONObject graph = (JSONObject) (new JSONParser ()).parse (differ.getReactionsJsonGraph());
//			JSONArray edges = (JSONArray) ((JSONObject) graph.get ("elements")).get ("edges");
//			JSONArray nodes = (JSONArray) ((JSONObject) graph.get ("elements")).get ("nodes");
//			//System.out.println (nodes);
//			assertEquals ("unexpected number of nodes in simple model comparison graph thing. (1 compartment + 2 reactions + 5 species)", 8, nodes.size ());
//			assertEquals ("unexpected number of edges in simple model comparison graph thing.", 7, edges.size ());
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
		differ.mapTrees(Diff.ALLOW_DIFFERENT_IDS, Diff.CARE_ABOUT_NAMES, Diff.STRICTER_NAMES);
		
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
	

	/**
	 * Test ontology mapping.
	 */
	@Test
	public void testOntologyMapping ()
	{
		String v1 = "test/test-onto1";
		String v2 = "test/test-onto2";
		
		SBMLValidator val = new SBMLValidator ();
		
		assertTrue ("model version 1 is unexpectingly not valid", val.validate(new File (v1)));
		SBMLDocument version1 = val.getDocument();
		
		assertTrue ("model version 2 is unexpectingly not valid", val.validate(new File (v2)));
		SBMLDocument version2 = val.getDocument();
		
		SBMLDiff differ = new SBMLDiff (version1, version2);
		try
		{
			differ.mapTrees(Diff.ALLOW_DIFFERENT_IDS, Diff.CARE_ABOUT_NAMES, Diff.STRICTER_NAMES);
		}
		catch (BivesConnectionException e)
		{
			LOGGER.error (e, "couldn't map trees");
			fail ("couldn't map trees");
		}
		
		// System.out.println (XmlTools.prettyPrintDocument (differ.getPatch ().getDocument ()));
		
		Patch p = differ.getPatch ();
		assertEquals ("expected to see only 6 inserts", 6, p.getNumInserts ());
		assertEquals ("expected to see exactly 6 updates", 6, p.getNumUpdates ());
		
		LOGGER.setMinLevel (LOGGER.WARN);
	}
}
