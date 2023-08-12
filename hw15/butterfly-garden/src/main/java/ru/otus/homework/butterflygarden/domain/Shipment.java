package ru.otus.homework.butterflygarden.domain;

import java.util.Collection;

public record Shipment<T>(Collection<T> batch) {
}
