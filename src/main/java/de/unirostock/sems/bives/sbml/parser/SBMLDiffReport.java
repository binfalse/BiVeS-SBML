/**
 * 
 */
package de.unirostock.sems.bives.sbml.parser;


import java.util.ArrayList;
import java.util.List;


/**
 * @author Martin Scharm
 *
 */
public class SBMLDiffReport
{
	private String header;
	
	private List<String>	modifiedSpecies;
	private List<String>	modifiedSpeciesTypes;
	private List<String>	modifiedParameter;
	private List<String>	modifiedReactions;
	private List<String>	modifiedCompartments;
	private List<String>	modifiedCompartmentTypes;
	private List<String>	modifiedRules;
	private List<String>	modifiedFunctions;
	private List<String>	modifiedConstraints;
	private List<String>	modifiedEvents;
	private List<String>	modifiedInitialAssignments;
	private List<String>	modifiedUnits;
	
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
	
	public void addHeader (String header)
	{
		this.header += header;
	}
	
	public void modifySpecies (String rep)
	{
		modifiedSpecies.add (rep);
	}
	
	public void modifySpeciesTypes (String rep)
	{
		modifiedSpeciesTypes.add (rep);
	}
	
	public void modifyParameter (String rep)
	{
		modifiedParameter.add (rep);
	}
	
	public void modifyReaction (String rep)
	{
		modifiedReactions.add (rep);
	}
	
	public void modifyCompartments (String rep)
	{
		modifiedCompartments.add (rep);
	}
	
	public void modifyCompartmentTypes (String rep)
	{
		modifiedCompartmentTypes.add (rep);
	}
	
	public void modifyRules (String rep)
	{
		modifiedRules.add (rep);
	}
	
	public void modifyFunctions (String rep)
	{
		modifiedFunctions.add (rep);
	}
	
	public void modifyContraints (String rep)
	{
		modifiedConstraints.add (rep);
	}
	
	public void modifyEvents (String rep)
	{
		modifiedEvents.add (rep);
	}
	
	public void modifyInitialAssignments (String rep)
	{
		modifiedInitialAssignments.add (rep);
	}
	
	public void modifyUnits (String rep)
	{
		modifiedUnits.add (rep);
	}
}
