package ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import client.Application;
import core.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class ListPlayers {
	Application app;

	public void start(Stage window, Application app) {
		Parent root;
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/listPlayers.fxml"));
			loader.setController(this);
			this.app = app;
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

	
	@FXML
	ListView<String> onlinelist;

	
	public void update(List<User> online) {

		for (User u : online) {
			
			onlinelist.getItems().add(u.getUsername());
			System.out.println("UPDATE:: " + u.getUsername());

		}

	}

	@FXML
	public void handleMouseClick(MouseEvent e) {

		System.out.println("Mouse Clicked");
		
		onlinelist.getItems().add("username1");
	}
	
	@FXML
	public void onMenuBtn(ActionEvent e) {
		app.showMainMenu();
	}

}
