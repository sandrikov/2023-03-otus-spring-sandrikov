package ru.otus.homework.butterflygarden.config;

import java.util.Collection;
import java.util.concurrent.Executors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.MessageChannelSpec;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.dsl.PollerSpec;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.scheduling.PollerMetadata;

import ru.otus.homework.butterflygarden.domain.Butterfly;
import ru.otus.homework.butterflygarden.domain.Caterpillar;
import ru.otus.homework.butterflygarden.domain.Delivery;
import ru.otus.homework.butterflygarden.domain.Shipment;
import ru.otus.homework.butterflygarden.services.LifeCycleService;

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

	@Bean(name = PollerMetadata.DEFAULT_POLLER)
	public PollerSpec poller(){
		return Pollers.fixedDelay(500).maxMessagesPerPoll(2);
	}

	@Bean
	public IntegrationFlow butterflyGardenFlow(LifeCycleService lifeCycleService) {
		return IntegrationFlow.from(supplierChannel())
				.<Shipment<Caterpillar>, Collection<Caterpillar>>transform(Shipment::batch)
				.split()
				.channel(c -> c.executor(Executors.newCachedThreadPool()))
				.handle(lifeCycleService, "growCaterpillar")
				.handle(lifeCycleService, "growPupa")
				.aggregate()
				.<Collection<Butterfly>, Delivery<Butterfly>>transform(Delivery::new)
				.channel(exhibitionChannel())
				.get();
	}

}
