package _StatusCheck;

import javafx.beans.binding.Bindings;
import javafx.geometry.Side;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitMenuButton;

public class ButtonFactory {
	
	public static Button scanButton() {
		Button button = new Button("Scan");
		button.setOnAction(_ -> {
				Operator.requestList = UILogic.items.stream()
													.<ScanRequest>mapMulti((item, consumer) -> {
														if (item instanceof RowItem.Pending(ScanRequest request)) {
															consumer.accept(request);
														}
													})
													.toList();
        		Operator.setUp();      		
        		if (Operator.executeScan() == false) {
        			PopUp.message("Failed to scan. Check if the list is empty");
        		} else	PopUp.message("Successfully scanned");
        		 		   	
        });
		return button;
	}
	
	public static MenuItem json() {
		MenuItem item = new MenuItem("Export to JSON");
		item.setOnAction(_ -> {
        	try {
        		JSON json = Operator.json.get();
        		ExportFile.exportJSON(json);
        		PopUp.message("Successfully exported");
        	} catch (Exception _) {
        		PopUp.message("Operation was interrupted");
        	}
        });
		return item;
	}
	
	public static MenuItem csv() {
		MenuItem item = new MenuItem("Export to CSV");
		item.setOnAction(_ -> {
        	try {
        		CSV csv = Operator.csv.get();
        		ExportFile.exportCSV(csv);
        		PopUp.message("Successfully exported");
        	} catch (Exception _) {
        		PopUp.message("Operation was interrupted");
        	}
        });
		return item;
	}
	
	public static SplitMenuButton saveButton() {
		SplitMenuButton button = new SplitMenuButton("Save");
		
		button.setPopupSide(Side.RIGHT);
		button.getItems().addAll(json(), csv());
		
		button.setOnAction(_ -> {
            if (button.isShowing()) {
                button.hide();
            } else {
                button.show();
            }
        });
		
		if (Operator.requestList.isEmpty()) button.disableProperty().bind(Bindings.isEmpty(UILogic.items));
		return button;
	}

}
