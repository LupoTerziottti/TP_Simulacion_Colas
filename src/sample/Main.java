package sample;

import javafx.scene.control.MenuBar;
import javafx.scene.layout.VBox;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {

    public void start(Stage primaryStage) throws Exception{


        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Trabajo Practico N5 Simulacion de Colas");
        primaryStage.setScene(new Scene(root, 1366, 768));
        primaryStage.setMaximized(true);
        primaryStage.show();



    }



    public static void main(String[] args) {
        launch(args);

    }
}
