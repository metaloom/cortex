package io.metaloom.cortex.api.option.action;

public class ThumbnailOptions extends AbstractActionOptions<ThumbnailOptions> {

	private static final int DEFAULT_TILE_SIZE = 384;

	private static final int DEFAULT_COLS = 6;

	private static final int DEFAULT_ROWS = 1;

	private String thumbnailPath;

	private int cols = DEFAULT_COLS;

	private int rows = DEFAULT_ROWS;

	private int tileSize = DEFAULT_TILE_SIZE;

	@Override
	protected ThumbnailOptions self() {
		return this;
	}

	public String getThumbnailPath() {
		return thumbnailPath;
	}

	public ThumbnailOptions setThumbnailPath(String thumbnailPath) {
		this.thumbnailPath = thumbnailPath;
		return this;
	}

	public int getTileSize() {
		return tileSize;
	}

	public int getCols() {
		return cols;
	}

	public ThumbnailOptions setCols(int cols) {
		this.cols = cols;
		return this;
	}

	public int getRows() {
		return rows;
	}

	public ThumbnailOptions setRows(int rows) {
		this.rows = rows;
		return this;
	}
}
