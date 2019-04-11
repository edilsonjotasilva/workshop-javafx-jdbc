package gui;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entities.Department;
import model.services.DepartmentService;

public class DepartmentListController implements Initializable {
	
	@FXML
	private DepartmentService service;
	@FXML
	private TableView<Department> tableViewDepartment;
	
	@FXML
	private TableColumn<Department, Integer> tableCollumnId;

	@FXML
	private TableColumn<Department, String> tableCollumnName;

	@FXML
	private Button btNew;	
	
	@FXML
	private ObservableList<Department>obsList;
	
	@FXML
	public void  onBtNewAction() {
		System.out.println("onBtNewAction");
	}	
	public void setDepartmentService(DepartmentService service) {
		this.service = service;
	}
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();		
	}
	private void initializeNodes() {
		//esse metodo é um padrao do javaFX para inicializar o comportamento das colunas
		tableCollumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableCollumnName.setCellValueFactory(new PropertyValueFactory<>("Name"));
		// a Classe Window é uma superclasse do Stage, por isso a necessidade do cast
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewDepartment.prefHeightProperty().bind(stage.heightProperty());
		
	}
	public void updateTableView() {
		if (service == null) {
			throw new IllegalStateException("Service stava null");
		}
		List<Department>list = service.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewDepartment.setItems(obsList);
		
	}

}
