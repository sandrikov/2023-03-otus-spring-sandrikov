package ru.otus.homework.quiz.services.io;

import java.io.IOException;
import java.io.InputStream;

public interface DatasourceLoader {

    InputStream getInputStream() throws IOException;
}
