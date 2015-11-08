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
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import de.binfalse.bflog.LOGGER;
import de.unirostock.sems.bives.api.Diff;
import de.unirostock.sems.bives.ds.Patch;
import de.unirostock.sems.bives.sbml.algorithm.SBMLValidator;
import de.unirostock.sems.bives.sbml.api.SBMLDiff;
import de.unirostock.sems.bives.sbml.parser.SBMLDocument;
import de.unirostock.sems.xmlutils.ds.DocumentNode;
import de.unirostock.sems.xmlutils.ds.TextNode;
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
	 * Test species name differs.
	 */
	@Test
	public void  testSpecChange1 ()
	{
		try
		{
			SBMLDocument doc1 = getValidTestModel ();
			SBMLDocument doc2 = getValidTestModel ();
			doc2.getTreeDocument ().getRoot ().setAttribute ("level", "23");
			doc2 = getModel (XmlTools.prettyPrintDocument (DocumentTools.getDoc (doc2.getTreeDocument ())));
			
			SBMLDiff differ = new SBMLDiff (doc1, doc2);
			differ.mapTrees ();
			checkDiff (differ);

//			System.out.println (differ.getDiff ());
			simpleCheckAnnotations (differ, 0, 0, 1, 0,
				false, false, false, false,
				false, false, false, false,
				false, false, false, false,
				false, true, false, false,
				false, false, false, false,
				false, false, false, false);
			
			/*
				changeSpeciesDef, changeFunctionDefinition, changeEventDefinition, changeRules,
				changeMetaIdentifier, changePerson, changeContributor, changeDate,
				changeCreationDate, changeModificationDate,	changeEntityIdentifier, changeEntityName,
				changeMathModel, changeSpecLevel, changeSpecVersion, changeModelName,
				changeReactionNetwork, changeReactionReversibility, changeReactionDefinition, changeUnits,
				changeKinetics, changeParameterDefinition, changeAnnotation, changeTextualDescription
			 */
		}
		catch (Exception e)
		{
			e.printStackTrace();
			fail ("unexpected exception while diffing cellml models: " + e.getMessage ());
		}
	}
	
	
	
	/**
	 * Test species name differs.
	 */
	@Test
	public void  testSpecChange2 ()
	{
		try
		{
			SBMLDocument doc1 = getValidTestModel ();
			SBMLDocument doc2 = getValidTestModel ();
			doc2.getTreeDocument ().getRoot ().setAttribute ("version", "42");
			doc2 = getModel (XmlTools.prettyPrintDocument (DocumentTools.getDoc (doc2.getTreeDocument ())));
			
			SBMLDiff differ = new SBMLDiff (doc1, doc2);
			differ.mapTrees ();
			checkDiff (differ);

//			System.out.println (differ.getDiff ());
			simpleCheckAnnotations (differ, 0, 0, 1, 0,
				false, false, false, false,
				false, false, false, false,
				false, false, false, false,
				false, false, true, false,
				false, false, false, false,
				false, false, false, false);
			
			/*
				changeSpeciesDef, changeFunctionDefinition, changeEventDefinition, changeRules,
				changeMetaIdentifier, changePerson, changeContributor, changeDate,
				changeCreationDate, changeModificationDate,	changeEntityIdentifier, changeEntityName,
				changeMathModel, changeSpecLevel, changeSpecVersion, changeModelName,
				changeReactionNetwork, changeReactionReversibility, changeReactionDefinition, changeUnits,
				changeKinetics, changeParameterDefinition, changeAnnotation, changeTextualDescription
			 */
		}
		catch (Exception e)
		{
			e.printStackTrace();
			fail ("unexpected exception while diffing cellml models: " + e.getMessage ());
		}
	}
	
	
	
	/**
	 * Test species name differs.
	 */
	@Test
	public void  testChangeModelName ()
	{
		try
		{
			SBMLDocument doc1 = getValidTestModel ();
			SBMLDocument doc2 = getValidTestModel ();
			doc2.getModel ().getDocumentNode ().setAttribute ("name", "TestModel_for_IB2015");
			doc2 = getModel (XmlTools.prettyPrintDocument (DocumentTools.getDoc (doc2.getTreeDocument ())));
			
			SBMLDiff differ = new SBMLDiff (doc1, doc2);
			differ.mapTrees ();
			checkDiff (differ);

//			System.out.println (differ.getDiff ());
			simpleCheckAnnotations (differ, 0, 0, 1, 0,
				false, false, false, false,
				false, false, false, false,
				false, false, false, false,
				false, false, false, true,
				false, false, false, false,
				false, false, false, false);
			
			/*
				changeSpeciesDef, changeFunctionDefinition, changeEventDefinition, changeRules,
				changeMetaIdentifier, changePerson, changeContributor, changeDate,
				changeCreationDate, changeModificationDate,	changeEntityIdentifier, changeEntityName,
				changeMathModel, changeSpecLevel, changeSpecVersion, changeModelName,
				changeReactionNetwork, changeReactionReversibility, changeReactionDefinition, changeUnits,
				changeKinetics, changeParameterDefinition, changeAnnotation, changeTextualDescription
			 */
		}
		catch (Exception e)
		{
			e.printStackTrace();
			fail ("unexpected exception while diffing cellml models: " + e.getMessage ());
		}
	}
	
	
	
	/**
	 * Test species name differs.
	 */
	@Test
	public void  testSpecChange3 ()
	{
		try
		{
			SBMLDocument doc1 = getValidTestModel ();
			SBMLDocument doc2 = getValidTestModel ();
			doc2.getTreeDocument ().getRoot ().setAttribute ("level", "23");
			doc2.getTreeDocument ().getRoot ().setAttribute ("version", "42");
			doc2 = getModel (XmlTools.prettyPrintDocument (DocumentTools.getDoc (doc2.getTreeDocument ())));
			
			SBMLDiff differ = new SBMLDiff (doc1, doc2);
			differ.mapTrees ();
			checkDiff (differ);

//			System.out.println (differ.getDiff ());
			simpleCheckAnnotations (differ, 0, 0, 2, 0,
				false, false, false, false,
				false, false, false, false,
				false, false, false, false,
				false, true, true, false,
				false, false, false, false,
				false, false, false, false);
			
			/*
				changeSpeciesDef, changeFunctionDefinition, changeEventDefinition, changeRules,
				changeMetaIdentifier, changePerson, changeContributor, changeDate,
				changeCreationDate, changeModificationDate,	changeEntityIdentifier, changeEntityName,
				changeMathModel, changeSpecLevel, changeSpecVersion, changeModelName,
				changeReactionNetwork, changeReactionReversibility, changeReactionDefinition, changeUnits,
				changeKinetics, changeParameterDefinition, changeAnnotation, changeTextualDescription
			 */
		}
		catch (Exception e)
		{
			e.printStackTrace();
			fail ("unexpected exception while diffing cellml models: " + e.getMessage ());
		}
	}
	
	
	
	/**
	 * Test species name differs.
	 */
	@Test
	public void  testRuleDel ()
	{
		try
		{
			SBMLDocument doc1 = getValidTestModel ();
			SBMLDocument doc2 = getValidTestModel ();
			((DocumentNode) doc2.getTreeDocument ().getNodeByPath ("/sbml[1]/model[1]")).rmChild ((DocumentNode) doc2.getTreeDocument ().getNodeByPath ("/sbml[1]/model[1]/listOfRules[1]"));
			doc2 = getModel (XmlTools.prettyPrintDocument (DocumentTools.getDoc (doc2.getTreeDocument ())));
			
			SBMLDiff differ = new SBMLDiff (doc1, doc2);
			differ.mapTrees ();
			checkDiff (differ);

//			System.out.println (differ.getDiff ());
			simpleCheckAnnotations (differ, 0, 36, 0, 3,
				false, false, false, true,
				false, false, false, false,
				false, false, false, false,
				false, false, false, false,
				false, false, false, false,
				false, false, false, false);
			
			/*
				changeSpeciesDef, changeFunctionDefinition, changeEventDefinition, changeRules,
				changeMetaIdentifier, changePerson, changeContributor, changeDate,
				changeCreationDate, changeModificationDate,	changeEntityIdentifier, changeEntityName,
				changeMathModel, changeSpecLevel, changeSpecVersion, changeModelName,
				changeReactionNetwork, changeReactionReversibility, changeReactionDefinition, changeUnits,
				changeKinetics, changeParameterDefinition, changeAnnotation, changeTextualDescription
			 */
		}
		catch (Exception e)
		{
			e.printStackTrace();
			fail ("unexpected exception while diffing cellml models: " + e.getMessage ());
		}
	}
	
	
	
	/**
	 * Test species name differs.
	 */
	@Test
	public void  testRuleDiffers ()
	{
		try
		{
			SBMLDocument doc1 = getValidTestModel ();
			SBMLDocument doc2 = getValidTestModel ();
			// update model -> change name of a variable
			((DocumentNode) doc2.getTreeDocument ().getNodeByPath ("/sbml[1]/model[1]/listOfRules[1]/assignmentRule[1]")).setAttribute ("variable", "E");
			doc2 = getModel (XmlTools.prettyPrintDocument (DocumentTools.getDoc (doc2.getTreeDocument ())));
			
			SBMLDiff differ = new SBMLDiff (doc1, doc2);
			differ.mapTrees ();
			checkDiff (differ);

//			System.out.println (differ.getDiff ());
			simpleCheckAnnotations (differ, 0, 0, 1, 0,
				false, false, false, true,
				false, false, false, false,
				false, false, false, false,
				false, false, false, false,
				false, false, false, false,
				false, false, false, false);
			
			/*
				changeSpeciesDef, changeFunctionDefinition, changeEventDefinition, changeRules,
				changeMetaIdentifier, changePerson, changeContributor, changeDate,
				changeCreationDate, changeModificationDate,	changeEntityIdentifier, changeEntityName,
				changeMathModel, changeSpecLevel, changeSpecVersion, changeModelName,
				changeReactionNetwork, changeReactionReversibility, changeReactionDefinition, changeUnits,
				changeKinetics, changeParameterDefinition, changeAnnotation, changeTextualDescription
			 */
		}
		catch (Exception e)
		{
			e.printStackTrace();
			fail ("unexpected exception while diffing cellml models: " + e.getMessage ());
		}
	}
	
	
	
	/**
	 * Test species name differs.
	 */
	@Test
	public void  testEventDel ()
	{
		try
		{
			SBMLDocument doc1 = getValidTestModel ();
			SBMLDocument doc2 = getValidTestModel ();
			((DocumentNode) doc2.getTreeDocument ().getNodeByPath ("/sbml[1]/model[1]")).rmChild ((DocumentNode) doc2.getTreeDocument ().getNodeByPath ("/sbml[1]/model[1]/listOfEvents[1]"));
			doc2 = getModel (XmlTools.prettyPrintDocument (DocumentTools.getDoc (doc2.getTreeDocument ())));
			
			SBMLDiff differ = new SBMLDiff (doc1, doc2);
			differ.mapTrees ();
			checkDiff (differ);

//			System.out.println (differ.getDiff ());
			simpleCheckAnnotations (differ, 0, 19, 0, 4,
				false, false, true, false,
				false, false, false, false,
				false, false, false, false,
				false, false, false, false,
				false, false, false, false,
				false, false, false, false);
			
			/*
				changeSpeciesDef, changeFunctionDefinition, changeEventDefinition, changeRules,
				changeMetaIdentifier, changePerson, changeContributor, changeDate,
				changeCreationDate, changeModificationDate,	changeEntityIdentifier, changeEntityName,
				changeMathModel, changeSpecLevel, changeSpecVersion, changeModelName,
				changeReactionNetwork, changeReactionReversibility, changeReactionDefinition, changeUnits,
				changeKinetics, changeParameterDefinition, changeAnnotation, changeTextualDescription
			 */
		}
		catch (Exception e)
		{
			e.printStackTrace();
			fail ("unexpected exception while diffing cellml models: " + e.getMessage ());
		}
	}
	
	
	
	/**
	 * Test species name differs.
	 */
	@Test
	public void  testEventDiffers ()
	{
		try
		{
			SBMLDocument doc1 = getValidTestModel ();
			SBMLDocument doc2 = getValidTestModel ();
			// update model -> change name of a variable
			((DocumentNode) doc2.getTreeDocument ().getNodeByPath ("/sbml[1]/model[1]/listOfEvents[1]/event[1]/listOfEventAssignments[1]/eventAssignment[1]")).setAttribute ("variable", "B");
			doc2 = getModel (XmlTools.prettyPrintDocument (DocumentTools.getDoc (doc2.getTreeDocument ())));
			
			SBMLDiff differ = new SBMLDiff (doc1, doc2);
			differ.mapTrees ();
			checkDiff (differ);

//			System.out.println (differ.getDiff ());
			simpleCheckAnnotations (differ, 0, 0, 1, 0,
				false, false, true, false,
				false, false, false, false,
				false, false, false, false,
				false, false, false, false,
				false, false, false, false,
				false, false, false, false);
			
			/*
				changeSpeciesDef, changeFunctionDefinition, changeEventDefinition, changeRules,
				changeMetaIdentifier, changePerson, changeContributor, changeDate,
				changeCreationDate, changeModificationDate,	changeEntityIdentifier, changeEntityName,
				changeMathModel, changeSpecLevel, changeSpecVersion, changeModelName,
				changeReactionNetwork, changeReactionReversibility, changeReactionDefinition, changeUnits,
				changeKinetics, changeParameterDefinition, changeAnnotation, changeTextualDescription
			 */
		}
		catch (Exception e)
		{
			e.printStackTrace();
			fail ("unexpected exception while diffing cellml models: " + e.getMessage ());
		}
	}
	
	
	
	/**
	 * Test species name differs.
	 */
	@Test
	public void  testFunctionDel ()
	{
		try
		{
			SBMLDocument doc1 = getValidTestModel ();
			SBMLDocument doc2 = getValidTestModel ();
			((DocumentNode) doc2.getTreeDocument ().getNodeByPath ("/sbml[1]/model[1]")).rmChild ((DocumentNode) doc2.getTreeDocument ().getNodeByPath ("/sbml[1]/model[1]/listOfFunctionDefinitions[1]"));
			doc2 = getModel (XmlTools.prettyPrintDocument (DocumentTools.getDoc (doc2.getTreeDocument ())));
			
			SBMLDiff differ = new SBMLDiff (doc1, doc2);
			differ.mapTrees ();
			checkDiff (differ);

//			System.out.println (differ.getDiff ());
			simpleCheckAnnotations (differ, 0, 15, 0, 5,
				false, true, false, false,
				false, false, false, false,
				false, false, true, false,
				false, false, false, false,
				false, false, false, false,
				false, false, false, false);
			
			/*
				changeSpeciesDef, changeFunctionDefinition, changeEventDefinition, changeRules,
				changeMetaIdentifier, changePerson, changeContributor, changeDate,
				changeCreationDate, changeModificationDate,	changeEntityIdentifier, changeEntityName,
				changeMathModel, changeSpecLevel, changeSpecVersion, changeModelName,
				changeReactionNetwork, changeReactionReversibility, changeReactionDefinition, changeUnits,
				changeKinetics, changeParameterDefinition, changeAnnotation, changeTextualDescription
			 */
		}
		catch (Exception e)
		{
			e.printStackTrace();
			fail ("unexpected exception while diffing cellml models: " + e.getMessage ());
		}
	}
	
	
	
	/**
	 * Test species name differs.
	 */
	@Test
	public void  testFunctionDiffers ()
	{
		try
		{
			SBMLDocument doc1 = getValidTestModel ();
			SBMLDocument doc2 = getValidTestModel ();
			// update model -> change name of a variable
			((TextNode) doc2.getTreeDocument ().getNodeByPath ("/sbml[1]/model[1]/listOfFunctionDefinitions[1]/functionDefinition[1]/math[1]/lambda[1]/apply[1]/cn[1]/text()[1]")).setText ("5");
			doc2 = getModel (XmlTools.prettyPrintDocument (DocumentTools.getDoc (doc2.getTreeDocument ())));
			
			SBMLDiff differ = new SBMLDiff (doc1, doc2);
			differ.mapTrees ();
			checkDiff (differ);

//			System.out.println (differ.getDiff ());
			simpleCheckAnnotations (differ, 1, 1, 0, 0,
				false, true, false, false,
				false, false, false, false,
				false, false, false, false,
				false, false, false, false,
				false, false, false, false,
				false, false, false, false);
			
			/*
				changeSpeciesDef, changeFunctionDefinition, changeEventDefinition, changeRules,
				changeMetaIdentifier, changePerson, changeContributor, changeDate,
				changeCreationDate, changeModificationDate,	changeEntityIdentifier, changeEntityName,
				changeMathModel, changeSpecLevel, changeSpecVersion, changeModelName,
				changeReactionNetwork, changeReactionReversibility, changeReactionDefinition, changeUnits,
				changeKinetics, changeParameterDefinition, changeAnnotation, changeTextualDescription
			 */
		}
		catch (Exception e)
		{
			e.printStackTrace();
			fail ("unexpected exception while diffing cellml models: " + e.getMessage ());
		}
	}
	
	
	
	/**
	 * Test species name differs.
	 */
	@Test
	public void  testSpeciesDel ()
	{
		try
		{
			SBMLDocument doc1 = getValidTestModel ();
			SBMLDocument doc2 = getValidTestModel ();
			// update model -> change name of a variable
			((DocumentNode) doc2.getTreeDocument ().getNodeByPath ("/sbml[1]/model[1]/listOfSpecies[1]")).rmChild ((DocumentNode) doc2.getTreeDocument ().getNodeByPath ("/sbml[1]/model[1]/listOfSpecies[1]/species[5]"));
			doc2 = getModel (XmlTools.prettyPrintDocument (DocumentTools.getDoc (doc2.getTreeDocument ())));
			
			SBMLDiff differ = new SBMLDiff (doc1, doc2);
			differ.mapTrees ();
			checkDiff (differ);

//			System.out.println (differ.getDiff ());
			simpleCheckAnnotations (differ, 0, 5, 0, 1,
				true, false, false, false,
				false, false, false, false,
				false, false, true, true,
				false, false, false, false,
				false, false, false, false,
				false, false, false, false);
			
			/*
				changeSpeciesDef, changeFunctionDefinition, changeEventDefinition, changeRules,
				changeMetaIdentifier, changePerson, changeContributor, changeDate,
				changeCreationDate, changeModificationDate,	changeEntityIdentifier, changeEntityName,
				changeMathModel, changeSpecLevel, changeSpecVersion, changeModelName,
				changeReactionNetwork, changeReactionReversibility, changeReactionDefinition, changeUnits,
				changeKinetics, changeParameterDefinition, changeAnnotation, changeTextualDescription
			 */
		}
		catch (Exception e)
		{
			e.printStackTrace();
			fail ("unexpected exception while diffing cellml models: " + e.getMessage ());
		}
	}
	
	
	
	/**
	 * Test species name differs.
	 */
	@Test
	public void  testSpeciesNameDiffers ()
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
			simpleCheckAnnotations (differ, 0, 0, 1, 0,
				false, false, false, false,
				false, false, false, false,
				false, false, false, true,
				false, false, false, false,
				false, false, false, false,
				false, false, false, false);
			
			/*
				changeSpeciesDef, changeFunctionDefinition, changeEventDefinition, changeRules,
				changeMetaIdentifier, changePerson, changeContributor, changeDate,
				changeCreationDate, changeModificationDate,	changeEntityIdentifier, changeEntityName,
				changeMathModel, changeSpecLevel, changeSpecVersion, changeModelName,
				changeReactionNetwork, changeReactionReversibility, changeReactionDefinition, changeUnits,
				changeKinetics, changeParameterDefinition, changeAnnotation, changeTextualDescription
			 */
		}
		catch (Exception e)
		{
			e.printStackTrace();
			fail ("unexpected exception while diffing cellml models: " + e.getMessage ());
		}
	}
	
	
	
	/**
	 * Test species name differs.
	 */
	@Test
	public void  testSpeciesIdDiffers ()
	{
		try
		{
			SBMLDocument doc1 = getValidTestModel ();
			SBMLDocument doc2 = getValidTestModel ();
			// update model -> change name of a variable
			doc2.getTreeDocument ().getNodeById ("E").setAttribute ("id", "e");
			doc2 = getModel (XmlTools.prettyPrintDocument (DocumentTools.getDoc (doc2.getTreeDocument ())));
			
			SBMLDiff differ = new SBMLDiff (doc1, doc2);
			differ.mapTrees ();
			checkDiff (differ);

//			System.out.println (differ.getDiff ());
			simpleCheckAnnotations (differ, 0, 0, 1, 0,
				false, false, false, false,
				false, false, false, false,
				false, false, true, false,
				false, false, false, false,
				false, false, false, false,
				false, false, false, false);
			
			/*
				changeSpeciesDef, changeFunctionDefinition, changeEventDefinition, changeRules,
				changeMetaIdentifier, changePerson, changeContributor, changeDate,
				changeCreationDate, changeModificationDate,	changeEntityIdentifier, changeEntityName,
				changeMathModel, changeSpecLevel, changeSpecVersion, changeModelName,
				changeReactionNetwork, changeReactionReversibility, changeReactionDefinition, changeUnits,
				changeKinetics, changeParameterDefinition, changeAnnotation, changeTextualDescription
			 */
		}
		catch (Exception e)
		{
			e.printStackTrace();
			fail ("unexpected exception while diffing cellml models: " + e.getMessage ());
		}
	}
	
	
	
	/**
	 * Test species name differs.
	 */
	@Test
	public void  testSpeciesMathDiffers ()
	{
		try
		{
			SBMLDocument doc1 = getValidTestModel ();
			SBMLDocument doc2 = getValidTestModel ();
			// update model -> change name of a variable
			doc2.getTreeDocument ().getNodeById ("E").setAttribute ("initialConcentration", "23");
			doc2 = getModel (XmlTools.prettyPrintDocument (DocumentTools.getDoc (doc2.getTreeDocument ())));
			
			SBMLDiff differ = new SBMLDiff (doc1, doc2);
			differ.mapTrees ();
			checkDiff (differ);

//			System.out.println (differ.getDiff ());
			simpleCheckAnnotations (differ, 0, 0, 1, 0,
				true, false, false, false,
				false, false, false, false,
				false, false, false, false,
				false, false, false, false,
				false, false, false, false,
				false, false, false, false);
			
			/*
				changeSpeciesDef, changeFunctionDefinition, changeEventDefinition, changeRules,
				changeMetaIdentifier, changePerson, changeContributor, changeDate,
				changeCreationDate, changeModificationDate,	changeEntityIdentifier, changeEntityName,
				changeMathModel, changeSpecLevel, changeSpecVersion, changeModelName,
				changeReactionNetwork, changeReactionReversibility, changeReactionDefinition, changeUnits,
				changeKinetics, changeParameterDefinition, changeAnnotation, changeTextualDescription
			 */
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
			fail ("unexpected exception while diffing cellml models: " + e.getMessage ());
		}
	}
	
	
	
	/**
	 * Test species name differs.
	 */
	@Test
	public void  testSpeciesMathDiffers2 ()
	{
		try
		{
			SBMLDocument doc1 = getValidTestModel ();
			SBMLDocument doc2 = getValidTestModel ();
			// update model -> change name of a variable
			doc2.getTreeDocument ().getNodeById ("E").rmAttribute ("initialConcentration");
			doc2.getTreeDocument ().getNodeById ("E").setAttribute ("initialAmount", "23");
			doc2 = getModel (XmlTools.prettyPrintDocument (DocumentTools.getDoc (doc2.getTreeDocument ())));
			
			SBMLDiff differ = new SBMLDiff (doc1, doc2);
			differ.mapTrees ();
			checkDiff (differ);

//			System.out.println (differ.getDiff ());
			simpleCheckAnnotations (differ, 1, 1, 0, 0,
				true, false, false, false,
				false, false, false, false,
				false, false, false, false,
				false, false, false, false,
				false, false, false, false,
				false, false, false, false);
			
			/*
				changeSpeciesDef, changeFunctionDefinition, changeEventDefinition, changeRules,
				changeMetaIdentifier, changePerson, changeContributor, changeDate,
				changeCreationDate, changeModificationDate,	changeEntityIdentifier, changeEntityName,
				changeMathModel, changeSpecLevel, changeSpecVersion, changeModelName,
				changeReactionNetwork, changeReactionReversibility, changeReactionDefinition, changeUnits,
				changeKinetics, changeParameterDefinition, changeAnnotation, changeTextualDescription
			 */
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
			fail ("unexpected exception while diffing cellml models: " + e.getMessage ());
		}
	}
	
	
	
	/**
	 * Test models equal.
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

			simpleCheckAnnotations (differ, 0, 0, 0, 0, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false);
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
	 * @param changeSpeciesDef the change species def
	 * @param changeFunctionDefinition the change function definition
	 * @param changeEventDefinition the change event definition
	 * @param changeRules the change rules
	 * @param changeMetaIdentifier the change meta identifier
	 * @param changePerson the change person
	 * @param changeContributor the change contributor
	 * @param changeDate the change date
	 * @param changeCreationDate the change creation date
	 * @param changeModificationDate the change modification date
	 * @param changeEntityIdentifier the change entity identifier
	 * @param changeEntityName the change entity name
	 * @param changeMathModel the change math model
	 * @param changeSpecLevel the change spec level
	 * @param changeSpecVersion the change spec version
	 * @param changeModelName the change model name
	 * @param changeReactionNetwork the change reaction network
	 * @param changeReactionReversibility the change reaction reversibility
	 * @param changeReactionDefinition the change reaction definition
	 * @param changeUnits the change units
	 * @param changeKinetics the change kinetics
	 * @param changeParameterDefinition the change component hierarchy
	 * @param changeAnnotation the change annotation
	 * @param changeTextualDescription the change textual description
	 * @throws XmlDocumentParseException the xml document parse exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws JDOMException the jDOM exception
	 */
	private void simpleCheckAnnotations (
		SBMLDiff differ,
		int ins, int del, int up, int mov,
		boolean changeSpeciesDef, boolean changeFunctionDefinition, boolean changeEventDefinition, boolean changeRules,
		boolean changeMetaIdentifier, boolean changePerson, boolean changeContributor, boolean changeDate,
		boolean changeCreationDate, boolean changeModificationDate,	boolean changeEntityIdentifier, boolean changeEntityName,
		boolean changeMathModel, boolean changeSpecLevel, boolean changeSpecVersion, boolean changeModelName,
		boolean changeReactionNetwork, boolean changeReactionReversibility, boolean changeReactionDefinition, boolean changeUnits,
		boolean changeKinetics, boolean changeParameterDefinition, boolean changeAnnotation, boolean changeTextualDescription
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
		assertEquals (pre + "occurence of http://purl.org/net/comodi#SpeciesDefinition", changeSpeciesDef, annotations.contains ("http://purl.org/net/comodi#SpeciesDefinition"));
		assertEquals (pre + "occurence of http://purl.org/net/comodi#EntityName", changeEntityName, annotations.contains ("http://purl.org/net/comodi#EntityName"));
		assertEquals (pre + "occurence of http://purl.org/net/comodi#MathematicalModel", changeMathModel, annotations.contains ("http://purl.org/net/comodi#MathematicalModel"));
		assertEquals (pre + "occurence of http://purl.org/net/comodi#SbmlLevel", changeSpecLevel, annotations.contains ("http://purl.org/net/comodi#SbmlLevel"));
		assertEquals (pre + "occurence of http://purl.org/net/comodi#SbmlVersion", changeSpecVersion, annotations.contains ("http://purl.org/net/comodi#SbmlVersion"));
		assertEquals (pre + "occurence of http://purl.org/net/comodi#ModelName", changeModelName, annotations.contains ("http://purl.org/net/comodi#ModelName"));
		assertEquals (pre + "occurence of http://purl.org/net/comodi#EntityIdentifier", changeEntityIdentifier, annotations.contains ("http://purl.org/net/comodi#EntityIdentifier"));
		assertEquals (pre + "occurence of http://purl.org/net/comodi#ReactionNetwork", changeReactionNetwork, annotations.contains ("http://purl.org/net/comodi#ReactionNetwork"));
		assertEquals (pre + "occurence of http://purl.org/net/comodi#ReactionReversibility", changeReactionReversibility, annotations.contains ("http://purl.org/net/comodi#ReactionReversibility"));
		assertEquals (pre + "occurence of http://purl.org/net/comodi#UnitDefinition", changeUnits, annotations.contains ("http://purl.org/net/comodi#UnitDefinition"));
		assertEquals (pre + "occurence of http://purl.org/net/comodi#Kinetics", changeKinetics, annotations.contains ("http://purl.org/net/comodi#Kinetics"));
		assertEquals (pre + "occurence of http://purl.org/net/comodi#ParameterDefinition", changeParameterDefinition, annotations.contains ("http://purl.org/net/comodi#ParameterDefinition"));
		assertEquals (pre + "occurence of http://purl.org/net/comodi#Annotation", changeAnnotation, annotations.contains ("http://purl.org/net/comodi#Annotation"));
		assertEquals (pre + "occurence of http://purl.org/net/comodi#TextualDescription", changeTextualDescription, annotations.contains ("http://purl.org/net/comodi#TextualDescription"));
		assertEquals (pre + "occurence of http://purl.org/net/comodi#FunctionDefinition", changeFunctionDefinition, annotations.contains ("http://purl.org/net/comodi#FunctionDefinition"));
		assertEquals (pre + "occurence of http://purl.org/net/comodi#EventDefinition", changeEventDefinition, annotations.contains ("http://purl.org/net/comodi#EventDefinition"));
		assertEquals (pre + "occurence of http://purl.org/net/comodi#Rules", changeRules, annotations.contains ("http://purl.org/net/comodi#Rules"));
		assertEquals (pre + "occurence of http://purl.org/net/comodi#ReactionDefinition", changeReactionDefinition, annotations.contains ("http://purl.org/net/comodi#ReactionDefinition"));
		assertEquals (pre + "occurence of http://purl.org/net/comodi#MetaIdentifier", changeMetaIdentifier, annotations.contains ("http://purl.org/net/comodi#MetaIdentifier"));
		assertEquals (pre + "occurence of http://purl.org/net/comodi#Person", changePerson, annotations.contains ("http://purl.org/net/comodi#Person"));
		assertEquals (pre + "occurence of http://purl.org/net/comodi#Contributor", changeContributor, annotations.contains ("http://purl.org/net/comodi#Contributor"));
		assertEquals (pre + "occurence of http://purl.org/net/comodi#Date", changeDate, annotations.contains ("http://purl.org/net/comodi#Date"));
		assertEquals (pre + "occurence of http://purl.org/net/comodi#CreationDate", changeCreationDate, annotations.contains ("http://purl.org/net/comodi#CreationDate"));
		assertEquals (pre + "occurence of http://purl.org/net/comodi#ModificationDate", changeModificationDate, annotations.contains ("http://purl.org/net/comodi#ModificationDate"));
//		assertEquals (pre + "occurence of http://purl.org/net/comodi#", change, annotations.contains ("http://purl.org/net/comodi#"));
//	assertEquals (pre + "occurence of http://purl.org/net/comodi#", , annotations.contains ("http://purl.org/net/comodi#"));
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
