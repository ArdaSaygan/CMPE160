package project.passengerContainer;

import java.util.ArrayList;

import project.airportContainer.Airport;
import project.airportContainer.HubAirport;
import project.airportContainer.MajorAirport;
import project.airportContainer.RegionalAirport;

public class LuxuryPassenger extends Passenger {

	
	
	private final int baggageCount; //I created this field to reach baggageCount which was kept in Passenger as private
	
	public LuxuryPassenger(long ID, double weight, int baggageCount, ArrayList<Long> destinations, Airport firstAirport) {
		super(ID, weight, baggageCount, destinations, firstAirport);
		this.baggageCount = baggageCount; //To reach the private baggageCount in Passenger

	}

	
	/**
	 * Passenger Multiplier for Luxury = 15
	 * 
	 * Multiply:
	 *  the distance between the airport of previous disembarkation and the toAirport,
	 *  aircraftTypeMultiplier, connectionMultiplier, airport multiplier, and passenger multiplier.
	 *  
	 *   For the last step, you take the ticket price above and increase it by 5% for every piece of baggage the passenger has. 
	 *  This is your final ticket price.
	 * @return ticketPrice
	 */
	@Override
	double calculateTicketPrice(Airport toAirport, double aircraftTypeMultiplier) {
		
		double airportMultiplier = giveMultiplier(toAirport);
		double passengerMultiplier = 15;
		double ticketPrice = lastAirport.getDistance(toAirport) * aircraftTypeMultiplier * connectionMultiplier * airportMultiplier * passengerMultiplier;
		
		double fivePercent = ticketPrice * (5/(double)100);
		ticketPrice += fivePercent * baggageCount;
		
		
		return ticketPrice;	
	}
	
	
}
