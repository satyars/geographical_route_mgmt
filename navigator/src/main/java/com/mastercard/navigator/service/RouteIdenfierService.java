package com.mastercard.navigator.service;
/**
 * 
 * @author Satya Molleti
 *
 */
public interface RouteIdenfierService {
/**
 * 
 * @param origin
 * @param destination
 * @return
 */
	String findRoute(String origin,String destination);
}
