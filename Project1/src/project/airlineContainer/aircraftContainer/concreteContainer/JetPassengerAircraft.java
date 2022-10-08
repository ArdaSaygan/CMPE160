package project.airlineContainer.aircraftContainer.concreteContainer;
import project.airlineContainer.aircraftContainer.*;
import project.airportContainer.Airport;
public class JetPassengerAircraft extends PassengerAircraft {

	
	public JetPassengerAircraft(double operationFee, Airport currentAirport) {
		super(30, 10000, 18000, 10000, 0.7, 5, operationFee, currentAirport);
		//floorArea, weight, maxWeight, fuelCapacity, fuelConsumption, aircraftTypeMultiplier
	}
	
	/**
	 * it's used in the fly function of Aircraft.java
	 * 
	 * Flight cost = landing cost(from curAirport) + departure cost(from toAirport) + flight operation cost
	 * flight operation cost = distance * fulness * 0.08
	 */
	@Override
	public double getFlightCost(Airport toAirport) {
	
		double landingCost = currentAirport.landAircraft(this);
		double departureCost = currentAirport.departAircraft(this);
		double distance = currentAirport.getDistance(toAirport);
		double fullness = this.getFullness();
		double flightOperationCost = distance * fullness * 0.08;
		double flightCost = landingCost + departureCost + flightOperationCost;
		
		return flightCost;
	}
	
	/**
	 * returns the total fuel needed for the distance
	 * 
	 * distance ratio = distance/ 5000
	 * bathtub coefficient = y = 25.9324 x^4 - 50.5633 x^3 + 35.0554 x^2 - 9.90346 x + 1.97413 ; x=distance ratio
	 * 
	 * fuel consumption = takeoff + cruise
	 * takeoff = weight * c / fuelWeight , c=0.1
	 * cruise = fuelConsumption * bathtub coefficient * distance
	 */
	@Override
	public double getFuelConsumption(double distance) {
	
		double distanceRatio = distance / 5000;
		double bathTubC = getBathTubCoefficient(distanceRatio);
		
		double c = 0.1;
		double takeOff = weight * c / fuelWeight;
		double cruise = fuelConsumption * bathTubC * distance;
		
		double fuelConsumption = takeOff + cruise;
		
		return fuelConsumption;
	}
}
