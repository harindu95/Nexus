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

public class ListPlayers extends Base {

	public ListPlayers(Application app) {
		super(app);
	}

	public void start(Stage window) {
		super.start(window, "fxml/listPlayers.fxml");
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

	
}
