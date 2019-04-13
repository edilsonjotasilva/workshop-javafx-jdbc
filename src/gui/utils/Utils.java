package gui.utils;


import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;

public class Utils {

	public static Stage currenteStage(ActionEvent event) {
		// TODO Auto-generated method stub
		return (Stage)((Node)event.getSource()).getScene().getWindow();
	}
}
