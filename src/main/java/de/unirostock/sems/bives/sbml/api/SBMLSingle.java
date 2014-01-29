/**
 * 
 */
package de.unirostock.sems.bives.sbml.api;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import de.unirostock.sems.bives.api.Single;
import de.unirostock.sems.bives.ds.graph.GraphTranslator;
import de.unirostock.sems.bives.ds.graph.GraphTranslatorDot;
import de.unirostock.sems.bives.ds.graph.GraphTranslatorGraphML;
import de.unirostock.sems.bives.ds.graph.GraphTranslatorJson;
import de.unirostock.sems.bives.exception.BivesDocumentConsistencyException;
import de.unirostock.sems.bives.sbml.algorithm.SBMLGraphProducer;
import de.unirostock.sems.bives.sbml.exception.BivesSBMLParseException;
import de.unirostock.sems.bives.sbml.parser.SBMLDocument;
import de.unirostock.sems.xmltools.exception.XmlDocumentParseException;

/**
 * @author Martin Scharm
 *
 */
public class SBMLSingle extends Single
{
	protected SBMLDocument doc1;
	
	public SBMLSingle (File a) throws XmlDocumentParseException, FileNotFoundException, ParserConfigurationException, SAXException, IOException, BivesDocumentConsistencyException, BivesSBMLParseException
	{
		super (a);
		doc1 = new SBMLDocument (treeA);
	}
	
	public SBMLSingle (String a) throws XmlDocumentParseException, FileNotFoundException, ParserConfigurationException, SAXException, IOException, BivesDocumentConsistencyException, BivesSBMLParseException
	{
		super (a);
		doc1 = new SBMLDocument (treeA);
	}
	
	public SBMLSingle (SBMLDocument a) throws XmlDocumentParseException, FileNotFoundException, ParserConfigurationException, SAXException, IOException, BivesDocumentConsistencyException
	{
		super (a.getTreeDocument ());
		doc1 = a;
	}



	protected SBMLGraphProducer graphProducer;


	@Override
	public String getCRNGraphML() throws ParserConfigurationException {
		if (graphProducer == null)
			graphProducer = new SBMLGraphProducer (doc1);
		return new GraphTranslatorGraphML ().translate (graphProducer.getCRN ());
	}

	@Override
	public Object getCRNGraph (GraphTranslator gt) throws Exception
	{
		if (graphProducer == null)
			graphProducer = new SBMLGraphProducer (doc1);
		return gt.translate (graphProducer.getCRN ());
	}


	@Override
	public String getCRNDotGraph ()
	{
		if (graphProducer == null)
			graphProducer = new SBMLGraphProducer (doc1);
		return new GraphTranslatorDot ().translate (graphProducer.getCRN ());
	}

	@Override
	public String getCRNJsonGraph ()
	{
		if (graphProducer == null)
			graphProducer = new SBMLGraphProducer (doc1);
		return new GraphTranslatorJson ().translate (graphProducer.getCRN ());
	}

	@Override
	public Object getHierarchyGraph (GraphTranslator gt)
	{
		return null;
	}

	@Override
	public String getHierarchyGraphML () throws ParserConfigurationException
	{
		return null;
	}

	@Override
	public String getHierarchyDotGraph ()
	{
		return null;
	}

	@Override
	public String getHierarchyJsonGraph ()
	{
		return null;
	}

}
