package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import gui.utils.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

public class MainViewControllers implements Initializable {
	@FXML
	private MenuItem menuItemSeller;
	@FXML
	private MenuItem menuItemDepartment;
	@FXML
	private MenuItem menuItemAbout;

	@FXML
	public void onMenuItemSellerAction() {
		System.out.println("onMenuItemSellerAction");
	}

	@FXML
	public void onMenuDepartmentAction() {
		loadView("/gui/DepartmentList.fxml");
	}

	@FXML
	public void onMenuAboutAction() {
		loadView("/gui/About.fxml");
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {

	}

	private synchronized void loadView(String absoluteName) {
		try {
			FXMLLoader telaDoAbout = new FXMLLoader(getClass().getResource(absoluteName));
			VBox newVboxComTelaDoAbout = telaDoAbout.load();//carrega a tela do About
			//abaixo estou pegando o objeto getcenaPrincipal da classe principal para poder referenciar o node da Scene cenaPrincipal
			//Principal, mais abaixo o metodo getRoot do objeto cenaPrincipal pega o primeiro node da nossa cena principal
			Scene cenaPrincipal = Main.getMainScene();
			// o metodo getRoot pega o primeiro elemento da view cenaPrincipal, que � o elemento ScrollPane, por isso temos
			//um cast para ScrollPane, e coloco tudo dentro de parenteses,depois eu uso o getContent para pegar o conteudo
			// que est� dentro do ScrollPane, e fa�o um cast para VBox para colocar dentro do elemento VBox(mainBox)
			// que acabei de criar
			VBox VBoxPrincipal = (VBox)((ScrollPane) cenaPrincipal.getRoot()).getContent();
			
			Node menuPrincipal = VBoxPrincipal.getChildren().get(0);

			VBoxPrincipal.getChildren().clear();
			VBoxPrincipal.getChildren().add(menuPrincipal);
			VBoxPrincipal.getChildren().addAll(newVboxComTelaDoAbout.getChildren());
			
			
		} catch (IOException e) {
			Alerts.showAlert("IOException", "Error loading View", e.getMessage(), AlertType.ERROR);
		}
	}
}