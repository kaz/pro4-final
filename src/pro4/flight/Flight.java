package pro4.flight;

import pro4.airport.*;

public class Flight {
	private static Flight[] flights = new Flight[] {
		new Flight(
			"1",
			Airport.getInstance(NaritaAirport.class),
			Airport.getInstance(AmsterdamAirport.class),
			60 * 10 + 25,
			60 * 15 + 10,
			5700
		),
		new Flight(
			"2",
			Airport.getInstance(AmsterdamAirport.class),
			Airport.getInstance(NaritaAirport.class),
			60 * 14 + 40,
			60 *  8 + 45,
			5700
		),
		new Flight(
			"3",
			Airport.getInstance(HanedaAirport.class),
			Airport.getInstance(ParisAirport.class),
			60 * 14 +  0,
			60 * 16 + 40,
			5800
		),
		new Flight(
			"4",
			Airport.getInstance(ParisAirport.class),
			Airport.getInstance(HanedaAirport.class),
			60 * 19 +  0,
			60 * 13 + 30,
			5800
		),
		new Flight(
			"11",
			Airport.getInstance(AmsterdamAirport.class),
			Airport.getInstance(ParisAirport.class),
			60 * 13 +  0,
			60 * 14 + 20,
			300
		),
		new Flight(
			"1",
			Airport.getInstance(AmsterdamAirport.class),
			Airport.getInstance(ParisAirport.class),
			60 * 16 + 55,
			60 * 18 +  5,
			300
		),
		new Flight(
			"2",
			Airport.getInstance(ParisAirport.class),
			Airport.getInstance(AmsterdamAirport.class),
			60 * 11 + 45,
			60 * 13 +  5,
			300
		),
		new Flight(
			"12",
			Airport.getInstance(ParisAirport.class),
			Airport.getInstance(AmsterdamAirport.class),
			60 * 17 + 20,
			60 * 18 + 40,
			300
		),
	};
	public static Flight[] getFlights() {
		return flights;
	}
	public static Flight getFlightByCode(int code) {
		for(Flight f : flights) {
			if(f.hashCode() == code) {
				return f;
			}
		}
		return null;
	}
	
	private String flightNumber;
	private Airport departure;
	private Airport destination;
	private int departureTime;
	private int flightTime;
	private int miles;
	
	public Flight(String flightNumber, Airport departure, Airport destination, int departureTime, int eta, int miles) {
		this.flightNumber = flightNumber;
		this.departure = departure;
		this.destination = destination;
		this.departureTime = departureTime;
		this.flightTime = (eta - departureTime + 24 * 60) % (24 * 60);
		this.miles = miles;
	}
	
	public String getFlightNumber() {
		return flightNumber;
	}
	public Airport getDeparture() {
		return departure;
	}
	public Airport getDestination() {
		return destination;
	}
	public int getDepartureTime() {
		return departureTime;
	}
	public int getFlightTime() {
		return flightTime;
	}
	public int getMiles() {
		return miles;
	}
	
	@Override
	public String toString() {
		return
			" no=" + flightNumber +
			" dept=" + departure +
			" dest=" + destination +
			" dtime=" + departureTime +
			" ftime=" + flightTime +
			" mile=" + miles +
			" code=" + hashCode();
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((departure == null) ? 0 : departure.hashCode());
		result = prime * result + departureTime;
		result = prime * result + ((destination == null) ? 0 : destination.hashCode());
		result = prime * result + ((flightNumber == null) ? 0 : flightNumber.hashCode());
		result = prime * result + flightTime;
		result = prime * result + miles;
		return result;
	}
}
