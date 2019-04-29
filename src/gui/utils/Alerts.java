package gui.utils;

import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

public class Alerts {

	public static void showAlert(String title, String header, String content, AlertType type) {

		Alert alert = new Alert(type);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(content);
		alert.show();
				
	}
	
	public static Optional<ButtonType>showConfirmation(String title, String content){
<<<<<<< HEAD
		Alert alert = new Alert(AlertType.CONFIRMATION);
=======
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
>>>>>>> 189f51d15f955de92608bc6b680f569c37201ffd
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(content);
		return alert.showAndWait();
	}
}
