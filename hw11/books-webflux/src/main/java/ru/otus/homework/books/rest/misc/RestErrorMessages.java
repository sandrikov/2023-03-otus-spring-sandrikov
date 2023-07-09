package ru.otus.homework.books.rest.misc;

public interface RestErrorMessages {

    String INVALID_ID = "Invalid ID %d <> %d";

    String NEW_ENTITY_HAVE_ID = "A new %s cannot already have an ID";

    static String getInvalidIdMessage(long pathVariableId, long entityId) {
        return String.format(INVALID_ID, pathVariableId, entityId);
    }

    static String getNewEntityHaveIdMessage(String entityName) {
        return String.format(NEW_ENTITY_HAVE_ID, entityName);
    }
}
