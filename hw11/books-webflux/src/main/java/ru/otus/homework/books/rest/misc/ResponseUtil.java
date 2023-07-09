package ru.otus.homework.books.rest.misc;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import ru.otus.homework.books.services.misc.Reply;

import java.net.URI;
import java.net.URISyntaxException;

import static ru.otus.homework.books.services.misc.Reply.Status.OK;

public interface ResponseUtil {

    static URI toURI(String str) {
        try {
            return new URI(str);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    static <X> ResponseEntity<X> wrapOrNotFound(Reply<X> reply) {
        return wrapOrNotFound(reply, null);
    }

    static <X> ResponseEntity<X> wrapOrNotFound(Reply<X> reply, HttpHeaders header) {
        if (reply.status() == OK) {
            return ResponseEntity.ok().headers(header).body(reply.data());
        }
        return ResponseEntity.notFound().build();
    }

}
