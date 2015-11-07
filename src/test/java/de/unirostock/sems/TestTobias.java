/**
 * 
 */
package de.unirostock.sems;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import de.binfalse.bflog.LOGGER;
import de.unirostock.sems.bives.api.Diff;
import de.unirostock.sems.bives.exception.BivesConnectionException;
import de.unirostock.sems.bives.markup.Typesetting;
import de.unirostock.sems.bives.sbml.algorithm.SBMLValidator;
import de.unirostock.sems.bives.sbml.api.SBMLDiff;
import de.unirostock.sems.bives.sbml.parser.SBMLDocument;

/**
 * @author Martin Scharm
 *
 */
@RunWith(JUnit4.class)
public class TestTobias
{

	/**
	 * Test sbml test suite.
	 */
	@Test
	public void  testTobiasFiles13 ()
	{
		LOGGER.setLevel (LOGGER.ERROR);
		System.out.println (" ------------------------------------------ ");
		File v1 = new File ("test/modelv1.xml");
		File v3 = new File ("test/modelv3.xml");
		/*Connector.allowDifferentIds = true;
		Connector.stricterNames = true;*/
		SBMLValidator val = new SBMLValidator ();
		
		assertTrue (v1 + "tobias v1 is not valid", val.validate(v1));
		SBMLDocument version1 = val.getDocument();

		assertTrue (v3 + " is not valid", val.validate(v3));
		SBMLDocument version2 = val.getDocument();
		
		SBMLDiff differ = new SBMLDiff (version1, version2);
		try
		{
			long startTime = System.currentTimeMillis();
			differ.mapTrees(true, true, true);
			long stopTime = System.currentTimeMillis();
      long elapsedTime = stopTime - startTime;
      System.out.println("time: " + elapsedTime);
      

      long seconds = elapsedTime / 1000;
      long minutes = seconds / 60;
      seconds %= 60;
      long hours = minutes / 60;
      minutes %= 60;
      hours %= 24;
      System.out.println("time: " + hours + "h " + minutes + "m " + seconds + "s");
		}
		catch (BivesConnectionException e1)
		{
			e1.printStackTrace();
			fail ("wasn't able to map trees");
		}
		
		try
		{
			File tmp = File.createTempFile ("report-" + new SimpleDateFormat ("yyyy-MM-dd-hh-mm-ss").format (new Date ()), ".html");
			BufferedWriter bw = new BufferedWriter (new FileWriter (tmp));
			bw.write (htmlPageStart () + differ.getHTMLReport () + htmlPageEnd ());
			bw.close ();
			System.out.println (tmp);
			
			File tmp2 = File.createTempFile ("diff-" + new SimpleDateFormat ("yyyy-MM-dd-hh-mm-ss").format (new Date ()), ".xml");
			bw = new BufferedWriter (new FileWriter (tmp2));
			bw.write (differ.getDiff ());
			bw.close ();
			
			System.out.println (tmp2);
			//System.out.println (differ.getHTMLReport ());
			//LOGGER.setMinLevel (LOGGER.DEBUG);
			/*JSONObject graph = (JSONObject) (new JSONParser ()).parse (differ.getReactionsJsonGraph());
			JSONArray edges = (JSONArray) ((JSONObject) graph.get ("elements")).get ("edges");
			JSONArray nodes = (JSONArray) ((JSONObject) graph.get ("elements")).get ("nodes");
			//System.out.println (nodes);
			assertEquals ("unexpected number of nodes in simple model comparison graph thing. (1 compartment + 2 reactions + 5 species)", 8, nodes.size ());
			assertEquals ("unexpected number of edges in simple model comparison graph thing.", 7, edges.size ());*/
		}
		catch (Exception e)
		{
			fail ("error parsing json graph: " + e);
		}
	}
	

	
	public static String htmlPageStart ()
	{
		return "<!DOCTYPE html>"
			+ Typesetting.NL_TXT
			+ "<html><head><title>BiVeS differences</title>"
			+ "<style type=\"text/css\">"
			+ ".bives-insert"
			+ "{color:#01DF01;}"
			+ ".bives-delete"
			+ "{color:#FF4000;}"
			+ ".bives-attr"
			+ "{font-weight: bold;font-style: italic;}"
			+ ".bives-suppl"
			+ "{color:#A4A4A4;}"
			+ ".bives-update"
			+ "{color:#DFA601;}"
			+ ".bives-move"
			+ "{color:#014ADF;}"
			+ "</style>"
			+ "</head><body>";

	}
	
	public static String htmlPageEnd ()
	{
		return "</body></html>";
	}
	
}
