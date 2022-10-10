package project;

import java.util.ArrayList;

public class SeparatistFrigate extends SeparatistDestroyer {

	/*
	* Necessary methods
	*/

	public SeparatistFrigate(int id, String name, Sector currentSector, int coordinate, ArrayList<Crewman> crew) {
		super(id, name, currentSector, coordinate, crew);
		this.armamentPower = 120;
		this.shieldPower =  100;
		this.crewCapacity = 12;
		this.escapePods = 2;
		
		this.changeLogUPDATE(currentSector.getName(), coordinate);
	}

}
