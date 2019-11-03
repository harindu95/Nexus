package ui;

import java.io.IOException;
import java.util.function.Predicate;

import client.Application;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class NetworkStatus extends Base {

	XYChart.Series<Number, Number> sent;
	XYChart.Series<Number, Number> recv;
	private int t;
	Scene scene;
	
	
	public NetworkStatus(Application app) {
		super(app);		
		t = 0;
		load();
	}
	
	public void load() {
		Parent root;
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/network.fxml"));
			loader.setController(this);
			root = loader.load();
			scene = new Scene(root);
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}
	
	public void start(Stage window) {
		
		window.setScene(scene);
		window.setResizable(false);
		window.setTitle("Nexus");
		
		window.setOnCloseRequest(e -> System.exit(0));
		window.show();
	}
	
	
	LineChart<Number, Number> chart;
	
	@FXML
	ScrollPane scrollPane;
	
	public void initialize() {
		
		VBox content = new VBox();
		final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Time");
        xAxis.setForceZeroInRange(false);
		chart = new LineChart<Number, Number>(xAxis, yAxis);
		sent = new XYChart.Series();
		sent.setName("Sent");		
		chart.getData().add(sent);
		recv = new XYChart.Series();
		recv.setName("Recieve");		
		chart.getData().add(recv);
		
		
		content.getChildren().add(chart);
		
		scrollPane.setContent(content);
	}
	
	public void update( int sentBytes, int recvBytes) {
		Platform.runLater(new Runnable() {
			
			@Override
			public void run() {
				sent.getData().add(new Data<Number, Number>(t++, sentBytes));
				ObservableList<Data<Number, Number>> data = sent.getData();
			
				data.removeIf(new Predicate<Data<Number, Number>>() {

					@Override
					public boolean test(Data<Number, Number> val) {
						int diff = (t - val.getXValue().intValue());
						return diff > 30;
					}
				});
				
				recv.getData().add(new Data<Number, Number>(t++, recvBytes));
				ObservableList<Data<Number, Number>> data2 = recv.getData();
			
				data2.removeIf(new Predicate<Data<Number, Number>>() {

					@Override
					public boolean test(Data<Number, Number> val) {
						int diff = (t - val.getXValue().intValue());
						return diff > 30;
					}
				});

				
			}
			
			
		});
		
	}
	

}
