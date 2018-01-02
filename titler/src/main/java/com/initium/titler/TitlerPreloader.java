package com.initium.titler;

import javafx.application.Preloader;
import javafx.application.Preloader.StateChangeNotification.Type;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

// TODO: Preloader invisible sometimes
public class TitlerPreloader extends Preloader {

	private Stage stage;

	@Override
	public void start(Stage stage) throws Exception {
		this.stage = stage;

		VBox vbox = new VBox();
		vbox.getChildren().add(new ImageView(Titler.class.getResource("/titler.png").toExternalForm()));

		Scene scene = new Scene(vbox);
		scene.setFill(null);

		stage.setScene(scene);
		stage.initStyle(StageStyle.UTILITY);
		stage.initStyle(StageStyle.TRANSPARENT);
		stage.show();
	}

	@Override
	public void handleStateChangeNotification(StateChangeNotification info) {
		super.handleStateChangeNotification(info);

		if (info.getType() == Type.BEFORE_START) {
			stage.hide();
		}
	}

}
