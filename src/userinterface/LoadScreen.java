package userinterface;

import java.util.concurrent.CountDownLatch;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;

public class LoadScreen extends Application {

	private Label loadingLabel = new Label("Hello World");

	public static LoadScreen loadscreen = null;
	public static final CountDownLatch latch = new CountDownLatch(1);

	public static LoadScreen waitForLoadScreen() {
		try {
			latch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return loadscreen;
	}

	public static void setLoadScreen(LoadScreen loadscreen0) {
		loadscreen = loadscreen0;
		latch.countDown();
	}

	public LoadScreen() {
		setLoadScreen(this);
	}

	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("PERSYST");
		VBox root = new VBox(40);
		root.setAlignment(Pos.CENTER);

		loadingLabel.setFont(new Font(24));

		ProgressIndicator progress = new ProgressIndicator(-1.0);

		progress.setMinHeight(200);
		progress.setMinWidth(200);

		root.getChildren().addAll(progress, loadingLabel);

		Scene scene = new Scene(root, 600, 400);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public void setLabelText(String text) {
		loadingLabel.setText(text);
	}

	public static void main(String[] args) {
		launch(args);
	}
}
