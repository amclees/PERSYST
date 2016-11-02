/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TreeCell;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.util.Callback;
import javax.swing.filechooser.FileSystemView;

import centralprocessor.CommunicationsInterface;

/**
 *
 * @author Sahagun, Jonathan Song
 */
public class PersystGUI extends Application {

    private BorderPane root;
    private Stage pstage;
    private CommunicationsInterface comint;

    //takes in communicationsinterface to call functions later
    public PersystGUI(CommunicationsInterface comint){
    	this.comint = comint;
    }

    private ImageView folderIcon() {
        return new ImageView(new Image(getClass().getResourceAsStream("/images/folder_icon.png")));
    }

    private ImageView fileIcon() {
        return new ImageView(new Image(getClass().getResourceAsStream("/images/file_icon.png")));
    }

    private ImageView folderLarge() {
        return new ImageView(new Image(getClass().getResourceAsStream("/images/folder.png")));
    }

    private ImageView fileLarge() {
        return new ImageView(new Image(getClass().getResourceAsStream("/images/file.png")));
    }

//    private final File target_dir = new File("C:\\Users\\Jpox\\Desktop\\Delete Me\\");
    private File target_dir = FileSystemView.getFileSystemView().getDefaultDirectory();

    @Override
    public void start(Stage primaryStage) {

    	this.pstage = primaryStage;
//        StackPane root = new StackPane();
//        root.getChildren().add(btn);
    	this.pstage.setTitle("PERSYST");

        chooseRoot(this.pstage);

        root = new BorderPane();

        root.setLeft(treeDirectory());
        root.setTop(createTopMenu());
        root.setCenter(fileView(selectedFile));

        Scene scene = new Scene(root, 600, 250);
        this.pstage.setScene(scene);
//        this.pstage.show();
    }
    
    public Stage getStage(){
    	return this.pstage;
    }

    /**
     * @param args the command line arguments
     */
//    public static void main(String[] args) {
//        launch(args);
//    }

    private void chooseRoot(Stage primaryStage) {
        //Set to user directory or go to default if cannot access
        String userDirectoryString = System.getProperty("user.home");
        File userDirectory = new File(userDirectoryString);
        if (!userDirectory.canRead()) {
            userDirectory = new File("C:/");
        }

        DirectoryChooser dirChooser = new DirectoryChooser();
        dirChooser.setTitle("Choose Root Folder");
        dirChooser.setInitialDirectory(userDirectory);
        File selectedFile = dirChooser.showDialog(primaryStage);
        target_dir = selectedFile;
        this.selectedFile = selectedFile;

//                FileChooser fileChooser = new FileChooser();
//                fileChooser.setTitle("Open Resource File");
//                fileChooser.getExtensionFilters().addAll(
//                new ExtensionFilter("Text Files", "*.txt"),
//                new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"),
//                new ExtensionFilter("Audio Files", "*.wav", "*.mp3", "*.aac"),
//                new ExtensionFilter("All Files", "*.*"));
        //File selectedFile = fileChooser.showOpenDialog(primaryStage);
    }

    private File selectedFile = null;

    public MenuBar createTopMenu() {
        Menu menu1 = new Menu("File");
        Menu menu2 = new Menu("Options");
        Menu menu3 = new Menu("Help");

        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(menu1, menu2, menu3);
        return menuBar;
    }

    public HBox fileView(File file) {
        HBox hbox = new HBox();
        if (file.isDirectory()) {
            hbox.getChildren().add(folderLarge());
        } else if (file.isFile()) {
            String ext = file.getPath().substring(file.getPath().lastIndexOf('.'));
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

            } else {
                hbox.getChildren().add(fileLarge());
            }
        }

        VBox vbox = new VBox();

        Path path = file.toPath();
        try {
            BasicFileAttributes attr = Files.readAttributes(path, BasicFileAttributes.class);

//            vbox.getChildren().add(vbox);
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

    private TreeView treeDirectory() {
        Pair p = new Pair(target_dir.getName(), target_dir.getPath());

        TreeItem<Pair> rootTreeItem = new TreeItem<>(new Pair(target_dir.getName(), target_dir.getPath()), folderIcon());
        makeBranches(rootTreeItem, target_dir);

        rootTreeItem.setExpanded(true);

        TreeView<Pair> treeView = new TreeView<>(rootTreeItem);

        treeView.getSelectionModel().selectedItemProperty().addListener(
                ((v, oldValue, newValue) -> {
                    if (newValue != null) {
//                        Path f = new File(newValue.getValue().y).toPath();
                        selectedFile = new File(newValue.getValue().y);
                        root.setCenter(fileView(selectedFile));
//                        try {
//                            BasicFileAttributes attr = Files.readAttributes(f, BasicFileAttributes.class);
//
//                            System.out.println("creationTime: " + attr.creationTime());
//                            System.out.println("lastAccessTime: " + attr.lastAccessTime());
//                            System.out.println("lastModifiedTime: " + attr.lastModifiedTime());
//
//                            System.out.println("isDirectory: " + attr.isDirectory());
//                            System.out.println("isOther: " + attr.isOther());
//                            System.out.println("isRegularFile: " + attr.isRegularFile());
//                            System.out.println("isSymbolicLink: " + attr.isSymbolicLink());
//
//                            // MB
//                            double sizeMB = Math.round(attr.size() / 1024.0 / 1024.0 * 100) / 100.0;
//                            // KB
//                            double sizeKB = Math.round(attr.size() / 1024.0 * 100) / 100.0;
//
//                            if (sizeMB > 1.0) {
//                                System.out.println("size: " + sizeMB + "MB");
//                            } else {
//                                System.out.println("size: " + sizeKB + "KB");
//                            }
//                        } catch (IOException e) {
//
//                        }
//                        System.out.println(newValue.getValue().y);
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
