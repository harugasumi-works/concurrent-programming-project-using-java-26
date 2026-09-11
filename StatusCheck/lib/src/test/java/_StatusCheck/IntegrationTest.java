package _StatusCheck;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;


@Tag("integration")
public class IntegrationTest {
	protected List<String> urls = List.of("www.google.com", "www.wyz.wyz");
	protected String testJSON, testCSV;
	
	@Test
	public void testOperation() {
		Operator.requestList = urls.stream()
									.map(content -> new ScanRequest(UUID.randomUUID().toString(), content))
									.toList();
		
		Operator.setUp();
		
		var ok = true;
		
		assertEquals(null, Operator.csv);
		assertEquals(null, Operator.json);
		assertEquals(ok, Operator.executeScan(_ -> {}));
		
		
		testJSON = Operator.json.get().data();
		testCSV = Operator.csv.get().data();
		
		assertTrue(testJSON.contains("www.google.com"));
		assertTrue(testCSV.contains("www.google.com"));
		
	}
	
	@AfterEach
	public void tearDown() {
		if (!Operator.requestList.isEmpty()) { 
			Operator.requestList =  new ArrayList<>();
			Operator.setUp();
		}
		Operator.json = null;
		Operator.csv = null;
		
	}
	
}
