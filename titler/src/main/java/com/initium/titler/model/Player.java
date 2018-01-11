package com.initium.titler.model;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Deque;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber.Exception;
import org.bytedeco.javacv.Java2DFrameConverter;

import com.initium.titler.Titler;

import javafx.application.Platform;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class Player {

	public static Java2DFrameConverter frameConv = new Java2DFrameConverter();

	public FFmpegFrameGrabber video;
	public MediaPlayer audio;

	public ImageView view;

	public ScheduledExecutorService timer;
	public ScheduledFuture<?> timerTask;

	public Player(ImageView view, File file) {
		this.view = view;

		video = new FFmpegFrameGrabber(file);
		try {
			video.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
		audio = new MediaPlayer(new Media(file.toURI().toString()));
		audio.setOnEndOfMedia(this::stop);

		timer = Executors.newScheduledThreadPool(8);
		
		// call a frame
		next();
	}

	public void start() {
		audio.play();
		timerTask = timer.scheduleAtFixedRate(this::next, 0, (long) (1000000000 / video.getFrameRate()),
				TimeUnit.NANOSECONDS);
	}

	public void stop() {
		timerTask.cancel(true);
		audio.pause();
	}

	public void next() {
		try {
			Frame frame = video.grabImage();

			Platform.runLater(() -> {
				BufferedImage awt = frameConv.convert(frame);
				if (awt != null) {
					if (view.getImage() == null) {
						view.setImage(Titler.awtToFXImage(awt, null));
					} else {
						Titler.awtToFXImage(awt, (WritableImage) view.getImage());
					}
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// both following methods are unfortunately expensive operations

	public void seekFrame(int frame) {
		try {
			audio.seek(Duration.seconds(frame * (1 / video.getFrameRate())));
			video.setFrameNumber(frame);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void seekDur(Duration dur) {
		audio.seek(dur);
		try {
			video.setFrameNumber((int) (dur.toSeconds() * video.getFrameRate()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
