/**
 * 
 */
package de.unirostock.sems.bives.sbml.api;

import java.io.File;
import java.io.IOException;

import org.jdom2.JDOMException;

import de.unirostock.sems.bives.api.Single;
import de.unirostock.sems.bives.ds.graph.GraphTranslator;
import de.unirostock.sems.bives.ds.graph.GraphTranslatorDot;
import de.unirostock.sems.bives.ds.graph.GraphTranslatorGraphML;
import de.unirostock.sems.bives.ds.graph.GraphTranslatorJson;
import de.unirostock.sems.bives.exception.BivesDocumentConsistencyException;
import de.unirostock.sems.bives.exception.BivesFlattenException;
import de.unirostock.sems.bives.exception.BivesUnsupportedException;
import de.unirostock.sems.bives.sbml.algorithm.SBMLGraphProducer;
import de.unirostock.sems.bives.sbml.exception.BivesSBMLParseException;
import de.unirostock.sems.bives.sbml.parser.SBMLDocument;
import de.unirostock.sems.xmlutils.ds.TreeDocument;
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
	 * @param file the file containing the model
	 * @throws XmlDocumentParseException the xml document parse exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws JDOMException the jDOM exception
	 * @throws BivesSBMLParseException the bives sbml parse exception
	 * @throws BivesDocumentConsistencyException the bives document consistency exception
	 */
	public SBMLSingle (File file) throws XmlDocumentParseException, IOException, JDOMException, BivesSBMLParseException, BivesDocumentConsistencyException
	{
		super (file);
		doc = new SBMLDocument (tree);
	}
	
	/**
	 * Instantiates a new object to study single SBML documents.
	 *
	 * @param xml the encoded the model
	 * @throws XmlDocumentParseException the xml document parse exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws JDOMException the jDOM exception
	 * @throws BivesSBMLParseException the bives sbml parse exception
	 * @throws BivesDocumentConsistencyException the bives document consistency exception
	 */
	public SBMLSingle (String xml) throws XmlDocumentParseException, IOException, JDOMException, BivesSBMLParseException, BivesDocumentConsistencyException
	{
		super (xml);
		doc = new SBMLDocument (tree);
	}
	
	
	/**
	 * Instantiates a new object.
	 *
	 * @param td the tree document
	 * @throws XmlDocumentParseException the xml document parse exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws JDOMException the jDOM exception
	 * @throws BivesCellMLParseException the bives cell ml parse exception
	 * @throws BivesDocumentConsistencyException the bives document consistency exception
	 * @throws BivesLogicalException the bives logical exception
	 * @throws BivesImportException the bives import exception
	 * @throws URISyntaxException the uRI syntax exception
	 */
	public SBMLSingle (TreeDocument td) throws XmlDocumentParseException, IOException, JDOMException, BivesSBMLParseException, BivesDocumentConsistencyException
	{
		super (td);
		doc = new SBMLDocument (tree);
	}
	
	/**
	 * Instantiates a new object to study single SBML documents.
	 *
	 * @param doc
	 *          the document
	 */
	public SBMLSingle (SBMLDocument doc)
	{
		super (doc.getTreeDocument ());
		this.doc = doc;
	}


	/* (non-Javadoc)
	 * @see de.unirostock.sems.bives.api.Single#getCRNGraphML()
	 */
	@Override
	public String getReactionsGraphML()
	{
		if (graphProducer == null)
			graphProducer = new SBMLGraphProducer (doc);
		return new GraphTranslatorGraphML ().translate (graphProducer.getCRN ());
	}

	/* (non-Javadoc)
	 * @see de.unirostock.sems.bives.api.Single#getCRNGraph(de.unirostock.sems.bives.ds.graph.GraphTranslator)
	 */
	@Override
	public Object getReactionsGraph (GraphTranslator gt) throws Exception
	{
		if (graphProducer == null)
			graphProducer = new SBMLGraphProducer (doc);
		return gt.translate (graphProducer.getCRN ());
	}


	/* (non-Javadoc)
	 * @see de.unirostock.sems.bives.api.Single#getCRNDotGraph()
	 */
	@Override
	public String getReactionsDotGraph ()
	{
		if (graphProducer == null)
			graphProducer = new SBMLGraphProducer (doc);
		return new GraphTranslatorDot ().translate (graphProducer.getCRN ());
	}

	/* (non-Javadoc)
	 * @see de.unirostock.sems.bives.api.Single#getCRNJsonGraph()
	 */
	@Override
	public String getReactionsJsonGraph ()
	{
		if (graphProducer == null)
			graphProducer = new SBMLGraphProducer (doc);
		return new GraphTranslatorJson ().translate (graphProducer.getCRN ());
	}

	/* (non-Javadoc)
	 * @see de.unirostock.sems.bives.api.Single#getHierarchyGraph(de.unirostock.sems.bives.ds.graph.GraphTranslator)
	 */
	@Override
	public Object getHierarchyGraph (GraphTranslator gt) throws BivesUnsupportedException
	{
		throw new BivesUnsupportedException ("there is no hierarchy graph for SBML models");
	}

	/* (non-Javadoc)
	 * @see de.unirostock.sems.bives.api.Single#getHierarchyGraphML()
	 */
	@Override
	public String getHierarchyGraphML () throws BivesUnsupportedException
	{
		throw new BivesUnsupportedException ("there is no hierarchy graph for SBML models");
	}

	/* (non-Javadoc)
	 * @see de.unirostock.sems.bives.api.Single#getHierarchyDotGraph()
	 */
	@Override
	public String getHierarchyDotGraph () throws BivesUnsupportedException
	{
		throw new BivesUnsupportedException ("there is no hierarchy graph for SBML models");
	}

	/* (non-Javadoc)
	 * @see de.unirostock.sems.bives.api.Single#getHierarchyJsonGraph()
	 */
	@Override
	public String getHierarchyJsonGraph () throws BivesUnsupportedException
	{
		throw new BivesUnsupportedException ("there is no hierarchy graph for SBML models");
	}

	/* (non-Javadoc)
	 * @see de.unirostock.sems.bives.api.Single#flatten()
	 */
	@Override
	public String flatten () throws Exception
	{
		throw new BivesFlattenException ("flattening of SBML not supported yet.");
	}

}
