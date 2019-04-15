package gui.utils;


import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;

public class Utils {
//essa função recebe o evento do botão que recebeu o clique nesse momento
	//esse evento pega o Stage referenciado por aquele botão que recebeu o clique
	public static Stage currenteStage(ActionEvent event) {
	
		return (Stage)((Node)event.getSource()).getScene().getWindow();
	}
}
