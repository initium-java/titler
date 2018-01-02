package com.initium.titler;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class About {

	private Stage stage;

	private Scene scene;

	private VBox root;

	public About() {
		root = new VBox();
		root.getChildren().add(new ImageView(About.class.getResource("/titler.png").toExternalForm()));
		root.getChildren().add(new Label("Titler by Initium\n\nMade with JavaFX & Java 8."));

		scene = new Scene(root);

		stage = new Stage();
		stage.setScene(scene);
		stage.show();
	}

}
