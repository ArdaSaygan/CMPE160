package project.airlineContainer;
import project.airportContainer.*;
import project.passengerContainer.*;
import project.airlineContainer.aircraftContainer.*;
import project.airlineContainer.aircraftContainer.concreteContainer.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author ardasaygan
 *
 */

public class Airline {
	
	private double expenses;
	private double revenue;
	/**
	 * Every operation will have a log String. These logs'll be hold in 
	 * this ArrayList and will be written in the output file at the end of main.
	 */
	private ArrayList<String> logs = new ArrayList<String>();
	
	/**
	 * Maximum number of aircrafts this airline can hold. Will be given in the input.
	 */
	private int maxAircraftCount;
	/**
	 * Operational cost value for this airline. Will be given in the input.
	 */
	private double operationalCost;
	/**
	 * Operation Fees for every aircraft type. Will be given with the input.
	 */
	private double propOpFee, wideOpFee, rapidOpFee, jetOpFee;
	
	
	/**
	 * 
	 * @param maxAircraftCount Maximum number of aircrafts this airline can hold
	 * @param operationalCost Operational cost value for this airline.
	 * @param propOpFee Operation Fees for every aircraft type
	 * @param wideOpFee Operation Fees for every aircraft type
	 * @param rapidOpFee Operation Fees for every aircraft type
	 * @param jetOpFee Operation Fees for every aircraft type
	 */
	public Airline(int maxAircraftCount, double operationalCost, double propOpFee, double wideOpFee, double rapidOpFee, double jetOpFee) {
		this.maxAircraftCount = maxAircraftCount;
		this.operationalCost = operationalCost;
		this.propOpFee = propOpFee;
		this.wideOpFee = wideOpFee;
		this.rapidOpFee = rapidOpFee;
		this.jetOpFee = jetOpFee;
		expenses = 0;
		revenue = 0;
	}
	
	/**
	 * Aircrafts are stored in Airline. Their index  represent their Aircraft IDs. 
	 * Each aircraft has a currentAirport variable, this is why we don't need to hold them in Airport.
	 */
	public ArrayList<Aircraft> aircrafts = new ArrayList<Aircraft>();
	private Airport[] givenAirports;
	private Passenger[] givenPassengers;
	
	
	/**
	 * There is a running cost for every flight.
	 * <P> 
	 * Running cost = operationalCost * numberOfAircraftsInTheAirport
	 * <P>
	 * Running cost is added to the expenses automatically when this method is called.
	 * If the flight is successful, flight cost is added to the expenses as well.
	 * @param toAirport Airport to fly
	 * @param aircraftIndex, Index at which aircraft is stored in the aircrafts ArrayList. It's also called the Aircraft ID.
	 * @return The total cost, depends on the flight's success.
	 */
	public double fly(Airport toAirport, int aircraftIndex) {
		
		//Running cost = operationalCost * numberOfAircrafts
		double runningCost = operationalCost * aircrafts.size();
		//Running cost should be added to the expenses every time this method is called.
		double totalCost = runningCost;
		
		//You don't need to think for the edge cases, they are already handled in Aircraft.canFly inside Aircraft.fly
		
		Aircraft aircraft = aircrafts.get(aircraftIndex);
		double flightCost = aircraft.fly(toAirport);
		
		totalCost += flightCost;


//		if (flightCost>0 ) {
//			//Aircraft can fly
//			logs.add("1 " + toAirport.getID() + " " + aircraftIndex + " = " + -totalCost);
//		}
		logs.add("1 " + toAirport.getID() + " " + aircraftIndex + " = " + -totalCost);
		expenses += totalCost;
		return totalCost;
	}
	
	
	
	/**
	 * 	Every successful loading will have a loading cost, and it will be added to the expenses.
	 * <P>
	 * If the operation cannot be done, actually there need to an operationFee paid. 
	 * Instead in this method, if the operation cannot be done, don't do that operation: don't log it and don't change expenses.
	 * <P>
	 * I also modified passengersInAirport, passengersInAircraft was already handled in PassengerAircraft
	 * 
	 * @param passenger passenger that'll be loaded
	 * @param airport The airport where the passenger is
	 * @param aircraftIndex of the aicraft to load
	 * @return true if operation is successful
	 */
	public boolean loadPassenger(Passenger passenger, Airport airport, int aircraftIndex) {
		// A load operation cannot happen when aircraft and the passenger are not in the same airport.
		// if the aircraft doesn't have seats or exceeds the maximum weight limit
		// passenger can sit on the lower class seats, but priority is to sit on which class they belong.
		
		PassengerAircraft aircraft = (PassengerAircraft) aircrafts.get(aircraftIndex);

		int seatType = aircraft.canLoadPassenger(passenger);
		if (seatType >= 0) {
			double loadingFee = aircraft.loadPassenger(passenger);
			// change in the passengersInAircraft is already done in Aircraft, you should modify passengerInAirport now.
			airport.passengersInAirport.remove(passenger.getID());
			expenses += loadingFee;
			logs.add("4 " + passenger.getID() + " " + aircraftIndex + " " + airport.getID() + " = " + -loadingFee);
			
			return true;
		}
		else {
			double operationFee = aircraft.loadPassenger(passenger);
			logs.add("4 " + passenger.getID() + " " + aircraftIndex + " " + airport.getID() + " = " + -operationFee);
			
			expenses += operationFee;
		
//			logs.add("4" + passenger.getID() + " " + aircraftIndex + " " + airport.getID() + " yolcuyu ekleyemedin, operation fee : " + operationFee);
			return false;
		}
		
		
	}
	/**
	 * Ticket price should be added to the revenue in this function. 
	 * Transfers and loading operations do not generate revenue, only disembarkation does.
	 * <P>
	 * In this method, passenger is disembarked if possible. If it isn't nothing is done.
	 * 
	 * @param passenger to unload
	 * @param aircraftIndex of the aircraft
	 * @return Ticket price, if the operation is successful.
	 */
	public boolean unloadPassenger(Passenger passenger, int aircraftIndex) {
		// An unload operation cannot be done if the passenger cannot disembark at the aricraft's airport.
		
		PassengerAircraft aircraft = (PassengerAircraft)aircrafts.get(aircraftIndex);
		Airport airport = aircraft.getCurrentAirport();
		double ticketPrice = aircraft.unloadPassenger(passenger);
		
		
		// change in the passengersInAircraft is already done in Aircraft, you should modify passengerInAirport now.
		if (ticketPrice >= 0) {
			airport.passengersInAirport.put(passenger.getID(), passenger);
			revenue += ticketPrice;
			logs.add("5 " + passenger.getID() + " " + aircraftIndex + " " + airport.getID() + " = " + ticketPrice);
			return true;
		}
		
	//	logs.add("5 " + passenger.getID() + " " + aircraftIndex + " " + airport.getID() + " Yolcuyu indiremedin ");
//		expenses -= ticketPrice; // If unloading is unsuccessful, unloadPassenger returns operationFee as a negative number
//		logs.add("5 " + passenger.getID() + " " + aircraftIndex + " " + airport.getID() + " = " + ticketPrice);
		return false;
	}
	/**
	 * If passenger cannot disembark in their airport, you can use this method to transfer them to another aircraft.
	 * 
	 * @param passenger to transfer
	 * @param FromaircraftIndex of the aircraft to transfer from
	 * @param ToaircraftIndex of the aircraft to transfer from
	 */
	public void transferPassenger(Passenger passenger, int FromaircraftIndex, int ToaircraftIndex) {
		PassengerAircraft fromAircraft = (PassengerAircraft) aircrafts.get(FromaircraftIndex);
		PassengerAircraft toAircraft = (PassengerAircraft) aircrafts.get(ToaircraftIndex);
		long airportID = fromAircraft.getCurrentAirport().getID();
		double fee = fromAircraft.transferPassenger(passenger, toAircraft);
		passenger.connection(passenger.seatType);
		
		logs.add("6 " + passenger.getID() + " " + FromaircraftIndex + " " + ToaircraftIndex + " " + airportID + " = " + -fee); 
		expenses += fee;
		
	}
	
	/**
	 * Refuels the specified aircraft with the amount of fuel given. If this operation cannot be done due to
	 *  fuel and weight limitations, refuel the aircraft as much as you can.
	 * @param aircraftIndex of the aircraft to refuel
	 * @param fuel, amount of fuel you try to refuel
	 */
	public void refuel(int aircraftIndex, double fuel){
		// write:
		// the amount fuel bought, from which airport, for which aircraft, if the fuel is dumped
		// don't forget to change the weight of the aircraft, fullWeight * fuel
		Aircraft aircraft = aircrafts.get(aircraftIndex);
		
		double refuelCost = aircraft.refuel(fuel);
		double fuelAdded = refuelCost/aircraft.getCurrentAirport().getFuelCost();
		expenses += refuelCost;
		logs.add("3 " + aircraftIndex + " " + fuelAdded + " = " + (-refuelCost));
	}
	
	/**
	 * Creates an aircraft object of the specified type and adds it to the aircrafts Arraylist.
	 * If there are already as many as maxAircraftCount, then doesn't create the aircraft.
	 * <P>
	 * Possible Aircraft types: JetPassenger, PropPassenger, RapidPassenger, WidebodyPassenger
	 * 
	 * @param initialAirport of the aircraft that will be created
	 * @param typeOfAircraft 0= Prop, 1= Widebody, 2= Rapid, 3= Jet
	 */
	public void createAircraft(Airport initialAirport ,int typeOfAircraft) {
		if (aircrafts.size() >= maxAircraftCount) {
			return;
		}
		switch (typeOfAircraft) {
		case 3:
			aircrafts.add(new JetPassengerAircraft(jetOpFee, initialAirport));
			break;
		case 0:
			aircrafts.add(new PropPassengerAircraft(propOpFee, initialAirport));
			break;
		case 2:
			aircrafts.add(new RapidPassengerAircraft(rapidOpFee, initialAirport));
			break;
		case 1:
			aircrafts.add(new WidebodyPassengerAircraft(wideOpFee, initialAirport));
			break;
			
		default:
			break;
		}
		initialAirport.createdAircraftHere();
		logs.add("0 " + initialAirport.getID() + " " + typeOfAircraft + " = " + 0);
	}
	
	/**
	 * Resets the seat configuration and tries to set given seat numbers, if it cannot do the operation, all of the remaining seats are setted to economy
	 * @param aircraftIndex of the aircraft
	 * @param economySeats Number of seats you want to set in this type
	 * @param businessSeats Number of seats you want to set in this type
	 * @param firstClassSeats Number of seats you want to set in this type
	 */
	public void seatSetting(int aircraftIndex, int  economySeats, int businessSeats, int firstClassSeats) {
		PassengerAircraft aircraft = (PassengerAircraft) aircrafts.get(aircraftIndex);
		boolean settedSeats = aircraft.setSeats(economySeats, businessSeats, firstClassSeats);
		if (!settedSeats) {
			int remainingEconomy = (int) aircraft.getEmptyFloorArea() / 1; 
			aircraft.setRemainingEconomy();
			logs.add("2 " + aircraftIndex + " " + remainingEconomy + " " + 0 + " " + 0 + " " + 0 + " = " + 0);
		}
		else {
			logs.add("2 " + aircraftIndex + " " + economySeats + " " + businessSeats + " " + firstClassSeats + " = " + 0);
		}
	}
	
	/**
	 * Return the profit
	 * @return profit
	 */
	public double getProfit() {
		return revenue - expenses;
	}
	
	/**
	 * This method is used in main to read the given airports in the input. An array of Airport logs is given as input.
	 * Every airport logs is an array of parameters, which will be given to the constructor of Airport.
	 * @param logs Arrays of Airport logs
	 */
	public void giveAirports(Object[] logs) {
		Airport[] airports = new Airport[logs.length];
		for (int i = 0; i < logs.length; i++) {
			//(int ID, double x, double y, double fuelCost, double operationFee, int aircraftCapacity)
			Object[] airportParameters = (Object[]) logs[i];
			String aircraftType =  (String) airportParameters[0];
			switch (aircraftType) {
			case "hub":
				HubAirport newAirport0 = new HubAirport((long) airportParameters[1], (double) airportParameters[2], (double)airportParameters[3], (double)airportParameters[4], (double)airportParameters[5],(int) airportParameters[6]);
				airports[i] = newAirport0;
				break;
				
			case "major":
				MajorAirport newAirport1 = new MajorAirport((long) airportParameters[1], (double) airportParameters[2], (double)airportParameters[3], (double)airportParameters[4], (double)airportParameters[5],(int) airportParameters[6]);
				
				airports[i] = newAirport1;
				break;

			case "regional":
				RegionalAirport newAirport = new RegionalAirport((long) airportParameters[1], (double) airportParameters[2], (double)airportParameters[3], (double)airportParameters[4], (double)airportParameters[5],(int) airportParameters[6]);
				
				airports[i] = newAirport;
				break;
			default:
				break;
			}
		}
		
		givenAirports = airports;
	}
	
	/**
	 * Used in main to reach the airports.
	 * @return an array of airports
	 */
	public Airport[] getAirports() {
		return givenAirports;
	}
	/**
	 * This method is used in main to read the given passengers in the input. An array of Passenger logs is given as input.
	 * Every passenegr logs is an array of parameters, which will be given to the constructor of Passenger.
	 * @param logs Arrays of Passenger logs
	 */
	
	public void givePassengers(Object[] logs) {
		Passenger[] passengers = new Passenger[logs.length];
		for (int i = 0; i < logs.length; i++) {
			//(int ID, double weight, int baggageCount, ArrayList<Airport> destinations)
			Object[] passengerParameters = (Object[]) logs[i];
			String passengerType = (String) passengerParameters[0];
			switch (passengerType) {
			case "economy":
				EconomyPassenger newPassenger0 = new EconomyPassenger((long) passengerParameters[1], (int) passengerParameters[2], (int) passengerParameters[3] , (ArrayList<Long>) passengerParameters[4], (Airport) passengerParameters[5]);
				passengers[i] = newPassenger0;
				break;

			case "business":
				BusinessPassenger newPassenger1 = new BusinessPassenger((long) passengerParameters[1], (int) passengerParameters[2], (int) passengerParameters[3] , (ArrayList<Long>) passengerParameters[4], (Airport) passengerParameters[5]);
				passengers[i] = newPassenger1;
				break;
				
			case "first":
				FirstClassPassenger newPassenger2 = new FirstClassPassenger((long) passengerParameters[1], (int) passengerParameters[2], (int) passengerParameters[3] , (ArrayList<Long>) passengerParameters[4], (Airport) passengerParameters[5]);
				passengers[i] = newPassenger2;
				break;
				
			case "luxury":
				LuxuryPassenger newPassenger3 = new LuxuryPassenger((long) passengerParameters[1], (int) passengerParameters[2], (int) passengerParameters[3] , (ArrayList<Long>) passengerParameters[4], (Airport) passengerParameters[5]);
				passengers[i] = newPassenger3;
				break;
				
			default:
				break;
			}
		}
		
		givenPassengers = passengers;
	}
	
	/**
	 * Used in main to reach the passengers
	 * @return an array of airports
	 */
	public Passenger[] getPassengers() {
		return givenPassengers;
	}
	/**
	 * Used in main to reach the logs of operations.
	 * @return logs 
	 */
	
	public ArrayList<String> getLogs() {
		return logs;
	}
	
	
	/**
	 * Used in main to get Expenses
	 * @return expenses
	 */
	public double getExpense() {
		return expenses;
	}
	/**
	 * Used in main to get Revenue
	 * @return revenue
	 */
	public double getRevenue() {
		return revenue;
	}
	
	
	/**
	 * Used in main to find the Airport that has the min fuel cost.
	 * @return Airport that have the cheapest fuel.
	 */
	public Airport airportWithMinFuelCost() {
		
		Airport cheapAirport = givenAirports[0];
		double minFuelCost = cheapAirport.getFuelCost();
		for (Airport airport : givenAirports) {
			if (airport.getFuelCost() < minFuelCost) {
				cheapAirport = airport;
				minFuelCost = cheapAirport.getFuelCost();
			}
		}
		
		return cheapAirport;
	}
	
	/**
	 * To reach Airport objects with their IDs. This method is necessary, since airports are represented with their IDs in the main.
	 * @param ID of the airport
	 * @return The Airport with ID
	 */
	public Airport findAirportWithID(Long ID) {
		for (Airport airport : givenAirports) {
			if (airport.getID() == ID) {
				return airport;
			}
		}
		
		return null;
	}
	
	/**
	 * To reach Passenger objects with their IDs. This method is necessary, since passengers are represented with their IDs in the main.
	 * @param ID of the passenegr
	 * @return The Passenger with ID
	 */
	public Passenger findPassengerWithID(Long ID) {
		for (Passenger passenger : givenPassengers) {
			if (passenger.getID() == ID) {
				return passenger;
			}
		}
		
		return null;
	}
	/**
	 * Firstly finds all passengers in the fromAirport, then finds the most common destination of these passengers.
	 * @param fromAirport_ID of airport that their passengers will be checked
	 * @return Airport that is most common in the passengers destination list
	 */
	
	public Airport findTheMostCommonDestination(Long fromAirport_ID) {
	
		Airport fromAirport = findAirportWithID(fromAirport_ID);

		Map<Long, Integer> destinationCounter = new HashMap<Long, Integer>();
		for (Passenger p : givenPassengers) {
			
			for (Long airportID : p.getDestinations()) {
				if (destinationCounter.containsKey(airportID)){
					destinationCounter.put(airportID, destinationCounter.get(airportID) + 1);
				}
				else {
					destinationCounter.put(airportID, 1);
				}
			}
		}
		Long keyOfMaxValue = givenPassengers[0].getDestinations().get(0); 
		for (Map.Entry<Long, Integer> entry : destinationCounter.entrySet()) {
			Long key = entry.getKey();
			Integer val = entry.getValue();
			Integer maxValue = destinationCounter.get(keyOfMaxValue);
			if (maxValue < val) {
				keyOfMaxValue = key;
			}
		}
		
		return findAirportWithID(keyOfMaxValue);
		
	}
	
	/**
	 * 
	 * Used in main to load all passengers in the specified aicraft, who have given toAirport in their destination
	 * @param toAirport Where passengers want to go
	 * @param toAircraftIndex of aicraft
	 */
	public void loadAllPassengersThat(Airport toAirport, int toAircraftIndex) {
		Airport fromAirport = this.aircrafts.get(toAircraftIndex).getCurrentAirport();
	
		int y = 2;
		for (Passenger p : this.getPassengers()) {
			
			if (p.getDestinations().get(0) == fromAirport.getID() && p.getDestinations().contains(toAirport.getID())) {
				
				if (y<0) {
					break;
				}
				y--;
				
				this.loadPassenger(p, fromAirport, toAircraftIndex);
			}
		}
	}
	
	/**
	 * Used in main to unload all passengers in an aircraft with the given index. If the passenger doesn't want to unload in this destination, then they stay in the aircraft.
	 * @param toAircraftIndex of aircaft to unload
	 */
	public void unloadAllPassengersIn(int toAircraftIndex) {
		Aircraft aircraft = aircrafts.get(toAircraftIndex);
		ArrayList<Passenger> unloadThese = new ArrayList<>();
		
		int x = 10;
		
		for (Map.Entry<Long, Passenger> entry : aircraft.passengersInAircraft.entrySet()) {
			if (x==0) {
				break;
			}
			x--;
			
			
			Long ID = entry.getKey();
			Passenger passenger = entry.getValue();
			
			unloadThese.add(passenger);
		}
		
		for (Passenger passenger : unloadThese) {
			this.unloadPassenger(passenger, toAircraftIndex);
		}
	}
	
	/**
	 * Used in main to transfer all passengers from an aicraft to another. 
	 * @param fromAircraftIndex of aicraft to transfer from
	 * @param toAircraftIndex of aircraft to transfer to
	 */
	public void transferAllPassengersFromTo( int fromAircraftIndex, int toAircraftIndex) {
		PassengerAircraft fromCraft = (PassengerAircraft) aircrafts.get(fromAircraftIndex);
		
		ArrayList<Passenger> transferThese = new ArrayList<>();
		
		
		for (Map.Entry<Long, Passenger> entry : fromCraft.passengersInAircraft.entrySet()) {
			
			Long ID = entry.getKey();
			Passenger passenger = entry.getValue();
			
			transferThese.add(passenger);
		}
		
		for (Passenger passenger : transferThese) {
			this.transferPassenger(passenger, fromAircraftIndex, toAircraftIndex);
		}
	}
	
	/**
	 * During the whole process, only one aircraft will be used. beenThere arrayList holds the airports that this aircraft's been.
	 * This arrayList is used in main in order not to go to same places again and again.
	 */
	ArrayList<Airport> beenThere = new ArrayList<>();
	/**
	 * Finds the closest airport that any aircraft of airline haven't been to. 
	 * @param aircraftIndex of aicraft to find the closrst airport
	 * @return The Airport closesto to the given aicraft.
	 */
	public Airport findTheClosestAirport(int aircraftIndex) {
		
		Airport fromHere = aircrafts.get(aircraftIndex).getCurrentAirport();
		double minDistance = Double.MAX_VALUE;
		Airport closestAirport = null;
		for (Airport airport : givenAirports) {
			if (beenThere.contains(airport) || airport.equals(fromHere) ) {
				continue;
			}
			
			if ( fromHere.getDistance(airport) < minDistance) {
				minDistance = fromHere.getDistance(airport);
				closestAirport = airport;
			}
		}
		if ( closestAirport != null) {
			beenThere.add(closestAirport);
			beenThere.add(fromHere);
		}
		
		return closestAirport;
	}
	
	
}
