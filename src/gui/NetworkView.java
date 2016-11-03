package gui;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class NetworkView extends VBox {

	public NetworkView(ArrayList<String> ipList){
		Label titleLabel = new Label("Network");
		titleLabel.setFont(new Font(20));
		
		this.getChildren().add(titleLabel);
		
		Region spacer = new Region();
		this.getChildren().add(spacer);
		
		
		Collections.sort(ipList);
		for(String ip : ipList){
			addComputer(ip);
		}
	}

	public void refresh(ArrayList<String> ipList){
		this.getChildren().remove(1, this.getChildren().size() - 1);
		
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
		Button btn = new Button("Connect");
		
		btn.setOnAction(event -> {
			// TODO connect to other Instance
        	System.out.println("Connect to " + ipAddress);
        });

		HBox box = new HBox(20);

		box.getChildren().addAll(label, btn);
		
		this.getChildren().add(box);
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
