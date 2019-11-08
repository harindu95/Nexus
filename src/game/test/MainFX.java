package game.test;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import javafx.application.Application;
import javafx.embed.swing.SwingNode;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class MainFX extends Application {

     @Override
     public void start(Stage stage) {
         final SwingNode swingNode = new SwingNode();
         createAndSetSwingContent(swingNode);
         Pane pane = new Pane();
         pane.getChildren().add(swingNode); // Adding swing node

         stage.setScene(new Scene(pane, 1080, 720));
         stage.show();
     }

     private void createAndSetSwingContent(final SwingNode swingNode) {
         SwingUtilities.invokeLater(new Runnable() {
             @Override
             public void run() {
                 JPanel panel = new MonopolyMain();
	         		panel.setSize(1080,720);
	         		panel.setBounds(0, 0, 1080, 720);                 
                 swingNode.setContent(panel);
             }
         });
     }

     public static void main(String[] args) {
         launch(args);
     }
 }