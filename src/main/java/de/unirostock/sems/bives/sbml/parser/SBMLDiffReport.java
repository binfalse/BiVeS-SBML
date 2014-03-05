/**
 * 
 */
package de.unirostock.sems.bives.sbml.parser;


import java.util.ArrayList;
import java.util.List;


/**
 * The Class SBMLDiffReport creates a report about the differences between two versions of an SBML document.
 *
 * @author Martin Scharm
 */
public class SBMLDiffReport
{
	
	/** The header. */
	private String header;
	
	/** The modified species. */
	private List<String>	modifiedSpecies;
	
	/** The modified species types. */
	private List<String>	modifiedSpeciesTypes;
	
	/** The modified parameter. */
	private List<String>	modifiedParameter;
	
	/** The modified reactions. */
	private List<String>	modifiedReactions;
	
	/** The modified compartments. */
	private List<String>	modifiedCompartments;
	
	/** The modified compartment types. */
	private List<String>	modifiedCompartmentTypes;
	
	/** The modified rules. */
	private List<String>	modifiedRules;
	
	/** The modified functions. */
	private List<String>	modifiedFunctions;
	
	/** The modified constraints. */
	private List<String>	modifiedConstraints;
	
	/** The modified events. */
	private List<String>	modifiedEvents;
	
	/** The modified initial assignments. */
	private List<String>	modifiedInitialAssignments;
	
	/** The modified units. */
	private List<String>	modifiedUnits;
	
	/**
	 * Instantiates a new sBML diff report.
	 */
	public SBMLDiffReport ()
	{
		header = "";
		modifiedSpecies = new ArrayList<String> ();
		modifiedSpeciesTypes = new ArrayList<String> ();
		modifiedParameter = new ArrayList<String> ();
		modifiedReactions = new ArrayList<String> ();
		modifiedCompartments = new ArrayList<String> ();
		modifiedCompartmentTypes = new ArrayList<String> ();
		modifiedRules = new ArrayList<String> ();
		modifiedFunctions = new ArrayList<String> ();
		modifiedConstraints = new ArrayList<String> ();
		modifiedEvents = new ArrayList<String> ();
		modifiedInitialAssignments = new ArrayList<String> ();
		modifiedUnits = new ArrayList<String> ();
	}
	
	/**
	 * Generate html report.
	 *
	 * @return the string
	 */
	public String generateHTMLReport ()
	{
		String report = "<h2>Diff Report</h2><p>Please keep in mind that these modifications are choosen by what we think is important!</p><p>"+header+"</p>";
		
		report += generateTable (modifiedSpecies, "Following Species were modified", "species");
		report += generateTable (modifiedSpeciesTypes, "Following SpeciesTypes were modified", "speciestypes");
		report += generateTable (modifiedParameter, "Following Parameter were modified", "parameter");
		report += generateTable (modifiedReactions, "Following Reactions were modified", "reaction");
		report += generateTable (modifiedCompartments, "Following Compartments were modified", "compartment");
		report += generateTable (modifiedCompartmentTypes, "Following CompartmentTypes were modified", "compartmenttype");
		report += generateTable (modifiedUnits, "Following Units were modified", "unit");
		report += generateTable (modifiedRules, "Following Rules were modified", "rule");
		report += generateTable (modifiedFunctions, "Following Functions were modified", "function");
		report += generateTable (modifiedConstraints, "Following Constraints were modified", "constraint");
		report += generateTable (modifiedEvents, "Following Events were modified", "event");
		report += generateTable (modifiedInitialAssignments, "Following InitialAssignments were modified", "initialssignment");
		
		return report;
	}
	
	/**
	 * Generate table.
	 *
	 * @param mods the mods
	 * @param headline the headline
	 * @param cssclass the cssclass
	 * @return the string
	 */
	private String generateTable (List<String> mods, String headline, String cssclass)
	{
		if (mods.size () < 1)
			return "";
		
		String rep = "";

		for (String mod: mods)
			rep += mod;
		
		if (rep.length () < 1)
			return "";
		
		return "<h3 class='"+cssclass+"'>"+headline+"</h3><table class='"+cssclass+"'>" +
			"<thead><th>ID (name)</th><th>Modification</th></thead>" + rep + "</table>";
	}
	
	/**
	 * Adds the header.
	 *
	 * @param header the header
	 */
	public void addHeader (String header)
	{
		this.header += header;
	}
	
	/**
	 * Modify species.
	 *
	 * @param rep the rep
	 */
	public void modifySpecies (String rep)
	{
		modifiedSpecies.add (rep);
	}
	
	/**
	 * Modify species types.
	 *
	 * @param rep the rep
	 */
	public void modifySpeciesTypes (String rep)
	{
		modifiedSpeciesTypes.add (rep);
	}
	
	/**
	 * Modify parameter.
	 *
	 * @param rep the rep
	 */
	public void modifyParameter (String rep)
	{
		modifiedParameter.add (rep);
	}
	
	/**
	 * Modify reaction.
	 *
	 * @param rep the rep
	 */
	public void modifyReaction (String rep)
	{
		modifiedReactions.add (rep);
	}
	
	/**
	 * Modify compartments.
	 *
	 * @param rep the rep
	 */
	public void modifyCompartments (String rep)
	{
		modifiedCompartments.add (rep);
	}
	
	/**
	 * Modify compartment types.
	 *
	 * @param rep the rep
	 */
	public void modifyCompartmentTypes (String rep)
	{
		modifiedCompartmentTypes.add (rep);
	}
	
	/**
	 * Modify rules.
	 *
	 * @param rep the rep
	 */
	public void modifyRules (String rep)
	{
		modifiedRules.add (rep);
	}
	
	/**
	 * Modify functions.
	 *
	 * @param rep the rep
	 */
	public void modifyFunctions (String rep)
	{
		modifiedFunctions.add (rep);
	}
	
	/**
	 * Modify contraints.
	 *
	 * @param rep the rep
	 */
	public void modifyContraints (String rep)
	{
		modifiedConstraints.add (rep);
	}
	
	/**
	 * Modify events.
	 *
	 * @param rep the rep
	 */
	public void modifyEvents (String rep)
	{
		modifiedEvents.add (rep);
	}
	
	/**
	 * Modify initial assignments.
	 *
	 * @param rep the rep
	 */
	public void modifyInitialAssignments (String rep)
	{
		modifiedInitialAssignments.add (rep);
	}
	
	/**
	 * Modify units.
	 *
	 * @param rep the rep
	 */
	public void modifyUnits (String rep)
	{
		modifiedUnits.add (rep);
	}
}
