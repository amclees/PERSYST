package userinterface;

import javafx.application.Application;

public class LoadingTest {
	
    public static void main(String[] args) {
        new Thread() {
            @Override
            public void run() {
                javafx.application.Application.launch(LoadScreen.class);
            }
        }.start();
        
        LoadScreen loadScreen = LoadScreen.waitForLoadScreen();
        loadScreen.setLabelText("Loading stuff here");
        
    }

}
