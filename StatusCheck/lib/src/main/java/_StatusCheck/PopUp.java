package _StatusCheck;


import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

public class PopUp {


	public static void message(String message) {
        Runnable show = () -> {
            Stage stage = new Stage();
            stage.initModality(Modality.WINDOW_MODAL);

            // Dynamically locate the currently active/focused window
            Window activeWindow = Window.getWindows().stream()
                    .filter(Window::isFocused)
                    .findFirst()
                    .orElse(null);

            if (activeWindow != null) {
                stage.initOwner(activeWindow);
            }

            stage.setScene(new Scene(new Group(new Text(10, 40, message))));
            stage.showAndWait();
        };

        if (Platform.isFxApplicationThread()) {
            show.run();
        } else {
            Platform.runLater(show);
        }
    }
	

}
