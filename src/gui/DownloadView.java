package gui;

import javafx.animation.Timeline;
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
import centralprocessor.CommunicationsInterface;
import centralprocessor.PERSYSTSession;

public class DownloadView extends ScrollPane {

	private CommunicationsInterface comint;
	private ListView<String> downloadList;
	private ObservableList<String> List;
	
	 //vars for testing
//	static ArrayList<String> fnames;
//	static ArrayList<Double> progresses;
	Timeline timeline;

	public DownloadView(CommunicationsInterface comint) {
		this.comint = comint;

		List = FXCollections.observableArrayList();
		downloadList = new ListView<>(List);

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
//		fnames = new ArrayList<String>();
//		fnames.add("filename1");
//		fnames.add("filename2");
//		fnames.add("filename3");
//		fnames.add("filename4");
//		progresses = new ArrayList<Double>();
//		progresses.add(new Double(0.2));
//		progresses.add(new Double(0.4));
//		progresses.add(new Double(0.6));
//		progresses.add(new Double(0.8));
		
//		timeline = new Timeline(new KeyFrame(
//		        Duration.millis(1000),
//		        ae -> updateList()));
//		timeline.setCycleCount(Animation.INDEFINITE);
//		timeline.play();
//		updateList();
	}
	
	public ObservableList<String> getList(){
		return this.List;
	}

	public void updateList() {
		System.out.println("download view update");
		List.clear();
		for(int i = 0; i < PERSYSTSession.comm.ftrans.processes.size(); i++){
			List.add(Integer.toString(i));
		}
//		for(int i = 0; i < progresses.size(); i++){
//			List.add(Integer.toString(i));
//		}
	}

	static class downloadCell extends ListCell<String> {
		HBox hbox = new HBox();
		Label label = new Label("(empty)");
		ProgressBar progressBar = new ProgressBar(-1.0);
		String lastItem;
		int index;

		public downloadCell() {
			super();
			
			//TODO update this block so that it gets the progress of the file
			Task<Void> task = new Task<Void>() {
	            @Override 
	            public Void call() {
	            	double progress; 
	            	boolean isfinished = false;
	            	while(!isfinished){
	            		progress = PERSYSTSession.comm.ftrans.processes.get(index).getProgress();
	            		try {
	            			Thread.sleep(100);
	            		} catch (InterruptedException e) {
	            			Thread.interrupted();
	            			break;
	            		}
//	            		System.out.println(Double.toString(progress));
	            		if(progress == 0.0){
	            			progress = 1.0;
	            			isfinished = true;
	            		}
		            	updateProgress(progress, 1);
	            	}
	            	return null;
	            }
			};
	          
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
//				System.out.println(item);
				if(item != null){
					index = Integer.parseInt(item);
					label.setText(PERSYSTSession.comm.ftrans.files.get(index));
//					label.setText(fnames.get(index));
				}
				setGraphic(hbox);
			}
		}
	}

}
