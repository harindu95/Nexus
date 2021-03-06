package ui;

import java.io.IOException;
import java.util.List;

import client.Application;
import core.ChatMessage;
import core.GameRoom;
import core.User;
import game.Player;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class GameLobby extends Base {

	public GameLobby(Application app) {
		super(app);
	}

	@FXML
	Label gameName_Label;

	public void start(Stage window) {
		super.start(window, "fxml/lobby.fxml");
	}

	@FXML
	ListView<String> players_list;

	public void update(GameRoom currentGame) {
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				gameName_Label.setText(currentGame.getName());
				for (User u : currentGame.getUsers()) {
					players_list.getItems().add(u.getUsername());
				}
			}
		});

	}

	@FXML
	TextArea chat;

	public void setChat(String chatMsg) {

		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				chat.setText(chatMsg);
			}
		});

	}

	@FXML
	TextField msg_field;

	@FXML
	public void handleSend(ActionEvent e) {
		String msg = msg_field.getText();
		msg_field.setText("");
		app.sendMsg(msg);
	}

	@FXML
	public void handleStartGameBtn(ActionEvent e) {
		app.showGame();

	}

	public String getChat() {
		return chat.getText();
	}

	public void updateUsers(List<Player> players) {
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				players_list.getItems().clear();
				for (Player p : players) {
					players_list.getItems().add(p.getName());
				}
			}
		});

	}
	
	@FXML
	public void handleLeaveGame(ActionEvent e) {
		app.leaveGame();
	}

}
