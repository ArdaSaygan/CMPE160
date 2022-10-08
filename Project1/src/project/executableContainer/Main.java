package project.executableContainer;
import java.io.File;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.Set;

import project.airlineContainer.*;
import project.airportContainer.Airport;
import project.passengerContainer.EconomyPassenger;
import project.passengerContainer.LuxuryPassenger;
import project.passengerContainer.Passenger;
public class Main {

	public static void main(String[] args) throws IOException{
		
		/*
		 * Main class consist of 3 parts:
		 * 1) Input Taking
		 * 2) Algorithm
		 * 3) Output Writing
		 */
		
		// 1) INPUT TAKING <<<<<<<<
		
		//Let's first read our input.
		int M, A, P;
		double prop, widebody, rapid, jet, operationCost;

		
		String inputFileName = "input.txt";
		
		File input = new File(inputFileName);
		Scanner sc = new Scanner(input);
		
		M=sc.nextInt();
		A=sc.nextInt();
		P=sc.nextInt();
		prop=sc.nextDouble();
		widebody=sc.nextDouble();
		rapid=sc.nextDouble();
		jet=sc.nextDouble();
		operationCost=sc.nextDouble();
		sc.nextLine();
		Airline airline = new Airline(M , operationCost, prop, widebody, rapid, jet);
		
		/*
		 *  Now let's reach the Airports
		 *  To do this an array of parameters is created, every parameter Airport's constructor requires is hold in this array.
		 *  logsAirport is an array of these arrays.
		 */
		
		Object[] logsAirport = new Object[A];
		for (int i = 0; i < A; i++) {
			
			String line = sc.nextLine().replaceAll(",", "");
			
			String[] elements = line.split(" ");
			
			
			String airportType = elements[0];
			long ID = Long.parseLong(elements[2]);
			double x = Double.parseDouble(elements[3]);
			double y = Double.parseDouble(elements[4]);
			double fuelCost = Double.parseDouble(elements[5]);
			double operationFee = Double.parseDouble(elements[6]);
			int aircraftCapacity = Integer.parseInt(elements[7]);
			
			Object[] temp = {airportType,ID, x, y, fuelCost, operationFee, aircraftCapacity};
			logsAirport[i] = temp;
		}
		/*
		 * This method reads the airport logs and creates Airport objects in the airline 
		 */
		airline.giveAirports(logsAirport);
		
		/*
		 *  Now let's reach the Passengers
		 *  To do this an array of parameters is created, every parameter Passenger's constructor requires is hold in this array.
		 *  logsPassengers is an array of these arrays.
		 */
		Object[] logsPassengers = new Object[P];
		for (int i = 0; i < P; i++) {
			
			String line = sc.nextLine().replaceAll(",", "").replaceAll("\\[", "").replaceAll("\\]", "");
			
			String[] elements = line.split(" ");
			
			
			String passengerType = elements[0];
			long ID = Long.parseLong(elements[2]);
			int weight = Integer.parseInt(elements[3]);
			int baggageCount = Integer.parseInt(elements[4]);
			ArrayList<Long> destinations = new ArrayList<Long>();
			for (int j = 5; j < elements.length ; j++) {

				destinations.add( Long.parseLong(elements[j]));
			}
			Airport firstAirport = null;
					
			for (Airport airport  : airline.getAirports()) {
				if (airport.getID() == destinations.get(0)) {
					firstAirport = airport;
				}
			}
			
			Object[] temp = {passengerType, ID, weight, baggageCount, destinations, firstAirport};
			logsPassengers[i] = temp;
		}
		/*
		 * This method reads the passenger logs and creates Passenger objects in the airline 
		 */
		airline.givePassengers(logsPassengers);
		
		
		
		// INPUTS HAVE BEEN TAKEN SUCCESSFULLY !
		
		// 2) ALGORITHM <<<<<<<<
		
		/*
		 * My strategy here is to work with only one aircraft and try to disembrak at least one passenger.
		 * These are my steps:
		 * 1) Find the airport with the cheapest fuel
		 * 2) Refuel as much as you can
		 * 3) Set the seats
		 * 4) Load the passengers in the airport and load as much as you can, their types are not important
		 * 5) Until the aircraft has less passengers than a given number, continue to do this:
		 *    - refuel you aircraft
		 *    - find the closest airport that you've never been to
		 *    - try to unload people
		 *    
		 */
		
		
		
		if (airline.airportWithMinFuelCost().passengersInAirport.size() != 0) {
			airline.createAircraft(airline.airportWithMinFuelCost(), 1);
		}
		else {
			airline.createAircraft(airline.findAirportWithID( airline.getPassengers()[0].getDestinations().get(0) ), 1);
		}
		
		airline.refuel(0, 1000000);
		airline.seatSetting(0, 20, 20, 4);
		
		airline.loadAllPassengersThat(airline.findTheMostCommonDestination(airline.airportWithMinFuelCost().getID()), 0);

		
//		airline.fly(airline.findTheMostCommonDestination(airline.airportWithMinFuelCost().getID()) , 0);
		//airline.unloadAllPassengersIn(0); 
		
		while (airline.aircrafts.get(0).passengersInAircraft.size() > 0 ) {
			
		
			
			airline.refuel(0, 1000000);
			
			airline.fly( airline.findTheClosestAirport(0), 0);
			
			airline.unloadAllPassengersIn(0);
		}
		
		
		
		// ALGORITHM IS OVER !
		
		// 3) OUTPUT WRITING <<<<<<<<
		
		// Now let's write the logs to the output file
		

		FileWriter fw = new FileWriter("output.txt");
		
		for (String log: airline.getLogs()) {
			System.out.println(log);
			fw.write(log);
			fw.write("\n");
		}
		fw.write( Double.toString(airline.getProfit()));
		fw.flush();
		System.out.println("Profit: " + airline.getProfit());
		System.out.println("Revenue " + airline.getRevenue());
		System.out.println("Expense " + airline.getExpense());
		

		
		
		
		
	}
	
	
	
	
	
	

	

	
	
}
