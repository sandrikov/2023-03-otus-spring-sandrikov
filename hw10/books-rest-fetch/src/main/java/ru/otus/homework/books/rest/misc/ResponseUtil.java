package ru.otus.homework.books.rest.misc;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import ru.otus.homework.books.services.ServiceResponse;

import static ru.otus.homework.books.services.ServiceResponse.Status.OK;

public interface ResponseUtil {

    static <X> ResponseEntity<X> wrapOrNotFound(ServiceResponse<X> serviceResponse) {
        return wrapOrNotFound(serviceResponse, null);
    }

    static <X> ResponseEntity<X> wrapOrNotFound(ServiceResponse<X> serviceResponse, HttpHeaders header) {
        if (serviceResponse.getStatus() == OK) {
            return ResponseEntity.ok().headers(header).body(serviceResponse.getData());
        }
        return ResponseEntity.notFound().build();
    }

}
