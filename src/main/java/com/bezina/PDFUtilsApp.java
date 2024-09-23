package com.bezina;

import com.bezina.utils.FileActions;
import com.bezina.utils.PDFSplitter;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.Button;



import java.io.File;
import java.io.IOException;

import java.util.List;


public class PDFUtilsApp extends Application {
    private File selectedFile;
    private Label fileLabel;
    private TextField pagesField;
    private TextField nameTemplateField;
    private Label messageLabel;
    private final FileActions fileActions = new FileActions();

        @Override
        public void start(Stage stage) {

            Button openButton = new Button("Open PDF");
            Button previewButton = new Button("Preview");
            Button saveButton = new Button("Save");
            Button saveSprtlButton = new Button("Save separately");
            Button clearButton = new Button("Clear");

            fileLabel = new Label("File not selected");
            pagesField = new TextField();
            pagesField.setPromptText("Enter pages (e.g., 1,2,5-10)");
            nameTemplateField = new TextField();
            nameTemplateField.setPromptText("Enter file name template (e.g., page-%d)");
            messageLabel = new Label();

            openButton.setOnAction(     event -> selectPdfFile(stage));
            previewButton.setOnAction(  event -> fileActions.previewPdfFile(selectedFile));
            saveButton.setOnAction(     event -> fileActions.savePdfFile(stage,pagesField, selectedFile));
            saveSprtlButton.setOnAction(event -> fileActions.savePagesSprtl(stage,selectedFile,
                                                  messageLabel,pagesField,nameTemplateField));
            clearButton.setOnAction(    event -> clearSelection());

            VBox root = new VBox(10);
            root.setPadding(new Insets(10));
            root.getChildren().addAll(openButton,previewButton,
                    fileLabel, pagesField, nameTemplateField,
                    saveButton, saveSprtlButton,
                    clearButton, messageLabel);

            Scene scene = new Scene(root, 500, 300);
            stage.setScene(scene);
            stage.setTitle("My PDF Utils");
            stage.show();
        }

    private void selectPdfFile(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            fileLabel.setText("Selected file: " + selectedFile.getName());
            messageLabel.setText("");
        } else {
            fileLabel.setText("File not selected");
        }
    }


    private void clearSelection() {
        selectedFile = null;
        fileLabel.setText("File not selected");
        pagesField.clear();
        messageLabel.setText("");
        nameTemplateField.clear();
    }

    public static void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

        public static void main(String[] args) {
            launch(args);
        }
}