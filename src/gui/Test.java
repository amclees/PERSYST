package gui;

import java.util.ArrayList;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Test extends Application{

	@Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Log In");
        
        ArrayList<String> list = new ArrayList<>();
        
        list.add("130.182.24.170");
        list.add("130.182.24.172");
        list.add("130.182.24.174");
        list.add("130.182.24.176");
        list.add("130.182.24.178");
        
       NetworkView root = new NetworkView(list);

        Scene scene = new Scene(root, 600, 250);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

	
}
