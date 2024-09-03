package application;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.media.AudioClip;
import javafx.stage.Stage;

public class GameEndUIController implements Initializable {

	@FXML
	private Label timeRecord;

	@FXML
	private Label scoreRecord;

	@FXML
	private ImageView player;

	@FXML
	private Pane root;

	public static AudioClip gameOverSound;

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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@FXML
	void processExist(ActionEvent event) {
		Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		primaryStage.close();

	}

	@FXML
	void processRestart(ActionEvent event) {
		Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		primaryStage.hide();
		if (MainController.level.equalsIgnoreCase("Level1")) {
			changeScene("MainUI.fxml", event, "Menu");
		} else if (MainController.level.equalsIgnoreCase("Level2")) {
			changeScene("MainUI2.fxml", event, "Menu");
		} else {
			changeScene("MainUI3.fxml", event, "Menu");
		}

	}

	@FXML
	void processBacktoHome(ActionEvent event) {
		Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		primaryStage.hide();
		changeScene("Menu.fxml", event, "Menu");

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		timeRecord.setText("Time: " + MainController.formattedTime);
		scoreRecord.setText("Score: " + MainController.score);

		// GameOver sound
		String gameOverFile = "/audio/game-over-.mp3";
		gameOverSound = new AudioClip(getClass().getResource(gameOverFile).toExternalForm());
		gameOverSound.play();

	}

}
