package _StatusCheck;

import tools.jackson.databind.*;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class JSON_DTO{
	
	private static final ObjectMapper mapper = new ObjectMapper();
	
	public static void exportJSON(Stage stage, String jsonString) {
        FileChooser window = new FileChooser();
        window.getExtensionFilters().add(new ExtensionFilter("extension for standard JSON", "*.json"));
        window.setTitle("Export File");
       

        File targetFile = window.showSaveDialog(stage);

        if (targetFile != null) {
            try {
                Files.writeString(targetFile.toPath(), jsonString);
                System.out.println("File saved to: " + targetFile.getAbsolutePath());
            } catch (IOException e) {
                stage.setScene(new Scene(new Group(new Text(10, 40, e.getMessage()))));
                stage.show();
            }
            
        Platform.exit();
        } else Platform.exit();
    }
	
	
	
	public static String convert(Object object) {
		return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
	}
	

}
