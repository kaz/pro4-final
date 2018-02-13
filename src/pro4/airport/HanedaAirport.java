package pro4.airport;

public class HanedaAirport extends Airport {
	HanedaAirport() {
		super("羽田");
		transfer.put(getClass(), 60);
		transfer.put(NaritaAirport.class, 120);
	}
}
