package com.wipro.srs.bean;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "SRS_TBL_Reservation")
public class ReservationBean {
	@Id
	private String reservationID;
	private String scheduleID;
	private String userID;
	@DateTimeFormat(pattern = "dd-MMM-yyyy")
	private Date bookingDate;
	@DateTimeFormat(pattern = "dd-MMM-yyyy")
	private Date journeyDate;
	private int noOfSeats;
	private double totalFare;
	private String bookingStatus;
	@ManyToOne
	@JoinColumn(name="userID", insertable = false, updatable = false)
	private CredentialsBean user;
	@OneToMany(mappedBy="reservation",cascade = CascadeType.ALL,fetch=FetchType.EAGER)
	private List<PassengerBean> passengers;
	
	
	public List<PassengerBean> getPassengers() {
		return passengers;
	}
	public void setPassengers(List<PassengerBean> passengers) {
		this.passengers = passengers;
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
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public Date getBookingDate() {
		return bookingDate;
	}
	public void setBookingDate(Date bookingDate) {
		this.bookingDate = bookingDate;
	}
	public Date getJourneyDate() {
		return journeyDate;
	}
	public void setJourneyDate(Date journeyDate) {
		this.journeyDate = journeyDate;
	}
	public int getNoOfSeats() {
		return noOfSeats;
	}
	public void setNoOfSeats(int noOfSeats) {
		this.noOfSeats = noOfSeats;
	}
	public double getTotalFare() {
		return totalFare;
	}
	public void setTotalFare(double totalFare) {
		this.totalFare = totalFare;
	}
	public String getBookingStatus() {
		return bookingStatus;
	}
	public void setBookingStatus(String bookingStatus) {
		this.bookingStatus = bookingStatus;
	}
}
