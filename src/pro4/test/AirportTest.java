package pro4.test;

import junit.framework.TestCase;
import pro4.airport.Airport;

public class AirportTest extends TestCase {
	
	// Airportはシングルトンであり、いかなる方法で取得したとしても、同じインスタンスが返る
	public void testSingleton() {
		for(String name : Airport.getNames()) {
			Airport fromName = Airport.getFromName(name);
			Airport fromClass = Airport.getInstance(fromName.getClass());
			assertEquals(fromName, fromClass);
		}
	}
	
	// 全ての空港の組に対して、有効な経路が少なくとも1つ存在する
	public void testRouteFinding() {
		for(String a : Airport.getNames()) {
			for(String b : Airport.getNames()) {
				if(!a.equals(b)) {
					assertTrue(Airport.getFromName(a).findRoutes(Airport.getFromName(b), 0).size() > 0);
				}
			}
		}
	}
	
}
