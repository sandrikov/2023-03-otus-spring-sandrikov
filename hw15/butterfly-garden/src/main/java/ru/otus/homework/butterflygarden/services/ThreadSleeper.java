package ru.otus.homework.butterflygarden.services;

import org.springframework.stereotype.Service;

@Service
public class ThreadSleeper implements Sleeper {
	@Override
	public void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}
}
