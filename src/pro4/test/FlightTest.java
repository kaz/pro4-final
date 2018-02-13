package pro4.test;

import junit.framework.TestCase;
import pro4.flight.Flight;

public class FlightTest extends TestCase {

	// FlightはhashCodeで一意に識別可能、同じhashCodeを持つインスタンスはただ一つのみ存在
	public void testIdentity() {
		for(Flight flight : Flight.getFlights()) {
			assertEquals(flight, Flight.getFlightByCode(flight.hashCode()));
		}
	}
	
	// 所要時間は日をまたぐ場合も正しく算出される
	public void testTimeCalc() {
		Flight a = new Flight(null, null, null, 0, 120, 0);
		assertEquals(120, a.getFlightTime());
		Flight b = new Flight(null, null, null, 240, 120, 0);
		assertEquals(1320, b.getFlightTime());
	}
	
}
