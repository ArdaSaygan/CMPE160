package project;

public class Officer extends Crewman {

	/*
	* Necessary fields
	*/
	
	private int intrinsicLevel;
	/**
	 * It's one of these: 
	 * TACTICAL,
     * PILOTING,
     * GUNNERY,
     * ENGINEERING,
     * COMMAND
	 */
	private Intrinsic intrinsic;
	/*
	* Necessary methods
	*/

	public Officer(int id, String name, Intrinsic intrinsic, int intrinsicLevel) {
		super(id, name);
		this.id = id;
		this.name = name;
		this.intrinsic = intrinsic;
		this.intrinsicLevel = intrinsicLevel;
		this.crewmanType = "Officer";
		
		log = "Officer" + " " + name + " is free";
	}
	/**
	 * Training increments his intrinsic level by 1. 
	 * Maximum value for intrinsic level is 10. So if an officer has level 10 intrinsic,
	 *  his intrinsic level won’t increment anymore.
	 *  If the officer is dead, then don't do anything
	 */
	public void train() {
		if (this.state == State.DEAD) {
			return;
		}
		if (this.intrinsicLevel < 10) {
			intrinsicLevel++;
		}

	}

	@Override
	public String getLog() {
		return log + "\n" + this.intrinsic + " " + this.getIntrinsicLevel();
	}
	
	
	// Getters

	public int getIntrinsicLevel() {
		return intrinsicLevel;
	}

	public Intrinsic getIntrinsic() {
		return intrinsic;
	}
}
