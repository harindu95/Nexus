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

}
