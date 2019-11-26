package ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import client.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import game.Board;
import game.Player;
import game.Tile;

public class Game extends Base {

	Player current;
	List<Player> players;
	int gameId = 0;
	int turn;

	public Game(Application app, int gameId, String username) {
		super(app);
		board = new Board();
		this.gameId = gameId;
		current = new Player(username, board);
		players = new ArrayList<>();
		players.add(current);
	}

	public Game() {
		super(null);
		board = new Board();
		players = new ArrayList<>();
	}

	public void start(Stage window) {
		super.start(window, "fxml/Game.fxml");
		Parent root;
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/Game.fxml"));
			loader.setController(this);
			root = loader.load();
			Scene scene = new Scene(root);
			window.setScene(scene);
			window.setResizable(false);
			window.setTitle("Nexus");
			draw();
			window.setOnCloseRequest(e -> System.exit(0));
			window.show();
		} catch (IOException e) {

			e.printStackTrace();
		}

	}

	@FXML
	Canvas canvas;

	@FXML
	Button buyBtn;

	@FXML
	TextArea properties;

	@FXML
	public void handleBuy(ActionEvent e) {
		current.buy();
		draw();
//		app.gameChanged();
	}

	@FXML
	public void handleRoll(ActionEvent e) {
		int r = board.rollDice();
		current.move(r);
		draw();
		btnRollDice.setDisable(true);
//		app.gameChanged();
	}

	@FXML
	Label playerLocation;

	@FXML
	Label playerName;

	@FXML
	Label money;

	@FXML
	Circle color;

	@FXML
	Label currentTurn;

	@FXML
	Button rentBtn;

	Board board;

	public void draw() {
		GraphicsContext gc = canvas.getGraphicsContext2D();
		board.draw(gc);

		playerName.setText(current.getName());
		color.setFill(current.getColor());
		money.setText(String.format("$ %.2f", current.getMoney()));
		properties.setText(current.getProperties());
		Tile tile = current.getLocation();
		playerLocation.setText(tile.toString());

		buyBtn.setDisable(!tile.canBuy());
		rentBtn.setDisable(!tile.canRent());

		for (Player p : players) {
			p.draw(gc);

		}

		drawTurn();
	}

	public void updateRoll(String username, int dice) {
		for (Player p : players) {
			if (p.getName().equals(username)) {
				p.move(dice);
			}
		}
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				draw();

			}
		});

	}

	public List<Player> getPlayers() {

		return players;
	}

	public void addPlayer(String username) {
		Player p = new Player(username);
		p.setId(players.size());
		players.add(p);
	}

	public void setPlayers(List<Player> players) {
		this.players = players;
	}

	public void update(List<Player> players) {
		this.players = players;
		int i = 0;
		for (Player p : players) {
			p.setBoard(board);
			p.setId(i);
			i++;
			if (p.getName().equals(current.getName())) {
				current = p;
			}
		}

		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				if (canvas != null)
					draw();

			}
		});

	}

	public int getId() {

		return gameId;
	}

	public void setTurn(int t) {
		turn = t;
	}

	public int getTurn() {

		return turn;
	}

	@FXML
	Button nextTurnBtn;

	@FXML
	public void handleNextTurn(ActionEvent e) {
		turn += 1;
		turn = (turn % players.size());
		draw();
		app.gameChanged();
	}

	@FXML
	Button btnRollDice;

	public void drawTurn() {
		turn = (turn % players.size());
		currentTurn.setText(players.get(turn).getName());
		if (current != players.get(turn)) {
			// disable buttons
			buyBtn.setDisable(true);
			rentBtn.setDisable(true);
			nextTurnBtn.setDisable(true);
			btnRollDice.setDisable(true);
		} else {
			nextTurnBtn.setDisable(false);
			btnRollDice.setDisable(false);
		}
	}

	@FXML
	TextArea chat;

	public void setChat(String chatMsg) {

		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				if (chat != null)
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

	public void removePlayer(String username) {
		boolean found = false;
		for(Player p: players) {
			if(p.getName().equals(username)) {
				players.remove(p);
				found = true;
				break;
			}
		}
		if(!found) {
			System.out.println(username + " not found !!");
		}
		
	}
	
	@FXML
	public void handleLeaveGame(ActionEvent e) {
		app.leaveGame();
	}
}
