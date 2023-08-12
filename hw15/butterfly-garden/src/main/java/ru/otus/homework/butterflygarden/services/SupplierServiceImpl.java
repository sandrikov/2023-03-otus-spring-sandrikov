package ru.otus.homework.butterflygarden.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.stereotype.Service;
import ru.otus.homework.butterflygarden.domain.Caterpillar;
import ru.otus.homework.butterflygarden.domain.Shipment;

import java.util.ArrayList;
import java.util.concurrent.ForkJoinPool;

@Log4j2
@Service
@RequiredArgsConstructor
public class SupplierServiceImpl implements SupplierService {
	private static final String[] TYPES = {"Kapustneytca", "Admiral", "Krapivnitca", "Boiaryshnitca"};

	private final Sleeper sleeper;

	private final ButterflyGardenService garden;

	@Override
	public void startSupply() {
		val pool = ForkJoinPool.commonPool();
		for (int i = 0; i < TYPES.length; i++) {
			int num = i + 1;
			val family = TYPES[i];
			pool.execute(() -> {

				val caterpillars = buyNextBatch(family, RandomUtils.nextInt(3, 5));
				log.info("Buy Batch#{} of {} caterpillars '{}'",
						num, caterpillars.batch().size(), family);

				val butterflies = garden.lifecycle(caterpillars);
				log.info("Ready Batch#{} of {} butterflies '{}'",
						num, butterflies.batch().size(), family);

			});
			sleeper.sleep(2000);
		}
	}

	private Shipment<Caterpillar> buyNextBatch(String family, int larvaNumber) {
		val shipment = new Shipment<Caterpillar>(new ArrayList<>());
		for (int i = 0; i < larvaNumber; i++) {
			shipment.batch().add(new Caterpillar(family, i));
		}
		return shipment;
	}
}
