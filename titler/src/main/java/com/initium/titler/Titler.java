package com.initium.titler;

import com.sun.javafx.application.LauncherImpl;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

@SuppressWarnings("restriction")
public class Titler extends Application {

	private Scene scene;

	private TitlerCont cont = new TitlerCont();

	@Override
	public void start(Stage stage) throws Exception {
		FXMLLoader fxml = new FXMLLoader();
		fxml.setController(cont);

		scene = new Scene(fxml.load(Titler.class.getResourceAsStream("/layout.fxml")));
		scene.getStylesheets().add(Titler.class.getResource("/styles.css").toExternalForm());
		stage.setScene(scene);
		stage.setTitle("Initium Titler");
		stage.getIcons().addAll(new Image(Titler.class.getResource("/icon512.png").toExternalForm()),
				new Image(Titler.class.getResource("/icon32.png").toExternalForm()));
		stage.show();

		cont.initActually(stage, scene);
		cont.onFileOpen(null);
	}

	public static void main(String[] args) {
		LauncherImpl.launchApplication(Titler.class, TitlerPreloader.class, args);
	}

}
