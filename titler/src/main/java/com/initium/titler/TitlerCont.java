package com.initium.titler;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import com.initium.titler.modifiers.CurrentSubtitleUpdator;
import com.initium.titler.modifiers.SeekableByDragging;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class TitlerCont implements Initializable {

	private FileChooser mediaChooser = new FileChooser();
	private FileChooser subtitlesChooser = new FileChooser();
	{
		mediaChooser.getExtensionFilters().add(new ExtensionFilter("Media files", "*.mp4"));
		subtitlesChooser.getExtensionFilters().add(new ExtensionFilter("SRT files", "*.srt"));
	}

	@FXML
	public AnchorPane mediaViewPane;

	@FXML
	public ScrollPane timelinePane;

	@FXML
	public Label currentSubtitle;

	@FXML
	public MediaView mediaView;
	public MediaPlayer mediaPlayer;
	public Media media;

	public Timeline timeline;
	public Seekbar seekbar;

	public Stage stage;

	public void initActually(Stage stage, Scene scene) {
		this.stage = stage;
		scene.setOnKeyPressed(ke -> {
			if (ke.getCode() == KeyCode.SPACE) {
				ke.consume();

				if (mediaPlayer.getStatus() != Status.PLAYING)
					mediaPlayer.play();
				else
					mediaPlayer.pause();
			}
		});

		// timelinePane.setOnMouseClicked(e -> timeline.fireEvent(e));
		// timelinePane.setOnKeyPressed(e -> timeline.fireEvent(e));
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		mediaView.fitWidthProperty().bind(mediaViewPane.widthProperty());
		mediaView.fitHeightProperty().bind(mediaViewPane.heightProperty());
		new SeekableByDragging(mediaView);

		timeline = new Timeline();

		timeline.prefHeightProperty().bind(timelinePane.heightProperty());
	}

	@FXML
	public void onFileOpen(ActionEvent e) {
		File mediaFile = mediaChooser.showOpenDialog(stage);

		if (mediaFile != null) {
			media = new Media(mediaFile.toURI().toString());
			mediaPlayer = new MediaPlayer(media);
			mediaView.setMediaPlayer(mediaPlayer);
			new CurrentSubtitleUpdator(mediaPlayer, currentSubtitle, timeline.subtitles);

			if (seekbar != null)
				seekbar.close();

			timeline.getChildren().clear();
			timeline.subtitles.load(mediaFile);
			timelinePane.setContent(new Group(timeline, seekbar = new Seekbar(this)));
		}
	}

	@FXML
	public void onFileSave(ActionEvent e) {
		if (!timeline.subtitles.save()) {
			onFileSaveAs(e);
		}
	}

	@FXML
	public void onFileSaveAs(ActionEvent e) {
		timeline.subtitles.file = subtitlesChooser.showSaveDialog(stage);

		if (timeline.subtitles.file != null) {
			onFileSave(e);
		}
	}

	@FXML
	public void onFilePreferences(ActionEvent e) {

	}

	@FXML
	public void onFileQuit(ActionEvent e) {
		Platform.exit();
	}

	@FXML
	public void onHelpTutorials(ActionEvent e) {

	}

	@FXML
	public void onHelpAbout(ActionEvent e) {
		new About();
	}

}
