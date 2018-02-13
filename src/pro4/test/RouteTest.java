package pro4.test;

import java.util.ArrayList;

import junit.framework.TestCase;
import pro4.flight.Flight;
import pro4.route.Route;

public class RouteTest extends TestCase {

	// 総マイル数は正しく計算される
	public void testTotalMiles() {
		ArrayList<Flight> flights = new ArrayList<>();
		assertEquals(0, new Route(flights).getMiles());
		flights.add(new Flight(null, null, null, 0, 0, 100));
		assertEquals(100, new Route(flights).getMiles());
		flights.add(new Flight(null, null, null, 0, 0, 200));
		assertEquals(300, new Route(flights).getMiles());
	}
	
	// 総所要時間は正しく計算される
	public void testTotalTime() {
		ArrayList<Flight> flights = new ArrayList<>();
		assertEquals(0, new Route(flights).getTime());
		flights.add(new Flight(null, null, null, 0, 120, 0));
		assertEquals(120, new Route(flights).getTime());
		flights.add(new Flight(null, null, null, 240, 120, 0));
		assertEquals(1440, new Route(flights).getTime());
	}
	
	// 運賃は正しく計算される
	public void testFare() {
		ArrayList<Flight> flights = new ArrayList<>();
		assertEquals(18000, new Route(flights).getFare());
		flights.add(new Flight(null, null, null, 0, 1, 900));
		assertEquals(25000, new Route(flights).getFare());
		flights.add(new Flight(null, null, null, 1, 2, 4000));
		assertEquals(100000, new Route(flights).getFare());
		flights.add(new Flight(null, null, null, 2, 3, 4000));
		assertEquals(120000, new Route(flights).getFare());
		flights.add(new Flight(null, null, null, 0, 1, 100));
		assertEquals(138000, new Route(flights).getFare());
	}
	
}
