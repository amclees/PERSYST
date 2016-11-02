package userinterface;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Test extends Application {

	private void networkTest(Stage primaryStage) {
		primaryStage.setTitle("Network");

		ArrayList<String> list = new ArrayList<>();

		list.add("130.182.24.170");
		list.add("130.182.24.172");
		list.add("130.182.24.174");
		list.add("130.182.24.176");
		list.add("130.182.24.178");
		list.add("130.182.24.170");
		list.add("130.182.24.172");
		list.add("130.182.24.174");
		list.add("130.182.24.176");
		list.add("130.182.24.178");
		list.add("130.182.24.170");
		list.add("130.182.24.172");
		list.add("130.182.24.174");
		list.add("130.182.24.176");
		list.add("130.182.24.178");
		list.add("130.182.24.170");
		list.add("130.182.24.172");
		list.add("130.182.24.174");
		list.add("130.182.24.176");
		list.add("130.182.24.178");

		NetworkView root = new NetworkView(list);
		ScrollPane sp = new ScrollPane(root);
		Scene scene = new Scene(sp, 600, 250);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	@Override
	public void start(Stage primaryStage) {
		
		try {
			InetAddress addr = InetAddress.getByName("8.8.8.8");
//			InetAddress addr = InetAddress.getByName("172.251.8.90");
//			  String host = addr.getLocalHost().getHostName();
			  String host = addr.getHostName();
			  System.out.println(host);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//networkTest(primaryStage);
	}

	public static void main(String[] args) {
		launch(args);
	}

}
