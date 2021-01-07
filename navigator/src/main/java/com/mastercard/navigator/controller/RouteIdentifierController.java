package com.mastercard.navigator.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mastercard.navigator.service.RouteIdentifierService;

import reactor.core.publisher.Mono;

@RestController
public class RouteIdentifierController {

	private final RouteIdentifierService service;
	
	public RouteIdentifierController(RouteIdentifierService service) {
		this.service = service;
	}
	
	@GetMapping("/connected")
	public Mono<String> findRoute(@RequestParam("origin") String origin,@RequestParam("destination")String destination){
		
		return Mono.just(service.findRoute(origin,destination));
	}
}
