package project.interfacesContainer;
import project.airportContainer.Airport;

public interface AircraftInterface {
	double fly(Airport toAirport);
	void addFuel(double fuel);
	void fillUp();
	boolean hasFuel(double fuel);
	/**
	 * 
	 * @return the ratio of weight to maximum weight
	 */
	double getWeightRatio();
}
