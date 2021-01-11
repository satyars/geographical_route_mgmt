package com.mastercard.navigator.service;

import java.util.HashSet;
import java.util.Map;
/**
 * 
 * @author Satya Molleti
 *
 */
public interface LoadCityRouteConfigHandler {
	/**
	 * This method been used to load or get all the individual city possible mappings.
	 * setting/read from redis cache.
	 * @return
	 */
	Map<String, HashSet <String>> getCityRouteMappings();
	/**
	 * This method for another alternative solution which is in progress.
	 * 
	 * @return
	 */
	Map<String,String> getCityMappings();
}
