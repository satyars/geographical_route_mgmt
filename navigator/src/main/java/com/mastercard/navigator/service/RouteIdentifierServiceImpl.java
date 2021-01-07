package com.mastercard.navigator.service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mastercard.navigator.util.Utilities;

@Service
public class RouteIdentifierServiceImpl implements RouteIdentifierService {

	@Autowired
	private LoadCityRouteConfigHandler loadConfigData;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String findRoute(String origin, String destination) {
		String result = Utilities.STR_NO;
		Map<String, HashSet<String>> cityMap = loadConfigData.getCityRouteMappings();
		if (cityMap != null) {
			HashSet<String> listDestinations = cityMap.get(origin);
			listDestinations.contains(destination);
			result = isRouteIdentifed(cityMap, origin, destination);
		}
		return result;
	}

	private String isRouteIdentifed(Map<String, HashSet<String>> cityMap, String source, String destination) {

		List<HashSet<String>> originMapping = cityMap.entrySet().stream()
				.filter(e -> e.getKey().contains(source)
						|| e.getValue().parallelStream().anyMatch(c -> c.equalsIgnoreCase(source)))
				.map(e -> e.getValue()).collect(Collectors.toList());
		boolean foundMatch = originMapping.stream().anyMatch(l -> l.contains(destination));

		/*
		 * List<LinkedHashSet<String>> destinationMapping = cityMap.entrySet().stream()
		 * .filter(e -> e.getKey().contains(destination) ||
		 * e.getValue().contains(destination)) .map(e ->
		 * e.getValue()).collect(Collectors.toList());
		 */

		boolean foundSourceMatch = originMapping.stream().anyMatch(l -> l.contains(source));

		return foundMatch && foundSourceMatch ? Utilities.STR_YES : Utilities.STR_NO;
	}

}
