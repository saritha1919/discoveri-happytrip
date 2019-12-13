package com.happytrip.controllers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.happytrip.controllers.dto.flight.FlightSearchDto;
import com.happytrip.controllers.dto.flight.FlightSelectionDto;
import com.happytrip.dao.LookupDao;
import com.happytrip.dao.RouteDao;
import com.happytrip.dao.jdbc.JdbcLookupDao;
import com.happytrip.dao.jdbc.JdbcRouteDao;
import com.happytrip.model.ScheduledFlight;
import com.happytrip.services.FlightBookingService;
import com.happytrip.services.UserProfileService;
import com.happytrip.services.impl.FlightBookingServiceImpl;
import com.happytrip.util.BeanFactory;

@Controller
@SessionAttributes({ "SearchData", "SelectedFlights" })
public class SearchController {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(SearchController.class);

	private FlightBookingService flightSearchService;
	private LookupDao lookupDao;

	public SearchController() {
		flightSearchService = new FlightBookingServiceImpl();
		lookupDao = new JdbcLookupDao();
	}

	public void setFlightSearchService(FlightBookingService flightSearchService) {
		this.flightSearchService = flightSearchService;
	}

	public void setLookupDao(LookupDao lookupDao) {
		this.lookupDao = lookupDao;
	}

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		dateFormat.setLenient(false);

		// true passed to CustomDateEditor constructor means convert empty
		// String to null
		binder.registerCustomEditor(Date.class, new CustomDateEditor(
				dateFormat, true));

	}

	@ModelAttribute("SearchData")
	public FlightSearchDto getSearchData() {
		return new FlightSearchDto();
	}

	@ModelAttribute("SelectedFlights")
	public FlightSelectionDto getSelectedFlights() {
		return new FlightSelectionDto();
	}

	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/searchFlights.html", method = RequestMethod.GET)
	public String searchFlights(Locale locale, Model model) {
		LOGGER.info("Welcome home! the client locale is " + locale.toString());

		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG,
				DateFormat.LONG, locale);

		String formattedDate = dateFormat.format(date);

		model.addAttribute("serverTime", formattedDate);

		return "flight/flight_booking";
	}

	/**
	 * 
	 * @param searchData
	 * @param result
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/flight/search.html", method = RequestMethod.GET)
	public ModelAndView search(
			@ModelAttribute("SearchData") FlightSearchDto searchData,Model model) {
		List<ScheduledFlight> toFlights = null;
		List<ScheduledFlight> returnFlights = null;

		RouteDao routeDao=new JdbcRouteDao();   // added code CR 3.3
		if(routeDao.findByCityNames(searchData.getFromCity(), searchData.getToCity())!=null){// added code CR 3.3
		
			System.out.println("city from"+searchData.getFromCity());
			System.out.println("to city"+searchData.getToCity());
			System.out.println(searchData.getFlightClass());
			System.out.println(searchData.getDepartureDate());
			System.out.println(searchData.getNoOfAdults() + searchData.getNoOfChildren()
						+ searchData.getNoOfInfants());
			
			System.out.println(routeDao.findByCityNames(searchData.getToCity(),searchData.getFromCity()));
		toFlights = this.flightSearchService.searchFlights(
				searchData.getFromCity(), searchData.getToCity(),
				lookupDao.findForClassName(searchData.getFlightClass()),
				searchData.getNoOfAdults() + searchData.getNoOfChildren()
						+ searchData.getNoOfInfants(),
				searchData.getDepartureDate());
		
		if(toFlights.isEmpty()){//Added code start from here to fix CR3.3
			model.addAttribute("message", "No scheduled Flights Found!! for Your search");
		}else{
			System.out.println("Flights Found");
			for(ScheduledFlight scheduledFlight:toFlights)
			System.out.println(scheduledFlight.toString()+""+scheduledFlight.getFlightRouteId());
		}
		
		}
		else{
				model.addAttribute("message", "No Direct Route Found!!"); 
		}
	                                     	//Added code start from here to fix CR3.3
		if (searchData.isReturnJourney()) {
			returnFlights = this.flightSearchService.searchFlights(
					searchData.getToCity(), searchData.getFromCity(),
					lookupDao.findForClassName(searchData.getFlightClass()),
					searchData.getNoOfAdults() + searchData.getNoOfChildren()
							+ searchData.getNoOfInfants(),
					searchData.getReturnDate());
		}
		
						
		Map<String, Object> models = new HashMap<String, Object>();
		models.put("fromCity", searchData.getFromCity());
		models.put("toCity", searchData.getToCity());
		/* code modified for CJBPCR_4.2
		 */
		//modified code starts here for CJBPCR_4.2
	   if(toFlights!=null)
		Collections.sort(toFlights,FlightBookingServiceImpl.compareByTicketCost);
		
		//modified code ends here for CJBPCR_4.2
		models.put("toFlights", toFlights);
		FlightSelectionDto selectedFlights = new FlightSelectionDto();
		if (toFlights != null && !toFlights.isEmpty()) {
			selectedFlights.setSelectedOutboundFlight(toFlights.get(0));
		}
		models.put("returnFlights", returnFlights);
		if (returnFlights != null && !returnFlights.isEmpty()) {
			selectedFlights.setSelectedReturnFlight(returnFlights.get(0));
		}
		models.put("SelectedFlights", selectedFlights);

		/* code modified for CR3-Search-exception
		 */
		//modified code starts here for CR3-Search-exception
		
		boolean isRouteAvailable = false;

		if ((new JdbcRouteDao().findByCityNames(searchData.getFromCity(),
				searchData.getToCity())) != null) {
			isRouteAvailable = true;
		}

		models.put("isRouteAvailable", isRouteAvailable);
		
		//modified code ends here for CR3-Search-exception
		
		return new ModelAndView("/flight/searchResults", models);
	}

}
