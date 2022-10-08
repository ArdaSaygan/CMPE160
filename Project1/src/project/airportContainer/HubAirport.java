package project.airportContainer;

import project.airlineContainer.aircraftContainer.Aircraft;

public class HubAirport extends Airport {

	public HubAirport(long ID, double x, double y, double fuelCost, double operationFee, int aircraftCapacity) {
		super(ID, x, y, fuelCost, operationFee, aircraftCapacity);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Does the necessary departure operations and returns the departure fee.
	 * 
	 * 
	 * define two variables: fullness coefficient and weight ratio
	 * fullness coefficient = 0.6*e^(aircraftRatio)
	 * aircraftRatio = numberOfAircrafts / total capacity
	 * aircraftWeightRatio = weightOfAircraft / maxWeight
	 * 
	 * departure fee = operationFee * weightRatio * fullnessCoefficient * 0.7
	 * 
	 * I also modified numberOfAircrafts in Airport.
	 * 	 
	 * @param aircraft to departure 
	 * @return departure fee as double
	 */
	@Override
	public double departAircraft(Aircraft aircraft) {
		
		// find the departure fee
		double aircraftRatio = numberOfAircrafts / (double)aircraftCapacity;
		double fullnessCoefficient = 0.6* Math.pow(Math.E , aircraftRatio);
		double aircraftWeightRatio = aircraft.getWeightRatio();
		
		double departureFee = operationFee * aircraftWeightRatio * fullnessCoefficient * 0.7;
		
		// modify the related variables
		numberOfAircrafts --;
		
		
		return departureFee;
	}
	
	/**
	 * Does the necessary landing operations and returns the landing fee.
	 * 
	 * 
	 * all variables are defined the same way in departure 
	 * landing fee = operationFee * weightRatio * fullnessCoefficient * 0.8
	 * 
	 * I also modified numberOfAircrafts in Airport.
	 * 		
	 * @param aircraft to land 
	 * @return landing fee as double
	 */
	@Override
	public double landAircraft(Aircraft aircraft) {
		
		
		// find the landing fee
		double aircraftRatio = numberOfAircrafts / (double)aircraftCapacity;
		double fullnessCoefficient = 0.6 * Math.pow(Math.E, aircraftRatio);
		double aircraftWeightRatio = aircraft.getWeightRatio();
		
		double landingFee = operationFee * aircraftWeightRatio * fullnessCoefficient * 0.8;
		
		// modify the related variables
		numberOfAircrafts ++;
		
		return landingFee;
		
	}

}
