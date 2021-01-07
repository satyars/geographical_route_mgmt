package com.mastercard.navigator;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mastercard.navigator.service.RouteIdenfierService;

import reactor.core.publisher.Mono;

@RestController
public class RouteIdentifierController {

	final RouteIdenfierService service;
	
	public RouteIdentifierController(RouteIdenfierService service) {
		this.service = service;
	}
	
	@GetMapping("/connected")
	public Mono<String> findRoute(@RequestParam("origin") String origin,@RequestParam("destination")String destination){
		
		return Mono.just(service.findRoute(origin,destination));
	}
}
