/**
 * 
 */
package de.unirostock.sems;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.jdom2.Document;
import org.jdom2.JDOMException;
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
import de.unirostock.sems.xmlutils.ds.TreeDocument;
import de.unirostock.sems.xmlutils.exception.XmlDocumentParseException;
import de.unirostock.sems.xmlutils.tools.DocumentTools;
import de.unirostock.sems.xmlutils.tools.XmlTools;

/**
 * @author Martin Scharm
 *
 */
@RunWith(JUnit4.class)
public class TestAnnotations
{

	/** The template for the annotation test. */
	public static final File ANNO_TEMPLATE = new File ("test/annotationTestTemplate.sbml");


	/**
	 * obtain the default sbml document
	 * @return the sbml document stored in the test file
	 */
	private static SBMLDocument  getValidTestModel ()
	{
		SBMLValidator val = new SBMLValidator ();
		if (!val.validate (ANNO_TEMPLATE))
		{
			LOGGER.error (val.getError (), "annotation test case is not valid");
			fail ("annotation test case is not valid: " + val.getError ().toString ());
		}
		return val.getDocument ();
	}
	
	/**
	 * obtain the sbml document encoded in s.
	 *
	 * @param s the sbml code of the model
	 * @return the model
	 */
	private static SBMLDocument getModel (String s)
	{
		SBMLValidator val = new SBMLValidator ();
		if (!val.validate (s))
		{
			LOGGER.error (val.getError (), "annotation test case is not valid");
			val.getError ().printStackTrace ();
			fail ("annotation test case is not valid: " + val.getError ().toString ());
		}
		return val.getDocument ();
	}
	
	
	
	/**
	 * 
	 */
	@Test
	public void  testVariableNameDiffers ()
	{
		try
		{
			SBMLDocument doc1 = getValidTestModel ();
			SBMLDocument doc2 = getValidTestModel ();
			// update model -> change name of a variable
			doc2.getTreeDocument ().getNodeById ("E").setAttribute ("name", "nouse");
			doc2 = getModel (XmlTools.prettyPrintDocument (DocumentTools.getDoc (doc2.getTreeDocument ())));
			
			SBMLDiff differ = new SBMLDiff (doc1, doc2);
			differ.mapTrees ();
			checkDiff (differ);

//			System.out.println (differ.getDiff ());
			simpleCheckAnnotations (differ, 0, 0, 1, 0, false, false, false, false, false, true, false, false, false, false, false, false, false);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			fail ("unexpected exception while diffing cellml models: " + e.getMessage ());
		}
	}
	
	
	
	/**
	 * 
	 */
	@Test
	public void  testModelsEqual ()
	{
		try
		{
			SBMLDocument doc1 = getValidTestModel ();
			SBMLDocument doc2 = getValidTestModel ();
			
			SBMLDiff differ = new SBMLDiff (doc1, doc2);
			differ.mapTrees ();
			checkDiff (differ);
			
			Patch patch = differ.getPatch ();
			
			String diff = differ.getDiff ();
			Document patchDoc = patch.getDocument (false);
			TreeDocument myPatchDoc = new TreeDocument (patchDoc, null);

			simpleCheckAnnotations (differ, 0, 0, 0, 0, false, false, false, false, false, false, false, false, false, false, false, false, false);
			assertNotNull ("diff shouldn't be null", diff);
			assertEquals ("didn't expect any changes", 5, myPatchDoc.getNumNodes ());
			assertTrue ("didn't expect any changes but some annotations", 5 < new TreeDocument (patch.getDocument (true), null).getNumNodes ());
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
			fail ("unexpected exception while diffing cellml models: " + e.getMessage ());
		}
	}
	
	
	
	/**
	 * Simple check annotations.
	 *
	 * @param differ the differ
	 * @param ins the ins
	 * @param del the del
	 * @param up the up
	 * @param mov the mov
	 * @param changeVariableDef the change variable def
	 * @param changeComponentDef the change component def
	 * @param changeMathModel the change math model
	 * @param changeSpec the change spec
	 * @param changeModelName the change model name
	 * @param changeEntityIdentifier the change entity identifier
	 * @param changeReactionNetwork the change reaction network
	 * @param changeReactionReversibility the change reaction reversibility
	 * @param changeUnits the change units
	 * @param changeVariableConnection the change variable connection
	 * @param changeComponentHierarchy the change component hierarchy
	 * @param changeAnnotation the change annotation
	 * @param changeTextualDescription the change textual description
	 * @throws XmlDocumentParseException the xml document parse exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws JDOMException the jDOM exception
	 */
	private void simpleCheckAnnotations (
		SBMLDiff differ,
		int ins, int del, int up, int mov,
		boolean changeVariableDef, boolean changeComponentDef, boolean changeMathModel,
		boolean changeSpec, boolean changeModelName, boolean changeEntityIdentifier,
		boolean changeReactionNetwork, boolean changeReactionReversibility,
		boolean changeUnits, boolean changeVariableConnection, boolean changeComponentHierarchy,
		boolean changeAnnotation, boolean changeTextualDescription
		) throws XmlDocumentParseException, IOException, JDOMException
	{
		// stolen from my logger :)
		StackTraceElement ste =  Thread.currentThread().getStackTrace()[2];
		String pre = ste.getClassName () + "@" + ste.getLineNumber() + ": ";

		Patch patch = differ.getPatch ();
		String diff = differ.getDiff ();
		Document patchDoc = patch.getDocument (false);
		TreeDocument myPatchDoc = new TreeDocument (patchDoc, null);
		String annotations = patch.getAnnotationDocumentXml ();
		TreeDocument annotationsDoc = new TreeDocument (XmlTools.readDocument (annotations), null);
		
		assertNotNull (pre + "diff shouldn't be null", diff);
		assertEquals (pre + "expected exactly " + (ins + del + up + mov) + " changes", 5 + ins + del + up + mov, myPatchDoc.getNumNodes ());
		assertEquals (pre + "expected exactly " + del + " del", del, patch.getNumDeletes ());
		assertEquals (pre + "expected exactly " + ins + " ins", ins, patch.getNumInserts ());
		assertEquals (pre + "expected exactly " + up + " up", up, patch.getNumUpdates ());
		assertEquals (pre + "expected exactly " + mov + " mov", mov, patch.getNumMoves ());
		assertTrue (pre + "there should be some annotation, even if there weren't any changes", annotationsDoc.getNumNodes () > 5);
		assertEquals (pre + "occurence of http://purl.org/net/comodi#VariableDefinition", changeVariableDef, annotations.contains ("http://purl.org/net/comodi#VariableDefinition"));
		assertEquals (pre + "occurence of http://purl.org/net/comodi#ComponentDefinition", changeComponentDef, annotations.contains ("http://purl.org/net/comodi#ComponentDefinition"));
		assertEquals (pre + "occurence of http://purl.org/net/comodi#MathematicalModel", changeMathModel, annotations.contains ("http://purl.org/net/comodi#MathematicalModel"));
		assertEquals (pre + "occurence of http://purl.org/net/comodi#CellmlSpecification", changeSpec, annotations.contains ("http://purl.org/net/comodi#CellmlSpecification"));
		assertEquals (pre + "occurence of http://purl.org/net/comodi#ModelName", changeModelName, annotations.contains ("http://purl.org/net/comodi#ModelName"));
		assertEquals ("occurence of http://purl.org/net/comodi#EntityIdentifier", changeEntityIdentifier, annotations.contains ("http://purl.org/net/comodi#EntityIdentifier"));
		assertEquals ("occurence of http://purl.org/net/comodi#ReactionNetwork", changeReactionNetwork, annotations.contains ("http://purl.org/net/comodi#ReactionNetwork"));
		assertEquals ("occurence of http://purl.org/net/comodi#ReactionReversibility", changeReactionReversibility, annotations.contains ("http://purl.org/net/comodi#ReactionReversibility"));
		assertEquals ("occurence of http://purl.org/net/comodi#UnitDefinition", changeUnits, annotations.contains ("http://purl.org/net/comodi#UnitDefinition"));
		assertEquals ("occurence of http://purl.org/net/comodi#VariableConnections", changeVariableConnection, annotations.contains ("http://purl.org/net/comodi#VariableConnections"));
		assertEquals ("occurence of http://purl.org/net/comodi#ComponentHierarchy", changeComponentHierarchy, annotations.contains ("http://purl.org/net/comodi#ComponentHierarchy"));
		assertEquals ("occurence of http://purl.org/net/comodi#Annotation", changeAnnotation, annotations.contains ("http://purl.org/net/comodi#Annotation"));
		assertEquals ("occurence of http://purl.org/net/comodi#TextualDescription", changeTextualDescription, annotations.contains ("http://purl.org/net/comodi#TextualDescription"));
//	assertEquals ("occurence of http://purl.org/net/comodi#", , annotations.contains ("http://purl.org/net/comodi#"));
	}

	/**
	 * @param diff
	 * @throws ParserConfigurationException
	 */
	public void checkDiff (Diff diff) throws ParserConfigurationException
	{
		try 
		{
			String reactionsGraphMl = diff.getReactionsGraphML ();
			String reactionsDot = diff.getReactionsDotGraph ();
			String reactionsJson = diff.getReactionsJsonGraph ();
			assertTrue ("reactionsGraphMl shouldn't be null", reactionsGraphMl == null || reactionsGraphMl.length () > 10);
			assertTrue ("reactionsDot shouldn't be null", reactionsDot == null || reactionsDot.length () > 10);
			assertTrue ("reactionsJson shouldn't be null", reactionsJson == null || reactionsJson.length () > 10);
	
			String hierarchyGraphml = diff.getHierarchyGraphML ();
			String hierarchyDot = diff.getHierarchyGraphML ();
			String hierarchyJson = diff.getHierarchyGraphML ();
			assertTrue ("hierarchyGraphml shouldn't be null", hierarchyGraphml == null || hierarchyGraphml.length () > 10);
			assertTrue ("hierarchyDot shouldn't be null", hierarchyDot == null || hierarchyDot.length () > 10);
			assertTrue ("hierarchyJson shouldn't be null", hierarchyJson == null || hierarchyJson.length () > 10);
	
			String html = diff.getHTMLReport ();
			String md = diff.getMarkDownReport ();
			String rst = diff.getReStructuredTextReport ();
			assertNotNull ("html shouldn't be null", html);
			assertNotNull ("md shouldn't be null", md);
			assertNotNull ("rst shouldn't be null", rst);
		}
		catch (Exception e)
		{
			fail ("unexpected exception " + e);
		}
	}
}
