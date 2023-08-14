package ru.otus.homework.butterflygarden.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.stereotype.Service;
import ru.otus.homework.butterflygarden.domain.Butterfly;
import ru.otus.homework.butterflygarden.domain.Caterpillar;
import ru.otus.homework.butterflygarden.domain.Pupa;

import java.util.function.Function;

@Service
@Log4j2
@RequiredArgsConstructor
public class LifeCycleServiceImpl implements LifeCycleService {

	private final Sleeper sleeper;

	@Override
	public Pupa growPupa(Caterpillar caterpillar) {
		return grow(caterpillar, Pupa::new, 100, 200);
	}

	@Override
	public Butterfly growButterfly(Pupa pupa) {
		return grow(pupa, Butterfly::new, 200, 300);
	}

	private <F,T> T grow(F source, Function<F,T> transmutation, long minGrowingTime, long maxGrowingTime) {
		log.info("{} growing... ", source);
		sleeper.sleep(RandomUtils.nextLong(minGrowingTime, maxGrowingTime));
		val result = transmutation.apply(source);
		log.info("Turned into {}", result);
		return result;
	}
}
