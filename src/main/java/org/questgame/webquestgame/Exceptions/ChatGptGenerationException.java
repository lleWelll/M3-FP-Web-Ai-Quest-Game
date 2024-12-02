package org.questgame.webquestgame.Exceptions;

public class ChatGptGenerationException extends RuntimeException {
	public ChatGptGenerationException() {
	}

	public ChatGptGenerationException(String message) {
		super(message);
	}

	public ChatGptGenerationException(String message, Throwable cause) {
		super(message, cause);
	}
}
