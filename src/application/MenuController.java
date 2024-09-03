package application;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Pane;
import javafx.scene.media.AudioClip;
import javafx.stage.Stage;

public class MenuController {

	// create a alert
	Alert a = new Alert(AlertType.NONE, "default Dialog", ButtonType.CLOSE);

	private AudioClip buttonClickSound = new AudioClip(
			getClass().getResource("/audio/buttonClick.mp3").toExternalForm());

	// Common Function
	void changeScene(String fxmlFileNames, ActionEvent event, String title) {
		Pane root;
		try {
			root = (Pane) FXMLLoader.load(getClass().getResource(fxmlFileNames));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			primaryStage.hide();
			primaryStage.setScene(scene);
			primaryStage.setTitle(title);
			primaryStage.setResizable(false); 
			primaryStage.show();
		} catch (IOException e) {
			// TODO Auto-generated catch blocka
			e.printStackTrace();
		}
	}

	@FXML
	void processStart(ActionEvent event) {
		changeScene("MainUI.fxml", event, "Toplay");
		buttonClickSound.play();

	}

	@FXML
	void processscoreBoard(ActionEvent event) {
		changeScene("scoreBoard.fxml", event, "Score History");
		buttonClickSound.play();

	}

	@FXML
	void processExit(ActionEvent event) {
		Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		primaryStage.close();
		buttonClickSound.play();

	}

	@FXML
	void processHelp(ActionEvent event) {
		changeScene("UserGuide.fxml", event, "Toplay");
		buttonClickSound.play();

	}

}
