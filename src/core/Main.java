package core;

import javafx.application.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

	@Override
    public void start(Stage stage) throws Exception {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("fxml/Login.fxml"));
        Parent root = loader.load();
        
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
	
	public static void main(String[] args) {
	    launch(args);
	}

}
