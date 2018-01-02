package com.initium.titler;

import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

/**
 * Pane that can be dragged and resized using the mouse
 * 
 * @author Midnightas
 */
public class SquishyPane extends Pane {

	private double startX;

	private double resizeThreshold = 5;
	private boolean resizeRight = false;
	private boolean resizeLeft = false;

	public SquishyPane() {
		this.addEventHandler(MouseEvent.MOUSE_MOVED, this::mouseMoved);

		this.addEventHandler(MouseEvent.MOUSE_PRESSED, this::startMaybeDrag);
		this.addEventHandler(MouseEvent.MOUSE_DRAGGED, this::drag);
	}

	private void mouseMoved(MouseEvent e) {
		if (e.getX() + resizeThreshold >= getWidth()) {
			resizeRight = true;
			resizeLeft = false;
		} else if (e.getX() < resizeThreshold) {
			resizeLeft = true;
			resizeRight = false;
		} else {
			resizeLeft = false;
			resizeRight = false;
		}

		setCursor(resizeLeft || resizeRight ? Cursor.H_RESIZE : Cursor.DEFAULT);
	}

	private void startMaybeDrag(MouseEvent e) {
		startX = e.getX();
	}

	private void drag(MouseEvent e) {
		if (resizeRight) {
			setPrefWidth(e.getX());
			onPaneResizedRight();
		} else if (resizeLeft) {
			setPrefWidth(getPrefWidth() - e.getX());
			setTranslateX(getTranslateX() + e.getX());
			onPaneResizedLeft();
		} else {
			setTranslateX(getTranslateX() + e.getX() - startX);
			onPaneMoved();
		}
	}

	protected void onPaneMoved() {
	}

	protected void onPaneResizedRight() {
	}

	protected void onPaneResizedLeft() {
	}

}
