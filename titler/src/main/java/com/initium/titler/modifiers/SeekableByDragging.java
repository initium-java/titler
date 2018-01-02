package com.initium.titler.modifiers;

import javafx.scene.input.MouseEvent;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;

/**
 * Lets the media player be seeked by dragging
 * 
 * @author Midnightas
 */
public class SeekableByDragging {

	private double startX;

	private MediaView view;

	public SeekableByDragging(MediaView view) {
		this.view = view;

		this.view.addEventHandler(MouseEvent.MOUSE_PRESSED, this::startMaybeDrag);
		this.view.addEventHandler(MouseEvent.MOUSE_DRAGGED, this::drag);
	}

	private void startMaybeDrag(MouseEvent e) {
		startX = e.getX();
	}

	private void drag(MouseEvent e) {
		MediaPlayer player = this.view.getMediaPlayer();
		if (player == null)
			return;

		Duration goTo = player.getCurrentTime().subtract(Duration.seconds((e.getX() - startX) / 8));
		player.seek(goTo);

		e.consume();
	}

}
