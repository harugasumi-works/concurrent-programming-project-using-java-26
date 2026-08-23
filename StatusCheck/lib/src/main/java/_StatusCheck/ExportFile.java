package _StatusCheck;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;

public class ExportFile {
	
	public static void export(Stage stage, String data) {
        FileChooser window = new FileChooser();
        window.getExtensionFilters().add(new ExtensionFilter("extension for standard JSON", "*.json"));
        window.getExtensionFilters().add(new ExtensionFilter("extension for standard CSV", "*.csv"));
        window.setTitle("Export File");
       

        File targetFile = window.showSaveDialog(stage);

        if (targetFile != null) {
            try {
                Files.writeString(targetFile.toPath(), data);
                System.out.println("File saved to: " + targetFile.getAbsolutePath());
            } catch (IOException e) {
                stage.setScene(new Scene(new Group(new Text(10, 40, e.getMessage()))));
                stage.show();
            }
            
        Platform.exit();
        } else Platform.exit();
    }
}
