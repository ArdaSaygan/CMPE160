package project.airlineContainer.aircraftContainer;
import project.airportContainer.Airport;
import project.interfacesContainer.*;
import project.passengerContainer.*;
public abstract class PassengerAircraft extends Aircraft implements PassengerInterface{
	/**
	 * This class holds functions that are related to passenger operations for aircraft.
	 * Passenger objects should be hold in this class if they are loaded in.
	 * Passenger objects should switch between airport and aircraft objects; 
	 * they should not be stored anywhere else.
	 */
	
	/**
	 * The total floor area of the aircraft, this field is different for each type of aircraft
	 */
	protected double floorArea;
	private int economySeats, businessSeats, firstClassSeats;
	private int occupiedEconomySeats, occupiedBusinessSeats, occupiedFirstClassSeats;

	/**
	 * I created these
	 */
	public int[] seatLimits = {economySeats, businessSeats, firstClassSeats};
	public int[] occupiedSeats = {occupiedEconomySeats, occupiedBusinessSeats, occupiedFirstClassSeats};
	
 
	
	public PassengerAircraft(double floorArea, double weight, double maxWeight, double fuelCapacity, double fuelConsumption, double aircraftTypeMultiplier, double operationFee, Airport currentAirport) {
		super(operationFee, currentAirport);
		this.floorArea =  floorArea;
		this.weight = weight;
		this.maxWeight = maxWeight;
		this.fuelCapacity = fuelCapacity;
		this.fuelConsumption = fuelConsumption;
		this.aircraftTypeMultiplier = aircraftTypeMultiplier;
	}
	
	
	/*
	 * You must make sure that the total area of seats assigned does not exceed the floor area of the plane. 
	 * Areas:
	 * Economy, 1; Business, 3; First-Class, 8.
	 * 
	 * You can set and reset seats, but ensure that seat you're deleting is empty.
	 */
	
	
	/**
	 * 
	 * loading fee is based on the operationFee(comes from the Aircraft)
	 * if loading cannot be completed, return only the operationFee
	 * if loading is completed, return complete loading fee.
	 * 
	 * I return the loading fee, and modify the related variables: wieght, passengersInAircraft
	 * passengersInAirport will be modified in Airline, related occupiedSeats variable
	 * 
	 * loading fee = operationFee * aircraftTypeMultiplier* c
	 * c for every seat:
	 * first class and luxury, 2.5; business, 1.5; economy, 1.2;
	 *
		
	 * @param passenger
	 * @return the loading fee
	 */
	public double loadPassenger(Passenger passenger) {
		
		// calculate the loadingFee
		double loadingFee;
		double c;
		switch (canLoadPassenger(passenger)) {
		case 0:
			c = 1.2;
			occupiedEconomySeats++;
			break;
		case 1:
			c = 1.5;
			occupiedBusinessSeats++;
			break;
		case 2:
			c = 2.5;
			occupiedFirstClassSeats++;
			break;
		
		default:
			return operationFee;
		}
		
		loadingFee = operationFee * aircraftTypeMultiplier * c;
		
		// modify the related variables
		weight += passenger.getWeight();
		passengersInAircraft.put(passenger.getID(), passenger);

		return loadingFee;
	
		
		
	}
	/**
	 * Helper method for loadPassenger(Passenger passenger)
	 * It's also used in Airline.loadPassenger
	 * 
	 * Assumes that passengers can sit on a lower level seat.
	 * @param passenger
	 * @return -1 if operation cannot be completed. Otherwise return the seat type.
	 */
	public int canLoadPassenger(Passenger passenger) {
		/*
		 * check if the operation can be done
		 * 
		 * Think the edge cases:
		 * 1. There may be no seat with the Passenger's seat types
		 * 2. Aircraft can be full
		 * 3. MaxWeight can be exceeded
		 */
		
		int seatType = -1;
		// No seat for the passengers seat type
		if (passenger.getClass() == EconomyPassenger.class) {	 
			if (this.economySeats <= this.occupiedEconomySeats || this.economySeats == 0) {
				return -1;
			}
			seatType = 0;
		}
		if (passenger.getClass() == BusinessPassenger.class) {
			if ( this.businessSeats <= this.occupiedBusinessSeats || this.businessSeats == 0) {
				if (economySeats <= occupiedEconomySeats || this.economySeats == 0) {
					return -1;
				}
				seatType = 0;
			}
			else {
				seatType = 1;	
			}
		}
		
		if (passenger.getClass() == FirstClassPassenger.class || passenger.getClass() == LuxuryPassenger.class) {
			if ( this.firstClassSeats <=  this.occupiedFirstClassSeats || this.firstClassSeats == 0) {

				if ( this.businessSeats <= this.occupiedBusinessSeats || this.businessSeats == 0) {
					if (economySeats <= occupiedEconomySeats || this.economySeats == 0) {
						return -1;
					}
					seatType = 0;
				}
				else {
					seatType = 1;
				}
			}
			else {
				seatType = 2;
			}
			
		}
		
		// Maxweight is exceeded
		if (passenger.getWeight() + this.weight > this.maxWeight) {
			seatType = -1;
		}
		
		//Otherwise
		passenger.seatType = seatType;
		return seatType;
	}
	
	/**
	 * Can return either Revenue or Cost, Attention!
	 * If canUnloadPassenger, then calculates the ticket price and returns it.
	 * It also modifies weight and passengerInAircraft.
	 * passengerInAirport will be modified in Airline, related occupiedSeats variable
	 * 
	 * Ticket price = disembarkation revenue(given by Passenger) * the seat constant
	 * seat constant:
	 * economy: 1.0, business: 2.8, first class: 7.5	
	 * 
	 * @return ticket price if operation can be done, otherwise -operationFee(negative).
	 */
	public double unloadPassenger(Passenger passenger) {
	
		if (canUnloadPassenger(passenger)) {

			//find the ticket price
			double seatConstant = 1.0;
			switch (passenger.seatType) {
			case 0:
				occupiedEconomySeats--;
				seatConstant = 1.0;
				break;
			case 1:
				occupiedBusinessSeats--;
				seatConstant = 2.8;
				break;
			case 2:
				occupiedFirstClassSeats--;
				seatConstant = 7.5;
				break;
			default:
				break;
			}
			
			double ticketPrice = passenger.disembark(currentAirport, aircraftTypeMultiplier) * seatConstant;
			
			//modify the related variables: weight, passengersInAircraft
			weight -= passenger.getWeight();
			passengersInAircraft.remove(passenger.getID());
			return ticketPrice;
		}
		else {
			return -operationFee;
		}
	}
	/**
	 * Helper method for UnloadPassenger()
	 * @param passenger
	 * @return true if the operation can be done.
	 */
	private boolean canUnloadPassenger(Passenger passenger) {
		/*
		 * Edge Cases:
		 * 1.If the current airport is not in passengers destination list,
		 * 
		 */
	
		for (Long destinationID : passenger.getDestinations()) {
			// Can we load and unload the passengers in the same airport? If so should we take the ticket price as zero
//			if (this.currentAirport.getID() == passenger.getDestinations().get(0)) {
//				continue;
//			}
			if (currentAirport.getID() == destinationID) {
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * 
	 * if the passenger cannot disembark, you can use this method
	 * bypasses the unloadPassenger() operation
	 * 
	 * if canTransfer
	 * modify weight and passengerInAircraft of current Aircraft and toAircraft
	 * return the loading fee
	 * 
	 * @return loading fee if operation is possible, otherwise operationFee 
	 */
	public double transferPassenger(Passenger passenger, PassengerAircraft toAircraft) {
		/*
		 * the implementation is similar to loadPassenger()
		 * 
		 * if operation is invalid, return operationFee as expense
		 * if operation is valid, return the loading fee. It is the same in loadPassenger()
		 */
		if (canTransferPassenger(passenger, toAircraft)) {
			//Calculate the loading fee
			double transferingFee;
			double c = 1.2;
			if (passenger.getClass() == FirstClassPassenger.class || passenger.getClass() == LuxuryPassenger.class) {
				c = 2.5;
			}
			if (passenger.getClass() == BusinessPassenger.class) {
				c = 1.5;
			}
			if (passenger.getClass() == EconomyPassenger.class) {
				c = 1.2;
			}
			
			transferingFee = operationFee * aircraftTypeMultiplier * c;
			
			// modify the related variables
			this.weight -= passenger.getWeight();
			toAircraft.weight += passenger.getWeight();
			this.passengersInAircraft.remove(passenger.getID());
			toAircraft.passengersInAircraft.put(passenger.getID(), passenger);
			
			return transferingFee;
		}
		else {
			return operationFee;
		}
		
	}
	/**
	 * Helper method for transferPassenger()
	 * @return true if the operation is successful
	 */
	private boolean canTransferPassenger(Passenger passenger, PassengerAircraft toAircraft) {
		/*
		 * Think the edge cases:
		 * 1.If the other aircraft is already full.
		 * 2. Actually same cases with loading
		 */
		if (toAircraft.isFull()) {
			return false;
		}
		// No seat for the passengers seat type
		if (passenger.getClass() == EconomyPassenger.class) {	 
			if (toAircraft.economySeats <= toAircraft.occupiedEconomySeats) {
				return false;
			}
		}
		if (passenger.getClass() == BusinessPassenger.class) {
			if ( toAircraft.businessSeats + toAircraft.economySeats <= toAircraft.occupiedBusinessSeats + toAircraft.occupiedEconomySeats) {
				return false;
			}
		}
		if (passenger.getClass() == FirstClassPassenger.class || passenger.getClass() == FirstClassPassenger.class) {
			if ( toAircraft.businessSeats + toAircraft.economySeats + toAircraft.firstClassSeats <= toAircraft.occupiedBusinessSeats + toAircraft.occupiedEconomySeats + toAircraft.occupiedFirstClassSeats) {
				return false;
			}
		}
		
		// Maxweight is exceeded
		if (passenger.getWeight() + toAircraft.weight > toAircraft.maxWeight) {
			return false;
		}
		
		return true;
	}
	
	
	//  Methods that come from PassengerInterface
	
	/**
	 * @return true if all seats in the aircraft are occupied.
	 */
	@Override
	public boolean isFull() {
		// TODO Auto-generated method stub
		if (getFullness() == 1) {
			return true;
		}
		return false;
	}

	/**
	 * @return true if all seats with the given seatType are occupied.
	 */
	@Override
	public boolean isFull(int seatType) {
		if (occupiedSeats[seatType] == seatLimits[seatType]) {
			return true;
		}
		
		return false;
	}

	/**
	 * @return true if there is no seat assigned in the aircraft.
	 */
	@Override
	public boolean isEmpty() {
		double total = economySeats + businessSeats + firstClassSeats;
		if (total == 0) {
			return true;
		}
		return false;
	}

	@Override
	public double getAvailableWeight() {
		return maxWeight - weight;
	}
	
	/**
	 * @return the ratio of occupied seats to the total seats.
	 */
	@Override
	public double getFullness() {
		double total = economySeats + businessSeats + firstClassSeats;
		double occupied = occupiedEconomySeats + occupiedBusinessSeats + occupiedFirstClassSeats;
		return occupied/total;
	}
	
	/**
	 * Assure that floorArea is not exceeded. If operation is not possible, do not do anything.
	 * Areas:
	 * Economy, 1; Business, 3; First-Class, 8.
	 * 
	 * @return True if seat configuration is possible, false if it isn't
	 * 
	 */
	@Override
	public boolean setSeats(int economy, int business, int firstClass) {
		// TODO Auto-generated method stub
		if (getEmptyFloorArea() < 8*firstClass + 3*business + economy) {
			return false;
		}
		
		economySeats += economy;
		businessSeats += business;
		firstClassSeats += firstClass;
		return true;
	}
	
	/**
	 * 
	 * @return the unassigned floor area for seat setting
	 */
	public double getEmptyFloorArea() {
		return floorArea - occupiedEconomySeats - 3*occupiedBusinessSeats - 8*occupiedFirstClassSeats;
	}

	@Override
	public void setAllEcomomy() {
		// TODO Auto-generated method stub
		economySeats = (int) getEmptyFloorArea()/1;
		businessSeats = 0;
		firstClassSeats = 0;
	}

	@Override
	public void setAllBusiness() {
		// TODO Auto-generated method stub
		economySeats = 0;
		businessSeats = (int) getEmptyFloorArea()/3;
		firstClassSeats = 0;
	}

	@Override
	public void setAllFirstClass() {
		// TODO Auto-generated method stub
		economySeats = 0;
		businessSeats = 0;
		firstClassSeats = (int) getEmptyFloorArea()/8;
	}

	@Override
	public void setRemainingEconomy() {
		// TODO Auto-generated method stub
		economySeats += (int) getEmptyFloorArea()/1;
	}

	@Override
	public void setRemainingBusiness() {
		// TODO Auto-generated method stub
		businessSeats += (int) getEmptyFloorArea()/3;
	}

	@Override
	public void setRemainingFirstClass() {
		// TODO Auto-generated method stub
		firstClassSeats += (int) getEmptyFloorArea()/8;
	}

	/**
	 * Fuel cost is not added in this method. It only changes the numerical value of Fuel.
	 * 
	 */
	@Override
	public void addFuel(double fuel) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void fillUp() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean hasFuel(double fuel) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public double getWeightRatio() {
		// TODO Auto-generated method stub
		return weight / maxWeight;
	}
	


	
}
