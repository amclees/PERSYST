package gui;

import centralprocessor.CommunicationsInterface;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.util.Callback;
import networking.FileTransfer;

public class DownloadView extends ScrollPane {

	private CommunicationsInterface comint;
	private ListView<String> downloadList;

	public DownloadView(CommunicationsInterface comint) {
		this.comint = comint;

		ObservableList<String> list = FXCollections.observableArrayList("Item 1", "Item 2", "Item 3", "Item 4");
		downloadList = new ListView<>(list);

		downloadList.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
			@Override
			public ListCell<String> call(ListView<String> param) {
				return new downloadCell();
			}
		});

		this.setFitToHeight(true);
		this.setFitToWidth(true);
		this.setContent(downloadList);
		// this.getChildren().add(new Label("dsfdsf"));
		updateList();
	}

	public void updateList() {

	}

	static class downloadCell extends ListCell<String> {
		HBox hbox = new HBox();
		Label label = new Label("(empty)");
		ProgressBar progressBar = new ProgressBar(-1.0);
		String lastItem;

		public downloadCell() {
			super();
			
			//TODO update this block so that it gets the progress of the file
			Task<Void> task = new Task<Void>() {
	            @Override 
	            public Void call() {
	            	for (double i = 0; i <= 1; i = i + .001) {
	            		try {
	            			Thread.sleep(100);
	            		} catch (InterruptedException e) {
	            			Thread.interrupted();
	            			break;
	            		}
		            	updateProgress(i, 1);
	            	}
	            	return null;
	            }
			};
			
			// Something Like This.
//			Task<Void> task = new Task<Void>() {
//	            @Override 
//	            public Void call() {
//	            	for (double i = 0; i <= 1; i = i + .001) {
//	            		try {
//	            			Thread.sleep(100);
//	            		} catch (InterruptedException e) {
//	            			Thread.interrupted();
//	            			break;
//	            		}
//            			String file = label.getText();
//            			FileTransfer.getProgress
//		            	updateProgress(i, 1);
//	            	}
//	            	return null;
//	            }
//			};
	          
	        progressBar.progressProperty().bind(task.progressProperty());
			Thread th = new Thread(task);th.setDaemon(true);th.start();

			
			hbox.getChildren().addAll(label, progressBar);
			HBox.setHgrow(progressBar, Priority.ALWAYS);
		}

		@Override
		protected void updateItem(String item, boolean empty) {
			super.updateItem(item, empty);
			setText(null);
			if (empty) {
				lastItem = null;
				setGraphic(null);
			} else {
				lastItem = item;
				label.setText(item != null ? item : "<null>");
				setGraphic(hbox);
			}
		}
	}

}
