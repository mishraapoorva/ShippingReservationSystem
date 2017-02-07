package com.wipro.srs.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.wipro.srs.bean.PassengerBean;
import com.wipro.srs.bean.PaymentBean;
import com.wipro.srs.bean.ReservationBean;
import com.wipro.srs.bean.ScheduleBean;
import com.wipro.srs.controller.CustomerController;

@Repository
public class CustomerDAO implements Customer {
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	public ArrayList<ScheduleBean> viewScheduleByRoute(String source, String destination, Date date) {
		Session session = this.sessionFactory.getCurrentSession();
		Query query = session.createQuery("from ScheduleBean where trunc(startDate) = trunc(?) and route.source = ? and route.destination = ?");
		query.setParameter(0, date);
		query.setParameter(1, source);
		query.setParameter(2, destination);
		@SuppressWarnings("unchecked")
		ArrayList<ScheduleBean> schedules = (ArrayList<ScheduleBean>) query.list();
		return schedules;
	}

	@Override
	public String reserveTicket(ReservationBean reservationBean, List<PassengerBean> passengerBean) {
		int uniqueId = getNextKey().intValueExact();
		reservationBean.setReservationID(CustomerController.source.substring(0, 2) + CustomerController.destination.substring(0, 2) + uniqueId);
		Iterator<PassengerBean> iterator = passengerBean.iterator();
		while(iterator.hasNext()){
			int uniqueId1 = getNextKey().intValueExact();
			PassengerBean bean = iterator.next();
			bean.setPassengerID(bean.getName().substring(0, 2)+ uniqueId1);
			bean.setScheduleID(reservationBean.getScheduleID());
			bean.setReservationID(reservationBean.getReservationID());
		}
		reservationBean.setPassengers(passengerBean);
		System.out.println(passengerBean.get(0).getName());
		Session session = this.sessionFactory.getCurrentSession();
		session.save(reservationBean);
		return reservationBean.getReservationID();
	}

	@Override
	public boolean cancelTicket(String reservationId) {
		Session session = this.sessionFactory.getCurrentSession();
		ReservationBean reservationBean = (ReservationBean) session.get(ReservationBean.class, reservationId);
		if(reservationBean != null){
			PaymentBean paymentBean = (PaymentBean) session.createQuery("from PaymentBean where userID = "+"'"+reservationBean.getUserID()+"'").uniqueResult();
			if(paymentBean != null){
				paymentBean.setBalance(paymentBean.getBalance() + reservationBean.getTotalFare());
				session.delete(reservationBean);
				return true;
			}
			else{
				return false;
			}
		}else{
			return false;
		}
	}

	@Override
	public Map<ReservationBean, PassengerBean> viewTicket(String reservationId) {
		Session session = this.sessionFactory.getCurrentSession(); 
		ReservationBean reservationBean = (ReservationBean) session.get(ReservationBean.class, reservationId);
		if(reservationBean != null){
			Map<ReservationBean, PassengerBean> ticket = new HashMap<ReservationBean, PassengerBean>();
			ticket.put(reservationBean, null);
			return ticket;
		}
		else{
			return null;
		}
	}
	
	public BigDecimal getNextKey()
	{
		Session session = this.sessionFactory.getCurrentSession();
		Query query = session.createSQLQuery("select SRS_SEQ_USER_ID.nextval from dual");
		return  (BigDecimal) query.uniqueResult();
	}

	@Override
	public boolean saveCard(PaymentBean paymentBean) {
		Session session = this.sessionFactory.getCurrentSession();
		session.save(paymentBean);
		return true;
	}
	
	@Override
	public boolean deductFare(PaymentBean paymentBean, double fare) {
		Session session = this.sessionFactory.getCurrentSession();
		PaymentBean payment = (PaymentBean) session.get(PaymentBean.class, paymentBean.getCreditCardNo());
		if(payment != null){
			if(payment.getBalance() > fare){
				payment.setBalance(payment.getBalance() - fare);
				System.out.println("Here");
				return true;
			}
		}
		return false;
	}
}