package project;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Scanner;
/**
 * 
 * @author ardasaygan
 *
 */
public class Main {

	static Sector[] sectors;
	static Crewman[] crewmen;
	static Warship[] warships;
	static Object[] eventList;
	
	public static void main(String[] args) throws IOException {
		/*
		 * 
		 * There are three stages in main method: 
		 * 1) Get the inputs
		 * 2) Do the events
		 * 3) Write on the output file
		 * 
		 */
		
		//1) GET THE INPUTS
		
		File input = new File(args[0]);
//		String[] inputs = {"large.in", "large2.in", "large3.in"};
//		String[] outputs = {"large.out", "large2.out", "large3.out"};
//		int index = 0;
//		File input = new File(inputs[index]);
		Scanner sc = new Scanner(input);
		
		// Get the inputs of sectors
		int S = sc.nextInt();
		sectors = new Sector[S];
		for (int i=0; i<S; i++) {
			String name = sc.next();
			Affiliation affiliation;
			if (sc.next().equals("REPUBLIC")) {
				affiliation = Affiliation.REPUBLIC;
			}
			else {
				affiliation = Affiliation.SEPARATISTS;
			}
			sectors[i] = new Sector(i+1, name, affiliation);
		}
		
		// Get the inputs of crewman
		int C = sc.nextInt();
		crewmen = new Crewman[C];
		for (int i = 0; i < C; i++) {
			String crewmanType = sc.next();
			
			switch (crewmanType) {
			case "Officer":
				String name = sc.next();
				Intrinsic intrinsic = null;
				switch (sc.next()) {
				case "PILOTING":
					intrinsic = Intrinsic.PILOTING;
					break;
				case "TACTICAL":
					intrinsic = Intrinsic.TACTICAL;
					break;
				case "GUNNERY":
					intrinsic = Intrinsic.GUNNERY;
					break;
				case "ENGINEERING":
					intrinsic = Intrinsic.ENGINEERING;
					break;
				case "COMMAND":
					intrinsic = Intrinsic.COMMAND;
					break;
				default:
					break;
				}
				int intrinsicLevel = sc.nextInt();
				crewmen[i] = new Officer(i+1, name, intrinsic, intrinsicLevel);
				break;
				
			case "Jedi":
				String nameJ = sc.next();
				int experienceJ = sc.nextInt();
				int midichlorianJ = sc.nextInt();
				int intelligence = sc.nextInt();
				
				crewmen[i] = new Jedi(i+1, nameJ, experienceJ, midichlorianJ, intelligence);				
				break;
				
			case "Sith":
				String nameS = sc.next();
				int experienceS = sc.nextInt();
				int midichlorianS = sc.nextInt();
				int persuasion = sc.nextInt();
				
				crewmen[i] = new Sith(i+1, nameS, experienceS, midichlorianS, persuasion);
				break;

			default:
				break;
			}
		}
		// Get the inputs of warships
		int W = sc.nextInt();
		warships = new Warship[W];
		for (int j = 0; j < W; j++) {
			String className = sc.next();
			String name = sc.next();
			int currSectorID = sc.nextInt();
			int coordinate = sc.nextInt();
			
			int crewSize = sc.nextInt();
			ArrayList<Crewman> crew = new ArrayList<Crewman>();
			for (int k = 0; k < crewSize; k++) {
				crew.add(crewmen[sc.nextInt()-1]);
			}
			
			switch (className) {
			case "RepublicCruiser":
				warships[j] = new RepublicCruiser(j+1, name, sectors[currSectorID-1], coordinate, crew);
				break;
			case "SeparatistBattleship":
				warships[j] = new SeparatistBattleship(j+1, name, sectors[currSectorID-1], coordinate, crew);
				break;
			case "SeparatistDestroyer":
				warships[j] = new SeparatistDestroyer(j+1, name, sectors[currSectorID-1], coordinate, crew);
				break;
			case "SeparatistFrigate":
				warships[j] = new SeparatistFrigate(j+1, name, sectors[currSectorID-1], coordinate, crew);
				break;
			default:
				break;
			}
		}
		// Get the inputs of events.
		/*
		 * Events are stored in an array. After the inputs are taken, we will iterate over this array and do each of them.
		 * Events arrau holds List of objects, which are parameters that describe the event.
		 */
		int E = sc.nextInt();
		eventList = new Object[E];
		for (int j = 0; j < E; j++) {
			ArrayList<Object> event = new ArrayList<>();
			String code = sc.next();
			event.add(code);
			if (code.equals("10") || code.equals("40")  || code.equals("41")) {
				event.add(sc.next());
				event.add(sc.next());
			}
			
			if (code.equals("11") || code.equals("30") || code.equals("50") ){
				event.add(sc.next());
			}
			
			if (code.equals("20") || code.equals("51")) {
				event.add(sc.next());
				event.add(sc.next());
				event.add(sc.next());
			}
			
			eventList[j] = event;
		}
	
		// INPUTS HAVE BEEN TAKEN SUCCESSFULY
		
		// 2) APPLY THE EVENTS
		for (Object e : eventList) {
			ArrayList<String> event = (ArrayList<String>) e;
			
			String code = event.get(0);
			switch (code) {
			case "10" : // Attack : 10 Attacker-Id Defender-Id
				Attack(event.get(1), event.get(2));
				break; 
			case "11": // Assult : 11 Sector-Id
				Assault(event.get(1));
				break;
			case "20": // Relocate : 20 Warship-Id Sector-Id Coordinate
				JumpToSector(event.get(1), event.get(2), event.get(3));
				break;
			case "30": // Visit Croisant : 30 Cruiser-Id
				VisitCoroussant(event.get(1));
				break;
				
			case "40": // Add Crewman : 40 Crewman-Id Warship-Id
				AddCrewman(event.get(1), event.get(2));
				break;
				
			case "41": // Remove Crewman : 41 Crewman-Id Warship-Id
				RemoveCrewman(event.get(1), event.get(2));
				break;
				
			case "50": // Train Officer : 50 Officer-Id
				TrainOfficer(event.get(1));
				break;
				
			case "51": // Upgrade ArmShield Power : 51 Warship-Id (Armament or Shield) Amount
				UpgradeWarship(event.get(1), event.get(2), event.get(3));
				break;

			default:
				break;
			}
		}
		
		
		
		// NOW WRITE THE OUTPUTS
		   
//		File output = new File("O.txt");
		FileWriter fw = new FileWriter(args[1]);
		
		// You should first sort the arrays,
		Arrays.sort(warships);
		Arrays.sort(crewmen);
		
		
		// Write warhips logs
		for (int i = 1; i <= W; i++) {
			Warship w = warships[W-i];
			
			if (w.state == StateW.WORKS) {
				w.changeLogUPDATE(w.currentSector.getName(), w.coordinate); // To update the Combat Power
			}
			
			fw.write(warships[W-i].getLog());
		}
		
		// Write crewmen's logs
		for (int i = 1; i <= C; i++) {
			fw.write(crewmen[C-i].getLog() + "\n");
		}
		fw.flush();
		
		// My Checker for comparing Test Outputs
//		if (Arrays.equals(Files.readAllBytes(output.toPath()), Files.readAllBytes( new File(outputs[index]).toPath()))) {
//			System.out.print("NICE JOBE ! ! !");
//		}
//		else {
//			System.out.print("you're not gargabe, at least gargabe CAN!");
//		}
		
		
	}
	
	/**
	 * This method will apply the attack event.
	 * @param attackerWarshipID that attacks
	 * @param defenderWarshipID that defends
	 */
	
	public static void Attack(String attackerWarshipID, String defenderWarshipID) {

		int attackerWi = Integer.parseInt(attackerWarshipID) - 1;
		int defenderWi = Integer.parseInt(defenderWarshipID) - 1;
		Warship attackerW = warships[attackerWi];
		Warship defenderW = warships[defenderWi];
		
		// If one of the warships are alreadt destroyed, ignore this event
		if (attackerW.state == StateW.DESTROYED || defenderW.state == StateW.DESTROYED) {
			return;
		}
		
		// Attacker jumps to the Defenders Sector
		attackerW.jumpToSector(defenderW.currentSector, defenderW.coordinate);
		attackerW.changeLogUPDATE(defenderW.currentSector.getName(), defenderW.coordinate);
	
		// Find the commanders
		Sith commanderSith;
		Jedi commanderJedi;
		Warship warshipSith;
		Warship warshipJedi;
		// Find the warship of Jedis and Siths
		if (attackerW.getAffiliation() == Affiliation.REPUBLIC) { // JEDIS ARE ATTACKING
			commanderSith = (Sith) defenderW.getCommander();
			commanderJedi = (Jedi) attackerW.getCommander();
			warshipSith = defenderW;
			warshipJedi = attackerW;
		}
		else { // SITHS ARE ATTACKING
			commanderSith = (Sith) attackerW.getCommander();
			commanderJedi = (Jedi) defenderW.getCommander();
			warshipSith = attackerW;
			warshipJedi = defenderW;
		}
		
		
		/*
		 * There are three scenarios:
		 * 0) Sith persuades Jedi. Jedi kills everybody, and Sith commander kills Jedi at the end
		 * 1) Sith cannot persuade Jedi but Sith kills everybody.
		 * 2) Sith cannot persuade Jedi and Jedis take Officer captive, Siths are killed
		 */

		
		int scenario;
		// Sith and Jedi Talk
		commanderJedi.talkWithSith(commanderSith);
		if (commanderJedi.getSanity() == 0) {
			// JEDI KILLS EVERYBODY 
			scenario = 0;
		}
		else {
			
			//If attacker warship has strictly more powerOutput, attacker wins
			if (attackerW.getPowerOutput() > defenderW.getPowerOutput()) {
				if (attackerW.getAffiliation() == Affiliation.SEPARATISTS) {
					// SITH KILLS EVERYBODY
					scenario = 1;
				}
				else {
					// JEDI TAKES CAPTIVES
					scenario = 2;
				}
			}
			// else, defenders win
			else {
				if (defenderW.getAffiliation() == Affiliation.SEPARATISTS) {
					// SITH KILLS EVERYBODY
					scenario = 1;
				}
				else {
					// JEDI TAKES CAPTIVES
					scenario = 2;
				}
			}
			
		}
		
		switch (scenario) {
		case 0: // JEDI KILLS EVERYBODY
			
			// Jedicommander kills every cruise member
			for (Crewman crewman : warshipJedi.crew) {
				if (crewman.equals(commanderJedi)) {
					continue;
				}
				crewman.state = State.DEAD;
				crewman.changeLogDEATH(commanderJedi.getName());
				commanderJedi.gainExperienceByKilling(crewman);
			}
			// also kills the captives
			for (Crewman captive : ((RepublicCruiser) warshipJedi).getCaptives()) {
				captive.state = State.DEAD;
				captive.changeLogDEATH(commanderJedi.getName());
				commanderJedi.gainExperienceByKilling(captive);
			}
			
			// Seperator will destroy the Cruiser
			warshipJedi.state = StateW.DESTROYED;
			warshipJedi.changeLogDEATH(warshipSith.name, defenderW.currentSector.getName(), defenderW.coordinate);
	
			// Sith commander kills the Jedi Commander
			commanderJedi.state = State.DEAD;
			commanderJedi.changeLogDEATH(commanderSith.getName());
			commanderSith.gainExperienceByKilling(commanderJedi);
			
			// The Total Power of the Siths Warship increased
			warshipSith.changeLogUPDATE(defenderW.currentSector.getName(), defenderW.coordinate);
			break;
			
		case 1:	// SITH KILLS EVERBODY
			
			// Cruise is destroyed
			warshipJedi.state = StateW.DESTROYED;
			warshipJedi.changeLogDEATH(warshipSith.name, defenderW.currentSector.getName(), defenderW.coordinate);
			// Everyone is killed by the Sith
			for (Crewman crewman : warshipJedi.crew) {
				crewman.state = State.DEAD;
				crewman.changeLogDEATH(commanderSith.getName());
				commanderSith.gainExperienceByKilling(crewman);
			}
			// also captives are killed by Sith commander
			for (Crewman captive : ((RepublicCruiser) warshipJedi).getCaptives() ) {
				captive.state = State.DEAD;
				captive.changeLogDEATH(commanderSith.getName());
				commanderSith.gainExperienceByKilling(captive);
			}
			
			// The Total Power of the Siths Warship increased
			warshipSith.changeLogUPDATE(defenderW.currentSector.getName(), defenderW.coordinate);
			break;
		case 2: // JEDI TAKES CAPTIVES
			
			// Sith warship is destroyed
			warshipSith.state = StateW.DESTROYED;
			warshipSith.changeLogDEATH(warshipJedi.name, defenderW.currentSector.getName(), defenderW.coordinate);
			
			
			LinkedList<General> sithsTryToEscape= new LinkedList<>();
			LinkedList<Crewman> killTheseSiths = new LinkedList<>();
			for (Crewman crewman : warshipSith.crew) {
				// All officers in SithWarship are kept captive
				if (crewman.crewmanType == "Officer") {
					crewman.state = State.CAPTIVE;
					crewman.changeLogInWarship(warshipJedi.name);
					((RepublicCruiser) warshipJedi).giveCaptive(crewman);
				}
				
				

				// Jedi tries to kill Siths
				if (crewman.crewmanType == "Sith")  {
					
					if (commanderJedi.getCombatPower() >= ((Sith) crewman).getCombatPower()) {
						killTheseSiths.add(crewman);
					}
					else {
						sithsTryToEscape.add((General)crewman);
					}
				}	
			}
			// These siths have less power so they're immidiately killed
			for (Crewman sith : killTheseSiths) {
				sith.state = State.DEAD;
				sith.changeLogDEATH(commanderJedi.getName());
				commanderJedi.gainExperienceByKilling(sith);
			}
			
			// Some of them try to escape
			Collections.sort(sithsTryToEscape);
			Collections.reverse(sithsTryToEscape);
			int noEscapedpods = ((SeparatistDestroyer) warshipSith).getEscapePods();
			int noSithEscaping = sithsTryToEscape.size();
			for (int i = 0; i < Math.min(noEscapedpods, noSithEscaping); i++) {
				
				General sith = sithsTryToEscape.pop();
				sith.state = State.FREE;
				sith.changeLogFREE();	
			}
			// These sits are more powerful than the Jedi but no escape pods left
			for (General sith : sithsTryToEscape) {
				sith.state = State.DEAD;
				sith.changeLogDEATH(commanderJedi.getName());
				commanderJedi.gainExperienceByKilling(sith);
			} 
			
			// The Total Power of the Jedi Warship increased
			warshipJedi.changeLogUPDATE(defenderW.currentSector.getName(), defenderW.coordinate);
			break;
		default:
			break;
		}
		
	}
	/*
	 * I created static methods for every event. While iterating over the events[] one of these methods are called.
	 */
	
	
	public static void Assault(String sectorID) {
		int sectorIndex = (Integer.parseInt(sectorID)) - 1;
		Sector sector = sectors[sectorIndex];
		sector.giveWarshipsInThisSector(warships);
		sector.assault();
		
	}
	
	
	public static void JumpToSector(String warshipID, String sectorID, String _coordinate) {
		int warshipIndex = Integer.parseInt(warshipID) - 1;
		int sectorIndex = Integer.parseInt(sectorID) - 1;
		int coordinate = Integer.parseInt(_coordinate);
		warships[warshipIndex].jumpToSector(sectors[sectorIndex], coordinate);
	}


	public static void VisitCoroussant(String CruiserID) {
		
		int warshipIndex = Integer.parseInt(CruiserID) - 1;
		((RepublicCruiser) warships[warshipIndex]).visitCoruscant();
 	}
	
	
	
	public static void AddCrewman(String CrewmanID, String WarshipID) {
		int warshipIndex = Integer.parseInt(WarshipID) - 1;
		int crewmanIndex = Integer.parseInt(CrewmanID) - 1;
		
		warships[warshipIndex].addCrewman(crewmen[crewmanIndex]);
	}


	public static void RemoveCrewman(String CrewmanID, String WarshipID) {
		
		int warshipIndex = Integer.parseInt(WarshipID) - 1;
		int crewmanIndex = Integer.parseInt(CrewmanID) - 1;
		warships[warshipIndex].removeCrewman(crewmen[crewmanIndex]);
	}
	

	public static void TrainOfficer(String OfficerID) {
		int officerIndex = Integer.parseInt(OfficerID) - 1;
		((Officer) crewmen[officerIndex]).train();
	}
	

	public static void UpgradeWarship(String WarshipID, String ArmamentOrShield, String Amount) {
		int warshipIndex = Integer.parseInt(WarshipID) - 1;
		int amount = Integer.parseInt(Amount);
		if (ArmamentOrShield.equals("Shield")) {
			warships[warshipIndex].upgradeShield(amount);
		}
		else {
			warships[warshipIndex].upgradeArmament(amount);
		}
	}
	
}
