/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import centralprocessor.CommunicationsInterface;
import centralprocessor.PERSYSTSession;

/**
 *
 * @author Jonathan Sahagun, Jonathan Song
 */
public class PersystGUI {

    private BorderPane root;
    private Stage pstage;
    private CommunicationsInterface comint;
    private File selectedFile;	// selectedFile lets the center view know what file is selected
    public VBox infoView;
    public DownloadView downView;
    private HBox fileView;
    
    //takes in communicationsinterface to call functions later
    public PersystGUI(CommunicationsInterface comint){
    	this.comint = comint;
    	selectedFile = comint.getRootFolder();
    }

    public void start(Stage primaryStage) {
    	this.pstage = primaryStage;
    	this.pstage.setTitle("PERSYST");

    	
    	fileView = createFileView(selectedFile);
        root = new BorderPane();
        
        root.setLeft(createLeft());
        root.setTop(createTopMenu());
        root.setCenter(createRight());
        
		comint.lgui.getStage().setOnHidden(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent we) {
				System.out.println("GUI Login worked");
				((Label) infoView.getChildren().get(0)).setText("Username: " + PERSYSTSession.usr.getUsername());;
			}
		}); 

        Scene scene = new Scene(root, 800, 600);
        this.pstage.setScene(scene);
    }
    
    public Stage getStage(){
    	return this.pstage;
    }
    
	/**
	 * An SplitPane holding the tree directory and the current network
	 * 
	 * @return A SplitPane for the left panel of the GUI
	 */
    private SplitPane createLeft(){
        SplitPane splitPane = new SplitPane();
        splitPane.setPrefWidth(400);
        splitPane.setOrientation(Orientation.VERTICAL);
        splitPane.getItems().add(treeDirectory());    	


        // TODO Get ip addresses
        //PERSYSTSession.usr.getConfiguration("ipaddress");
		ArrayList<String> iplist = new ArrayList<>();
        try {
        	iplist.add(InetAddress.getLocalHost().getHostAddress());
        	//iplist.addAll(comint.conNode.GetLanPeers(comint.netconfig));
        } catch (UnknownHostException e) {}

        splitPane.getItems().add(new NetworkView(iplist, comint));
        return splitPane;
    }

	/**
	 * An SplitPane holding the info on the file selected and the current downloads
	 * 
	 * @return A SplitPane for the left panel of the GUI
	 */
    private SplitPane createRight(){
        SplitPane splitPane = new SplitPane();
        splitPane.setOrientation(Orientation.VERTICAL);
        splitPane.getItems().add(fileView);
        
        infoView = new VBox();
        Label username = new Label("Username: ");
        Label conStatus = new Label("Network: Disconnected");
        Button cComplete = new Button("Clear Completed Downloads");
        cComplete.setOnAction((event) -> {
			PERSYSTSession.comm.ftrans.removeComplete();
		});
        infoView.getChildren().addAll(username, conStatus, cComplete);
        splitPane.getItems().add(infoView);
        downView = new DownloadView(comint);
        splitPane.getItems().add(downView);
        
        return splitPane;
    }

	/**
	 * Creates a populated MenuBar for the main gui of PERSYST
	 * menuItems File -> Exit		Closes the window
	 * menuItems Help -> Sorry		Does Nothing
	 * menuItems Options-> ChooseRootFolder		Opens the ChooseRootFolder dialog box
	 * menuItems Options-> Refresh				Redraws most of the nodes in the gui
	 * menuItems Options-> Configurations		Open the Config dialog box
	 * 
	 */
    public MenuBar createTopMenu() {
        Menu fileMenu = new Menu("File");

        MenuItem ExitMenuItem = new MenuItem("Exit");
        fileMenu.getItems().add(ExitMenuItem);
        ExitMenuItem.setOnAction((event) -> {
        	this.comint.sendShutdownSignal();
        });     

        
        Menu options = new Menu("Options");
        
//        MenuItem rootMenuItem = new MenuItem("Choose Root Folder...");
//        options.getItems().add(rootMenuItem);
//        rootMenuItem.setOnAction((event) -> {
//            ChooseRootFolder dialog = new ChooseRootFolder(comint);
//            dialog.start(new Stage());
//            updateGui();
//        });     
        
        MenuItem refreshMenuItem = new MenuItem("Refresh");
        options.getItems().add(refreshMenuItem);
        refreshMenuItem.setOnAction((event) -> {
            updateGui();
        });     
        
        
        MenuItem configMenuItem = new MenuItem("Configurations...");
        options.getItems().add(configMenuItem);
        configMenuItem.setOnAction((event) -> {
        	if(PERSYSTSession.usr == null) return;
        	this.comint.updateConfigurations();
            ConfigGUI dialog = new ConfigGUI(comint);
            dialog.start(new Stage());
            dialog.getStage().initModality(Modality.WINDOW_MODAL);
            dialog.getStage().initOwner(pstage);
            dialog.getStage().setOnHidden(new EventHandler<WindowEvent>() {
            	@Override
				public void handle(WindowEvent we) {
            		updateGui();
				}
			});

            dialog.getStage().show();
        });     
        
        
        Menu help = new Menu("Help");
        help.getItems().add(new MenuItem("Sorry No Help"));

        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(fileMenu, options, help);
        return menuBar;
    }

        
	/**
	 * Recreates the file directory tree and the file view
	 */
    private void updateGui() {
    	selectedFile = comint.getRootFolder();
    	updateFileView();
    	updateTreeDirectory();
//    	updateNetWorkView();
    }
    
	/**
	 * Recreates the file directory tree
	 */
    private void updateTreeDirectory() {
    	SplitPane splitPane = (SplitPane) root.getLeft();
    	splitPane.getItems().remove(0);
    	splitPane.getItems().add(0, treeDirectory());
    }
    
	/**
	 * Recreates the file directory tree and the file view
	 */
    private void updateNetWorkView() {
    	SplitPane splitPane = (SplitPane) root.getLeft();
    	NetworkView networkView = (NetworkView)splitPane.getItems().get(1);
    	
    	
    	
    	// TODO Get the iplist

        
        ArrayList<String> iplist = new ArrayList<>();
		iplist.add("8.8.8.8");
        try {
        	iplist.add(InetAddress.getLocalHost().getHostAddress());
 
        } catch (UnknownHostException e) {
        
        }

		networkView.refresh(iplist);
    }
    
	/**
	 * Updates the file view on the top left when this is called with the currently selected file
	 * 
	 */    
    void updateFileView(){
    	SplitPane splitPane = (SplitPane) root.getCenter();
    	splitPane.getItems().remove(0);
    	splitPane.getItems().add(0, createFileView(selectedFile));
    }

    
        
	/**
	 * An HBox that displaces information about the selected file
	 * 
	 * @param File
	 *            The file should be the root folder
	 * @return A HBox with info about the selected file
	 */
    public HBox createFileView(File file) {
    	if(file == null) file = new File(System.getProperty("user.home") + "/Desktop");
        HBox hbox = new HBox();
        hbox.setSpacing(30);
        if (file.isDirectory()) {
            hbox.getChildren().add(folderLarge());
        } else if (file.isFile()) {
        	String ext;
        	try {
        		 ext = file.getPath().substring(file.getPath().lastIndexOf('.'));
        	} catch(Exception e) {
        		 ext = ".invalid";
        	}
            if (ext.equalsIgnoreCase(".png") || ext.equalsIgnoreCase(".jpg")
                    || ext.equalsIgnoreCase(".jpeg") || ext.equalsIgnoreCase(".gif")) {

                System.out.print(file.getPath());
                Image image = new Image("File:\\" + file.getPath());
                if (image.getHeight() > 800 || image.getWidth() > 800) {
                    if (image.getHeight() < image.getWidth()) {
                        image = new Image("File:\\" + file.getPath(), 800, 0, true, false, true);
                    } else {
                        image = new Image("File:\\" + file.getPath(), 0, 800, true, false, true);
                        
                    }
                }
                ImageView imageView = new ImageView(image);
                hbox.getChildren().add(imageView);

            } else if(ext.equalsIgnoreCase(".txt")) {
            	Text textNode = new Text("                                       \n");
            	try {
        			BufferedReader rd = new BufferedReader(new FileReader(file));
        			while(rd.ready()) {
        				textNode.setText(textNode.getText() + "\n" + rd.readLine());
        			}
        			rd.close();
            	} catch(Exception e) {}
            	ScrollPane scroll = new ScrollPane();
            	scroll.setHbarPolicy(ScrollBarPolicy.AS_NEEDED);
            	scroll.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
            	scroll.setContent(textNode);
            	
       
            	hbox.getChildren().add(scroll);
            	
            } else {
                hbox.getChildren().add(fileLarge());
            }
        }

        VBox vbox = new VBox();

        Path path = file.toPath();
        try {
            BasicFileAttributes attr = Files.readAttributes(path, BasicFileAttributes.class);

            Text name = new Text("Name: " + path.getFileName());
            Text creationTime = new Text("creationTime: " + attr.creationTime());
            Text lastAccessTime = new Text("lastAccessTime: " + attr.lastAccessTime());
            Text lastModifiedTime = new Text("lastModifiedTime: " + attr.lastModifiedTime());

            Text isDirectory = new Text("isDirectory: " + attr.isDirectory());
            Text isOther = new Text("isOther: " + attr.isOther());
            Text isRegularFile = new Text("isRegularFile: " + attr.isRegularFile());
            Text isSymbolicLink = new Text("isSymbolicLink: " + attr.isSymbolicLink());

            // MB
            double sizeMB = Math.round(attr.size() / 1024.0 / 1024.0 * 100) / 100.0;
            // KB
            double sizeKB = Math.round(attr.size() / 1024.0 * 100) / 100.0;

            Text size;
            if (sizeMB > 1.0) {
                size = new Text("size: " + sizeMB + "MB");
            } else {
                size = new Text("size: " + sizeKB + "KB");
            }

            vbox.getChildren().addAll(
                    name,
                    creationTime,
                    lastAccessTime,
                    lastModifiedTime,
                    isDirectory,
                    isOther,
                    isRegularFile,
                    isSymbolicLink,
                    size
            );
            
            hbox.getChildren().add(vbox);
        } catch (IOException e) {

        }
        
        return hbox;
    }
   
	/**
	 * Creates a image and puts that into an ImageView for the folder icon
	 * 
	 * @return A ImageView for the folder icon
	 */
    private ImageView folderIcon() {
        return new ImageView(new Image(getClass().getResourceAsStream("/images/folder_icon.png")));
    }

	/**
	 * Creates a image and puts that into an ImageView for the file icon
	 * 
	 * @return A ImageView for the file icon
	 */
    private ImageView fileIcon() {
        return new ImageView(new Image(getClass().getResourceAsStream("/images/file_icon.png")));
    }
    
	/**
	 * Creates a image and puts that into an ImageView for the folder image
	 * 
	 * @return A ImageView for the folder image
	 */
    private ImageView folderLarge() {
        return new ImageView(new Image(getClass().getResourceAsStream("/images/folder.png")));
    }

	/**
	 * Creates a image and puts that into an ImageView for the file image
	 * 
	 * @return A ImageView for the file image
	 */
    private ImageView fileLarge() {
        return new ImageView(new Image(getClass().getResourceAsStream("/images/file.png")));
    }
    
	/**
	 * Creates and populates a TreeView that shows the file structure of the root folder
	 * 
	 * @return A TreeView with the file structure of the root folder
	 */
    private TreeView<Pair> treeDirectory() {
        Pair p = new Pair(comint.getRootFolder().getName(), comint.getRootFolder().getPath());
        TreeItem<Pair> rootTreeItem = new TreeItem<>(p, folderIcon());
        makeBranches(rootTreeItem, comint.getRootFolder());

        rootTreeItem.setExpanded(true);

        TreeView<Pair> treeView = new TreeView<>(rootTreeItem);

        treeView.getSelectionModel().selectedItemProperty().addListener(
                ((v, oldValue, newValue) -> {
                    if (newValue != null) {
                        selectedFile = new File(newValue.getValue().y);
                        updateFileView();
                    }
                }));

        treeView.setCellFactory(new Callback<TreeView<Pair>, TreeCell<Pair>>() {
            @Override
            public TreeCell<Pair> call(TreeView<Pair> p) {
                return new ContextMenuTreeCell();
            }
        });

        treeView.setPrefWidth(500);
        return treeView;
    }

	/**
	 * A recursive helper function to go though a file structure and populate TreeItems
	 * 
	 * @param TreeItem
	 * 				The parent TreeItem that will be populated
	 * @param File
	 * 				The location of that file that will populate the TreeItem
	 * @return A TreeView with the file structure of the root folder
	 */    
    void makeBranches(TreeItem<Pair> parent, File source) {
        if (source.isDirectory()) {
            for (String s : source.list()) {
                File scr = new File(source, s);

                if (scr.isDirectory()) {
                    TreeItem<Pair> child = new TreeItem<>(new Pair(s, scr.getPath()), folderIcon());
                    parent.getChildren().add(child);
                    makeBranches(child, scr);
                } else {
                    TreeItem<Pair> child = new TreeItem<>(new Pair(s, scr.getPath()), fileIcon());
                    parent.getChildren().add(child);

                }
            }
        }
    }

	/**
	 * A pair class used to store pairs
	 */
    private class Pair {
        private String x, y;

        public Pair(String x, String y) {
            this.x = x;
            this.y = y;
        }

        public String getX() {
            return x;
        }

        public String getY() {
            return y;
        }

        public void setX(String x) {
            this.x = x;
        }

        public void setY(String y) {
            this.y = y;
        }

        @Override
        public String toString() {
            return x;
        }
    }
    
	/**
	 * A custom TreeCell that adds a context menu to the cells
	 */
    private final class ContextMenuTreeCell extends TreeCell<Pair> {

        private ContextMenu openMenu = new ContextMenu();

        public ContextMenuTreeCell() {

            if (Desktop.isDesktopSupported()) {
                MenuItem openMenuItem = new MenuItem("Show in Windows");
                openMenu.getItems().add(openMenuItem);

                openMenuItem.setOnAction(new EventHandler() {
                    public void handle(Event t) {
                        File file = new File(getTreeItem().getValue().getY());
                        Desktop desktop = Desktop.getDesktop();

                        try {
                            if (file.isDirectory()) {
                                desktop.open(file);
                            } else if (file.isFile()) {
                                desktop.open(file.getParentFile());
                            }
                        } catch (IOException ex) {

                        }
                    }
                });
            }
        }

        @Override
        public void startEdit() {
            super.startEdit();
            setText((String) getItem().x);
            setGraphic(getTreeItem().getGraphic());
        }

        @Override
        public void cancelEdit() {
            super.cancelEdit();

            setText((String) getItem().x);
            setGraphic(getTreeItem().getGraphic());
        }

        @Override
        public void updateItem(Pair item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
                setText(null);
                setGraphic(null);
            } else {
                setText((String) getItem().x);
                setGraphic(getTreeItem().getGraphic());
                setContextMenu(openMenu);
            }
        }
    }
}
