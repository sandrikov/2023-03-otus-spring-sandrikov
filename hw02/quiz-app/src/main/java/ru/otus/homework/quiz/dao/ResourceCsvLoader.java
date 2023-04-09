package ru.otus.homework.quiz.dao;

import static java.util.stream.Collectors.toList;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;

public class ResourceCsvLoader implements CsvLoader {

	private final String resourceName;

	public ResourceCsvLoader(String resourceName) {
		this.resourceName = resourceName;
	}

	@Override
	public List<String[]> getRows() {
		final Resource resource = new DefaultResourceLoader().getResource("classpath:" + resourceName);
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
			return reader.lines().map(l -> l.split(";")).collect(toList());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
