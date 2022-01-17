package de.bergtiger.tigerhomes.exception;

public class NoRegionException  extends Exception {

	/**
	 * default serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	public NoRegionException() {
		super("No region selected");
	}
}
