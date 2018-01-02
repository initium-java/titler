package com.initium.titler;

import java.io.Closeable;

import javafx.animation.AnimationTimer;
import javafx.animation.Interpolator;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Seekbar extends Rectangle implements Closeable {

	private TitlerCont titlercont;

	private double gotoX;
	private AnimationTimer posUpdator = new AnimationTimer() {
		@Override
		public void handle(long now) {
			setX(Interpolator.LINEAR.interpolate(getX(), gotoX, 0.7f));
		}
	};
	
	public Seekbar(TitlerCont titlercunt) {
		this.titlercont = titlercunt;

		setFill(Color.BLACK);
		setWidth(2);
		heightProperty().bind(this.titlercont.timelinePane.heightProperty());

		this.titlercont.mediaPlayer.currentTimeProperty().addListener((ob, ol, ne) -> {
			gotoX = ne.toSeconds() / titlercont.timeline.zoom;
		});
		
		posUpdator.start();
	}
	
	public void close() {
		posUpdator.stop();
	}

}
