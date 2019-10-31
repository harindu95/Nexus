package core;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class NewGame {
	public void start(Stage window) {
        Parent root;
		try {
			root = FXMLLoader.load(getClass().getResource("fxml/makeNewGame.fxml"));
		    Scene scene =  new Scene(root);
	        window.setScene(scene);
	        window.setResizable(false);
	        window.setTitle("Nexus");
	        window.show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    
    }
}
