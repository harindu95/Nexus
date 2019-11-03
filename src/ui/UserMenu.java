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

public class UserMenu extends Base{
	@FXML
	Label username_label;

	public UserMenu(Application app) {
		super(app);
	}

	public void initialize() {
		username_label.setText(app.getUser().getUsername());
	}

	public void start(Stage window) {
		super.start(window, "fxml/UserMenu.fxml");
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
		ListPlayers listPlayers = new ListPlayers(app);
		app.requestOnlineUsers();
		app.setListPlayers(listPlayers);
		listPlayers.start(stage);
	}
	
	
	@FXML
	void handleJoinGame(ActionEvent e) {
		Stage stage = (Stage) ((Button) e.getSource()).getScene().getWindow();
		ViewGames viewGames = new ViewGames(app);
		app.viewGames(viewGames);
		viewGames.start(stage);
			
	}
}
