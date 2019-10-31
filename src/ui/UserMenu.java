package ui;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class UserMenu {

	public void start(Stage window) {
        Parent root;
		try {
			root = FXMLLoader.load(getClass().getResource("fxml/UserMenu.fxml"));
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
	
	
	
	@FXML
	void handleNewGameBtn(ActionEvent e) {
	
		Stage stage = (Stage)((Button)e.getSource()).getScene().getWindow();
		NewGame newGame = new NewGame();
		newGame.start(stage);
		
	}
}
