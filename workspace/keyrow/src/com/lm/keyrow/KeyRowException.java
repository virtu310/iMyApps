package com.lm.keyrow;

public class KeyRowException extends Exception {
	private static final long serialVersionUID = -5826840027420952158L;

	public KeyRowException(String message) {
		super(message);
	}
	
	public KeyRowException(Throwable e) {
		super(e);
	}
	
	public KeyRowException(String message, Throwable e) {
		super(message, e);
	}
}
