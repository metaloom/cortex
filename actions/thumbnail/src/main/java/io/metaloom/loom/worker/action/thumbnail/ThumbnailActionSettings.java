package io.metaloom.loom.worker.action.thumbnail;

import io.metaloom.worker.action.common.settings.AbstractActionSettings;

public class ThumbnailActionSettings extends AbstractActionSettings {

	private static final int DEFAULT_TILE_SIZE = 384;

	private static final int DEFAULT_COLS = 6;

	private static final int DEFAULT_ROWS = 1;

	private String thumbnailPath;

	private int cols = DEFAULT_COLS;

	private int rows = DEFAULT_ROWS;

	private int tileSize = DEFAULT_TILE_SIZE;

	public String getThumbnailPath() {
		return thumbnailPath;
	}

	public ThumbnailActionSettings setThumbnailPath(String thumbnailPath) {
		this.thumbnailPath = thumbnailPath;
		return this;
	}

	public int getTileSize() {
		return tileSize;
	}

	public int getCols() {
		return cols;
	}

	public ThumbnailActionSettings setCols(int cols) {
		this.cols = cols;
		return this;
	}

	public int getRows() {
		return rows;
	}

	public ThumbnailActionSettings setRows(int rows) {
		this.rows = rows;
		return this;
	}
}
