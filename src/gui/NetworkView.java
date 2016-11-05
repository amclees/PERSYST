package gui;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.WindowEvent;

import org.hive2hive.core.api.configs.NetworkConfiguration;

import centralprocessor.CommunicationsInterface;

public class NetworkView extends VBox {
//	private ObservableList<Button> cButtons;
	private CommunicationsInterface comint;
	private ProgressIndicator pi;
	private Label pleasewait;
	
//	public ObservableList<Button> getCButtons(){
//		return cButtons;
//	}
	
	public NetworkView(ArrayList<String> ipList, CommunicationsInterface comint){
		
		pleasewait = new Label("Please Wait...");
		pi = new ProgressIndicator(-1);
		
		this.comint = comint;
//		cButtons = FXCollections.observableArrayList();
		
		HBox hspace = new HBox();
		HBox.setHgrow(hspace, Priority.ALWAYS);
		Label titleLabel = new Label("Network");
		titleLabel.setFont(new Font(20));
		
		HBox networktop = new HBox();
		this.setNotBusy();
		networktop.getChildren().addAll(titleLabel, hspace, pi, pleasewait);
		
		
		this.getChildren().add(networktop);

		HBox connectoptions = new HBox();
		
		TextField ipField = new TextField();
		HBox.setHgrow(ipField, Priority.ALWAYS);
		
		Button cnButton = new Button("Create Network");
		cnButton.setOnAction((event) -> {
			setDisable();
			comint.netconfig = NetworkConfiguration.createInitial();
			comint.conNode.Connect(comint.netconfig);
			setInfoViewText("Network: Connected");
			comint.lgui.getStage().show();
        });
		
		Button ciButton = new Button("Connect IP");
		ciButton.setOnAction((event) -> {
			String ipAddress = ipField.getText();
			
			if(validateIP(ipAddress)){
	        	System.out.println("Connect to " + ipAddress);
	        	if(tryConnect(ipAddress)){
	        		setDisable();
	        		setInfoViewText("Network: Connected");
	    			comint.lgui.getStage().show();
	        	} else
	        		connectionFail();
			}
        });
		
		Button slButton = new Button("Scan LAN");
		slButton.setOnAction((event) -> {
//			slButton.setDisable(true);
			setBusy();
			Thread slthread = new Thread() {
			    public void run() {	    	
					comint.netconfig = NetworkConfiguration.createInitial();
					ArrayList<String> ipList = comint.conNode.GetLanPeers(comint.netconfig);
					Platform.runLater(new Runnable() {
					    @Override
					    public void run() {
					        //if you change the UI, do it here !
					    	refresh(ipList);
							setNotBusy();
					    	
					    }
					});
			    }  
			};
			
			try {
				slthread.start();
			} catch (IllegalStateException e){
			}
        });
		
		connectoptions.getChildren().addAll(cnButton, ipField, ciButton, slButton);
		this.getChildren().add(connectoptions);
		
		Region spacer = new Region();
		this.getChildren().add(spacer);
		
		comint.lgui.getStage().setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent we) {
				if(comint.conNode.node.isConnected()){
					comint.conNode.node.disconnect();
					setEnable();
					setInfoViewText("Network: Disconnected");
				}
			}
		});   

		
		Collections.sort(ipList);
		for(String ip : ipList){
			addComputer(ip);
		}
	}

	public void refresh(ArrayList<String> ipList){
		if(ipList == null) return;
		this.getChildren().remove(2, this.getChildren().size());
//		this.cButtons.clear();
		
		Collections.sort(ipList);
		for(String ip : ipList){
			addComputer(ip);
		}
	}
	
	public NetworkView(String ipAddress){
		addComputer(ipAddress);
	}
	
	private void addComputer(String ipAddress){

        InetAddress ip;
        String hostname;
        try {
            ip = InetAddress.getByName(ipAddress);
            hostname = ip.getHostName();
 
        } catch (UnknownHostException e) {
        	hostname = ipAddress;
        }

		Label label = new Label(hostname, new ImageView(new Image(getClass().getResourceAsStream("/images/computer_icon.png"))));
		
		HBox hspace = new HBox();
		HBox.setHgrow(hspace, Priority.ALWAYS);
		
		Button btn = new Button("Connect");
		btn.setOnAction(event -> {
			// TODO connect to other Instance
        	System.out.println("Connect to " + ipAddress);
        	if(tryConnect(ipAddress)){
        		setDisable();
        		setInfoViewText("Network: Connected");
    			comint.lgui.getStage().show();
        	} else
        		connectionFail();
        });

//		this.cButtons.add(btn);
		
		HBox box = new HBox(20);

		box.getChildren().addAll(label, hspace, btn);
		
		this.getChildren().add(box);
	}
	
	private boolean tryConnect(String ip){
		setBusy();
		try {
			comint.netconfig = NetworkConfiguration.create(InetAddress.getByName(ip));
		} catch (UnknownHostException e) {
			setNotBusy();
			return false;
		}
		if(comint.conNode.Connect(comint.netconfig)){
//			this.setDisable(true);
			setNotBusy();
			return true;
		}
		setNotBusy();
		return false;
	}
	
	private void connectionFail(){
		if(!comint.conNode.node.isConnected()){
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Connection Failed");
			alert.setHeaderText("Connection Failed");
			alert.show();
		}
	}
	
	public void setInfoViewText(String text){
		((Label) comint.pgui.infoView.getChildren().get(1)).setText(text);
	}
	
//code from: http://javafxforbeginners.blogspot.com/2015/08/javafx-validating-text-field-with-ip.html
	public boolean validateIP(final String ip) {
        Pattern pattern;
        Matcher matcher;
        String IPADDRESS_PATTERN
                = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
                + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
                + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
                + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
        pattern = Pattern.compile(IPADDRESS_PATTERN);
        matcher = pattern.matcher(ip);
        return matcher.matches();
    }
	
	private void setBusy(){
		this.setDisable(true);
		pi.setVisible(true);
		pleasewait.setVisible(true);
	}
	
	private void setNotBusy(){
		this.setDisable(false);
		pi.setVisible(false);
		pleasewait.setVisible(false);
	}
	
	private void setDisable(){
		this.setDisable(true);
	}
	
	private void setEnable(){
		this.setDisable(false);
	}
	
	private class LabelContextMenu extends ContextMenu {
        public LabelContextMenu(Label label) {

            MenuItem item = new MenuItem("Connect");
            item.setOnAction(event -> {
            	// TODO
            	// Connect to the ip address
            	System.out.println("Connect to " + label.getText());
                event.consume();
            });
            getItems().add(item);
        }
    }	
}
