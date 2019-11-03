
package ui;

import java.io.IOException;
import java.util.List;

import client.Application;
import core.GameRoom;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class ViewGames {
	Application app;
	List<GameRoom> games;
	
	public ViewGames(Application app) {
		this.app = app;
	}

	public void initialize() {
		gamesTable.setPlaceholder(new Label("No current games available."));
		name_col.setCellValueFactory(new PropertyValueFactory<GameRoom, String>("name"));
		id_col.setCellValueFactory(new PropertyValueFactory<GameRoom, Integer>("id"));
		max_col.setCellValueFactory(new PropertyValueFactory<GameRoom, Integer>("maxPlayers"));
	}
	
	public void start(Stage window) {
		Parent root;
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/listGames.fxml"));
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
	TableView<GameRoom> gamesTable;
	
	@FXML
	TableColumn<GameRoom, String> name_col;
	
	@FXML
	TableColumn<GameRoom, Integer> id_col;
	
	@FXML
	TableColumn<GameRoom, Integer> max_col;

	public void update(List<GameRoom> gameList) {
		this.games = gameList;
		gamesTable.setItems(FXCollections.observableArrayList(gameList));
	}

	@FXML
	public void onClick(MouseEvent e) {
		GameRoom test = new core.GameRoom("game_name__@@@", 12);
		gamesTable.getItems().add(test);
	}
	
	@FXML
	public void onMenuBtn(ActionEvent e) {
		app.showMainMenu();
	}
	
	@FXML
	public void handleJoinBtn(ActionEvent e) {
		GameRoom selected = gamesTable.getSelectionModel().getSelectedItem();
		app.joinGame(selected.getId());
	}

}
