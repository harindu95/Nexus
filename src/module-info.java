module core {
	requires javafx.controls;
	requires javafx.fxml;
	requires javafx.base;
	requires javafx.graphics;
	requires java.desktop;
	requires javafx.swing;
	opens ui to javafx.fxml;
	opens core to javafx.base;
	opens game to javafx.base;
	exports ui;
	exports game;
	exports game.test;
}	