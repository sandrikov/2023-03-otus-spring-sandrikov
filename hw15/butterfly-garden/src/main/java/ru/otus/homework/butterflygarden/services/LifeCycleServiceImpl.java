package ru.otus.homework.butterflygarden.services;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import ru.otus.homework.butterflygarden.domain.Butterfly;
import ru.otus.homework.butterflygarden.domain.Caterpillar;
import ru.otus.homework.butterflygarden.domain.Pupa;

@Service
@Log4j2
@RequiredArgsConstructor
public class LifeCycleServiceImpl implements LifeCycleService {

	private final Sleeper sleeper;

	@Override
	public Pupa growCaterpillar(Caterpillar caterpillar) {
		log.info("{} growing... ", caterpillar);
		sleeper.sleep (200);
		val pupa = new Pupa(caterpillar);
		log.info("{} turned into {}", caterpillar, pupa);
		return pupa;
	}

	@Override
	public Butterfly growPupa(Pupa pupa) {
		log.info("{} growing... ", pupa);
		sleeper.sleep (200);
		val butterfly = new Butterfly(pupa);
		log.info("{} turned into {}", pupa, butterfly);
		return butterfly;
	}
}
