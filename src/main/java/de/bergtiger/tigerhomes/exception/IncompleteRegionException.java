package de.bergtiger.tigerhomes.exception;

public class IncompleteRegionException  extends Exception {

	/**
	 * default serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	public IncompleteRegionException() {
		super("Incomplete region selected");
	}
}
