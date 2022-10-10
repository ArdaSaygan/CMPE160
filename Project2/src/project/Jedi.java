package project;

public class Jedi extends General {

	/*
	* Necessary fields
	*/
	private int sanity;
	private int intelligence;
	/*
	* Necessary methods
	*/

	public Jedi(int id, String name, int experience, int midichlorian, int intelligence) {
		super(id, name, experience, midichlorian);
		this.sanity = 100;
		this.intelligence = intelligence;
		this.crewmanType = "Jedi";
		
		log = "Jedi" + " " + name + " is free";
	}

	
	@Override
	public int getForcePower() {
		// TODO Auto-generated method stub
		return 3*midichlorian;
	}
	
	@Override
	public int getCombatPower() {
		// TODO Auto-generated method stub
		return this.getForcePower() + experience + (sanity - 100) + intelligence;
	}
	
	// Getters
	
	public int getSanity() {
		return sanity;
	}

	public int getIntelligence() {
		return intelligence;
	}
	
	// Setters
	/**
	 * Sets the sanity again to full. It's used when a Jedi visits Coroussant and talks to Yoda
	 */
	public void fullSanity() {
		sanity = 100;
	}
	/**
	 * When two warships from another affiliations fight, firstly their commanders, a sith and a jedi 
	 * have small conversation. After the conversation Jedi's sanity may change and this determines the
	 * faith of the attack.
	 * @param sith that jedi talks to 
	 */
	public void talkWithSith(Sith sith) {
		int change = sith.getPersuasion() - this.getIntelligence();
		if (change < 0) {
			return;
		}
		sanity -= change;
		if (sanity < 0) {
			sanity = 0;
		}
	}
}
