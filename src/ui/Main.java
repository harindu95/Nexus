package ui;

import java.io.IOException;

import client.Client;
import core.Login_Request;
import javafx.application.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class Main extends Application {

	client.Application app;

	public void initialize(client.Application app) {
		 this.app = app; 
	}

	@Override
	public void start(Stage stage) throws Exception {

		
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("fxml/Login.fxml"));
		Parent root = loader.load();
		Scene scene = new Scene(root);
		client.Application app = new client.Application();
		app.setLoginStage(stage);
		Main controller = loader.<Main>getController();
		
		controller.initialize(app);
		stage.setScene(scene);
		stage.setResizable(false);
		stage.show();
	}

	
	public static void main(String[] args) {
		launch(args);
	}

	@FXML
	TextField username_field;
	@FXML
	PasswordField password_field;

	@FXML
	void handleLoginBtn(ActionEvent e) {
		String password = password_field.getText();
		String username = username_field.getText();
		System.out.println(password + "  " + username);
		Stage login = (Stage)password_field.getScene().getWindow();
		
		app.login(username, password);

	}

//	• User login/logout 
//	Database to keep track of users and login credentials
//TODO	• User accounts (profiles) on the game server
//	Database to keep track of user’s WIns, Ranks, games played etc.
//TODO	• Create a game room
//	• List online users in the system
//	• Join or leave a game room
//	• chat in the game room
//	Public
//	• Interactive game playing (real-time update of game status to all players)
//	Live game vs. turn based game?
//	• Handle player disconnection and reconnection (pause and resume game)
//	Keep track of user connection
//	If they are disconnected: 
//	Try to reconnect them 
//	Remove them from the game is fail to connect after 3 attempts
//	• Server checks game movements and calculates points (if applicable to the game)
//	• interface to graphically display the game status
//	• interface to play the game

	
}
