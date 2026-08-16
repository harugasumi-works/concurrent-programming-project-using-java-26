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
import java.util.concurrent.Callable;
import java.util.concurrent.StructuredTaskScope;

public class Main  {
	
	private static final CustomJoin joiner = new CustomJoin();
	
	private static final HttpClient client = HttpClient.newBuilder()
			.version(Version.HTTP_3)
            .connectTimeout(Duration.ofSeconds(10))
            .build(); 

	@SuppressWarnings("preview")
	public static void main(String[] args) {

		List<ScanRequest> request = List.of(
				new ScanRequest("www.facebook.com"),
				new ScanRequest("www.google.com"),
				new ScanRequest("www.youtube.com"));
		
		
		List<Callable<ScanResult>> tasks = request.stream()
				.<Callable<ScanResult>>map(req -> () -> new ScanResult(req, executeScan(req)))
				.toList();
		
		try (var scope = StructuredTaskScope.open(joiner)) {
			
			tasks.stream().forEach(scope::fork) ;
			try {
				ExecutionResult results = scope.join();
				Stats stat = Stats.summarize(results);
				IO.println(stat);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}


		}
		
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
			int code = response.statusCode();
			return (code != 200) ? new Fail("Failed to make a request. Status code:" + code) : new Success(code, Duration.between(start, end).toMillis());
		} catch (IOException | InterruptedException e) {
			return new Fail("The connection was disrupted: " + e.getMessage());
		}
        
	}
	}
	


