package org.cryptic.fileencryptordecryptortool;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class FEDTApplicationUI extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(FEDTApplicationUI.class.getResource("main.fxml"));
        Parent root = fxmlLoader.load();
        root.minWidth(790);
        root.minHeight(300);
        root.prefHeight(790);
        root.prefHeight(390);
        Scene scene = new Scene(root);

        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}