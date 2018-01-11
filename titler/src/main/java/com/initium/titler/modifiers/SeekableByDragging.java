package com.initium.titler.modifiers;

import com.initium.titler.TitlerCont;

import javafx.scene.input.MouseEvent;
import javafx.util.Duration;

/**
 * Lets the media player be seeked by dragging
 * 
 * @author Midnightas
 */
public class SeekableByDragging {

	private double startX;

	private TitlerCont titlerCont;

	public SeekableByDragging(TitlerCont cont) {
		this.titlerCont = cont;

		titlerCont.imageView.addEventHandler(MouseEvent.MOUSE_PRESSED, this::startMaybeDrag);
		titlerCont.imageView.addEventHandler(MouseEvent.MOUSE_DRAGGED, this::drag);
	}

	private void startMaybeDrag(MouseEvent e) {
		startX = e.getX();
	}

	private void drag(MouseEvent e) {
		Duration goTo = titlerCont.player.audio.getCurrentTime().subtract(Duration.seconds((e.getX() - startX) / 8));
		titlerCont.player.seekDur(goTo);

		e.consume();
	}

}
