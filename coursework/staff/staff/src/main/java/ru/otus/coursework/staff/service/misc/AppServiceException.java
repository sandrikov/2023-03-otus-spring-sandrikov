package ru.otus.coursework.staff.service.misc;

import java.io.Serial;

public class AppServiceException extends RuntimeException {
	@Serial
	private static final long serialVersionUID = -1474396923832934996L;

	public AppServiceException(String message) {
		super(message);
	}
}

