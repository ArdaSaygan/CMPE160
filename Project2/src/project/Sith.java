package project;
/**
 * 
 * @author ardasaygan
 *
 */
public class Sith extends General {

	/*
	* Necessary fields
	*/
	private int persuasion;
	/*
	* Necessary methods
	*/
	
	public Sith(int id, String name, int experience, int midichlorian, int persuasion) {
		super(id, name, experience, midichlorian);
		this.persuasion = persuasion;
		this.crewmanType = "Sith";
		
		log = "Sith" + " " + name + " is free";
	}
	
	@Override
	public int getForcePower() {
		// TODO Auto-generated method stub
		return 4*midichlorian;
	}

	@Override
	public int getCombatPower() {
		// TODO Auto-generated method stub
		return this.getForcePower() + experience + persuasion;
	}
	
	// Getters
	public int getPersuasion() {
		return persuasion;
	}


}
