package org.questgame.webquestgame.Exceptions;

public class ChoiceInitializeException extends RuntimeException {
	public ChoiceInitializeException() {
	}

	public ChoiceInitializeException(String message) {
		super(message);
	}

	public ChoiceInitializeException(String message, Throwable cause) {
		super(message, cause);
	}

	public ChoiceInitializeException(Throwable cause) {
		super(cause);
	}

	public ChoiceInitializeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
