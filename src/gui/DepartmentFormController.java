package gui;



import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.listeners.DataChangeListeners;
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
import model.Exceptions.ValidationException;
import model.entities.Department;
import model.services.DepartmentService;

// A classe DepartmenFormControler eu o subject, ou seja ela emite um evento sempre que
//ela for alterada.
//enquanto a classe DepartmentListController e o observer, ou seja ela vai ouvir os 
//eventos disparados pela classe DepartmenFormControler
public class DepartmentFormController implements Initializable {
	private Department entity;
	private DepartmentService depService;
	// A List<DataChangeListeners> guarda uma lista dos objetos interessados em ouvir
	//os eventos emitidos 
//para que os objetos interessados em ouvir os eventos possam se inscrever no DataChangeListeners, na 
	//linha 55 tem o metodo que faz essa incrição
	private List<DataChangeListeners>dataChangeListeners = new ArrayList();
	
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
	////////////////////////////////////////////////////////////////////
	//metodo que emite os eventos
	public void subscribeDataChangeListeners(DataChangeListeners listener) {
		dataChangeListeners.add(listener);
	}
	//////////////////////////////////////////////////////////
	@FXML
	public void onBtSaveAction(ActionEvent event) {
		if(entity == null) {
			throw new IllegalStateException("Entity was null");
		}if(depService == null ) {
			throw new IllegalStateException("depService was null");
		}
		try {			
		entity = buscaFormData();
		depService.saveOrUpdate(entity);
		notifyDataChangeListeners();
		Utils.currenteStage(event).close();
		
		} catch (DbException e) {
			Alerts.showAlert("Error Saving DataBase", null, e.getMessage(), AlertType.ERROR);
		}catch(ValidationException e) {
			setErrorMessages(e.pegaErros());
		}
	}
	private void notifyDataChangeListeners() {
		for (DataChangeListeners listener : dataChangeListeners) {
			listener.onDataChanged();
		}
		
	}
	private Department buscaFormData() {
		Department obj = new Department();
		obj.setId(Utils.tryParseToInt(txtId.getText()));
		ValidationException exception = new ValidationException("Validation Error");
		
		if(txtName.getText() == null || txtName.getText().trim().equals("")){
			exception.addErrors("name", "Field can't be empty");
		}
		obj.setName(txtName.getText());
		
		if(exception.pegaErros().size()>0) {
			throw exception;
		}
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
	
	private void setErrorMessages(Map<String,String>errors) {
		Set<String> fields = errors.keySet();
		
		if(fields.contains("name")) {
			labelErrorName.setText(errors.get("name"));
		}
	}
}
