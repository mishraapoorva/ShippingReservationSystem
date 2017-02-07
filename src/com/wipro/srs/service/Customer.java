package com.wipro.srs.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.wipro.srs.bean.PassengerBean;
import com.wipro.srs.bean.ReservationBean;
import com.wipro.srs.bean.ScheduleBean;

public interface Customer {
	ArrayList<ScheduleBean> viewScheduleByRoute(String source, String destination, Date date);
	String reserveTicket(ReservationBean reservationBean, List<PassengerBean> list);
	boolean cancelTicket(String reservationId);
	Map<ReservationBean,PassengerBean> viewTicket(String reservationId);
}
