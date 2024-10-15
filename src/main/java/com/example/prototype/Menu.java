package com.example.prototype;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class Menu extends Application {
    @Override
    public void start(Stage stage) throws IOException {

//        // Check if the file is open
//        if (ExcelManager.isFileOpen()) {
//            // Show an alert and close the application if the file is open
//            Alert alert = new Alert(Alert.AlertType.ERROR);
//            alert.setTitle("File Open Error");
//            alert.setHeaderText("The Excel file is currently open.");
//            alert.setContentText("Please close the file and try again.");
//            alert.showAndWait();
//
//            // Exit the application
//            Platform.exit();
//            return;
//        }
        ExcelManager.initializeWorkbook();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("menu.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 900, 600);

        MenuController cont = fxmlLoader.getController();
        cont.setScene(scene);

        stage.setTitle("Menu");
        stage.setScene(scene);
        stage.show();


    }
    @Override
    public void stop() {
        try {
            ExcelManager.saveWorkbook();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Save Error");
            alert.setHeaderText("An error occurred while saving the workbook.");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }


    public static void main(String[] args) {
        launch();
    }
}