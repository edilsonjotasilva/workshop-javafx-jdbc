package gui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import application.Main;
import db.DbIntegridException;
import gui.listeners.DataChangeListeners;
import gui.utils.Alerts;
import gui.utils.Utils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Department;
import model.services.DepartmentService;

public class DepartmentListController implements Initializable, DataChangeListeners {

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
	private TableColumn<Department, Department> tableColumnEDIT;
	
	@FXML
	private TableColumn<Department,Department>tableColumnREMOVE;
	
	@FXML
	private ObservableList<Department> obsList;

	@FXML
	public void onBtNewAction(ActionEvent eventoCarregaDepartmentForm) {
		Stage parentStage = Utils.currenteStage(eventoCarregaDepartmentForm);
		Department obj = new Department();
		createDialogForm(obj, "/gui/DepartmentForm.fxml", parentStage);
	}

	public void setDepartmentService(DepartmentService service) {
		this.service = service;
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}

	private void initializeNodes() {
		// esse metodo é um padrao do javaFX para inicializar o comportamento das
		// colunas
		tableCollumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableCollumnName.setCellValueFactory(new PropertyValueFactory<>("Name"));
		// a Classe Window é uma superclasse do Stage, por isso a necessidade do cast
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewDepartment.prefHeightProperty().bind(stage.heightProperty());

	}

	public void updateTableView() {
		if (service == null) {
			throw new IllegalStateException("Service estava null");
		}
		List<Department> list = service.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewDepartment.setItems(obsList);		
		initEditButtons();
		initRemoveButtons();

	}

	private void createDialogForm(Department obj, String absoluteName, Stage parentStage) {// parentStage nome da janela
																							// de dialogo
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();

			DepartmentFormController controller = loader.getController();
			controller.setDepartment(obj);
			controller.setDepartmentService(new DepartmentService());
			controller.subscribeDataChangeListeners(this);
			controller.updateFormData();

			// codigos da janela de dialogo da View DepartmentForm.fxml
			Stage dialogStage = new Stage();// quando criamos uma janela do tipo MODAL temos de criar um movo
			// palco(Stage), pois teremos um palco abrindo na frente do outro
			dialogStage.setTitle("Enter department Data");
			dialogStage.setScene(new Scene(pane));// para um novo palco criamos uma nova cena
			dialogStage.setResizable(false);// false nao permite o redimensionamento da janela
			dialogStage.initOwner(parentStage);// inicia o palco pai(parenteStage) da janela
			dialogStage.initModality(Modality.WINDOW_MODAL);// MODAL não permite clicar fora da janela enquanto ele
															// tiver aberta
			dialogStage.showAndWait();// exibe(carrega) a janela

		} catch (IOException e) {

			Alerts.showAlert("IO Exceptions", "Error loading view", e.getMessage(), AlertType.ERROR);
		}
	}

	@Override
	public void onDataChanged() {
		updateTableView();
	}

	private void initEditButtons() {
		tableColumnEDIT.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEDIT.setCellFactory(param -> new TableCell<Department, Department>() {
			private final Button button = new Button("edit");

			@Override
			protected void updateItem(Department obj, boolean empty) {
				super.updateItem(obj, empty);

				if (obj == null) {
					setGraphic(null);
					return;
				}

				setGraphic(button);
				button.setOnAction(
						event -> createDialogForm(obj, "/gui/DepartmentForm.fxml", Utils.currenteStage(event)));
						
			}
		});
	}
	private void initRemoveButtons() {
		tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnREMOVE.setCellFactory(param -> new TableCell<Department, Department>() {
			private final Button button = new Button("remove");

			@Override
			protected void updateItem(Department obj, boolean empty) {
				super.updateItem(obj, empty);

				if (obj == null) {
					setGraphic(null);
					return;
				}

				setGraphic(button);
				button.setOnAction(
						event -> removeEntity(obj));
						
			}
		});
	}

	private void  removeEntity(Department obj) {
		
		Optional <ButtonType>result = Alerts.showConfirmation("Confirmation", "Are you sure to delete?");
		
		if(result.get()== ButtonType.OK) {
			if(service == null) {
				throw new IllegalStateException("Sevice was null");
			}
		}
		
		try {
			service.remove(obj);
			updateTableView();
		}catch(DbIntegridException e) {
			Alerts.showAlert("Error removing object", null, e.getMessage(), AlertType.ERROR);
		}
	}

}
