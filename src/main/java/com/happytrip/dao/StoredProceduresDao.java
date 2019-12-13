package com.happytrip.dao;

import java.util.List;

import com.happytrip.model.Airline;
import com.happytrip.model.Flight;
import com.happytrip.model.Route;

public interface StoredProceduresDao {
	
	public List<Route> listAllRoutes();
	public List<Airline> listAllAirlines();
	void save(Airline airline);
	List<Flight> listAllFlights();

}
