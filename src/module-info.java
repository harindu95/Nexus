module core {
	requires javafx.controls;
	requires javafx.fxml;
	requires javafx.base;
	opens ui to javafx.fxml;
	opens core to javafx.base;
	exports ui;
}	