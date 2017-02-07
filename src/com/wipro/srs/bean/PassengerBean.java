package com.wipro.srs.bean;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "SRS_TBL_passenger")
public class PassengerBean {
	@Id
	private String passengerID;
	private String reservationID;
	private String scheduleID;
	private String name;
	private int age;
	private String gender;
	@ManyToOne(cascade=CascadeType.ALL)
    @JoinColumn(name="scheduleID",insertable = false, updatable = false)
	private ScheduleBean schedule;
	@ManyToOne(cascade=CascadeType.ALL)
    @JoinColumn(name="reservationID",insertable = false, updatable = false)
	private ReservationBean reservation;
	
	public String getPassengerID() {
		return passengerID;
	}
	public void setPassengerID(String passengerID) {
		this.passengerID = passengerID;
	}
	public ScheduleBean getSchedule() {
		return schedule;
	}
	public void setSchedule(ScheduleBean schedule) {
		this.schedule = schedule;
	}
	public ReservationBean getReservation() {
		return reservation;
	}
	public void setReservation(ReservationBean reservation) {
		this.reservation = reservation;
	}
	public String getReservationID() {
		return reservationID;
	}
	public void setReservationID(String reservationID) {
		this.reservationID = reservationID;
	}
	public String getScheduleID() {
		return scheduleID;
	}
	public void setScheduleID(String scheduleID) {
		this.scheduleID = scheduleID;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	
	
}
