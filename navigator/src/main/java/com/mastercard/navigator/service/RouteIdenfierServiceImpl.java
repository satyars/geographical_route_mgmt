package com.mastercard.navigator.service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
@Service
public class RouteIdenfierServiceImpl implements RouteIdenfierService {
	private ValueOperations<String, Object> valOps;

	@Autowired
	LoadConfigData loadConfigData;

	private final RedisTemplate<String, Object> redisTemplate;

	public RouteIdenfierServiceImpl(RedisTemplate<String, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;

	}

	@PostConstruct
	private void init() {
		valOps = redisTemplate.opsForValue();
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String findRoute(String origin, String destination) {

		if (!redisTemplate.hasKey("CityRouts")) {
			loadConfigData.readFile();
		}

		Object object = valOps.get("CityRouts");
		Map<String, HashSet<String>> cityMap = null;
		
		if(object instanceof Map) {
			cityMap = (Map<String, HashSet <String>>) object;
		}else {
			return "no";
		}
		System.out.println(cityMap);
		HashSet<String> listDestinations = cityMap.get(origin);
		listDestinations.contains(destination);
		return isRouteIdentifed(cityMap, origin, destination);
	}

	String isRouteIdentifed(Map<String, HashSet<String>> cityMap, String source, String destination) {

		List<HashSet<String>> originMapping = cityMap.entrySet().stream()
				.filter(e -> e.getKey().contains(source) || e.getValue().parallelStream().anyMatch(c ->c.equalsIgnoreCase(source)))
				.map(e -> e.getValue())
				.collect(Collectors.toList());
		boolean foundMatch = originMapping.stream().anyMatch(l -> l.contains(destination));

		/*
		 * List<LinkedHashSet<String>> destinationMapping = cityMap.entrySet().stream()
		 * .filter(e -> e.getKey().contains(destination) ||
		 * e.getValue().contains(destination)) .map(e ->
		 * e.getValue()).collect(Collectors.toList());
		 */

		boolean foundSourceMatch = originMapping.stream().anyMatch(l -> l.contains(source));

		return foundMatch && foundSourceMatch ? "yes" : "no";
	}

}
