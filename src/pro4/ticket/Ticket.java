package pro4.ticket;

import java.util.ArrayList;
import java.util.stream.Stream;

import pro4.flight.Flight;

public class Ticket {
	private static ArrayList<Ticket> database = new ArrayList<>();
	
	private String user;
	private String date;
	private Flight flight;
	private int row, col;
	
	public static Ticket reserve(String user, String date, Flight flight, int row, int col) {
		Ticket r = new Ticket();
		r.user = user;
		r.date = date;
		r.flight = flight;
		r.row = row;
		r.col = col;
		database.add(r);
		return r;
	}

	public static Stream<Ticket> getAll() {
		return database.stream();
	}
	public static Stream<Ticket> getByUser(String user) {
		return database.stream().filter(r -> user.equals(r.user));
	}
	public static Ticket getByCode(int code) {
		for(Ticket r : database) {
			if(r.hashCode() == code) {
				return r;
			}
		}
		return null;
	}
	
	public void cancel() {
		database.remove(this);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(date);
		sb.append("\u001F");
		sb.append(flight.hashCode());
		sb.append("\u001F");
		sb.append(row);
		sb.append("\u001F");
		sb.append(col);
		sb.append("\u001F");
		sb.append(hashCode());
		return sb.toString();
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + col;
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + ((flight == null) ? 0 : flight.hashCode());
		result = prime * result + row;
		result = prime * result + ((user == null) ? 0 : user.hashCode());
		return result;
	}
}
