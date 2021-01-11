package com.mastercard.navigator.service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import com.mastercard.navigator.util.Utilities;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class LoadCityRouteConfigHandlerImpl implements LoadCityRouteConfigHandler {

	private final String filePath;
	private ValueOperations<String, Object> valOps;
	private final RedisTemplate<String, Object> redisTemplate;

	public LoadCityRouteConfigHandlerImpl(RedisTemplate<String, Object> redisTemplate,
			@Value("${filepath}") String filePath) {
		this.redisTemplate = redisTemplate;
		this.filePath = filePath;

	}

	@PostConstruct
	private void init() {
		valOps = redisTemplate.opsForValue();
	}

	/**
	 * This method is used cache the city route mapping to redis and return.
	 * 
	 * @throws IOException.
	 */
	@Override
	public Map<String, HashSet<String>> getCityRouteMappings() {
		Map<String, HashSet<String>> cityRouteMapping = null;
		Map<String,String> cityMapping =new HashMap<>();
		try {
			if (!redisTemplate.hasKey(Utilities.REDIS_CACHE_CITY_ROUTES_KEY)) {
				cityRouteMapping = new HashMap<>();
				try (InputStream in = LoadCityRouteConfigHandlerImpl.class.getResourceAsStream(filePath)) {
					BufferedReader br = new BufferedReader(new InputStreamReader(in));
					String line;
					String[] arrayLines;
					while ((line = br.readLine()) != null) {
						arrayLines = line.split(",");
						log.debug("{}, {} ", arrayLines[0], arrayLines[1]);
						cityMapping.put(arrayLines[0], arrayLines[1]);
						findExistingMapping(cityRouteMapping, arrayLines[0].trim(), arrayLines[1].trim());
						findExistingMapping(cityRouteMapping, arrayLines[1].trim(), arrayLines[0].trim());
					}
				} catch (Exception ex) {
					log.error(ex);
				}
				log.debug("Loaded city.txt  data --> {}", cityRouteMapping);
				/* System.out.println("Loaded city.txt  data --> {}"+ cityRouteMapping); */
				valOps.set(Utilities.REDIS_CACHE_CITY_ROUTES_KEY, cityRouteMapping);
				valOps.set("cityMapping", cityMapping);/* for another alternative solution this could be remove try do */
			} else {
				Object object = valOps.get(Utilities.REDIS_CACHE_CITY_ROUTES_KEY);
				Map<String, HashSet<String>> cityMap = null;

				if (object instanceof Map) {
					cityRouteMapping = (Map<String, HashSet<String>>) object;
				}
				log.debug("Cache ity.txt  data --> {}"+ cityRouteMapping);
				/* System.out.println("Cache ity.txt  data --> {}"+ cityRouteMapping); */
			}
		} catch (Exception ex) {
			log.debug("Error while loading/reading from readis cache date  --> {} Excption : {}", cityRouteMapping, ex);
		}
		return cityRouteMapping;
	}

	
	public Map<String,String> getCityMappings(){
		Map<String,String> cityMap = null; 
		Object object =  valOps.get("cityMapping");
		if (object instanceof Map) {
			cityMap = (Map<String, String>) object;
		}
		return cityMap;
	}
	private void findExistingMapping(Map<String, HashSet<String>> cities, String origin, String destination) {
		if (cities.get(destination) != null) {
			if (!cities.get(destination).contains(origin)) {
				cities.get(destination).add(origin);
			}
			if (cities.get(origin) != null) {
				cities.get(origin).add(destination);
			} else {
				cities.put(origin, new HashSet<>());
				cities.get(origin).add(destination);
			}

		} else {
			cities.put(destination, new HashSet<>());
			cities.get(destination).add(origin);
		}
		
	}

}
