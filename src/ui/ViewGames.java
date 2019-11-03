
package ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import client.Application;
import core.Game;
import core.User;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Callback;

public class ViewGames {
	Application app;
	List<Game> games;
	
	public ViewGames(Application app) {
		this.app = app;
	}

	public void initialize() {
		gamesTable.setPlaceholder(new Label("No current games available."));
		name_col.setCellValueFactory(new PropertyValueFactory<Game, String>("name"));
		id_col.setCellValueFactory(new PropertyValueFactory<Game, Integer>("id"));
		max_col.setCellValueFactory(new PropertyValueFactory<Game, Integer>("maxPlayers"));
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
	TableView<Game> gamesTable;
	
	@FXML
	TableColumn<Game, String> name_col;
	
	@FXML
	TableColumn<Game, Integer> id_col;
	
	@FXML
	TableColumn<Game, Integer> max_col;

	public void update(List<Game> gameList) {
		this.games = gameList;
		gamesTable.setItems(FXCollections.observableArrayList(gameList));
	}

	@FXML
	public void onClick(MouseEvent e) {
		Game test = new core.Game("game_name__@@@", 12);
		gamesTable.getItems().add(test);
	}
	
	@FXML
	public void onMenuBtn(ActionEvent e) {
		app.showMainMenu();
	}
	
	@FXML
	public void handleJoinBtn(ActionEvent e) {
		Game selected = gamesTable.getSelectionModel().getSelectedItem();
		app.joinGame(selected.getId());
	}

}
