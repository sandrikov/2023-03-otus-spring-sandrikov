package ru.otus.homework.butterflygarden.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ForkJoinPool;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import ru.otus.homework.butterflygarden.domain.Caterpillar;
import ru.otus.homework.butterflygarden.domain.Shipment;

@Log4j2
@Service
@RequiredArgsConstructor
public class SupplierServiceImpl implements SupplierService {
	private static final String[] TYPES = {"Крапивница", "Капустница", "Боярышница", "Адмирал"};

	private final Sleeper sleeper;

	private final ButterflyGardenService garden;

	@Override
	public void startSupply() {
		ForkJoinPool pool = ForkJoinPool.commonPool();
		for (int i = 0; i < TYPES.length; i++) {
			int num = i + 1;
			val family = TYPES[i];
			pool.execute(() -> {
				val caterpillars = buyNextBatch(family, 5);
				log.info("Batch#{} Buy of {} caterpillars '{}'", num, caterpillars.batch().size(), family);
				val butterflies = garden.lifecycle(caterpillars);
				log.info("Batch#{} Ready of {} batterflies '{}'", num, butterflies.batch().size(), family);
			});
			sleeper.sleep (2000);
		}
	}

	private Shipment buyNextBatch(String family, int larvaNum) {
		Collection<Caterpillar> caterpillars = new ArrayList<>();
		for (int i = 0; i < larvaNum; i++) {
			caterpillars.add(new Caterpillar(family, i));
		}
		return new Shipment(caterpillars);
	}
}
