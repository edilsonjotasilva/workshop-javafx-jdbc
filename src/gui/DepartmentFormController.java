package gui;



import java.net.URL;
import java.util.ResourceBundle;

import db.DbException;
import gui.utils.Alerts;
import gui.utils.Constraints;
import gui.utils.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import model.entities.Department;
import model.services.DepartmentService;

public class DepartmentFormController implements Initializable {
	private Department entity;
	private DepartmentService depService;
	@FXML
	private TextField txtId;
	@FXML
	private TextField txtName;
	@FXML
	private Label  labelErrorName;
	@FXML
	private Button btSave;
	@FXML
	private Button btCancel;
	public void  setDepartment(Department entity) {		
		this.entity = entity;
	}
	public void setDepartmentService(DepartmentService depService ) {
		this.depService = depService;
	}
	@FXML
	public void onBtSaveAction(ActionEvent event) {
		if(entity == null) {
			throw new IllegalStateException("Entity was null");
		}if(depService == null ) {
			throw new IllegalStateException("depService was null");
		}
		try {			
		entity = getFormData();
		depService.saveOrUpdate(entity);
		Utils.currenteStage(event).close();
		} catch (DbException e) {
			Alerts.showAlert("Error Saving DataBase", null, e.getMessage(), AlertType.ERROR);
		}
	}
	private Department getFormData() {
		Department obj = new Department();
		obj.setId(Utils.tryParseToInt(txtId.getText()));
		obj.setName(txtName.getText());
		return obj;
	}
	@FXML
	public void onBtCancelAction(ActionEvent event) {
		Utils.currenteStage(event).close();
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}
	private void initializeNodes() {
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLength(txtName, 20);
	}
	public void updateFormData(){
		if(entity == null) {
			throw new IllegalStateException("entity was null");
		}
		txtId.setText(String.valueOf(entity.getId()));
		txtName.setText(entity.getName());
	}

}
