package com.wipro.srs.dao;

import java.util.ArrayList;
import java.util.Date;

import com.wipro.srs.bean.PassengerBean;
import com.wipro.srs.bean.RouteBean;
import com.wipro.srs.bean.ScheduleBean;
import com.wipro.srs.bean.ShipBean;

public interface Ship {
	String addShip(ShipBean shipbean);
	int removeShip(ArrayList<String> ShipId);
	String addSchedule(ScheduleBean schedulebean);
	boolean modifySchedule(ScheduleBean schedulebean);
	int removeSchedule(ArrayList<String> scheduleid);
	String addRoute(RouteBean routebean);
	boolean modifyRoute(RouteBean routebean);
	int removeRoute(String routeid);
	ShipBean viewByShipId(String ShipId);
	RouteBean viewByRouteId(String routeid);
	ArrayList<ShipBean> viewByAllShips();
	ArrayList<RouteBean> viewByAllRoute();
	ArrayList<ScheduleBean> viewByAllSchedule();
	ScheduleBean viewByScheduleId(String scheduleid);
	ArrayList<PassengerBean> viewPasengersByShip(String scheduleid);
	public int reduceShipCapacity(String scheduleID, Date travelDate, boolean flag) ;
	boolean checkFor7DaysSchedule(String shipID, Date startDate);
}
