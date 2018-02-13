package pro4.airport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import pro4.route.Route;
import pro4.flight.Flight;

public abstract class Airport {
	private static HashMap<Class<? extends Airport>, Airport> instances = new HashMap<>();
	public static Airport getInstance(Class<? extends Airport> c) {
		if(!instances.containsKey(c)) {
			try {
				instances.put(c, c.getDeclaredConstructor().newInstance());
			} catch(Exception e) {
				e.printStackTrace();
				System.exit(-1);
			}
		}
		return instances.get(c);
	}
	
	private static HashMap<String, Airport> str2instance = new HashMap<>();
	public static Set<String> getNames() {
		Flight.getFlights();
		return str2instance.keySet();
	}
	public static Airport getFromName(String name) {
		Flight.getFlights();
		return str2instance.get(name);
	}
	
	private String name;
	protected HashMap<Class<? extends Airport>, Integer> transfer = new HashMap<>();
	
	Airport(String name) {
		this.name = name;
		str2instance.put(name, this);
	}
	
	protected void searchRoute(ArrayList<Route> routes, int now, ArrayList<Flight> route, Airport dst, int depth) {
		if(this == dst) {
			routes.add(new Route(route));
			return;
		}
		if(depth == 0) {
			return;
		}
		for(Entry<Class<? extends Airport>, Integer> e : transfer.entrySet()) {
			Airport transferDest = getInstance(e.getKey());
			for(Flight f : Flight.getFlights()) {
				if(transferDest == f.getDeparture()) {
					int tranTime = (f.getDepartureTime() - e.getValue() + 24 * 60) % (24 * 60);
					int nextTime = (f.getDepartureTime() + f.getFlightTime()) % (24 * 60);
					
					Flight trans = new Flight(
						route.isEmpty() ? "搭乗待ち" : "乗継",
						this,
						transferDest,
						now,
						f.getDepartureTime(),
						0
					);
					if(trans.getFlightTime() < e.getValue()) {
						trans = new Flight(
							route.isEmpty() ? "搭乗待ち" : "乗継",
							this,
							transferDest,
							tranTime,
							f.getDepartureTime(),
							0
						);
					}
					
					ArrayList<Flight> nextRoute = new ArrayList<>();
					nextRoute.addAll(route);
					nextRoute.add(trans);
					nextRoute.add(f);
					f.getDestination().searchRoute(routes, nextTime, nextRoute, dst, depth - 1);
				}
			}
		}
		
	}
	public ArrayList<Route> findRoutes(Airport dst, int now) {
		ArrayList<Route> routes = new ArrayList<>();
		searchRoute(routes, now, new ArrayList<>(), dst, 4);
		return routes;
	}
	
	@Override
	public String toString() {
		return name;
	}
	@Override
	public int hashCode() {
		return name.hashCode();
	}
}
