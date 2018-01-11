package com.initium.titler;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

import org.bytedeco.javacpp.opencv_core.Mat;

import com.sun.javafx.application.LauncherImpl;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
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

	public static WritableImage awtToFXImage(BufferedImage awt, WritableImage img) {
		return SwingFXUtils.toFXImage(awt, img);
	}

	public static BufferedImage matToBufferedImage(Mat original) {
		BufferedImage image = null;
		int width = original.cols(), height = original.rows(), channels = original.channels();
		byte[] sourcePixels = new byte[width * height * channels];
		original.arrayData().asBuffer().get(sourcePixels);

		if (original.channels() > 1) {
			image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
		} else {
			image = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
		}
		final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
		System.arraycopy(sourcePixels, 0, targetPixels, 0, sourcePixels.length);

		return image;
	}

	public static void main(String[] args) {
		LauncherImpl.launchApplication(Titler.class, TitlerPreloader.class, args);
	}

}
