package ui;

import java.io.IOException;

import client.Application;
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

	Application app;

	@FXML
	Label username_label;

	public void initialize(Application app) {
		this.app = app;
		username_label.setText(app.getUser().getUsername());
	}

	public void start(Stage window, Application app) {
		Parent root;
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/UserMenu.fxml"));
			root = loader.load();
			Scene scene = new Scene(root);
			UserMenu controller = loader.<UserMenu>getController();
			controller.initialize(app);
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

		Stage stage = (Stage) ((Button) e.getSource()).getScene().getWindow();
		NewGame newGame = new NewGame();
		newGame.start(stage);

	}

	@FXML
	void handleViewOnlineUsers(ActionEvent e) {
		Stage stage = (Stage) ((Button) e.getSource()).getScene().getWindow();
		ListPlayers listPlayers = new ListPlayers();
		app.requestOnlineUsers();
		app.setListPlayers(listPlayers);
		listPlayers.start(stage, app);
		
	}
}
