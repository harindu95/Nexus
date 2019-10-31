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

	

	@Override
	public void init() {

	}

	@Override
	public void start(Stage stage) throws Exception {

//		
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("fxml/Login.fxml"));
		Parent root = loader.load();

		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.setResizable(false);
		stage.show();
	}

	static Client client;
	public static void main(String[] args) {
		
		try {
			client = new Client();
			Thread t = new Thread(client);
			t.start();
			launch(args);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
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
		Login_Request req = new Login_Request(username, password);

		if (client == null) {
			System.out.println("Client is null????");
		} else {
			client.setOutput(req);
		}

//		Stage stage = (Stage)password_field.getScene().getWindow();
//		stage.close();
//		Stage window = new Stage();
//		UserMenu userMenu = new UserMenu();
//		userMenu.start(window);

	}

}