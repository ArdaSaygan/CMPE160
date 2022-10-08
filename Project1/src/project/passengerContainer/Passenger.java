package project.passengerContainer;
import java.util.ArrayList;
import java.util.Iterator;

import project.airlineContainer.aircraftContainer.*;
import project.airportContainer.*;

public abstract class Passenger {
	
	private final long ID;
	private final double weight;
	private final int baggageCount;
	/**
	 * whether a passenger can disembark or not. 
	 * Passengers can disembark at future destinations of this ArrayList. 
	 * When a passenger disembarks, arraylist shrinks.
	 */
	private ArrayList<Long> destinations;
	protected Airport lastAirport;
	
	protected double seatMultiplier;
	public int seatType=-1;
	
	public Passenger(long ID, double weight, int baggageCount, ArrayList<Long> destinations , Airport firstAirport) {
		this.ID=ID;
		this.weight = weight;
		this.baggageCount=baggageCount;
		this.destinations = destinations;
		this.lastAirport = firstAirport;
		
	}

	/**
	 * Passenger class does not need to keep track of which aircraft is in. 
	 * Instead, we need to define a new value called seatMultiplier.
	 * seatMultiplier: 
	 * Economy, 0.6; Business, 1.2; First-Class, 3.2;
	 * @param seatType
	 * @return seatMultiplier
	 */
	private double board(int seatType) {
		// set the seatMultiplier
		double[] seatMultipliers = {0.6, 1.2, 3.2};
		seatMultiplier = seatMultipliers[seatType];;
		
		// modify the related variables
		
		return seatMultiplier;
	}
	
	/**
	 * Disembarks the passenger to the airport. Returns the ticket price. 
	 * This method will be where ticket price is calculated and returned.
	 * If the airport is not a future destination, returns 0 and not perform any operation. 
	 * 
	 * @param airport
	 * @param aircraftTypeMultiplier 
	 * @return the ticket price as double, 0 if the airport is not in the future destinations.
	 */
	public double disembark(Airport airport, double aircraftTypeMultiplier) {
	
		boolean flightIsPossible = false;
		// Check if the airport is in a future destination
		for (Long airportID : destinations) {
			if (airportID == airport.getID()) {
				flightIsPossible = true;
				break;
			}
		}
		
		if (flightIsPossible) {
			// calculate the ticket price
			double ticketPrice = calculateTicketPrice(airport, aircraftTypeMultiplier) * board(seatType);

			//update lastAirport
			lastAirport = airport;
			
			// update destinatios
			// Removes all the destinations before the airport from the destinations ArrayList
			Iterator iter = destinations.iterator();
			while(iter.hasNext()) {
				Object cur = iter.next();
				if (cur.equals(airport)) {
					break;
				}
				iter.remove();
			}
			
			// reset the necessary multipliers
			connectionMultiplier = 1;
			// ? ? ? ? ? ? ? ? ? ?
			
			
			return ticketPrice;
		}
		else {
			return 0;
		}
		
		
		
	}
	
	/**
	 * Transfers the passengers to another plane, connects the flight.
	 * seatMultiplier should be multiplied with __ if the seat is:
	 * Economy, 0.6; Business, 1.2; First-Class, 3.2
	 * @param seatType
	 * @return
	 */
	public boolean connection(int seatType) {
		double[] multiplyWith = {0.6, 1.2, 3.2};
		seatMultiplier *= multiplyWith[seatType];
		connectionMultiplier *= 0.8;
		return false;
	}
	/**
	 * This value will be used when calculating ticket prices. 
	 * Each time a connection is made, connection multiplier is multiplied with 0.8.
	 */
	double connectionMultiplier = 1;
	
	/**
	 * This method is called in the embark() method.
	 * @param airport
	 * @param aircraftTypeMultiplier
	 * @return the ticket price
	 */
	abstract double calculateTicketPrice(Airport toAirport, double aircraftTypeMultiplier);

	
	protected double giveMultiplier(Airport toAirport) {
		if (lastAirport.getClass() == HubAirport.class) {
			
			if (toAirport.getClass() == HubAirport.class) {
				return 0.5;
			}
			if (toAirport.getClass() == MajorAirport.class) {
				return 0.7;
			}
			if (toAirport.getClass() == RegionalAirport.class) {
				return 1.0;
			}
		}
		
		if (lastAirport.getClass() == MajorAirport.class) {
			
			if (toAirport.getClass() == HubAirport.class) {
				return 0.6;
			}
			if (toAirport.getClass() == MajorAirport.class) {
				return 0.8;
			}
			if (toAirport.getClass() == RegionalAirport.class) {
				return 1.8;
			}
		}
		
		if (lastAirport.getClass() == RegionalAirport.class) {
			
			if (toAirport.getClass() == HubAirport.class) {
				return 0.9;
			}
			if (toAirport.getClass() == MajorAirport.class) {
				return 1.6;
			}
			if (toAirport.getClass() == RegionalAirport.class) {
				return 3.0;
			}
		}
		
		return 0;
		
	}
	
	/*
	 * Getters
	 */
	
	public double getWeight() {
		return weight;
	}
	public long getID() {
		return ID;
	}
	
	/**
	 * 
	 * @return An Arraylist<Long> of the airports in the passengers destination
	 */
	public ArrayList<Long> getDestinations() {
		return destinations;
	}
}
