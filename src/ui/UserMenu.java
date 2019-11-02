package ui;

import java.io.IOException;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class UserMenu {

	String username = "Username";
		
	@FXML
	Label username_label;
		
	
	public void initialize(String username) {
		 username_label.setText(username);
	}
	
	public void start(Stage window, String username) {
        Parent root;
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/UserMenu.fxml"));
			root = loader.load();
			Scene scene =  new Scene(root);
		    UserMenu controller = loader.<UserMenu>getController();
		    controller.initialize(username);
	        window.setScene(scene);
	        window.setResizable(false);
	        window.setTitle("Nexus");
	        window.setOnCloseRequest(e -> System.exit(0));
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
