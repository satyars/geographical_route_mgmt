package com.mastercard.navigator.service;




import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.log4j.Log4j2;


@Log4j2
@Service
public class RouteIdentifierServiceImpl implements RouteIdentifierService {

	@Autowired
	private LoadCityRouteConfigHandler loadConfigData;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String findRoute(String origin, String destination) {
		return isRouteIdentifed(loadConfigData.getCityRouteMappings(), origin, destination);
	}

	private String isRouteIdentifed(Map<String, HashSet<String>> cityMap, String source, String destination) {
		boolean isRouteIdentifed = false;
		System.out.println("cityMap -->"+cityMap);
		List<String> listOfMapping = getAllRoutes(source,cityMap);
		List<String> listDestinationMapping = getAllRoutes(destination,cityMap);
		
		/*System.out.println("source  -->"+source);
		System.out.println("listOfMapping -->"+listOfMapping);
		System.out.println("destination  -->"+destination);
		System.out.println("listDestinationMapping  -->"+listDestinationMapping); */
		
		log.debug("source  --> {} listOfMapping ----> {}",source,listOfMapping);
		log.debug("destination  --> {}   listDestinationMapping --> {}"+destination,listDestinationMapping);
		
		/*
		 * finding intersection cities if not empty then we have route else no
		 * 
		 */
		if(listOfMapping != null && listDestinationMapping !=null) {
			List<String> result = listOfMapping.parallelStream()
					.distinct()
					.filter(listDestinationMapping::contains)
					.collect(Collectors.toList());
			/* System.out.println("result  -->"+result); */
			isRouteIdentifed = !result.isEmpty();
		}
		return isRouteIdentifed ?  "yes" : "no";
	}
	
	private List<String> getAllRoutes(String city,Map<String, HashSet<String>> cityMap){
		
		return cityMap.entrySet().parallelStream()
				.filter(e -> (e.getKey().equalsIgnoreCase(city)
						|| e.getValue().parallelStream().anyMatch(c -> c.equalsIgnoreCase(city))
						)
						)
				.map(e -> e.getValue()).flatMap(Set::stream).distinct()
				.collect(Collectors.toList());
	}

}
