package com.initium.titler.model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.util.Duration;

public class Subtitles extends SimpleListProperty<Subtitle> {

	public File file;

	public Subtitles() {
		set(FXCollections.observableArrayList());
	}

	public void load(File file) {
		if (file == null)
			return;

		if (!file.getAbsolutePath().endsWith(".srt")) {
			String path = file.getAbsolutePath();

			file = new File(path.substring(0, path.lastIndexOf('.')) + ".srt");
		}

		if (!file.exists())
			return;

		this.file = file;
		clear();

		try {
			List<String> lines = Files.readAllLines(file.toPath());
			for (int i = 0; i < lines.size(); i++) {
				if (lines.get(i).isEmpty())
					continue;

				Integer.parseInt(lines.get(i++));
				Duration[] times = Subtitle.getTimesFromString(lines.get(i++));
				StringBuilder subText = new StringBuilder();
				for (int j = i; j < lines.size(); j++, i++) {
					String line = lines.get(j);

					if (line.trim().isEmpty()) {
						break;
					} else {
						subText.append(line);
					}
				}
				this.add(new Subtitle(times[0], times[1].subtract(times[0]), subText.toString()));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean save() {
		if (file == null) {
			return false;
		}

		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(file, false));

			int sequenceNumber = 1;

			sort((one, two) -> one.start.compareTo(two.start));
			for (Subtitle st : this) { // this line looks so weird
				writer.write(Integer.toString(sequenceNumber++));
				writer.newLine();

				writer.write(Subtitle.durationToString(st.start) + " --> "
						+ Subtitle.durationToString(st.start.add(st.length)));
				writer.newLine();

				writer.write(st.text.get());
				writer.newLine();
				writer.newLine();
			}

			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

}
