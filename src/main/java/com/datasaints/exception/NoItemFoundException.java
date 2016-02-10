package com.datasaints.exception;

public class NoItemFoundException extends RuntimeException {
	/**
	 * Had to add default serialize uid... don't know why...
	 */
	private static final long serialVersionUID = 1L;

	public NoItemFoundException(String message) {
		super(message);
	}
}