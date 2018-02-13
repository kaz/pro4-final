package pro4;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

import pro4.airport.Airport;
import pro4.flight.Flight;
import pro4.route.Route;
import pro4.ticket.Ticket;

public class Server {
	public static void main(String[] args) throws Exception {
		String port = System.getenv("PORT");
		new Server().listen(port == null ? 3000 : Integer.parseInt(port));
	}

	HashMap<String, String> mime = new HashMap<>();
	HashMap<String, Function<String, Response>> handlers = new HashMap<>();
	
	private byte[] readFile(Path p) {
		try {
			return Files.readAllBytes(p);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new byte[0];
	}
	
	public Server() throws Exception {
		mime.put("ttf", "font/ttf");
		mime.put("css", "text/css");
		mime.put("js", "text/javascript");
		
		handlers.put("/", this::index);
		handlers.put("/getFlights", this::getFlights);
		handlers.put("/getAirports", this::getAirports);
		handlers.put("/findRoutes", this::findRoutes);
		handlers.put("/checkTicket", this::checkTicket);
		handlers.put("/reserveTicket", this::reserveTicket);
		handlers.put("/cancelTicket", this::cancelTicket);
	}
	
	private Response staticHandler(String path) {
		String[] ext = path.trim().split("\\.");
		String type = mime.get(ext[ext.length - 1]);
		byte[] data = readFile(Paths.get("./client/dist", path));
		return new Response(data.length > 0 ? 200 : 404, type != null ? type : "text/plain", data);
	}

	private Response index(String body) {
		return new Response(200, "text/html", readFile(Paths.get("./client/dist/index.html")));
	}
	private Response getFlights(String body) {
		String result = Arrays.stream(Flight.getFlights()).map(Flight::toString).collect(Collectors.joining("\n"));
		return new Response(200, "application/octet-stream", result.getBytes());
	}
	private Response getAirports(String body) {
		String result = Airport.getNames().stream().collect(Collectors.joining("\u001F"));
		return new Response(200, "application/octet-stream", result.getBytes());
	}
	private Response findRoutes(String body) {
		String[] data = body.split("\u001F");
		String result = Airport.getFromName(data[0]).findRoutes(Airport.getFromName(data[1]), Integer.parseInt(data[2])).stream().map(Route::toString).collect(Collectors.joining());
		return new Response(200, "application/octet-stream", result.getBytes());
	}
	private Response checkTicket(String body) {
		String result = (body.length() > 0 ? Ticket.getByUser(body.trim()) : Ticket.getAll()).map(Ticket::toString).collect(Collectors.joining("\u001E"));
		return new Response(200, "application/octet-stream", result.getBytes());
	}
	private Response reserveTicket(String body) {
		String name = null;
		for(String entry : body.split("\u001E")) {
			if(name == null) {
				name = entry;
				continue;
			}
			String[] data = entry.split("\u001F");
			Ticket.reserve(name, data[0], Flight.getFlightByCode(Integer.parseInt(data[1])), Integer.parseInt(data[2]), Integer.parseInt(data[3]));
		}
		return new Response(200, "application/octet-stream", "succeeded".getBytes());
	}
	private Response cancelTicket(String body) {
		Ticket.getByCode(Integer.parseInt(body)).cancel();
		return new Response(200, "application/octet-stream", "succeeded".getBytes());
	}
	
	public void listen(int port) throws Exception {
		@SuppressWarnings("resource")
		ServerSocket server = new ServerSocket(port);
		System.out.println("listening on " + port);
		
		while(true) {
			Socket socket = null;
			try {
				socket = server.accept();
				handleRequest(socket);
			} catch(Exception e) {
				e.printStackTrace();
			}
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	public void handleRequest(Socket socket) throws Exception {
		InputStream in = socket.getInputStream();
		OutputStream out = socket.getOutputStream();
		
		byte[] buf = new byte[4*1024*1024];
		in.read(buf);

		String req = new String(buf).trim() + "\r\n\r\n\r\n";
		String[] httpReq = req.split("\r?\n")[0].trim().split(" ");
		
		System.out.println(">>> " + String.join(" ", httpReq));
		
		Response resp;
		if(httpReq.length < 3 || !(httpReq[0].equals("GET") || httpReq[0].equals("POST"))) {
			resp = new Response(204, "application/octet-stream", new byte[0]);
		} else if(handlers.containsKey(httpReq[1])) {
			String body = req.split("(\r?\n){2}")[1].trim();
			if(body.length() > 0) {
				try {
					body = new String(Base64.getUrlDecoder().decode(body), "UTF-8");
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
			resp = handlers.get(httpReq[1]).apply(body);
			if(resp.type.equals("application/octet-stream")) {
				resp.body = Base64.getUrlEncoder().encode(resp.body);
			}
		} else if(httpReq[1].startsWith("/static/")) {
			resp = staticHandler(httpReq[1]);
		} else {
			resp = new Response(404, "text/plain", "not found".getBytes());
		}
		
		StringBuilder sb = new StringBuilder();
		String respLine = httpReq[2] + " " + resp.status;
		
		sb.append(respLine);
		sb.append("\r\n");
		sb.append("Content-Type: ");
		sb.append(resp.type);
		sb.append("\r\n");
		sb.append("Content-Length: ");
		sb.append(resp.body.length);
		sb.append("\r\n");
		sb.append("Connection: close");
		sb.append("\r\n");
		sb.append("Access-Control-Allow-Origin: *");
		sb.append("\r\n");
		sb.append("\r\n");
		
		out.write(sb.toString().getBytes());
		out.write(resp.body);
		out.flush();
		
		System.out.println("<<< " + respLine);

		out.close();
		in.close();
	}
	
	private class Response {
		public int status;
		public String type;
		public byte[] body;
		
		public Response(int status, String type, byte[] body) {
			this.status = status;
			this.type = type;
			this.body = body;
		}
	}
}
