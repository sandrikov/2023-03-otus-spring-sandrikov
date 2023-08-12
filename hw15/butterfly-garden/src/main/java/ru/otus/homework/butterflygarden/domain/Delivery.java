package ru.otus.homework.butterflygarden.domain;

import java.util.Collection;

public record Delivery<T>(Collection<T> batch) {
}
