package _StatusCheck;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Version;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.Instant;
import java.util.List;

public class Main {
	
	private static final HttpClient client = HttpClient.newBuilder()
			.version(Version.HTTP_3)
            .connectTimeout(Duration.ofSeconds(10))
            .build(); 

	public static void main(String[] args) {

		List<ScanRequest> request = List.of(
				new ScanRequest("www.facebook.com/"));
		
					
		List<ScanResult> results = request.stream()
		    .map(req -> new ScanResult(req, executeScan(req)))
		    .toList();
		
		IO.println(results);
	}
	
	
	private static Outcome executeScan(ScanRequest req) {
        HttpRequest request = HttpRequest.newBuilder()
        		 .uri(URI.create("https://" + req.URL()))
        	        .timeout(Duration.ofSeconds(5))
        	        .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36")
        	        .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8")
        	        .header("Accept-Language", "en-US,en;q=0.9")
        	        .GET()
        	        .build();

		try {
			Instant start = Instant.now();
			HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
			Instant end = Instant.now();
			return new Success(response.statusCode(), Duration.between(start, end).toMillis());
		} catch (IOException | InterruptedException e) {
			return new Fail("The connection was disrupted: " + e.getMessage());
		}
        
	}
	}
	


