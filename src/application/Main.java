package application;

import java.sql.*;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.io.File;


public class Main extends Application {
	
	@Override
	public void start(Stage primaryStage) {
		try {
				Media sound = new Media(new File("o95.wav").toURI().toString());
				MediaPlayer mediaPlayer = new MediaPlayer(sound);
				mediaPlayer.play();
		        Parent root = FXMLLoader.load(getClass().getResource("gui_final.fxml"));
		        Scene scene = new Scene(root, 800, 600);
		        primaryStage.setTitle("Data Modelling Final");
		        primaryStage.setScene(scene);
		        primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
