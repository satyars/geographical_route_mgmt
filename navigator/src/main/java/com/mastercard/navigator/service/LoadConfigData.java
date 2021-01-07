package com.mastercard.navigator.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class LoadConfigData {

	String fileName = "/city.text";
	private ValueOperations<String, Object> valOps;
	private final RedisTemplate<String, Object> redisTemplate;

	public LoadConfigData(RedisTemplate<String, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;

	}
	@PostConstruct
    private void init() {
		valOps = redisTemplate.opsForValue();
    }
	public void readFile() {
		try {
			readFromInputStream();
		} catch (IOException e) {
			log.error(e);
		}
	}

	private Map<String, HashSet <String>> readFromInputStream() throws IOException {
		Map<String, HashSet <String>> cityRouteMapping = new HashMap<>();
		if (!redisTemplate.hasKey("CityRouts")) {
			try (InputStream in = LoadConfigData.class.getResourceAsStream("/city.txt")) {
					BufferedReader br = new BufferedReader(new InputStreamReader(in));
					String line;
					String[] arrayLines;
					while ((line = br.readLine()) != null) {
						arrayLines = line.split(",");
						log.debug("{}, {} ",arrayLines[0] , arrayLines[1]);
						findExistingMapping(cityRouteMapping,  arrayLines[0].trim(),arrayLines[1].trim());
						findExistingMapping(cityRouteMapping, arrayLines[1].trim(), arrayLines[0].trim());
					}
			} catch (Exception ex) {
				log.error(ex);
			}
			log.debug("lodded city.txt  data --> {}" , cityRouteMapping);
			valOps.set("CityRouts", cityRouteMapping);
		} else {
			log.debug("cityRouteMapping --> {} " , cityRouteMapping);
			Object object = valOps.get("CityRouts");
			if (object instanceof Map) {
				cityRouteMapping = (Map<String, HashSet<String>>) object;
			}
		}

		return cityRouteMapping;
	}

	private void findExistingMapping(Map<String, HashSet<String>> cities, String origin,String destination) {
		if(cities.get(destination) != null) {
				if(!cities.get(destination).contains(origin)){
					cities.get(destination).add(origin);
				}
				if(cities.get(origin) != null) {
					cities.get(origin).add(destination);
				}else {
					cities.put(origin, new HashSet<>());
					cities.get(origin).add(destination);
				}
			
		} else {
			cities.put(destination, new HashSet <>());
			cities.get(destination).add(origin);
		}
	}
}
