package com.happytrip.services.impl;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.happytrip.controllers.dto.flight.FlightScheduleDto;
import com.happytrip.dao.AirlineDao;
import com.happytrip.dao.CityDao;
import com.happytrip.dao.FlightDao;
import com.happytrip.dao.FlightRouteDao;
import com.happytrip.dao.GenericDao;
import com.happytrip.dao.LookupDao;
import com.happytrip.dao.RouteDao;
import com.happytrip.dao.StoredProceduresDao;
import com.happytrip.dao.jdbc.JdbcAirlineDao;
import com.happytrip.dao.jdbc.JdbcCityDao;
import com.happytrip.dao.jdbc.JdbcFlightDao;
import com.happytrip.dao.jdbc.JdbcFlightRouteDao;
import com.happytrip.dao.jdbc.JdbcGenericDao;
import com.happytrip.dao.jdbc.JdbcLookupDao;
import com.happytrip.dao.jdbc.JdbcReportDao;
import com.happytrip.dao.jdbc.JdbcRouteDao;
import com.happytrip.dao.jdbc.JdbcStoredProceduresDao;
import com.happytrip.dao.report.ReportDao;
import com.happytrip.model.Airline;
import com.happytrip.model.City;
import com.happytrip.model.Flight;
import com.happytrip.model.FlightCapacity;
import com.happytrip.model.FlightClass;
import com.happytrip.model.Route;
import com.happytrip.model.ScheduledFlight;
import com.happytrip.model.SeatAvailability;
import com.happytrip.model.reports.AirlineReport;
import com.happytrip.model.reports.RouteReport;
import com.happytrip.model.reports.ScheduledFlightReport;
import com.happytrip.services.AdminService;
import com.happytrip.util.transformer.AirlineModelTransformer;
import com.happytrip.util.transformer.RouteModelTransformer;
import com.happytrip.util.transformer.ScheduledFlightModelTransformer;

public class AdminServiceImpl implements AdminService {

	private AirlineDao airlineDao;

	private FlightDao flightDao;

	private LookupDao lookupDao;

	private CityDao cityDao;

	private RouteDao routeDao;

	private FlightRouteDao flightRouteDao;

	private ReportDao reportDao;

	private GenericDao genericDao;

	private StoredProceduresDao storedProceduresDao;

	public AdminServiceImpl() {
		lookupDao = new JdbcLookupDao();
		cityDao = new JdbcCityDao();
		airlineDao = new JdbcAirlineDao();
		flightDao = new JdbcFlightDao();
		cityDao = new JdbcCityDao();
		routeDao = new JdbcRouteDao();
		flightRouteDao = new JdbcFlightRouteDao();
		reportDao = new JdbcReportDao();
		genericDao = new JdbcGenericDao();
		storedProceduresDao = new JdbcStoredProceduresDao();
	}

	private boolean checkGenericAdmin() {
		Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();

		// get logged in username
		String name = auth.getName();
		return name.equalsIgnoreCase("happyadmin");
	}

	@Override
	public List<FlightClass> getAllFlightClasses() {
		if (checkGenericAdmin()) {
			return genericDao.findAllFlightClasses();
		} else {
			return lookupDao.findAllFlightClasses();
		}

	}

	@Override
	public void addFlight(Flight flight, String airlineCode) {
		Airline airline = airlineDao.findByAirlineCode(airlineCode);
		flight.setAirline(airline);
		flightDao.save(flight);
	}

	@Override
	public boolean isDuplicateFlight(Flight flight) {
		Flight f = flightDao.findByFlightName(flight.getFlightName());
		return null != f;
	}

	@Override
	public void addRoute(Route route) {
		if (checkGenericAdmin()) {
			genericDao.saveRoute(route);
		} else {
			routeDao.save(route);
		}
		RouteReport routeReport = RouteModelTransformer
				.transformToRouteReport(route);
		reportDao.storeRouteReport(routeReport);

	}

	@Override
	public boolean isDuplicateRoute(String fromCity, String toCity) {
		//Route r = routeDao.findByCityNames(fromCity, toCity);
		/* code modified for CR2-Route-exception
		 */
		//modified code starts here for CR2-Route-exception
		  Route route=null;
		  for (Route r : routeDao.getAllRoutes()) {
		   if ((r.getFromCity().getCityName().equalsIgnoreCase(fromCity))
		     && (r.getToCity().getCityName().equalsIgnoreCase(toCity))){
			   route=r;
		   }
		  }
			//modified code ends here for CR2-Route-exception
		return null!=route;
		
	}
	
	@Override
	public void addAirline(Airline airline) {
		if (checkGenericAdmin()) {
			genericDao.saveAirline(airline);
		} else {
			/*
			 * code modified for StoredProcedure_CR08.3
			 */
			// modified code starts here for StoredProcedure_CR08.3
			// storedProceduresDao.save(airline); //use this if you want to get
			// airline data through stored procedure

			// modified code ends here for StoredProcedure_CR08.3
			airlineDao.save(airline);
		}
		AirlineReport airlineReport = AirlineModelTransformer
				.transformToAirlineReport(airline);
		reportDao.storeAirlinesReport(airlineReport);
	}

	@Override
	public boolean isDuplicateAirline(Airline airline) {
		Airline air = airlineDao.findByAirlineCode(airline.getAirlineCode());

		/*
		 * code modified for CR1-Airline-exception
		 */
		// modified code starts here for CR1-Airline-exception
		List<Airline> airlines = getAllAirlines();
		System.out.println("size of airlines list"+airlines.size());
		for (Airline airline2 : airlines) {
			System.out.println("//////////////////////////////////////////////////////////////////////////////////////");
			System.out.println(airline.getAirlineCode());
			System.out.println(airline2.getAirlineName());
			System.out.println(airline2.getAirlineCode());
			
			if (airline2.getAirlineName().equals(
							airline.getAirlineName())) {
				return true;
			}
			
			else if(airline2.getAirlineCode().equals(airline.getAirlineCode())) {
				return true;
			}
			
		}
		// modified code ends here for CR1-Airline-exception
		return null != air;
	}

	@Override
	public List<Airline> getAllAirlines() {
		if (checkGenericAdmin()) {
			//return genericDao.getAllAirlines(); //causing errors by retrieving null airlines in clean code for fixing bug thats i am removing this line of code
		return airlineDao.findAll();
		} else {

			/*
			 * code modified for StoredProcedure_CR08.2
			 */
			// modified code starts here for StoredProcedure_CR08.2

			// return storedProceduresDao.listAllAirlines(); //use this to get
			// airlines info using stored procedure

			// modified code ends here for StoredProcedure_CR08.2
			return airlineDao.findAll();
		}

	}

	@Override
	public FlightClass getClassForName(String className) {
		return lookupDao.findForClassName(className);
	}

	@Override
	public Airline getAirlineByCode(String code) {
		return airlineDao.findByAirlineCode(code);
	}

	@Override
	public List<City> getAllCities() {
		if (checkGenericAdmin()) {
			return genericDao.getAllCities();
		} else {
			return cityDao.getAllCities();
		}

	}

	@Override
	public City getCityByName(String name) {
		return cityDao.findCityByName(name);
	}

	@Override
	public List<Flight> getAllFlights() {

		/*
		 * code modified for StoredProcedure_CR08.4
		 */
		// modified code starts here for StoredProcedure_CR08.4

		// return storedProceduresDao.listAllFlights();//remove the comment for
		// getting the all flights data from stored procedures
		// modified code ends here for StoredProcedure_CR08.4
		return flightDao.getAllFlights();
	}

	@Override
	public List<Route> getAllRoutes() {

		/*
		 * code modified for StoredProcedure_CR08.1
		 */
		// modified code starts here for StoredProcedure_CR08.1

		// return storedProceduresDao.listAllRoutes(); //remove the comment for
		// getting the all routes data from stored procedures

		// modified code ends here for StoredProcedure_CR08.1
		return routeDao.getAllRoutes();
	}

	@Override
	public void addFlightSchedule(ScheduledFlight scheduledFlight) {

		flightRouteDao.saveScheduledFlight(scheduledFlight);
		ScheduledFlightReport scheduleFlightReport = ScheduledFlightModelTransformer
				.transformToScheduleReport(scheduledFlight);
		reportDao.storeScheduledFlighReport(scheduleFlightReport);
	}

	@Override
	public Flight getFlightById(long flightId) {
		return flightDao.findByFlightId(flightId);
	}

	@Override
	public List<FlightCapacity> getCapacitiesForFlightId(long flightId) {
		return flightDao.getCapacitiesForFlightId(flightId);
	}

	@Override
	public Route getRouteByCityNames(String fromCity, String toCity) {
		return routeDao.findByCityNames(fromCity, toCity);
	}

	@Override
	public void setScheduleFlightDetails(FlightScheduleDto flightScheduleDto,
			ScheduledFlight scheduledFlight, List<FlightCapacity> capacities) {
		Set<SeatAvailability> availabilitySet = new HashSet<SeatAvailability>();
		int capacitySize = capacities.size() - 1;
		// Loop through the capacities and create the flight capacity objects

		/*
		 * code modified for fixing mock bug 1.1
		 */
		// modified code starts here
		if (flightScheduleDto.getScheduledFlight().getDistanceInKms() > 6000) { // //modified
																				// code
																				// ends
																				// here
			capacitySize = capacities.size();
		}
		for (int i = 0; i <= capacitySize; i++) {
			FlightCapacity seats = capacities.get(i);
			SeatAvailability availability = new SeatAvailability();
			availability.setAvailableSeats(seats.getTotalSeats());
			availability.setFlightClass(seats.getFlightClass());
			availability.setScheduledFlight(scheduledFlight);
			availabilitySet.add(availability);
		}
		scheduledFlight.setAvailability(availabilitySet);
	}

	/*
	 * code modified for CJBPCR_4.1
	 */
	// modified code starts here for CJBPCR_4.1
	public static Comparator<Route> compareRoute = new Comparator<Route>() {

		@Override
		public int compare(Route o1, Route o2) {

			return o1.getFromCity().getCityName().toLowerCase()
					.compareTo(o2.getFromCity().getCityName().toLowerCase());
		}
	};
	// modified code ends here for CJBPCR_4.1

	/*
	 * code modified for CJBPCR_4.3
	 */
	// modified code starts here for CJBPCR_4.3
	public static Comparator<Airline> compareAirline = new Comparator<Airline>() {

		public int compare(Airline o1, Airline o2) {
			Airline air1 = (Airline) o1;
			Airline air2 = (Airline) o2;

			return air1.getAirlineName().toLowerCase().compareTo(air2.getAirlineName().toLowerCase());
		}
	};

	// modified code ends here for CJBPCR_4.3

	/*
	 * code modified for CJBPCR_4.4
	 */
	// modified code starts here for CJBPCR_4.4
	public static Comparator<City> compareCity = new Comparator<City>() {

		@Override
		public int compare(City o1, City o2) {
			return ((City) o1).getCityName().toLowerCase().compareTo(
					((City) o2).getCityName().toLowerCase());
		}
	};

	// modified code ends here for CJBPCR_4.4

}
