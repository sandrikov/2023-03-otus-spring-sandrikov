package ru.otus.homework.quiz.services.io;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import static java.util.stream.Collectors.toList;

@ConditionalOnBean(value = DatasourceLoader.class)
@Service
public class ResourceCsvLoader implements CsvLoader {

    private final DatasourceLoader datasourceLoader;

    public ResourceCsvLoader(DatasourceLoader datasourceLoader) {
        this.datasourceLoader = datasourceLoader;
    }

    @Override
    public List<String[]> getRows() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(datasourceLoader.getInputStream()))) {
            return reader.lines().map(l -> l.split(";")).collect(toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
