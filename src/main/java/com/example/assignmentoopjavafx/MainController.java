package com.example.assignmentoopjavafx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.net.URL;


public class MainController {

    @FXML
    private MenuBar menuBar;

    @FXML
    private VBox leftPanel;

    @FXML
    private ImageView profileImageView;

    @FXML
    private TableView<Student> studentTableView;

    @FXML
    private TableColumn<Student, Integer> idColumn;

    @FXML
    private TableColumn<Student, String> firstNameColumn;

    @FXML
    private TableColumn<Student, String> lastNameColumn;

    @FXML
    private TableColumn<Student, String> departmentColumn;

    @FXML
    private TableColumn<Student, String> majorColumn;

    @FXML
    private TableColumn<Student, String> emailColumn;

    @FXML
    private TextField firstNameTextField;

    @FXML
    private TextField lastNameTextField;

    @FXML
    private TextField departmentTextField;

    @FXML
    private TextField majorTextField;

    @FXML
    private TextField emailTextField;

    @FXML
    private TextField imageUrlTextField;

    private ObservableList<Student> studentData;

    @FXML
    public void initialize() {
        // Set up TableView columns
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        departmentColumn.setCellValueFactory(new PropertyValueFactory<>("department"));
        majorColumn.setCellValueFactory(new PropertyValueFactory<>("major"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        // Initialize student data list
        studentData = FXCollections.observableArrayList();
        studentTableView.setItems(studentData);

        // Add a listener to the table selection to populate text fields
        studentTableView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showStudentDetails(newValue));

        // Set a default profile image
        try {
            URL defaultImageUrl = getClass().getResource("default_profile.jpg");
            if (defaultImageUrl != null) {
                Image defaultImage = new Image(defaultImageUrl.toExternalForm());
                profileImageView.setImage(defaultImage);
            } else {
                System.err.println("Default profile image not found: default_profile.jpg");
            }
        } catch (Exception e) {
            System.err.println("Error loading default profile image: " + e.getMessage());
        }
    }

    private void showStudentDetails(Student student) {
        if (student != null) {
            firstNameTextField.setText(student.getFirstName());
            lastNameTextField.setText(student.getLastName());
            departmentTextField.setText(student.getDepartment());
            majorTextField.setText(student.getMajor());
            emailTextField.setText(student.getEmail());
            imageUrlTextField.setText(student.getImageUrl());

            // Load image if imageUrl is provided
            if (student.getImageUrl() != null && !student.getImageUrl().isEmpty()) {
                try {
                    Image image = new Image(student.getImageUrl());
                    profileImageView.setImage(image);
                } catch (Exception e) {
                    System.err.println("Could not load image from URL: " + student.getImageUrl() + " - " + e.getMessage());
                    // Fallback to default image if loading fails
                    URL defaultImageUrl = getClass().getResource("default_profile.jpg");
                    if (defaultImageUrl != null) {
                        profileImageView.setImage(new Image(defaultImageUrl.toExternalForm()));
                    }
                }
            } else {
                // If no image URL, show default profile image
                URL defaultImageUrl = getClass().getResource("default_profile.jpg");
                if (defaultImageUrl != null) {
                    profileImageView.setImage(new Image(defaultImageUrl.toExternalForm()));
                }
            }
        } else {
            clearTextFields();
            // Show default profile image
            URL defaultImageUrl = getClass().getResource("default_profile.jpg");
            if (defaultImageUrl != null) {
                profileImageView.setImage(new Image(defaultImageUrl.toExternalForm()));
            }
        }
    }

    @FXML
    private void handleClear() {
        clearTextFields();
        studentTableView.getSelectionModel().clearSelection();
        // Reset to default profile image
        URL defaultImageUrl = getClass().getResource("default_profile.jpg");
        if (defaultImageUrl != null) {
            profileImageView.setImage(new Image(defaultImageUrl.toExternalForm()));
        }
    }

    private void clearTextFields() {
        firstNameTextField.clear();
        lastNameTextField.clear();
        departmentTextField.clear();
        majorTextField.clear();
        emailTextField.clear();
        imageUrlTextField.clear();
    }

    @FXML
    private void handleAdd() {
        if (isInputValid()) {
            Student selectedStudent = studentTableView.getSelectionModel().getSelectedItem();
            if (selectedStudent != null) {
                // If a student is selected, update it (acting as 'Edit' when selected)
                selectedStudent.setFirstName(firstNameTextField.getText());
                selectedStudent.setLastName(lastNameTextField.getText());
                selectedStudent.setDepartment(departmentTextField.getText());
                selectedStudent.setMajor(majorTextField.getText());
                selectedStudent.setEmail(emailTextField.getText());
                selectedStudent.setImageUrl(imageUrlTextField.getText());
                studentTableView.refresh(); // Refresh the table to show updated data
            } else {
                // Otherwise, add a new student
                Student newStudent = new Student(
                        firstNameTextField.getText(),
                        lastNameTextField.getText(),
                        departmentTextField.getText(),
                        majorTextField.getText(),
                        emailTextField.getText(),
                        imageUrlTextField.getText()
                );
                studentData.add(newStudent);
            }
            clearTextFields();
            studentTableView.getSelectionModel().clearSelection();
        } else {
            showAlert("Invalid Input", "Please fill in all fields.");
        }
    }

    @FXML
    private void handleDelete() {
        int selectedIndex = studentTableView.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            studentTableView.getItems().remove(selectedIndex);
            clearTextFields();
            // Reset to default profile image
            URL defaultImageUrl = getClass().getResource("default_profile.jpg");
            if (defaultImageUrl != null) {
                profileImageView.setImage(new Image(defaultImageUrl.toExternalForm()));
            }
        } else {
            showAlert("No Selection", "No student selected to delete.");
        }
    }

    @FXML
    private void handleEdit() {
        // The "Add" button's logic already handles editing when a row is selected.
        // This button can be used to explicitly trigger populating fields if not already done by selection listener.
        // Or it could change the "Add" button's text to "Update" and handle the update logic.
        // For simplicity, let's just ensure a selection exists to enable the "Add" button to update.
        // The `showStudentDetails` already populates fields on selection.
        Student selectedStudent = studentTableView.getSelectionModel().getSelectedItem();
        if (selectedStudent == null) {
            showAlert("No Selection", "Please select a student to edit.");
        }
        // Fields are already populated by selection listener, no action needed here.
    }

    // Menu handlers (for extra credit functionality)
    @FXML
    private void handleFileNew() {
        System.out.println("File -> New selected");
        handleClear(); // Clear fields when starting a new entry
    }

    @FXML
    private void handleFileSave() {
        System.out.println("File -> Save selected");
        showAlert("Save", "Save functionality not implemented.");
    }

    @FXML
    private void handleFileExit() {
        System.out.println("File -> Exit selected");
        System.exit(0);
    }

    @FXML
    private void handleEditCopy() {
        System.out.println("Edit -> Copy selected");
        showAlert("Copy", "Copy functionality not implemented.");
    }

    @FXML
    private void handleEditPaste() {
        System.out.println("Edit -> Paste selected");
        showAlert("Paste", "Paste functionality not implemented.");
    }

    @FXML
    private void handleThemeLight() {
        System.out.println("Theme -> Light selected");
        // Example: Change scene stylesheet
        studentTableView.getScene().getStylesheets().clear();
        studentTableView.getScene().getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        showAlert("Theme", "Switched to Light Theme.");
    }

    @FXML
    private void handleThemeDark() {
        System.out.println("Theme -> Dark selected");
        // Example: Change scene stylesheet to a dark theme CSS (needs another CSS file)
        showAlert("Theme", "Dark Theme not implemented. This would require a separate CSS file.");
    }

    @FXML
    private void handleHelpAbout() {
        System.out.println("Help -> About selected");
        showAlert("About", "FSC CSC325 Full Stack Project\nVersion 1.0");
    }

    private boolean isInputValid() {
        // Check if all essential text fields are filled
        return !firstNameTextField.getText().trim().isEmpty() &&
                !lastNameTextField.getText().trim().isEmpty() &&
                !departmentTextField.getText().trim().isEmpty() &&
                !majorTextField.getText().trim().isEmpty() &&
                !emailTextField.getText().trim().isEmpty();
        // imageUrlTextField can be optional, so not included in validation
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    }