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
import de.unirostock.sems.xmlutils.exception.XmlDocumentParseException;

/**
 * The Class SBMLSingle to study single SBML documents.
 *
 * @author Martin Scharm
 */
public class SBMLSingle extends Single
{
	
	/** The doc. */
	protected SBMLDocument doc;

	/** The graph producer. */
	protected SBMLGraphProducer graphProducer;
	
	/**
	 * Instantiates a new object to study single SBML documents.
	 *
	 * @param file
	 *          the file containing the model
	 * @throws XmlDocumentParseException the xml document parse exception
	 * @throws FileNotFoundException the file not found exception
	 * @throws ParserConfigurationException the parser configuration exception
	 * @throws SAXException the sAX exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws BivesDocumentConsistencyException the bives document consistency exception
	 * @throws BivesSBMLParseException the bives sbml parse exception
	 */
	public SBMLSingle (File file) throws XmlDocumentParseException, FileNotFoundException, ParserConfigurationException, SAXException, IOException, BivesDocumentConsistencyException, BivesSBMLParseException
	{
		super (file);
		doc = new SBMLDocument (tree);
	}
	
	/**
	 * Instantiates a new object to study single SBML documents.
	 *
	 * @param xml
	 *          the encoded the model
	 * @throws XmlDocumentParseException the xml document parse exception
	 * @throws FileNotFoundException the file not found exception
	 * @throws ParserConfigurationException the parser configuration exception
	 * @throws SAXException the sAX exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws BivesDocumentConsistencyException the bives document consistency exception
	 * @throws BivesSBMLParseException the bives sbml parse exception
	 */
	public SBMLSingle (String xml) throws XmlDocumentParseException, FileNotFoundException, ParserConfigurationException, SAXException, IOException, BivesDocumentConsistencyException, BivesSBMLParseException
	{
		super (xml);
		doc = new SBMLDocument (tree);
	}
	
	/**
	 * Instantiates a new object to study single SBML documents.
	 *
	 * @param doc
	 *          the document
	 * @throws XmlDocumentParseException the xml document parse exception
	 * @throws FileNotFoundException the file not found exception
	 * @throws ParserConfigurationException the parser configuration exception
	 * @throws SAXException the sAX exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws BivesDocumentConsistencyException the bives document consistency exception
	 */
	public SBMLSingle (SBMLDocument doc) throws XmlDocumentParseException, FileNotFoundException, ParserConfigurationException, SAXException, IOException, BivesDocumentConsistencyException
	{
		super (doc.getTreeDocument ());
		this.doc = doc;
	}


	/* (non-Javadoc)
	 * @see de.unirostock.sems.bives.api.Single#getCRNGraphML()
	 */
	@Override
	public String getCRNGraphML() throws ParserConfigurationException {
		if (graphProducer == null)
			graphProducer = new SBMLGraphProducer (doc);
		return new GraphTranslatorGraphML ().translate (graphProducer.getCRN ());
	}

	/* (non-Javadoc)
	 * @see de.unirostock.sems.bives.api.Single#getCRNGraph(de.unirostock.sems.bives.ds.graph.GraphTranslator)
	 */
	@Override
	public Object getCRNGraph (GraphTranslator gt) throws Exception
	{
		if (graphProducer == null)
			graphProducer = new SBMLGraphProducer (doc);
		return gt.translate (graphProducer.getCRN ());
	}


	/* (non-Javadoc)
	 * @see de.unirostock.sems.bives.api.Single#getCRNDotGraph()
	 */
	@Override
	public String getCRNDotGraph ()
	{
		if (graphProducer == null)
			graphProducer = new SBMLGraphProducer (doc);
		return new GraphTranslatorDot ().translate (graphProducer.getCRN ());
	}

	/* (non-Javadoc)
	 * @see de.unirostock.sems.bives.api.Single#getCRNJsonGraph()
	 */
	@Override
	public String getCRNJsonGraph ()
	{
		if (graphProducer == null)
			graphProducer = new SBMLGraphProducer (doc);
		return new GraphTranslatorJson ().translate (graphProducer.getCRN ());
	}

	/* (non-Javadoc)
	 * @see de.unirostock.sems.bives.api.Single#getHierarchyGraph(de.unirostock.sems.bives.ds.graph.GraphTranslator)
	 */
	@Override
	public Object getHierarchyGraph (GraphTranslator gt)
	{
		return null;
	}

	/* (non-Javadoc)
	 * @see de.unirostock.sems.bives.api.Single#getHierarchyGraphML()
	 */
	@Override
	public String getHierarchyGraphML () throws ParserConfigurationException
	{
		return null;
	}

	/* (non-Javadoc)
	 * @see de.unirostock.sems.bives.api.Single#getHierarchyDotGraph()
	 */
	@Override
	public String getHierarchyDotGraph ()
	{
		return null;
	}

	/* (non-Javadoc)
	 * @see de.unirostock.sems.bives.api.Single#getHierarchyJsonGraph()
	 */
	@Override
	public String getHierarchyJsonGraph ()
	{
		return null;
	}

}
