package ui;

import java.io.IOException;

import client.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public abstract class Base {
	Application app;
	
	public Base(Application app) {
		this.app = app;
	}
	
	@FXML
	public void onMenuBtn(ActionEvent e) {
		app.showMainMenu();
	}
	
	@FXML
	public void onLogoutBtn(ActionEvent e) {
		app.logout();
	}
	
	public void start(Stage window, String resource) {
		Parent root;
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(resource));
			loader.setController(this);
			root = loader.load();
			Scene scene = new Scene(root);
			window.setScene(scene);
			window.setResizable(false);
			window.setTitle("Nexus");
			
			window.setOnCloseRequest(e -> System.exit(0));
			window.show();
		} catch (IOException e) {
			
			e.printStackTrace();
		}

	}
}
