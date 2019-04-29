package gui.utils;


import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;

public class Utils {
//essa fun��o recebe o evento do bot�o que recebeu o clique nesse momento
	//esse evento pega o Stage referenciado por aquele bot�o que recebeu o clique
	//ou seja, ele vai pegar "/gui/DepartmentForm.fxml"
	public static Stage currenteStage(ActionEvent event) {
	
		return (Stage)((Node)event.getSource()).getScene().getWindow();
	}
	public static Integer tryParseToInt(String str) {
		
		try {
			return Integer.parseInt(str);
		}catch(NumberFormatException e) {
			return null;
		}
		
	}
}
