package com.wipro.srs.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wipro.srs.bean.PassengerBean;
import com.wipro.srs.bean.ReservationBean;
import com.wipro.srs.bean.ScheduleBean;

@Service
public class CustomerImplementation implements Customer{

	@Autowired
	private com.wipro.srs.dao.Customer customerDAO;
	
	@Override
	@Transactional
	public ArrayList<ScheduleBean> viewScheduleByRoute(String source, String destination, Date date) {
		return this.customerDAO.viewScheduleByRoute(source, destination, date);
	}


	@Override
	@Transactional
	public boolean cancelTicket(String reservationId) {
		return this.customerDAO.cancelTicket(reservationId);
	}

	@Override
	@Transactional
	public Map<ReservationBean, PassengerBean> viewTicket(String reservationId) {
		return this.customerDAO.viewTicket(reservationId);
	}

	@Override
	@Transactional
	public String reserveTicket(ReservationBean reservationBean, List<PassengerBean> list) {
		return this.customerDAO.reserveTicket(reservationBean, list);
	}
}