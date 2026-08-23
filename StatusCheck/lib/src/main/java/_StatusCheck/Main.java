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

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class Main extends Application {
	
	private static final CustomJoin joiner = new CustomJoin();
	
	private static final HttpClient client = HttpClient.newBuilder()
			.version(Version.HTTP_3)
            .connectTimeout(Duration.ofSeconds(10))
            .build(); 
	
	private static String json = "";
	private static String csv = "";
	
	@Override
    public void start(Stage primaryStage) {
		
		 // 1. Create the two buttons
        Button button1 = new Button("CSV export");
        Button button2 = new Button("JSON export");

        // 2. Add click actions to the buttons using lambda expressions
        button1.setOnAction(_ -> ExportFile.export(primaryStage, csv));
        button2.setOnAction(_ -> ExportFile.export(primaryStage, json));

        // 3. Arrange buttons in a horizontal box with 10px spacing
        HBox root = new HBox(10); 
        root.setAlignment(Pos.CENTER); // Center the buttons in the window
        root.getChildren().addAll(button1, button2);

        // 4. Create the scene with the layout pane and set dimensions (width, height)
        Scene scene = new Scene(root, 300, 200);

        // 5. Configure and display the main window (Stage)
        primaryStage.setTitle("ExportFile");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

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
				Report stat = Report.summarize(results);
				json = JSON_DTO.convert(stat);
				try {
				    csv = CSV_DTO.convert(results);
				} catch (Exception e) {
				    e.printStackTrace();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}


		}
		
	Application.launch(args);
		
	}
	
	
	private static Outcome executeScan(ScanRequest req) {
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
	}
	


