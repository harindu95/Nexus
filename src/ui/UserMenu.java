package ui;

import java.io.IOException;

import client.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class UserMenu {

	Application app;

	@FXML
	Label username_label;

	public UserMenu(Application app) {
		this.app = app;
	}

	public void initialize() {
		username_label.setText(app.getUser().getUsername());
	}

	public void start(Stage window) {
		Parent root;
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/UserMenu.fxml"));
			loader.setController(this);
			root = loader.load();
			Scene scene = new Scene(root);
//			this.initialize();
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
	void handleNewGameBtn(ActionEvent e) {

		Stage stage = (Stage) ((Button) e.getSource()).getScene().getWindow();
		NewGame newGame = new NewGame(app);
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
	
	
	@FXML
	void handleJoinGame(ActionEvent e) {
		Stage stage = (Stage) ((Button) e.getSource()).getScene().getWindow();
		ViewGames viewGames = new ViewGames(app);
		app.viewGames(viewGames);
		viewGames.start(stage);
		
		
	}
}
