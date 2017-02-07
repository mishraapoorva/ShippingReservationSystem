package com.wipro.srs.bean;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;


@Entity
@Table(name = "SRS_TBL_Schedule")
public class ScheduleBean {
	
	@Id
	private String scheduleID;
	private String shipID;
	private String routeID;
	@DateTimeFormat(pattern = "dd-MMM-yyyy")
	private Date startDate;
	@ManyToOne
    @JoinColumn(name="shipID", insertable = false, updatable = false)
    private ShipBean ship;
	@ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="routeID",insertable = false, updatable = false)
    private RouteBean route;

	public RouteBean getRoute() {
		return route;
	}
	public void setRoute(RouteBean route) {
		this.route = route;
	}
	public ShipBean getShip() {
		return ship;
	}
	public void setShip(ShipBean ship) {
		this.ship = ship;
	}
	public String getScheduleID() {
		return scheduleID;
	}
	public void setScheduleID(String scheduleID) {
		this.scheduleID = scheduleID;
	}
	public String getShipID() {
		return shipID;
	}
	public void setShipID(String shipID) {
		this.shipID = shipID;
	}
	public String getRouteID() {
		return routeID;
	}
	public void setRouteID(String routeID) {
		this.routeID = routeID;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}	
}
