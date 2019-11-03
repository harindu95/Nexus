package ui;

import java.io.IOException;

import client.Application;
import core.ChatMessage;
import core.GameRoom;
import core.User;
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

public class GameLobby {
	Application app;
	
	public GameLobby(Application app){
		this.app = app;
	}

	@FXML
	Label gameName_Label;
	
	
	
	public void start(Stage window) {
		Parent root;
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/lobby.fxml"));
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

	@FXML
	public void onMenuBtn(ActionEvent e) {
		app.showMainMenu();
	}

	@FXML
	ListView<String> players_list;
	
	public void update(GameRoom currentGame) {
		Platform.runLater( new Runnable() {
			
			@Override
			public void run() {
				gameName_Label.setText(currentGame.getName());
				for(User u: currentGame.getUsers()) {
					players_list.getItems().add(u.getUsername());
				}
			}
		});
		
	}

	@FXML 
	TextArea chat;
	
	public void addMsg(ChatMessage chatMsg) {
		String msg = String.format("%s: %s\n", chatMsg.getUsername(), chatMsg.getMessage());
		System.out.println(msg);
		Platform.runLater(new Runnable() {
			
			@Override
			public void run() {
				chat.appendText(msg);
				
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
	



}
