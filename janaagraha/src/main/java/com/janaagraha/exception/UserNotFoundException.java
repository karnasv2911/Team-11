package com.janaagraha.exception;

public class UserNotFoundException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String message;
	public UserNotFoundException(String message) {
		super(message);
	}

}
