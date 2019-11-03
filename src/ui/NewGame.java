package ui;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParsePosition;

import client.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.stage.Stage;

public class NewGame {
	Application app;
	
	public NewGame(Application app) {
		this.app = app;
	}
		
	public void start(Stage window) {
        Parent root;
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/makeNewGame.fxml"));
			loader.setController(this);
			root = loader.load();
		    Scene scene =  new Scene(root);
		    window.setScene(scene);
	        window.setResizable(false);
	        window.setTitle("Nexus");
	        window.show();
		} catch (IOException e) {
		
			e.printStackTrace();
		}
    
    }
	
	@FXML
	TextField name_field;
	
	@FXML
	TextField max_field;
	
	@FXML
	public void initialize() {
		DecimalFormat format = new DecimalFormat( "#.0" );

		
		max_field.setTextFormatter( new TextFormatter<>(c ->
		{
		    if ( c.getControlNewText().isEmpty() )
		    {
		        return c;
		    }

		    ParsePosition parsePosition = new ParsePosition( 0 );
		    Object object = format.parse( c.getControlNewText(), parsePosition );

		    if ( object == null || parsePosition.getIndex() < c.getControlNewText().length() )
		    {
		        return null;
		    }
		    else
		    {
		        return c;
		    }
		}));
	}
	
	@FXML
	public void handleCreateBtn(ActionEvent e) {
		Stage stage = (Stage)name_field.getScene().getWindow();
		String gameName = name_field.getText();
		String max = max_field.getText();
		int maxPlayers = Integer.parseInt(max);
		app.createGame(gameName, maxPlayers);
	}
	
	@FXML
	public void onMenuBtn(ActionEvent e) {
		app.showMainMenu();
	}

	
}
