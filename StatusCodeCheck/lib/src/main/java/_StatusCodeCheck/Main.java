package _StatusCodeCheck;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.URI;
import java.net.UnknownHostException;
import java.time.Duration;
import java.time.Instant;
import java.util.List;

public class Main {

	public static void main(String[] args) {

		List<ScanRequest> request = List.of(
				new ScanRequest("www.google.com"),
				new ScanRequest("www.facebook.com"),
				new ScanRequest("www.youtube.com"));
		
					

		// Clean Stream pipeline
		List<ScanResult> results = request.stream()
		    .map(req -> new ScanResult(req, executeScan(req)))
		    .toList();
		

	}
	
	public static Outcome testSocket(String host, int port) {		
			SocketAddress socketAddress = new InetSocketAddress(host, port);
			try (Socket sock = new Socket();) {
				Instant start = Instant.now();
				sock.connect(socketAddress, 3000);
				Instant end = Instant.now();
				return new Success(200, Duration.between(start, end).toMillis());
				} catch (UnknownHostException noHost) {
					return new Fail("Server not found: " + noHost.getMessage());
				} catch (IOException noConn) {
					return new Fail("I/O error connecting to server: " + noConn.getMessage());
				}
		}
		
		
	private static Outcome executeScan(ScanRequest req) {
	    try {
	        URI myURI = URI.create("https://" + req.URL());
	        return testSocket(myURI.getHost(), myURI.toURL().getDefaultPort());
	    } catch (MalformedURLException malf) {
	        return new Fail("The URL is malformed: " + malf.getMessage());
	    }
	}

	}
	


