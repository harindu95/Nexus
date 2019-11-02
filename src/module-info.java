module core {
	requires javafx.controls;
	requires javafx.fxml;
	requires javafx.base;
	opens ui to javafx.fxml;
	exports ui;
}	