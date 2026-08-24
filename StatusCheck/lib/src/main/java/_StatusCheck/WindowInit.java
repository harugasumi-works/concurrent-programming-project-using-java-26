package _StatusCheck;

import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class WindowInit {
	
	public static void messageWindow(String message) {
		Stage stage = new Stage();
		stage.setScene(new Scene(new Group(new Text(10, 40, message))));
        stage.show();
		
	}
	
	public static void launch(Stage stage) {
		 // 1. Create the two buttons
		Button scanBbutton = new Button("Attempt scan");
        SplitMenuButton exportButton = new SplitMenuButton("Save");
        MenuItem optionJSON = new MenuItem("Export to JSON");
        MenuItem optionCSV = new MenuItem("Export to CSV");
        
        exportButton.setPopupSide(Side.RIGHT);
        exportButton.getItems().addAll(optionJSON, optionCSV);

        // 2. Add click actions to the buttons using lambda expressions
        scanBbutton.setOnAction(_ -> {
        	try {
        		Operator.setUp();
        		Operator.executeScan();
        		messageWindow("Successfully scanned");
        		
        	} catch (Exception _){
        		messageWindow("Failed to scan. Check if the list is empty");
        	}
        });
        	
        exportButton.setOnAction(_ -> {
            if (exportButton.isShowing()) {
                exportButton.hide();
            } else {
                exportButton.show();
            }
        });
        
        optionJSON.setOnAction(_ -> {
        	try {
        		JSON json = Operator.json.get();
        		ExportFile.exportJSON(json);
        		messageWindow("Successfully exported");
        	} catch (Exception _) {
        		messageWindow("Operation was interrupted");
        	}
        });
        optionCSV.setOnAction(_ -> {
        	try {
        		CSV csv = Operator.csv.get();
        		ExportFile.exportCSV(csv);
        		messageWindow("Successfully exported");
        	} catch (Exception _) {
        		messageWindow("Operation was interrupted");
        	}
        });

        // 3. Arrange buttons in a horizontal box with 10px spacing
        HBox root = new HBox(10); 
        root.setAlignment(Pos.CENTER); // Center the buttons in the window
        root.getChildren().addAll(scanBbutton, exportButton);

        // 4. Create the scene with the layout pane and set dimensions (width, height)
        Scene scene = new Scene(root, 300, 200);

        // 5. Configure and display the main window (Stage)
        stage.setTitle("Application");
        stage.setScene(scene);
        stage.show();
	}

}
