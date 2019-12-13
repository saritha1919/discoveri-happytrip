package com.happytrip.services.impl;

import java.io.IOException;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import com.happytrip.controllers.dto.flight.BookingDetailDto;
import com.happytrip.dao.BackupDao;
import com.happytrip.dao.CityDao;
import com.happytrip.dao.FlightBookingDao;
import com.happytrip.dao.FlightRouteDao;
import com.happytrip.dao.jdbc.JdbcBackupDao;
import com.happytrip.dao.jdbc.JdbcCityDao;
import com.happytrip.dao.jdbc.JdbcFlightBookingDao;
import com.happytrip.dao.jdbc.JdbcFlightRouteDao;
import com.happytrip.dao.jdbc.JdbcReportDao;
import com.happytrip.dao.report.ReportDao;
import com.happytrip.model.Backup;
import com.happytrip.model.City;
import com.happytrip.model.FlightBooking;
import com.happytrip.model.FlightClass;
import com.happytrip.model.FlightRouteCost;
import com.happytrip.model.ScheduledFlight;
import com.happytrip.model.reports.BookingReport;
import com.happytrip.services.FlightBookingService;
import com.happytrip.util.backup.FlightBookingBackup;
import com.happytrip.util.transformer.FlightBookingModelTransformer;

public class FlightBookingServiceImpl implements FlightBookingService {

	private FlightRouteDao flightRouteDao;
	
	private FlightBookingDao flightBookingDao;
	
	private BackupDao backupDao;

	private CityDao cityDao;

	private ReportDao reportDao;
	
	public FlightBookingServiceImpl() {
		flightBookingDao = new JdbcFlightBookingDao();
		flightRouteDao = new JdbcFlightRouteDao();
		backupDao = new JdbcBackupDao();
		reportDao = new JdbcReportDao();
		cityDao = new JdbcCityDao();
	}
	
	@Override
	public List<City> findAllFromCities(String cityName) {
		List<City> cities = flightRouteDao.findAllFromCities(cityName);
		return cities.isEmpty()?flightRouteDao.findAllToCity(cityName):cities;
	}

	//--------------------------------------------------------------------------------------------------------------// added method for bug 3.3 starts here
		public boolean isRouteFound(String tocity , String fromcity){
			boolean var1=false, var2=false;
			
			List<City> citiesFrom=flightRouteDao.findAllFromCities(fromcity);
			List<City> citiesTo=flightRouteDao.findAllToCity(tocity);
			for(City city: citiesFrom){
				if(city.getCityName().equalsIgnoreCase(fromcity))
					var1=true;
			}
			for(City city: citiesTo){
				if(city.getCityName().equalsIgnoreCase(tocity))
					var2=true;
			}
			if(var1==true&&var2==true)
				return true;
			return false;
		}
		
	//-------------------------------------------------------added code  for 3.3 ended here	
	@Override
	public List<City> findAllCitiesFlownFrom(String cityName) {
		// TODO Auto-generated method stub
		return flightRouteDao.findAllToCity(cityName);
	}

	@Override
	public List<ScheduledFlight> searchFlights(String fromCity, String toCity,
			FlightClass classOfFlight, int paxNo, Date departureDate) {
		// TODO Auto-generated method stub
		return flightRouteDao.searchFlights(fromCity, toCity, departureDate,
				classOfFlight, paxNo);
	}

	@Override
	public void bookFlight(FlightBooking flightBooking) {
		// TODO Auto-generated method stub
		if(flightBooking.getCostPerTicket()>5000){
			BookingDetailDto booking = FlightBookingModelTransformer.transform(flightBooking);
			try {
				byte[] data = FlightBookingBackup.backup(booking);
				Backup backup = new Backup();
				backup.setData(data);
				backup.setName("FlightBooking");
				backupDao.save(backup);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				throw new RuntimeException(e);
			}
			
		}
		flightBookingDao.save(flightBooking);
		BookingReport bookingReport = FlightBookingModelTransformer.transformToBookingReport(flightBooking);
		reportDao.storeBookingReport(bookingReport );
		
	}
	
	@Override
	public ScheduledFlight searchScheduledFlightById(long id){
		return flightRouteDao.findScheduledFlightById(id);
	}
	
	/* code modified for CJBPCR_4.2
	 */
	//modified code starts here for CJBPCR_4.2
	
	public static Comparator<ScheduledFlight> compareByTicketCost=new Comparator<ScheduledFlight>() {
		  @Override
		  public int compare(ScheduledFlight o1, ScheduledFlight o2) {
		    int a=0,b=0;

		   for(FlightRouteCost frc:o1.getFlightRouteCosts()){
		    a=(int) (frc.getCostPerTicket()+a);
		   }
		   for(FlightRouteCost frc:o2.getFlightRouteCosts()){
		    b=(int) (frc.getCostPerTicket()+b);
		   }
		   return  new Integer(a).compareTo(new Integer(b));
		   }
		 };
		 
			//modified code ends here for CJBPCR_4.2
}
