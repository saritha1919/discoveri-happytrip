package com.happytrip.controllers.flight;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.happytrip.controllers.dto.flight.FlightBookingDto;
import com.happytrip.controllers.dto.flight.FlightSearchDto;
import com.happytrip.controllers.dto.flight.FlightSelectionDto;
import com.happytrip.controllers.dto.flight.PassengerListDto;
import com.happytrip.dao.FlightRouteDao;
import com.happytrip.dao.LookupDao;
import com.happytrip.dao.jdbc.JdbcLookupDao;
import com.happytrip.model.Booking;
import com.happytrip.model.BookingContact;
import com.happytrip.model.FlightBooking;
import com.happytrip.model.FlightRouteCost;
import com.happytrip.model.Passenger;
import com.happytrip.model.ScheduledFlight;
import com.happytrip.model.User;
import com.happytrip.services.UserProfileService;
import com.happytrip.services.impl.UserProfileServiceImpl;
import com.happytrip.util.AuthenticatedUserUtil;
import com.happytrip.util.BeanFactory;
import com.happytrip.util.StringUtil;

@Controller
@SessionAttributes({ "SearchData", "FlightSelected", "PassengersList",
		"Bookings" })
public class FlightConfirmationController {

	private UserProfileService profileService;
	private LookupDao lookupDao;

	public FlightConfirmationController() {
		lookupDao = new JdbcLookupDao();
		profileService = new UserProfileServiceImpl();
	}

	public void setProfileService(UserProfileService profileService) {
		this.profileService = profileService;
	}

	public void setLookupDao(LookupDao lookupDao) {
		this.lookupDao = lookupDao;
	}

	@ModelAttribute("Bookings")
	public FlightBookingDto getFlightBooking() {
		return new FlightBookingDto();
	}

	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(
				dateFormat, false));

	}
	
	
	/*
	@RequestMapping(value = "/flight/showBookingSummary.html", method = RequestMethod.GET)
	public String addPassengers1(
			@ModelAttribute("SearchData") FlightSearchDto searchData,
			@ModelAttribute("FlightSelected") FlightSelectionDto selectedFlights,
			@ModelAttribute("PassengersList") PassengerListDto passengerList,
			@ModelAttribute("Bookings") FlightBookingDto bookings,BindingResult result){
			
		for(Passenger passenger:passengerList.getPassengers()){
			try {
				if (StringUtil.getAllString(" ", "data/abusivewords.txt").contains(passenger.getName())) {
					result.rejectValue("email","UserDto.email.required","Blocked Email");
					return "addPassengers";
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
     }
		return null;	
		
		
	}
	
*/	
	
	
	

	@RequestMapping(value = "/flight/showBookingSummary.html", method = RequestMethod.GET)
	public ModelAndView addPassengers(
			@ModelAttribute("SearchData") FlightSearchDto searchData,
			@ModelAttribute("FlightSelected") FlightSelectionDto selectedFlights,
			@ModelAttribute("PassengersList") PassengerListDto passengerList,
			@ModelAttribute("Bookings") FlightBookingDto bookings,BindingResult result, Model model) throws IOException {
		Map<String, Object> models = new HashMap<String, Object>();
		
		// modified code starts here for File Handling_CR_07.3
        for(Passenger passenger:passengerList.getPassengers()){
				if (StringUtil.getAllString(" ", "data/abusivewords.txt").contains(passenger.getName())) {
					model.addAttribute("error", "Passengers name should not contain abusive words");
					model.addAttribute("PassengerList", passengerList);
					return new ModelAndView("/flight/addPassengers",models);
				}
	     }	
				// modified code ends here for File Handling_CR_07.3

		
		
		Passenger primaryPassenger = passengerList.getPassengers().get(0);

		System.out.println("in FlightconfirmationContrller");
		
		
		models.put("PrimaryPassenger", primaryPassenger);

		FlightBooking outboundFlightBooking = createOutboundBooking(
				searchData.getFlightClass(), searchData.getDepartureDate(),
				selectedFlights.getSelectedOutboundFlight(), passengerList,
				primaryPassenger);
		bookings.setOutboundFlightBooking(outboundFlightBooking);

		if (selectedFlights.getSelectedReturnFlight() != null) {
			FlightBooking returnFlightBooking = createOutboundBooking(
					searchData.getFlightClass(), searchData.getReturnDate(),
					selectedFlights.getSelectedReturnFlight(), passengerList,
					primaryPassenger);
			bookings.setReturnFlightBooking(returnFlightBooking);
		}
		return new ModelAndView("/flight/showBookingSummary", models);
	}

	FlightBooking createOutboundBooking(String flightClassName,
			Date journeyDate, ScheduledFlight flightToBeBooked,
			PassengerListDto passengerList, Passenger primaryPassenger) {
		// TODO Auto-generated method stub
		FlightBooking flightBooking = new FlightBooking();

		flightBooking.setFlightClass(lookupDao
				.findForClassName(flightClassName));

		flightBooking.setDateOfJourney(journeyDate);

		Set<Passenger> passengers = new HashSet<Passenger>();
		passengers.addAll(passengerList.getPassengers());
		flightBooking.setPassengers(passengers);

		flightBooking.setFlightRoute(flightToBeBooked);
		Set<FlightRouteCost> costs = flightToBeBooked.getFlightRouteCosts();

		float totalCost = calculateTotalCost(costs, passengerList);

		Booking booking = new Booking();
		booking.setBookingReferenceNo(Long.toString(StringUtil
				.generateReference(System.currentTimeMillis())));

		BookingContact contact = new BookingContact();
		contact.setContactName(primaryPassenger.getName());
		contact.setMobileNo(passengerList.getMobileNumber());

		User loggedInUser = getLoggedInUser();

		if (loggedInUser != null) {
			booking.setBooker(loggedInUser);
		}
		booking.setBookingcontact(contact);
		flightBooking.setBooking(booking);
		/*
		 * 
		 * code modified for mock bug 2.1
		 */
		// modified code starts here
		booking.setTotalCost(totalCost);
		// modified code ends here
		return flightBooking;
	}

	public User getLoggedInUser() {
		User loggedInUser = AuthenticatedUserUtil
				.currentLoggedInUser(profileService);
		return loggedInUser;
	}

	private float calculateTotalCost(Set<FlightRouteCost> costs,
			PassengerListDto passengerList) {
		// TODO Auto-generated method stub
		int totalPax = passengerList.getPassengers().size();
		float totalCost = 0;
		if (costs != null) {
			FlightRouteCost perTicketCost = costs.iterator().next();
			totalCost = totalPax * perTicketCost.getCostPerTicket();
		}
		return totalCost;
	}

}
