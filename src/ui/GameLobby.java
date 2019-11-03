package ui;

import java.io.IOException;

import client.Application;
import core.Game;
import core.User;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
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
	
	public void update(Game currentGame) {
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

}
