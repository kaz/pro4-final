package pro4.airport;

public class ParisAirport extends Airport {
	ParisAirport() {
		super("パリ");
		transfer.put(getClass(), 30);
	}
}
