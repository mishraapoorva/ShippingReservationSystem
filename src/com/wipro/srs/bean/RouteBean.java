package com.wipro.srs.bean;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;


@Entity
@Table(name = "SRS_TBL_Route")
public class RouteBean {
	@Id
	private String routeID;
	private String source;
	private String destination;
	private String travelDuration;
	private double fare;
	@OneToMany(mappedBy="route", cascade=CascadeType.ALL)
	private Set<ScheduleBean> schedules;
	
	public Set<ScheduleBean> getSchedules() {
		return schedules;
	}
	public void setSchedules(Set<ScheduleBean> schedules) {
		this.schedules = schedules;
	}
	public String getRouteID() {
		return routeID;
	}
	public void setRouteID(String routeID) {
		this.routeID = routeID;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getDestination() {
		return destination;
	}
	public void setDestination(String destination) {
		this.destination = destination;
	}
	public String getTravelDuration() {
		return travelDuration;
	}
	public void setTravelDuration(String travelDuration) {
		this.travelDuration = travelDuration;
	}
	public double getFare() {
		return fare;
	}
	public void setFare(double fare) {
		this.fare = fare;
	}
	
	
}
