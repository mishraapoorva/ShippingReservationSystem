package com.wipro.srs.bean;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;


@Entity
@Table(name = "SRS_TBL_User_Credentials")
public class CredentialsBean {
	@Id
	private String userID;
	private String password;
	private String userType;
	private int loginStatus;
	@OneToOne(mappedBy="credentialsBean",cascade=CascadeType.ALL,fetch=FetchType.EAGER)
	private ProfileBean profileBean;
	@OneToMany
	private Set<ReservationBean> reservations;
	
	public ProfileBean getProfileBean() {
		return profileBean;
	}
	public void setProfileBean(ProfileBean profileBean) {
		this.profileBean = profileBean;
	}
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	public int getLoginStatus() {
		return loginStatus;
	}
	public void setLoginStatus(int loginStatus) {
		this.loginStatus = loginStatus;
	}	
}
