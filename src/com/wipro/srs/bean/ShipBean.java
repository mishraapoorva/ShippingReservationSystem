package com.wipro.srs.bean;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "SRS_TBL_Ship")
public class ShipBean {
	@Id
	private String shipID;
	private String shipName;
	private int seatingCapacity;
	private int reservationCapacity;
	@OneToMany(mappedBy="ship", fetch=FetchType.EAGER)
	private Set<ScheduleBean> schedules;
	
	public Set<ScheduleBean> getSchedules() {
		return schedules;
	}
	public void setSchedules(Set<ScheduleBean> schedules) {
		this.schedules = schedules;
	}
	public String getShipID() {
		return shipID;
	}
	public void setShipID(String shipID) {
		this.shipID = shipID;
	}
	public String getShipName() {
		return shipName;
	}
	public void setShipName(String shipName) {
		this.shipName = shipName;
	}
	public int getSeatingCapacity() {
		return seatingCapacity;
	}
	public void setSeatingCapacity(int seatingCapacity) {
		this.seatingCapacity = seatingCapacity;
	}
	public int getReservationCapacity() {
		return reservationCapacity;
	}
	public void setReservationCapacity(int reservationCapacity) {
		this.reservationCapacity = reservationCapacity;
	}
}
