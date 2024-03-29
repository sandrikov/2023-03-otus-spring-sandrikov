package ru.otus.homework.butterflygarden.services;

import ru.otus.homework.butterflygarden.domain.Butterfly;
import ru.otus.homework.butterflygarden.domain.Caterpillar;
import ru.otus.homework.butterflygarden.domain.Pupa;

public interface LifeCycleService {
	Pupa growPupa(Caterpillar caterpillar);

	Butterfly growButterfly(Pupa pupa);
}
