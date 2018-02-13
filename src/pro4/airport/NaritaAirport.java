package pro4.airport;

public class NaritaAirport extends Airport {
	NaritaAirport() {
		super("成田");
		transfer.put(getClass(), 60);
		transfer.put(HanedaAirport.class, 120);
	}
}
