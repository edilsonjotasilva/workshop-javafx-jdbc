package gui;

import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entities.Department;

public class DepartmentListController implements Initializable {
	@FXML
	private TableView<Department> tableViewDepartment;
	
	@FXML
	private TableColumn<Department, Integer> tableCollumnId;

	@FXML
	private TableColumn<Department, String> tableCollumnName;

	@FXML
	private Button btNew;
	
	public void  onBtNewAction() {
		System.out.println("onBtNewAction");
	}
	

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
		
	}

	private void initializeNodes() {			
		tableCollumnId.setCellValueFactory(new PropertyValueFactory<>("Id"));
		tableCollumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
		// a Classe Window é uma superclasse do Stage, por isso a necessidade do cast
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewDepartment.prefHeightProperty().bind(stage.heightProperty());
		
	}

}
