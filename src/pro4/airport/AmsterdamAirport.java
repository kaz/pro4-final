package pro4.airport;

public class AmsterdamAirport extends Airport {
	AmsterdamAirport() {
		super("アムステルダム");
		transfer.put(getClass(), 30);
	}
}
