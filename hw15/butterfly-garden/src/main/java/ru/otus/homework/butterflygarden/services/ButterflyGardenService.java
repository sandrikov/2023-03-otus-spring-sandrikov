package ru.otus.homework.butterflygarden.services;

import java.util.Collection;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;

import ru.otus.homework.butterflygarden.domain.Butterfly;
import ru.otus.homework.butterflygarden.domain.Caterpillar;
import ru.otus.homework.butterflygarden.domain.Delivery;
import ru.otus.homework.butterflygarden.domain.Shipment;

@MessagingGateway
public interface ButterflyGardenService {

	@Gateway(requestChannel = "supplierChannel", replyChannel = "exhibitionChannel")
	Delivery<Butterfly> lifecycle(Shipment<Caterpillar> orderItem);

}
