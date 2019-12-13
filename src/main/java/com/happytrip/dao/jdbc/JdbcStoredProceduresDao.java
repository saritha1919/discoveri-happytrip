package com.happytrip.dao.jdbc;

import static com.happytrip.dao.jdbc.ConnectionHelper.cleanup;
import static com.happytrip.dao.jdbc.ConnectionHelper.getMySqlConnection;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.happytrip.dao.StoredProceduresDao;
import com.happytrip.model.Airline;
import com.happytrip.model.City;
import com.happytrip.model.Flight;
import com.happytrip.model.Route;

public class JdbcStoredProceduresDao implements StoredProceduresDao {

	/*
	 * code modified for StoredProcedure_CR08.1
	 */
	// modified code starts here for StoredProcedure_CR08.1

	@Override
	public List<Route> listAllRoutes() {
		Connection con = null;
		List<Route> routes = new ArrayList<Route>();
		try {
			con = getMySqlConnection();
			CallableStatement callableStatement = con
					.prepareCall("call listAllRoutes()");
			callableStatement.executeQuery();

			ResultSet result = callableStatement.getResultSet();
			while (result.next()) {
				Route route = new Route();
				route.setRouteId(result.getLong(1));
				route.setFromCity(new City(result.getString(2)));
				route.setToCity(new City(result.getString(3)));
				route.getFromCity().setCityId(result.getLong(4));
				route.getToCity().setCityId(result.getLong(5));
				routes.add(route);
			}

		} catch (SQLException s) {
			s.printStackTrace();

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return routes;
	}

	// modified code ends here for StoredProcedure_CR08.1

	/*
	 * code modified for StoredProcedure_CR08.2
	 */
	// modified code starts here for StoredProcedure_CR08.2
	@Override
	public List<Airline> listAllAirlines() {
		Connection con = null;
		List<Airline> airlines = new ArrayList<Airline>();
		try {
			con = getMySqlConnection();
			CallableStatement callableStatement = con
					.prepareCall("call listAllAirlines");
			callableStatement.executeQuery();

			ResultSet result = callableStatement.getResultSet();
			while (result.next()) {
				Airline airline = new Airline();
				airline.setAirlineCode(result.getString("airlinecode"));
				airline.setAirlineId(result.getInt("airlineid"));
				airline.setAirlineName(result.getString("airlinename"));
				airline.setAirlineLogo(result.getString("airlinelogo"));
				airlines.add(airline);
			}

		} catch (SQLException s) {
			s.printStackTrace();

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return airlines;
	}

	// modified code ends here for StoredProcedure_CR08.2

	/*
	 * code modified for StoredProcedure_CR08.3
	 */
	// modified code starts here for StoredProcedure_CR08.3
	@Override
	public void save(Airline airline) {

		Connection con = null;

		try {
			con = getMySqlConnection();

			CallableStatement callableStatement = con
					.prepareCall("call saveAirline(?,?)");
			callableStatement.setString(1, airline.getAirlineCode());
			callableStatement.setString(2, airline.getAirlineName());
			callableStatement.executeUpdate();
			con.close();
		} catch (Exception e) {

			throw new RuntimeException(e.getMessage());
		}

	}

	// modified code ends here for StoredProcedure_CR08.4

	/*
	 * code modified for StoredProcedure_CR08.4
	 */
	// modified code starts here for StoredProcedure_CR08.4

	@Override
	public List<Flight> listAllFlights() {
		Connection con = null;
		List<Flight> flights = new ArrayList<Flight>();
		try {
			con = getMySqlConnection();
			CallableStatement callableStatement = con
					.prepareCall("call listAllFlights()");
			callableStatement.executeQuery();

			ResultSet result = callableStatement.getResultSet();
			while (result.next()) {

				Flight flight = new Flight();
				flight.setFlightId(result.getLong(1));
				flight.setFlightName(result.getString(2));
				flight.setAirline(new Airline(result.getLong(1)));
				flights.add(flight);
			}

		} catch (SQLException s) {
			s.printStackTrace();

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return flights;
	}
	// modified code ends here for StoredProcedure_CR08.4

}
