package ru.otus.homework.quiz;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import ru.otus.homework.quiz.dao.CsvLoader;
import ru.otus.homework.quiz.dao.ResourceCsvLoader;
import ru.otus.homework.quiz.services.IOService;
import ru.otus.homework.quiz.services.IOServiceStreams;

@Configuration
@ComponentScan
@PropertySource("classpath:app.properties")
public class AppConfig {

	@Bean
	IOService getIOService() {
		return new IOServiceStreams(System.out, System.in);
	}

	@Bean
	CsvLoader getTableLoader(@Value("${resource.name}") String resourceName) {
		return new ResourceCsvLoader(resourceName);
	}

}
