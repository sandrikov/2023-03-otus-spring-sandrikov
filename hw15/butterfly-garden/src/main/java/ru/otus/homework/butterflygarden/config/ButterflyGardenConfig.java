package ru.otus.homework.butterflygarden.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.MessageChannelSpec;
import org.springframework.integration.dsl.MessageChannels;
import ru.otus.homework.butterflygarden.domain.Butterfly;
import ru.otus.homework.butterflygarden.domain.Caterpillar;
import ru.otus.homework.butterflygarden.domain.Delivery;
import ru.otus.homework.butterflygarden.domain.Shipment;
import ru.otus.homework.butterflygarden.services.LifeCycleService;

import java.util.Collection;
import java.util.concurrent.Executors;

@Configuration
public class ButterflyGardenConfig {

	@Bean
	public MessageChannelSpec<?, ?> supplierChannel() {
		return MessageChannels.queue(2);
	}

	@Bean
	public MessageChannelSpec<?, ?> exhibitionChannel() {
		return MessageChannels.publishSubscribe();
	}

	@Bean
	public IntegrationFlow butterflyGardenFlow(LifeCycleService lifeCycleService) {
		return IntegrationFlow.from(supplierChannel())
				.<Shipment<Caterpillar>, Collection<Caterpillar>>transform(Shipment::batch)
				.split()
				.channel(c -> c.executor(Executors.newCachedThreadPool()))
				.handle(lifeCycleService, "growPupa")
				.handle(lifeCycleService, "growButterfly")
				.aggregate()
				.<Collection<Butterfly>, Delivery<Butterfly>>transform(Delivery::new)
				.channel(exhibitionChannel())
				.get();
	}

}
