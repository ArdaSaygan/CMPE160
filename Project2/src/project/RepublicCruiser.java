package project;

import java.util.ArrayList;
/**
 * 
 * @author ardasaygan
 *
 */
public class RepublicCruiser extends Warship {

	/*
	* Necessary fields
	*/
	
	private ArrayList<Crewman> captives;
	/*
	* Necessary methods
	*/

	public RepublicCruiser(int id, String name, Sector currentSector, int coordinate, ArrayList<Crewman> crew) {
		super(id, name, currentSector, coordinate, crew);
		this.armamentPower = 100;
		this.shieldPower = 100;
		this.crewCapacity = 10;
		captives = new ArrayList<>();
		
		this.changeLogUPDATE(currentSector.getName(), coordinate);
	}
	
	/**
	 * 
     * Corousant is the base of the Republic and where Jedi temple is located. 
     * When a spacecraft visits corousant all jedi crew members will talk with Yoda which makes their sanity 100 again. 
     * All captives will be put in Corousant-Prison so change their state to imprisoned. 
     * 
	 */
	public void visitCoruscant() {
		if (this.state == StateW.DESTROYED) {
			return;
		}
		
		// Full Jedis sanity
		for (Crewman crewman : crew) {
			if (crewman.getClass() == Jedi.class) {
				Jedi jedi = (Jedi) crewman;
				jedi.fullSanity();
			}
		}
		
		// Imprisone crewman
		for (Crewman crewman : captives) {
			
			crewman.state = State.IMPRISONED;
			crewman.changeLogImprisoned();
		}
		this.captives.clear();
	}
	
	@Override
	/**
	 * Republic ships will accept anybody if there is enough space for crewmen. 
	 * If there is not enough space ignore the event and do nothing.
	 */
	protected boolean canAddCrewman(Crewman crewman) {
		if(crewman.state != State.FREE) {
			return false;
		}
		if(crewman.getClass() == Sith.class) { 
			return false;
		}
		
		if(this.crew.size() >= this.crewCapacity) {
			return false;
		}
		
		return true;
	}

	
	// Getters

	public ArrayList<Crewman> getCaptives(){
		return captives;
	}
	/**
	 * Give a new captive to this cruiser
	 * @param crewman to take captive
	 */
	public void giveCaptive(Crewman crewman) {
		captives.add(crewman);
	}
	@Override
	public Affiliation getAffiliation() {
		return Affiliation.REPUBLIC;
	}
	@Override 
	/**
	 * @return the Commander of this ship, which is the general with highest experince
	 */
	public General getCommander() {
		General commander = null;
		int highestExperience = Integer.MIN_VALUE;
		
		for (Crewman crewman : crew) {
			if (crewman.getClass() != Officer.class) { // If it's a general
				General general = (General) crewman;
				 if (highestExperience < general.getExperience()) {
					 commander = general;
					 highestExperience = general.getExperience();
				 }
				 else if (highestExperience == general.getExperience()) {
					 if (general.getID() < commander.getID()) {
						 commander = general;
					 }
				 }
			}
		}
		
		return commander;
	}

}
