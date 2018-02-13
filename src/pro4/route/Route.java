package pro4.route;

import java.util.ArrayList;

import pro4.flight.Flight;

public class Route {
	private ArrayList<Flight> flights;
	
	public Route(ArrayList<Flight> flights) {
		this.flights = flights;
	}

	public int getTime() {
		return flights.stream().map(f -> f.getFlightTime()).reduce(0, (a, b) -> a + b);
	}
	public int getMiles() {
		return flights.stream().map(f -> f.getMiles()).reduce(0, (a, b) -> a + b);
	}
	public int getFare() {
		int now = 0;
		int fare = 0;
		int miles = 0;
		for(Flight f : flights) {
			if(f.getDepartureTime() < now) {
				fare += calcFare(miles);
				miles = 0;
			}
			miles += f.getMiles();
			now = f.getDepartureTime();
		}
		return fare + calcFare(miles);
	}
	private int calcFare(int miles) {
		if(miles <  301) return  18000;
		if(miles < 1001) return  25000;
		if(miles < 5700) return 100000;
		else             return 120000;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("----- route -----\n");
		sb.append("Time: ");
		sb.append(getTime());
		sb.append(" min\n");
		sb.append("Miles: ");
		sb.append(getMiles());
		sb.append(" Km\n");
		sb.append("Fare: ");
		sb.append(getFare());
		sb.append(" JPY\n");
		flights.stream().forEach(f -> {
			sb.append(f);
			sb.append("\n");
		});
		return sb.toString();
	}
}
