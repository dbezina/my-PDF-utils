package com.bezina.utils;


import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.awt.*;
import java.io.File;
import javafx.scene.control.Label;
import java.io.IOException;
import java.util.List;

import static com.bezina.PDFUtilsApp.showAlert;

public class FileActions {

    // Метод для сохранения нескольких страниц в один файл
    public void savePdfFile(Stage stage, TextField pagesField, File selectedFile) {
        if (selectedFile == null) {
            showAlert("No PDF file selected.");
            return;
        }

        // Получение диапазона страниц
        String pagesText = pagesField.getText();
        if (pagesText.isEmpty()) {
            showAlert("Please enter page numbers.");
            return;
        }

        List<Integer> pages = PDFSplitter.parsePages(pagesText);
        if (pages.isEmpty()) {
            showAlert("Invalid page numbers.");
            return;
        }

        // Открываем диалог для выбора места сохранения файла
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Merged PDF");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        File saveFile = fileChooser.showSaveDialog(stage);
        if (saveFile != null) {
            try {
                mergePages(selectedFile, pages, saveFile);
                showAlert("PDF saved successfully.");
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Error saving PDF.");
            }
        }
    }
    // Метод для сохранения нескольких страниц каждая в отдельный файл
    public void savePagesSprtl(Stage stage, File selectedFile, Label messageLabel,
                                TextField pagesField, TextField nameTemplateField) {
        if (selectedFile == null) {
            showAlert("No file selected");
            return;
        }

        String pagesText = pagesField.getText();
        if (pagesText.isEmpty()) {
            showAlert("No pages specified");
            return;
        }

        String nameTemplate = nameTemplateField.getText();
        if (nameTemplate.isEmpty()) {
            messageLabel.setText("No file name template specified, will be saved with page-% name");
            nameTemplate = "page";
            // return;
        }

        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(stage);

        if (selectedDirectory != null) {
            try {
                List<Integer> pages = PDFSplitter.parsePages(pagesText);
                if (!pages.isEmpty()) {
                    PDFSplitter.splitPdf(selectedFile, pages, selectedDirectory.getAbsolutePath(), nameTemplate);
                    showAlert("Files saved successfully");
                } else{
                    showAlert("Something went wrong");
                    showAlert("Files weren't save. Check pages interval");}
            } catch (IOException e) {
                e.printStackTrace();
                messageLabel.setText("Error saving files");
            }
        } else {
            messageLabel.setText("No directory selected");
        }
    }



    // Метод для объединения выбранных страниц в один файл
    public void mergePages(File pdfFile, List<Integer> pages, File outputFile) throws IOException {
        try (PDDocument document = PDDocument.load(pdfFile);
             PDDocument newDocument = new PDDocument()) {

            for (int pageNum : pages) {
                if (pageNum > 0 && pageNum <= document.getNumberOfPages()) {
                    newDocument.addPage(document.getPage(pageNum - 1));  // PDFBox использует 0-индексацию
                }
            }
            // Сохранение нового документа
            newDocument.save(outputFile);
        }
    }
    //предпросмотр выбранного файла
    public void previewPdfFile(File selectedFile) {
        if (selectedFile != null) {
            if (Desktop.isDesktopSupported()) {
                try {
                    Desktop desktop = Desktop.getDesktop();
                    if (desktop.isSupported(Desktop.Action.OPEN)) {
                        // Открывает PDF файл в системном приложении
                        desktop.open(selectedFile);
                    } else {
                        System.out.println("Opening PDFs is not supported on this system.");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("Failed to open the PDF file.");
                }
            } else {
               showAlert("Desktop is not supported.");
            }
        }
    }
}
