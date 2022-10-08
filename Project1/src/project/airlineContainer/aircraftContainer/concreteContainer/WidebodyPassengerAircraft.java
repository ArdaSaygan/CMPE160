package project.airlineContainer.aircraftContainer.concreteContainer;
import project.airlineContainer.aircraftContainer.*;
import project.airportContainer.Airport;
public class WidebodyPassengerAircraft extends PassengerAircraft {
	
	public WidebodyPassengerAircraft(double operationFee, Airport currentAirport) {
		super(450, 135000, 250000, 140000, 3.0, 0.7, operationFee, currentAirport);
		//floorArea, weight, maxWeight, fuelCapacity, fuelConsumption, aircraftTypeMultiplier
	}
	
	/**
	 * it's used in the fly function of Aircraft.java
	 * 
	 * Flight cost = landing cost(from curAirport) + departure cost(from toAirport) + flight operation cost
	 * flight operation cost = distance * fulness * 0.15
	 */
	@Override
	public double getFlightCost(Airport toAirport) {
		
		double distance = currentAirport.getDistance(toAirport);
		
		double departureCost = currentAirport.departAircraft(this);
		double landingCost = toAirport.landAircraft(this);
		this.currentAirport = toAirport;
		
		double fullness = this.getFullness();
		double flightOperationCost = distance * fullness * 0.15;
		double flightCost = landingCost + departureCost + flightOperationCost;
		
		return flightCost;
	}
	
	@Override
	public double getFuelConsumption(double distance) {
		/*
		 * returns the total fuel needed for the distance
		 * 
		 * distance ratio = distance/ 14000
		 * bathtub coefficient = y = 25.9324 x^4 - 50.5633 x^3 + 35.0554 x^2 - 9.90346 x + 1.97413 ; x=distance ratio
		 * 
		 * fuel consumption = takeoff + cruise
		 * takeoff = weight * c / fuelWeight , c=0.1
		 * cruise = fuelConsumption * bathtub coefficient * distance
		 */
		double distanceRatio = distance / 14000;
		double bathTubC = getBathTubCoefficient(distanceRatio);
		
		double c = 0.1;
		double takeOff = weight * c / fuelWeight;
		double cruise = fuelConsumption * bathTubC * distance;
		
		double fuelConsumption = takeOff + cruise;
		
		return fuelConsumption;
	}
}
