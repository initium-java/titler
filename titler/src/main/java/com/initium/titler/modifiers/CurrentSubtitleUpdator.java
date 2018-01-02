package com.initium.titler.modifiers;

import com.initium.titler.model.Subtitle;
import com.initium.titler.model.Subtitles;

import javafx.scene.control.Label;
import javafx.scene.media.MediaPlayer;

public class CurrentSubtitleUpdator {

	public CurrentSubtitleUpdator(MediaPlayer player, Label label, Subtitles subtitles) {
		player.currentTimeProperty().addListener((ob, ol, ne) -> {
			label.setText("");
			for (Subtitle st : subtitles) {
				if(st.start.lessThan(ne) && st.start.add(st.length).greaterThan(ne)) {
					label.setText(st.text.get());
					break;
				}
			}
		});
	}

}
