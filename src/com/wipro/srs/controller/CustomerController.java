package com.wipro.srs.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.wipro.srs.bean.PassengerBean;
import com.wipro.srs.bean.PaymentBean;
import com.wipro.srs.bean.ProfileBean;
import com.wipro.srs.bean.ReservationBean;
import com.wipro.srs.bean.ScheduleBean;
import com.wipro.srs.service.Administrator;
import com.wipro.srs.service.Customer;

@Controller
@SessionAttributes({"emptyPassengers","profile"})
public class CustomerController {
	
	@Autowired
	private Customer customerService;
	@Autowired
	private Administrator adminService;
	
	public static String source;
	public static String destination;
	private static Date travelDate;
	private static double fare;
	
	@RequestMapping("/bookTicket")
	public ModelAndView bookTicket(Model model, HttpServletRequest request,@ModelAttribute("profile") ProfileBean profile){
		List<PassengerBean> passengers = new ArrayList<PassengerBean>();
		int count = Integer.parseInt(request.getParameter("passengers"));
		fare = Double.parseDouble(request.getParameter("fare"));
		fare = fare * count;
		String scheduleID = request.getParameter("scheduleID");
		for(int i = 0; i < count; i++){
			passengers.add(new PassengerBean());
		}
		ReservationBean reservationBean = new ReservationBean();
		reservationBean.setPassengers(passengers);
		reservationBean.setScheduleID(scheduleID);
		reservationBean.setUserID(profile.getUserID());
		reservationBean.setNoOfSeats(count);
		reservationBean.setJourneyDate(travelDate);
		reservationBean.setBookingDate(new Date());
		reservationBean.setTotalFare(fare);
		model.addAttribute("emptyPassengers", reservationBean);
		return new ModelAndView("bookTicket");
	}
	
	@RequestMapping(value = "/setPassenger",method = RequestMethod.POST)
	public ModelAndView allSchedules(Model model, HttpServletRequest request){
		source = request.getParameter("source");
		destination = request.getParameter("destination");
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
		String dateInString = request.getParameter("travelDate");
		Date date = null;
		try {
			date = formatter.parse(dateInString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		travelDate = date;
		model.addAttribute("allSchedules",this.customerService.viewScheduleByRoute(source, destination,date));
		return new ModelAndView("allSchedules");
	}
	
	@RequestMapping(value = "/reserveTicket",method = RequestMethod.POST)
	public ModelAndView reserveTicket(Model model,@ModelAttribute("emptyPassengers") ReservationBean reservationBean, @ModelAttribute("paymentBean") PaymentBean paymentBean){
		if(this.adminService.deductFare(paymentBean,fare)){
			String reservationID = this.customerService.reserveTicket(reservationBean, reservationBean.getPassengers());
			Map<ReservationBean, PassengerBean> ticket = this.customerService.viewTicket(reservationID);
			ArrayList<ReservationBean> reservation = (ArrayList<ReservationBean>) new ArrayList<>(ticket.keySet());
			model.addAttribute("ticket", reservationBean);
			System.out.println(reservation.get(0).getScheduleID());
			model.addAttribute("schedule", this.adminService.viewByScheduleId(reservation.get(0).getScheduleID()));
			return new ModelAndView("viewTicket");
		}
		else{
			return new ModelAndView("Error");
		}	
	}
	
	@RequestMapping(value = "/viewTicket",method = RequestMethod.GET)
	public ModelAndView viewTicket(Model model,HttpServletRequest request){
		Map<ReservationBean, PassengerBean> ticket = this.customerService.viewTicket(request.getParameter("id"));
		ArrayList<ReservationBean> reservation = (ArrayList<ReservationBean>) new ArrayList<>(ticket.keySet());
		model.addAttribute("ticket", reservation);
		System.out.println(reservation.get(0).getScheduleID());
		model.addAttribute("schedule", this.adminService.viewByScheduleId(reservation.get(0).getScheduleID()));
		return new ModelAndView("viewTicket");	
	}
	
	@ModelAttribute("paymentBean")
	public PaymentBean saveCreditCard(){
		return new PaymentBean();
	}
	
	@RequestMapping(value = "/setCard",method = RequestMethod.GET)
	public ModelAndView setCard(){
		return new ModelAndView("payment");
		
	}
	
	@RequestMapping(value = "/doPayment",method = RequestMethod.POST)
	public ModelAndView payTicket(Model model,@ModelAttribute("emptyPassengers") ReservationBean reservationBean){
		model.addAttribute("totalFare",fare);
		return new ModelAndView("makePayment");
		
	}
	
	
	@RequestMapping(value = "/saveCard",method = RequestMethod.POST)
	public ModelAndView saveCard(@ModelAttribute("paymentBean") PaymentBean paymentBean, @ModelAttribute("profile") ProfileBean profile){
		paymentBean.setUserID(profile.getUserID());
		System.out.println(profile.getUserID());
		System.out.println(paymentBean.getCreditCardNo());
		this.adminService.saveCard(paymentBean);
		return new ModelAndView("Login");
		
	}
		
	@RequestMapping(value = "/cancelTicket",method = RequestMethod.GET)
	public ModelAndView cancelTicket(HttpServletRequest request){
		if(this.customerService.cancelTicket(request.getParameter("id"))){
			return new ModelAndView("Home");
		}else{
			return new ModelAndView("Error");
		}
		
	}
	
	@RequestMapping(value = "/setReservation",method = RequestMethod.GET)
	public ModelAndView setPassenger(){
		return new ModelAndView("setReservation");
	}
	
	@ModelAttribute(value = "schedule")
	public ScheduleBean setScheduleModel(){
		return new ScheduleBean();
	}
}