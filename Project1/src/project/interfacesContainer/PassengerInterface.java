package project.interfacesContainer;
import project.airlineContainer.aircraftContainer.*;
import project.passengerContainer.*;

public interface PassengerInterface {
	// you can add more methods if you want
	
	double transferPassenger(Passenger passenger, PassengerAircraft toAircraft);
	double loadPassenger(Passenger passenger);
	double unloadPassenger(Passenger passenger);
	/**
	 * 
	 * @return true if the aircraft is full
	 */
	boolean isFull();
	/**
	 * 
	 * @param seatType
	 * @return true if the given seat type is full
	 */
	boolean isFull(int seatType);
	
	boolean isEmpty();
	/**
	 * 
	 * @return Returns the leftover weight capacity of the aircraft.
	 */
	public double getAvailableWeight();
	
	
	public boolean setSeats(int economy, int business, int firstClass);
	/**
	 * Sets every seat to economy
	 * @return
	 */
	public void setAllEcomomy();
	/**
	 * Sets every seat to business
	 * @return
	 */
	public void setAllBusiness();
	/**
	 * Sets every seat to first class
	 * @return
	 */
	public void setAllFirstClass();
	
	/**
	 * Does not change previously set seats, sets the remaining to economy.
	 * @return
	 */
	public void setRemainingEconomy();
	/**
	 * Does not change previously set seats, sets the remaining to business.
	 * @return
	 */
	public void setRemainingBusiness();
	/**
	 * Does not change previously set seats, sets the remaining to first class.
	 * @return
	 */
	public void setRemainingFirstClass();
	
	/**
	 * 
	 * @return Returns the ratio of occupied seats to all seats.
	 */
	public double getFullness();
	
}
