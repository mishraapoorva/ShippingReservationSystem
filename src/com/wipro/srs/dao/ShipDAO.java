package com.wipro.srs.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.wipro.srs.bean.PassengerBean;
import com.wipro.srs.bean.PaymentBean;
import com.wipro.srs.bean.ReservationBean;
import com.wipro.srs.bean.RouteBean;
import com.wipro.srs.bean.ScheduleBean;
import com.wipro.srs.bean.ShipBean;

@Repository
public class ShipDAO implements Ship {
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	public String addShip(ShipBean shipbean) {
		if(shipbean.getShipID() != null){
			Session session = this.sessionFactory.getCurrentSession();
			session.update(shipbean);
			return "SUCCESS";
		}
		else{
			int uniqueId = getNextKey().intValueExact();
			shipbean.setShipID(shipbean.getShipName().substring(0, 2) + uniqueId);
			try{
				Session session = this.sessionFactory.getCurrentSession();
				session.save(shipbean);
				return "SUCCESS";
			}catch(Exception e){
				return "FAIL";
			}
		}
	}

	@Override
	public int removeShip(ArrayList<String> ShipId) {
		Session session = this.sessionFactory.getCurrentSession();
		for(int i = 0; i < ShipId.size(); i++){
			ShipBean ship = (ShipBean) session.get(ShipBean.class, ShipId.get(i));
			if(ship != null){
				@SuppressWarnings("unchecked")
				ArrayList<ScheduleBean> allSchedules = (ArrayList<ScheduleBean>) session.createQuery("from ScheduleBean where shipID ="+"'"+ShipId.get(i)+"'").list();
				ArrayList<String> allScheduleIDs = new ArrayList<String>();
				if(allSchedules.size() != 0){
					for(int j = 0; j < allSchedules.size(); j++){
						allScheduleIDs.add(allSchedules.get(j).getScheduleID());
					}
					if(removeSchedule(allScheduleIDs) != 1){
						return 0;
					}
				}else{
					System.out.println("Right Here");
					session.delete(ship);
				}
			}
			
		}
		return 0;
	}

	@Override
	public String addSchedule(ScheduleBean schedulebean) {
		System.out.println(schedulebean.getScheduleID());
		if(schedulebean.getScheduleID() != null){
			Session session = this.sessionFactory.getCurrentSession();
			session.update(schedulebean);
			return "SUCCESS";
		}
		else{
			int uniqueId = getNextKey().intValueExact();
			schedulebean.setRoute(viewByRouteId(schedulebean.getRouteID()));
			schedulebean.setShip(viewByShipId(schedulebean.getShipID()));
			schedulebean.setScheduleID(schedulebean.getRoute().getSource().substring(0, 2) + schedulebean.getRoute().getDestination().substring(0, 2) + uniqueId);
			try{
				Session session = this.sessionFactory.getCurrentSession();
				session.save(schedulebean);
			    return "SUCCESS";
			}catch(Exception e){
				return "FAIL";
			}
		}
	}

	@Override
	public boolean modifySchedule(ScheduleBean schedulebean) {
		return false;
	}

	@Override
	public int removeSchedule(ArrayList<String> scheduleid) {
		if(scheduleid.size() > 0){
			Session session = this.sessionFactory.getCurrentSession();
			for(int i = 0; i < scheduleid.size(); i++){
				String id = scheduleid.get(i).replaceAll("~!","");
				@SuppressWarnings("unchecked")
				ArrayList<ReservationBean> reservations = (ArrayList<ReservationBean>) session.createQuery("from ReservationBean where scheduleID = "+"'"+id+"'").list();
				System.out.println(reservations.size());
				if(reservations.size() > 0){
					for(int j = 0; j < reservations.size(); j++){
						cancelTicket(reservations.get(j).getReservationID());
					}
					System.out.println("Hi");
					Query query = session.createQuery("delete from ScheduleBean where scheduleID = "+"'"+id+"'");
					System.out.println(query.executeUpdate());
					ScheduleBean schedulebean = (ScheduleBean) session.get(ScheduleBean.class, id);
					session.delete(schedulebean);
					return 1;
				}	
				else{
					ScheduleBean schedulebean = (ScheduleBean) session.get(ScheduleBean.class, id);
					session.delete(schedulebean);
				}
			}
		}
		else{
			return 0;
		}
		return 0;
	}

	
	public boolean cancelTicket(String reservationId) {
		Session session = this.sessionFactory.getCurrentSession();
		ReservationBean reservationBean = (ReservationBean) session.get(ReservationBean.class, reservationId);
		if(reservationBean != null){
			PaymentBean paymentBean = (PaymentBean) session.createQuery("from PaymentBean where userID = "+"'"+reservationBean.getUserID()+"'").uniqueResult();
			if(paymentBean != null){
				Query query = session.createQuery("from PassengerBean where reservationID = ?");
				query.setParameter(0, reservationId);
				ArrayList<PassengerBean> passengers = (ArrayList<PassengerBean>) query.list();
				System.out.println(passengers.size());
				for(int i = 0; i < passengers.size(); i++){
					PassengerBean passenger = (PassengerBean) session.get(PassengerBean.class,passengers.get(i).getPassengerID());
					session.delete(passenger);
				}
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
	public String addRoute(RouteBean routebean) {
		int uniqueId = getNextKey().intValueExact();
		routebean.setRouteID(routebean.getSource().substring(0, 2) + routebean.getDestination().substring(0, 2) + uniqueId);
		try{
			Session session = this.sessionFactory.getCurrentSession();
			session.save(routebean);
			return "SUCCESS";
		}catch(Exception e){
			return "FAIL";
		}
	}

	@Override
	public boolean modifyRoute(RouteBean routebean) {
		return false;
	}

	@Override
	public int removeRoute(String routeid) {
		Session session = this.sessionFactory.getCurrentSession();
		RouteBean routeBean = (RouteBean) session.get(RouteBean.class, routeid);
		if(routeBean != null){
			@SuppressWarnings("unchecked")
			ArrayList<ScheduleBean> allSchedules = (ArrayList<ScheduleBean>) session.createQuery("from ScheduleBean where routeid ="+"'"+routeid+"'").list();
			if(allSchedules.size() == 0){
				session.delete(routeBean);
			}
			else{
				ArrayList<String> scheduleIDs = new ArrayList<String>();
				for(ScheduleBean each : allSchedules){
					scheduleIDs.add(each.getScheduleID());
				}
				if(removeSchedule(scheduleIDs) == 1){
					return 1;
				}
				return 0;
			}
			return 0;
		}
		else{
			return 0;
		}
	}

	@Override
	public ShipBean viewByShipId(String ShipId) {
		Session session = this.sessionFactory.getCurrentSession();
		ShipBean shipBean = (ShipBean) session.get(ShipBean.class, ShipId);
		return shipBean;
	}

	@Override
	public RouteBean viewByRouteId(String routeid) {
		Session session = this.sessionFactory.getCurrentSession();
		RouteBean routeBean = (RouteBean) session.get(RouteBean.class, routeid);
		return routeBean;
	}

	@Override
	public ArrayList<ShipBean> viewByAllShips() {
		Session session = this.sessionFactory.getCurrentSession();
		@SuppressWarnings("unchecked")
		ArrayList<ShipBean> shipsList = (ArrayList<ShipBean>) session.createQuery("from ShipBean").list();
		if(shipsList != null){
			return shipsList;
		}else{
			return null;
		}
	}

	@Override
	public ArrayList<RouteBean> viewByAllRoute() {
		Session session = this.sessionFactory.getCurrentSession();
		@SuppressWarnings("unchecked")
		ArrayList<RouteBean> routeList = (ArrayList<RouteBean>) session.createQuery("from RouteBean").list();
		if(routeList != null){
			return routeList;
		}else{
			return null;
		}
	}

	@Override
	public ArrayList<ScheduleBean> viewByAllSchedule() {
		Session session = this.sessionFactory.getCurrentSession();
		@SuppressWarnings("unchecked")
		Date today = new Date();
		Query query = session.createQuery("from ScheduleBean where trunc(startDate) >= trunc(?)");
		query.setParameter(0, today); 
		@SuppressWarnings("unchecked")
		ArrayList<ScheduleBean> scheduleList = (ArrayList<ScheduleBean>)query.list();
		if(scheduleList != null){
			return scheduleList;
		}else{
			return null;
		}
	}

	@Override
	public ScheduleBean viewByScheduleId(String scheduleid) {
		Session session = this.sessionFactory.getCurrentSession();
		ScheduleBean schedule = (ScheduleBean) session.get(ScheduleBean.class, scheduleid);
		if(schedule != null){
			return schedule;
		}
		else{
			return null;
		}
		
	}

	@Override
	public ArrayList<PassengerBean> viewPasengersByShip(String scheduleid) {
		Session session = this.sessionFactory.getCurrentSession();
		Query query = session.createQuery("from PassengerBean where scheduleID = ?");
		query.setParameter(0, scheduleid);
		@SuppressWarnings("unchecked")
		ArrayList<PassengerBean> passengers = (ArrayList<PassengerBean>) query.list();
		System.out.println(passengers.size());
		if(passengers.size() > 0){
				return passengers;
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
	public int reduceShipCapacity(String scheduleID, Date travelDate, boolean flag) {
		Session session = this.sessionFactory.getCurrentSession();
		Query query = session.createQuery("from ReservationBean where trunc(journeyDate) = trunc(?) and scheduleID = ?");
		query.setParameter(0, travelDate);
		query.setParameter(1, scheduleID);
		@SuppressWarnings("unchecked")
		ArrayList<ReservationBean> reservations = (ArrayList<ReservationBean>)query.list();
		int seats = 0;
		for(ReservationBean bean: reservations){
			seats = seats + bean.getNoOfSeats();
		}
		Query shipQuery = session.createQuery("from ScheduleBean where scheduleID = ?");
		shipQuery.setParameter(0,scheduleID);
		ScheduleBean scheduleBean = (ScheduleBean) shipQuery.uniqueResult();
		ShipBean shipBean = scheduleBean.getShip();
		System.out.println(seats);
		if(flag){
			shipBean.setSeatingCapacity(shipBean.getReservationCapacity());
			session.save(shipBean);
		}
		return shipBean.getReservationCapacity() - seats;
	}

	@Override
	public boolean checkFor7DaysSchedule(String shipID, Date startDate) {
		Session session = this.sessionFactory.getCurrentSession();
		Date plus6 = new Date(startDate.getTime() + (1000 * 60 * 60 * 24 * 6));
		Date minus6 = new Date(startDate.getTime() - (1000 * 60 * 60 * 24 * 6));
		Query query = session.createQuery("from ScheduleBean where trunc(startDate) > trunc(?) and trunc(startDate) < trunc(?) and shipID = ?");
		query.setParameter(0, minus6);
		query.setParameter(1, plus6);
		query.setParameter(2, shipID);
		@SuppressWarnings("unchecked")
		ArrayList<ScheduleBean> schedules = (ArrayList<ScheduleBean>) query.list();
		if(schedules.size() == 0){
			return true;
		}else{
			return false;
		}
		
	}
}