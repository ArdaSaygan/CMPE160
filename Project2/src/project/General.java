package project;
/**
 * 
 * @author ardasaygan
 *
 */
public abstract class General extends Crewman implements IForceUser{

	/*
	* Necessary fields
	*/
	protected int experience;
	protected int midichlorian;
	/*
	* Necessary methods
	*/

	public General(int id, String name, int experience , int midichlorian) {
		super(id, name);
		this.experience = experience;
		this.midichlorian = midichlorian;
	}
	
	/**
	 * @return the log of this General, it's used in the end of the programme
	 */
	@Override
	public String getLog() {
		return log + "\n" + this.getCombatPower();
	}
	
	// Getters 

	public int getExperience() {
		return experience;
	}

	public int getMidichlorian() {
		return midichlorian;
	}
	
	/**
	 * When a general kills a crewman, it's experience increases.
	 * <P> If this crewman is another general, it gains its experience; if it's an officer, it gains its intrinsic level
	 * @param crewman
	 */
	public void gainExperienceByKilling(Crewman crewman) {
		if (crewman.crewmanType == "Jedi" || crewman.crewmanType == "Sith") {
			this.experience += ((General) crewman).experience;
		}
		if (crewman.crewmanType == "Officer") {
			this.experience += ((Officer) crewman).getIntrinsicLevel();
		}
	}
}
