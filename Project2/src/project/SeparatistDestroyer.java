package project;

import java.util.ArrayList;
/**
 * 
 * @author ardasaygan
 *
 */
public class SeparatistDestroyer extends Warship {

	/*
	* Necessary fields
	*/
	protected int escapePods;
	/*
	* Necessary methods
	*/

	public SeparatistDestroyer(int id, String name, Sector currentSector, int coordinate, ArrayList<Crewman> crew) {
		super(id, name, currentSector, coordinate, crew);
		this.armamentPower = 80;
		this.shieldPower = 60;
		this.crewCapacity = 7;
		this.escapePods = 1;
		
		this.changeLogUPDATE(currentSector.getName(), coordinate);
	}
	
	@Override
	/**
	 * there should be enough space. 
	 * the new crewman should increase the ship’s combat power
	 * @return true if this conditions are true
	 */
	public boolean canAddCrewman(Crewman crewman) {
		if (crewman.state != State.FREE) {
			return false;
		}
		if (crewman.getClass() == Jedi.class) {
			return false;
		}
		
		if (this.crew.size() >= crewCapacity) {
			return false;
		}
		
		// If the officier is not increasing the power output
		if (crewman.getClass() == Officer.class) {
			// Officier should increase the power output of the ship, it should a greater instrinsic value than other officier in the ship with the same intrinsic type.
			int maxIntrinsic = Integer.MIN_VALUE; // Max Intrinsic of other crew members with the same intrinnsix type
			Officer crewmanO = (Officer) crewman;
			for (Crewman c : crew) {
				if (c.getClass() == Officer.class) {
					Officer officerI = (Officer) c;
					if (officerI.getIntrinsic() == crewmanO.getIntrinsic()) {
						if (maxIntrinsic < officerI.getIntrinsicLevel()) {
							maxIntrinsic = officerI.getIntrinsicLevel();
						}
					}
					
				}
			
			}
			if (crewmanO.getIntrinsicLevel() <= maxIntrinsic) {
				return false;
			}	
		}
		
		//Any general will always increase the power output
		
		return true;
	}

	// Getters

	public int getEscapePods() {
		return escapePods;
	}
	@Override
	public Affiliation getAffiliation() {
		return Affiliation.SEPARATISTS;
	}
	@Override 
	/**
	 * Find the commander of this ship, which is the commander with the highest power
	 * @return Commander
	 */
	public General getCommander() {
		General commander = null;
		int highestCombat = Integer.MIN_VALUE;
		
		for (Crewman crewman : crew) {
			if (crewman.getClass() != Officer.class) { // If it's a general
				General general = (General) crewman;
				 if (highestCombat < general.getCombatPower()) {
					 commander = general;
					 highestCombat = general.getCombatPower();
				 }
				 else if (highestCombat == general.getCombatPower()) {
					 if (general.getID() < commander.getID()) {
						 commander = general;
					 }
				 }
			}
		}
		
		return commander;
	}
}
