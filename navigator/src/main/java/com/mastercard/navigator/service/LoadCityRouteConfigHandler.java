package com.mastercard.navigator.service;

import java.util.HashSet;
import java.util.Map;

public interface LoadCityRouteConfigHandler {
	Map<String, HashSet <String>> getCityRouteMappings();
}
