package io.metaloom.cortex.action.thumbnail;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.metaloom.cortex.api.option.action.AbstractActionOptions;

@Singleton
public class ThumbnailActionOptions extends AbstractActionOptions<ThumbnailActionOptions> {

	private static final int DEFAULT_TILE_SIZE = 384;

	private static final int DEFAULT_COLS = 6;

	private static final int DEFAULT_ROWS = 1;

	public static final String KEY = "thumbnail";

	private int cols = DEFAULT_COLS;

	private int rows = DEFAULT_ROWS;

	private int tileSize = DEFAULT_TILE_SIZE;

	@Inject
	public ThumbnailActionOptions() {
	}

	@Override
	protected ThumbnailActionOptions self() {
		return this;
	}

	public int getTileSize() {
		return tileSize;
	}

	public int getCols() {
		return cols;
	}

	public ThumbnailActionOptions setCols(int cols) {
		this.cols = cols;
		return this;
	}

	public int getRows() {
		return rows;
	}

	public ThumbnailActionOptions setRows(int rows) {
		this.rows = rows;
		return this;
	}

}
