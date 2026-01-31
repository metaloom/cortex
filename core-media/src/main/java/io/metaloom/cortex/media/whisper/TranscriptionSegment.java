package io.metaloom.cortex.media.whisper;

public class TranscriptionSegment {

	private String text;
	private long from = 0;
	private long to = 0;

	public TranscriptionSegment(String text, long from, long to) {
		this.from = from;
		this.to = to;
	}

	public String getText() {
		return text;
	}

	public long getFrom() {
		return from;
	}

	public long getTo() {
		return to;
	}

	public long length() {
		return to - from;
	}
}
