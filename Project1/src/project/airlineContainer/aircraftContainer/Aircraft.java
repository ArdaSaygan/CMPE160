package project.airlineContainer.aircraftContainer;
import java.util.HashMap;

import project.airportContainer.*;
import project.interfacesContainer.*;
import project.passengerContainer.Passenger;
public abstract class Aircraft implements AircraftInterface {
	
	protected Airport currentAirport;
	protected double weight;
	protected double maxWeight;
	/**
	 *  it is the constant we use to convert fuel volume to fuel weight.
	 */
	protected double fuelWeight = 0.7;
	/**
	 * This field holds the amount of fuel the aircraft has at any moment.
	 * Should not exceed fuelCapacity
	 * It is in terms of volume.
	 */
	protected double fuel = 0;
	/**
	 * It's different and fixed for every type of aircraft.
	 * It is in terms of volume.
	 */
	protected double fuelCapacity;
	
	protected double fuelConsumption;
	protected double aircraftTypeMultiplier;
	/**
	 * There are two operation fees in the documentation, one for Airport, one for Aircraft.
	 * It will be given in the constructor, specific to each aircraft.
	 * OperationFee for Aircraft is not clearly mentioned in the documentation.
	 * 
	 * It's used in PassengerAircraft.loadPassenger()
	 */
	protected double operationFee;
	
	/**
	 * Passengers are stored both in Airport and Aircarfts in a HashMap. Their ID will be the key.
	 * <P>
	 * This method is public because it'll be modified in other classes.
	 */
	public HashMap<Long, Passenger> passengersInAircraft = new HashMap<>();
	
	//These two methods will be implemented in each Concrete Aircraft Class
	public abstract double getFlightCost(Airport toAirport);
	public abstract double getFuelConsumption(double distance);
	
	public Aircraft(double operationFee, Airport currentAirport) {
		this.operationFee = operationFee;
		this.currentAirport = currentAirport;
	}
	
	/**
	 * does the necessary fuel calculations using getFuelConsumption() and return the flight cost
	 * It's called in Airline.fly(...) 
	 * 
	 * I check if the flight is possible,
	 * if so, I reduced the fuel, weight and return the flight cost, 
	 * 
	 * 
	 * @return Flight cost as double, 0 if the operation cannot be done.
	 */
	@Override
	public double fly(Airport toAirport) { 
	
		if (this.canFly(toAirport)) {
			double distance = currentAirport.getDistance(toAirport);
			double flightCost = getFlightCost(toAirport);
			
			// Modify the related variables
			fuel -= getFuelConsumption(distance);
			weight -= getFuelConsumption(distance) * fuelWeight;

			return flightCost;
		}
		else {
			return 0;
		}
	}
	/**
	 * Helper method for fly(Airport toAirport)
	 * @param toAirport
	 * @return true if the flight is possible
	 */
	private boolean canFly(Airport toAirport) {
		/*
		 * check if the flight is possible
		 */ 
		 
		// If fromAirport == toAirport
		if (this.currentAirport.getID() == toAirport.getID()) {
			return false;
		}
		// If toAirport capacity is already full.
		if (toAirport.isFull()) {
			return false;
		}
		// Becuase of the aircrafts range limitations.
		
		// If there's not enough fuel
		double neededFuel = this.getFuelConsumption(currentAirport.getDistance(toAirport)); 
		if (this.fuel < neededFuel) {
			return false;
		}
		
		return true;
	}
	
	/**
	 * Refuels the aircraft. This method will be used by the matching methods in the Airline.
	 * 
	 * If operation cannot be completed due to MaxWeight or FuelCapacity, then refuel the aircraft as much as you can.
	 * 
	 * @param fuelAmount amount of fuel that will be added in terms of volume.
	 * @return the fuel cost as double
	 */
	public double refuel(double fuelAmount) {
		
		double fuelThatWillBeAdded = fuelAmount;
		
		double weightOfFuel = fuelAmount * fuelWeight;
		
		// If operation is not possible, refuel as much as you can
		if (weight + weightOfFuel > maxWeight || fuel + fuelAmount > fuelCapacity ) {
			fuelThatWillBeAdded =  Math.min( (maxWeight - weight)/fuelWeight , fuelCapacity - fuel); 
			if (fuelThatWillBeAdded <0) {
				fuelThatWillBeAdded = 0;
			}
		}
		
		// Modify the weight of the aircraft and other related variables
		weight += fuelThatWillBeAdded*fuelWeight;
		fuel += fuelThatWillBeAdded;
		
		
		double fuelCost = fuelThatWillBeAdded * currentAirport.getFuelCost();
		
		return fuelCost;
	}

	/**
	 * Used in concreteAircraftClasses inside getFuelConsumption()
	 * Uses the polynomial: 
	 * y = 25.9324 x^4 - 50.5633 x^3 + 35.0554 x^2 - 9.90346 x + 1.97413 ; x=distance ratio
	 * 
	 * @param distanceRatio
	 * @return BathTubConstant
	 */
	public double getBathTubCoefficient(double distanceRatio) {
		double x = distanceRatio;
		double y = 25.9324*x*x*x*x - 50.5633*x*x*x + 35.0554*x*x - 9.90346*x + 1.97413;
		return y;
	}
	
	public Airport getCurrentAirport() {
		return currentAirport;
	}
	
	public double getAircraftTypeMultiplier() {
		return aircraftTypeMultiplier;
	}
	
}
