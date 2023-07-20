package io.f12.notionlinkedblog.exceptions;

public class AlreadyExistException extends RuntimeException {
	public AlreadyExistException() {
	}

	public AlreadyExistException(String message) {
		super(message);
	}
}
