package com.initium.titler;

import java.util.List;
import java.util.stream.Collectors;

import com.initium.titler.model.Subtitle;
import com.initium.titler.model.Subtitles;

import javafx.collections.ListChangeListener;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class Timeline extends Pane implements ListChangeListener<Subtitle> {

	public Subtitles subtitles = new Subtitles();

	public double zoom = 0.01;

	public SubtitleLabel chosen;

	public Timeline() {
		List<Subtitle> list = subtitles.get();
		subtitles.clear();
		subtitles.addListener(this);
		subtitles.addAll(list);

		this.setOnMouseClicked(this::mouseClick);
		this.setOnKeyPressed(this::keyPressed);
		this.setOnKeyReleased(this::keyReleased);
	}

	@Override
	public void onChanged(Change<? extends Subtitle> chang) {
		while (chang.next()) {

			if (chang.wasRemoved()) {
				// fixes a bug in where deleting a single element does nothing
				if (chang.getFrom() == chang.getTo()) {
					getChildren().remove(chang.getFrom());
				} else {
					getChildren().remove(chang.getFrom(), chang.getTo());
				}
			}
			if (chang.wasAdded()) {
				List<? extends Subtitle> added = chang.getAddedSubList();

				getChildren().addAll(chang.getFrom(),
						added.stream().map(SubtitleLabel::new).collect(Collectors.toList()));
			}

		}
	}

	private void keyPressed(KeyEvent e) {
	}

	private void keyReleased(KeyEvent e) {

	}

	private void mouseClick(MouseEvent e) {
		if (chosen != null) {
			chosen.unchoose();
		}

		if (e.getClickCount() == 2) {
			subtitles.add(new Subtitle(Duration.seconds(e.getX() * zoom), Duration.seconds(1), "hey there"));
			chosen.edit(); // the binding causes SubtitleLabel::new to be called, which sets chosen
		}
	}

	public class SubtitleLabel extends SquishyPane {
		private Subtitle model;

		private Label label = new Label();
		private TextArea textArea = new TextArea();

		public SubtitleLabel(Subtitle subtitle) {
			this.model = subtitle;

			label.textProperty().bind(model.text);
			textArea.textProperty().bindBidirectional(model.text);

			reset();

			addEventHandler(MouseEvent.MOUSE_CLICKED, this::maybeEditOrDel);
			addEventHandler(KeyEvent.KEY_PRESSED, this::maybeDel);
			textArea.addEventHandler(KeyEvent.KEY_PRESSED, this::maybeUnedit);
			textArea.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> e.consume());

			getStyleClass().add("subtitle");

			chooseMe();
		}

		public void edit() {
			getChildren().setAll(textArea);

			textArea.selectEnd();
			textArea.deselect();
		}

		public void unedit() {
			getChildren().setAll(label);

			requestFocus();
		}

		private void maybeDel(KeyEvent e) {
			if (e.getCode() == KeyCode.DELETE) {
				e.consume();
				Timeline.this.subtitles.remove(this.model);
			}
		}

		private void maybeEditOrDel(MouseEvent e) {
			e.consume();

			switch (e.getButton()) {
			case PRIMARY:
				chooseMe();
				if (e.getClickCount() == 2) {
					edit();
				}
				break;
			case SECONDARY:
				Timeline.this.subtitles.remove(this.model);
				break;
			default:
				break;
			}
		}

		private void maybeUnedit(KeyEvent e) {
			if (e.isControlDown() && e.getCode() == KeyCode.ENTER) {
				e.consume();

				unedit();
			}
		}

		@Override
		protected void onPaneMoved() {
			super.onPaneMoved();
			model.start = Duration.seconds(getTranslateX() * zoom);
		}

		@Override
		protected void onPaneResizedRight() {
			super.onPaneResizedRight();
			model.length = Duration.seconds(getPrefWidth() * zoom);
		}

		@Override
		protected void onPaneResizedLeft() {
			super.onPaneResizedLeft();
			model.start = Duration.seconds(getTranslateX() * zoom);
			model.length = Duration.seconds(getPrefWidth() * zoom);
		}

		private void chooseMe() {
			if (Timeline.this.chosen != null) {
				Timeline.this.chosen.unchoose();
			}
			Timeline.this.chosen = this;
			getStyleClass().add("chosen");

			requestFocus();
		}

		private void unchoose() {
			Timeline.this.chosen = null;
			getStyleClass().remove("chosen");

			unedit();
		}

		private void reset() {
			unedit();

			textArea.prefWidthProperty().bind(this.widthProperty());
			textArea.prefHeightProperty().bind(Timeline.this.heightProperty());

			label.prefWidthProperty().bind(this.widthProperty());
			label.prefHeightProperty().bind(Timeline.this.heightProperty());

			setTranslateX(model.start.toSeconds() / zoom);
			setPrefWidth(model.length.toSeconds() / zoom);

			setFocusTraversable(true);
		}
	}
}
