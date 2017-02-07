package com.wipro.srs.service;

import java.util.ArrayList;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wipro.srs.bean.PassengerBean;
import com.wipro.srs.bean.PaymentBean;
import com.wipro.srs.bean.RouteBean;
import com.wipro.srs.bean.ScheduleBean;
import com.wipro.srs.bean.ShipBean;
import com.wipro.srs.dao.Ship;

@Service
public class AdministratorImplementation implements Administrator {
	
	@Autowired
	private Ship shipDAO;	
	@Autowired
	private com.wipro.srs.dao.Customer customerDAO;
	
	@Override
	@Transactional
	public String addShip(ShipBean shipbean) {
		return this.shipDAO.addShip(shipbean);
	}

	@Override
	@Transactional
	public boolean modifyShip(ShipBean Shipbean) {
		
		return false;
	}

	@Override
	@Transactional
	public int removeShip(ArrayList<String> ShipId) {		
		return this.shipDAO.removeShip(ShipId); 
	}

	
	
	@Override
	@Transactional
	public String addSchedule(ScheduleBean schedulebean) {
		return this.shipDAO.addSchedule(schedulebean);
	}

	@Override
	@Transactional
	public boolean modifySchedule(ScheduleBean schedulebean) {
		return false;
	}

	@Override
	@Transactional
	public int removeSchedule(ArrayList<String> scheduleid) {
		return this.shipDAO.removeSchedule(scheduleid);
	}

	@Override
	@Transactional
	public String addRoute(RouteBean routebean) {
		return this.shipDAO.addRoute(routebean);
	}

	@Override
	@Transactional
	public boolean modifyRoute(RouteBean routebean) {
		return false;
	}

	@Override
	@Transactional
	public int removeRoute(String routeid) {
		return 0;
	}

	@Override
	@Transactional
	public ShipBean viewByShipId(String ShipId) {
		return this.shipDAO.viewByShipId(ShipId);
	}

	@Override
	@Transactional
	public RouteBean viewByRouteId(String routeid) {
		return null;
	}

	@Override
	@Transactional
	public ArrayList<ShipBean> viewByAllShips() {
		return this.shipDAO.viewByAllShips();
	}

	@Override
	@Transactional
	public ArrayList<RouteBean> viewByAllRoute() {
		return this.shipDAO.viewByAllRoute();
	}

	@Override
	@Transactional
	public ArrayList<ScheduleBean> viewByAllSchedule() {
		return this.shipDAO.viewByAllSchedule();
	}

	@Override
	@Transactional
	public ScheduleBean viewByScheduleId(String scheduleid) {
		return this.shipDAO.viewByScheduleId(scheduleid);
	}

	@Override
	@Transactional
	public ArrayList<PassengerBean> viewPasengersByShip(String scheduleid) {
		return this.shipDAO.viewPasengersByShip(scheduleid);
	}

	@Override
	@Transactional
	public int reduceShipCapacity(String scheduleID, Date travelDate, boolean flag) {
		return this.shipDAO.reduceShipCapacity(scheduleID, travelDate, flag); 
	}

	@Override
	@Transactional
	public boolean saveCard(PaymentBean paymentBean) {
		return this.customerDAO.saveCard(paymentBean);
	}

	@Override
	@Transactional
	public boolean deductFare(PaymentBean paymentBean, double fare) {
		return this.customerDAO.deductFare(paymentBean, fare);
	}

	@Override
	@Transactional
	public boolean checkFor7DaysSchedule(String shipID, Date startDate) {
		return this.shipDAO.checkFor7DaysSchedule(shipID,startDate);
	}
}