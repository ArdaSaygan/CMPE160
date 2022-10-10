package project;

import java.util.LinkedList;
/**
 * 
 * @author ardasaygan
 *	
 */

public class Sector {
	/*
	* Necessary fields
	*/

	private int id;
	private String name;
	/**
	 * It can be either REPUBLIC or SEPARATIST
	 */
	private Affiliation affiliation;
	
	/**
	 * This warhips array is used to find the warships in this sector.
	 * It's modified in the Sector.giveWarships(Warships[] warships) method.
	 */
	private Warship[] warships;

	public Sector(int id, String name, Affiliation affiliation) {
		this.id = id;
		this.name = name;
		this.affiliation = affiliation;
	}
	
	/**
	 * Assult is an important method in the project. It's called in Main.Assult() method. 
	 * <P>
	 * When assault takes place all Republic Cruisers will choose the closest Separatist Warship as their target 
	 * such that the target will be on their right side (Target should have a greater x-coordinate) 
	 * and that target should have a strictly smaller power output than the cruiser. 
	 * When two republic cruiser aims for the same target, only the one who is closer to the target will attack 
	 * and the other one won’t attack or search for a new target.
	 * <P>
	 * My Approach: I first put every warship in the sector that is not destroyed to a 	LinkedList called warshipsLinked.
	 * While adding them to the LinkedList I insert them so that LinkedList will always be in order. 
	 * <P>
	 * Then I iterated over this warshipLinked from the 0th index. WarshipsLinked was ordered in descending order of coordinates.
	 * This is what I do:<P>
	 * 		1) Find the Republic Cruiser with the biggest coordinate<P>
	 * 		2) Then look at the ships with greater coordinate than this ship, find the closest Separatist Ship with less PowerOutput<P>
	 * 		3) If this Separatist Ship is in Attacked list, continue with the nextrepublic Cruiser. Otherwise attack the ship and put  it in the Attacked list<P>
	 * For every Republic Cruiser we search the ships in front of it. So if there are a total of N republic Cruisers and no Seperators,
	 * the worst case scenario will be O(n*(n+1)/2) = O(N^2)
	 * <P>
	 * 
	 */
	public void assault() {
		Sector sector = this;
		
//		System.out.println("ASSAULT IN:" + sector.getName());		
		
		LinkedList<Warship> warshipsLinked = new LinkedList<Warship>();
		
		// Find the warships in this sector
		for (Warship warship : warships) {
			
			if (warship.currentSector != sector || warship.state == StateW.DESTROYED) {
				continue;
			}
			// We hold them in a sorted LinkedList. This method inserts them so that order is conserved
			insertWRCoordinate(warshipsLinked, warship);
		}
		// This warships are alreadt attacked and shouldnt be targeted from other ships
		LinkedList<Warship> attacked = new LinkedList<>();
		LinkedList<Warship> willAttack = new LinkedList<>(); // Even indexs are attackers, odd indexes are defenders
		// This algorithm is explained in the Javadoc comment of assault()
		for (int i = 0; i < warshipsLinked.size(); i++) {
			Warship currWarship = warshipsLinked.get(i);
			if (currWarship.getAffiliation() == Affiliation.REPUBLIC) {
				for (int j = i-1; 0 <= j; j--) {
					Warship posSith = warshipsLinked.get(j);
					if (posSith.getAffiliation() == Affiliation.SEPARATISTS) {
						
						
						if (posSith.getPowerOutput() < currWarship.getPowerOutput()) {
							
							if (attacked.contains(posSith)) {
								break;
							}
							
							attacked.add(posSith);
							willAttack.add(currWarship);
							willAttack.add(posSith);
							break;
						}
					}
				}
			}
		}
		
		for (int i = 0; i < willAttack.size(); i++) {
			if (i%2 == 1) {
				continue;
			}
			String attackerId = Integer.toString(willAttack.get(i).id);
			String defenderId = Integer.toString(willAttack.get(i+1).id);
			
//			System.out.println(willAttack.get(i).name + " attacks " + willAttack.get(i+1).name);
			Main.Attack( attackerId, defenderId );
		}
	

		
	}
	
	/**
	 * This method is to give Sector accessibilty to all warhips. So that it can determine which warcrafts are in it.
	 * @param warships
	 */
	public void giveWarshipsInThisSector(Warship[] warships) {
		this.warships = warships;
	}
	
	/**
	 * This method inserts an element to an already sorted LinkedList of warships in this sector such that the order of Warship coordinates is conserved.
	 * @param l An already sorted Warship list in descending order of Warship coordinates
	 * @param warship to insert
	 * @return the inserted LinkedList of Warships in this sector
	 */
	private static LinkedList<Warship> insertWRCoordinate(LinkedList<Warship> l, Warship warship) {
		// Descending Order
		
		if (l.size() == 0) {
			l.add(warship);
		}
		// If every element is already smaller than warship, then insert to the last
		if (warship.coordinate <= l.getLast().coordinate) {
			l.addLast(warship);
		}
		for (int i = 0; i < l.size(); i++) {
			// Every elements should be greater than its consecutive
			if (warship.coordinate > l.get(i).coordinate) {
				l.add(i, warship);
				break;
			}
		}
		return l;
	}
	
	//Getters 

	public int getID() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Affiliation getAffiliation() {
		return affiliation;
	}
}
