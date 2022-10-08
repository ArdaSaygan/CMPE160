package project.airlineContainer.aircraftContainer.concreteContainer;
import project.airlineContainer.aircraftContainer.*;
import project.airportContainer.*;
public class PropPassengerAircraft extends PassengerAircraft {
	
	public PropPassengerAircraft(double operationFee, Airport currentAirport) {
		super(60, 14000, 23000, 6000, 0.6, 0.9, operationFee, currentAirport);
		//floorArea, weight, maxWeight, fuelCapacity, fuelConsumption, aircraftTypeMultiplier
	}
	
	/**
	 * it's used in Aircraft.fly()
	 * 
	 * Flight cost = landing cost(from curAirport) + departure cost(from toAirport) + flight operation cost
	 * flight operation cost = distance * fulness * 0.1
	 * 
	 * @return flight cost
	 */
	@Override
	public double getFlightCost(Airport toAirport) {
		
		double landingCost = currentAirport.landAircraft(this);
		double departureCost = toAirport.departAircraft(this);
		double distance = currentAirport.getDistance(toAirport);
		double fullness = this.getFullness();
		double flightOperationCost = distance * fullness * 0.1;
		double flightCost = landingCost + departureCost + flightOperationCost;
		
		return flightCost;
	}
	
	/**
	 * returns the total fuel needed for the distance
	 * It's used in Airport.fly(...)
	 * 
	 * distance ratio = distance/ 2000
	 * bathtub coefficient = y = 25.9324 x^4 - 50.5633 x^3 + 35.0554 x^2 - 9.90346 x + 1.97413 ; x=distance ratio
	 * 
	 * fuel consumption = takeoff + cruise
	 * takeoff = weight * c / fuelWeight , c=0.08
	 * cruise = fuelConsumption * bathtub coefficient * distance
	 * 
	 * @return fuelConsumption
	 */
	@Override
	public double getFuelConsumption(double distance) {

		double distanceRatio = distance / 2000;
		double bathTubC = getBathTubCoefficient(distanceRatio);
		
		double c = 0.08;
		double takeOff = weight * c / fuelWeight;
		double cruise = fuelConsumption * bathTubC * distance;
		
		double fuelConsumption = takeOff + cruise;
		
		return fuelConsumption;
		
	}


	



}
