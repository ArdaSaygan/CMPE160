package project.airportContainer;
import java.util.HashMap;

import project.airlineContainer.aircraftContainer.*;
import project.passengerContainer.Passenger;

public abstract class Airport {
	private final long ID;
	private final double x,y;
	protected double fuelCost;
	protected double operationFee;
	
	/**
	 * This variable should be modified in every landing and departure operation. 
	 * It's used in Airline.fly() to calculate the running cost
	 */
	protected int numberOfAircrafts=0;
	
	public int getNumberOfAircrafts() {
		return numberOfAircrafts;
	}
	
	public Airport(long ID, double x, double y, double fuelCost, double operationFee, int aircraftCapacity) {
		this.ID = ID;
		this.x=x;
		this.y=y;
		this.fuelCost = fuelCost;
		this.operationFee = operationFee;
		this.aircraftCapacity = aircraftCapacity;
	}
	
	/**
	 * Maximum number of aircrafts this airport can hold
	 */
	protected int aircraftCapacity;
	
	/**
	 * Passengers are stored both in Airport and Aircarfts in a HashMap. Their ID will be the key.
	 */
	public HashMap<Long, Passenger> passengersInAirport = new HashMap<>();
	
	/**
	 * Does the necessary departure operations and returns the departure fee.
	 * @param aircraft to departure from
	 * @return departure fee as double
	 */
	public abstract double departAircraft(Aircraft aircraft); //modify aircrafts' current Airports
	/**
	 * Does the necessary landing operations and returns the landing fee.
	 * @param aircraft to land to
	 * @return landing fee as double
	 */
	public abstract double landAircraft(Aircraft aircraft); //modify aircrafts' current Airports
	
	/**
	 * When an aircraft is created, you should modify the number  of aircrafts in that airline.
	 * </p>
	 * Called in Airline.createAircraft
	 */
	public void createdAircraftHere() {
		numberOfAircrafts ++;
	}
	
	// you can add helper methods...
	
	/**
	 * Will be used to calculate the distance between two Airports
	 * @return an array of size two, [x, y] 
	 */
	public double[] getCoordinates() {
		double[] coordinates = {x,y};
		return coordinates;
	}
	
	public double getDistance(Airport toAirport){
		double toApX = toAirport.getCoordinates()[0];
		double toApY = toAirport.getCoordinates()[1];
		
		double res = Math.sqrt( Math.pow(x-toApX, 2) + Math.pow(y-toApY, 2) );
		return res;
	}
	
	public double getFuelCost() {
		return fuelCost;
	}
	public long getID() {
		return ID;
	}
	
	public boolean isFull() {
		if (numberOfAircrafts >= aircraftCapacity) {
			return true;
		}
		return false;
	}
}
