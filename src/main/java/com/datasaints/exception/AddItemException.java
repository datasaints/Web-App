package com.datasaints.exception;

public class AddItemException extends RuntimeException {
	/**
	 * Had to add default serialize uid... don't know why...
	 */
	private static final long serialVersionUID = 1L;

	public AddItemException(String message) {
		super(message);
	}
}