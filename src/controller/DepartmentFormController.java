package controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import model.entities.Department;
import model.services.DepartmentService;

public class DepartmentFormController implements Initializable {

	private Department department;
	
	private DepartmentService service;
	
	private List<DataChangeListener> dataChangeListerners = new ArrayList<>();
	
	@FXML
	private TextField textFieldId;
	
	@FXML
	private TextField textFieldName;
	
	@FXML
	private Button btnSave;
	
	@FXML
	private Button btnCancel;
	
	@FXML
	private Label labelErrorName;

	public void setDepartment(Department department) {
		this.department = department;
	}
	
	public void setDepartmentService(DepartmentService service) {
		this.service = service;
	}
	
	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListerners.add(listener);
	}
	
	@FXML
	public void onbtnSaveAction(ActionEvent event) {
		
		if (department == null) {
			throw new IllegalStateException("Entity was null");
		}
		
		if (department == null) {
			throw new IllegalStateException("Service was null");
		}
		
		try {
			department = getFormData();
			service.saveOrUpdate(department);
			notifyDataChangeListeners();
			Utils.currentStage(event).close();
		} catch (DbException e) {
			Alerts.showAlert("Error saving object", null, e.getMessage(), AlertType.ERROR);
		}
		
	}
	
	@FXML
	public void onBtnCancelAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}
	
	private Department getFormData() {
		Department department = new Department();
		department.setId(Utils.tryParseToInt(textFieldId.getText()));
		department.setName(textFieldName.getText());
		
		return department;
	}
	
	private void notifyDataChangeListeners() {
		for (DataChangeListener listener : dataChangeListerners) {
			listener.onDataChanged();
		}
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}
	
	private void initializeNodes() {
		Constraints.setTextFieldInteger(textFieldId);
		Constraints.setTextFieldMaxLength(textFieldName, 30);
	}
	
	public void updateFormData() {
		if (department == null) {
			throw new IllegalStateException("Entity was null");
		}
		
		textFieldId.setText(String.valueOf(department.getId()));
		textFieldName.setText(department.getName());
	}
}
