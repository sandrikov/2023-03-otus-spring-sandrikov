package ru.otus.homework.books.rest.misc;

import java.net.URI;
import java.net.URISyntaxException;

public interface ResponseUtil {

    static URI toURI(String str) {
        try {
            return new URI(str);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

}
