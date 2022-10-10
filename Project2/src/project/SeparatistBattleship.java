package project;

import java.util.ArrayList;
/**
 * 
 * @author ardasaygan
 *
 */
public class SeparatistBattleship extends SeparatistDestroyer {

	/*
	* Necessary methods
	*/

	public SeparatistBattleship(int id, String name, Sector currentSector, int coordinate, ArrayList<Crewman> crew) {
		super(id, name, currentSector, coordinate, crew);
		this.armamentPower = 400;
		this.shieldPower = 200;
		this.crewCapacity = 20;
		this.escapePods = 3;
	
		this.changeLogUPDATE(currentSector.getName(), coordinate);
	}

}
