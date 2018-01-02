package com.initium.titler.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.util.Duration;

public class Subtitle {

	public Duration start, length;
	public StringProperty text;

	public Subtitle(Duration start, Duration length, String text) {
		this.start = start;
		this.length = length;
		this.text = new SimpleStringProperty(text);
	}

	public static Duration[] getTimesFromString(String s) {
		String[] strs = s.split("\\-\\-\\>");
		return new Duration[] { parseDuration(strs[0].trim()), parseDuration(strs[1].trim()) };
	}

	public static Duration parseDuration(String s) {
		String[] hmsm = s.split("\\:");
		String[] sm = hmsm[2].split("\\,");
		return new Duration(Integer.parseInt(hmsm[0]) * 60 * 60 * 1000 + Integer.parseInt(hmsm[1]) * 60 * 1000
				+ Integer.parseInt(sm[0]) * 1000 + Integer.parseInt(sm[1]));
	}

	public static String durationToString(Duration dur) {
		double millis = dur.toMillis();
		double seconds = millis / 1000;
		double minutes = seconds / 60;
		double hours = minutes / 60;
		return (int) hours + ":" + (int) minutes % 60 + ":" + (int) seconds % 60 + "," + (int) millis % 1000;
	}

}
