package project;

import java.util.ArrayList;

public abstract class Warship implements IWarship, Comparable<Warship>{

	/*
	* Necessary fields
	*/
	protected int id;
	protected String name;
	protected Sector currentSector;
	protected  int coordinate;
	protected int armamentPower;
	protected int shieldPower;
	protected int crewCapacity;
	protected ArrayList<Crewman> crew;
	
	/**
	 * Every Warhip and Crewman in this project will  have a log field.
	 * Changes in this Objects will be also change the log here.
	 * In the end when we write the state of every Warhip and Crewman, this log will be written.
	 */
	protected String log;
	/**
	 * Stae of the warship can eithe be:
	 * WORKS,
	 * DESTROYED
	 */
	public StateW state;
	/*
	* Necessary methods
	*/
	/**
	 * Constructor
	 * <P> When a new warship is created, it's crew members are given. While adding them to crew, we also change their state to ONBOARD.
	 * @param id of this warhsip
	 * @param name of this warhsip
	 * @param currentSector of this warhsip
	 * @param coordinate of this warhsip
	 * @param crew of this warhsip
	 */
	public Warship(int id,String name, Sector currentSector, int coordinate, ArrayList<Crewman> crew) {
		this.id = id;
		this.name = name;
		this.currentSector = currentSector;
		this.coordinate = coordinate;
		this.crew = crew;
		this.state = StateW.WORKS;
//		this.changeLogUPDATE(currentSector.getName(), coordinate);
		
		for (Crewman crewman : crew) {
			crewman.state = State.ONBOARD;
			crewman.changeLogInWarship(name);
		}
	}

	@Override
	/**
	 * Adds the crewman to the warship if this crewman if possible, Sith and Jedi warhips have different protocols for this
	 * so an abstract canAddCrewman is define for this.
	 * <P> Changes the state of crewman to onboard, also update warships and crewmans logs
	 * @param crewman to add
	 */
	public void addCrewman(Crewman crewman) {
		// TODO Auto-generated method stub
		if (!canAddCrewman(crewman) || this.state == StateW.DESTROYED) {
			return;
		}
		
		crewman.state = State.ONBOARD;
		this.crew.add(crewman);
		this.changeLogUPDATE(this.currentSector.getName(), this.coordinate);
		crewman.changeLogInWarship(this.name);
	}
	/**
	 * Republic and seperatists have different protocols to have a new crewman. They are implemented in their own classes
	 * @param crewman
	 * @return true if it's possible to add
	 */
	abstract protected boolean canAddCrewman(Crewman crewman);

	@Override
	/**
	 * Removes the crewman and updates both warships and crewmen's state and logs.
	 */
	public void removeCrewman(Crewman crewman) {
		if (this.state == StateW.DESTROYED) {
			return;
		}
		
		if (crew.contains(crewman)) {
			crew.remove(crewman);
			crewman.state = State.FREE;
			this.changeLogUPDATE(this.currentSector.getName(), this.coordinate);
			crewman.changeLogFREE();
		}
	}

	@Override
	/**
	 * Chanes the sector of the warship. updates warships log.
	 */
	public void jumpToSector(Sector sector, int coordinate) {
		// TODO Auto-generated method stub
		this.currentSector = sector;
		this.coordinate = coordinate;
		this.changeLogUPDATE(this.currentSector.getName(), this.coordinate);
	}

	@Override
	/**
	 * This method crucial since it's used in attack and assault method. Also warships are sorted in order of their power outputs.
	 * @return the power output of the warship
	 */
	public int getPowerOutput() {
		if (this.state == StateW.DESTROYED) {
			return 0;
		}
		
		// TODO Auto-generated method stub
		int SectorBuff = 2;
		if (this.currentSector.getAffiliation() == this.getAffiliation()) {
			SectorBuff = 3;
		}
		
		return SectorBuff*(armamentPower + shieldPower + getGeneralsContribution() + getOfficersContribution());
		
	}


	@Override 
	/**
	 * Upgrades the armanentPower field with the given amount
	 * There's no limit to upgrade. 
	 * @param amount that armanent will be updated
	 */
	public void upgradeArmament(int amount) {
		// TODO Auto-generated method stub
		this.armamentPower += amount;
		this.changeLogUPDATE(this.currentSector.getName(), this.coordinate);
	}

	@Override
	/**
	 * Upgrades the shieldPower field with the given amount
	 * There's no limit to upgrade. 
	 * @param amount that shield will be updated
	 */
	public void upgradeShield(int amount) {
		// TODO Auto-generated method stub
		this.shieldPower += amount;
		this.changeLogUPDATE(this.currentSector.getName(), this.coordinate);
	}

	// Getters
	/**
	 * Affiliation is either Republic or Separatist
	 * @return the affiliation type of this warship
	 */
	abstract Affiliation getAffiliation();
	/**
	 * This method is used in getPowerOutput
	 * @return the officers contribution to the power output
	 */
	private int getOfficersContribution() {
		// These  will hold the maximum levels of intrinsic level for every type
		int IntTACTICAL = 0;
		int IntPILOTING = 0;
		int IntGUNNERY = 0;
		int IntENGINEERING = 0;
		int IntCOMMAND = 0;
		
		// Iterate over crewman, if their intrinsic level are greater than that levels maximum level, update
		for (Crewman crewman : crew) {
			if (crewman.getClass() == Officer.class) {
				Officer officer = (Officer) crewman;
				
				switch (officer.getIntrinsic()) {
				case TACTICAL:
					if (IntTACTICAL < officer.getIntrinsicLevel()) {
						IntTACTICAL = officer.getIntrinsicLevel();
					}
					break;
				case PILOTING:
					if (IntPILOTING < officer.getIntrinsicLevel()) {
						IntPILOTING = officer.getIntrinsicLevel();
					}
					break;
				case GUNNERY:
					if (IntGUNNERY < officer.getIntrinsicLevel()) {
						IntGUNNERY = officer.getIntrinsicLevel();
					}
					break;
				case ENGINEERING:
					if (IntENGINEERING < officer.getIntrinsicLevel()) {
						IntENGINEERING = officer.getIntrinsicLevel();
					}
					break;
				case COMMAND:
					if (IntCOMMAND < officer.getIntrinsicLevel()) {
						IntCOMMAND = officer.getIntrinsicLevel();
					}
					break;
				default:
					break;
				}
			}
		}
		
		return (IntTACTICAL + 1)*(IntPILOTING + 1)*(IntGUNNERY + 1)*(IntENGINEERING + 1)*(IntCOMMAND + 1);
	}
	
	/**
	 * This method is used in getPowerOutput
	 * @return the generals contribution to the power output to the ship
	 */
	private int getGeneralsContribution() {
		int contribution = 0;
		for (Crewman crewman : crew) {
			if (crewman.getClass() != Officer.class) { // If it's a general
				General general = (General) crewman;
				 contribution += general.getCombatPower();
			}
		}
		return contribution;
	}
	
	
	/**
	 * Updates the log with the following format<P>
	 * Warship Name in ( Sector Name , Coordinate ) Commander Name Power Output
	 * @param sectorName of the sector it's in
	 * @param coordinate of the ship
	 */
	public void changeLogUPDATE( String sectorName, int coordinate) {
		if (this.state == StateW.DESTROYED) { // You shouldn't update a destroyed warship
			return;
		}
		log =  "Warship " + this.name + " in" + " (" + sectorName + ", " + coordinate + ")" + "\n" + this.getCommander().name + " " + this.getPowerOutput() + "\n"; 
	}
	/**
	 * Updates the log with the following format<P>
	 * Warship Name is destroyed by _DestroyerWarshipName_ on ( _SectorName_ , _Coordinate_ )
	 * @param destroyerWarshipperName that destroyed this ship
	 * @param sectorName where it's destroyed
	 * @param coordinate of this ship
	 */
	public void changeLogDEATH(String destroyerWarshipperName, String sectorName, int coordinate) {
		log = "Warship " + this.name + " is destroyed by " + destroyerWarshipperName + " in (" + sectorName +  "," +  coordinate+ ")" + "\n";
	}
	/**
	 * This method is used in the end of the programme
	 * @return the log of this ship
	 */
	public String getLog() {
		return log;
	}
	
	/**
	 * Warships' logs are printed in an order in the end of the programme. This method defines that order.
	 * <P>Ships that Work are greater than Destroyed ones.
	 * <P> Ships with greater power output are also greater.
	 * <P> Ships with lower ID are also greater.
	 */
	public int compareTo(Warship otherWarship) {
		if (otherWarship.state == StateW.WORKS && this.state == StateW.DESTROYED) {
			return -1;
		}
		else if (otherWarship.state == StateW.DESTROYED && this.state == StateW.WORKS) {
			return 1;
		}
		
		if (otherWarship.getPowerOutput() < this.getPowerOutput()) {
			return 1;
		}
		else if (otherWarship.getPowerOutput() > this.getPowerOutput()) {
			return -1;
		}
		else {
			if (otherWarship.id > this.id) {
				return 1;
			}
			else if (otherWarship.id < this.id) {
				return -1;
			}
			else {
				return 0;
			}
		}
	}
}
