package application;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class UserGuideController {
	
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
    void processClose(ActionEvent event) {
    	changeScene("Menu.fxml", event, "Menu Stage");

    }

}
