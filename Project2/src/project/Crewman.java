package project;
/**
 * 
 * @author ardasaygan
 *
 */
public abstract class Crewman implements Comparable<Crewman>{
	/*
	* Necessary fields
	*/
	/**
	 * id of the crewman will determined by its order of creation
	 */
	protected int id;
	protected String name;
	/**
	 * Every Warhip and Crewman in this project will  have a log field.
	 * Changes in this Objects will be also change the log here.
	 * In the end when we write the state of every Warhip and Crewman, this log will be written.
	 */
	protected String log;
	
	/**
	 * Can be one of these:
	 * Officer, Sith, Jedi
	 */
	protected String crewmanType;
	/**
	 * Can be one of these:
	 * 	FREE,
	 *  DEAD,
	 *	IMPRISONED,
	 *	ONBOARD,
	 *	CAPTIVE
	 */
	public State state;
	/*
	* Necessary methods
	*/
	/**
	 * Constructor,
	 * <P>
	 * In the beggining, every crewman start as free
	 * @param id of this crewman
	 * @param name of this crewman
	 */
	public Crewman(int id, String name) {
		this.id = id;
		this.name = name;
		state = State.FREE;

	}
	
	
	// Getters
	
	public int getID() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public String getCrewmanType() {
		return this.crewmanType;
	}
	/**
	 * Updates the log field.
	 */
	public void changeLogFREE() {
		log = crewmanType + " " + name + " is free";
	}
	/**
	 * Updates the log field.
	 * @param KillerName of the killer
	 */
	public void changeLogDEATH(String KillerName) {
		log = crewmanType + " " + name + " is killed by " + KillerName;
	}
	/**
	 * Updates the log field.
	 * @param WarshipName of the warship
	 */
	public void changeLogInWarship(String WarshipName) { // May be Captive or OnBoard
		log = crewmanType + " " + name + " is in " + WarshipName;
	}
	/**
	 * Updates the log field.
	 */
	public void changeLogImprisoned() {
		log = crewmanType + " " + name + " is imprisoned";
	}
	/**
	 * @return the log of this crewman. Used at the end of the programme to print the states of every crewman.
	 */
	public abstract String getLog();

	/**
	 * Crewmen's state should be printed in a some kind of order. This compareTo method defines that order
	 * @param crewman
	 * @return a negetive number if this is less than crewman, positive if it's greater, zero if they're equal
	 */
	@Override
	public int compareTo( Crewman crewman) {
		// CrewmanType is either "Officer", "Sith" or "Jedi"
		// If it's not Officer, than it's a General
		
		// Generals are greater than Officers
		if (crewman.crewmanType.equals("Officer") && !(this.crewmanType.equals("Officer"))) {
			return 1;
		}
		
		else if (!(crewman.crewmanType.equals("Officer")) && this.crewmanType.equals("Officer")) {
			return -1;
		}
		
		else if (!(crewman.crewmanType.equals("Officer")) && !(this.crewmanType.equals("Officer"))) {
			
			// One with the higher combat power is greater
			if (((General) crewman).getCombatPower() < ((General) this).getCombatPower()){
				return 1;
			}
			else if (((General) crewman).getCombatPower() > ((General) this).getCombatPower()) {
				return -1;
			}
			else {
				
				// One with the less ID is greater
				if (crewman.getID() > this.getID()) {
					return 1;
				}
				else {
					return -1;
				}
			}
		}
		
		else if (crewman.crewmanType.equals("Officer") && this.crewmanType.equals("Officer")) {
			if (((Officer) crewman).getIntrinsicLevel() < ((Officer) this).getIntrinsicLevel()){
				return 1;
			}
			else if (((Officer) crewman).getIntrinsicLevel() > ((Officer) this).getIntrinsicLevel()) {
				return -1;
			}
			else {
				if (crewman.getID() > this.getID()) {
					return 1;
				}
				else {
					return -1;
				}
			}
		}
		else {
			return 0;
		}
		
	}
}
