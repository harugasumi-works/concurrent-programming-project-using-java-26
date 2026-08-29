package _StatusCheck;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Version;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.StructuredTaskScope;

public class Operator {
	
	private static final CustomJoin joiner = new CustomJoin();
	private static List<Callable<ScanResult>> tasks = new ArrayList<>();
	public static List<ScanRequest> requestList = new ArrayList<>();
	public static LazyConstant<JSON> json = null;
	public static LazyConstant<CSV> csv = null;
	


	
	private static final HttpClient client = HttpClient.newBuilder()
			.version(Version.HTTP_3)
            .connectTimeout(Duration.ofSeconds(10))
            .build(); 

	private static Outcome scanOperator(ScanRequest req) {
        HttpRequest request = HttpRequest.newBuilder()
        		 .uri(URI.create("https://" + req.requestedURL()))
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
			return switch(code) {
			case int c when (c < 400) -> new Success(start, code, Duration.between(start, end).toMillis());
			case int c when (c >= 400 && c < 500) -> new Fail(start, code,"Client failed to make a request.");
			default -> new Fail(start, code,"Server failed.");
			};
		} catch (IOException | InterruptedException e) {
			return new Fail(Instant.now(), 0,"The connection was disrupted: " + e.getMessage());
		}
        
	}
	
	@SuppressWarnings("preview")
	public static boolean executeScan() {
		if (tasks.isEmpty()) return false;
		try (var scope = StructuredTaskScope.open(joiner)) {		
			tasks.stream().forEach(scope::fork) ;
			try {
				ExecutionResult results = scope.join();
				Report stat = Report.summarize(results);
				json = LazyConstant.of(() -> {return JSON_DTO.convert(stat); });
				csv = LazyConstant.of(() -> {return CSV_DTO.convert(results); });
			} catch (InterruptedException e) {
				e.printStackTrace();
			}


		}
		
		return true;
		
	}
	
	public static void setUp() {
		if (!requestList.isEmpty())
		tasks = requestList.stream()
				.<Callable<ScanResult>>map(req -> () -> new ScanResult(req.id() ,req, scanOperator(req)))
				.toList();	
	
	}
	
}
