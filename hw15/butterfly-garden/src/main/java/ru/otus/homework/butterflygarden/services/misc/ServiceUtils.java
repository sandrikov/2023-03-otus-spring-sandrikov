package ru.otus.homework.butterflygarden .services.misc;

import java.util.Optional;
import java.util.function.Function;

public class ServiceUtils {
    public static <T> T findById(Long id, Function<Long, Optional<T>> finder, String formatMsg)
            throws EntityNotFoundException {
        if (id == null) {
            return null;
        }
        return finder.apply(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format(formatMsg, "id=" + id)));
    }
}
