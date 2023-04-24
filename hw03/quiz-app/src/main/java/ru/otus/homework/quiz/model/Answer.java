package ru.otus.homework.quiz.model;

import java.util.Set;

public record Answer(Set<String> results, boolean correct) {

}
