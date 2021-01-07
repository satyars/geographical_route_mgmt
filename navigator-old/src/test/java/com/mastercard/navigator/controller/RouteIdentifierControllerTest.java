package com.mastercard.navigator.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.mastercard.navigator.service.RouteIdentifierService;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {RouteIdentifierController.class })
@WebFluxTest
public class RouteIdentifierControllerTest {
	@MockBean
	RouteIdentifierService service;
	
	@Autowired
    WebTestClient webTestClient;
	
	@Test
	public void testFindRoute() {
		Mockito.when(service.findRoute("New York","Buston")).thenReturn("yes");
		
		webTestClient.get().uri("/connected?origin=New York&destination=Buston")
		.exchange()
		.expectStatus()
		.isOk()
		.expectBody(String.class)
		.isEqualTo("yes");
	}
	@Test
	public void testNoRouteFound() {
		Mockito.when(service.findRoute("New York","Buston")).thenReturn("no");
		
		webTestClient.get().uri("/connected?origin=New York&destination=Buston")
		.exchange()
		.expectStatus()
		.isOk()
		.expectBody(String.class)
		.isEqualTo("no");
	}
}
